
<%@page import="com.soward.object.Supplier"%>
<%@page import="com.soward.util.SupplierUtil"%>
<%@page import="com.soward.util.ProductUtils"%>
<%@page import="com.soward.util.StockOrderReport"%><jsp:directive.page import="com.soward.object.Product"/>
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
<SCRIPT LANGUAGE="JavaScript">
function IsNumeric(strString)
   //  check for valid numeric strings	
   {
   var strValidChars = "0123456789.-";
   var strChar;
   var blnResult = true;

   if (strString.length == 0) return false;

   //  test strString consists of valid characters listed above
   for (i = 0; i < strString.length && blnResult == true; i++)
      {
      strChar = strString.charAt(i);
      if (strValidChars.indexOf(strChar) == -1)
         {
         blnResult = false;
         }
      }
   return blnResult;
   }

function ValidateFormSingle(){
	var dt=document.singleForm.singleDate;
  if(!IsNumeric(dt.value)){
    alert('Date must be in form: 2007-05-10 \nNot Numeric in: '+dt.value);
    dt.focus();
    return;
  }
  else if(dt.value.charAt(4)=="-"&& dt.value.charAt(7)=="-"){
      document.singleForm.submit();
    }
  else{
    alert('Date must be in form: 2007-05-10 \nMisplaced - in: '+dt.value);
    dt.focus();
    return;
  }
 }


function ValidateForm(){
	var dt=document.myform.dateOne;
	var dtt=document.myform.dateTwo;
  if(!IsNumeric(dt.value)){
    alert('Date must be in form: 2007-05-10 \nNot Numeric in: '+dt.value);
    dt.focus();
    return;
  }
  else if(!IsNumeric(dtt.value)){
    alert('Date must be in form: 2007-05-10 \nNot Numeric in: '+dtt.value);
    dtt.focus();
    return;
  }
  else if(dt.value.charAt(4)=="-"&& dt.value.charAt(7)=="-"){
    if(dtt.value.charAt(4)=="-"&& dtt.value.charAt(7)=="-"){
      document.myform.submit();
    }
    else{
      alert('Date must be in form: 2007-05-10 \nMisplaced - in: ' +dtt.value);
      dtt.focus();
      return;
    }
  }
  else{
    alert('Date must be in form: 2007-05-10 \nMisplaced - in: '+dt.value);
    dt.focus();
    return;
  }
 }

</script>
<SCRIPT LANGUAGE="JavaScript" SRC="css/_CalendarPopup.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"> var cal = new CalendarPopup(); </SCRIPT>


<%
	InventoryReport invUtil = new InventoryReport();
	List<Product> invList = null;
	TimeZone tz = TimeZone.getTimeZone("America/Salt_Lake");
	Calendar cal = Calendar.getInstance();
 	cal.add( Calendar.MONTH, -1 ) ;
  	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String startDate = sdf.format( cal.getTime() );
  	cal.add( Calendar.MONTH, 1 ) ;
	String endDate = sdf.format( cal.getTime() );
	
	String message = request.getParameter( "message" );
	String location = request.getParameter( "location" );
	String dateOne = request.getParameter("dateOne");
	String dateTwo = request.getParameter("dateTwo"); 
	String supplier = request.getParameter("supplier");
	ArrayList<Supplier> supList = SupplierUtil.getAllSuppliersNumAndName();
	Supplier supName = null;
	List<Map<String,String>> prodList = null;
	  if(supplier!=null){
	    prodList = StockOrderReport.getInventorySoldReport(dateOne, dateTwo, location, supplier);
	    supName = SupplierUtil.getSupplierForNum(supplier);
	    //session.setAttribute("stockOrderReport", prodList);
	  }
  
	if(dateTwo==null&&dateOne==null){
	    dateTwo=endDate;
	    dateOne=startDate;
	}
		%>
	<br/>	 
