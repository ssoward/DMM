//package com.soward.util;
//
//import com.soward.db.MySQL;
//import com.soward.object.Invoice;
//import com.soward.object.Transaction;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.List;
//
///**
// * Created by ssoward on 9/27/14.
// */
//public class RepairDB {
//
//this class was created to make transactions.transNum unique
//    ALTER TABLE `dmm`.`Transactions` ADD UNIQUE INDEX `transNum_UNIQUE` (`transNum` ASC);
//
//    public static void main(String args[]) {
//        List<Transaction> transList = getDups();
//        //get all duplicate transNum
//        for(Transaction tran: transList){
////            System.out.println( tran.getTransNum());
//            //get duplicate for specific transNum
//            List<Transaction> specificDups = getDups(tran.getTransNum());
//            for(Transaction dup: specificDups) {
//                updateTransNum(dup);
//                System.out.println( "Updated: "+dup.getTransNum());
//            }
//
//        }
//    }
//
//    private static void updateTransNum(Transaction dup) {
//        String updateDup = "update Transactions set transNum = ? where invoiceNum=? and transType = ? and productNum = ? and productQty = ? and transDate = ?";
//        Connection con = null;
//        MySQL sdb = new MySQL();
//        try {
//            con = sdb.getConn();
//            PreparedStatement pstmt = null;
//            pstmt = con.prepareStatement( updateDup );
//
//            pstmt.setString(1, getNextTransNum());
//            pstmt.setString(2, dup.getInvoiceNum());
//            pstmt.setString(3, dup.getTransType());
//            pstmt.setString(4, dup.getProductNum());
//            pstmt.setString(5, dup.getProductQty());
//            pstmt.setString(6, dup.getTransDate());
//
//            pstmt.executeUpdate();
//            pstmt.close();
//            con.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private static String getNextTransNum() {
//        Connection con = null;
//        MySQL sdb = new MySQL();
//        String getDups = "select max(transNum)+1 next from Transactions";
//        String next = null;
//        try {
//            con = sdb.getConn();
//            PreparedStatement pstmt = null;
//            pstmt = con.prepareStatement( getDups );
//
//            ResultSet rset = pstmt.executeQuery();
//            if(rset.next()) {
//                next = rset.getString("next");
//            }
//            pstmt.close();
//            con.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return next;
//    }
//
//    private static List<Transaction> getDups(String transNum) {
//        Connection con = null;
//        MySQL sdb = new MySQL();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss'.0'");
//        List<Transaction> transList = null;
//        String getDups = "select * from Transactions where transNum = ?";
//        Invoice inv = null;
//        try {
//            con = sdb.getConn();
//            PreparedStatement pstmt = null;
//            pstmt = con.prepareStatement( getDups );
//
//            pstmt.setString(1, transNum);
//
//            ResultSet rset = pstmt.executeQuery();
//            transList = TransUtil.getForRSet(rset);
//            pstmt.close();
//            con.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return transList;
//    }
//
//    public static List<Transaction> getDups(){
//        Connection con = null;
//        MySQL sdb = new MySQL();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss'.0'");
//        List<Transaction> transList = null;
//        String getDups = "select transNum, count(*) from Transactions group by transNum HAVING count(*)>1";
//        Invoice inv = null;
//        try {
//            con = sdb.getConn();
//            PreparedStatement pstmt = null;
//            pstmt = con.prepareStatement( getDups );
//
//            ResultSet rset = pstmt.executeQuery();
//            transList = TransUtil.getForRSet(rset);
//            pstmt.close();
//            con.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return transList;
//    }
//}
