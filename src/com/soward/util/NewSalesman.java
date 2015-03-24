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

public class NewSalesman extends HttpServlet {
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

            String firstName = request.getParameter( "firstName" );
            String lastName = request.getParameter( "lastName" );
            String Addr1 = request.getParameter( "Addr1" );
            String Addr2 = request.getParameter( "Addr2" );
            String hireDte = request.getParameter( "hireDte" );
            String phone = request.getParameter( "phone" );
            String email = request.getParameter( "email" );
            String reportsTo = request.getParameter( "reportsTo" );
            String lvlCommis = request.getParameter( "lvlCommis" );

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

            String sql = "insert into salesman "
                    + "(pid, first_name, last_name, addr1, addr2, date_hired, phone, email, reports_to, comm_lvl)"
                    + "values(" + null + "," + "'" + ( firstName.replaceAll( "'", "&#39" ) ) + "'" + "," + "'"
                    + ( lastName.replaceAll( "'", "&#39" ) ) + "'" + "," + "'" + ( Addr1.replaceAll( "'", "&#39" ) )
                    + "'" + "," + "'" + ( Addr2.replaceAll( "'", "&#39" ) ) + "'" + "," + "'"
                    + ( hireDte.replaceAll( "'", "&#39" ) ) + "'" + "," + "'" + ( phone.replaceAll( "'", "&#39" ) )
                    + "'" + "," + "'" + ( email.replaceAll( "'", "&#39" ) ) + "'" + "," + "'"
                    + ( reportsTo.replaceAll( "'", "&#39" ) ) + "'" + "," + "'"
                    + ( lvlCommis.replaceAll( "'", "&#39" ) ) + "'" + ")";
            // System.out.println(sql);
            Statement stm = conn.createStatement();
            stm.executeUpdate( sql );
            // PrintWriter out = response.getWriter();

            session.setAttribute( "message", "<font color=red size=2>User added.</font>" );
            response.sendRedirect( "newSalesman.jsp?message=true" );
        } catch ( Exception e ) {
            System.out.println( "e: " + e );
        }
    }
}
