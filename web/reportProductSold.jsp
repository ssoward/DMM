

<%@page import="org.apache.commons.lang.StringUtils"%><jsp:directive.page import="com.soward.object.Product"/>
<jsp:directive.page import="com.soward.util.InventoryReport"/>
<jsp:directive.page import="java.text.NumberFormat"/>
<jsp:directive.page import="java.text.DecimalFormat"/>
<jsp:directive.page import="com.soward.util.ProductSoldReport"/>
<jsp:directive.page import="com.soward.object.Transaction"/>
<jsp:directive.page import="com.soward.object.Events"/>
<jsp:directive.page import="com.soward.object.Descriptions"/>
<jsp:directive.page import="com.soward.object.Departments"/>
<jsp:directive.page import="com.soward.util.DepartmentsUtil"/>
<jsp:directive.page import="com.soward.util.DescriptionsUtil"/>
<jsp:directive.page import="com.soward.util.EventsUtil"/>
<jsp:directive.page import="com.soward.enums.LocationsDBName"/>
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
	 document.myform.saveUpdatedProduct.value = 'thisisatleastlongerthanfivecharacters';
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
<SCRIPT LANGUAGE="JavaScript">                       
  function refreshDescriptions(){ 
  	   document.myform.saveUpdatedProduct.value = '';	                                                                 
       document.myform.submit();      
  }                                                                                       
</script>

<%
	String events1         = request.getParameter("events1")!=null?request.getParameter("events1"):"0";
	String saveUpdatedProduct = request.getParameter( "saveUpdatedProduct" );
	List<Departments> allDepartments = DepartmentsUtil.getAllDepartments();
    String department      = request.getParameter("department")!=null?request.getParameter("department"):"0";
    String location      = request.getParameter("location")!=null?request.getParameter("location"):"ALL";
    String descriptions    = request.getParameter("descriptions")!=null?request.getParameter("descriptions"):"0";
    List<Descriptions> allDescriptions = DescriptionsUtil.getAll2CodeDescriptions(department); 
    List<Events> allEvents = EventsUtil.getAllEvents();
	
	ProductSoldReport invUtil = new ProductSoldReport();
	HashMap<String, ArrayList<HashMap<String, String>>> salesListM = null;
	HashMap<String, ArrayList<HashMap<String, String>>> salesListL = null;
	HashMap<String, ArrayList<HashMap<String, String>>> salesListO = null;
	HashMap<String, ArrayList<HashMap<String, String>>> all = null;
	TimeZone tz = TimeZone.getTimeZone("America/Salt_Lake");
	Calendar cal = Calendar.getInstance();
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
	
	String startDate = sdf.format( cal.getTime() );
  	cal.add( Calendar.DAY_OF_WEEK, 7 ) ;
	String endDate = sdf.format( cal.getTime() );
	
	String message = request.getParameter( "message" );
	String dateOne = request.getParameter("dateOne");
	String dateTwo = request.getParameter("dateTwo");
	boolean allBool = false;
	if(dateTwo==null&&dateOne==null){
	    dateTwo=endDate;
	    dateOne=startDate;
	}
	else if(saveUpdatedProduct!=null&&saveUpdatedProduct.length()>5){
		salesListM = invUtil.getQtySales(dateOne, dateTwo, "MURRAY", department, descriptions, events1);
		all = salesListM;
//		salesListL = invUtil.getQtySales(dateOne, dateTwo, "LEHI", department, descriptions, events1);
//		all.putAll(salesListL);
//		salesListO = invUtil.getQtySales(dateOne, dateTwo, "OREM", department, descriptions, events1);
//		all.putAll(salesListO);
		if(all==null){
			message = "Date range is too great. 1 weeks is max range."; 
		}
	}
		%>
<h1>Sold Per Dept Report</h1>
<p class="text">This reports runs against the transcations and returns the count for all items sold within the dates given.</p>
		  <p class="text">Click on the calendar icons or enter dates to specify a date range. If you like to get data for just one day use single day calendar.</p>
       <table align="center">
       <tr>
       <th>Date Range</th></tr><tr>
       
		<form name="myform" method="post" action="./reportProductSold.jsp" >
				
		 <td><table border=0 width="100%"> <tr>
		 <td><table border=0> <tr>
		
