package com.soward.object;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.soward.db.DB;

public class Client {
    private String pid;

    private String firstName;

    private String lastName;

    private String addr1;

    private String addr2;

    private String email;

    private String homePage;

    private String compName;

    private String phone;

    public Client() {
    }

    public Client( String pid, String firstName, String lastName, String addr1, String addr2,
            String email, String homePage, String compName, String phone ) {

        this.pid = pid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.email = email;
        this.homePage = homePage;
        this.compName = compName;
        this.phone = phone;

    }

    public ArrayList<Client> getAllClients() {
        ArrayList<Client> clients = new ArrayList<Client>();

        DB db = new DB();
        try {
            Connection conn = db.openConnection();
            Statement stm = conn.createStatement();
            ResultSet rset = stm.executeQuery( "select * from client" );
            while ( rset.next() ) {
                Client cl = new Client();
                cl.setPid( rset.getString( "pid" ) );
                cl.setFristName( rset.getString( "first_name" ) );
                cl.setLastName( rset.getString( "last_name" ) );
                cl.setAddr1( rset.getString( "addr1" ) );
                cl.setAddr2( rset.getString( "addr2" ) );
                cl.setEmail( rset.getString( "email" ) );
                cl.setHomePage( rset.getString( "homepage" ) );
                cl.setCompName( rset.getString( "company" ) );
                cl.setPhone( rset.getString( "phone" ) );
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

    public void setFristName( String str ) {
        this.firstName = str;
    }

    public void setLastName( String str ) {
        this.lastName = str;
    }

    public void setAddr1( String str ) {
        this.addr1 = str;
    }

    public void setAddr2( String str ) {
        this.addr2 = str;
    }

    public void setEmail( String str ) {
        this.email = str;
    }

    public void setHomePage( String str ) {
        this.homePage = str;
    }

    public void setCompName( String str ) {
        this.compName = str;
    }

    public void setPhone( String str ) {
        this.phone = str;
    }

    public String getPid() {
        return this.pid;
    }

    public String getFristName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getAddr1() {
        return this.addr1;
    }

    public String getAddr2() {
        return this.addr2;
    }

    public String getEmail() {
        return this.email;
    }

    public String getHomePage() {
        return this.homePage;
    }

    public String getCompName() {
        return this.compName;
    }

    public String getPhone() {
        return this.phone;
    }
    public static void main( String args[] ) {
        Client cc = new Client();
        ArrayList<Client> clients = cc.getAllClients();
        for (Client temp: clients){
//            temp.getFristName()+" "+temp.getLastName();
//            temp.getAddr1()+" "+temp.getAddr2();
            temp.getEmail();
            temp.getHomePage();
            temp.getCompName();
            temp.getPhone();           
            
        }
    }
}
