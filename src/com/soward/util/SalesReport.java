package com.soward.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.StringUtils;

import com.soward.db.MySQL;
import com.soward.enums.LocationsDBName;
import com.soward.object.Account;
import com.soward.object.Invoice;
import com.soward.object.Product;
import com.soward.object.Supplier;
import com.soward.object.Transaction;

public class SalesReport {
    private static SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm");
    private static SimpleDateFormat yyy = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public HashMap<String, ArrayList<ArrayList<String>>> getQtySales( String startDate, String endDate, String accountNumLocation ) {
        HashMap<String, ArrayList<ArrayList<String>>> soldList = new HashMap<String, ArrayList<ArrayList<String>>>();

        //hashmap supplierName
        //       -- ArrayList
        //                  -- ArrayList: count, prodNum
        //                  -- count, prodNum
        //                  -- count, prodNum
        Connection con = null;
        MySQL sdb = new MySQL();
        if(startDate==null||endDate==null||accountNumLocation==null){
            return null;
        }
        String productLocation = "Products";
        String sql = "";
        String sqlLocationWhere = "";
        //set murray variables
        if(accountNumLocation.equalsIgnoreCase( "101" )){
            sqlLocationWhere= " accountNum = "+accountNumLocation;//+ " or locationNum in (7,5,9,8,3,6,4))";
        }
        //set lehi variables
        if(accountNumLocation.equalsIgnoreCase( "102" )){
            sqlLocationWhere= " accountNum = "+accountNumLocation;//+ " or locationNum in (0))";
        }
        //set lehi variables
        if(accountNumLocation.equalsIgnoreCase( "103" )){
            sqlLocationWhere= " accountNum = "+accountNumLocation;
        }
        //set online variables
        if(accountNumLocation.equalsIgnoreCase( "104" )){
            //sqlLocationWhere= " locationNum in (99))";
        }
        //set PO variables
        if(accountNumLocation.equalsIgnoreCase( "104" )){
        }
        sql = "select  count(productQty) prodCount, prod.productName prodName, supplierName, prod.productSKU sku from Transactions, "+productLocation+" prod, Suppliers " +
                "where invoiceNum in " +
                "(select invoiceNum from Invoices where ("+sqlLocationWhere+") " +
                "and Transactions.productNum = prod.productNum " +
                "and productSupplier1 = Suppliers.supplierNum " +
                "and transDate>'" + startDate +"' "+
                "and transDate< '"+endDate+"' group by prod.productName, supplierName, prod.productSKU";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            int count = 0;
            // only get the next 20 entries
            while ( rset.next() ) {
                ArrayList<String> row = new ArrayList<String>();
                String prodCount = ( rset.getString( "prodCount" ) );
                String supplierName = ( rset.getString( "supplierName" ) );
                String prodName = ( rset.getString( "prodName" ) );
                String sku = ( rset.getString( "sku" ) );
                row.add( prodCount );
                row.add( prodName );
                row.add( sku );
                //row.add( supplierName );
                if(soldList.get( supplierName )!=null){
                    soldList.get( supplierName ).add( row );
                }else{
                    ArrayList al = new ArrayList();
                    al.add( row );
                    soldList.put( supplierName, al );
                }
                count++;
            }
            pstmt.close();
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return soldList;
    }


