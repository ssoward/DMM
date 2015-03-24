
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


<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.object.Invoice"%>

<script type="text/javascript" src="js/inventoryAjax.js"></script>


<%
 
 ArrayList<Supplier> allSups = SupplierUtil.getAllSuppliersNumAndName();
 List<Departments> allDepartments = DepartmentsUtil.getAllDepartments();
  String department      = request.getParameter("department")!=null?request.getParameter("department"):"0";
 
 List<Events> allEvents = EventsUtil.getAllEvents();
 HashMap eventsHash = new HashMap<String, String>();
 HashMap deptHash = new HashMap<String, String>();
 HashMap descHash = new HashMap<String, String>();
 for(Events event: allEvents){
 	eventsHash.put(event.getEventCode(), event.getEventName());
 }
 for(Departments dept: allDepartments){
 	deptHash.put(dept.getDepartmentCode(), dept.getDepartmentName());
 }

 
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
 String loco = "";
 if(location!=null){
 	loco = "MURRAY";
 	if(location.equals("LehiProducts")){
 		loco = "LEHI";
 	}
 	if(location.equals("OremProducts")){
 		loco = "OREM";
 	}
 }
 //ArrayList<Transaction> transList = TransUtil.getTransListForProdNum( prodNum, LocationsDBName.valueOf(loco).name());
 //HashMap<String, String> ySales = new HashMap<String, String>();
 //if(getYearlySales!=null){
 //	ySales = ProductUtils.getProductYearlySold(prodNum, location);
 //}
 if(saveUpdatedProduct!=null){
 	  Product prod = new Product();
            String productNum           = request.getParameter("productNum");
		    prodNum = productNum;
            String productName          = request.getParameter("productName")!=null?request.getParameter( "productName" ):""; 
            String productAuthor        = request.getParameter("productAuthor")!=null?request.getParameter("productAuthor"):"";
            String productArtist        = request.getParameter("productArtist")!=null?request.getParameter("productArtist"):"";
            String productArranger      = request.getParameter("productArranger")!=null?request.getParameter("productArranger"):"";
            String productDescription   = request.getParameter("productDescription")!=null?request.getParameter("productDescription"):"";
            String productCost1         = request.getParameter("productCost1")!=null?request.getParameter("productCost1"):"";
            String productCost2         = request.getParameter("productCost2")!=null?request.getParameter("productCost2"):"";
            String productBarCode       = request.getParameter("productBarCode")!=null?request.getParameter("productBarCode"):"";
            String productSKU           = request.getParameter("productSKU")!=null?request.getParameter("productSKU"):"";
            String productCatalogNum    = request.getParameter("productCatalogNum")!=null?request.getParameter("productCatalogNum"):"";
            String productSupplier1     = request.getParameter("productSupplier1")!=null?request.getParameter("productSupplier1"):"";
            String numAvailable         = request.getParameter("numAvailable")!=null?request.getParameter("numAvailable"):"";
            String lastSold             = request.getParameter("lastSold")!=null?request.getParameter("lastSold"):"";
            String lastInvDate          = request.getParameter("lastInvDate")!=null?request.getParameter("lastInvDate"):"";
            String DCCatalogNum         = request.getParameter("DCCatalogNum")!=null?request.getParameter("DCCatalogNum"):"";
            
            
            String events1         = request.getParameter("events1")!=null?request.getParameter("events1"):"0";
            String events2         = request.getParameter("events2")!=null?request.getParameter("events2"):"0";
            String events3         = request.getParameter("events3")!=null?request.getParameter("events3"):"0";
            String events4         = request.getParameter("events4")!=null?request.getParameter("events4"):"0";
            String descriptions    = request.getParameter("descriptions")!=null?request.getParameter("descriptions"):"0";
			String category = department+events1+events2+events3+events4+descriptions;
            

            prod.setProductNum        ( productNum        );
            prod.setProductName       ( productName       );
            prod.setProductAuthor     ( productAuthor     );
            prod.setProductArtist     ( productArtist     );
            prod.setProductArranger   ( productArranger   );
            prod.setProductDescription( productDescription);
            prod.setCategory          ( category          );
            prod.setProductCost1      ( productCost1      );
            prod.setProductCost2      ( productCost2      );
            prod.setProductBarCode    ( productBarCode    );
            prod.setProductSKU        ( productSKU        );
            prod.setProductCatalogNum ( productCatalogNum );
            prod.setProductSupplier1  ( productSupplier1  );
            //prod.setProductSupplier2  ( productSupplier2  );
            //prod.setProductSupplier3  ( productSupplier3  );
            //prod.setProductSupplier4  ( productSupplier4  );
            prod.setNumAvailable      ( numAvailable      );
            prod.setLastSold          ( lastSold          );
            prod.setLastInvDate       ( lastInvDate       );
            prod.setDCCatalogNum      ( DCCatalogNum      );
            message = ProductUtils.save( prod ); 
 }
 Product prod = null;
 if(prodNum!=null&&prodNum.length()>2&&location!=null){ 
    prod = ProductUtils.fetchProductForNum( prodNum, loco ); 
 }
  if(printBarCode!=null&&prod!=null&&printer!=null){
 	BarcodeGenerator.printBarCode(prod, printCount, printer); 
 }
  String locationDisplay = loco;
