<%@page import="com.soward.util.UserUtil"%>
<%@page import="com.soward.object.User"%>

<%@ page import="java.sql.*,java.util.*,java.util.Date.*"%>
<%@ page import="com.soward.object.Client"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.PreparedStatement"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="javax.naming.Context"%>
<%@ page import="javax.naming.InitialContext"%>
<%@ page import="javax.naming.NamingException"%>
<%@ page import="javax.servlet.ServletException"%>
<%@ page import="javax.servlet.http.HttpServlet"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<%@ page import="javax.servlet.http.HttpServletResponse"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%@ page import="javax.sql.DataSource"%>
<%@ page import="com.soward.*"%>
<%
	java.util.Date now = new java.util.Date();
	String Uid = (String) session.getAttribute("Uid");
    UserUtil userAd = new UserUtil();
    String userrole = (String) session.getAttribute("userrole");
    
    boolean isAdmin = false;
    try{
        isAdmin = userAd.isAdmin(Uid);
    }catch(Exception e){
        //
    }
	String username = (String) session.getAttribute("username");
	String ipaddress = (String) session.getAttribute("ipaddress");
	//System.out.println(Uid);
	if (Uid == null||userrole == null || username == null) {
		request.getSession().invalidate();
		if (session != null) {
			session = null;
		}
		response.sendRedirect("home.jsp?message=Please Login");
	}
	if(userrole !=null && userrole.equals( "adam" )){
    	response.sendRedirect("adamIndex.jsp");
    }
%>