    //get list of invoices for date range and location
    public static HashMap<String, Invoice> getTotalForInvoices(String startDated, String endDated, boolean shippingOnly){
        Connection con = null;
        MySQL sdb = new MySQL();
        HashMap<String, Invoice> taxHash = new HashMap<String, Invoice>();
        //"04-14-2009 00:00"

        String startDate = null;
        String endDate = null;
        try {
            Date date = df.parse(startDated);
            Date eDate = df.parse(endDated);
            startDate = yyy.format(date);
            endDate = yyy.format(eDate);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(startDate==null||endDate==null){
            return taxHash;
        }
        String sql = "select invoiceNum, invoiceTotal, invoiceTax from Invoices iv where  invoiceDate>'"+startDate+"' and invoiceDate< '"+endDate+"'";
        if(shippingOnly){
            sql = "select invoiceNum, transCost as invoiceTotal, 0.00 as invoiceTax from Transactions where transDate>'"+startDate+"' and transDate< '"+endDate+"'  and productNum in (313,314,104749)";
        }
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            // only get the next 20 entries
            while ( rset.next() ) {
                String invoiceNum = rset.getString( "invoiceNum" );
                BigDecimal invoiceTotal = rset.getBigDecimal( "invoiceTotal" );
                BigDecimal invoiceTax = rset.getBigDecimal( "invoiceTax" );
                Invoice inv = new Invoice();
                inv.setInvoiceTotalBD( invoiceTotal );
                inv.setInvoiceTotalTaxBD( invoiceTax );
                taxHash.put( invoiceNum, inv );
            }
            pstmt.close();
            rset.close();
            con.close();
        } catch ( SQLException e ) {
            e.printStackTrace();
        }


        return taxHash;
    }