%><center>
<form name="productForm" action="productInvEdit.jsp">
<% if(getYearlySales!=null){%>
<input type=hidden value="true" name="getYearlySales">
<%}%>
<input type=hidden value="<%=location %>" name="location">
<input type=hidden value="<%=location %>" name="saveUpdatedProduct">
<input type="hidden" value="<%=prod.getProductNum()          %>" name="productNum">
<input type="hidden" value="<%=prod.getProductNum()          %>" name="prodNum">
		<%if(message!=null){%>
		<center><font color=red size=3><%=message%></font></center>
		 <%}%>
     <br/>
    <table >
    <tr><td>
<table class="simple" cellspacing="0" cellpadding="0">
<tr><td colspan=8><center><b>Edit Product for <%=prod.getProductNum() %></b></center>
<tr><td>

    <tr><td>
<table border=0 cellspacing="3" cellpadding="3">

<tr><td>
	<table border=0 cellspacing="3" cellpadding="3">
    <tr><td>Title:          </td><td><input size="30" type="text" value="<%=prod.getProductName()          %>" name="productName"> </td></tr>
    <tr><td>CatalogNum:     </td><td><input size="30" type="text" value="<%=prod.getProductCatalogNum()    %>" name="productCatalogNum"> </td></tr>
    <tr><td>Price:          </td><td><input size="30" type="text" value="<%=prod.getProductCost1()         %>" name="productCost1"> </td></tr>
    <tr><td>Discount:       </td><td><input size="30" type="text" value="<%=prod.getProductCost2()         %>" name="productCost2"> </td></tr>
    <tr><td>BarCode:        </td><td><input size="30" type="text" value="<%=prod.getProductBarCode()       %>" name="productBarCode"> </td></tr>
    <tr><td>Composer:       </td><td><input size="30" type="text" value="<%=prod.getProductAuthor()        %>" name="productAuthor"> </td></tr>
    <tr><td>Arranger:       </td><td><input size="30" type="text" value="<%=prod.getProductArranger()      %>" name="productArranger"> </td></tr>
    <tr><td>Artist:         </td><td><input size="30" type="text" value="<%=prod.getProductArtist()        %>" name="productArtist"> </td></tr>
    <tr><td>Description:    </td><td><input size="30" type="text" value="<%=prod.getProductDescription()   %>" name="productDescription"> </td></tr>
    <tr><td>Supplier1:      </td><td>
     <select name="productSupplier1">
     	<option value="<%=prod.getSupplier().getSupplierNum() %>"><%=prod.getSupplier().getSupplierName()%></option>
    	<%for(Supplier sup: allSups){ %>
    		<option value="<%= sup.getSupplierNum() %>"><%=sup.getSupplierName() %></option>
    	<%} %>
    </select> 
    </td></tr>

    <tr><td>DMM#:         </td><td><input type="text" readonly value="<%=prod.getProductSKU()%>" name=""> </td>
    </tr>
   
    </table>
    </td>
	<td>    
	<table border=0 cellspacing="3" cellpadding="3">
	<!-- 
    <tr><td>DMM#:         </td><td><input type="text" readonly value="N/A" name="productSKU"> </td>
    <tr><td>Number:         </td><td><input readonly type="text" value="N/A" name="productNum"> </td>
    
    <tr><td>Quantity:       </td><td><input type="text" value="<%=prod.getNumAvailable()         %>" name="numAvailable"> </td></tr>
    <tr><td>LastSold:           </td><td><input type="text" value="<%=prod.getLastSold()             %>" name="lastSold"> </td></tr>
    <tr><td>LastInvDate:        </td><td><input type="text" value="<%=prod.getLastInvDate()          %>" name="lastInvDate"> </td></tr>
    -->
    <tr><td>DCCatalogNum:       </td><td><input type="text" value="<%=prod.getDCCatalogNum()         %>" name="DCCatalogNum"> </td></tr>
    <tr> <td>Department:   </td><td>
     <select name="department">
     	<%	
     		String name = "";
     		String key = "";
     		try{
     			key = prod.getCategory().charAt(0)+"";
     			name = (String)deptHash.get(key);
     		}catch(Exception e){
     		}
     		if(name!=null){%>
     		<option value="<%=key%>"><%= name %></option>
     		<%} %>
    	<%for(Departments sup: allDepartments){ %>
    		<option value="<%= sup.getDepartmentCode() %>"><%=sup.getDepartmentName()%></option>
    	<%} 
    	List<Descriptions> allDescriptions = DescriptionsUtil.getAll2CodeDescriptions(key);
    	 for(Descriptions desc: allDescriptions){
 	 		descHash.put(desc.getDescriptionCode(), desc.getDescriptionName());
 		}
    	%>
    </select> 
    </td></tr>
    
     <tr><td>Descriptions:   </td><td>
     <select name="descriptions">
    	<%  
     		name = "";
     		key = "";
     		try{
     			key = prod.getCategory().charAt(5)+"";
     			name = (String)descHash.get(key);
     		}catch(Exception e){
     		}
     		if(name!=null){%>
     		<option value="<%=key%>"><%= name %></option>
     		<%} %>
    	<%for(Descriptions sup: allDescriptions){ %>
    		<option value="<%= sup.getDescriptionCode() %>"><%=sup.getDescriptionName()%></option>
    	<%} %>
    </select> 
    </td></tr>
     <tr><td>Events:   </td><td>
     <select name="events1">
     <%  
     		name = "";
     		key = "";
     		try{
     			key = prod.getCategory().charAt(1)+"";
     			name = (String)eventsHash.get(key);
     		}catch(Exception e){
     		}
     		if(name!=null){%>
     		<option value="<%=key%>"><%= name %></option>
     		<%} %>
    	<%for(Events sup: allEvents){ %>
    		<option value="<%= sup.getEventCode() %>"><%=sup.getEventName()%></option>
    	<%} %>
    </select> 
    </td></tr>
 
     <tr><td>Events:   </td><td>
     <select name="events2">
     <%  
     		name = "";
     		key = "";
     		try{
     			key = prod.getCategory().charAt(2)+"";
     			name = (String)eventsHash.get(key);
     		}catch(Exception e){
     		}
     		if(name!=null){%>
     		<option value="<%=key%>"><%= name %></option>
     		<%} %>
    	<%for(Events sup: allEvents){ %>
    		<option value="<%= sup.getEventCode() %>"><%=sup.getEventName()%></option>
    	<%} %>
    </select> 
    </td></tr>   
 
     <tr><td>Events:   </td><td>
     <select name="events3">
     <%  
     		name = "";
     		key = "";
     		try{
     			key = prod.getCategory().charAt(3)+"";
     			name = (String)eventsHash.get(key);
     		}catch(Exception e){
     		}
     		if(name!=null){%>
     		<option value="<%=key%>"><%= name %></option>
     		<%} %>
    	<%for(Events sup: allEvents){ %>
    		<option value="<%= sup.getEventCode() %>"><%=sup.getEventName()%></option>
    	<%} %>
    </select> 
    </td></tr>
 
     <tr><td>Events:   </td><td>
     <select name="events4">
     <%  
     		name = "";
     		key = "";
     		try{
     			key = prod.getCategory().charAt(4)+"";
     			name = (String)eventsHash.get(key);
     		}catch(Exception e){
     		}
     		if(name!=null){%>
     		<option value="<%=key%>"><%= name %></option>
     		<%} %>
    	<%for(Events sup: allEvents){ %>
    		<option value="<%= sup.getEventCode() %>"><%=sup.getEventName()%></option>
    	<%} %>
    </select> 
    </td></tr> 
    </table>
    </td></tr>  
    <tr><td colspan=8 valign=center><input type=submit value="Save" class="btn"/> 
    </form>
    &nbsp;&nbsp;&nbsp;<input type=button value="Close" onClick="javascript:window.close();" class="btn">
    </td></tr>
 
    </table>
    </td></tr>
    <table >
</center>
