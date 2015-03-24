package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.soward.db.DB;
import com.soward.object.Bugs;

public class BugsUtil {


    public static String saveBug( Bugs bug ) {
        Connection con = null;
        String sql = "";
        String saveResult = "Saved request successfully";
        try {
            DB db = new DB();
            bug.setBugStatus( "new" );
            con = db.openConnection();
            PreparedStatement pstmt = null;
            if(bug.getBugReporter()!=null&&bug.getBugReporter().length()>0&&
                    bug.getBugDesc()!=null&&bug.getBugDesc().length()>0&&
                    bug.getBugTitle()!=null&&bug.getBugTitle().length()>0){
                
                sql = "insert into bugs (bug_id, bug_title, bug_reporter, bug_desc, bug_status, bug_date) "+
                "values (null, ?, ?, ?, ?, now())";
                pstmt = con.prepareStatement( sql );
                pstmt.setString( 1, bug.getBugTitle() );
                pstmt.setString( 2, bug.getBugReporter() );
                pstmt.setString( 3, bug.getBugDesc() );
                pstmt.setString( 4, bug.getBugStatus() );
                pstmt.executeUpdate();
                con.close();
            }
            else{
                saveResult =  "Invalid input for description or title.";
            }
        } catch ( SQLException e ) {
            saveResult = "Error occured: "+e.getMessage();
            System.out.println( sql );
            e.printStackTrace();
        }
        return saveResult;

    }
    public static ArrayList<Bugs> fetchAll() {
        Connection con = null;
        ArrayList<Bugs> allBugs = new ArrayList<Bugs>();
        String sql = "";
        try {
            DB db = new DB();
            con = db.openConnection();
            Statement stm = con.createStatement();
            sql = "select * from bugs order by bug_status desc, bug_id desc";
            ResultSet rset = stm.executeQuery( sql );
            while(rset.next()){
                Bugs bug = new Bugs();
                bug.setBugId( rset.getString( "bug_id" ) );
                bug.setBugDesc( rset.getString( "bug_desc" ) );
                bug.setBugTitle( rset.getString( "bug_title" ) );
                bug.setBugReporter( rset.getString( "bug_reporter" ) );
                bug.setBugDate( rset.getString( "bug_date" ) );
                bug.setBugStatus( rset.getString( "bug_status" ) );
                allBugs.add( bug );
            }
            stm.close();
            con.close();
        } catch ( SQLException e ) {
            System.out.println( sql );
            e.printStackTrace();

        }
        return allBugs;

    }
    public static HashMap<String, String> getBugCommentsCounts(){
        HashMap<String, String> countHash = new HashMap<String, String>();
        
        Connection con = null;
        String sql = "";
        try {
            DB db = new DB();
            con = db.openConnection();
            Statement stm = con.createStatement();
            sql = "SELECT count(*) comment_count, bug_id FROM bug_threads bts group by bug_id";
            ResultSet rset = stm.executeQuery( sql );
            while(rset.next()){
               countHash.put( rset.getString( "bug_id" ), rset.getString( "comment_count" ) );
            }
            stm.close();
            con.close();
        } catch ( SQLException e ) {
            System.out.println( sql );
            e.printStackTrace();

        }
        return countHash;
    }
    public static Bugs fetchForId(String bugId) {
        Connection con = null;
        Bugs bug = new Bugs();
        String sql = "";
        try {
            DB db = new DB();
            con = db.openConnection();
            Statement stm = con.createStatement();
            sql = "select * from bugs where bug_id="+bugId;
            ResultSet rset = stm.executeQuery( sql );
            if(rset.next()){
                bug.setBugId( rset.getString( "bug_id" ) );
                bug.setBugDesc( rset.getString( "bug_desc" ) );
                bug.setBugTitle( rset.getString( "bug_title" ) );
                bug.setBugReporter( rset.getString( "bug_reporter" ) );
                bug.setBugDate( rset.getString( "bug_date" ) );
                bug.setBugStatus( rset.getString( "bug_status" ) );
            }
            stm.close();
            con.close();
        } catch ( SQLException e ) {
            System.out.println( sql );
            e.printStackTrace();
            
        }
        return bug;
        
    }

}
