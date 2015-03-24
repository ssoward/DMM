
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
<head>
    <META HTTP-EQUIV="imagetoolbar" CONTENT="no">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>DMM DashBoard</title>
    <link href="css/stylesheet.css" rel="stylesheet" type="text/css" media="all" />
    <!--link rel="stylesheet" type="text/css" href="css/chromestyle.css" /-->
    <script type="text/javascript" src="js/sorttable.js"></script>
    <script type="text/javascript" src="js/chrome.js"></script>
    <script type="text/javascript" src="js/jquery-1.4.2.js"></script>
    <script type="text/javascript" src="js/dmm.js"></script>
    <!-- APP SCRIPT -->

    <link type="text/css" href="css/jquery-ui-1.8.4.custom.css" rel="stylesheet" />
    <script type="text/javascript" src="js/jquery-ui-1.8.4.custom.min.js"></script>


</head>
    <%
    String loggedInUser = "Welcome!";

  String tempUser = "";
try{
  tempUser = (String) session.getAttribute("username");
  if(tempUser!=null){
	  loggedInUser = "Welcome: "+ tempUser;
  }
}catch(Exception e){
  loggedInUser = "Welcome!";
    //null session
}
%>
<script type="text/javascript" src="js/notification.js"></script>
<body>
<div id="content">
    <div class="header-bg">
        <span class="welcome"><%=loggedInUser%></span>
               <span class="menu_box">
                    <a href="http://www.daymurraymusic.com">DMM Website</a>
                     | <a href="index.jsp">Home</a> | 
                     <a href="logout.jsp">Logout</a>          
               </span>
    </div>
    <div id="ajaxLoader" class="hideAjaxLoader"></div>
    <div id="dialogBackground" class="dialogBackgroundHide"> </div>

         
