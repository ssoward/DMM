package com.soward.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import com.soward.db.DB;
import com.soward.object.Client;
import com.soward.object.Traffic;

public class TrafficUtil {
    public String query;

    public TrafficUtil() {
        this.query = "";
    }

    public TrafficUtil( String que, String dte1, String dte2 ) {

    }

    public ArrayList getIPS( String dte ) {
        ArrayList<Traffic> traffic = new ArrayList<Traffic>();
        if ( dte != null ) {
            DB db = new DB();
            try {
                Connection conn = db.openConnection();
                Statement stm = conn.createStatement();
                ResultSet rset = stm.executeQuery( "select *, count(*) as IPcount from dmm where Time like '" + dte
                        + "%' group by ip order by IPcount desc" );
                while ( rset.next() ) {
                    Traffic cl = new Traffic();
                    cl.setTime( rset.getString( "Time" ) );
                    cl.setInfo( rset.getString( "Info" ) );
                    cl.setSession( rset.getString( "Session" ) );
                    cl.setIP( rset.getString( "IP" ) );
                    cl.setID( rset.getString( "ID" ) );
                    cl.setHits( rset.getString( "IPcount" ) );
                    traffic.add( cl );
                }
            } catch ( SQLException e ) {
                System.out.println( "TrafficUtil--->getIPS: " + e.toString() );
            }
        }
        return traffic;
    }

    public ArrayList getIPS( String dte1, String dte2 ) {
        ArrayList<Traffic> traffic = new ArrayList<Traffic>();
        if ( dte1 != null && dte2 != null ) {
            DB db = new DB();
            try {
                Connection conn = db.openConnection();
                Statement stm = conn.createStatement();
                String sql = "select *, count(*) as IPcount from dmm where Time > '" + dte1
                + "' and Time < '" + dte2 + "' group by ip order by IPcount desc";
                System.out.println(sql);
                ResultSet rset = stm.executeQuery( sql );
                while ( rset.next() ) {
                    Traffic cl = new Traffic();
                    cl.setTime( rset.getString( "Time" ) );
                    cl.setInfo( rset.getString( "Info" ) );
                    cl.setSession( rset.getString( "Session" ) );
                    cl.setIP( rset.getString( "IP" ) );
                    cl.setID( rset.getString( "ID" ) );
                    cl.setHits( rset.getString( "IPcount" ) );
                    traffic.add( cl );
                }
            } catch ( SQLException e ) {
                System.out.println( "TrafficUtil--->getIPS: " + e.toString() );
            }
        }
        return traffic;
    }

    public String getMaxHits( String dte ) {
        String max = "";
        DB db = new DB();
        try {
            Connection conn = db.openConnection();
            Statement stm = conn.createStatement();
            ResultSet rset = stm.executeQuery( "select count(ID) from dmm where Time like '" + dte + "%'" );
            while ( rset.next() ) {
                max = rset.getString( "count(ID)" );
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return max;
    }

}
