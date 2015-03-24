package com.soward.object;

import com.soward.db.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Pages {

    private String pid;

    private String page;

    private String page_preview;

    private String page_date;

    public Pages() {
    }

    public Pages( String pid,

    String page, String page_preview, String page_date

    ) {

        this.pid = pid;
        this.page = page;
        this.page_preview = page_preview;
        this.page_date = page_date;

    }

    public ArrayList<Pages> getAllPages() {
        ArrayList<Pages> page = new ArrayList<Pages>();

        DB db = new DB();
        try {
            Connection conn = db.openConnection();
            Statement stm = conn.createStatement();
            ResultSet rset = stm.executeQuery( "select * from pages" );
            while ( rset.next() ) {
                Pages cl = new Pages();

                cl.setPid( rset.getString( "pid" ) );
                cl.setPage( rset.getString( "page" ) );
                cl.setPage_preview( rset.getString( "page_preview" ) );
                cl.setPage_date( rset.getString( "page_date" ) );

                page.add( cl );
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return page;

    }
    public Pages getPageByID(String pid) {
        Pages cl = new Pages();
        
        DB db = new DB();
        try {
            Connection conn = db.openConnection();
            Statement stm = conn.createStatement();
            ResultSet rset = stm.executeQuery( "select * from pages where pid='"+pid+"'" );
            while ( rset.next() ) {
                
                cl.setPid( rset.getString( "pid" ) );
                cl.setPage( rset.getString( "page" ) );
                cl.setPage_preview( rset.getString( "page_preview" ) );
                cl.setPage_date( rset.getString( "page_date" ) );
                
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return cl;
        
    }

    public void setPid( String str ) {
        this.pid = str;
    }

    public void setPage( String str ) {
        this.page = str;
    }

    public void setPage_preview( String str ) {
        this.page_preview = str;
    }

    public void setPage_date( String str ) {
        this.page_date = str;
    }

    public String getPid() {
        return this.pid;
    }

    public String getPage() {
        return this.page;
    }

    public String getPage_preview() {
        return this.page_preview;
    }

    public String getPage_date() {
        return this.page_date;
    }

    public static void main( String args[] ) {
        Pages cc = new Pages();
        ArrayList<Pages> service = cc.getAllPages();
        for ( Pages temp : service ) {
            temp.getPid();
            temp.getPage();
            temp.getPage_preview();
            temp.getPage_date();
            
        }
        
    }
}
