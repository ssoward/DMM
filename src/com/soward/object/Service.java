package com.soward.object;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.soward.db.DB;

public class Service {

    private String pid;

    private String bvPhone;

    private String bvUserName;

    private String bvRegName;

    private String bvActDte;

    private String bvFee;

    public Service() {
    }

    public Service( String pid,

    String bvPhone, String bvUserName, String bvRegName, String bvActDte, String bvFee

    ) {

        this.bvPhone = bvPhone;
        this.bvUserName = bvUserName;
        this.bvRegName = bvRegName;
        this.bvActDte = bvActDte;
        this.bvFee = bvFee;

    }

    public ArrayList<Service> getAllSalesman() {
        ArrayList<Service> service = new ArrayList<Service>();

        DB db = new DB();
        try {
            Connection conn = db.openConnection();
            Statement stm = conn.createStatement();
            ResultSet rset = stm.executeQuery( "select * from service" );
            while ( rset.next() ) {
                Service cl = new Service();
                cl.setPid( rset.getString( "pid" ) );

                cl.setBvPhone( rset.getString( "bv_phone" ) );
                cl.setBvUserName( rset.getString( "bv_user_name" ) );
                cl.setBvRegName( rset.getString( "bv_reg_name" ) );
                cl.setBvActDte( rset.getString( "bv_act_date" ) );
                cl.setBvFee( rset.getString( "bv_base_amt" ) );

                service.add( cl );
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return service;

    }

    public void setPid( String str ) {
        this.pid = str;
    }

    public void setBvPhone( String str ) {
        this.bvPhone = str;
    }

    public void setBvUserName( String str ) {
        this.bvUserName = str;
    }

    public void setBvRegName( String str ) {
        this.bvRegName = str;
    }

    public void setBvActDte( String str ) {
        this.bvActDte = str;
    }

    public void setBvFee( String str ) {
        this.bvFee = str;
    }

    public String getPid() {
        return this.pid;
    }

    public String getBvPhone() {
        return this.bvPhone;
    }

    public String getBvUserName() {
        return this.bvUserName;
    }

    public String getBvRegName() {
        return this.bvRegName;
    }

    public String getBvActDte() {
        return this.bvActDte;
    }

    public String getBvFee() {
        return this.bvFee;
    }

    public static void main( String args[] ) {
        Service cc = new Service();
        ArrayList<Service> service = cc.getAllSalesman();
        for ( Service temp : service ) {
            temp.getPid();
            temp.getBvPhone();
            temp.getBvUserName();
            temp.getBvRegName();
            temp.getBvActDte();
            temp.getBvFee();

        }
    }
}
