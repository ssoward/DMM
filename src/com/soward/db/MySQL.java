package com.soward.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: ssoward
 * Date: 11/19/13
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class MySQL {
    static {
        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        } catch ( ClassNotFoundException cnfe ) {
            throw new RuntimeException( cnfe );
        }
    }

    // Creates connections to the database.

    public static Connection getConn() throws SQLException {
        //daymurray -pkCehjiYSe9pYhwgIZkUt0QdTDNnpaxJro2edV1k8Qn5suS8PBgtrgDOz40Y0J02
//        return DriverManager.getConnection("jdbc:mysql://192.168.100.1:3306/dmm", "daymurray", "kCehjiYSe9pYhwgIZkUt0QdTDNnpaxJro2edV1k8Qn5suS8PBgtrgDOz40Y0J02");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/dmm", "root", "asdfg123");
    }
}
