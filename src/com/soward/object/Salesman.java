package com.soward.object;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.soward.db.DB;

public class Salesman {

    private String firstName;

    private String lastName;

    private String Addr1;

    private String Addr2;

    private String hireDte;

    private String phone;

    private String email;

    private String reportsTo;

    private String lvlCommis;

    private String pid;

    public Salesman() {
    }

    public Salesman(

    String pid, String firstName, String lastName, String Addr1, String Addr2, String hireDte, String phone,
            String email, String reportsTo, String lvlCommis

    ) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.Addr1 = Addr1;
        this.Addr2 = Addr2;
        this.hireDte = hireDte;
        this.phone = phone;
        this.email = email;
        this.reportsTo = reportsTo;
        this.lvlCommis = lvlCommis;

    }

    public ArrayList<Salesman> getAllSalesman() {
        ArrayList<Salesman> clients = new ArrayList<Salesman>();

        DB db = new DB();
        try {
            Connection conn = db.openConnection();
            Statement stm = conn.createStatement();
            ResultSet rset = stm.executeQuery( "select * from salesman" );
            while ( rset.next() ) {
                Salesman cl = new Salesman();
                cl.setPid( rset.getString( "pid" ) );

                cl.setFirstName( rset.getString( "first_name" ) );
                cl.setLastName( rset.getString( "last_name" ) );
                cl.setaddr1( rset.getString( "addr1" ) );
                cl.setaddr2( rset.getString( "addr2" ) );
                cl.setHireDte( rset.getString( "date_hired" ) );
                cl.setPhone( rset.getString( "phone" ) );
                cl.setEmail( rset.getString( "email" ) );
                cl.setReportsTo( rset.getString( "reports_to" ) );
                cl.setLvlCommis( rset.getString( "comm_lvl" ) );

                clients.add( cl );
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return clients;

    }

    public void setPid( String str ) {
        this.pid = str;
    }

    public void setFirstName( String str ) {
        this.firstName = str;
    }

    public void setLastName( String str ) {
        this.lastName = str;
    }

    public void setaddr1( String str ) {
        this.Addr1 = str;
    }

    public void setaddr2( String str ) {
        this.Addr2 = str;
    }

    public void setHireDte( String str ) {
        this.hireDte = str;
    }

    public void setPhone( String str ) {
        this.phone = str;
    }

    public void setEmail( String str ) {
        this.email = str;
    }

    public void setReportsTo( String str ) {
        this.reportsTo = str;
    }

    public void setLvlCommis( String str ) {
        this.lvlCommis = str;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getAddr1() {
        return this.Addr1;
    }

    public String getAddr2() {
        return this.Addr2;
    }

    public String getHireDte() {
        return this.hireDte;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getEmail() {
        return this.email;
    }

    public String getReportsTo() {
        return this.reportsTo;
    }

    public String getLvlCommis() {
        return this.lvlCommis;
    }

    public static void main( String args[] ) {
        Salesman cc = new Salesman();
        ArrayList<Salesman> clients = cc.getAllSalesman();
        for ( Salesman temp : clients ) {
         temp.getFirstName();
         temp.getLastName();
         temp.getAddr1();
         temp.getAddr2();
         temp.getHireDte();
         temp.getPhone();
         temp.getEmail();
         temp.getReportsTo();
         temp.getLvlCommis();
         
         

        }
    }
}
