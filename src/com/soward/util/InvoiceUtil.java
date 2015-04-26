/**
 * @author Scott Soward
 * Date: May 14, 2007
 *
 */
package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import com.soward.db.DB;
import com.soward.object.*;
import org.apache.commons.lang.StringUtils;

import com.soward.db.MySQL;

public class InvoiceUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss'.0'");
    private String taxExemptSql =
            "(165930, 165932, 165933,165929, 104388,165931  ) or productNum  in " +
                    "(select productNum from Products where category in " +
                    "('G00000', '400000', 'P00000')))) and ";
    public InvoiceUtil() {

    }
    // get invoice for pid
    public static ArrayList<Invoice> searchInvPid( String pid ) {
        ArrayList<Invoice> invList = new ArrayList<Invoice>();
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Invoices where invoiceNum like '%"+pid+"%'";

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            invList.addAll(getInvoiceListFromReSet(rset, false));
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return invList;
    }
    // get invoice for pid
    public static ArrayList<Invoice> invoiceContactNum( String pid ) {
        ArrayList<Invoice> invList = new ArrayList<Invoice>();
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Invoices where invoiceNum like '%"+pid+"%' and invoiceContactNum = 'PO' order by invoiceDate desc";

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            invList.addAll(getInvoiceListFromReSet(rset, false));
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return invList;
    }

    private static List<Invoice> getInvoiceListFromReSet(ResultSet rset, boolean getTransList) throws SQLException{
        return getInvoiceListFromReSet(rset, getTransList, false);
    }

    private static List<Invoice> getInvoiceListFromReSet(ResultSet rset, boolean getTransList, boolean getProds) throws SQLException{
        List<Invoice> invList = new ArrayList<Invoice>();
        while ( rset.next() ) {
            Invoice inv = new Invoice();

            inv.setInvoiceNum                    (rset.getString("invoiceNum"));
            inv.setAccountNum                    (rset.getString("accountNum"));
            inv.setInvoiceDate                   (rset.getString("invoiceDate"));
            inv.setLocationNum                   (rset.getString("locationNum"));
            inv.setUsername2                     (rset.getString("username"));
            inv.setInvoiceTotal                  (rset.getString("invoiceTotal"));
            inv.setInvoiceTax                    (rset.getString("invoiceTax"));
            inv.setInvoiceShipTotal              (rset.getString("invoiceShipTotal"));
            inv.setInvoicePaid                   (rset.getString("invoicePaid"));
            inv.setPaymentMethod1                (rset.getString("paymentMethod1"));
            inv.setPaymentMethod2                (rset.getString("paymentMethod2"));
            inv.setInvoicePaid1                  (rset.getString("invoicePaid1"));
            inv.setInvoicePaid2                  (rset.getString("invoicePaid2"));
            inv.setInvoiceReceivedBy             (rset.getString("invoiceReceivedBy"));
            inv.setInvoiceContactNum             (rset.getString("invoiceContactNum"));
            inv.setInvoiceReferenceNum           (rset.getString("invoiceReferenceNum"));
            inv.setInvoiceChargeStatus           (rset.getString("invoiceChargeStatus"));
            inv.setInvoiceChargeDate             (rset.getString("invoiceChargeDate"));
            inv.setInvoiceChargePaymentMethod    (rset.getString("invoiceChargePaymentMethod"));
            inv.setInvoiceDiscount               (rset.getString("invoiceDiscount"));
            if(getTransList){
                inv.setTransList( TransUtil.getTransactions(inv.getInvoiceNum(), getProds));
            }
            invList.add( inv );
        }
        return invList;
    }

    // get invoice for pid
    public static ArrayList<Invoice> getChargedInvoices( String pid ) {
        ArrayList<Invoice> invList = new ArrayList<Invoice>();
        Connection con = null;
        MySQL sdb = new MySQL();
        String TE = "";
        String UTAHFILTER = "";
        String sql = "select"+

                " invoiceNum                   ,"+
                " accountNum                   ,"+
                " invoiceDate                  ,"+
                " locationNum                  ,"+
                " username                     ,"+
                " invoiceTotal                 ,"+
                " invoiceTax                   ,"+
                " invoiceShipTotal             ,"+
                " invoicePaid                  ,"+
                " paymentMethod1               ,"+
                " paymentMethod2               ,"+
                " invoicePaid1                 ,"+
                " invoicePaid2                 ,"+
                " invoiceReceivedBy            ,"+
                " invoiceContactNum            ,"+
                " invoiceReferenceNum          ,"+
                " invoiceChargeStatus          ,"+
                " invoiceChargeDate            ,"+
                " invoiceChargePaymentMethod   ,"+
                " invoiceDiscount              " +
                " from Invoices where accountNum like '%"+pid+"%'";

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            while ( rset.next() ) {
                Invoice inv = new Invoice();

                inv.setInvoiceNum                    (rset.getString("invoiceNum"));
                inv.setAccountNum                    (rset.getString("accountNum"));
                inv.setInvoiceDate                   (rset.getString("invoiceDate"));
                inv.setLocationNum                   (rset.getString("locationNum"));
                inv.setUsername2                     (rset.getString("username"));
                inv.setInvoiceTotal                  (rset.getString("invoiceTotal"));
                inv.setInvoiceTax                    (rset.getString("invoiceTax"));
                inv.setInvoiceShipTotal              (rset.getString("invoiceShipTotal"));
                inv.setInvoicePaid                   (rset.getString("invoicePaid"));
                inv.setPaymentMethod1                (rset.getString("paymentMethod1"));
                inv.setPaymentMethod2                (rset.getString("paymentMethod2"));
                inv.setInvoicePaid1                  (rset.getString("invoicePaid1"));
                inv.setInvoicePaid2                  (rset.getString("invoicePaid2"));
                inv.setInvoiceReceivedBy             (rset.getString("invoiceReceivedBy"));
                inv.setInvoiceContactNum             (rset.getString("invoiceContactNum"));
                inv.setInvoiceReferenceNum           (rset.getString("invoiceReferenceNum"));
                inv.setInvoiceChargeStatus           (rset.getString("invoiceChargeStatus"));
                inv.setInvoiceChargeDate             (rset.getString("invoiceChargeDate"));
                inv.setInvoiceChargePaymentMethod    (rset.getString("invoiceChargePaymentMethod"));
                inv.setInvoiceDiscount               (rset.getString("invoiceDiscount"));
                invList.add( inv );

            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return invList;
    }

    // get invoice for pid
    public static Invoice getForPid( String pid ) {
        return getForPid(pid, false);
    }
    public static Invoice getForPid( String pid, boolean getProds ) {
        Invoice inv = null;
        Connection con = null;
        MySQL sdb = new MySQL();
        String TE = "";
        String UTAHFILTER = "";
        String sql = "select"+

                " invoiceNum                   ,"+
                " accountNum                   ,"+
                " invoiceDate                  ,"+
                " locationNum                  ,"+
                " username                     ,"+
                " invoiceTotal                 ,"+
                " invoiceTax                   ,"+
                " invoiceShipTotal             ,"+
                " invoicePaid                  ,"+
                " paymentMethod1               ,"+
                " paymentMethod2               ,"+
                " invoicePaid1                 ,"+
                " invoicePaid2                 ,"+
                " invoiceReceivedBy            ,"+
                " invoiceContactNum            ,"+
                " invoiceReferenceNum          ,"+
                " invoiceChargeStatus          ,"+
                " invoiceChargeDate            ,"+
                " invoiceChargePaymentMethod   ,"+
                " invoiceDiscount              " +
                " from Invoices where invoiceNum="+pid;

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            if ( rset.next() ) {
                inv = new Invoice();
                inv.setInvoiceNum                    (rset.getString("invoiceNum"));
                inv.setAccountNum                    (rset.getString("accountNum"));
                inv.setInvoiceDate                   (rset.getString("invoiceDate"));
                inv.setLocationNum                   (rset.getString("locationNum"));
                inv.setUsername2                     (rset.getString("username"));
                inv.setInvoiceTotal                  (rset.getString("invoiceTotal"));
                inv.setInvoiceTax                    (rset.getString("invoiceTax"));
                inv.setInvoiceShipTotal              (rset.getString("invoiceShipTotal"));
                inv.setInvoicePaid                   (rset.getString("invoicePaid"));
                inv.setPaymentMethod1                (rset.getString("paymentMethod1"));
                inv.setPaymentMethod2                (rset.getString("paymentMethod2"));
                inv.setInvoicePaid1                  (rset.getString("invoicePaid1"));
                inv.setInvoicePaid2                  (rset.getString("invoicePaid2"));
                inv.setInvoiceReceivedBy             (rset.getString("invoiceReceivedBy"));
                inv.setInvoiceContactNum             (rset.getString("invoiceContactNum"));
                inv.setInvoiceReferenceNum           (rset.getString("invoiceReferenceNum"));
                inv.setInvoiceChargeStatus           (rset.getString("invoiceChargeStatus"));
                inv.setInvoiceChargeDate             (rset.getString("invoiceChargeDate"));
                inv.setInvoiceChargePaymentMethod    (rset.getString("invoiceChargePaymentMethod"));
                inv.setInvoiceDiscount               (rset.getString("invoiceDiscount"));
            }
            rset.close();
            con.close();
            AccountUtil acu = new AccountUtil();
            TransUtil tru = new TransUtil();
            if(inv!=null){
                inv.setAccount( acu.getAccount( inv.getAccountNum() ));
                inv.setTransList( tru.getTransactions(inv.getInvoiceNum(), getProds) );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return inv;
    }

    public static Map<Long, ProductsLocationCount> getProductCountsForSales( Date date, String location ) {
        MySQL sdb = new MySQL();
        String sql = "select prods.* from \n" +
                "\tInvoiceLocation invLoc join Transactions trans on invLoc.invoiceNum = trans.invoiceNum\n" +
                "    join ProductsLocationCount prods on prods.productNum = trans.productNum\n" +
                "\t  where invLoc.location = ? and trans.transDate like '"+new SimpleDateFormat("yyyy-MM-dd").format(date)+"%'";
        Connection con = null;
        List<ProductsLocationCount> plcList = new ArrayList<ProductsLocationCount>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString(1,  location);
            ResultSet rset = pstmt.executeQuery();
            plcList.addAll(ProductsLocationCountUtil.getPLCResult(rset));
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        Map<Long, ProductsLocationCount> map = new HashMap<Long, ProductsLocationCount>();
        if(plcList != null){
            for(ProductsLocationCount plc: plcList){
                map.put(new Long(plc.getProductNum()), plc);
            }
            return map;
        }
        return null;
    }
    public static Map<Long, ProductSold> getProdSoldForInvoices( Date date, String location ) {
        MySQL sdb = new MySQL();
        String sql = "select inv.* from Invoices inv join InvoiceLocation invLoc on invLoc.invoiceNum = inv.invoiceNum" +
                " where invLoc.location = ? and invoiceDate like '"+new SimpleDateFormat("yyyy-MM-dd").format(date)+"%'";
        Connection con = null;
        List<Invoice> invList = new ArrayList<Invoice>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString(1,  location);
            ResultSet rset = pstmt.executeQuery();
            invList.addAll(getInvoiceListFromReSet(rset, true, true));
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        //collect product ids
        List<Long> pIds = new ArrayList<Long>();
        if(invList != null){
            for(Invoice inv: invList){
                for(Transaction trans: inv.getTransList()){
                    pIds.add(Long.parseLong(trans.getProductNum()));
                }
            }
            //clear duplicates
            Set<Long> hs = new HashSet<Long>();
            hs.addAll(pIds);
            pIds.clear();
            pIds.addAll(hs);
            return ProductUtils.fetchPastThreeYearsSold(pIds, location);
        }
        return null;
    }

    public static List<Invoice> getForDate( Date date, String location ) {
        MySQL sdb = new MySQL();
        String sql = "select inv.* from Invoices inv join InvoiceLocation invLoc on invLoc.invoiceNum = inv.invoiceNum" +
                " where invLoc.location = ? and invoiceDate like '"+new SimpleDateFormat("yyyy-MM-dd").format(date)+"%'";
        Connection con = null;
        List<Invoice> invList = new ArrayList<Invoice>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString(1,  location);
            ResultSet rset = pstmt.executeQuery();
            invList.addAll(getInvoiceListFromReSet(rset, true, true));
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return invList;
    }

    public static Invoice getForInvoiceDate( String date ) {
        Invoice inv = new Invoice();
        Connection con = null;
        MySQL sdb = new MySQL();
        String TE = "";
        String UTAHFILTER = "";
        String sql = "select"+
                " invoiceNum                   ,"+
                " accountNum                   ,"+
                " invoiceDate                  ,"+
                " locationNum                  ,"+
                " username                     ,"+
                " invoiceTotal                 ,"+
                " invoiceTax                   ,"+
                " invoiceShipTotal             ,"+
                " invoicePaid                  ,"+
                " paymentMethod1               ,"+
                " paymentMethod2               ,"+
                " invoicePaid1                 ,"+
                " invoicePaid2                 ,"+
                " invoiceReceivedBy            ,"+
                " invoiceContactNum            ,"+
                " invoiceReferenceNum          ,"+
                " invoiceChargeStatus          ,"+
                " invoiceChargeDate            ,"+
                " invoiceChargePaymentMethod   ,"+
                " invoiceDiscount              " +
                " from Invoices where invoiceDate='"+date+"'";

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            if ( rset.next() ) {


                inv.setInvoiceNum                    (rset.getString("invoiceNum"));
                inv.setAccountNum                    (rset.getString("accountNum"));
                inv.setInvoiceDate                   (rset.getString("invoiceDate"));
                inv.setLocationNum                   (rset.getString("locationNum"));
                inv.setUsername2                     (rset.getString("username"));
                inv.setInvoiceTotal                  (rset.getString("invoiceTotal"));
                inv.setInvoiceTax                    (rset.getString("invoiceTax"));
                inv.setInvoiceShipTotal              (rset.getString("invoiceShipTotal"));
                inv.setInvoicePaid                   (rset.getString("invoicePaid"));
                inv.setPaymentMethod1                (rset.getString("paymentMethod1"));
                inv.setPaymentMethod2                (rset.getString("paymentMethod2"));
                inv.setInvoicePaid1                  (rset.getString("invoicePaid1"));
                inv.setInvoicePaid2                  (rset.getString("invoicePaid2"));
                inv.setInvoiceReceivedBy             (rset.getString("invoiceReceivedBy"));
                inv.setInvoiceContactNum             (rset.getString("invoiceContactNum"));
                inv.setInvoiceReferenceNum           (rset.getString("invoiceReferenceNum"));
                inv.setInvoiceChargeStatus           (rset.getString("invoiceChargeStatus"));
                inv.setInvoiceChargeDate             (rset.getString("invoiceChargeDate"));
                inv.setInvoiceChargePaymentMethod    (rset.getString("invoiceChargePaymentMethod"));
                inv.setInvoiceDiscount               (rset.getString("invoiceDiscount"));


            }
            rset.close();
            con.close();
            AccountUtil acu = new AccountUtil();
            inv.setAccount( acu.getAccount( inv.getAccountNum() ));
            inv.setTransList( TransUtil.getTransaction(inv.getInvoiceNum()) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return inv;
    }
    public static String trunDouble(String trunCanidate){
        double val = 0.0;
        try{
            val = Double.parseDouble(trunCanidate);
            val = Math.rint(val * 1000.0) / 1000.0;
        }catch(Exception e){
            //e.printStackTrace();
            //do nothing: string is not a double
        }
        return val+"";

    }

    public static List<Invoice> getSynced() {
        ArrayList<Invoice> invList = new ArrayList<Invoice>();
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select inv.* from \n" +
                "Invoices inv join \n" +
                "sm_sales_order smso on smso.dmm_invoice_id = inv.invoiceNum\n" ;
//                " where date(created_dt)  = curdate()";

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            invList.addAll(getInvoiceListFromReSet(rset, true));
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return invList;
    }

    /*
      * get invoice for specific day
      */

    public List<Invoice> getTEInvoiceForSingleDate(String date){
        List<Invoice> invList = new ArrayList<Invoice>();
        try {
            AccountUtil acu = new AccountUtil();
            TransUtil tru = new TransUtil();
            MySQL sdb = new MySQL();
            Connection con = sdb.getConn();
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            String sql = "select "+
                    "invoiceNum                       ,"+
                    "accountNum                       ,"+
                    "invoiceDate                      ,"+
                    "locationNum                      ,"+
                    "username                         ,"+
                    "invoiceTotal                     ,"+
                    "invoiceTax                       ,"+
                    "invoiceShipTotal                 ,"+
                    "invoicePaid                      ,"+
                    "paymentMethod1                   ,"+
                    "paymentMethod2                   ,"+
                    "invoicePaid1                     ,"+
                    "invoicePaid2                     ,"+
                    "invoiceReceivedBy                ,"+
                    "invoiceContactNum                ,"+
                    "invoiceReferenceNum              ,"+
                    "invoiceChargeStatus              ,"+
                    "invoiceChargeDate                ,"+
                    "invoiceChargePaymentMethod       ,"+
                    "invoiceDiscount                  "+
                    " from Invoices where " +
                    "(invoiceTax=0.0 or invoiceNum in " +
                    "(select invoiceNum from Transactions where productNum in " +
                    taxExemptSql +
                    " DATE_FORMAT(invoiceDate, '%Y-%m-%d') =  '"+date+"'"+
//                    "datename( year, '"+date+"' )= datename( year, invoiceDate  ) and " +
//                    "datename( month , '"+date+"' )= datename( month , invoiceDate ) and " +
//                    "datename( day, '"+date+"' )= datename( day, invoiceDate  ) "+
                    " order by invoiceNum";
//            System.out.println(sql);
            pstmt = con.prepareStatement( sql );
            rset = pstmt.executeQuery();

            invList = getInvoiceListFromReSet(rset, true);
            rset.close();

            pstmt.close();
            con.close();
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return invList;
    }
    /*
      * get invoice for date range;
      * to include the two given dates
      * add 24 hours to the top date range and
      * sub 24 hours to the bottom date range
      */

    public ArrayList<Invoice> getTEInvoiceForRangeDate(String dateOne, String dateTwo){
        ArrayList<Invoice> invList = new ArrayList<Invoice>();
        try {
            MySQL sdb = new MySQL();
            Connection con = sdb.getConn();
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            String sql = "select "+
                    "invoiceNum                       ,"+
                    "accountNum                       ,"+
                    "invoiceDate                      ,"+
                    "locationNum                      ,"+
                    "username                         ,"+
                    "invoiceTotal                     ,"+
                    "invoiceTax                       ,"+
                    "invoiceShipTotal                 ,"+
                    "invoicePaid                      ,"+
                    "paymentMethod1                   ,"+
                    "paymentMethod2                   ,"+
                    "invoicePaid1                     ,"+
                    "invoicePaid2                     ,"+
                    "invoiceReceivedBy                ,"+
                    "invoiceContactNum                ,"+
                    "invoiceReferenceNum              ,"+
                    "invoiceChargeStatus              ,"+
                    "invoiceChargeDate                ,"+
                    "invoiceChargePaymentMethod       ,"+
                    "invoiceDiscount                  "+
                    " from Invoices where " +
                    "(invoiceTax=0.0 or invoiceNum in " +
                    "(select invoiceNum from Transactions where productNum in " +
                    taxExemptSql +
                    "invoiceDate > '" + dateOne + "' and " +
                    "invoiceDate< dateadd(hh, 24, '" + dateTwo + "')"+
                    "order by invoiceNum";

            pstmt = con.prepareStatement( sql );
            rset = pstmt.executeQuery();

            while ( rset.next() ) {
                Invoice inv = new Invoice();
                inv.setInvoiceNum                       ( rset.getString("invoiceNum"));
                inv.setAccountNum                       ( rset.getString("accountNum"));
                inv.setInvoiceDate                      ( rset.getString("invoiceDate"));
                inv.setLocationNum                      ( rset.getString("locationNum"));
                inv.setUsername2                        ( rset.getString("username"));
                inv.setInvoiceTotal                     ( rset.getString("invoiceTotal"));
                inv.setInvoiceTax                       ( rset.getString("invoiceTax"));
                inv.setInvoiceShipTotal                 ( rset.getString("invoiceShipTotal"));
                inv.setInvoicePaid                      ( rset.getString("invoicePaid"));
                inv.setPaymentMethod1                   ( rset.getString("paymentMethod1"));
                inv.setPaymentMethod2                   ( rset.getString("paymentMethod2"));
                inv.setInvoicePaid1                     ( rset.getString("invoicePaid1"));
                inv.setInvoicePaid2                     ( rset.getString("invoicePaid2"));
                inv.setInvoiceReceivedBy                ( rset.getString("invoiceReceivedBy"));
                inv.setInvoiceContactNum                ( rset.getString("invoiceContactNum"));
                inv.setInvoiceReferenceNum              ( rset.getString("invoiceReferenceNum"));
                inv.setInvoiceChargeStatus              ( rset.getString("invoiceChargeStatus"));
                inv.setInvoiceChargeDate                ( rset.getString("invoiceChargeDate"));
                inv.setInvoiceChargePaymentMethod       ( rset.getString("invoiceChargePaymentMethod"));
                inv.setInvoiceDiscount                  ( rset.getString("invoiceDiscount"));

                inv.setTransList( TransUtil.getTransaction(inv.getInvoiceNum()) );
                invList.add(inv);
            }
            rset.close();

            pstmt.close();
            con.close();
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return invList;
    }

    public static double getDoubleSum (double currSum, String addDouble){
        double sum = 0.0;
        try{
            sum = currSum;
            try{
                double temp = Double.parseDouble( addDouble );
                sum += temp;
                //comment out to keep long doubles ie 23213.2343234
                //				sum = Math.rint(sum * 1000.0) / 1000.0;
            }catch(Exception e){
                //e.printStackTrace();
                //do nothing: string is not a double
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return sum;
    }


    public static int getMaxInvoiceNum(){
        Connection con = null;
        ArrayList<Supplier> allSups = new ArrayList<Supplier>();
        MySQL sdb = new MySQL();
        String sql = "select max(invoiceNum) mmm from Invoices";
        int max = 0;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            if(rset.next()){
                max = rset.getInt("mmm");
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return max;
    }

    public static Invoice createNewPO(String username, String supplier){
        Connection con = null;
        MySQL sdb = new MySQL();
        String invoiceDate = sdf.format(Calendar.getInstance().getTime());
        String sql = "insert into Invoices (invoiceNum, accountNum, invoiceDate, locationNum, username, invoiceTotal, " +
                "invoiceTax, invoiceShipTotal, invoicePaid, invoiceReceivedBy, invoiceContactNum, invoiceReferenceNum, " +
                "invoiceChargeStatus, paymentMethod1) " +
                "values (?, 300, ?, 1, ?, 0.00, 0.00, 0.00, 0.00, ?, 'PO', NULL, NULL,NULL)";
        Invoice inv = null;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );

            pstmt.setInt(1, incrementAdminInvoice());
            pstmt.setString(2, invoiceDate);
            pstmt.setString(3, username);
            pstmt.setString(4, supplier);

            pstmt.executeUpdate();
            inv = getForInvoiceDate(invoiceDate);
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return inv;
    }

    public static Boolean isInvoiceAdminLocked(){
        Connection con = null;
        MySQL sdb = new MySQL();

        try {
            con = sdb.getConn();
            String isReady = "select invoiceNumLocked from Admin";
            PreparedStatement pstmt = con.prepareStatement( isReady );
            ResultSet r = pstmt.executeQuery();
            if ( r.next() ) {
                return r.getLong("invoiceNumLocked") == 1;
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void attemptToLockAdminInvoice(){
        Connection con = null;
        MySQL sdb = new MySQL();
        try {
            con = sdb.getConn();
            String lock = "update Admin set invoiceNumLocked = 1 where invoiceNumLocked = 0";
            PreparedStatement pstmt = con.prepareStatement( lock );
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) {
        //System.out.println(InvoiceUtil.incrementAdminInvoice());
        System.out.println(InvoiceUtil.getForDate(new Date(), "MURRAY"));
    }

    private static int incrementAdminInvoice() {
        int newId = -1;
        Connection con = null;
        MySQL sdb = new MySQL();

        while(!isInvoiceAdminLocked()){
            attemptToLockAdminInvoice();
        }
        try{
            String update = "update Admin set invoiceNum = invoiceNum+1";
            String fetch = "select invoiceNum from Admin";
            String updateInx = "update Admin set invoiceNum =invoiceNum +1";
            String unLock = "update Admin set invoiceNumLocked = 0";

            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement(update);
            pstmt.executeUpdate();
            pstmt = con.prepareStatement(fetch);
            pstmt.executeQuery();
            ResultSet r = pstmt.executeQuery();
            if ( r.next() ) {
                newId = r.getInt("invoiceNum");
            }
            pstmt = con.prepareStatement(updateInx);
            pstmt = con.prepareStatement(unLock);
            pstmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
        return newId;
    }

    public static Invoice saveInvoice(Invoice inv){
        Connection con = null;
        MySQL sdb = new MySQL();
        String invoiceDate = sdf.format(Calendar.getInstance().getTime());
        String sql =
                " INSERT INTO dmm.Invoices"+
                        " (invoiceNum, accountNum, invoiceDate, locationNum, username, invoiceTotal, invoiceTax, invoiceShipTotal, invoicePaid,"+
                        " paymentMethod1, paymentMethod2, invoicePaid1, invoicePaid2, invoiceReceivedBy, invoiceContactNum, invoiceReferenceNum,"+
                        " invoiceChargeStatus, invoiceChargeDate, invoiceChargePaymentMethod, invoiceDiscount)"+

                        "VALUES("+
                        " ?, "+//(<{invoiceNum: }>,
                        " ?, "+//<{accountNum: 0}>,
                        " ?, "+//<{invoiceDate: 0000-00-00 00:00:00}>,
                        " ?, "+//<{locationNum: 0}>,
                        " ?, "+//<{username: }>,
                        " ?, "+//<{invoiceTotal: 0.0000}>,
                        " ?, "+//<{invoiceTax: }>,
                        " ?, "+//<{invoiceShipTotal: }>,
                        " ?, "+//<{invoicePaid: 0.0000}>,
                        " ?, "+//<{paymentMethod1: }>,
                        " ?, "+//<{paymentMethod2: }>,
                        " ?, "+//<{invoicePaid1: }>,
                        " ?, "+//<{invoicePaid2: }>,
                        " ?, "+//<{invoiceReceivedBy: }>,
                        " ?, "+//<{invoiceContactNum: }>,
                        " ?, "+//<{invoiceReferenceNum: }>,
                        " ?, "+//<{invoiceChargeStatus: }>,
                        " ?, "+//<{invoiceChargeDate: }>,
                        " ?, "+//<{invoiceChargePaymentMethod: }>,
                        " ?  "+//<{invoiceDiscount: }>);
                        ")";

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );

            pstmt.setInt(1,  incrementAdminInvoice());  //(<{invoiceNum: }>,
            pstmt.setString(2,  StringUtils.isBlank(inv.getAccountNum())                ?null:inv.getAccountNum());                //<{accountNum: 0}>,
            pstmt.setString(3,  StringUtils.isBlank(inv.getInvoiceDate())               ?null:inv.getInvoiceDate());               //<{invoiceDate: 0000-00-00 00:00:00}>,
            pstmt.setString(4,  StringUtils.isBlank(inv.getLocationNum())               ?null:inv.getLocationNum());               //<{locationNum: 0}>,
            pstmt.setString(5,  StringUtils.isBlank(inv.getUsername2())                 ?null:inv.getUsername2());                 //<{username: }>,
            pstmt.setString(6,  StringUtils.isBlank(inv.getInvoiceTotal())              ?"0.0":inv.getInvoiceTotal());             //<{invoiceTotal: 0.0000}>,
            pstmt.setString(7,  StringUtils.isBlank(inv.getInvoiceTax())                ?null:inv.getInvoiceTax());                //<{invoiceTax: }>,
            pstmt.setString(8,  StringUtils.isBlank(inv.getInvoiceShipTotal())          ?null:inv.getInvoiceShipTotal());          //<{invoiceShipTotal: }>,
            pstmt.setString(9,  StringUtils.isBlank(inv.getInvoicePaid())               ?"0.0":inv.getInvoicePaid());              //<{invoicePaid: 0.0000}>,
            pstmt.setString(10, StringUtils.isBlank(inv.getPaymentMethod1())            ?null:inv.getPaymentMethod1());            //<{paymentMethod1: }>,
            pstmt.setString(11, StringUtils.isBlank(inv.getPaymentMethod2())            ?"0.0":inv.getPaymentMethod2());           //<{paymentMethod2: }>,
            pstmt.setString(12, StringUtils.isBlank(inv.getInvoicePaid1())              ?"0.0":inv.getInvoicePaid1());             //<{invoicePaid1: }>,
            pstmt.setString(13, StringUtils.isBlank(inv.getInvoicePaid2())              ?"0.0":inv.getInvoicePaid2());             //<{invoicePaid2: }>,
            pstmt.setString(14, StringUtils.isBlank(inv.getInvoiceReceivedBy())         ?null:inv.getInvoiceReceivedBy());         //<{invoiceReceivedBy: }>,
            pstmt.setString(15, StringUtils.isBlank(inv.getInvoiceContactNum())         ?null:inv.getInvoiceContactNum());         //<{invoiceContactNum: }>,
            pstmt.setString(16, StringUtils.isBlank(inv.getInvoiceReferenceNum())       ?null:inv.getInvoiceReferenceNum());       //<{invoiceReferenceNum: }>,
            pstmt.setString(17, StringUtils.isBlank(inv.getInvoiceChargeStatus())       ?null:inv.getInvoiceChargeStatus());       //<{invoiceChargeStatus: }>,
            pstmt.setString(18, StringUtils.isBlank(inv.getInvoiceChargeDate())         ?null:inv.getInvoiceChargeDate());         //<{invoiceChargeDate: }>,
            pstmt.setString(19, StringUtils.isBlank(inv.getInvoiceChargePaymentMethod())?null:inv.getInvoiceChargePaymentMethod());//<{invoiceChargePaymentMethod: }>,
            pstmt.setString(20, StringUtils.isBlank(inv.getInvoiceDiscount())           ?"0.0":inv.getInvoiceDiscount());          //<{invoiceDiscount: }>);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                String newId = rs.getString(1);
                inv.setInvoiceNum(newId);
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return inv;
    }

    public static boolean saveArchiveInvoice(ArchivedInvoice ai) {
        Connection con = null;
        DB db = new DB();
        boolean result = false;
        try {
            con = db.openConnection();
            String sql = "insert into archivedinvoice values (null, now(), ?,?,?,?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement( sql );
            pstmt.setString(1, ai.getInvoiceJSON());
            pstmt.setString(2, ai.getUserId());
            pstmt.setString(3, ai.getUserName());
            pstmt.setString(4, ai.getInvoiceNum());
            pstmt.setLong  (5, ai.getReason());
            pstmt.setString(6, ai.getAdditionComments());
            pstmt.setString(7, ai.getRegister());
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
            result = true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static ArchivedInvoice fetchForId(String id){
        Connection con = null;
        DB db = new DB();
        ArchivedInvoice ai = null;
        try {
            con = db.openConnection();
            String sql = "select * from archivedinvoice where id =?";
            PreparedStatement pstmt = con.prepareStatement( sql );
            pstmt.setInt(1, Integer.parseInt(id));
            System.out.println(sql);
            ResultSet r = pstmt.executeQuery();
            while ( r.next() ) {
                ai = new ArchivedInvoice();
                ai.setId(r.getLong("id"));
                ai.setDate(r.getTimestamp("date"));
                ai.setInvoiceJSON(r.getString("invoice"));
                ai.setInvoiceNum(r.getString("invoicenum"));
                ai.setReason(r.getLong("reason"));
                ai.setAdditionComments(r.getString("comments"));
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ai;
    }

    public static boolean deleteInvoice(String inum) {
        Connection con = null;
        MySQL db = new MySQL();
        boolean result = false;
        //first delete transactions
        if(deleteTransactions(inum)){
            try {
                con = db.getConn();
                String sql = "delete from Invoices where invoiceNum = ?";
                PreparedStatement pstmt = con.prepareStatement( sql );
                pstmt.setInt(1, Integer.parseInt(inum));
                pstmt.executeUpdate();
                pstmt.close();
                con.close();
                result = true;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    public static boolean deleteTransaction(String trasNum) {
        Connection con = null;
        MySQL db = new MySQL();
        boolean result = false;
        try {
            con = db.getConn();
            String sql = "delete from Transactions where transNum = ?";
            PreparedStatement pstmt = con.prepareStatement( sql );
            pstmt.setInt(1, Integer.parseInt(trasNum));
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
            result = true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private static boolean deleteTransactions(String inum) {
        Connection con = null;
        MySQL db = new MySQL();
        boolean result = false;
        try {
            con = db.getConn();
            String sql = "delete from Transactions where invoiceNum = ?";
            PreparedStatement pstmt = con.prepareStatement( sql );
            pstmt.setInt(1, Integer.parseInt(inum));
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
            result = true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }


//    public static void main( String[] args ) {
    // InvoiceUtil.deleteInvoice("577479");
//        ArchivedInvoice ai = InvoiceUtil.fetchForId("5");
//        System.out.println(ai.getInvoiceJSON());
//		InvoiceUtil iul = new InvoiceUtil();
//		System.out.println(InvoiceUtil.getMaxInvoiceNum());
    //InvoiceUtil.createNewPO("bonita", "bogus");
//		//iul.getForPid( "287899" );
//		DBObj tt = new DBObj();
//		String sql = "select * from Invoices where invoiceNum like %287";
//		System.out.println( sql );
//		tt.runAndPrintOutQuery( sql, "execute" );
//    }

}