<h1>Stock Order Report</h1>
<p class="text">The effort to diminuate the deluge of internet browser instances and augment<br/> server-side cpu and memory allocations yeilded the genesis of this report, thanks Kelly.</p>
       <table align="center">
       <tr>
       <th>Date Range</th></tr><tr>
       
		<form name="myform" method="post" action="./reportStockOrder.jsp" >
		 <td><table border=0 width="100%"> <tr>
		 <td><table border=0> <tr>
            	<TD align=left>Start date: </td><td>
            	<A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateOne"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>">
            	<img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;</TD>
      			<TD align=left><input size=10 type="text" value="<%=dateOne%>" name="dateOne">
            </td></tr>
            <tr>
           
            	<TD align=right>  End date: </td><td>
            	<A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateTwo"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>">
            	<img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;</TD>
      			<TD align=left>
      			<input size=10 type="text" value="<%=dateTwo%>" name="dateTwo">
      			</td>
            </tr>
            <tr><td align=right colspan=2>
            Location</td><td colspan=2>
            <select name="location">
              <option name="MURRAY" <%=location!=null&&location.equals("MURRAY")?"selected=selected":""%> value="MURRAY">Murray</option>
              <option name="LEHI"   <%=location!=null&&location.equals("LEHI")?"selected=selected":""%>   value="LEHI">Lehi</option>
              <option name="OREM"   <%=location!=null&&location.equals("OREM")?"selected=selected":""%>   value="OREM">DV</option>
            </select>
            </table>
      			</td>
      			</tr>

		    <tr> <td  align=left>
        Supplier:
           <select name="supplier">
             <%for(Supplier supp: supList){ 
             if(supplier!=null&&supp.getSupplierNum().equals(supplier)){
             %>
            	<option name="<%=supp.getSupplierNum() %>" selected=selected value="<%=supp.getSupplierNum() %>"><%=supp.getSupplierName() %></option>
             <%}else{%>
             %>
            	<option name="<%=supp.getSupplierNum() %>" value="<%=supp.getSupplierNum() %>"><%=supp.getSupplierName() %></option>
            <%}} %>
            </select>
		 	<input type="button" class="btn" onclick="ValidateForm()" name="Enter" value="Enter" >
			 </td></tr>
		    <tr><td colspan=4 align=center>
			 </td></tr>
			 
			 <%if(prodList!=null){
				 if(!prodList.isEmpty()){%>
		        <tr> <td  align=left colspan="5">
		        <center>
				  <b>																			
				    <a href="StockOrder.pdf?reportCollection=StockOrderJasperReport&dOne=<%=dateOne%>&supplier=<%=supplier%>&dTwo=<%=dateTwo%>&lName=<%=location%>">PDF</a>
				    &nbsp;
				    <a href="StockOrder.pdf?reportCollection=StockOrderJasperReport&dOne=<%=dateOne%>&supplier=<%=supplier%>&dTwo=<%=dateTwo%>&lName=<%=location%>&outputType=CSV">CSV</a>
				  </b>
 
				</td></tr>
		        <tr> <td  align=left>
                <b>
            	Product List for Supplier <%=supName.getSupplierName()%>
            </b>
            	<br/>
              <table width="100%" border="1" cellspacing="3" cellpadding="3">
                <tr>
                  <th>Product</th>
                  <th>Prod#</th>
                  <th>Catalog#</th>
                  <th>Count</th>
                </tr>
                <%
                int total = 0;
                boolean flip = true;
                for(Map<String, String> map: prodList){
                  String count = map.get("count");
                  String productName = map.get("productName");
                  String productNum = map.get("productNum");
                  String productCatalogNum = map.get("productCatalogNum");
                  total += Integer.parseInt(count);
                  String color = flip?"bgcolor=\"#dddddd\"":"";
                  flip = !flip;
                %>
                <tr <%=color%>>
                  <td><%=productName%></td>
                  <td><%=productNum%></td>
                  <td><%=productCatalogNum%></td>
                  <td><%=count%></td>
                </tr>
            <%}%>
            <tr>  
              <td colspan="3">Total</td>
              <td><%=total%></td>
            </tr>
              </table>
              	
			    </td></tr>
            <%}else{%>
            	<tr> <td  align=left>
                  <b>
            	No sales for Supplier <%=supName.getSupplierName()%> at <%=location %>
            </b>
            	</td></tr>
            <%}%>
            <%}%>
			 
			 
			 
      			</table></td>      			</tr>		  
        <tr><td colspan=8> 
		  <hr>
		</td>
		</tr>
		</table>
		</form>
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

