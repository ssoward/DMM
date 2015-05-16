<%@ page import="java.sql.*,java.util.*,java.util.Date.*"%>
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
<%@ page import="java.text.NumberFormat"%>
<%@ page import="com.soward.util.UserUtil" %>

<%
    java.util.Date now = new java.util.Date();
    NumberFormat bdec = NumberFormat.getCurrencyInstance(Locale.US);
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
<head>
    <META HTTP-EQUIV="imagetoolbar" CONTENT="no">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>DMM DashBoard</title>
    <link href="css/stylesheet.css" rel="stylesheet" type="text/css" media="all" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
    <!--link rel="stylesheet" type="text/css" href="css/chromestyle.css" /-->
    <script type="text/javascript" src="js/sorttable.js"></script>
    <script type="text/javascript" src="js/chrome.js"></script>
    <script type="text/javascript" src="js/dmm.js"></script>
    <!-- APP SCRIPT -->
    <script src="lib/angular/angular.js"></script>
    <script src="lib/js/component/lodash.js"></script>
    <script src="lib/angular/angular-animate.js"></script>
    <script src="lib/angular/angular-ui-router.js"></script>
    <script src="lib/angular/angular-route.js"></script>
    <script src="lib/angular/ui-bootstrap-tpls.js"></script>
    <script src="lib/angular/angular-slider.min.js"></script>

    <script src="lib/js/DMMCore.js"></script>

    <script src="lib/js/ProductsApp.js"></script>
    <script src="lib/js/productsRoute.js"></script>

    <script src="lib/js/DailySalesApp.js"></script>
    <script src="lib/js/dailySalesRouter.js"></script>

    <script src="lib/js/AccountsApp.js"></script>
    <script src="lib/js/accountsRouter.js"></script>

    <script src="lib/js/PurchaseOrderApp.js"></script>
    <script src="lib/js/purchaseOrderRouter.js"></script>

    <script src="lib/js/SMAuthApp.js"></script>
    <script src="lib/js/SMAuthRouter.js"></script>

    <script src="lib/js/controllers/PurchaseOrderController.js"></script>
    <script src="lib/js/controllers/DailySalesController.js"></script>
    <script src="lib/js/controllers/SMAController.js"></script>
    <script src="lib/js/controllers/ProductsController.js"></script>
    <script src="lib/js/controllers/AccountsController.js"></script>

    <script src="lib/js/services/SMAuthService.js"></script>
    <script src="lib/js/services/DialogService.js"></script>
    <script src="lib/js/services/POService.js"></script>
    <script src="lib/js/services/InvoiceTransService.js"></script>
    <script src="lib/js/services/ProductService.js"></script>
    <script src="lib/js/services/AccountService.js"></script>

    <script src="lib/js/directives/spinner.js"></script>
    <script src="lib/js/directives/ProductSold.js"></script>
    <script src="lib/js/directives/DMMCheckBox.js"></script>
    <script src="lib/js/directives/DMMRadio.js"></script>

    <link type="text/css" href="css/jquery-ui-1.8.4.custom.css" rel="stylesheet" />
    <script type="text/javascript" src="js/notification.js"></script>
</head>
<body>
<div id="content">
    <div class="header-bg">
        <span class="welcome"></span>
        <span class="menu_box">
            <a href="http://www.daymurraymusic.com">DMM Website</a>
            <a href="index.jsp">Home</a> |
            <a href="logout.jsp">Logout</a>
        </span>
    </div>
    <div id="ajaxLoader" class="hideAjaxLoader"></div>
    <div id="dialogBackground" class="dialogBackgroundHide"> </div>
    <%@ include file="menu.jsp"%>







