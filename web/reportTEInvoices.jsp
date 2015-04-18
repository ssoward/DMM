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
InvoiceUtil invUtil = new InvoiceUtil();
List<Invoice> invList = null;
TimeZone tz = TimeZone.getTimeZone("America/Salt_Lake");
Calendar c = Calendar.getInstance();

int year = c.get(Calendar.YEAR);
int mon  = c.get(Calendar.MONTH);
int day  = c.get(Calendar.DAY_OF_MONTH);
//System.out.println(year+" / "+(mon+1)+" / "+day);

String date = year+"-";
if(mon<10){date+="0"+(mon+1);}else{date+=(mon+1);}
if(day<10){date+="-0"+day;}else{date+="-"+day;}

//message = (String)session.getAttribute("message");
String message = request.getParameter( "message" );
String dateOne = request.getParameter("dateOne");
String singleDate = request.getParameter("singleDate");
String dateTwo = request.getParameter("dateTwo"); 
if(singleDate==null){
    singleDate=date;
}
else{
	invList = invUtil.getTEInvoiceForSingleDate(singleDate);
}
if(dateTwo==null&&dateOne==null){
    dateTwo=date;
    dateOne=date;
}
else{
	invList = invUtil.getTEInvoiceForRangeDate(dateOne, dateTwo);
}
		%>

<h1>Tax Exempt Invoice Report</h1>
<p align="center">Get totals for all tax exempt invoice for a given date.</p>
<br />
<p align="center">Click on the calendar icons or enter dates to specify a date range.<br />
     If you like to get data for just one day use single day calendar.</p>
<table align="center">
     <form name="singleForm" method="post" action="./reportTEInvoices.jsp" >
          <tr>
               <th>Single Day</th>
               <th>Date Range</th>
          </tr>
          <tr>
               <td><table border=0>
                         <tr>
                              <TD align=left>Get data from: </td>
                              <td><A HREF="#" onClick="cal.select(document.forms['<%="singleForm"%>'].<%="singleDate"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>"> <img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;</TD>
                              <TD align=left><input size=10 type="text" value="<%=singleDate%>" name="singleDate">
                         </tr>
                         <tr>
                              <td colspan=4 align=right><input type="button" class="btn" onclick="ValidateFormSingle()" name="Enter" value="Enter" >
                              </td>
                         </tr>
                    </table>
     </form>
     <form name="myform" method="post" action="./reportTEInvoices.jsp" >
     
     <td><table border=0>
               <tr>
                    <TD align=left>Get data from: </td>
                    <td><A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateOne"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>"> <img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;</TD>
                    <TD align=left><input size=10 type="text" value="<%=dateOne%>" name="dateOne">
               </tr>
               <tr>
                    <TD align=left> to end date: </td>
                    <td><A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateTwo"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>"> <img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;</TD>
                    <TD align=left><input size=10 type="text" value="<%=dateTwo%>" name="dateTwo">
                    </td>
               </tr>
               <tr>
                    <td colspan=4 align=right><input type="button" class="btn" onclick="ValidateForm()" name="Enter" value="Enter" >
                    </td>
               </tr>
          </table></td>
     </tr>
     
     <tr>
          <td colspan=8><hr>
          </td>
     </tr>
</table>
</form>
<%if(invList!=null){ 
		    double teTrans = 0.0;
		    double totSpent = 0.0;
		    double totDisct = 0.0;
		    double totTax = 0.0;
		%>
<table class="common" id="viewTEInvoices" align="center" cellpadding="0" cellspacing="0" width="780">
     <tr>
          <th>&nbsp;#&nbsp;</th>
          <th>&nbsp;Invoice Date&nbsp;</th>
          <th>&nbsp;Invoice&nbsp;</th>
          <th>&nbsp;Account&nbsp;</th>
          <th>&nbsp;Clerk &nbsp;</th>
          <th>&nbsp;Invoice Total&nbsp;</th>
          <th>&nbsp;TE transaction Total&nbsp;</th>
          <th>&nbsp;Invoice Tax&nbsp;</th>
          <th>&nbsp;Invoice Discount &nbsp;</th>
     </tr>
     <% 
        	int count = 0;
        	TransUtil tu = new TransUtil();
          boolean flipShade = false;
        	for ( Invoice temp : invList ) {
        		totSpent = InvoiceUtil.getDoubleSum(totSpent, temp.getInvoiceTotal());
        		totTax = InvoiceUtil.getDoubleSum(totTax, temp.getInvoiceTax());
                teTrans += tu.getTETransTotal(temp.getTransList());
        		totDisct = InvoiceUtil.getDoubleSum(totDisct, temp.getInvoiceDiscount());
                //get double value after checking for null if null return 0.0;
                count++;
                %>
     <% if(flipShade){%>
     <tr>
          <%}else{%>
     <tr>
          <%}%>
          <td><%= count%></td>
          <td><%= temp.getInvoiceDate()%></td>
          <td><a href="./viewInvoice.jsp?pid=<%= temp.getInvoiceNum()%>"><%= temp.getInvoiceNum()%></a></td>
          <td><a href="./editAccounts.jsp?pid=<%= temp.getAccountNum()%>"><%= temp.getAccountNum()%></a></td>
          <td width="780"><%= temp.getUsername2()%></td>
          <td><%= temp.getInvoiceTotal()%></td>
          <td><%= InvoiceUtil.trunDouble(tu.getTETransTotal(temp.getTransList())+"")%></td>
          <td><%= temp.getInvoiceTax()%></td>
          <td><%= temp.getInvoiceDiscount()%></td>
     </tr>
     <%
                flipShade = !flipShade;
                }//end of for loop %>
     <%
                //get totals of all accounts from individual sums.
                    %>
     <tr>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td width="780">&nbsp;</td>
          <td><%=InvoiceUtil.trunDouble(totSpent+"") %></td>
          <td><%=InvoiceUtil.trunDouble(teTrans+"") %></td>
          <td><%=InvoiceUtil.trunDouble(totTax+"") %></td>
          <td><%=InvoiceUtil.trunDouble(totDisct+"") %></td>
     </tr>
     <%
	                //end of if(acc!=null)
                    }else{%>
     <center>
          Select date range to generate tax exempt account data.
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
