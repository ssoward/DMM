

<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.soward.util.ProductUtils"%>
<%@page import="com.soward.util.Utils"%><jsp:directive.page import="com.soward.object.Product"/>
<jsp:directive.page import="com.soward.util.InventoryReport"/>
<jsp:directive.page import="java.text.NumberFormat"/>
<jsp:directive.page import="java.text.DecimalFormat"/>
<jsp:directive.page import="com.soward.enums.LocationsDBName"/>
<%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.InvBundle"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.util.AccountUtil"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.util.TransUtil"%>

<SCRIPT LANGUAGE="JavaScript" SRC="css/_CalendarPopup.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"> var cal = new CalendarPopup(); </SCRIPT>


<%
	ProductUtils invUtil = new ProductUtils();
	ArrayList<Product> invList = new ArrayList<Product>();
	String message = request.getParameter( "message" );
	String direction = request.getParameter( "dir" );
	String reset = request.getParameter( "reset" );
	ArrayList<String> offSet = (ArrayList<String>)request.getSession().getAttribute("offSet");
	if(offSet==null||reset!=null){
	    offSet = new ArrayList<String>();
	    offSet.add("0");
	}
	String searchType = request.getParameter( "searchType" );
	try{
		if(!StringUtils.isBlank(searchType)){
		    String off = null;
		    if(direction!=null){
		        if(direction.equals("nx")){
		            off = offSet!=null?offSet.get(offSet.size()-1):null;
		        }
		        if(direction.equals("pv")){
		            offSet.remove(offSet.size()-1);
		            try{offSet.remove(offSet.size()-1);}catch(Exception e){}
		            off = offSet!=null?offSet.get(offSet.size()-1):null;
		        }
		                
		    }
			invList = invUtil.getProducts(searchType, off);
			if(invList!=null){
			    try{
			        Product last = invList.get(invList.size()-1); 
			 		if(last!=null&&last.getProductNum()!=null){
			     		offSet.add(last.getProductNum());
			     		request.getSession().setAttribute("offSet", offSet);
			     	}
			    }catch(Exception e){
			        e.printStackTrace();
			    }
			}
			
		}
	}catch(Exception e){
	    e.printStackTrace();
	}
		%>
		 
<h1>Product Validate Report</h1>
<p class="text">Searches products and returns selected criteria
</p>
       <table border="0" align="center">
       
		<form name="myform" method="get" action="./reportProductValidate.jsp" >
           <tr>
             <td>
               Missing Events:</td>
             <td><input type="checkbox" value="missingEvents" <%=searchType!=null&&searchType.equals("missingEvents")?"CHECKED=CHECKED":"" %>name="searchType" />
             </td>
           </tr>
		    <tr><td colspan=4 align=center>
		 	<input type="submit" class="btn" value="Enter">
			 <input type="hidden" value="true" name="reset" >
			 </td></tr>
		</table>
		  <hr>
		</form>
		<%if(invList!=null&&!invList.isEmpty()){ 
		%>
        <br/>
        </center>
    <%
          
          String prevLink = offSet.size()>2?"<a href=\"./reportProductValidate.jsp?searchType="+searchType+"&dir=pv\"><--Previous</a>":null;
          String nextLink = "<a href=\"./reportProductValidate.jsp?searchType="+searchType+"&dir=nx\">Next--></a>";
      %>
      <table border="0" width="100%">
      <tr>
      <%if(!StringUtils.isBlank(prevLink)){ %>
        <td align="left">
      <%=prevLink %>
        </td>
      <%} %>
        <td align="right">
      <%=nextLink %>
        </td></tr>
		<table class="sortable, common" id="viewTEInvoices" cellpadding="0" cellspacing="0" width="780" align="center">
			<tr>
        	<th>#</th>
        	<th>DMM#</th>
        	<th>Catalog#</th>
        	<th>Name</th>
        	<th>Desc</th>
        	<th>Catagory#</th>
        	</tr>      
        	<% 
        	int count = 0;
        	TransUtil tu = new TransUtil();
          	boolean flipShade = false;
        	for ( Product temp : invList ) {
                count++;
                if(count>100){
                  break;
                  }
                %>
                  <% if(flipShade){%>
                <tr bgcolor="#eeeeee">
                <%}else{%><tr><%}%>
                <td align="right"><%= count%></td>
                <td align="left"><%=temp.getProductNum()        %></td>
                <%
                String pcn = temp.getProductCatalogNum();
                if(pcn!=null&&pcn.length()>10){
                	pcn = pcn.substring(0,10);
                }
                %>
                <td align="left"><%=pcn        %></td>
                <td><%=temp.getProductName()       %></td>
                <td><%=temp.getProductDescription()      %></td>
                <td><%=temp.getCategory()      %></td>
                </tr>
                <%
                flipShade = !flipShade;
                }//end of for loop %>
                    <%
	                //end of if(acc!=null)
                    }else{%><center>
                    Select search type.
                    </center>
                <%}//end of else %>
    <%
		if ( message != null ) {
		%>
		<tr>
		<td><%=message%></td>
		</tr>
		<%
		}
		%>
</table>
<%@include file="bottomLayout.jsp"%>

