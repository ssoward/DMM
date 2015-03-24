/**
 * Title:        DescriptionsUtil.java
 * Description:  DescriptionsUtil.java 
 * Copyright:    Copyright (c)  2008
 * Company:      Meridias Capital Inc.
 * @author 		 Scott Soward     
 */
package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.soward.db.MySQL;
import com.soward.object.Descriptions;
import com.soward.object.Events;

public class DescriptionsUtil {

    

    public static Descriptions getDescriptionsForDescriptionCode(String descriptionsCode){
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Descriptions where descriptionsCode = "+descriptionsCode;
        Descriptions description = new Descriptions();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            if ( rset.next() ) {
                description.setDescriptionCode( rset.getString("descriptionCode" ));
                description.setDepartmentCode( rset.getString("departmentCode"));
                description.setDescriptionName( rset.getString("descriptionName"));
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return description;
    }
    
    public static List<Descriptions> getAll2CodeDescriptions(String deptCode){
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Descriptions where departmentCode = '"+deptCode+"' order by descriptionName";
        List<Descriptions> descriptionList = new ArrayList<Descriptions>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            while ( rset.next() ) {
                Descriptions description = new Descriptions();
                description.setDescriptionCode( rset.getString("descriptionCode" ));
                description.setDepartmentCode( rset.getString("departmentCode"));
                description.setDescriptionName( rset.getString("descriptionName"));
                descriptionList.add( description );
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return descriptionList;
    }
    /**
     * get a hash of dept codes/names in form:
     * HashMap<descriptionCode+departmentCode, descriptionName>()
     * @return
     */
    public static HashMap<String, String> getHashCodes(){
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Descriptions order by descriptionName";
        HashMap<String,String> hash = new HashMap<String, String>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            while ( rset.next() ) {
                String descCode = rset.getString("descriptionCode" );
                String depaCode = rset.getString("departmentCode");
                String descName = rset.getString("descriptionName");
                hash.put( descCode+depaCode, descName );
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return hash;
    }
   
    
    /**
     * @param args
     */
    public static void main( String[] args ) {
        for(Descriptions des: DescriptionsUtil.getAll2CodeDescriptions("2")){
            System.out.println(des.getDescriptionName());
        }

    }

}
