
<jsp:directive.page import="com.soward.object.Product"/>
<jsp:directive.page import="com.soward.util.InventoryReport"/>
<jsp:directive.page import="java.text.NumberFormat"/>
<jsp:directive.page import="java.text.DecimalFormat"/>
<jsp:directive.page import="com.soward.util.SalesReport"/>
<jsp:directive.page import="com.soward.util.Utils"/>
<%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.InvBundle"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.util.AccountUtil"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.util.TransUtil"%>
<script type="text/javascript" src="js/comboBox.js"></script>
<SCRIPT LANGUAGE="JavaScript">
function IsNumeric(strString)
   //  check for valid numeric strings	
   {
   var strValidChars = "0123456789.- AMPM:";
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


function ValidateForm(){
	var dt=document.myform.dateOne;
	var dtt=document.myform.dateTwo;
  if(!IsNumeric(dt.value)){
    alert('Date must be in form: MM-dd-yyyy \nNot Numeric in: '+dt.value);
    dt.focus();
    return;
    }
  else if(dt.value.charAt(2)=="-"&& dt.value.charAt(5)=="-"){
    if(dtt.value.charAt(2)=="-"&& dtt.value.charAt(5)=="-"){
      document.myform.submit();
    }
    else{
      alert('Date must be in form: MM-dd-yyyy \nMisplaced - in: ' +dtt.value);
      dtt.focus();
      return;
    }
  }
  else{
    alert('Date must be in form: MM-dd-yyyy \nMisplaced - in: '+dt.value);
    dt.focus();
    return;
  }
 }

</script>
<SCRIPT LANGUAGE="JavaScript" SRC="css/_CalendarPopup.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"> var cal = new CalendarPopup(); </SCRIPT>


<%
	SalesReport salesReport = new SalesReport();
	HashMap<String, ArrayList<ArrayList<String>>> salesList = null;
	TimeZone tz = TimeZone.getTimeZone("America/Salt_Lake");
	Calendar cal = Calendar.getInstance();
  	cal.set(Calendar.HOUR_OF_DAY, 0) ;
  	cal.set(Calendar.MINUTE, 0) ;
    HashMap<String, String> otherLocationHM = new HashMap<String, String>();
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
    
	
	String startDate = sdf.format( cal.getTime() );
  	cal.set( Calendar.HOUR_OF_DAY, 23 ) ;
  	cal.set( Calendar.MINUTE, 59 ) ;
	String endDate = sdf.format( cal.getTime() );
	
	String message = request.getParameter( "message" );
	String accountNumLocation = request.getParameter( "accountNumLocation" );
	String dateOne = request.getParameter("dateOne");
	String dateTwo = request.getParameter("dateTwo"); 
	String invQtyEnd = request.getParameter("invQtyEnd"); 
	boolean isOnline = false;
	String currSelectedNum001 = "101";
	String currSelectedLoca001 = "Murray";
	String currSelectedNum002 = "102";
	String currSelectedLoca002 = "Lehi";
	String currSelectedNum003 = "103";
	String currSelectedNum004 = "DV";
	String currSelectedLoca003 = "Online";
	String currSelectedLoca004 = "DV";
	if(accountNumLocation!=null&&accountNumLocation.equalsIgnoreCase("102")){
		currSelectedNum001 = "102";
		currSelectedLoca001 = "Lehi";
	    currSelectedNum002 = "101";
	    currSelectedLoca002 = "Murray";
	}
	else if(accountNumLocation!=null&&accountNumLocation.equalsIgnoreCase("103")){
		currSelectedNum001 = "103";
		currSelectedLoca001 = "Online";
	    currSelectedNum003 = "101";
	    currSelectedLoca003 = "Murray";
	    isOnline = true;
	}
	else if(accountNumLocation!=null&&accountNumLocation.equalsIgnoreCase("104")){
		currSelectedNum001 = "104";
		currSelectedLoca001 = "DV";
	    currSelectedNum004 = "101";
	    currSelectedLoca004 = "Murray";
	}
	
	if(dateTwo==null&&dateOne==null&&accountNumLocation==null){
	    dateTwo=endDate;
	    dateOne=startDate;
	}
	else{
		salesList = salesReport.getQtySales(dateOne, dateTwo, accountNumLocation );
		if(salesList ==null){ 
		message = "Date range is too great. 4 days is max range.";
		}
	}
		%>
		 
		<h1>Location Sales Report</h1>
    		<p class="text">Get report for a location the lists product, quantity sold, and supplier for a date range. Click on the calendar icons or enter dates to specify a date range.</p>
      <table align="center">
       <tr>
       <th>Date Range</th></tr><tr>
       
		<form name="myform" method="post" action="./reportsLocationProductSold.jsp" >
		 <td><table border=0 width="100%"> <tr>
		 <td><table border=0> <tr>
            	<TD align=left>Start date: </td><td>
            	<A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateOne"%>,'<%="anchor"%>','MM-dd-yyyy 00:00'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>">
            	<img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;</TD>
      			<TD align=left><input size=20 type="text" value="<%=dateOne%>" name="dateOne">
            </td>
           </tr>
           <tr>
            	<TD align=right>  End date: </td><td>
            	<A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateTwo"%>,'<%="anchor"%>','MM-dd-yyyy 23:59'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>">
            	<img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;</TD>
      			<TD align=left>
      			<input size=20 type="text" value="<%=dateTwo%>" name="dateTwo">
      			</td>
            </tr>
            </table>
      			</td>
      			</tr>

		    <tr>
        <td  align=left>
        Select location:
        	<select name="accountNumLocation" value="test">
        		<option value="<%=currSelectedNum001 %>"><%=currSelectedLoca001 %></option>
        		<option value="<%=currSelectedNum002 %>"><%=currSelectedLoca002 %></option>
        		<option value="<%=currSelectedNum003 %>"><%=currSelectedLoca003 %></option>
        		<option value="<%=currSelectedNum004 %>"><%=currSelectedLoca004 %></option>
			 	</select>
			 </td></tr>

		    <tr><td colspan=4 align=center>
		 	<input type="button" class="btn" onclick="ValidateForm()" name="Enter" value="Enter" >
			 </td></tr>
      			</table></td>      			</tr>		  
        <tr><td colspan=8> 
		  <hr>
		</td>
		</tr>
		</table>
		</form>
		</center>
        	<% 
        	int count = 0;
            boolean flipShade = false;
            if(salesList!=null&&!salesList.isEmpty()){%>
			    <table class="common" id="viewTEInvoices" align="center" cellpadding="0" cellspacing="0" width="780">
                <tr>
				<th><b>Report run for <%=currSelectedLoca001%> from <%=dateOne+" to "+dateTwo %></b></th>
			</tr>
				<table class="common" id="viewTEInvoicesTWO" align="left" width="100%" cellpadding="0" cellspacing="0" style="width: 100%">
					<tr>
		        	<th>Supplier&nbsp;Name</th>
		        	<th>Count&nbsp;Sold</th>
		        	<th>Product&nbsp;Name</th>
		        	<th>Product&nbsp;DMM</th>
		        	</tr>
            <%
        	Set set = salesList.keySet();
        	Iterator iter = set.iterator();
        	while (iter.hasNext()){
        		String supp = (String)iter.next();
            	ArrayList<ArrayList> al = (ArrayList)salesList.get( supp );
        	
        	for ( ArrayList<ArrayList<String>> temp : al ) {
        		%><tr><%
		        		%><td><%=supp %></td> <%
		        	for(int i = 0; i< temp.size(); i++){
		        		%><td><%=temp.get( i ) %></td> <%
		        	}
		        	%></tr><%
                }
                }//end of for loop
                }//end of if null
                else{%>
                <center><font size="2" color="red">No data found for date range.</font> </center>
                <%}
		if ( message != null ) {
		%>
		<br>
		<center><font color=red size=4><%=message%></font></center>
		<%
		}
		%>
          </table>
          </table>
<%@include file="bottomLayout.jsp"%>
