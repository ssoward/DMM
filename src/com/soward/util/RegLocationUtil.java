/**
 * Title:        RegLocationUtil.java
 * Description:  RegLocationUtil.java 
 * Copyright:    Copyright (c)  2009
 * @author 		 Scott Soward
 */
package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.soward.db.MySQL;
import com.soward.object.Location;
import com.soward.object.RegLocation;
import org.apache.commons.lang.StringUtils;

public class RegLocationUtil {

    private ArrayList<RegLocation> rList;
    public RegLocationUtil(){
        this.rList = RegLocationUtil.getRegLocations();
    }

    public ArrayList<RegLocation> getrList() {
        return rList;
    }
    public static void main(String arg[]){
        RegLocationUtil util = new RegLocationUtil();
        for(RegLocation l: util.getrList()){
            System.out.println(l.getRegName());
        }
    }

    public static ArrayList<RegLocation> getRegLocations( ) {
        String sql = "select locationNum, locationName, locationDescription, locationIP from Locations " +
                "where locationNum not in (2,10,11,12,13,14,15) order by locationName";
        ArrayList<RegLocation> rList = new ArrayList<RegLocation>();
        Connection con = null;
        try {
            MySQL db = new MySQL();
            con = db.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            while(rset.next()){
                RegLocation rl = new RegLocation();
                rl.setRegDesc( rset.getString( "locationDescription" ) );
                rl.setRegNum( rset.getString( "locationNum" ) );
                rl.setRegName( rset.getString( "locationName" ) );
                rl.setRegIP( rset.getString( "locationIP" ) );
                rList.add( rl );
            }
            con.close();
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return rList;
    }

    public static Location fetch(String pid){
        Connection con = null;
        MySQL sdb = new MySQL();
        if(StringUtils.isBlank(pid)){
            return null;
        }
        String sql = "select * from Locations where locationNum="+pid+"";

        Location loc = null;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            ArrayList<Location> lList = prcRset(rset);
            if(lList!=null && !lList.isEmpty()){
                loc = lList.get(0);
            }

            rset.close();
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return loc;
    }

    private static ArrayList<Location> prcRset(ResultSet rset) throws SQLException {
        ArrayList<Location> lList = new ArrayList<Location>();
        while(rset.next()){
            Location loc = new Location();
            loc.setLocationIP(rset.getString("locationIP"));
            loc.setLocationName(rset.getString("locationName"));
            loc.setLocationDescription(rset.getString("locationDescription"));
            loc.setLocationNum(rset.getString( "locationNum" ) );
            lList.add(loc);
        }
        return lList;

    }

    public RegLocation getRegLocation(String locId) {
        for(RegLocation reg: rList){
            if(reg.getRegNum().equals(locId)){
                return reg;
            }
        }
        return null;
    }
}
