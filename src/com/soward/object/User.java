package com.soward.object;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.soward.db.DB;

public class User {
    private String pid;

    private String name;

    private String pass;

    private String first;

    private String last;

    private String email;

    private String role;

    private String phone;

    public User() {
    }

    public User( String pid, String name, String pass, String first, String last, String email, String role,
            String phone ) {

        this.pid = pid;
        this.name = name;
        this.pass = pass;
        this.first = first;
        this.last = last;
        this.email = email;
        this.role = role;
        this.phone = phone;
    }

    public ArrayList<User> getAllUser() {
        ArrayList<User> user = new ArrayList<User>();

        DB db = new DB();
        try {
            Connection conn = db.openConnection();
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM users ";// join user_roles on
            // user_roles.user_pid=users.user_pid";
            ResultSet rset = stm.executeQuery( sql );
            while ( rset.next() ) {
                User cl = new User();
                cl.setPid( rset.getString( "user_pid" ) );
                cl.setName( rset.getString( "user_name" ) );
                cl.setPass( rset.getString( "user_pass" ) );
                cl.setFirst( rset.getString( "user_first" ) );
                cl.setLast( rset.getString( "user_last" ) );
                cl.setEmail( rset.getString( "user_email" ) );
                cl.setRole( rset.getString( "user_role" ) );
                cl.setPhone( rset.getString( "user_phone" ) );

                user.add( cl );
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return user;
    }
    // get master userPid
    public User getUser(String suer) {
        
        DB db = new DB();
        User cl = new User();
        try {
            Connection conn = db.openConnection();
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM users where user_name='"+suer+"'";// join user_roles on
            // user_roles.user_pid=users.user_pid";
            ResultSet rset = stm.executeQuery( sql );
            while ( rset.next() ) {
                cl.setPid( rset.getString( "user_pid" ) );
                cl.setName( rset.getString( "user_name" ) );
                cl.setPass( rset.getString( "user_pass" ) );
                cl.setFirst( rset.getString( "user_first" ) );
                cl.setLast( rset.getString( "user_last" ) );
                cl.setEmail( rset.getString( "user_email" ) );
                cl.setRole( rset.getString( "user_role" ) );
                cl.setPhone( rset.getString( "user_phone" ) );

            }
        } catch ( SQLException e ) {
            System.out.println( "User-->getUser: " + e.toString() );
            e.printStackTrace();
        }
        return cl;
    }

    // returns user with provided pid.
    public User fetchUser( String pid ) {

        DB db = new DB();
        User cl = new User();
        try {
            Connection conn = db.openConnection();
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM users where user_pid='" + pid + "'";// join
            // user_roles
            // on
            // user_roles.user_pid=users.user_pid";
            ResultSet rset = stm.executeQuery( sql );
            while ( rset.next() ) {
                cl.setName( rset.getString( "user_name" ) );
                cl.setPass( rset.getString( "user_pass" ) );
                cl.setFirst( rset.getString( "user_first" ) );
                cl.setLast( rset.getString( "user_last" ) );
                cl.setEmail( rset.getString( "user_email" ) );
                cl.setRole( rset.getString( "user_role" ) );
                cl.setPhone( rset.getString( "user_phone" ) );

            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return cl;
    }

    public boolean deleteUser( String pid ) {
        DB db = new DB();
        boolean deleted = true;
        try {
            Connection conn = db.openConnection();
            Statement stm = conn.createStatement();
            String sql = "delete FROM users where user_pid='" + pid + "'";
            int removed = stm.executeUpdate( sql );
        } catch ( SQLException e ) {
            deleted = false;
            System.out.println( "User-->deleteUser: " + e.toString() );
        }
        return deleted;
    }

    public void setPid( String str ) {
        this.pid = str;
    }

    public boolean isAdmin() {
        return ( this.getRole().equalsIgnoreCase( "admin" ) );
    }

    public void setName( String str ) {
        this.name = str;
    }

    public void setPass( String str ) {
        this.pass = str;
    }

    public void setFirst( String str ) {
        this.first = str;
    }

    public void setLast( String str ) {
        this.last = str;
    }

    public void setEmail( String str ) {
        this.email = str;
    }

    public void setRole( String str ) {
        this.role = str;
    }

    public void setPhone( String str ) {
        this.phone = str;
    }

    public String getPid() {
        return this.pid;
    }

    public String getName() {
        return this.name;
    }

    public String getPass() {
        return this.pass;
    }

    public String getFirst() {
        return this.first;
    }

    public String getLast() {
        return this.last;
    }

    public String getEmail() {
        return this.email;
    }

    public String getRole() {
        return this.role;
    }

    public String getPhone() {
        return this.phone;
    }

    public static void main( String[] args ) {
        User user = new User();
        ArrayList<User> uu = user.getAllUser();
    }

}
