
<jsp:directive.page import="com.soward.object.Product"/>
<%@page import="com.soward.object.User"%>
<head>
    <META HTTP-EQUIV="imagetoolbar" CONTENT="no">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>DMM DashBoard</title>
    <link href="css/stylesheet.css" rel="stylesheet" type="text/css" media="all" />
    <!--link rel="stylesheet" type="text/css" href="css/chromestyle.css" /-->
    <script type="text/javascript" src="js/sorttable.js"></script>
    <script type="text/javascript" src="js/chrome.js"></script>
</head>
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
<script type="text/javascript" src="js/inventoryAjax.js"></script>
<%
    java.util.Date now = new java.util.Date();
    String Uid = (String) session.getAttribute("Uid");
    UserUtil userAd = new UserUtil();
    int count = 1000;

    boolean isAdmin = false;
    try{
        isAdmin = userAd.isAdmin(Uid);
    }catch(Exception e){
        //
    }
    String username = (String) session.getAttribute("username");
    //System.out.println(Uid);
    if (Uid == null) {
        request.getSession().invalidate();
        if (session != null) {
            session = null;
        }
        response.sendRedirect("home.jsp?message=Please Login");
    }
%>

<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.object.Transaction"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.object.Location"%>
<%@ page import="com.soward.util.*" %>
<BR>
<BR>


<%
    TransUtil tu = new TransUtil();
    String message = request.getParameter( "message" );
    String pid = request.getParameter( "pid" );
    Invoice inv = null;
    if(pid!=null){
        InvoiceUtil inu = new InvoiceUtil();
        inv = inu.getForPid(pid);
    }
%>
<%if(inv!=null){
    List<Transaction> transList = inv.getTransList();
    Location loc = RegLocationUtil.fetch(inv.getLocationNum());
%>
<table width="754" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr><td></td></tr><tr><td><br><br></td></tr><tr><td>
    <table align=center border=1>
        <tr><td>

            <table align=center bgcolor="" width="100%" border=0>
                <tr><td>&nbsp;</td></tr>
                <tr align=center> <td colspan=8><font size=4 color=>View invoice for #
                    <font color=red><%=pid %></font></font></td>
                <tr width="75%"><td colspan=8><hr></td>
                </tr>
                </tr>
                <tr><td colspan=8>
                    <table align=center border=0>

                        <tr><td>Clerk:     </td> <td><%=inv.getUsername2()%></td></tr>
                        <tr><td>Register:  </td><td><%=" "+loc.getLocationIP()+"("+inv.getLocationNum()+")"%></td>
                        <tr><td>Invoice:     </td> <td><%=inv.getInvoiceNum()%></td>
                        <tr><td>Date: </td> <td><%=inv.getInvoiceDate()%></td>
                        <tr><td><%=inv.getAccount().getAccountName()%></td><td><a href="./editAccounts.jsp?pid=<%= inv.getAccountNum()%>"><%= inv.getAccountNum()%></a></td></tr>
                        <tr><td><%=inv.getAccount().getAccountStreet()%></td></tr>
                        <tr><td><%=inv.getAccount().getAccountCity()%><%=", "+inv.getAccount().getAccountState()%><%=" "+inv.getAccount().getAccountZip()%></td></tr>
                        <tr><td><%=inv.getAccount().getAccountPhone1()%></td></tr>
                        </tr><td></table>
                <tr align=center><td colspan=8>
                    <table>
                        <tr><td colspan=8>
                            <table align=center border=1>
                                <tr>
                                    <th>Number    </th>
                                    <th>Name      </th>
                                    <th>Qty       </th>
                                    <th>Tax       </th>
                                    <th>Total     </th>
                                </tr>
                                <% for(Transaction trans: transList){ %>
                                <tr>
                                    <td><%=trans.getProductNum()%></td>
                                    <td><%=trans.getProductName()%></td>
                                    <td><%=trans.getProductQty()%></td>
                                    <td><%=trans.getTransTax()%></td>
                                    <td><%=trans.getTransCost()%></td>
                                </tr>
                                <%} %>
                            </table></td></tr>

                        <tr align=right><td colspan=8>
                            <table border=0>
                                <tr align=right><td colspan=8>Sub Total: </td><td><%=tu.getTransTotal(inv.getTransList())%></td>
                                <tr align=right><td colspan=8>Discount: </td><td><%=inv.getInvoiceDiscount()%></td>
                                </tr><tr align=right><td colspan=8>Tax: </td><td><%=inv.getInvoiceTax()%> </td></tr>
                                </tr><tr align=right><td colspan=8>Shipping: </td><td><%=inv.getInvoiceShipTotal()%> </td></tr>
                                </tr><tr align=right><td colspan=8>&nbsp;</td><td><hr> </td></tr>
                                <tr align=right><td colspan=8>Total:    </td><td><%=inv.getInvoiceTotal()%></td></tr>
                                <tr align=right><td colspan=8>Paid:    </td><td><%=inv.getInvoicePaid()%></td></tr>
                            </table> </td></tr>
                    </table> </td></tr>


            </table></td></tr>
    </table>
</td></tr>
    <%} else{%>
    <center>No invoice found for pid: <%=pid %></center>
    <%} %>
    </td>
    <%
        if ( message != null ) {
    %>
    <td><%=message%></td>
    <%
        }
    %>
    </tr>
</table>
<center>
    <form>
        <input type=button value="Close" class="btn" onClick="javascript:window.close();">
        <br>
    </form>
