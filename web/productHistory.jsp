
<jsp:directive.page import="com.soward.object.Product"/>
<jsp:directive.page import="com.soward.util.ProductUtils"/>
<%@page import="com.soward.util.UserUtil"%>
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
<jsp:directive.page import="com.soward.object.Supplier"/>
<jsp:directive.page import="com.soward.util.SupplierUtil"/>
<jsp:directive.page import="com.soward.object.Departments"/>
<jsp:directive.page import="com.soward.object.Descriptions"/>
<jsp:directive.page import="com.soward.object.Events"/>
<jsp:directive.page import="com.soward.util.EventsUtil"/>
<jsp:directive.page import="com.soward.util.DescriptionsUtil"/>
<jsp:directive.page import="com.soward.util.DepartmentsUtil"/>
<jsp:directive.page import="com.soward.object.Transaction"/>
<jsp:directive.page import="com.soward.enums.LocationsDBName"/>
<jsp:directive.page import="com.soward.util.TransUtil"/>
<jsp:directive.page import="com.soward.enums.TransactionType"/>
<jsp:directive.page import="com.soward.util.WebProductInfoUtil"/>
<jsp:directive.page import="com.soward.object.WebProductInfo"/>
<jsp:directive.page import="com.soward.util.BarcodeGenerator"/>
<script type="text/javascript" src="js/inventoryAjax.js"></script>
<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.object.Invoice"%>
<%
    java.util.Date now = new java.util.Date();
    String Uid = (String) session.getAttribute("Uid");
    UserUtil userAd = new UserUtil();
    
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
    <script type="text/javascript" src="js/inventoryAjax.js"></script>
    <%
    
    String message = request.getParameter( "message" );
    String printBarCode = request.getParameter( "printBarCode" );
    String getYearlySales = request.getParameter( "getYearlySales" );
    String saveUpdatedProduct = request.getParameter( "saveUpdatedProduct" );
    String prodNum = request.getParameter( "prodNum" );
    String printer = request.getParameter( "printer" );
    String printCount = request.getParameter( "printCount" );
    String updateBlurb = request.getParameter( "updateBlurb" );
    WebProductInfo wpi = WebProductInfoUtil.fetchForProdNum( prodNum );
    
    if(updateBlurb!=null){
	    String productBlurb = request.getParameter( "productBlurb" );
	    if(wpi==null)wpi = new WebProductInfo();
	    wpi.setProductBlurb(productBlurb);
	    wpi.setProductNum(prodNum);
	    wpi.setProductFeature("");
	    message = WebProductInfoUtil.saveOrUpdate(wpi);
    }
    String location = request.getParameter( "location" );
    ArrayList<Transaction> transListMurray = TransUtil.getTransListForProdNum( prodNum, "MURRAY");
    ArrayList<Transaction> transListLehi = TransUtil.getTransListForProdNum( prodNum, "LEHI");
    ArrayList<Transaction> transListOrem = TransUtil.getTransListForProdNum( prodNum, "OREM");
    HashMap<String, Integer> mySales = new HashMap<String, Integer>();
    HashMap<String, Integer> oySales = new HashMap<String, Integer>();
    HashMap<String, Integer> lySales = new HashMap<String, Integer>();
    
    //if(getYearlySales!=null){
	    mySales = TransUtil.getSalesFormat(transListMurray);//ProductUtils.getProductYearlySold(prodNum, "MURRAY");
	    oySales = TransUtil.getSalesFormat(transListOrem);//ProductUtils.getProductYearlySold(prodNum, "OREM");
	    lySales = TransUtil.getSalesFormat(transListLehi);//ProductUtils.getProductYearlySold(prodNum, "LEHI");
    //}
    
    Product prod = null;
    if(prodNum!=null&&prodNum.length()>2&&location!=null){ 
      prod = ProductUtils.fetchProductForNum( prodNum, location ); 
     }
     if(printBarCode!=null&&prod!=null&&printer!=null){
     	BarcodeGenerator.printBarCode(prod, printCount, printer); 
     }
     String locationDisplay = location;
  %><center>
    <br/>
    <%if(message!=null){%>
    <center><font color=red size=3><%=message%></font></center>
    <%}%>
    <table width="100%">
      <tr><td>
          <table class="simple" cellspacing="0" cellpadding="0">
            <tr><td colspan=8><center><b>Product history for <%=prodNum %></b></center>
            </td></tr>

          </table>
      </td></tr>
      <tr><td colspan="5" align="right">
          <input type=button value="Close" onClick="javascript:window.close();" class="btn">
      </td></tr>
    </table>
  </center>
  <table border=1 width=100% cellpadding=3 cellspacing=1>
    <tr><td>
        <%
        if(transListMurray!=null){
        %>
        <table cellpadding=3 cellspacing=3>
          <tr><td align=left colspan="4">
              Purchase History
          </td></tr>
          <tr>
            <td align=left>
              <table class="simple" border=1 cellpadding="3" cellspacing="0">
                <tr>
                  <th>Murray</th>
                  <th>DV</th>
                  <th>Lehi</th>
                  </tr><tr>
                  <td align=left>
                    <select name="" size=10>
                      <%
                      String currentTransType = "";
                      int count = 1;
                      for(Transaction trans: transListMurray){%>
                      <%if(currentTransType.length()<1){%>
                      <option value="0"><%="--- "+TransactionType.valueOf(trans.getTransType()).value()+" -----" %></option>
                      <%currentTransType = trans.getTransType();
                      }else if(!currentTransType.equalsIgnoreCase(trans.getTransType())){%>
                      <option value="0"><%="--- "+TransactionType.valueOf(trans.getTransType()).value()+" -----" %></option>
                      <%currentTransType = trans.getTransType();
                      } %>
                      <option value="0"><%=count+". "+trans.getTransDateFormatted()+" "+trans.getTransType()+" - "+trans.getProductQty()+" - "+trans.getInvoiceNum() %></option>
                      <%count++;}%>
                    </select> 
                  </td>
                  <td align=left>
                    <select name="" size=10>
                      <%
                      currentTransType = "";
                      count = 1;
                      for(Transaction trans: transListOrem){%>
                      <%if(currentTransType.length()<1){%>
                      <option value="0"><%="--- "+TransactionType.valueOf(trans.getTransType()).value()+" -----" %></option>
                      <%currentTransType = trans.getTransType();
                      }else if(!currentTransType.equalsIgnoreCase(trans.getTransType())){%>
                      <option value="0"><%="--- "+TransactionType.valueOf(trans.getTransType()).value()+" -----" %></option>
                      <%currentTransType = trans.getTransType();
                      } %>
                      <option value="0"><%=count+". "+trans.getTransDateFormatted()+" "+trans.getTransType()+" - "+trans.getProductQty()+" - "+trans.getInvoiceNum() %></option>
                      <%count++;}%>
                    </select> 
                  </td>
                  <td align=left>
                    <select name="" size=10>
                      <%
                      count = 1;
                      currentTransType = "";
                      for(Transaction trans: transListLehi){%>
                      <%if(currentTransType.length()<1){%>
                      <option value="0"><%="--- "+TransactionType.valueOf(trans.getTransType()).value()+" -----" %></option>
                      <%currentTransType = trans.getTransType();
                      }else if(!currentTransType.equalsIgnoreCase(trans.getTransType())){%>
                      <option value="0"><%="--- "+TransactionType.valueOf(trans.getTransType()).value()+" -----" %></option>
                      <%currentTransType = trans.getTransType();
                      } %>
                      <option value="0"><%=count+". "+trans.getTransDateFormatted()+" "+trans.getTransType()+" - "+trans.getProductQty()+" - "+trans.getInvoiceNum() %></option>
                      <%count++;}%>
                    </select> 
                  </td>

                </tr> 
              </table>
            </td>
          </tr>
        </table>
      </td>

    <%Calendar calNow = Calendar.getInstance();
    int currYear = calNow.get(Calendar.YEAR);
    %>
    </tr><tr>
   <td>
        <table class="simple" border=1 cellpadding="3" cellspacing="0">
          <tr>
            <th>Murray</th>
            <th>DV</th>
            <th>Lehi</th>
          </tr>
          <tr>
            <td align=left>
              <table cellpadding=3 cellspacing=3 border=1><th>Year</th><th>Sold</th>
                <tr><td><%=currYear %></td><td><%=mySales.get(currYear+"") %></td></tr>
                <tr><td><%=currYear-1 %></td><td><%=mySales.get((currYear-1)+"") %></td></tr>
                <tr><td><%=currYear-2 %></td><td><%=mySales.get((currYear-2)+"") %></td></tr>
                <tr><td><%=currYear-3 %></td><td><%=mySales.get((currYear-3)+"") %></td></tr>
                <tr><td>Total</td><td><%=mySales.get("Total") %></td></tr>
              </table>
            </td>
            <td align=left>
              <table cellpadding=3 cellspacing=3 border=1><th>Year</th><th>Sold</th>
                <tr><td><%=currYear %></td><td><%=oySales.get(currYear+"") %></td></tr>
                <tr><td><%=currYear-1 %></td><td><%=oySales.get((currYear-1)+"") %></td></tr>
                <tr><td><%=currYear-2 %></td><td><%=oySales.get((currYear-2)+"") %></td></tr>
                <tr><td><%=currYear-3 %></td><td><%=oySales.get((currYear-3)+"") %></td></tr>
                <tr><td>Total</td><td><%=oySales.get("Total") %></td></tr>
              </table>
            </td>
            <td align=left>
              <table cellpadding=3 cellspacing=3 border=1><th>Year</th><th>Sold</th>
                <tr><td><%=currYear %></td><td><%=lySales.get(currYear+"") %></td></tr>
                <tr><td><%=currYear-1 %></td><td><%=lySales.get((currYear-1)+"") %></td></tr>
                <tr><td><%=currYear-2 %></td><td><%=lySales.get((currYear-2)+"") %></td></tr>
                <tr><td><%=currYear-3 %></td><td><%=lySales.get((currYear-3)+"") %></td></tr>
                <tr><td>Total</td><td><%=lySales.get("Total") %></td></tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
 
        <%}%>
      <tr>
      <td colspan="10">
        <table cellpadding=3 cellspacing=3>
          <tr><td align=left>
              Edit Blurb Text
          </td></tr>
          <tr><td align=left>
              <form name="productForm" action="productHistory.jsp">
                <input type=hidden value="<%=location %>" name="location">
                <input type=hidden value="true" name="updateBlurb">
                <input type="hidden" value="<%=prod.getProductNum()          %>" name="prodNum">
                <textarea name="productBlurb" cols="50" rows="5"><%=wpi.getProductBlurb() %></textarea><br>
                <input type="submit" value="Save" class="btn" />
              </form>
          </td></tr>
        </table>
      </td>
    </tr>
        
        </table>
    </td></tr>
    <table >
    </center>
