//package com.soward.db;
//
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.Properties;
//
////import com.sybase.jdbc3.*;
//
///**
// * @author Scott Soward
// *
// */
//public class MySQL {
//    public MySQL() {
//        try {
//            Class.forName( "com.sybase.jdbc3.jdbc.SybDriver" );
//        } catch ( ClassNotFoundException cnfe ) {
//            System.out.println( "TestConnect cnfe: " + cnfe );
//        }
//    }
//
//    public java.sql.Connection getConn() {
//
//        Properties SysProps = new Properties();
//
//        SysProps.put( "user", "sforzando" );
//        SysProps.put( "password", "johnNarlette" );
//        java.sql.Connection con = null;
//        String port = "7110";
//        //local dev
//        boolean dev = false;
//
//
//
//        //dev = true;
//
//
//
//        String url = "jdbc:sybase:Tds:192.168.100.1:7110/Sforzando";
//        if(dev)
//            url = "jdbc:sybase:Tds:70.58.38.249:7110/Sforzando";
//        try {
//            con = DriverManager.getConnection( url, SysProps );
//            //          con.setAutoCommit( false ) ;
//        } catch ( SQLException e1 ) {
//            System.out.println( "DMM.MySQL-->getConn:");
//            e1.printStackTrace();
//        }
//        return con;
//    }
//
//    /**
//     * @param args
//     */
//    //roll back changes in inventoryReport and here.
//    public static void main( String[] args ) {
//    }
//}
