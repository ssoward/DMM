/**
 * @author Scott Soward
 * Date: May 12, 2007
 *
 */
package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.soward.db.MySQL;
import com.soward.enums.LocationsAccountNum;
import com.soward.enums.LocationsDBName;
import com.soward.object.Account;
import com.soward.object.Invoice;
import com.soward.object.Product;
import com.soward.object.Supplier;
import com.soward.object.Transaction;

public class TransUtil {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
    /*
      * get sum of all tax exempt trans in list
      */
    public double getTETransTotal(List<Transaction> transList) {
        double sum = 0.0;
        for(Transaction trans: transList){
            try{
                double temp = Double.parseDouble(trans.getTransTax());
                if(temp==0.0){
                    sum+=Double.parseDouble(trans.getTransCost());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return sum;
    }
    /*
      * get sum of all transaction in list.
      */
    public double getTransTotal(List<Transaction> transList) {
        double sum = 0.0;
        for(Transaction trans: transList){
            try{
                sum+=Double.parseDouble(trans.getTransCost());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return sum;
    }


    public static Boolean addTransactionToInvoice(String userName, String invoiceNum, String productNum){
        Connection con = null;
        MySQL sdb = new MySQL();

        Product prod = ProductUtils.fetchProductForNum(productNum, null);

        String sql = "insert into Transactions (transNum, invoiceNum, transType, productName, productNum, productQty, transCost, transTax, " +
                "transDate, locationNum, username, transShipCost, transShipped, transShipDate, transProductStatus) values " +
                "(null, ?, 'PO', ?, ?, 0, 0.00, 0.00, now(), " +
                "12, ?, 0.00, 0, null,'ORDERED')";;
        Boolean ok = true;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );

            pstmt.setString(1, invoiceNum);
            pstmt.setString(2, prod.getProductName());
            pstmt.setString(3, prod.getProductNum());
            pstmt.setString(4, userName);

            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        }catch(Exception e){
            ok = false;
            e.printStackTrace();
        }
        return ok;
    }

    public static Boolean updateTransactionQty(String transNum, String qty){
        Connection con = null;
        MySQL sdb = new MySQL();


        String sql = "update Transactions set productQty = ? where transNum = ?";;
        Boolean ok = true;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );

            pstmt.setString(1, qty);
            pstmt.setString(2, transNum);

            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        }catch(Exception e){
            ok = false;
            e.printStackTrace();
        }
        return ok;
    }


    // get trans from pid
    public static List<Transaction> getTransaction(String pid) {
        return getTransaction(pid, false);
    }
    public static List<Transaction> getTransaction(String pid, boolean getProds) {
        List<Transaction> transList = new ArrayList<Transaction>();
        Connection con = null;
        MySQL sdb = new MySQL();
        int pidNum = 0;
        if ( pid != null && pid.length() > 1 ) {
            try {
                pidNum = Integer.parseInt( pid );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            // first get the transactions in the holding bin.
            String sql = "select transNum, invoiceNum, transType, productNum, productName, "
                    + "productQty, transCost, transTax, transProductStatus, transDate, "
                    + "locationNum, username, transShipCost, transShipped, transShipDate "
                    + "from Transactions where invoiceNum="+pidNum+" group by transDate desc";
            // Transactions.transDate";

            try {
                con = sdb.getConn();
                PreparedStatement pstmt = null;
                pstmt = con.prepareStatement( sql );
                ResultSet rset = pstmt.executeQuery();
                transList = getForRSet(rset);
                rset.close();
                con.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            if(getProds){
                for(Transaction t: transList){
                    t.setProd(ProductUtils.fetchProductForNum(t.getProductNum(), "MURRAY"));
                }
            }
        }
        return transList;
    }
    public static List<Transaction> getTransactions(String pid, boolean getProds) {
        List<Transaction> transList = new ArrayList<Transaction>();
        Connection con = null;
        MySQL sdb = new MySQL();
        int pidNum = 0;
        if ( pid != null && pid.length() > 1 ) {
            try {
                pidNum = Integer.parseInt( pid );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            // first get the transactions in the holding bin.
            String sql = "select * from Transactions where invoiceNum="+pidNum;
            // Transactions.transDate";

            try {
                con = sdb.getConn();
                PreparedStatement pstmt = null;
                pstmt = con.prepareStatement( sql );
                ResultSet rset = pstmt.executeQuery();
                transList = getForRSet(rset);
                rset.close();
                con.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            if(getProds){
                for(Transaction t: transList){
                    t.setProd(ProductUtils.fetchProductForNum(t.getProductNum(), "MURRAY"));
                }
            }
        }
        return transList;
    }

    public static List<Transaction> getForRSet(ResultSet rset) throws SQLException {
        List<Transaction> transList = new ArrayList<Transaction>();
        while ( rset.next() ) {
            Transaction tran = new Transaction();

            try{tran.setTransNum( rset.getString( "transNum" ) );}catch(SQLException e){}
            try{tran.setInvoiceNum( rset.getString( "invoiceNum" ) );}catch(SQLException e){}
            try{tran.setTransType( rset.getString( "transType" ) );}catch(SQLException e){}
            try{tran.setProductNum( rset.getString( "productNum" ) );}catch(SQLException e){}
            try{tran.setProductName( rset.getString( "productName" ) );}catch(SQLException e){}
            try{tran.setProductQty( rset.getString( "productQty" ) );}catch(SQLException e){}
            try{tran.setTransCost( rset.getString( "transCost" ) );}catch(SQLException e){}
            try{tran.setTransTax( rset.getString( "transTax" ) );}catch(SQLException e){}
            try{tran.setTransProductStatus( rset.getString( "transProductStatus" ) );}catch(SQLException e){}
            try{tran.setTransDate( rset.getString( "transDate" ) );}catch(SQLException e){}
            try{tran.setLocationNum( rset.getString( "locationNum" ) );}catch(SQLException e){}
            try{tran.setUsername( rset.getString( "username" ) );}catch(SQLException e){}
            try{tran.setTransShipCost( rset.getString( "transShipCost" ) );}catch(SQLException e){}
            try{tran.setTransShipped( rset.getString( "transShipped" ) );}catch(SQLException e){}
            try{tran.setTransShipDate( rset.getString( "transShipDate" ) );}catch(SQLException e){}
            transList.add( tran );
        }
        return transList;
    }

    // get all the invoices that are in the Holding bin.
    public static HashMap getHBInvoices() {
        HashMap<String, ArrayList<Invoice>> buildInvoices = new HashMap<String, ArrayList<Invoice>>();
        Connection con = null;
        MySQL sdb = new MySQL();
        // first get the transactions in the holding bin.
        String sql = "select  invoiceNum, accountNum, invoiceDate, locationNum, username, "
                + "invoiceTotal, invoiceTax, invoiceShipTotal, invoicePaid, paymentMethod1, "
                + "paymentMethod2, invoicePaid1, invoicePaid2, invoiceReceivedBy, invoiceContactNum, "
                + "invoiceReferenceNum, invoiceChargeStatus, invoiceChargeDate, "
                + "invoiceChargePaymentMethod, invoiceDiscount  " + "from Invoices where invoiceNum in "
                + "(select invoiceNum from Transactions where transType='HB')";

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            int count = 0;
            while ( rset.next() ) {
                count++;
                Invoice inv = new Invoice();
                inv.setInvoiceNum( rset.getString( "invoiceNum" ) );
                inv.setAccountNum( rset.getString( "accountNum" ) );
                inv.setInvoiceDate( rset.getString( "invoiceDate" ) );
                inv.setLocationNum( rset.getString( "locationNum" ) );
                inv.setUsername2( rset.getString( "username" ) );
                inv.setInvoiceTotal( rset.getString( "invoiceTotal" ) );
                inv.setInvoiceTax( rset.getString( "invoiceTax" ) );
                inv.setInvoiceShipTotal( rset.getString( "invoiceShipTotal" ) );
                inv.setInvoicePaid( rset.getString( "invoicePaid" ) );
                inv.setPaymentMethod1( rset.getString( "paymentMethod1" ) );
                inv.setPaymentMethod2( rset.getString( "paymentMethod2" ) );
                inv.setInvoicePaid1( rset.getString( "invoicePaid1" ) );
                inv.setInvoicePaid2( rset.getString( "invoicePaid2" ) );
                inv.setInvoiceReceivedBy( rset.getString( "invoiceReceivedBy" ) );
                inv.setInvoiceContactNum( rset.getString( "invoiceContactNum" ) );
                inv.setInvoiceReferenceNum( rset.getString( "invoiceReferenceNum" ) );
                inv.setInvoiceChargeStatus( rset.getString( "invoiceChargeStatus" ) );
                inv.setInvoiceChargeDate( rset.getString( "invoiceChargeDate" ) );
                inv.setInvoiceChargePaymentMethod( rset.getString( "invoiceChargePaymentMethod" ) );
                inv.setInvoiceDiscount( rset.getString( "invoiceDiscount" ) );
                //check to see that the hash doesnt already have an entry with this
                // accountNum, if so add this inv to the arraylist, if not, create new
                // entry to hash with key=accountnum and value= new arraylist with
                // inv added to arraylist
                if(buildInvoices.containsKey( inv.getAccountNum() )){
                    buildInvoices.get( inv.getAccountNum() ).add( inv );
                }
                else{
                    ArrayList<Invoice> temp = new ArrayList<Invoice>();
                    temp.add( inv );
                    buildInvoices.put( inv.getAccountNum(), temp );

                }

            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return buildInvoices;
    }
    public static ArrayList<Invoice> getHBReport(String location){
        return addAccountsToHBInvoice(location);
    }

    // attach an account to each Holding bin invoice
    public static ArrayList<Invoice> addAccountsToHBInvoice(String location) {
        HashMap<String, ArrayList<Invoice>> emptyAccountInv = getHBInvoices();
        ArrayList<Invoice> hbInvoices = new ArrayList<Invoice>();
        Connection con = null;
        MySQL sdb = new MySQL();

        String sql = "select acct.accountNum, acct.accountName, acct.accountPassword, acct.accountContact, acct.accountEmail, acct.accountPhone1, acct.accountPhone2, acct.accountFax, acct.accountStreet, \n" +
                "       acct.accountCity, acct.accountState, acct.accountPostalCode, acct.accountCountry, acct.accountType1, acct.accountType2, acct.accountOpenDate, acct.accountCloseDate, acct.accountBalance \n" +
                "    from Accounts acct join Invoices inv\n" +
                "            on acct.accountNum = inv.accountNum\n" +
                "        join Transactions trans\n" +
                "            on trans.invoiceNum = inv.invoiceNum\n" +
                "        join InvoiceLocation il\n" +
                "            on il.invoiceNum = inv.invoiceNum\n" +
                "        where trans.transType = 'HB' and il.location = '"+location+"'  group by inv.accountNum";

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            int count = 0;
            while ( rset.next() ) {
                count++;
                Account cl = new Account();
                cl.setAccountNum( rset.getString("accountNum") );
                cl.setAccountName( rset.getString( "accountName" ) );
                cl.setAccountPassword( rset.getString( "accountPassword" ) );
                cl.setAccountContact( rset.getString( "accountContact" ) );
                cl.setAccountEmail( rset.getString( "accountEmail" ) );
                cl.setAccountPhone1( rset.getString( "accountPhone1" ) );
                cl.setAccountPhone2( rset.getString( "accountPhone2" ) );
                cl.setAccountFax( rset.getString( "accountFax" ) );
                cl.setAccountStreet( rset.getString( "accountStreet" ) );
                cl.setAccountCity( rset.getString( "accountCity" ) );
                cl.setAccountState( rset.getString( "accountState" ) );
                cl.setAccountPostalCode( rset.getString( "accountPostalCode" ) );
                cl.setAccountCountry( rset.getString( "accountCountry" ) );
                cl.setAccountType1( rset.getString( "accountType1" ) );
                cl.setAccountType2( rset.getString( "accountType2" ) );
                cl.setAccountOpenDate( rset.getString( "accountOpenDate" ) );
                cl.setAccountCloseDate( rset.getString( "accountCloseDate" ) );
                cl.setAccountBalance( rset.getString( "accountBalance" ) );
                //add account to invoice
                if(emptyAccountInv.containsKey( cl.getAccountNum() )){
                    ArrayList<Invoice> invList =  emptyAccountInv.get( cl.getAccountNum() );
                    for(Invoice tempInv: invList){
                        tempInv.setAccount( cl );
                        hbInvoices.add(tempInv );
                    }
                }
            }

            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return hbInvoices;
    }
    public static ArrayList<Transaction> getAllTransForProd(String prodNum){
        ArrayList<Transaction> transList = new ArrayList<Transaction>();
        Connection con = null;
        MySQL sdb = new MySQL();
        try {
            String  sql =
                    "select Transactions.invoiceNum, transType, Transactions.productQty, transDate from Transactions where"+
                            " Transactions.productNum=" + prodNum +
                            " order by transType, transDate desc";
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            //System.out.println(sql);
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            int count = 0;
            while ( rset.next() ) {
                count++;
                Transaction tran = new Transaction();
                tran.setInvoiceNum( rset.getString( "invoiceNum" ) );
                tran.setTransType( rset.getString( "transType" ) );
                tran.setProductQty( rset.getString( "productQty" ) );
                tran.setTransDate( rset.getString( "transDate" ) );
                transList.add( tran );
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return transList;

    }
    public static int getTransHistForProd(String prodNum, String loca){
        Connection con = null;
        MySQL sdb = new MySQL();
        int count = -1;
        if(StringUtils.isBlank(prodNum)){
            return count;
        }
        try {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal2.add(Calendar.HOUR, 12);
            cal1.add(Calendar.MONTH, -12);
            String  sql =
                    "select count(Transactions.productNum) as ccc, productNum "+
                            " from Transactions, InvoiceLocation invloc"+
                            " where invloc.location='"+loca+"'"+
                            " and Transactions.invoiceNum = invloc.invoiceNum"+
                            " and productNum=" + prodNum;
            sql += " and transDate>'"+sdf.format(cal1.getTime());
            sql += "' and transDate< '"+sdf.format(cal2.getTime())+"'";
            sql += " group by productNum, productNum";

            con = sdb.getConn();
            PreparedStatement pstmt = null;
            //System.out.println(sql);
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            if ( rset.next() ) {
                count = rset.getInt( "ccc" );
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return count;

    }
    /**
     * get hashmap <prodNum, count> of all holdbins for productNum
     * @param
     * @return
     */
    public static HashMap<String, String> getAllHBHash(){
        HashMap<String, String> transList = new HashMap<String, String>();
        Connection con = null;
        MySQL sdb = new MySQL();
        try {
            String  sql =
                    "select count(productQty) cnt, productNum from Transactions where transType = 'HB' group by productNum";
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            //System.out.println(sql);
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            while ( rset.next() ) {
                transList.put(rset.getString("productNum"), rset.getString("cnt"));
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return transList;

    }

    public static HashMap<String, Integer> getSalesFormat(ArrayList<Transaction> tList){
        HashMap<String, Integer> hh = new HashMap<String, Integer>();
        Calendar calNow = Calendar.getInstance();
        int currYear = calNow.get(Calendar.YEAR);
        hh.put(currYear+"", 0);
        hh.put((currYear-1)+"", 0);
        hh.put((currYear-2)+"", 0);
        hh.put((currYear-3)+"", 0);
        int total = 0;
        for(Transaction tt: tList){
            int countSold = 0;
            try{
                countSold = Integer.parseInt(tt.getProductQty());
                total += countSold;
            }catch(Exception e){
                //bad number
            }
            if(tt != null && tt.getTransYear() != null && hh.get(tt.getTransYear()) != null){
                hh.put(tt.getTransYear(), hh.get(tt.getTransYear()) + countSold);
            }
        }
        hh.put("Total", total);
        return hh;

    }
    public static ArrayList<Transaction> getTransListForProdNum( String prodNum, String loca ) {
        ArrayList<Transaction> transListNS = new ArrayList<Transaction>();
        ArrayList<Transaction> transList = new ArrayList<Transaction>();
        Connection con = null;
        MySQL sdb = new MySQL();
        try {
            //get the accountnum of the opposite location to exclude in sql
            String exclude = loca.equalsIgnoreCase( "LEHI" )?"102":"101";
            String noDS = "";
            //if not murray include location list
            if("OREM".equals( loca )){
                exclude = "103";
            }
            //			String  sql =
            //				"(select transNum, Transactions.invoiceNum, transType, Transactions.productQty, transDate from Transactions, Products prod, Invoices inv \n"+
            //				" where Transactions.invoiceNum = inv.invoiceNum and accountNum  = ("+exclude+")  and \n"+
            //				" Transactions.productNum=" + prodNum+
            //				" and prod.productNum=Transactions.productNum and transDate< '2009-05-01')";
            //			sql += " union \n";
            String sql = "select Transactions.transNum, Transactions.invoiceNum, Transactions.transType, \n"+
                    "Transactions.productQty, Transactions.transDate from Transactions, InvoiceLocation  \n"+
                    "where Transactions.invoiceNum = InvoiceLocation.invoiceNum and InvoiceLocation.location='"+loca+"' \n"+
                    "and productNum="+prodNum+" order by transType, transDate desc \n";
            //			System.out.println(sql);
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            transList = parseRSet(rset);

            for(Transaction tt: transList){
                if(tt.getTransType().equals( "NS" )){
                    transListNS.add( tt );
                }
            }
            for(Transaction tt: transListNS){
                transList.remove( tt );
            }
            transListNS.addAll( transList );

            rset.close();
            con.close();

        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return transListNS;
    }

    private static ArrayList<Transaction> parseRSet(ResultSet rset) throws Exception{
        ArrayList<Transaction> trasMap = new ArrayList<Transaction>();
        while ( rset.next() ) {
            Transaction tran = new Transaction();
            Product prod = new Product();
            tran.setInvoiceNum( rset.getString( "invoiceNum" ) );
            tran.setTransType( rset.getString( "transType" ) );
            tran.setProductQty( rset.getString( "productQty" ) );
            tran.setTransDate( rset.getString( "transDate" ) );
            tran.setTransNum( rset.getString( "transNum" ) );
            trasMap.add(tran);
        }
        return trasMap;
    }
    public static void main( String[] args ) {
        //		HashMap<String, String> hm = TransUtil.getAllHBHash();
        //		Set set = hm.keySet();
        //		Iterator<String> iter = set.iterator();
        //		while(iter.hasNext()){
        //			String key = iter.next();
        //			System.out.println(key+" "+hm.get(key));
        //		}
        //
        //		TransUtil tu = new TransUtil();
        //      tu.addAccountsToHBInvoice();
        //        ArrayList<Transaction> tl = TransUtil.getAllTransForProd( "d166396");
        //ArrayList<Transaction> tl = TransUtil.getTransListForProdNum( "134444", LocationsDBName.OREM.name());
        System.out.println(TransUtil.getTransHistForProd( "4420", "MURRAY"));
        //        ArrayList<Transaction> tl = TransUtil.getTransListForProdNum( "123551", LocationsDBName.MURRAY.name());
        //        try {
        //            for(Transaction trans: tl){
        //                System.out.println(trans.getTransDateFormatted()+" "+trans.getTransType()+" - "+trans.getProductQty()+" - "+trans.getInvoiceNum());
        //            }
        //        } catch (Exception e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        }
    }

    public static Transaction saveTransaction(Transaction trans) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql =
                " INSERT INTO dmm.Transactions"+
                        " (transNum, invoiceNum, transType, productNum, productName, productQty, transCost, transTax,"+
                        " transProductStatus, transDate, locationNum, username, transShipCost, transShipped, transShipDate)"+

                        "VALUES(" +
                        " ?, " + //(<{transNum: }>,
                        " ?, " + //<{invoiceNum: 0}>,
                        " ?, " + //<{transType: }>,
                        " ?, " + //<{productNum: 0}>,
                        " ?, " + //<{productName: }>,
                        " ?, " + //<{productQty: 0}>,
                        " ?, " + //<{transCost: }>,
                        " ?, " + //<{transTax: }>,
                        " ?, " + //<{transProductStatus: }>,
                        " ?, " + //<{transDate: 0000-00-00 00:00:00}>,
                        " ?, " + //<{locationNum: 0}>,
                        " ?, " + //<{username: }>,
                        " ?, " + //<{transShipCost: }>,
                        " ?, " + //<{transShipped: 0}>,
                        " ? " +  //<{transShipDate: }>);
                        ")";

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1,  null); //(<{transNum: }>,
            pstmt.setString(2,  StringUtils.isBlank(trans.getInvoiceNum())         ? null:trans.getInvoiceNum())        ;//<{invoiceNum: 0}>,
            pstmt.setString(3,  StringUtils.isBlank(trans.getTransType())          ? null:trans.getTransType())         ;//<{transType: }>,
            pstmt.setString(4,  StringUtils.isBlank(trans.getProductNum())         ? null:trans.getProductNum())        ;//<{productNum: 0}>,
            pstmt.setString(5,  StringUtils.isBlank(trans.getProductName())        ? null:trans.getProductName())       ;//<{productName: }>,
            pstmt.setString(6,  StringUtils.isBlank(trans.getProductQty())         ? null:trans.getProductQty())        ;//<{productQty: 0}>,
            pstmt.setString(7,  StringUtils.isBlank(trans.getTransCost())          ? null:trans.getTransCost())         ;//<{transCost: }>,
            pstmt.setString(8,  StringUtils.isBlank(trans.getTransTax())           ? null:trans.getTransTax())          ;//<{transTax: }>,
            pstmt.setString(9,  StringUtils.isBlank(trans.getTransProductStatus()) ? null:trans.getTransProductStatus());//<{transProductStatus: }>,
            pstmt.setString(10, StringUtils.isBlank(trans.getTransDate())          ? null:trans.getTransDate())         ;//<{transDate: 0000-00-00 00:00:00}>,
            pstmt.setString(11, StringUtils.isBlank(trans.getLocationNum())        ? null:trans.getLocationNum())       ;//<{locationNum: 0}>,
            pstmt.setString(12, StringUtils.isBlank(trans.getUsername())           ? null:trans.getUsername())          ;//<{username: }>,
            pstmt.setString(13, StringUtils.isBlank(trans.getTransShipCost())      ? null:trans.getTransShipCost())     ;//<{transShipCost: }>,
            pstmt.setString(14, StringUtils.isBlank(trans.getTransShipped())       ? null:trans.getTransShipped())      ;//<{transShipped: 0}>,
            pstmt.setString(15, StringUtils.isBlank(trans.getTransShipDate())      ? null:trans.getTransShipDate())     ;//<{transShipDate: }>);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                String newId = rs.getString(1);
                trans.setInvoiceNum(newId);
            }
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trans;

    }
}
