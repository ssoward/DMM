
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
<%@ include file="topLayout.jsp"%>
<%
%>


<%@page import="com.soward.object.Pages"%>
<BR>
<BR>

<head>
<link href="css/bv.css" rel="stylesheet" type="text/css">
</head>
<%
String message = request.getParameter("message");
//message = (String)session.getAttribute("message");

%>
 <table width="754" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr> <td align="" valign="top">
<table>
  <tr>
  <td>
  <%
    Pages pag = new Pages();
    Pages currPage = pag.getPageByID( "2" );
    %>
    <%=currPage.getPage() %>
                        
                        
  </td>
  </tr>
</table>

</td>
<% if(message!=null){%>
<td><%=message %></td>
  <%}%>
</tr>
</table>
<%@include file="bottomLayout.jsp"%>
