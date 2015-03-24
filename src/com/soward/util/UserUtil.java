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
import com.soward.object.User;

public class UserUtil extends HttpServlet {
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

            String loginName = request.getParameter( "loginName" );
            String userFirst = request.getParameter( "userFirst" );
            String userLast = request.getParameter( "userLast" );
            String userRole = request.getParameter( "userRole" );
            String userEmail = request.getParameter( "userEmail" );
            String userPhone = request.getParameter( "userPhone" );
            String userPass1 = request.getParameter( "userPass1" );
            String userPass2 = request.getParameter( "userPass2" );
            String editUserPid = request.getParameter( "editUserPid" );

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
            Statement stm = conn.createStatement();
            String sql = "";
            if ( editUserPid != null ) {
                sql = "update users set" + "  user_name   = '" + loginName.replaceAll( "'", "&#39" ) + "'"
                        + ", user_pass   = md5('" + userPass1.replaceAll( "'", "&#39" ) + "')" + ", user_first  = '"
                        + userFirst.replaceAll( "'", "&#39" ) + "'" + ", user_last   = '"
                        + userLast.replaceAll( "'", "&#39" ) + "'" + ", user_email  = '"
                        + userEmail.replaceAll( "'", "&#39" ) + "'" + ", user_role   = '"
                        + userRole.replaceAll( "'", "&#39" ) + "'" + ", user_phone  = '"
                        + userPhone.replaceAll( "'", "&#39" ) + "'" + " where user_pid='" + editUserPid + "';";
                //              System.out.println( sql );
                stm.executeUpdate( sql );
                // PrintWriter out = response.getWriter();

                session.setAttribute( "message", "<font color=red size=2>User edited successfully.</font>" );
                response.sendRedirect( "editUser.jsp?message=User edited successfully.&pid="+editUserPid );
            } else {
                sql = "insert into users "
                        + "(user_pid, user_name ,user_pass ,user_first ,user_last ,user_email ,user_role ,user_phone)"
                        + "values(" + null + "," + "'" + loginName.replaceAll( "'", "&#39" ) + "'" + "," + "md5('"
                        + userPass1.replaceAll( "'", "&#39" ) + "')" + "," + "'" + userFirst.replaceAll( "'", "&#39" )
                        + "'" + "," + "'" + userLast.replaceAll( "'", "&#39" ) + "'" + "," + "'"
                        + userEmail.replaceAll( "'", "&#39" ) + "'" + "," + "'" + userRole.replaceAll( "'", "&#39" )
                        + "'" + "," + "'" + userPhone.replaceAll( "'", "&#39" ) + "'" + ")";
                // System.out.println(sql);
                ResultSet rset = stm.executeQuery( "select * from users where user_name='"
                        + loginName.replaceAll( "'", "&#39" ) + "'" );
                if ( rset.next() && editUserPid == null ) {
                    session.setAttribute( "message", "<font color=red size=2>User name already in use.</font>" );
                    response.sendRedirect( "newUser.jsp?message=User name already in use." );

                } else {
                    stm.executeUpdate( sql );
                    // PrintWriter out = response.getWriter();

                    session.setAttribute( "message", "<font color=red size=2>User successfully added.</font>" );
                    response.sendRedirect( "newUser.jsp?message=User successfully added." );
                }
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
            System.out.println( "Parker.UserUtil-->isAdmin: " + e );
        }
        return isAdmin;
    }
    public static ArrayList<User> getAllUsers( ) {
    	DB db = new DB();
    	ArrayList<User> al = new ArrayList<User>();
    	try {
    		Connection conn = db.openConnection();
    		Statement stm = conn.createStatement();
    		String sql = "SELECT * FROM users ";
    		ResultSet rset = stm.executeQuery( sql );
    		
    		while ( rset.next() ) {
    			User user = new User();
    			user.setRole(rset.getString( "user_role" ));
    			user.setEmail(rset.getString( "user_email" ));
    			user.setFirst(rset.getString( "user_first" ));
    			user.setLast(rset.getString( "user_last" ));
    			user.setName(rset.getString( "user_name" ));
    				al.add(user);
    		}
    	} catch ( Exception e ) {
    		e.printStackTrace();
    	}
    	return al;
    }

}
