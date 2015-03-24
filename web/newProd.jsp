<jsp:directive.page import="com.soward.object.Product"/>
<jsp:directive.page import="com.soward.util.ProductUtils"/>
<jsp:directive.page import="com.soward.util.Utils"/>
<jsp:directive.page import="com.soward.util.SupplierUtil"/>
<jsp:directive.page import="com.soward.object.Supplier"/>
<jsp:directive.page import="com.soward.object.Departments"/>
<jsp:directive.page import="com.soward.util.DepartmentsUtil"/>
<jsp:directive.page import="com.soward.object.Descriptions"/>
<jsp:directive.page import="com.soward.util.DescriptionsUtil"/>
<jsp:directive.page import="com.soward.object.Events"/>
<jsp:directive.page import="com.soward.util.EventsUtil"/>


<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.object.Invoice"%>

<script type="text/javascript" src="js/comboBox.js"></script>
<script type="text/javascript" src="js/inventoryAjax.js"></script>
<SCRIPT LANGUAGE="JavaScript">                       
  function refreshDescriptions(){ 
  	   document.productForm.saveUpdatedProduct.value = '';	                                                                 
       document.productForm.submit();      
  }                                                                                       
</script>

<%
 Calendar cal = Calendar.getInstance();
 SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
 String defaultInvDate = sdf.format( cal.getTime() );
 boolean popup         = request.getParameter("popup")!=null?true:false;

 ArrayList<Supplier> sups = SupplierUtil.getAllSuppliersNumAndName();
 List<Departments> depts = DepartmentsUtil.getAllDepartments();
 String department      = request.getParameter("department")!=null?request.getParameter("department"):"0";
 List<Descriptions> allDescriptions = DescriptionsUtil.getAll2CodeDescriptions(department); 
 List<Events> allEvents = EventsUtil.getAllEvents();
 String mss = request.getParameter( "message" );
 String saveUpdatedProduct = request.getParameter( "saveUpdatedProduct" );
 String prodNum = request.getParameter( "prodNum" );
 String productName          = request.getParameter("productName")!=null?request.getParameter( "productName" ):""; 
 String productAuthor        = request.getParameter("productAuthor")!=null?request.getParameter("productAuthor"):"";
 String productArtist        = request.getParameter("productArtist")!=null?request.getParameter("productArtist"):"";
 String productArranger      = request.getParameter("productArranger")!=null?request.getParameter("productArranger"):"";
 String productDescription   = request.getParameter("productDescription")!=null?request.getParameter("productDescription"):"";
 String productCost1         = request.getParameter("productCost1")!=null?request.getParameter("productCost1"):"0.0";
 String productCost2         = request.getParameter("productCost2")!=null?request.getParameter("productCost2"):"0.0";

 String productBarCode       = request.getParameter("productBarCode")!=null?request.getParameter("productBarCode"):"";
 //String productSKU           = request.getParameter("productSKU")!=null?request.getParameter("productSKU"):"";
 String productCatalogNum    = request.getParameter("productCatalogNum")!=null?request.getParameter("productCatalogNum"):"";
 String productSupplier1     = request.getParameter("productSupplier1")!=null?request.getParameter("productSupplier1"):"";

 String numAvailable         = request.getParameter("numAvailable")!=null?request.getParameter("numAvailable"):"0";
 String lehiQty              = request.getParameter("lehiQty")!=null?request.getParameter("lehiQty"):"0";
 String murrayQty            = request.getParameter("murrayQty")!=null?request.getParameter("murrayQty"):"0";
 String oremQty            = request.getParameter("oremQty")!=null?request.getParameter("oremQty"):"0";
 String lastSold             = request.getParameter("lastSold")!=null?request.getParameter("lastSold"):"";
 String lastInvDate          = request.getParameter("lastInvDate")!=null?request.getParameter("lastInvDate"):defaultInvDate;
 String DCCatalogNum         = request.getParameter("DCCatalogNum")!=null?request.getParameter("DCCatalogNum"):"";

 String events1         = request.getParameter("events1")!=null?request.getParameter("events1"):"0";
 String events2         = request.getParameter("events2")!=null?request.getParameter("events2"):"0";
 String events3         = request.getParameter("events3")!=null?request.getParameter("events3"):"0";
 String events4         = request.getParameter("events4")!=null?request.getParameter("events4"):"0";
 String descriptions    = request.getParameter("descriptions")!=null?request.getParameter("descriptions"):"0";
 if(saveUpdatedProduct!=null&&saveUpdatedProduct.length()>5){
 	  Product prod = new Product();
            //String productNum           = request.getParameter("productNum");
		    prodNum = prod.getProductNum();
            
			String category = department+events1+events2+events3+events4+descriptions;

            prod.setProductName       ( productName       );
            prod.setProductAuthor     ( productAuthor     );
            prod.setProductArtist     ( productArtist     );
            prod.setProductArranger   ( productArranger   );
            prod.setProductDescription( productDescription);
            prod.setCategory          ( category          );
            prod.setProductCost1      ( productCost1      );
            prod.setProductCost2      ( productCost2      );
 
            prod.setProductBarCode    ( productBarCode    );
            //prod.setProductSKU        ( productSKU        );
            prod.setProductCatalogNum ( productCatalogNum );
            prod.setProductSupplier1  ( productSupplier1  );

            prod.setNumAvailable      ( numAvailable      );
            prod.setLastSold          ( lastSold          );
            prod.setLastInvDate       ( lastInvDate       );
            prod.setDCCatalogNum      ( DCCatalogNum      );
            
           	if(productName!=null&&productName.length()>1&&ProductUtils.saveNewToAllLocations(prod, lehiQty, murrayQty, oremQty)){
           	mss = "<br>Successfully created product for all locations.";
           	}else{
           	mss = "<br>Failed to create product for all locations.";
           	}
          }