    public List<Product> getSalesReport( String startDateMMFirst, String endDateMMFirst, String locationName,
                                         int sortByType, String regLocation, boolean consolidatedReport, boolean shippingOnly,
                                         boolean pastYearsSoldBool, String department) {
        List<Product> prodList = new ArrayList<Product>();
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "";
        String sqlLocationWhere = "";
        //set murray variables
        Date date = null;
        Date changeDate = null;
        boolean whereSet = false;
        Map<String, String> orderTypeHash = getOrderTypeHash();
        Map<String, String> paymentTypeHash = getPaymentTypeHash();

        String startDate = null;
        String endDate = null;
        try {
            date = df.parse(startDateMMFirst);
            Date eDate = df.parse(endDateMMFirst);
            startDate = yyy.format(date);
            endDate = yyy.format(eDate);
            //this is the date of when I added new db table and code to track location
            changeDate = df.parse("04-14-2009 19:00");
            Calendar startCal = Calendar.getInstance();
            Calendar codeChangeCal = Calendar.getInstance();
            startCal.setTime( date );
            codeChangeCal.setTime( changeDate );
            if(startCal.after( codeChangeCal )){
                whereSet = true;
                sqlLocationWhere = "inv.invoiceNum in (select invoiceNum from InvoiceLocation \n" +
                        "where location='"+locationName+"' and invDate>'" + startDate +"' and invDate< '" + endDate +"')\n";
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        String regLoca = "";
        if(regLocation!=null&&regLocation.length()>0){
            regLoca = "and Transactions.locationNum  = "+regLocation+"  \n";
        }
        LocationsDBName locNm = LocationsDBName.valueOf( locationName );
        if(!whereSet){
            if(locNm.name().equalsIgnoreCase( "MURRAY" )){
                sqlLocationWhere= " accountNum = "+locNm.account();//+ " or Transactions.locationNum in (7,5,9,8,3,6,4)";
            }
            //set lehi variables
            else if(locNm.name().equalsIgnoreCase( "LEHI" )){
                sqlLocationWhere= " accountNum = "+locNm.account();//+ " or Transactions.locationNum in (0)";
            }
            //set online variables
            else if(locNm.name().equalsIgnoreCase( "ONLINE" )){
                sqlLocationWhere= " locationNum in (99)";
            }
            //set Orem variables
            else if(locNm.name().equalsIgnoreCase( "OREM" )){
                sqlLocationWhere= " accountNum = "+locNm.account();
            }
        }

        String selectValues = " trans.locationNum, trans.productNum, trans.transType transType, trans.productName   ,\n"+
                " productCatalogNum, productSKU           , productDescription   , numAvailable          , productQty    ,\n" +
                " trans.invoiceNum           , transDate            , supplierName          ,\n"+
                " supplierPhone , supplierNum, inv.accountNum, paymentMethod1,  " +
                " wpi.productFeature productFeature \n";

        sql = "select "+selectValues+" " +
                " from Transactions trans \n" +
                "    join Invoices inv            on trans.invoiceNum = inv.invoiceNum\n" +
                "    join InvoiceLocation invloc  on inv.invoiceNum=invloc.invoiceNum\n" +
                "    join Products prod           on trans.productNum = prod.productNum\n" +
                "    join Suppliers sup           on productSupplier1 = sup.supplierNum\n" +
                "    left join WebProductInfo wpi on wpi.productNum = prod.productNum "+
                "where "+sqlLocationWhere+" " + regLoca ;

        if(!StringUtils.isBlank(department) && !department.equals("0")){
            sql += "and category like '" + department + "%' \n";
        }

        if(shippingOnly){
            sql += "\nand  prod.productNum in (313, 314, 104749)                                                 ";

        }

        sql +="and transDate>'" + startDate +"' \n"+
                "and transDate< '"+endDate+"'";
        if (sortByType == 3){
            sql += " order by productFeature desc";
        }else{
            sql += " order by supplierName";
        }

        if(consolidatedReport){
            sql  = "select prod.productNum, prod.productName   ,                                             ";
            sql += "\n productCatalogNum, productSKU           , productDescription, sum(productQty) productQty   , ";
            sql += "\n supplierName          ,                                                               ";
            sql += "\n supplierPhone , supplierNum,  wpi.productFeature productFeature  ";

            sql +=  " from Transactions trans \n" +
                    "    join Invoices inv            on trans.invoiceNum = inv.invoiceNum\n" +
                    "    join InvoiceLocation invloc  on inv.invoiceNum=invloc.invoiceNum\n" +
                    "    join Products prod           on trans.productNum = prod.productNum\n" +
                    "    join Suppliers sup           on productSupplier1 = sup.supplierNum\n" +
                    "    left join WebProductInfo wpi on wpi.productNum = prod.productNum ";
            if(locNm != null && locNm.equals(LocationsDBName.DV)){
                sql += "\nwhere category like 'V%' and productCatalogNum like 'A%'";
            }else{
                sql += "\nwhere invloc.location='"+locationName+"'                                                 ";
            }
            sql += "\nand invloc.invDate>'"+startDate+"'                                                     ";
            sql += "\nand invloc.invDate< '"+endDate+"'                                                      ";
            sql += "\nand transDate>'"+startDate+"'                                                          ";
            sql += "\nand transDate< '"+endDate+"'                                                           ";

            if(shippingOnly){
                sql += "\nand  prod.productNum in (313, 314, 104749)                                                 ";
            }

            sql += "\ngroup by prod.productNum, prod.productName   ,                                         ";
            sql += "\nproductCatalogNum, productSKU           , productDescription   ,                       ";
            sql += "\nsupplierName          ,                                                                ";
            sql += "\nsupplierPhone , supplierNum, wpi.productNum                                            ";
            if(locNm != null && locNm.equals(LocationsDBName.DV)){
                sql += " order by productArranger";
            }else if (sortByType == 3){
                sql += " order by productFeature desc";
            }else{
                sql += " order by supplierName";
            }
        }
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            int count = 0;
            // only get the next 20 entries
            while ( rset.next() ) {
                Product prod = new Product();
                Supplier sup = new Supplier();
                Transaction trans = new Transaction();
                prod.setProductCatalogNum( rset.getString( "productCatalogNum" ) );
                prod.setProductSKU( rset.getString( "productSKU" ) );
                prod.setProductName( rset.getString( "productName" ) );
                prod.setProductDescription( rset.getString( "productDescription" ) );
                prod.setProductDescription( prod.getProductDescription()==null?"":prod.getProductDescription() );
                //prod.setNumAvailable( rset.getString( "numAvailable" ) );
                prod.setProductNum( rset.getString( "productNum" ) );
                prod.setLocation(StringUtils.isBlank(rset.getString( "productFeature"))?"":rset.getString( "productFeature"));

                if(sortByType==1&&!consolidatedReport){
                    String orderCode = rset.getString("transType");
                    String orderTypeName = orderTypeHash.get( orderCode );
                    sup.setSupplierName       (orderTypeName);
                    sup.setSupplierPhone      (orderCode);
                    sup.setSupplierNum        (orderCode+orderTypeName);
                }else if(sortByType==2&&!consolidatedReport){
                    String paymentType = rset.getString("paymentMethod1");
                    String paymentTypeName = paymentTypeHash.get(paymentType);
                    sup.setSupplierName(paymentTypeName);
                    sup.setSupplierPhone(paymentType);
                    sup.setSupplierNum(paymentType+paymentTypeName);
                }else{
                    sup.setSupplierName       (rset.getString("supplierName"));
                    sup.setSupplierPhone      (rset.getString("supplierPhone"));
                    sup.setSupplierNum        (rset.getString("supplierNum"));
                }
                trans.setProductQty( rset.getString("productQty") );
                if(!consolidatedReport){
                    trans.setInvoiceNum( rset.getString( "invoiceNum" ) );
                    trans.setLocationNum( rset.getString( "locationNum" ) );
                    trans.setTransDate( rset.getString( "transDate" ) );
                    Account acct = new Account();
                    acct.setAccountNum( rset.getString( "accountNum" ) );
                    trans.setAccount( acct );
                }
                prod.setTransaction(trans);
                prod.setSupplier(sup);
                prodList.add( prod );
                count++;
            }
            pstmt.close();
            rset.close();
            con.close();
        } catch ( Exception e ) {
            System.out.println("SQL: "+sql);
            e.printStackTrace();
        }
        if(pastYearsSoldBool){
            prodList = getYearsSold(locationName, prodList);
        }
        if(consolidatedReport){
            prodList = consolidateProducts(prodList);
        }
        else if(sortByType!=0){
            return sortByOrderType(prodList);
        }
        return prodList;
    }
    HashMap<String, String> map1 = new HashMap<String, String>();
    HashMap<String, String> map6 = new HashMap<String, String>();
    HashMap<String, String> map12 = new HashMap<String, String>();

    private List<Product> getYearsSold(String loca, List<Product> prodList) {
        String lList = "";
        for(Product p: prodList){
            if(!StringUtils.isBlank(lList)){
                lList += ",";
            }
            lList += p.getProductNum();
            if(lList.length()>200){
                getSales(loca, lList);
                lList = "";
            }
        }
        if(lList.length()>0){
            getSales(loca, lList);
        }
        for(Product p: prodList){
            if(map1.containsKey(p.getProductNum())){
                String pp = map1.get(p.getProductNum());
                p.setOneMonthSold(pp);
            }
            if(map6.containsKey(p.getProductNum())){
                String pp = map6.get(p.getProductNum());
                p.setSixMonthsSold(pp);
            }
            if(map12.containsKey(p.getProductNum())){
                String pp = map12.get(p.getProductNum());
                p.setYearsSold(pp);
            }
        }
        return prodList;
    }

    private void getSales(String loca, String lList) {
        //today
        Calendar m00 = Calendar.getInstance();
        m00.add(Calendar.HOUR, 12);
        //1 months back
        Calendar m01 = Calendar.getInstance();
        m01.add(Calendar.MONTH, -1);
        //6 months back
        Calendar m06 = Calendar.getInstance();
        m06.add(Calendar.MONTH, -6);
        //year ago
        Calendar m12 = Calendar.getInstance();
        m12.add(Calendar.MONTH, -12);
        Connection con = null;

        try {
            MySQL sdb = new MySQL();
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            ResultSet rset = null;

            String sql = "select sum(Transactions.productQty) as ccc, productNum "+
                    " from Transactions, InvoiceLocation invloc"+
                    " where invloc.location='"+loca+"'"+
                    " and Transactions.invoiceNum = invloc.invoiceNum"+
                    " and productNum in("+lList+")" +
                    " and transDate> '"+TransUtil.sdf.format(m01.getTime())+"'"+
                    " and transDate< '"+TransUtil.sdf.format(m00.getTime())+"'"+
                    " group by productNum ";

            pstmt = con.prepareStatement( sql );
            rset = pstmt.executeQuery();
            while ( rset.next() ) {
                map1.put(rset.getString("productNum"), rset.getString("ccc"));
            }
            // ======================= 1month ---> 6months

            sql = "select sum(Transactions.productQty) as ccc, productNum "+
                    " from Transactions, InvoiceLocation invloc"+
                    " where invloc.location='"+loca+"'"+
                    " and Transactions.invoiceNum = invloc.invoiceNum"+
                    " and productNum in("+lList+")" +
                    " and transDate> '"+TransUtil.sdf.format(m06.getTime())+"'"+
                    " and transDate< '"+TransUtil.sdf.format(m00.getTime())+"'"+
                    " group by productNum ";

            pstmt = con.prepareStatement( sql );
            rset = pstmt.executeQuery();
            while ( rset.next() ) {
                int n = 0;
                try{n = Integer.parseInt(rset.getString("ccc"));}catch(Exception e){}
                map6.put(rset.getString("productNum"), n+"");
            }
            // ======================= 6month ---> 12months
            sql = "select sum(Transactions.productQty) as ccc, productNum "+
                    " from Transactions, InvoiceLocation invloc"+
                    " where invloc.location='"+loca+"'"+
                    " and Transactions.invoiceNum = invloc.invoiceNum"+
                    " and productNum in("+lList+")" +
                    " and transDate> '"+TransUtil.sdf.format(m12.getTime())+"'"+
                    " and transDate< '"+TransUtil.sdf.format(m00.getTime())+"'"+
                    " group by productNum ";
            pstmt = con.prepareStatement( sql );
            rset = pstmt.executeQuery();
            while ( rset.next() ) {
                int n = 0;
                try{n = Integer.parseInt(rset.getString("ccc"));}catch(Exception e){}
                map12.put(rset.getString("productNum"), n+"");
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }


    private List<Product> consolidateProducts(List<Product> prodList) {
        // TODO Auto-generated method stub
        return prodList;
    }
    /**
     * This list comes in with orderCode+orderTypeName as the suplierName
     * @param prodList
     * @return
     */
    public static List<Product> sortByOrderType(List<Product> prodList){
        HashMap<String, ArrayList<Product>> prodHash = new HashMap<String, ArrayList<Product>>();
        List<Product> ordered = new ArrayList<Product>();
        for(Product prod: prodList){
            String key = prod.getSupplier().getSupplierNum();
            if(prodHash.containsKey( key )){
                prodHash.get( key ).add( prod );
            }else{
                ArrayList<Product> prodListTemp = new ArrayList<Product>();
                prodListTemp.add( prod );
                prodHash.put( key, prodListTemp );
            }
        }
        Set set = prodHash.keySet();
        Iterator<String> iter = set.iterator();
        while(iter.hasNext()){
            String key = iter.next();
            for(Product pp: prodHash.get( key )){
                ordered.add( pp );
            }
        }
        return ordered;
    }

    public static Map<String, String> getOrderTypeHash(){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OO","Online Order"    );
        map.put("SO","Special Order" );
        map.put("NS","Normal Sale" );
        map.put("HB","Hold Bin" );
        map.put("PO","Purchase Order" );
        map.put("DS","Drop Ship" );
        map.put("WO","Web Order" );
        map.put("DO","Daily Order" );
        return map;
    }
    public static Map<String, String> getPaymentTypeHash(){
        HashMap<String, String> map = new HashMap<String, String>();
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select  * from PaymentMethods";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            // only get the next 20 entries
            while ( rset.next() ) {
                String paymentMethod = rset.getString( "paymentMethod" );
                String paymentAccountType = rset.getString( "paymentAccountType" );
                map.put(paymentMethod, paymentAccountType);
            }
            pstmt.close();
            rset.close();
            con.close();
        } catch ( SQLException e ) {
            e.printStackTrace();
        }

        return map;
    }

    //
    //hashmap<prodNum, hashMap<locationName, qty>>
    /**
     * this method for the reports page, it is not needed to return the location inputed, just the 'others'
     * @return hashmap<prodNum, hashmap<locationName, locationQty>
     */
    @Deprecated
    public static HashMap<String, HashMap<String, String>> getLocationQty(List<Product> prodList, String location){
        HashMap<String, HashMap<String, String>> hm = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> currLoc = new HashMap<String, String>();
        for(LocationsDBName lname : LocationsDBName.values()){
            if(!"ONLINE".equals( lname.name() )&&!location.equals( lname.name() )){
                currLoc = SalesReport.getLocationsQTYForProdList( prodList, lname.name() );
                //iterate through this hashmap and add to global hashmap
                Set set = currLoc.keySet();
                Iterator iter = set.iterator();
                try {
                    while (iter.hasNext()){
                        String currProdNum = (String)iter.next();
                        String currQty = currLoc.get( currProdNum );
                        //if hm already has this prodnum, get hashmap out
                        if(hm.containsKey( currProdNum )){
                            HashMap<String, String> inner = hm.get( currProdNum );
                            //add new location, qty
                            inner.put( lname.name(), currQty );
                            hm.put( currProdNum, inner );
                        }else{
                            HashMap<String, String> inner = new HashMap<String, String>();
                            inner.put( lname.name(), currQty );
                            hm.put( currProdNum, inner );
                        }

                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }
        return hm;
    }


    //returns a map of productNum, of productAvailable based on list prodNums
    //
    public static HashMap<String, String> getLocationsQTYForProdList(List<Product> prodList, String location){
        String accountNumList = "";
        HashMap<String, String> hm = new HashMap<String, String>();
        int count = 0;
        for(Product pr: prodList){
            if(pr.getProductNum()!=null){
                if(accountNumList.length()>0){
                    accountNumList +=", ";
                }
                count++;
                accountNumList += pr.getProductNum();
                if(count>245){
                    Map map = getHashMap(accountNumList, location);
                    if(map!=null)
                        hm.putAll( map );
                    accountNumList = "";
                    count = 0;
                }
            }
        }
        //get remainder of count less than 245
        if(count>0){
            Map map = getHashMap(accountNumList, location);
            if(map!=null)
                hm.putAll( map );
            accountNumList = "";
            count = 0;
        }
        return hm;
    }
    private static HashMap<String, String> getHashMap(String str, String locationName){

        LocationsDBName locNm = LocationsDBName.valueOf( locationName );
        String sql = "select productNum, numAvailable from "+locNm.dbName()+" where productNum in ("+str+")";
        HashMap<String, String> hm = new HashMap<String, String>();
        try {
            Connection con = null;
            MySQL sdb = new MySQL();
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            while ( rset.next() ) {
                hm.put( rset.getString( "productNum" ), rset.getString( "numAvailable" ) );
            }
            pstmt.close();
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return hm;
    }

    public static void main( String args[] ) {
        SalesReport ir = new SalesReport();
//        ir.fetchPastThreeYearsSold(Arrays.asList(174893L), "LEHI");

//        List<Product> prodList = ir.getSalesReport( "12-07-2010 00:00", "12-07-2010 23:59", "LEHI", 2,"", true, false, true, null);
//        System.out.println(prodList.size());
//        for(Product prod: prodList){
//            System.out.println(prod.getNumAvailable()+"# -->"+prod.getProductNum()+" "+prod.getYearsSold());
//        }
        //      SalesReport ir = new SalesReport();
        //      ir.getTaxesForInvoices( "03-05-2009 00:00", "03-06-2009 00:00");
        //      Date date=null;
        //      try {
        //      SimpleDateFormat df=new SimpleDateFormat("MM-dd-yyyy HH:mm");
        //      date=df.parse("06-07-2008 23:59");
        //      }
        //      catch (Exception err) {
        //      }
        //      System.out.println ("Date: "+date); // will print the Date object
    }

}
