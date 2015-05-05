package com.soward.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.soward.db.MySQL;
import com.soward.object.WebProductInfo;

public class WebProductInfoUtil {



    public static String saveOrUpdate( WebProductInfo webProdInfo) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String result = "Successfully updated blurb text for productNumber: " + webProdInfo.getProductNum();
        try {
            String sql = "SELECT    *   "
                + " FROM WebProductInfo where productNum=?";
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString( 1, webProdInfo.getProductNum() );
            ResultSet rset = pstmt.executeQuery();
            if(rset.next()){
                //update
                sql = "update WebProductInfo set productBlurb=?"+
                ", productFeature =? where productNum =?";
                pstmt.close();
                pstmt = con.prepareStatement( sql );
                pstmt.setString( 1, webProdInfo.getProductBlurb() );
                pstmt.setString( 2, webProdInfo.getProductFeature() );
                pstmt.setString( 3, webProdInfo.getProductNum() );
                pstmt.executeUpdate();
            }
            else{
                //insert
                sql = "insert into WebProductInfo (productBlurb, productFeature, productNum)" +
                "values (?,?,?)";
                pstmt.close();
                pstmt = con.prepareStatement( sql );
                pstmt.setString( 1, webProdInfo.getProductBlurb() );
                pstmt.setString( 2, webProdInfo.getProductFeature() );
                pstmt.setString( 3, webProdInfo.getProductNum() );
                pstmt.executeUpdate();
            }

            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return result;
    }

    public static WebProductInfo fetchForProdNum(String productNum){
        WebProductInfo wpi = new WebProductInfo();
        Connection con = null;
        MySQL sdb = new MySQL();
        try {
            String sql = "SELECT    *   "
                + " FROM WebProductInfo where productNum=?";
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString( 1, productNum );
            ResultSet rset = pstmt.executeQuery();
            while(rset.next()){
                wpi.setProductBlurb( rset.getString( "productBlurb" ) );
                wpi.setProductFeature( rset.getString( "productFeature" ) );
                wpi.setProductNum( productNum );
            }

            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return wpi;
    }
    public static ArrayList<WebProductInfo> fetchForProdNumTEST(int fromNum, int toNum){
        Connection con = null;
        ArrayList<WebProductInfo> wpList = new ArrayList<WebProductInfo>();
        MySQL sdb = new MySQL();
        String result = "Successfully updated product for productNumber";
        try {
            //SELECT * FROM WebProductInfo where convert(int, productNum) < 1000
            String sql = "SELECT    *   "
                + " FROM WebProductInfo where convert(int, productNum)>? and convert(int, productNum)<?";
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setInt( 1, fromNum );
            pstmt.setInt( 2, toNum );
            ResultSet rset = pstmt.executeQuery();
            while(rset.next()){
                WebProductInfo wpi = new WebProductInfo();
                wpi.setProductBlurb( rset.getString( "productBlurb" ) );
                wpi.setProductFeature( rset.getString( "productFeature" ) );
                wpi.setProductNum( rset.getString( "productNum" ) );
                wpList.add( wpi );
            }

            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return wpList;
    }
    /**
     * @param args
     */
    public static void main( String[] args ) {
//      WebProductInfo wpi = new WebProductInfo();
//      wpi.setProductBlurb( "hello world");
//      wpi.setProductFeature( "some new cool feature" );
//      wpi.setProductNum( "162043" );
//      //WebProductInfoUtil.saveOrUpdate( wpi );
//      //WebProductInfoUtil.saveOrUpdate( wpi );
//      // TODO Auto-generated method stub
//      wpi = WebProductInfoUtil.fetchForProdNum( wpi.getProductNum() );
//      System.out.println(wpi.getProductBlurb());
        ArrayList<WebProductInfo> wList = WebProductInfoUtil.fetchForProdNumTEST( 0, 9905000 );
        ArrayList<WebProductInfo> fixList = new ArrayList<WebProductInfo>();
        try{
            // Create file 
            FileWriter fstream = new FileWriter("/Users/scottsoward/deleteMe.txt");
            BufferedWriter out = new BufferedWriter(fstream);
        for(WebProductInfo wp: wList){
            if(wp.getProductBlurb().contains( " ? " )){
                fixList.add( wp );
            }
        }
        System.out.println("fixed: "+fixList.size()+" == 1514");
        System.out.println(wList.size());
        System.out.println("-----------------------");
        System.out.println("-----------------------");
        System.out.println("-----------------------");
        for(WebProductInfo info: fixList){
            System.out.println(info.getProductNum());
            String note = "------------------------------- \nSaving: "+info.getProductNum();
            String fixed = info.getProductBlurb().replace( " ? ", " * " );
            out.write(note+"\n");
            out.write("OLD: "+info.getProductBlurb()+"\n");
            info.setProductBlurb( fixed );
            out.write("NEW: "+info.getProductBlurb()+"\n");
            WebProductInfoUtil.saveOrUpdate( info );
            out.write("Updated successfully: "+info.getProductNum()+"\n");
        }
//        WebProductInfo info = WebProductInfoUtil.fetchForProdNum( "1099" );
//        System.out.println(info.getProductBlurb());
        out.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

}
