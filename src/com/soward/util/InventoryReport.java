package com.soward.util;

import com.soward.db.MySQL;
import com.soward.object.Departments;
import com.soward.object.Product;
import com.soward.object.gto.ProductGto;
import com.sybase.jdbc3.jdbc.SybSQLException;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InventoryReport {

    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.0");
    SimpleDateFormat sdf3 = new SimpleDateFormat("MM/dd/yyyy");

    //  set rowcount 20 select * from Products where lastInvDate >'2007-01-01' and numAvailable>0
    public List<Product> getInventoryReport( String startDate1, String endDate1, String invQtyStart, String invQtyEnd, ArrayList<Departments> depts ) {
        return getInventoryReport( startDate1,  endDate1,  invQtyStart,  invQtyEnd,  depts,  null);
    }
    public List<Product> getInventoryReport( String startDate1, String endDate1, String invQtyStart, String invQtyEnd, ArrayList<Departments> depts, String perDept ) {
        List<Product> prodList = new ArrayList<Product>();
        Connection con = null;
        MySQL sdb = new MySQL();
        String startDate = "";
        String endDate = "";
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.0");
        SimpleDateFormat sdf3 = new SimpleDateFormat("MM/dd/yyyy");
        if(perDept==null){
            try{
                endDate = sdf2.format(  sdf3.parse( endDate1 ));
                startDate = sdf2.format(  sdf3.parse( startDate1 ));
                //check if this is the same day
                if(endDate.equals( startDate )){
                    Calendar cal = Calendar.getInstance();
                    cal.setTime( sdf3.parse( startDate1 ) );
                    cal.add( Calendar.DAY_OF_MONTH, 1 );
                    endDate = sdf2.format( cal.getTime() );
                }
            }catch (Exception e){
                e.printStackTrace();
                return prodList;
            }
        }

        String sql = "";
        if(perDept!=null){
            sql = "select category, productCatalogNum, productName, productNum," +
                    "'-' as LEHI, '-' as MURRAY, '-' as totalInv," +
                    "productCost1 from Products p where  category like '" + perDept + "%'";
        }else{
            sql += "select lastInvDate, category, productCatalogNum, productName, plc.productNum, LEHI, MURRAY,(LEHI + MURRAY) as totalInv," +
                    "productCost1 from ProductsLocationCount plc, Products p where"+
                    " plc.productNum = p.productNum"+
                    " and lastInvDate >='" + startDate +
                    " ' and lastInvDate<='"+endDate+"' "+
                    " and (LEHI + MURRAY) >= "+invQtyStart+(!StringUtils.isBlank(invQtyEnd)?" and (LEHI + MURRAY) <= "+invQtyEnd:"");
        }
        if(depts!=null&&!depts.isEmpty()){
            sql += " and (";
            for(Departments ddd: depts){
                String department = ddd.getDepartmentCode();
                if (!StringUtils.isBlank( department ) ) {
                    sql += " category like '" + department + "%'     or";
                }
            }
            sql = sql.substring(0, sql.length()-4);
            sql += " )";
        }

        try {
//            System.out.println(sql);
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            // only get the next 20 entries
            while ( rset.next() ) {

                Product prod = new Product();
                prod.setProductNum        ( rset.getString( "productNum" ) );
                prod.setNumAvailable      ( rset.getString( "MURRAY" ) +" / "+rset.getString( "LEHI" ));
                prod.setProductCost4      ( rset.getString("totalInv") );
                prod.setProductName       ( rset.getString( "productName" ) );
                prod.setProductAuthor     ( "-");//rset.getString( "productAuthor" ) );
                prod.setProductArtist     ( "-");//rset.getString( "productArtist" ) );
                prod.setProductArranger   ( "-");//rset.getString( "productArranger" ) );
                prod.setProductDescription( "-");//rset.getString( "productDescription" ) );
                prod.setCategory          (rset.getString( "category" ) );
                prod.setProductCost1      ( rset.getString( "productCost1" ) );
                prod.setProductCost2      ( "-");//rset.getString( "productCost2" ) );
                prod.setProductCost3      ( "-");//rset.getString( "productCost3" ) );
                prod.setProductBarCode    ( "-");//rset.getString( "productBarCode" ) );
                prod.setProductSKU        ( "-");//rset.getString( "productSKU" ) );
                prod.setProductCatalogNum ( rset.getString( "productCatalogNum" ) );
                prod.setProductSupplier1  ( "-");//rset.getString( "productSupplier1" ) );
                prod.setProductSupplier2  ( "-");//rset.getString( "productSupplier2" ) );
                prod.setProductSupplier3  ( "-");//rset.getString( "productSupplier3" ) );
                prod.setProductSupplier4  ( "-");//rset.getString( "productSupplier4" ) );
                //prod.setLastSold          ( rset.getString( "lastSold" ) );
                //prod.setLastInvDate       ( rset.getString( "lastInvDate" ) );
                String d = getTotal( prod.getProductCost4(), prod.getProductCost1());
                prod.setDCCatalogNum      ( d!=null?d:"");
                prodList.add( prod );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return prodList;
    }
    private String getTotal(String prodTotalCount, String price){

        String  d = null;
        try{
            Double c = Double.parseDouble(price);
            Double t = Double.parseDouble(prodTotalCount);
            d = new java.text.DecimalFormat("$0.00").format(c * t);
        }catch(Exception e){}
        return d;
    }

    public List<ProductGto> getInventorySoldReport( String st, String ed, ArrayList<Departments> depts ) {
        if(StringUtils.isBlank( st ) || StringUtils.isBlank( ed )){
            return new ArrayList<ProductGto>();
        }

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        try {
            cal1.setTime(sdf3.parse(st));
            cal2.setTime(sdf3.parse(ed));
            //Have to add 1 day to the end so that we can INCLUDE end date, do transDate< endDate and include the end date.
            //But we do not have to MINUS one day from the begin date because transDate>beginDate INCLUDES begin date.
            cal2.add(Calendar.DAY_OF_YEAR, 1);
            ed = sdf3.format( cal2.getTime() );
            cal2.add(Calendar.DAY_OF_YEAR, -1);
        } catch ( ParseException e ) {
            e.printStackTrace();
        }

        List<Product> l = new ArrayList<Product>();
        //chunk off the process in 30 day increments
        long ll = Utils.getDaysDiff( cal1, cal2 );

        int var = 10;

        while(ll > var ){
            String d2 = sdf3.format( cal1.getTime());

            cal1.add( Calendar.DAY_OF_YEAR, var );
            String d3 = sdf3.format( cal1.getTime() );
            l.addAll( getISReport(d2, d3));
            ll -= var;
        }

        //        get the last few days < 30
        String d2 = sdf3.format( cal1.getTime() );
        l.addAll( getISReport(d2, ed));

        Map<String, Product> m = new HashMap<String, Product>();
        // because we are chunking the queries, we have combine the chunks
        // ie. the chunks or incremental fetches get the data in 10 day chucks
        // and even though the group by works for each individual query, the next query might return the same product number.
        for(Product p: l){
            String num = p.getProductNum();
            if(m.containsKey( num )){
                Product pro = m.get( num );
                String s1 = pro.getProductCost2();
                String s2 = p.getProductCost2();
                try{
                    int i1 = Integer.parseInt( s1 );
                    int i2 = Integer.parseInt( s2 );
                    p.setProductCost2( i1+i2+"" );
                    m.put( num, p );
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }

            }else{
                m.put( num, p );
            }
        }
        ArrayList<Product> condensedProductsMixed = new ArrayList<Product>();
        Set<String> s = m.keySet();
        Iterator<String> iter = s.iterator();
        while(iter.hasNext()){
            Product u = m.get( iter.next());
            int c = Integer.parseInt( u.getProductCost2());
            double d = Double.parseDouble( u.getProductCost4() );
            //u.setProductCost4( (c*d)+"" );
            condensedProductsMixed.add( u );

        }
        List<String> departments = new ArrayList<String>();

        if(depts!=null&&!depts.isEmpty()){
            for(Departments ddd: depts){
                String department = ddd.getDepartmentName();
                if (!StringUtils.isBlank( department ) ) {
                    departments.add(department);
                }
            }
        }

        List<Departments> allDepartments = DepartmentsUtil.getAllDepartments();
        HashMap deptHash = new HashMap<String, String>();
        for (Departments dept : allDepartments) {
            deptHash.put(dept.getDepartmentCode(), dept.getDepartmentName());
        }
        // sort list per department
        HashMap<String, ProductGto> perDept = new HashMap<String, ProductGto>();
        for(Product rod: condensedProductsMixed){
            String caa = rod.getCategory();
            String key = rod.getCategory().charAt(0)+"";
            String deptName = (String)deptHash.get(key);

            double mTot = Double.parseDouble(rod.getProductSupplier1());
            double lTot = Double.parseDouble(rod.getProductSupplier2());
            double oTot = Double.parseDouble(rod.getProductSupplier3());

            ProductGto gto = new ProductGto();
            if(perDept.containsKey(deptName)){
                gto = perDept.get(deptName);
            }
            gto.setName(deptName);
            gto.getProdList().add(rod);
            gto.setmTotal(mTot+gto.getmTotal());
            gto.setlTotal(lTot+gto.getlTotal());
            gto.setoTotal(oTot+gto.getoTotal());
            perDept.put(deptName, gto);

        }

        //Get selected depts
        ArrayList<ProductGto> deptList = new ArrayList<ProductGto>();
        Set<String> ss = perDept.keySet();
        Iterator<String> dNames = ss.iterator();
        while(dNames.hasNext()){
            ProductGto ggto = perDept.get(dNames.next());
            if(departments.contains(ggto.getName()))
                deptList.add(ggto);
        }

        return deptList;

    }
    private List<Product> getISReport( String startDate1, String endDate1){//}, ArrayList<Departments> depts ) {
        List<Product> prodList = new ArrayList<Product>();
        Connection con = null;
        MySQL sdb = new MySQL();
        String startDate = "";
        String endDate = "";

        /*
            The date in the db is in the form: yyyy-MM-dd and time,
            so ie. '2011-01-07' will never equal a date without adding the time.
            however '2011-01-07' can be used as  in < or > some date
        */

        SimpleDateFormat sdf12 = new SimpleDateFormat("yyyy-MM-dd");

        try{
            endDate = sdf12.format(  sdf3.parse( endDate1 ));
            startDate = sdf12.format(  sdf3.parse( startDate1 ));

        }catch (Exception e){
            e.printStackTrace();
            return prodList;
        }
        String sql = "";
        sql += "select count(prod.productNum) as qty, prod.productNum, prod.productName, MURRAY, LEHI, prod.productCost1, prod.productCatalogNum, category \n"+
                "from Transactions trans, Products prod, ProductsLocationCount plc  \n"+
                "where transDate>'" + startDate + "' and transDate< '"+endDate+"'  \n" +
                "and prod.productNum = trans.productNum and prod.productNum = plc.productNum  \n"+
                "and (LEHI > 0 or MURRAY > 0 )  \n";

        sql += " group by prod.productNum, prod.productName, MURRAY, LEHI, prod.productCost1, prod.productCatalogNum, category";;

        try {
            System.out.println(sql);
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            // only get the next 20 entries
            while ( rset.next() ) {

                Product prod = new Product();
                String ms = rset.getString( "MURRAY" );
                String ls = rset.getString( "LEHI" );
//                String os = rset.getString( "OREM" );

                int onShelf = ms!=null?(Integer.parseInt(ms)>0?Integer.parseInt(ms):0):0;
                onShelf += ls!=null?(Integer.parseInt(ls)>0?Integer.parseInt(ls):0):0;
//                onShelf += os!=null?(Integer.parseInt(os)>0?Integer.parseInt(os):0):0;


                String cost =  rset.getString( "productCost1" );
                double md = StringUtils.isBlank( ms )?0.0:Double.parseDouble( ms );
                double ld = StringUtils.isBlank( ls )?0.0:Double.parseDouble( ls );
//                double od = StringUtils.isBlank( os )?0.0:Double.parseDouble( os );
                double ct = StringUtils.isBlank( cost )?0.0:Double.parseDouble( cost );

                prod.setProductNum        ( rset.getString( "productNum" ) );
                prod.setNumAvailable      ( ms +" / "+ls );
                prod.setProductName       ( rset.getString( "productName" ) );
                //                prod.setProductAuthor     ( "-");//rset.getString( "productAuthor" ) );
                //                prod.setProductArtist     ( "-");//rset.getString( "productArtist" ) );
                //                prod.setProductArranger   ( "-");//rset.getString( "productArranger" ) );
                //                prod.setProductDescription( "-");//rset.getString( "productDescription" ) );
                prod.setCategory          (rset.getString( "category" ) );
                prod.setProductCost1      (cost );
                prod.setProductCost2      ( rset.getString( "qty" ) ); //TOTAL SOLD
                prod.setProductCost3      (onShelf+"");
                prod.setProductCost4      (( ( (md>0?md:0.0)+(ld>0?ld:0.0) )   *ct)+"" );
                //                prod.setProductBarCode    ( "-");//rset.getString( "productBarCode" ) );
                //                prod.setProductSKU        ( "-");//rset.getString( "productSKU" ) );
                prod.setProductCatalogNum ( rset.getString( "productCatalogNum" ) );
                prod.setProductSupplier1  ( ""+(md>0?(md*ct):0.0));//MURRAY ON SHELF
                prod.setProductSupplier2  ( ""+(ld>0?(ld*ct):0.0));//LEHI   ON SHELF
//                prod.setProductSupplier3  ( ""+(od>0?(od*ct):0.0));//OREM   ON SHELF
                //                prod.setProductSupplier4  ( "-");//rset.getString( "productSupplier4" ) );
                //                prod.setLastSold          ( "-");//rset.getString( "lastSold" ) );
                //                prod.setLastInvDate       ( "-");//rset.getString( "lastInvDate" ) );
                //                prod.setDCCatalogNum      ( "-");//rset.getString( "DCCatalogNum" ) );
                prodList.add( prod );
            }
        } catch ( SybSQLException e ) {
            System.out.println(e.getMessage());

        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return prodList;
    }


    public static void main( String args[] ) {
        InventoryReport ir = new InventoryReport();
        ArrayList<Departments> dlist = new ArrayList<Departments>();
        dlist.add(new Departments("1", "Sheet Music", "1"));
//        dlist.add(new Departments("2", "2", "2"));
        dlist.add(new Departments("V", "DAY VIOLINS", "V"));
//        List<ProductGto> prodList = ir.getInventorySoldReport( "01/08/2011", "01/11/2011", dlist );
        List<Product> prodList = ir.getInventoryReport( "2007-01-01", "2008-01-01", "50" , "200", null);
        //        List<Product> prodList = ir.getInventorySoldReport( "03/01/2011", "04/01/2011", null);
        System.out.println(prodList.size());
        for(Product dept: prodList){
//            System.out.println(dept.get());
//            System.out.println(dept.getmTotal());
//            System.out.println(dept.getlTotal());
//            System.out.println(dept.getoTotal());
//            System.out.println(dept.getTotalValue());
            for(Product prod: prodList){
                System.out.println(prod);
            }
        }
    }

}
