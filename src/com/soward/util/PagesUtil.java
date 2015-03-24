package com.soward.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.soward.db.DB;

public class PagesUtil extends HttpServlet {
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        try {
            String previewPage = request.getParameter( "previewPage" );
            HttpSession session = request.getSession();
            String Uid = (String) session.getAttribute( "Uid" );
            String username = (String) session.getAttribute( "username" );
            String input = request.getParameter( "input" );
            response.setHeader( "Cache-Control", "no-cache" ); // HTTP 1.1
            response.setHeader( "Pragma", "no-cache" ); // HTTP 1.0
            response.setDateHeader( "Expires", 0 ); // prevents caching at the
            // proxy server
            response.setContentType( "text/html" );

            PrintWriter out = response.getWriter();

            if ( Uid == null ) {
                request.getSession().invalidate();
                if ( session != null ) {
                    session = null;
                }
                response.sendRedirect( "home.jsp?message=Please Login" );
            }

            String output = "";
            output += previewPage;
            out.println( output );
            out.close();
            // System.out.println(Uid);
        } catch ( Exception e ) {
            System.out.println( "e: " + e );
        }
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        try {

            String message = "";

            String newPageText = request.getParameter( "newPageText" );
            String currPageId = request.getParameter( "currPageId" );
            String previewPage = request.getParameter( "previewPage" );

            HttpSession session = request.getSession();
            String Uid = (String) session.getAttribute( "Uid" );
            String username = (String) session.getAttribute( "username" );
            // System.out.println(Uid);
            if ( Uid == null ) {
                request.getSession().invalidate();
                if ( session != null ) {
                    session = null;
                }
                response.sendRedirect( "home.jsp?mustLogin=true" );
            }
            String input = request.getParameter( "input" );
            String possibleSolutions = request.getParameter( "getPoss" );
            response.setHeader( "Cache-Control", "no-cache" ); // HTTP 1.1
            response.setHeader( "Pragma", "no-cache" ); // HTTP 1.0
            response.setDateHeader( "Expires", 0 ); // prevents caching at the
            // proxy server
            response.setContentType( "text/html" );

            DB db = new DB();
            Connection conn = db.openConnection();
//            System.out.println( "currPageId " + currPageId );
            System.out.println( "previewPage " + previewPage );
            String sql = "";
            if ( previewPage != null ) {
              System.out.println(newPageText.replaceAll( "'", "&#39" ));  
                sql = "update pages set page_preview='" + newPageText.replaceAll( "'", "&#39" ) + "'" + " where pid='"
                        + currPageId + "'";

            } else {

                sql = "update pages set page='" + newPageText.replaceAll( "'", "&#39" ) + "'"
                        + ", page_date=now() where pid='" + currPageId + "'";
            }
            // String sql = "insert into pages "
            // + "(pid, page ,page_preview ,page_date)"
            // + "values(" + null + ",'"
            // + newPageText.replaceAll( "'", "&#39" ) + "'" + "," + "'"
            // + "previousPage" + "'" + "," + ""
            // + "now()" + ")";
            // System.out.println(sql);
            Statement stm = conn.createStatement();
//            System.out.println( sql );
            stm.executeUpdate( sql );
            // PrintWriter out = response.getWriter();

            session.setAttribute( "message", "<font color=red size=2>User added.</font>" );
            if ( previewPage != null ) {
                response.sendRedirect( "editPages.jsp?previous=true&message=true&page=" + currPageId );
            } else {
                response.sendRedirect( "editPages.jsp?message=true&page=" + currPageId );
            }
        } catch ( Exception e ) {
            System.out.println( "PagesUtil: " + e );
        }
    }
}
