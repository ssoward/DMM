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
import com.soward.object.DBObj;

public class UpdateAccount extends HttpServlet {
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
            DBObj dbobj = new DBObj();
            String message = "";

            String accountNum = request.getParameter( "accountNum" );
            String accountPassword = request.getParameter( "accountPassword" );
            String accountEmail = request.getParameter( "accountEmail" );
            String accountPhone2 = request.getParameter( "accountPhone2" );
            String accountStreet = request.getParameter( "accountStreet" );
            String accountState = request.getParameter( "accountState" );
            String accountCountry = request.getParameter( "accountCountry" );
            String accountType2 = request.getParameter( "accountType2" );
            String accountCloseDate = request.getParameter( "accountCloseDate" );
            String accountName = request.getParameter( "accountName" );
            String accountContact = request.getParameter( "accountContact" );
            String accountPhone1 = request.getParameter( "accountPhone1" );
            String accountFax = request.getParameter( "accountFax" );
            String accountCity = request.getParameter( "accountCity" );
            String accountPostalCode = request.getParameter( "accountPostalCode" );
            String accountType1 = request.getParameter( "accountType1" );
            String accountOpenDate = request.getParameter( "accountOpenDate" );
            String accountBalance = request.getParameter( "accountBalance" );

            if ( accountNum == null ) {
                accountNum = "";
            }
            if ( accountPassword == null ) {
                accountPassword = "";
            }
            if ( accountEmail == null ) {
                accountEmail = "";
            }
            if ( accountPhone2 == null ) {
                accountPhone2 = "";
            }
            if ( accountStreet == null ) {
                accountStreet = "";
            }
            if ( accountState == null ) {
                accountState = "";
            }
            if ( accountCountry == null ) {
                accountCountry = "";
            }
            if ( accountType2 == null ) {
                accountType2 = "";
            }
            if ( accountCloseDate == null ) {
                accountCloseDate = "";
            }
            if ( accountName == null ) {
                accountName = "";
            }
            if ( accountContact == null ) {
                accountContact = "";
            }
            if ( accountPhone1 == null ) {
                accountPhone1 = "";
            }
            if ( accountFax == null ) {
                accountFax = "";
            }
            if ( accountCity == null ) {
                accountCity = "";
            }
            if ( accountPostalCode == null ) {
                accountPostalCode = "";
            }
            if ( accountType1 == null ) {
                accountType1 = "";
            }
            if ( accountOpenDate == null ) {
                accountOpenDate = "";
            }
            if ( accountBalance == null ) {
                accountBalance = "";
            }

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

            String sql = "";
            if ( accountNum != null ) {
                sql = "update Accounts set" + "  " +

                "accountEmail     = '" + accountEmail.replaceAll( "'", "&#39" ) + "', " + "accountPhone2    = '"
                        + accountPhone2.replaceAll( "'", "&#39" ) + "', " + "accountStreet    = '"
                        + accountStreet.replaceAll( "'", "&#39" ) + "', " + "accountState     = '"
                        + accountState.replaceAll( "'", "&#39" ) + "', " + "accountCountry   = '"
                        + accountCountry.replaceAll( "'", "&#39" ) + "', " + "accountType2     = '"
                        + accountType2.replaceAll( "'", "&#39" ) + "', " + "accountCloseDate = '"
                        + accountCloseDate.replaceAll( "'", "&#39" ) + "', " + "accountName      = '"
                        + accountName.replaceAll( "'", "&#39" ) + "', " + "accountContact   = '"
                        + accountContact.replaceAll( "'", "&#39" ) + "', " + "accountPhone1    = '"
                        + accountPhone1.replaceAll( "'", "&#39" ) + "', " + "accountFax       = '"
                        + accountFax.replaceAll( "'", "&#39" ) + "', " + "accountCity      = '"
                        + accountCity.replaceAll( "'", "&#39" ) + "', " + "accountPostalCode= '"
                        + accountPostalCode.replaceAll( "'", "&#39" ) + "', " + "accountType1     = '"
                        + accountType1.replaceAll( "'", "&#39" ) + "', " + "accountOpenDate  = '"
                        + accountOpenDate.replaceAll( "'", "&#39" ) + "'" +

                        " where accountNum=" + accountNum + "";
                // System.out.println( sql );
                ArrayList<DBObj> returnObject = dbobj.runQuery( sql, "update" );
                if ( returnObject != null ) {
                    session.setAttribute( "message", "<font color=red size=2>Account updated successfully.</font>" );
                    response.sendRedirect( "editAccounts.jsp?message=Account updated successfully.&pid=" + accountNum );

                }
                // PrintWriter out = response.getWriter();

                session.setAttribute( "message", "<font color=red size=2>Account update FAILED.</font>" );
                response.sendRedirect( "editAccounts.jsp?message=Account update FAILED.&pid=" + accountNum );
            }
            // check to see if user already exists
        } catch ( Exception e ) {
            System.out.println( "e: " + e );
        }
    }

    public boolean isAdmin( String pid ) {
        DB db = new DB();
        boolean isAdmin = true;
        try {
            Connection conn = db.openConnection();
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM users where user_pid='" + pid + "'";
            ResultSet rset = stm.executeQuery( sql );
            if ( rset.next() ) {
                String temp = rset.getString( "user_role" );
                if ( temp.equalsIgnoreCase( "admin" ) ) {
                    isAdmin = true;
                } else {
                    isAdmin = false;
                }
            }
        } catch ( Exception e ) {
            isAdmin = false;
            System.out.println( "DMM.UserUtil-->isAdmin: " + e );
        }
        return isAdmin;
    }

}
