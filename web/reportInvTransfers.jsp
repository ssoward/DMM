
<%@page import="com.soward.object.Supplier"%>
<%@page import="com.soward.util.SupplierUtil"%>
<%@page import="com.soward.util.ProductUtils"%>
<%@page import="com.soward.util.StockOrderReport"%>
<%@page import="com.soward.util.InvTransfersReport"%><jsp:directive.page import="com.soward.object.Product"/>
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
	String locationFrom = request.getParameter( "locationFrom" );
	String locationTo = request.getParameter( "locationTo" );
	String dateOne = request.getParameter("dateOne");
	String dateTwo = request.getParameter("dateTwo"); 
	String moveUser = request.getParameter("moveUser");
	String runReport = request.getParameter("runReport");
	ArrayList<User> userList = UserUtil.getAllUsers();
	User blank = new User();
	blank.setName("");
	userList.add(0, blank);
	if(dateTwo==null&&dateOne==null){
	    dateTwo=endDate;
	    dateOne=startDate;
	}
	List<Map<String,String>> transfersList = null;
	if(runReport!=null&&dateOne!=null){
		transfersList = InvTransfersReport.getInventoryTransferReport(dateOne, dateTwo, locationFrom, locationTo);
	}
		%>
		 
    <br/>
<h1>Invetory Transfers</h1>
<p class="text">This report provides a graphical representation of the various inventory transfers from one location to the next.</p>
<table align="center">
  <tr>
    <th>Date Range</th></tr><tr>

    <form name="myform" method="post" action="./reportInvTransfers.jsp" >
      <td>
        <table style="width:100%;border:1px solid #888888;" width="100%"> <tr>
            <tr><td>
                <table width="100%" border="0" cellpadding="2" cellspacing="2"> <tr>
                    <TD align="right">Start date: </td><td>
                      <A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateOne"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
                        NAME="<%="anchor"%>" ID="<%="anchor"%>">
                        <img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;</TD>
                    <TD align=left><input size=10 type="text" value="<%=dateOne%>" name="dateOne">
                  </td></tr>
                  <tr>

                    <TD align=right>  End date: </td><td width="10">
                      <A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateTwo"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
                        NAME="<%="anchor"%>" ID="<%="anchor"%>">
                        <img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;</TD>
                    <TD align=left>
                      <input size=10 type="text" value="<%=dateTwo%>" name="dateTwo">
                    </td>
                  </tr>
                  <tr><td align=right colspan=2>
                      Location From</td><td colspan=2>
                      <select name="locationFrom">
                        <option name="" value=""></option>
                        <option name="MURRAY" <%=locationFrom!=null&&locationFrom.equals("MURRAY")?"selected=selected":""%> value="MURRAY">Murray</option>
                        <option name="LEHI"   <%=locationFrom!=null&&locationFrom.equals("LEHI")?"selected=selected":""%>   value="LEHI">Lehi</option>
                        <option name="OREM"   <%=locationFrom!=null&&locationFrom.equals("OREM")?"selected=selected":""%>   value="OREM">DV</option>
                      </select>
                  </td></tr>
                  <tr><td align=right colspan=2>
                      Location To</td><td colspan=2>
                      <select name="locationTo">
                        <option name="" value=""></option>
                        <option name="MURRAY" <%=locationTo!=null&&locationTo.equals("MURRAY")?"selected=selected":""%> value="MURRAY">Murray</option>
                        <option name="LEHI"   <%=locationTo!=null&&locationTo.equals("LEHI")?"selected=selected":""%>   value="LEHI">Lehi</option>
                        <option name="OREM"   <%=locationTo!=null&&locationTo.equals("OREM")?"selected=selected":""%>   value="OREM">DV</option>
                      </select>
                  </td></tr>

                  
                  <tr><td colspan=4 align=center>
                      <input type="hidden" name="runReport" value="true">
                      <input type="button" class="btn" onclick="ValidateForm()" name="Enter" value="Enter" >
                  </td></tr>
                </table>
            </td></tr>
          </table>
          <br/>
          <% if(transfersList!=null){ %>
          <table style="width:100%;border:1px solid #888888;" width="100%" cellpadding="2" cellspacing="2">
            <tr><td colspan=4 align=center>
                Report generated for input:
            </td></tr>
            <tr><td>
                <table border="1" cellpadding="2" cellspacing="2">
                  <tr>
                    <th>Date</th>
                    <th>User</th>
                    <th>Qty</th>
                    <th>From</th>
                    <th>To</th>
                    <th>Prod#</th>
                  </tr>
                  <%
                  boolean ii = true;
                  for(Map map:transfersList){
                  String bgSet = (ii)?"bgcolor=\"#dddddd\"":"";
                  ii = !ii;
                  %>
                  <tr <%=bgSet%>>
                    <td><%=map.get("moveDate")%></td>
                    <td><%=map.get("moveUser")%></td>
                    <td><%=map.get("moveAmt")%></td>
                    <td><%=map.get("moveFrom")%></td>
                    <td><%=map.get("moveTo")%></td>
                    <td><%=map.get("prodNum")%></td>
                  </tr>
                  <%}%>
              </td></tr>
              <%} %>
            </table>

        </td></tr>



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

