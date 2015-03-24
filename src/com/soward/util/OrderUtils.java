package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.soward.db.DB;
import com.soward.object.Order;
import com.soward.object.Product;
import com.soward.object.ProductsLocationCount;

public class OrderUtils {
    public static void saveUpdateOrderList(List<Order> sList) {
        for(Order o: sList){
            saveUpdateOrder(o);
        }
    }
    public static void saveUpdateOrder(Order sData) {
        Connection con = null;
        DB db = new DB();

        //Order sup = null;
        try {
            String updateSql = "";
            con = db.openConnection();
            PreparedStatement pstmt = null;
            if(sData.getId()>0){
                updateSql = "update dmm.order set "+
                "product_num=?, "+
                "count=?, "+
                "date_eval=?, "+
                "date_email=?, "+
                "user=?, "+
                "location=?, "+
                "supplier_num=?, "+
                "date_received=?, "+
                "user_received=?, "+
                "note=? "+
                "where id=?";
                //				System.out.println(updateSql);
                //				System.out.println(sData.toString());
                pstmt = con.prepareStatement( updateSql );
                pstmt.setInt(1,     sData.getProdNum());
                pstmt.setInt(2,     sData.getCount());
                pstmt.setString(3,  sData.getDateEval()!=null? TransUtil.sdf.format(sData.getDateEval()):null);
                pstmt.setString(4,  sData.getDateEmail()!=null? TransUtil.sdf.format(sData.getDateEmail()):null);
                pstmt.setString(5,  sData.getUser());
                pstmt.setString(6,  sData.getLocation());
                pstmt.setInt(7,     sData.getSupplierNum());

                pstmt.setString(8,  sData.getDateReceived()!=null? TransUtil.sdf.format(sData.getDateReceived()):null);
                pstmt.setString(9,  sData.getUserReceived());
                pstmt.setString(10,  sData.getNotes());

                pstmt.setInt(11,     sData.getId());
                pstmt.executeUpdate();

            }else{
                updateSql = "insert into dmm.order (id, count, date_eval, date_email, user, location, supplier_num, product_num, date_received, user_received, note) values(null, ?,now(),null,?,?,?,?, null, null,null)";
                //System.out.println(updateSql);
                pstmt = con.prepareStatement( updateSql );
                pstmt.setInt(1,     sData.getCount());
                //pstmt.setDate(3,    (java.sql.Date) sData.getDateEval());
                //				pstmt.setDate(4,    (java.sql.Date) sData.getDateSent());
                pstmt.setString(2,  sData.getUser());
                pstmt.setString(3,  sData.getLocation());
                pstmt.setInt(4,     sData.getSupplierNum());
                pstmt.setInt(5,     sData.getProdNum());
                pstmt.executeUpdate();
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<Order> getAllForSup(String supId, String locationName){
        return getAllForSup(supId, false, true, locationName);
    }
    /**
     * 
     * @param supId supplierID
     * @param prodData boolean attach product data
     * @param notEmailed boolean fetch only orders that have not been emailed.
     * @return
     */
    public static ArrayList<Order> getAllForSup(String supId, boolean prodData, boolean notEmailed, String locationName){
        Connection con = null;
        ArrayList<Order> oList = new ArrayList<Order>();
        DB db = new DB();
        PreparedStatement pstmt = null;
        try{
            con = db.openConnection();
            String sql = "select * from dmm.order where supplier_num = ? and location=?";
            if(notEmailed){
                sql += " and date_email is null";
            }
            pstmt = con.prepareStatement( sql );
            pstmt.setInt(1, Integer.parseInt(supId));
            pstmt.setString(2, locationName);
            ResultSet rset = pstmt.executeQuery();
            oList = parseRSet(rset);
            if(prodData){
                oList = combineProducts(oList);
            }

            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return oList;
    }

    private static ArrayList<Order> combineProducts(ArrayList<Order> oList) {
        String whereIn = "";
        int count = 0;
        ArrayList<Product> pList = new ArrayList<Product>();
        for(Order o: oList){
            count++;
            whereIn = StringUtils.isBlank(whereIn)?"":whereIn+",";
            whereIn+=o.getProdNum()+"";
            if(count>200){
                pList.addAll(ProductUtils.fetchProductsForProdNums(whereIn, null));
                count = 0;
                whereIn = "";
            }
        }
        if(!StringUtils.isBlank(whereIn)){
            pList.addAll(ProductUtils.fetchProductsForProdNums(whereIn, null));
        }
        HashMap<String, Product> pMap = new HashMap<String, Product>();
        for(Product p: pList){
            pMap.put(p.getProductNum(), p);
        }

        for(Order o: oList){
            if(pMap.containsKey(o.getProdNum()+"")){
                o.setProd(pMap.get(o.getProdNum()+""));
            }
        }
        return oList;		
    }

    private static ArrayList<Order> parseRSet(ResultSet rset) {
        return parseRSet(rset, false);
    }

    private static ArrayList<Order> parseRSet(ResultSet rset, boolean getProdData) {
        ArrayList<Order> oList = new ArrayList<Order>();

        try {
            while(rset.next()){
                Order o = new Order();
                o.setId(rset.getInt(1));
                o.setCount(rset.getInt(2));
                o.setDateEval(rset.getTimestamp(3));
                o.setDateEmail(rset.getTimestamp(4));
                o.setUser(rset.getString(5));
                o.setLocation(rset.getString(6));
                o.setSupplierNum(rset.getInt(7));
                o.setProdNum(rset.getInt(8));

                o.setDateReceived(rset.getTimestamp(9));
                o.setUserReceived(rset.getString(10));
                o.setNotes(rset.getString(11));
                o.setOrderMethod(rset.getString(12));
                o.setDropped(rset.getString(13));
                o.setShippedMethod(rset.getString(14));
                o.setAccountNum(rset.getString(15));
                o.setDeliveryDate(rset.getTimestamp(16));

                oList.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(getProdData){
            oList = combineProducts(oList);
        }
        return oList;
    }
    public static Order getOrderForProdNum(String prodNum, String supNum) {        Connection con = null;
        Order ord = new Order();
        DB db = new DB();
        PreparedStatement pstmt = null;
        try{
            con = db.openConnection();
            String sql = "select * from dmm.order where supplier_num = ? and product_num= ?";
            pstmt = con.prepareStatement( sql );
            pstmt.setInt(1, Integer.parseInt(supNum));
            pstmt.setInt(2, Integer.parseInt(prodNum));
            ResultSet rset = pstmt.executeQuery();
            ArrayList<Order> oList = parseRSet(rset);
            if(oList!=null&&!oList.isEmpty()){
                ord = oList.get(0);
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ord;
    }
    public static Order fetchOrder(String orderNum) {        Connection con = null;
        Order ord = new Order();
        DB db = new DB();
        PreparedStatement pstmt = null;
        try{
            con = db.openConnection();
            String sql = "select * from dmm.order where id = ?";
            pstmt = con.prepareStatement( sql );
            pstmt.setInt(1, Integer.parseInt(orderNum));
            ResultSet rset = pstmt.executeQuery();
            ArrayList<Order> oList = parseRSet(rset);
            //System.out.println(sql+" "+orderNum);
            if(oList!=null&&!oList.isEmpty()){
                ord = oList.get(0);
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ord;
    }

    public static Order fetchOrderForProdLocation(String prodNum,
            String locationName) {        Connection con = null;
        Order ord = new Order();
        DB db = new DB();
        PreparedStatement pstmt = null;
        try{
            con = db.openConnection();
            String sql = "select * from dmm.order where product_num = ? and location =?";
            pstmt = con.prepareStatement( sql );
            pstmt.setInt(1, Integer.parseInt(prodNum));
            pstmt.setString(2, locationName);
            ResultSet rset = pstmt.executeQuery();
            ArrayList<Order> oList = parseRSet(rset);
            if(oList!=null&&!oList.isEmpty()){
                ord = oList.get(0);
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ord;
    }
    public static HashMap<String, ArrayList<Order>> getAllOrders(String locationName, boolean received) {        HashMap<String, ArrayList<Order>> oMap = new HashMap<String, ArrayList<Order>>();
        Connection con = null;
        DB db = new DB();
        PreparedStatement pstmt = null;
        try{
            con = db.openConnection();
            String sql = "select * from dmm.order where location=?";
            sql += " and date_email is not null";
            if(!received){
                sql += " and date_received is null";
            }else{
                sql += " and date_received is not null";
            }
            //System.out.println(sql);
            pstmt = con.prepareStatement( sql );
            pstmt.setString(1, locationName);
            ResultSet rset = pstmt.executeQuery();
            ArrayList<Order> oList = parseRSet(rset, true);

            for(Order o: oList){
                String key = o.getSupplierNum()+"";
                if(oMap.containsKey(key)){
                    oMap.get(key).add(o);
                }else{
                    ArrayList<Order> toList = new ArrayList<Order>();
                    toList.add(o);
                    oMap.put(key, toList);
                }
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return oMap;
    }

    public static void deleteOrderForSupAndProd(List<Order> asList) {
        Connection con = null;
        HashMap<String, Order> allSups = new HashMap<String, Order>();
        DB db = new DB();

        //Order sup = null;
        try {
            String updateSql = "";
            con = db.openConnection();
            PreparedStatement pstmt = null;

            updateSql = "delete from dmm.order where supplier_num=? and product_num=?";
            pstmt = con.prepareStatement( updateSql );
            for(Order sData: asList){
                pstmt.setInt(1,     sData.getSupplierNum());
                pstmt.setInt(2,     sData.getProdNum());
                pstmt.executeUpdate();
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static HashMap<String,String> getOrderSumsBySupNum(String locationName) {
        HashMap<String,String> oMap = new HashMap<String,String>();
        Connection con = null;
        Order ord = new Order();
        DB db = new DB();
        PreparedStatement pstmt = null;
        try{
            con = db.openConnection();
            String sql = "select distinct(supplier_num) from dmm.order where date_email is null and location='"+locationName+"'";

            pstmt = con.prepareStatement( sql );

            ResultSet rset = pstmt.executeQuery();
            while(rset.next()){
                String supId = rset.getString("supplier_num");
                ArrayList<Order> oL = getAllForSup(supId, true, true, locationName);
                double cco = 0.0;

                for(Order o: oL){
                    if(o!=null&&o.getProd()!=null&&o.getProd().getProductCost1()!=null){
                        String rr = o.getProd().getProductCost1();
                        if(!StringUtils.isBlank(rr)){
                            try{cco += Double.parseDouble(rr)*o.getCount();}catch(Exception e){}
                        }
                    }
                }
                String cat = cco+"";
                oMap.put(supId, cat);
                //System.out.println(supId+"   "+cat);
            }

            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return oMap;
    }
    public static void updateSupForEmailSent(String supId, String locationName, String userName) {
        Connection con = null;
        HashMap<String, Order> allSups = new HashMap<String, Order>();
        DB db = new DB();

        //Order sup = null;
        try {
            String updateSql = "";
            con = db.openConnection();
            PreparedStatement pstmt = null;
            updateSql = "update dmm.order set date_email = now(), user=? where date_email is null and supplier_num=? and location=?";
            //			System.out.println(updateSql+" \n"+userName
            //					+"\n"+supId
            //					+"\n"+locationName);
            pstmt = con.prepareStatement( updateSql );
            pstmt.setString(1, userName);
            pstmt.setInt(2,    Integer.parseInt(supId));
            pstmt.setString(3, locationName);
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main (String args[]){
        Order sData = OrderUtils.getAllForSup("27", "OREM").get(0); //new Order();
        sData.setCount(200);
        sData.setSupplierNum(27);
        sData.setProdNum(160013);
        sData.setLocation("MURRAY");
        sData.setUser("ssoward");
        //sData.setId(51);
        //		OrderUtils.saveUpdateOrder(sData);
        OrderUtils.getOrderSumsBySupNum("OREM");
        //OrderUtils.deleteSupProdOrder("11");
        //		List<Order>oList = OrderUtils.getAllForSup("27");
        //		for(Order o: oList){
        //			System.out.println(o.getProd().getProductCatalogNum());
        //		}
    }

    public static void deleteSupProdOrder(String orderId) {
        Connection con = null;
        DB db = new DB();
        try {
            String updateSql = "";
            con = db.openConnection();
            PreparedStatement pstmt = null;
            updateSql = "delete from dmm.order where id=?";
            pstmt = con.prepareStatement( updateSql );
            pstmt.setInt(1,     Integer.parseInt(orderId));
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void deleteAllSupProdOrder(String supNum, String loca) {
        Connection con = null;
        DB db = new DB();
        try {
            String updateSql = "";
            con = db.openConnection();
            PreparedStatement pstmt = null;
            updateSql = "delete from dmm.order where supplier_num=? and location=?";
            pstmt = con.prepareStatement( updateSql );
            pstmt.setInt(1,     Integer.parseInt(supNum));
            pstmt.setString(2,  loca);
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Order toggleOrderArrvied(String orderNum, String userName) {
        Order o = new Order();
        try {
            String updateSql = "";
            o = fetchOrder(orderNum);
            ProductsLocationCount plc = ProductsLocationCountUtil.fetchForProdNum(o.getProdNum());
            int count = o.getCount();
            String loca = o.getLocation();
            if(o.getDateReceived()!=null){
                o.setDateReceived(null);
                o.setUserReceived("");
                //remove count
                count = count*-1;
            }else{
                Calendar cal = Calendar.getInstance();
                o.setDateReceived(cal.getTime());
                o.setUserReceived(userName);
            }
            if(o.getLocation().equals("OREM")){
                plc.setOREM(plc.getOREM()+count);
            }
            if(o.getLocation().equals("MURRAY")){
                plc.setMURRAY(plc.getMURRAY()+count);
            }
            if(o.getLocation().equals("LEHI")){
                plc.setLEHI(plc.getLEHI()+count);
            }
            ProductsLocationCountUtil.save(plc);
            saveUpdateOrder(o);
        }catch(Exception e){
            e.printStackTrace();
        }
        return o;
    }

}