%>
<h1>Create New Product</h1>
<form name="productForm" action="newProd.jsp">
<input type=hidden value="saveUpdatedProduct" name="saveUpdatedProduct">
		<%if(mss!=null){%>
		<p class="message"><%=mss%></p>
		 <%}%>
		<p class="text">To create a new product select which inventory you'd like to have your new product be a member of, and fill out the fields below.</p>
<table cellspacing="0" cellpadding="0" align="center" class="simple">
<tr><td>
<table border=0 cellspacing="3" cellpadding="3">

<tr><td>
	<table border=0 cellspacing="3" cellpadding="3">
    <tr><td>Title:       </td><td><input type="text" value="<%=productName        %>" name="productName"       > </td></tr>
    <tr><td>CatalogNum:  </td><td><input type="text" value="<%=productCatalogNum  %>" name="productCatalogNum" > </td></tr>
    <tr><td>Price:       </td><td><input type="text" value="<%=productCost1       %>" name="productCost1"      > </td></tr>
    <tr><td>Discount     </td><td><input type="text" value="<%=productCost2       %>" name="productCost2"      > </td></tr>
    <tr><td>BarCode:     </td><td><input type="text" value="<%=productBarCode     %>" name="productBarCode"    > </td></tr>
    <tr><td>Composer:    </td><td><input type="text" value="<%=productAuthor      %>" name="productAuthor"     > </td></tr>
    <tr><td>Arranger:    </td><td><input type="text" value="<%=productArranger    %>" name="productArranger"   > </td></tr>
    <tr><td>Artist:      </td><td><input type="text" value="<%=productArtist      %>" name="productArtist"     > </td></tr>
    <tr><td>Description: </td><td><input type="text" value="<%=productDescription %>" name="productDescription"> </td></tr>
    <tr><td>Supplier1:   </td><td>
     <select name="productSupplier1">
     <%if(productSupplier1!=null&&productSupplier1.length()>0){
       Supplier pickedSup = new Supplier();
        for(Supplier sup: sups){
          if(sup.getSupplierNum().equals(productSupplier1)){
            pickedSup = sup;
            break;
          }
        }
        %>
    		<option value="<%= pickedSup.getSupplierNum() %>"><%=pickedSup.getSupplierName() %></option>
        <%
     }%>
    	<%for(Supplier sup: sups){ %>
    		<option value="<%= sup.getSupplierNum() %>"><%=sup.getSupplierName() %></option>
    	<%} %>
    </select> 
    </td></tr>
   
    </table>
    </td>
	<td>    
	<table border=0 cellspacing="3" cellpadding="3">
    
    <tr><td colspan=1>Qty: Murray
    <select name="murrayQty" >
    <%if(murrayQty!=null){
      try{
      int qty = Integer.parseInt(murrayQty);
      if(qty>0){ %><option value="<%=qty%>"><%=qty%></option><% }
      }catch(Exception e){e.printStackTrace();}
    }%>
    <%for(int i = 0; i < 50; i++){%>
    <option value="<%=i%>"><%=i%></option>
    <%}%></td><td>Lehi
    <select name="lehiQty" >
    <%if(lehiQty!=null){
      try{
      int qty = Integer.parseInt(lehiQty);
      if(qty>0){ %><option value="<%=qty%>"><%=qty%></option><% }
      }catch(Exception e){e.printStackTrace();}
    }%>
    <%for(int i = 0; i < 50; i++){%>
    <option value="<%=i%>"><%=i%></option>
    <%}%></td><td>DV
    <select name="oremQty" >
    <%if(oremQty!=null){
      try{
      int qty = Integer.parseInt(oremQty);
      if(qty>0){ %><option value="<%=qty%>"><%=qty%></option><% }
      }catch(Exception e){e.printStackTrace();}
    }%>
    <%for(int i = 0; i < 50; i++){%>
    <option value="<%=i%>"><%=i%></option>
    <%}%></td>
    </tr>
    
    <tr><td>DCCatalogNum:     </td><td colspan=2><input type="text" value="<%=DCCatalogNum %>" name="DCCatalogNum" > </td></tr>
    <tr><td>Department:   </td><td colspan=2>
     <select name="department" onchange="refreshDescriptions();">

     <%if(department!=null&&department.length()>0){
       Departments pickedDept = new Departments();
        for(Departments dept: depts){
          if(dept.getDepartmentCode().equals(department)){
            pickedDept = dept;
            break;
          }
        }
        %>
    		<option value="<%= pickedDept.getDepartmentCode() %>"><%=pickedDept.getDepartmentName()%></option>
        <%
     }%>
 
    	<%
    	int count = 0;
    	for(Departments sup: depts){ 
    		count++;
    		String selected = count==400?"SELECTED":"";
    	%>
    		<option <%=selected %> value="<%= sup.getDepartmentCode() %>"><%=sup.getDepartmentName()%></option>
    	<%} %>
    </select> 
    </td></tr>
    
    <tr><td>Descriptions:   </td><td colspan=2>
     <select name="descriptions">

     <%if(descriptions!=null&&descriptions.length()>0&&!descriptions.equals("null")){
       Descriptions pickedDept = new Descriptions();
        for(Descriptions dept: allDescriptions){
          if(dept.getDescriptionCode().equals(descriptions)){
            pickedDept = dept;
            break;
          }
        }
        //if the department was reset we cannot set description to previous value
        if(pickedDept.getDescriptionCode()!=null){
        %>
    		<option value="<%= pickedDept.getDescriptionCode() %>"><%=pickedDept.getDescriptionName()%></option>
        <%
        }
     }%>
 

    	<%for(Descriptions sup: allDescriptions){ %>
    		<option value="<%= sup.getDescriptionCode() %>"><%=sup.getDescriptionName()%></option>
    	<%} %>
    </select> 
    </td></tr>
    <tr><td>Events:   </td><td colspan=2>
     <select name="events1">

     <%if(events1!=null&&events1.length()>0){
       Events pickedEnt = new Events();
        for(Events env: allEvents){
          if(env.getEventCode().equals(events1)){
            pickedEnt = env;
            break;
          }
        }
        %>
    		<option value="<%= pickedEnt.getEventCode() %>"><%=pickedEnt.getEventName()%></option>
        <%
     }%>
 
    	<%for(Events sup: allEvents){ %>
    		<option value="<%= sup.getEventCode() %>"><%=sup.getEventName()%></option>
    	<%} %>
    </select> 
    </td></tr>
 
     <tr><td>Events:   </td><td colspan=2>
     <select name="events2">

     <%if(events2!=null&&events2.length()>0){
       Events pickedEnt = new Events();
        for(Events env: allEvents){
          if(env.getEventCode().equals(events2)){
            pickedEnt = env;
            break;
          }
        }
        %>
    		<option value="<%= pickedEnt.getEventCode() %>"><%=pickedEnt.getEventName()%></option>
        <%
     }%>
 

    	<%for(Events sup: allEvents){ %>
    		<option value="<%= sup.getEventCode() %>"><%=sup.getEventName()%></option>
    	<%} %>
    </select> 
    </td></tr>   
 
     <tr><td>Events:   </td><td colspan=2>
     <select name="events3">

     <%if(events3!=null&&events3.length()>0){
       Events pickedEnt = new Events();
        for(Events env: allEvents){
          if(env.getEventCode().equals(events3)){
            pickedEnt = env;
            break;
          }
        }
        %>
    		<option value="<%= pickedEnt.getEventCode() %>"><%=pickedEnt.getEventName()%></option>
        <%
     }%>
 


    	<%for(Events sup: allEvents){ %>
    		<option value="<%= sup.getEventCode() %>"><%=sup.getEventName()%></option>
    	<%} %>
    </select> 
    </td>
 
     <tr><td>Events:   </td><td colspan=2>
     <select name="events4">

     <%if(events4!=null&&events4.length()>0){
       Events pickedEnt = new Events();
        for(Events env: allEvents){
          if(env.getEventCode().equals(events4)){
            pickedEnt = env;
            break;
          }
        }
        %>
    		<option value="<%= pickedEnt.getEventCode() %>"><%=pickedEnt.getEventName()%></option>
        <%
     }%>
 


    	<%for(Events sup: allEvents){ %>
    		<option value="<%= sup.getEventCode() %>"><%=sup.getEventName()%></option>
    	<%} %>
    </select> 
    </td></tr> 
    </table>
    </td></tr>  
 
    </table>
    </td></tr>
    </table>
    <center><br>
    <input type=button value="Save" onclick="this.value='Loading...'; this.disabled =true;this.form.submit()" class="btn" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <%if(popup){%>
    &nbsp;&nbsp;&nbsp;<input type=button value="Close" onClick="javascript:window.close();" class="btn">
    <%}%>
    <!--
    <input type=button value="Update Descriptions" onclick="refreshDescriptions();"/>
    -->
    <br>
        </form> 
</center>


