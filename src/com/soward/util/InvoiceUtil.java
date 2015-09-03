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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.soward.db.DB;
import com.soward.enums.FloorEnum;
import com.soward.enums.ProductCacheEnum;
import com.soward.object.*;
import org.apache.commons.lang.StringUtils;

import com.soward.db.MySQL;
import org.codehaus.jackson.map.ObjectMapper;

public class InvoiceUtil {
    public static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
    private static SimpleDateFormat sdfInvoice = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss'.0'");
    private static SimpleDateFormat sdfInv = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            try {inv.setInvDate(sdfInv.parse(inv.getInvoiceDate()));} catch (Exception e) {/*e.printStackTrace();*/}
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
        if(getTransList && invList.size()>0){
            List<Long> pIds = new ArrayList<Long>();
            for(Invoice inv: invList){
                pIds.add(Long.parseLong(inv.getInvoiceNum()));
            }
            //clear duplicates
            Set<Long> hs = new HashSet<Long>();
            hs.addAll(pIds);
            pIds.clear();
            pIds.addAll(hs);
            List<Transaction> transList = TransUtil.getTransactionsForInvNum(pIds, getProds);

            for(Invoice invoice: invList){
                for(Transaction transaction: transList) {
                    if(invoice.getInvoiceNum().equals(transaction.getInvoiceNum())) {
                        invoice.setTransList((invoice.getTransList()!=null?invoice.getTransList():new ArrayList<Transaction>()));
                        invoice.getTransList().add(transaction);
                    }
                }
            }
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

    public static Long saveInventoryCache(DailySalesCache dsc) {
        return saveInventoryCache(dsc, null, null);
    }
    public static Long saveInventoryCache(DailySalesCache dsc, Date date, String location) {
        Connection con = null;
        MySQL sdb = new MySQL();
        Long key = null;
        DailySalesCache current = dsc.getId()!=null?getInventoryCache(dsc.getId()):null;

        try {
            String sql = "insert into DailySalesInventory values (null,?,?,?, null)";
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            if(current != null && current.getMap() != null){
                current.getMap().putAll(dsc.getMap());
                String json = new ObjectMapper().writeValueAsString(current.getMap());
                sql = "update DailySalesInventory set data1 = ? where id = ?";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, json);
                pstmt.setLong(2, current.getId());
            } else {
                String json = new ObjectMapper().writeValueAsString(dsc.getMap());
                pstmt = con.prepareStatement(sql);
                pstmt.setDate(1, new java.sql.Date(date.getTime()));
                pstmt.setString(2, location);
                pstmt.setString(3, json);
            }
            pstmt.executeUpdate();
            ResultSet keys = pstmt.getGeneratedKeys();

            if(keys.next()){
                key = keys.getLong( 1 );
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return key;
    }

    public static DailySalesCache getInventoryCache(Long id) {
        return getInventoryCacheForDateOrId(id, null, null);
    }

    public static DailySalesCache getInventoryCacheForDate( Date date, String location ) {
        return getInventoryCacheForDateOrId(null, date, location);
    }

    public static DailySalesCache getInventoryCacheForDateOrId(Long id, Date date, String location ) {
        MySQL sdb = new MySQL();
        String sql = "select * from DailySalesInventory where id = ?";
        if(id == null){
            sql = "select * from DailySalesInventory where location = ? and date like '"+new SimpleDateFormat("yyyy-MM-dd").format(date)+"%'";
        }
        Connection con = null;
        DailySalesCache dsc = null;
        PreparedStatement pstmt = null;
        try {

            con = sdb.getConn();
            pstmt = con.prepareStatement( sql );
            if(id != null){
                pstmt.setLong(1, id);
            }else {
                pstmt.setString(1, location);
            }
            ResultSet rset = pstmt.executeQuery();
            ObjectMapper mapper = new ObjectMapper();
            if(rset.next()) {
                String data = rset.getString("data1");
                Long dscId = rset.getLong("id");
                Map<String, Map> map = mapper.readValue(data, HashMap.class);
                map = map!=null?map:new HashMap<String, Map>();
                dsc = new DailySalesCache(dscId, map);
            }
            //if DSC doesnt exist: create it
            else if(date != null && location != null){
                //create and return empty DailySalesCache for date and location
                Long key = saveInventoryCache(new DailySalesCache(null, new HashMap<String, Map>()), date, location);
                dsc = getInventoryCacheForDateOrId(key, null, null);
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return dsc;
    }

    public static void main( String[] args ) {
//        Calendar toCal = Calendar.getInstance();
//        Calendar fromCal = Calendar.getInstance();
//        toCal.add(Calendar.MONTH, -5);
//        fromCal.add(Calendar.MONTH, -4);
//        Map<Long, ProductsLocationCount> map = InvoiceUtil.getProductCountsForSales(toCal.getTime(), toCal.getTime(), "MURRAY");
//        Map<Long, ProductSold> map = InvoiceUtil.getProdSoldForInvoices(toCal.getTime(), fromCal.getTime(), "MURRAY");
//        List<Invoice> map = getForDateRange(toCal.getTime(), fromCal.getTime(), "MURRAY", true, true);
//        System.out.println(map);
        InvoiceUtil.getRecentSold("167594");
    }

    public static Map<Long, Map> getRecentSoldForDate(Date fromDate, Date toDate, String location ) {
        Map<Long, Map> map = new HashMap<Long, Map>();

        List<Long> prodIds = getProdIdsForDate(fromDate, toDate, location);

        Map<Long, Long> map30  = addAllForPropIds(30, prodIds);
        Map<Long, Long> map90  = addAllForPropIds(90, prodIds);
        Map<Long, Long> map365 = addAllForPropIds(365, prodIds);

        Set set = map30.keySet();
        Iterator<Long> iter = set.iterator();
        while(iter.hasNext()){
            Long prodNum = iter.next();
            Map<String, Long> prodMap = map.get(prodNum)!=null?map.get(prodNum):new HashMap<String, Long>();
            prodMap.put("Days30", map30.get(prodNum));
            prodMap.put("Days90", map90.get(prodNum));
            prodMap.put("Days365",map365.get(prodNum));
            map.put(prodNum, prodMap);
        }
        set = map90.keySet();
        iter = set.iterator();
        while(iter.hasNext()){
            Long prodNum = iter.next();
            Map<String, Long> prodMap = map.get(prodNum)!=null?map.get(prodNum):new HashMap<String, Long>();
            prodMap.put("Days30", map30.get(prodNum));
            prodMap.put("Days90", map90.get(prodNum));
            prodMap.put("Days365",map365.get(prodNum));
            map.put(prodNum, prodMap);
        }
        set = map365.keySet();
        iter = set.iterator();
        while(iter.hasNext()){
            Long prodNum = iter.next();
            Map<String, Long> prodMap = map.get(prodNum)!=null?map.get(prodNum):new HashMap<String, Long>();
            prodMap.put("Days30", map30.get(prodNum));
            prodMap.put("Days90", map90.get(prodNum));
            prodMap.put("Days365",map365.get(prodNum));
            map.put(prodNum, prodMap);
        }

        return map;
    }

    //iterate over the list prod ids and added them to the where clause at < 1000 chucks.
    private static Map<Long, Long> addAllForPropIds(int daysBack, List<Long> prodIds) {
        Map<Long, Long> prodCountMap = new HashMap<Long, Long>();
        List<Long> subsetProdList = new ArrayList<Long>();
        int count = 0;
        for(Long l: prodIds){
            if(count > 900){
                Map<Long, Long> temp = getRecentSoldForDateAndDays(daysBack , subsetProdList);
                addCountsToMap(prodCountMap, temp);
                count = 0;
            }
            subsetProdList.add(l);
            count++;
        }
        Map<Long, Long> temp = getRecentSoldForDateAndDays(daysBack , subsetProdList);
        addCountsToMap(prodCountMap, temp);
        return prodCountMap;
    }

    private static void addCountsToMap(Map<Long, Long> prodCountMap, Map<Long, Long> temp) {
        Set<Long> set = temp.keySet();
        Iterator<Long> iter = set.iterator();
        while(iter.hasNext()){
            Long prodNum = iter.next();
            Long count = temp.get(prodNum);
            if(prodCountMap.containsKey(prodNum)){
                count += temp.get(prodNum);
            }
            prodCountMap.put(prodNum, count);
        }
    }

    private static List<Long> getProdIdsForDate(Date fromDate, Date toDate, String location) {
        MySQL sdb = new MySQL();
        List<Long> list = new ArrayList<Long>();
        String sql = "select productNum from \n" +
                "\tInvoiceLocation invLoc join Transactions trans on invLoc.invoiceNum = trans.invoiceNum\n" +
                "\t  where invLoc.location = ?";

        String wSql = " and trans.transDate between '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate)+"%'\n"+
                "\t  and '"+new SimpleDateFormat("yyyy-MM-dd").format(toDate)+"%'\n";
        if(fromDate.equals(toDate)){
            wSql = " and trans.transDate like '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate)+"%'\n";
        }
        sql+=wSql;
        Connection con = null;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString(1,  location);
            ResultSet rset = pstmt.executeQuery();
            while(rset.next()) {
                list.add(rset.getLong("productNum"));
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return list;
    }

    private static Map<Long, Long> getRecentSoldForDateAndDays(int daysBack, List<Long> pIds ) {
        MySQL sdb = new MySQL();
        Map<Long, Long> map = new HashMap<Long, Long>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -daysBack);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String sql = "select sum(productQty) count, productNum from Transactions where transDate >= '"+simpleDateFormat.format(cal.getTime())+"%'";
        sql += "and productNum in ("+StringUtils.join(pIds, ",")+") group by productNum";

        Connection con = null;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            while(rset.next()) {
                map.put(rset.getLong("productNum"), rset.getLong("count"));
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return map;
    }

//    private static Map<Long, Long> getRecentSoldForDateAndDays(Date fromDate, Date toDate, String location, int daysBack, List<Long> pIds ) {
//        MySQL sdb = new MySQL();
//        Map<Long, Long> map = new HashMap<Long, Long>();
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DAY_OF_YEAR, -daysBack);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
//        String sql = "select sum(productQty) count, productNum from Transactions where transDate >= '"+simpleDateFormat.format(cal.getTime())+"%'";
//
//        sql += "and productNum in (select productNum from \n" +
//                "\tInvoiceLocation invLoc join Transactions trans on invLoc.invoiceNum = trans.invoiceNum\n" +
//                "\t  where invLoc.location = ?";
//
//        String wSql = " and trans.transDate between '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate)+"%'\n"+
//                "\t  and '"+new SimpleDateFormat("yyyy-MM-dd").format(toDate)+"%'\n";
//        if(fromDate.equals(toDate)){
//            wSql = " and trans.transDate like '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate)+"%'\n";
//        }
//
//        sql+=wSql +") group by productNum";
//
//        Connection con = null;
//        try {
//            con = sdb.getConn();
//            PreparedStatement pstmt = null;
//            pstmt = con.prepareStatement( sql );
//            pstmt.setString(1,  location);
//            ResultSet rset = pstmt.executeQuery();
//            while(rset.next()) {
//                map.put(rset.getLong("productNum"), rset.getLong("count"));
//            }
//            rset.close();
//            con.close();
//        } catch ( Exception e ) {
//            e.printStackTrace();
//        }
//
//        return map;
//    }

    public static Map<String, Object> getRecentSold(String productNum) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Days30", getRecentSoldForDaysBack(productNum, 30));
        map.put("Days90", getRecentSoldForDaysBack(productNum, 90));
        map.put("Days365", getRecentSoldForDaysBack(productNum, 365));
        return map;
    }

    private static int getRecentSoldForDaysBack(String productNum, int daysBack) {
        MySQL sdb = new MySQL();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -daysBack);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String sql = "select sum(productQty) count from Transactions where productNum = ? and transDate >= '"+simpleDateFormat.format(cal.getTime())+"%'";
        Connection con = null;
        int count = 0;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString(1,  productNum);
            ResultSet rset = pstmt.executeQuery();
            if(rset.next()) {
                count = rset.getInt("count");
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return count;
    }

    public static List<Map<String, String>> getProductLocationForDate(Date fromDate, Date toDate, String location ) {
        MySQL sdb = new MySQL();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        String sql = "select webInfo.* from \n" +
                "\tInvoiceLocation invLoc join Transactions trans on invLoc.invoiceNum = trans.invoiceNum\n" +
                "    join WebProductInfo webInfo on webInfo.productNum = trans.productNum\n" +
                "\t  where invLoc.location = ?";

        String wSql = " and trans.transDate between '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate)+"%'\n"+
                "\t  and '"+new SimpleDateFormat("yyyy-MM-dd").format(toDate)+"%'\n";
        if(fromDate.equals(toDate)){
            wSql = " and trans.transDate like '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate)+"%'\n";
        }

        sql+=wSql;

        Connection con = null;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString(1,  location);
            ResultSet rset = pstmt.executeQuery();
            while(rset.next()) {
                String productFeature = rset.getString("productFeature");
                if(productFeature!=null&&!StringUtils.isBlank(productFeature)&&productFeature.length()>2) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("productNum" , rset.getString("productNum"));
                    map.put("prodLocation", productFeature.substring(0, 3));
                    map.put("prodLocationFull", rset.getString("productFeature"));
                    list.add(map);
                }
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return list;
    }

    public static Map<String, String> getProductLocation(String productNum) {
        MySQL sdb = new MySQL();
        String sql = "select productFeature from WebProductInfo where productNum = ?";
        Connection con = null;
        String productFeature = null;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString(1,  productNum);
            ResultSet rset = pstmt.executeQuery();
            if(rset.next()) {
                productFeature = rset.getString("productFeature");
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("location", productFeature!=null&&productFeature.length()>2?productFeature.substring(0, 3):productFeature);
        return map;
    }

    public static Map<Long, ProductsLocationCount> getProductCountsForSales(Date fromDate, Date toDate, String location ) {
        MySQL sdb = new MySQL();
        String sql = "select prods.* from \n" +
                "\tInvoiceLocation invLoc join Transactions trans on invLoc.invoiceNum = trans.invoiceNum\n" +
                "    join ProductsLocationCount prods on prods.productNum = trans.productNum\n" +
                "\t  where invLoc.location = ?";

        String wSql = " and trans.transDate between '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate)+"%'\n"+
                "\t  and '"+new SimpleDateFormat("yyyy-MM-dd").format(toDate)+"%'\n";
        if(fromDate.equals(toDate)){
            wSql = " and trans.transDate like '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate)+"%'\n";
        }

        sql+=wSql;
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

    public static Map<Long, ProductSold> getProdSoldForInvoices( Date fromDate, Date toDate, String location ) {
        MySQL sdb = new MySQL();
        String sql = "select prods.productNum from \n" +
                "\tInvoiceLocation invLoc join Transactions trans on invLoc.invoiceNum = trans.invoiceNum\n" +
                "    join ProductsLocationCount prods on prods.productNum = trans.productNum\n" +
                "\t  where invLoc.location = ?";

        String wSql = " and trans.transDate between '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate)+"%'\n"+
                "\t  and '"+new SimpleDateFormat("yyyy-MM-dd").format(toDate)+"%'\n";
        if(fromDate.equals(toDate)){
            wSql = " and trans.transDate like '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate)+"%'\n";
        }

        sql+=wSql;

        Connection con = null;
        List<Long> pIds = new ArrayList<Long>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString(1,  location);
            ResultSet rset = pstmt.executeQuery();
            while ( rset.next() ) {
                pIds.add(Long.parseLong(rset.getString("productNum")));
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        //collect product ids

        if(pIds != null){

            //clear duplicates
            Set<Long> hs = new HashSet<Long>();
            hs.addAll(pIds);
            pIds.clear();
            pIds.addAll(hs);
            if(pIds!=null&&pIds.size()>0) {
                return ProductUtils.fetchPastThreeYearsSold(pIds, location);
            }
        }
        return null;
    }

    public static Map<String, List<Invoice>> getHourlyLocatioForDate( Date date, String location, boolean getTransList ) {
        List<Invoice> invoices = getForDate(date, location, false, false);
        Map<String, List<Invoice>> map = new HashMap<String, List<Invoice>>();
        if(invoices != null){
            for(final Invoice invoice: invoices){
                invoice.setAccount(null);
                FloorEnum floorEnum = FloorEnum.getForName(invoice.getLocationNum());
                if(floorEnum != null){
                    if(map.containsKey(floorEnum.getLocationName())){
                        map.get(floorEnum.getLocationName()).add(invoice);
                    }else{
                        map.put(floorEnum.getLocationName(), new ArrayList<Invoice>(){{add(invoice);}});
                    }
                }
            }
        }
        return map;
    }

    public static List<Invoice> getForDate( Date date, String location, boolean getTransList, boolean getProds ) {
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
            invList.addAll(getInvoiceListFromReSet(rset, getTransList, getProds));
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return invList;
    }

    public static List<Invoice> getRecentSalesFromDateRange( Date fromDate, Date toDate, String location, boolean getTransList, boolean getProds ) {
        MySQL sdb = new MySQL();
        String sql = "select inv.* from Invoices inv join InvoiceLocation invLoc on invLoc.invoiceNum = inv.invoiceNum" +
                " where invLoc.location = ? and invoiceDate between '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate)+"%'\n"+
                "\t  and '"+new SimpleDateFormat("yyyy-MM-dd").format(toDate)+"%'\n";
        Connection con = null;
        List<Invoice> invList = new ArrayList<Invoice>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString(1,  location);
            ResultSet rset = pstmt.executeQuery();
            invList.addAll(getInvoiceListFromReSet(rset, getTransList, getProds));
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return invList;
    }

    public static List<Invoice> getForDateRange( Date fromDate, Date toDate, String location, boolean getTransList, boolean getProds ) {
        MySQL sdb = new MySQL();
        String sql = "select inv.* from Invoices inv join InvoiceLocation invLoc on invLoc.invoiceNum = inv.invoiceNum" +
                " where invLoc.location = ?";

        String wSql = " and invoiceDate between '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate)+"%'\n"+
                "\t  and '"+new SimpleDateFormat("yyyy-MM-dd").format(toDate)+"%'\n";
        if(fromDate.equals(toDate)){
            wSql = " and invoiceDate like '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate)+"%'\n";
        }
        sql+=wSql;
        Connection con = null;
        List<Invoice> invList = new ArrayList<Invoice>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString(1,  location);
            ResultSet rset = pstmt.executeQuery();
            invList.addAll(getInvoiceListFromReSet(rset, getTransList, getProds));
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
        String invoiceDate = sdfInvoice.format(Calendar.getInstance().getTime());
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
        String invoiceDate = sdfInvoice.format(Calendar.getInstance().getTime());
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

    public static void saveInventoryCacheProperty(String invCacheId, Object property, String productNum, ProductCacheEnum cacheEnum) {
        if(invCacheId != null){
            DailySalesCache dailySalesCache = getInventoryCache(Long.parseLong(invCacheId));
            if(dailySalesCache != null){
                try {
                    Map prodMap = dailySalesCache.getMap().get(productNum);
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> map = prodMap != null?prodMap:new HashMap<String, Object>();
                    map.put(cacheEnum.getName(), property);
                    dailySalesCache.getMap().put(productNum, map);
                    saveInventoryCache(dailySalesCache);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

//    public static void saveInventoryCacheDone(String invCacheId, String done, Long productNum) {
//        if(invCacheId != null){
//            DailySalesCache dailySalesCache = getInventoryCache(Long.parseLong(invCacheId));
//            if(dailySalesCache != null){
//                try {
//                    Map prodMap = dailySalesCache.getMap().get(productNum);
//                    ObjectMapper mapper = new ObjectMapper();
//                    Map<String, Object> map = prodMap != null?prodMap:new HashMap<Map, Object>();
//                    map.put("done", "true".equals(done));
//                    dailySalesCache.getMap().put(productNum, map);
//                    saveInventoryCache(dailySalesCache);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    private static String cleanString(String json) {
        json = json.replaceAll("'", "\\\\'"); // escapes all ' (turns all ' into \')
        json = json.replaceAll("(?<!\\\\)\"", "'"); // turns all "bla" into 'bla'
        return json;
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
