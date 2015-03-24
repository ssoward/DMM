<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.soward.object.Pages"%>
<%@page import="java.util.Date"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>DMM Dashboard</title>
<link href="css/stylesheet.css" rel="stylesheet" type="text/css" media="all" />
<META HTTP-EQUIV="imagetoolbar" CONTENT="no">
</head>
<%
   //String ipid =session.getId();
   

%>


<body>
     <div id="content">
          <div class="header-bg">
               <span class="welcome">Welcome to Day Murray Music!</span>
               <span class="menu_box">
               <a href="http://www.daymurraymusic.com" target="_blank">DMM Website</a>               </span>          </div>
          <div id="content-main">
          	<div class="margined">
                    <div class="search-bg">
                         <h3>Admin Login</h3>
                              <%@include file="login.jsp"%>
                    </div>
                    <div style="float:left">
                         <%
                         Pages pag = new Pages();
                         Pages currPage = pag.getPageByID( "1" );
                         %>
                    <%=currPage.getPage() %>               </div>
                    <div class="clear">&nbsp;</div>
                </div>
          </div>
     </div>
     <div id="footer">
          <a href="http://www.daymurraymusic.com" target="_blank">DMM Website</a>     </div>
</body>
</html>
