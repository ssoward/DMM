/**
 * Title:        DepartmentsUtil.java
 * Description:  DepartmentsUtil.java 
 * Copyright:    Copyright (c)  2008
 * Company:      Meridias Capital Inc.
 * @author 		 Scott Soward     
 */
package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.soward.db.MySQL;
import com.soward.object.Departments;

public class DepartmentsUtil {
    public static Departments getDepartmentsForDepartmentsCode(String departmentsCode){
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Departments where departmentsCode = "+departmentsCode;
        Departments departments = new Departments();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            if ( rset.next() ) {
                departments.setDepartmentCode( rset.getString("departmentCode" ));
                departments.setDepartmentName( rset.getString("departmentName"));
                departments.setDepartmentHead( rset.getString("departmentHead"));
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return departments;
    }
    public static List<Departments> getAllDepartments(){
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Departments ";
        List<Departments> departmentsList = new ArrayList<Departments>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            while ( rset.next() ) {
                Departments departments = new Departments();
                departments.setDepartmentCode( rset.getString("departmentCode" ));
                departments.setDepartmentName( rset.getString("departmentName"));
                departments.setDepartmentHead( rset.getString("departmentHead"));
                departmentsList.add( departments );
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return departmentsList;
    }
    /**
     * @param args
     */
    public static void main( String[] args ) {
        for(Departments dept: DepartmentsUtil.getAllDepartments()){
            System.out.println(dept.getDepartmentName());
        }
    }

}
