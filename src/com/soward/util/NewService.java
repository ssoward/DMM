package com.soward.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.print.DocFlavor.CHAR_ARRAY;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.soward.db.DB;

public class NewService extends HttpServlet {
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            String Uid = (String) session.getAttribute( "Uid" );
            String username = (String) session.getAttribute( "username" );
            // System.out.println(Uid);
            if ( Uid == null ) {
                request.getSession().invalidate();
                if ( session != null ) {
                    session = null;
                }
                response.sendRedirect( "home.jsp?message=Please Login" );
            }
        } catch ( Exception e ) {
            System.out.println( "e: " + e );
        }
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        try {

            String message = "";
String bvPhone    = request.getParameter("bvPhone"   ); 
String bvUserName = request.getParameter("bvUserName"); 
String bvRegName  = request.getParameter("bvRegName" ); 
String bvActDte   = request.getParameter("bvActDte"  ); 
String bvFee      = request.getParameter("bvFee"     ); 



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

            String sql = "insert into service "
                    + "(pid, bv_phone, bv_user_name, bv_reg_name, bv_act_date, bv_base_amt)"

                    + "values(" + null + "," + "'" 
                    + ( bvPhone.replaceAll( "'", "&#39" ) ) + "'" + "," + "'"
                    + ( bvUserName.replaceAll( "'", "&#39" ) ) + "'" + "," + "'" 
                    + ( bvRegName.replaceAll( "'", "&#39" ) ) + "'" + "," + "'" 
                    + ( bvActDte.replaceAll( "'", "&#39" ) ) + "'" + "," + "'"
                    + ( bvFee.replaceAll( "'", "&#39" ) ) + "'" + ")";   
            // System.out.println(sql);
            Statement stm = conn.createStatement();
            stm.executeUpdate( sql );
            // PrintWriter out = response.getWriter();

            session.setAttribute( "message", "<font color=red size=2>User added.</font>" );
            response.sendRedirect( "newService.jsp?message=true" );
        } catch ( Exception e ) {
            System.out.println( "e: " + e );
        }
    }
}
