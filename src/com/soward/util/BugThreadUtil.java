package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.soward.db.DB;
import com.soward.object.BugThreads;
import com.soward.object.Bugs;

public class BugThreadUtil {


    public static String saveBugThread( BugThreads bugThread ) {
        Connection con = null;
        String sql = "";
        String saveResult = "Saved comment successfully";
        try {
            DB db = new DB();
            con = db.openConnection();
            PreparedStatement pstmt = null;
            if(bugThread.getBugThread_reporter()!=null&&bugThread.getBugThread_reporter().length()>0&&
                    bugThread.getBugThread_desc()!=null&&bugThread.getBugThread_desc().length()>0){
                sql = "insert into bug_threads (bug_thread_id, bug_id, bug_thread_reporter, bug_thread_desc, bug_thread_date) "+
                "values (null, ?, ?, ?, now())";
                pstmt = con.prepareStatement( sql );
                pstmt.setString( 1, bugThread.getBugId() );
                pstmt.setString( 2, bugThread.getBugThread_reporter() );
                pstmt.setString( 3, bugThread.getBugThread_desc() );
                pstmt.executeUpdate();
                con.close();
            }
            else{
                saveResult =  "Invalid input for comment.";
            }
        } catch ( SQLException e ) {
            saveResult = "Error occured: "+e.getMessage();
            System.out.println( sql );
            e.printStackTrace();
        }
        return saveResult;

    }

    public static ArrayList<BugThreads> fetchAll(String bugId) {
        Connection con = null;
        ArrayList<BugThreads> allBugThreads = new ArrayList<BugThreads>();
        String sql = "";
        try {
            DB db = new DB();
            con = db.openConnection();
            Statement stm = con.createStatement();
            sql = "select * from bug_threads where bug_id = '"+bugId+"' order by bug_thread_id desc";

            ResultSet rset = stm.executeQuery( sql );
            while(rset.next()){
                BugThreads bug = new BugThreads();
                bug.setBugId( rset.getString( "bug_id" ) );
                bug.setBugThread_desc( rset.getString( "bug_thread_desc" ) );
                bug.setBugThread_id( rset.getString( "bug_thread_id" ) );
                bug.setBugThread_reporter( rset.getString( "bug_thread_reporter" ) );
                bug.setBugThread_date( rset.getString( "bug_thread_date" ) );
                allBugThreads.add( bug );
            }
            stm.close();
            con.close();
        } catch ( SQLException e ) {
            System.out.println( sql );
            e.printStackTrace();

        }
        return allBugThreads;

    }
    public static HashMap<String , ArrayList<BugThreads>> getAllBugs(){
        HashMap<String , ArrayList<BugThreads>> hmap = new HashMap<String , ArrayList<BugThreads>>();
        Connection con = null;
        String sql = "";
        try {
            DB db = new DB();
            con = db.openConnection();
            Statement stm = con.createStatement();
            sql = "select * from bug_threads order by bug_id desc , bug_thread_date desc";

            ResultSet rset = stm.executeQuery( sql );
            int currBugId = -0;
            ArrayList<BugThreads> allBugThreads = new ArrayList<BugThreads>();
            boolean getLastValue = false;
            while(rset.next()){
                if(!getLastValue){
                    getLastValue = true;
                }
                int bugId = Integer.parseInt( rset.getString( "bug_id" ) );
                if(currBugId!=bugId){
                    //not first case
                    if(!(currBugId<0)){
                        hmap.put( currBugId+"", allBugThreads );
                        currBugId = bugId;
                        allBugThreads = new ArrayList<BugThreads>();
                    }
                    currBugId = bugId;

                }

                BugThreads bug = new BugThreads();
                bug.setBugId( bugId+"" );
                bug.setBugThread_desc( rset.getString( "bug_thread_desc" ) );
                bug.setBugThread_id( rset.getString( "bug_thread_id" ) );
                bug.setBugThread_reporter( rset.getString( "bug_thread_reporter" ) );
                bug.setBugThread_date( rset.getString( "bug_thread_date" ) );
                allBugThreads.add( bug );
            }
            if(getLastValue){
                hmap.put( currBugId+"", allBugThreads );
            }
            stm.close();
            con.close();
        } catch ( SQLException e ) {
            System.out.println( sql );
            e.printStackTrace();

        }
        return hmap;
    }
    public static void main (String args[]){
        HashMap hm = (BugThreadUtil.getAllBugs());
        Set ks = hm.keySet();
        Iterator itr = ks.iterator();
        while(itr.hasNext()){
            String key = (String)itr.next();
            ArrayList<BugThreads> al = (ArrayList)hm.get( key );
            for(BugThreads tt: al){
                System.out.println(key +" == "+tt.getBugId()+" "+tt.getBugThread_desc());
                
            }
        }
    }

}
