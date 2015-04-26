package com.soward.util;

import com.soward.db.MySQL;
import com.soward.enums.LocationsDBName;
import com.soward.object.Product;
import com.soward.object.ProductsLocationCount;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ProductsLocationCountUtil {

    /**
     * updates the specified locations count with given number
     * @param updatedCount
     * @param updateAvailableCountProductNum
     * @param location OREM, LEHI, MURRAY ...
     * @return
     */
    public static String updateCountForLocation(String updatedCount, String updateAvailableCountProductNum, String location ){
        String ll = location!=null&&(location.toUpperCase().equals("OREM"))?"DV":location;
        String message = "Updated count for "+StringUtils.capitalize(ll);
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "";
        try {
            PreparedStatement pstmt = null;
            con = sdb.getConn();
            sql = "update ProductsLocationCount set "+location+" = ? where productNum=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(updatedCount));
            pstmt.setInt(2, Integer.parseInt(updateAvailableCountProductNum));
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
            message = "update failed "+e.getMessage();
        }

        return message;
    }

    public static String addToCountForLocation(LocationsDBName location, int additionInventory, String productNum ){
        String message = "Updated count for "+StringUtils.capitalize(location.name());
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "";
        try {
            PreparedStatement pstmt = null;
            con = sdb.getConn();
            sql = "update ProductsLocationCount set "+location.capName()+" = ("+location.capName()+" + ?) where productNum = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, additionInventory);
            pstmt.setInt(2, Integer.parseInt(productNum));
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
            message = "update failed "+e.getMessage();
        }

        return message;
    }
    public static HashMap<String, ProductsLocationCount> getHashForList(String salesList){
        String[] list = salesList.split(",");
        List<Product> sales = new ArrayList<Product>();
        for(String prodNum: list){
            Product prod = new Product();
            prod.setProductNum(prodNum);
            sales.add(prod);
        }
        return getHashForList(sales);
    }



    /**
     * From a list of products, gets the productnumbers from list and returns 
     * Map productNum , productslocationcounts for each productnumber
     * <br/>
     * @param salesList
     * @return HashMap<String, ProductsLocationCount>
     */
    public static HashMap<String, ProductsLocationCount> getHashForList(List<Product> salesList){
        ArrayList<ProductsLocationCount> plcList = new ArrayList<ProductsLocationCount>();
        HashMap<String, ProductsLocationCount> plcHash = new HashMap<String, ProductsLocationCount>();
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from ProductsLocationCount where productNum in (";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            int count = 0;
            for(Product prod: salesList){
                count++;
                sql += prod.getProductNum()+",";
                if(count > 20){
                    sql = sql.substring(0, sql.length()-1)+")";
                    pstmt = con.prepareStatement(sql);
                    rset = pstmt.executeQuery();
                    plcList.addAll(getPLCResult(rset));
                    sql = "select * from ProductsLocationCount where productNum in (";
                    count = 0;
                }
            }
            if(count > 0){
                sql = sql.substring(0, sql.length()-1)+")";
                pstmt = con.prepareStatement(sql);
                rset = pstmt.executeQuery();
                plcList.addAll(getPLCResult(rset));
            }

            if(pstmt!=null)pstmt.close();
            con.close();
            for(ProductsLocationCount plc: plcList){
                plcHash.put(plc.getProductNum()+"", plc);
            }
        } catch ( Exception e ) {
            System.out.println("ERROR SQL: "+sql);
            e.printStackTrace();
        }
        return plcHash;
    }

    public static void save(ProductsLocationCount plc){
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "";
        int key = plc.getPlcNum();
        try {
            PreparedStatement pstmt = null;
            con = sdb.getConn();
            boolean productNumExists = fetchForProdNum( plc.getProductNum())!=null;
            if(plc.getPlcNum()>0||productNumExists){
                sql = "update ProductsLocationCount set productNum = ?, LEHI=?, OREM=?, MURRAY=?, LOC01=?, LOC02=?, DAYVIOLIN=? ";
                if(productNumExists){
                    sql += " where productNum = ?";
                }else if (plc.getPlcNum()>0){
                    sql += " where plcNum = ?";
                }else{
                	return;
                }
                pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, plc.getProductNum());
                pstmt.setInt(2, plc.getLEHI());
                pstmt.setInt(3, plc.getOREM());
                pstmt.setInt(4, plc.getMURRAY());

                pstmt.setInt(5, plc.getLoc01());
                pstmt.setInt(6, plc.getLoc02());
                pstmt.setInt(7, plc.getDAYVIOLIN());
                if(productNumExists){
                    pstmt.setInt(8, plc.getProductNum());
                }else{
                    pstmt.setInt(8, plc.getPlcNum());
                }
                pstmt.executeUpdate();
            }else{
                key = ProductsLocationCountUtil.getNextKey();
                sql = "insert into ProductsLocationCount values(?,?,?,?,?,?,?,?)";
                pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, key);
                pstmt.setInt(2, plc.getProductNum());
                pstmt.setInt(3, plc.getOREM());
                pstmt.setInt(4, plc.getLEHI());
                pstmt.setInt(5, plc.getMURRAY());
                pstmt.setInt(6, plc.getLoc01());
                pstmt.setInt(7, plc.getLoc02());
                pstmt.setInt(8, plc.getDAYVIOLIN());
                pstmt.executeUpdate();
            }
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            System.out.println("sql: "+sql);
            System.out.println("KEY: "+key);
            System.out.println("JProdNum: "+plc.getProductNum());
            e.printStackTrace();
        }
    }


    public static ProductsLocationCount getForProdNum(String productNum){
        ProductsLocationCount plc = new ProductsLocationCount();
        Connection con = null;
        MySQL sdb = new MySQL();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement("select * from ProductsLocationCount where productNum=?" );
            pstmt.setInt(1, Integer.parseInt(productNum));
            ResultSet rset = pstmt.executeQuery();
            ArrayList<ProductsLocationCount> pList = getPLCResult(rset);
            if(pList.isEmpty()){
                if(Utils.toInt(productNum)>0){
                    plc.setProductNum(Utils.toInt(productNum));
                    ProductsLocationCountUtil.save(plc);
                    return plc;
                }else{
                    return null;
                }
            }else{
                plc = pList.get(0);
            }
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return plc;
    }
    public static ProductsLocationCount fetchForPid(int pid){
        ProductsLocationCount plc = new ProductsLocationCount();
        Connection con = null;
        MySQL sdb = new MySQL();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement("select * from ProductsLocationCount where plcNum=?" );
            pstmt.setInt(1, pid);
            ResultSet rset = pstmt.executeQuery();
            if(rset!=null){
                ArrayList<ProductsLocationCount> asdf = getPLCResult(rset);
                if(asdf!=null&&!asdf.isEmpty()){
                	plc = asdf.get(0);
                }
            }
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return plc;
    }
    public static ProductsLocationCount fetchForProdNum(int pid){
        ProductsLocationCount plc = null;
        Connection con = null;
        MySQL sdb = new MySQL();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement("select * from ProductsLocationCount where productNum=?" );
            pstmt.setInt(1, pid);
            ResultSet rset = pstmt.executeQuery();
            if(rset!=null){
                ArrayList<ProductsLocationCount> asdf = getPLCResult(rset);
                if(asdf!=null&&!asdf.isEmpty()){
                    plc = asdf.get(0);
                }
            }
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return plc;
    }


    public static ArrayList<ProductsLocationCount> getPLCResult(ResultSet rset) throws Exception{
        ArrayList<ProductsLocationCount> plcList = new ArrayList<ProductsLocationCount>();
        while(rset.next()){
            ProductsLocationCount plc = new ProductsLocationCount();
            plc.setPlcNum(rset.getInt("plcNum"));
            plc.setProductNum(rset.getInt("productNum"));
            plc.setLEHI(rset.getInt("LEHI"));
            plc.setOREM(rset.getInt("OREM"));
            plc.setMURRAY(rset.getInt("MURRAY"));
            plc.setLoc01(rset.getInt("LOC01"));
            plc.setLoc02(rset.getInt("LOC02"));
            plc.setDAYVIOLIN(rset.getInt("DAYVIOLIN"));
            plcList.add(plc);
        }
        return plcList;
    }


    public static int getNextKey(){
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "";	
        int maxKey = 0;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement("select max(plcNum) maxKey from ProductsLocationCount" );
            ResultSet rset = pstmt.executeQuery();
            if(rset.next()){
                maxKey = rset.getInt("maxKey");
                maxKey++;
            }
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return maxKey;
    }
    //updates new product tables for an invoice number
    public static void updateProductCountForInvoiceNumber(){
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "";	
        try {
            List<ProductsLocationCount> plcList = new ArrayList<ProductsLocationCount>();
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement("select productNum, productQty from Transactions where invoiceNum in (441305)" );
            ResultSet rset = pstmt.executeQuery();
            while(rset.next()){
                ProductsLocationCount plc = new ProductsLocationCount();
                plc.setProductNum( rset.getInt("productNum") );
                plc.setMURRAY( rset.getInt("productQty") );
                plcList.add(plc);
            }
            for(ProductsLocationCount plc: plcList){
                pstmt = con.prepareStatement("update ProductsLocationCount set MURRAY = MURRAY + ? where productNum = ?" );
                pstmt.setInt(1, plc.getMURRAY());
                pstmt.setInt(2, plc.getProductNum());
                if(plc.getProductNum()!=8367)
                    pstmt.execute();
                System.out.println(plc.getProductNum()+" "+plc.getMURRAY());
            }
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Product> sortByTotalCount( ArrayList<Product> prodList ) {
        ArrayList<Product> sortList = new ArrayList<Product>();
        if(prodList==null||prodList.isEmpty()){
            return sortList;
        }
        HashMap<String, Product> prodMap = new HashMap<String, Product>();
        for(Product pp: prodList){
            prodMap.put( pp.getProductNum(), pp );
        }
        String sql = "select (OREM+LEHI+MURRAY) as tot, productNum from ProductsLocationCount where productNum in ("; 
        for(Product prod: prodList){
            sql += prod.getProductNum()+",";
        }
        sql = sql.substring( 0, sql.length()-1 );
        sql += ")";

        Connection con = null;
        MySQL sdb = new MySQL();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            //System.out.println("sql: "+sql);
            ResultSet rset = pstmt.executeQuery();
            while(rset.next()){
                String tot = rset.getString( "tot" );
                String num = rset.getString( "productNum" );
                Product pro = prodMap.get( num );
                prodMap.remove( num );
                pro.setNumAvailable( tot );
                sortList.add( pro );

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        //add any missing products that did not have counts for any location
        Set set = prodMap.keySet();
        Iterator<String> iter = set.iterator();
        while(iter.hasNext()){
            sortList.add( prodMap.get( iter.next() ) );
        }
        Collections.sort( sortList, new ProductComparator() );
        //        for(Product px: sortList){
        //            System.out.println(px.getProductNum()+" "+px.getNumAvailable());
        //        }

        return sortList;
    }

    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     * Comparator class to sort SResult by date DESC
     */
    static class ProductComparator implements Comparator<Product>{

        public int compare( Product o1, Product o2 ) {
            try{
                Integer d1 = new Integer( Utils.toInt( o1.getNumAvailable() ));
                Integer d2 = new Integer( Utils.toInt( o2.getNumAvailable() ));

                if ( d1 == null && d2 == null )
                    return 0;
                if ( d1 == null )
                    return 1;
                if ( d2 == null )
                    return -1;
                return d2.compareTo( d1 );
            } catch(Exception e ){
                return 0;
            }
        }

    }

        public static void main (String args[]){
        //		ProductsLocationCount plc = ProductsLocationCountUtil.getForProdNum("179236");
        //		System.out.println(plc.getProductNum());
//        Product p1 = new Product();p1.setProductNum( "200" );
//        Product p2 = new Product();p2.setProductNum( "202" );
//        Product p3 = new Product();p3.setProductNum( "301" );
//        Product p4 = new Product();p4.setProductNum( "302" );
//        Product p5 = new Product();p5.setProductNum( "303" );
//        Product p6 = new Product();p6.setProductNum( "305" );
//        Product p7 = new Product();p7.setProductNum( "306" );
//        Product p8 = new Product();p8.setProductNum( "179789" );
//        Product p9 = new Product();p9.setProductNum( "985308" );
//        Product p0 = new Product();p0.setProductNum( "310" );
//        ArrayList<Product> pList = new ArrayList<Product>();
//        pList.add( p1 );
//        pList.add( p2 );
//        pList.add( p3 );
//        pList.add( p4 );
//        pList.add( p5 );
//        pList.add( p6 );
//        pList.add( p7 );
//        pList.add( p8 );
//        pList.add( p9 );
//        pList.add( p0 );

//        ProductsLocationCountUtil.sortByTotalCount( pList);
//        ProductsLocationCountUtil.addToCountForLocation(LocationsDBName.OREM, -8, "180115");
    }

}
