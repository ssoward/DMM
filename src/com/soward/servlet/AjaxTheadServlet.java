package com.soward.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.soward.object.BugThreads;
import com.soward.object.Bugs;
import com.soward.util.BugThreadUtil;
import com.soward.util.BugsUtil;

public class AjaxTheadServlet extends HttpServlet{
    public int count = 0;
//  ArrayList hp = new ArrayList<String>();
    public void service( final HttpServletRequest request, HttpServletResponse response ) throws ServletException,
    IOException {
        try {                        
            // Get spring context beans.
            count++;
            String bugComment = (request.getParameter( "bugComment" ));
            String bugId = (request.getParameter( "bugId" ));
            String bugCommentReporter = (request.getParameter( "bugCommentReporter" ));
//          hp.add(  temp );

            if(bugId!=null&&bugComment!=null&&bugCommentReporter!=null){
                BugThreads newBugThread = new BugThreads();
                newBugThread.setBugId(bugId);
                newBugThread.setBugThread_desc(bugComment);
                newBugThread.setBugThread_reporter(bugCommentReporter);
                BugThreadUtil.saveBugThread(newBugThread);
            }

            StringBuffer cList = new StringBuffer("<center>");
            ArrayList<BugThreads> threadList = new ArrayList<BugThreads>();
            Bugs bug = new Bugs();
            bug = BugsUtil.fetchForId( bugId );
            threadList = BugThreadUtil.fetchAll(bug.getBugId());
            if(threadList.size()>0){ 
                for(BugThreads bugt: threadList){ 
                    cList.append( "--------------------------------------");
                    cList.append( "  <br><b>Name:</b>  "+bugt.getBugThread_reporter());
                    cList.append( "  <br><b>Date:</b>   "+bugt.getBugThread_date());
                    cList.append( "<br><b>Comment:</b>  "+bugt.getBugThread_desc() );
                    cList.append( "<br>");
                }
            }

            response.getWriter().print( cList.toString() );
            //response.getWriter().print( new JSONArray(hp) );



        } catch ( Exception e ) {
            response.sendRedirect("SystemError.html");
        }
    }
}
