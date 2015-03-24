<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="com.soward.db.DB"%>
<HTML>
<HEAD>
<TITLE>MurrayDayMusic web traffic.</TITLE>
</HEAD>
<H1 ALIGN="CENTER">MurrayDayMusic web traffic.</H1>


<%@ page import="java.sql.*,java.util.Date" %>
<%@ page errorPage="WEB-INF/error.jsp" %>

<%
   String ipid =session.getId();
   String ipinfo =application.getServerInfo();
   String ipadd =request.getRemoteAddr();
   Connection connection2 = DB.openConnection();
   Statement statement2 = connection2.createStatement();
   String query2 = "INSERT INTO dmm  (Time, Info, Session, IP) VALUES (now(), "+
                   "\'"+ipinfo+"\',"+ 
                   "\'"+ipid+"\',"+ 
                   "\'"+ipadd+"\'"+")"; 
   statement2.executeUpdate(query2);
   connection2.close();
//   System.out.print("DMM HIT: " + ipadd);
Date d1 = new Date();
//System.out.println(" Time: " + d1);

%>
<center> 
<p>
<a href="DMM_Traffic.jsp">View web traffic</a>
<p>
<p>
</center> 
</BODY></HTML>