<!-- Add desctiptions & events	 -->
 <tr><td>Department:   </td><td>
     <select name="department" onchange="refreshDescriptions();">
       <%if(department!=null&&department.length()>0&&!department.equals("0")){
       Departments pickedDept = new Departments();
        for(Departments dept: allDepartments){
          if(dept.getDepartmentCode().equals(department)){
            pickedDept = dept;
            break;
          }
        }
        %>
    		<option value="<%= pickedDept.getDepartmentCode() %>"><%=pickedDept.getDepartmentName()%></option>
        <%
     }%>
     <option value="">NONE</option>
    	<%
    	int countt = 0;
    	for(Departments sup: allDepartments){ 
	       if(StringUtils.isBlank(sup.getDepartmentName())){
	          continue;
	       }
    		countt++;
    		String selected = countt==400?"SELECTED":"";
    	%>
    		<option <%=selected %> value="<%= sup.getDepartmentCode() %>"><%=sup.getDepartmentName()%></option>
    	<%} %>
    </select> 
    </td></tr>
    
    <tr><td>Descriptions:   </td><td>
     <select name="descriptions">
      <%if(descriptions!=null&&descriptions.length()>0&&!descriptions.equals("null")&&!descriptions.equals("0")){
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
 
    		<option value="">NONE</option>
    	<%for(Descriptions sup: allDescriptions){ %>
    		<option value="<%= sup.getDescriptionCode() %>"><%=sup.getDescriptionName()%></option>
    	<%} %>
    </select> 
    </td></tr>
    <tr><td>Events:   </td><td>
     <select name="events1">
        <%if(events1!=null&&events1.length()>0&&!events1.equals("0")){
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
     <option value="">NONE</option>
    	<%for(Events sup: allEvents){ %>
    		<option value="<%= sup.getEventCode() %>"><%=sup.getEventName()%></option>
    	<%} %>
    </select> 
    </td></tr>



<!--  -->		

            	<TD align=left>Start date: </td><td>
            	<A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateOne"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>">
            	<img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;
      			<input size=10 type="text" value="<%=dateOne%>" name="dateOne">
            </td></tr>
            <tr>
           
            	<TD align=left>End date: </td><td>
            	<A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateTwo"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>">
            	<img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;
      			<input size=10 type="text" value="<%=dateTwo%>" name="dateTwo">
      			</td>
            </tr>
            <tr><td colspan=4 align=center>
             <input type=hidden value="saveUpdatedProduct" name="saveUpdatedProduct">
		 	<input type="button" class="btn" onclick="ValidateForm()" name="Enter" value="Enter" >
			 </td></tr>
      			</table></td>      			</tr>		  
        <tr><td colspan="8"> 
		  <hr>
		</td>
		</tr>
            </table>
      			</td>
      			</tr>

		</table>
		</form>
			<% 
      try{
        	int count = 0;
            boolean flipShade = false;
            if(all!=null&&!all.isEmpty()){%>
			    <table class="common" id="viewTEInvoices" align="center" cellpadding="0" cellspacing="0" width="100%">
                <tr width="100%">
				<th><b>Report run for <%=location%> from <%=dateOne+" to "+dateTwo %></b></th>
			</tr>
				<table class="sortable, common" id="viewTEInvoicesTWO" align="left" width="100%" cellpadding="0" cellspacing="0" style="width: 100%">
					<tr>
		        	<th>Supplier&nbsp;Name</th>
		        	<th>Department</th>
		        	<th>Description</th>
              <th><font size="1">Murray<font></th>
		        	<th>Name</th>
		        	<th>DMM</th>
		        	<th>Cat#</th>
		        	<th>Price</th>
		        	</tr>
            <%
        	Set set = all.keySet();
        	Iterator iter = set.iterator();
        	while (iter.hasNext()){
        		String prodNum = (String)iter.next();
            	ArrayList<HashMap<String, String>> alM = salesListM.containsKey( prodNum )?(ArrayList)salesListM.get( prodNum ):null;
            	ArrayList<HashMap<String, String>> alL = null;//salesListL.containsKey( prodNum )?(ArrayList)salesListL.get( prodNum ):null;
            	ArrayList<HashMap<String, String>> alO = null;//salesListO.containsKey( prodNum )?(ArrayList)salesListO.get( prodNum ):null;
            	ArrayList<HashMap<String, String>> summ = alM!=null?alM:alL!=null?alL:alO!=null?alO:null;
        	for (int i = 0; i< summ.size(); i++ ) { 
        	    HashMap<String, String> temp = summ.get(i);
        		%><tr><%
		        		%><td><%=temp.get("supplierName") %></td> <%
		        		%><td><%=temp.get("dept") %></td> <%
		        		%><td><%=temp.get("desc") %></td> <%
		        		int mc = alM!=null?alM.get(i)!=null?Integer.parseInt(alM.get(i).get("prodCount")):0:0;
		        		%><td align="center"><%=mc%></td> <%
		        		%><td><%=temp.get("prodName") %></td> <%
		        		%><td><%=temp.get("sku") %></td> <%
		        		%><td><%=temp.get("cat") %></td> <%
		        		%><td><%=temp.get("price") %></td> <%
		        	
		        	%></tr><%
                }
                }//end of for loop
                }//end of if null
                
                else{%>
                <center><font size="2" color="red">No data found for date range.</font> </center>
                <%}
                }catch(Exception e){
                  e.printStackTrace();
                }
		if ( message != null ) {
		%>
		<br>
		<center><font color=red size=4><%=message%></font></center>
		<%
		}
		%>
</table>
<%@include file="bottomLayout.jsp"%>
