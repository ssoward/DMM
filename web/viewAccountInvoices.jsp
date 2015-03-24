<%@include file="jspsetup.jsp"%>
  <%@page import="com.soward.object.InvBundle"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<SCRIPT LANGUAGE="JavaScript">
function checkAll()
{
  for (i = 0; i < document.myform.length; i++)
    document.myform.elements[i].checked = true;
}

function uncheckAll()
{
  for (i = 0; i < document.myform.length; i++)
    document.myform.elements[i].checked = false;
}
</script>
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
<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>


<%
TimeZone tz = TimeZone.getTimeZone("America/Salt_Lake");
Calendar c = Calendar.getInstance();

int year = c.get(Calendar.YEAR);
int mon  = c.get(Calendar.MONTH);
int day  = c.get(Calendar.DAY_OF_MONTH);
//System.out.println(year+" / "+(mon+1)+" / "+day);

String date = year+"-";
if(mon<10){date+="0"+(mon+1);}else{date+=(mon+1);}
if(day<10){date+="-0"+day;}else{date+="-"+day;}



String message = request.getParameter( "message" );
String pid = request.getParameter( "pid" );
if(pid==null||pid.length()<1){
	 response.sendRedirect("./index.jsp?message=There was an error on viewAccountInvoice page.");
}else{
DBObj dbobj = new DBObj();

String  invoiceNum                = request.getParameter("invoiceNum"                );
String  accountNum                = request.getParameter("accountNum"                );
String  invoiceDate               = request.getParameter("invoiceDate"               );
String  locationNum               = request.getParameter("locationNum"               );
String  userName                  = request.getParameter("userName"                  );
String  invoiceTotal              = request.getParameter("invoiceTotal"              );
String  invoiceTax                = request.getParameter("invoiceTax"                );
String  invoiceShipTotal          = request.getParameter("invoiceShipTotal"          );
String  invoicePaid               = request.getParameter("invoicePaid"               );
String  paymentMethod1            = request.getParameter("paymentMethod1"            );
String  paymentMethod2            = request.getParameter("paymentMethod2"            );
String  invoicePaid1              = request.getParameter("invoicePaid1"              );
String  invoicePaid2              = request.getParameter("invoicePaid2"              );
String  invoiceReceivedBy         = request.getParameter("invoiceReceivedBy"         );
String  invoiceContactNum         = request.getParameter("invoiceContactNum"         );
String  invoiceReferenceNum       = request.getParameter("invoiceReferenceNum"       );
String  invoiceChargeStatus       = request.getParameter("invoiceChargeStatus"       );
String  invoiceChargeDate         = request.getParameter("invoiceChargeDate"         );
String  invoiceChargePaymentMethod= request.getParameter("invoiceChargePaymentMethod");
String  invoiceDiscount           = request.getParameter("invoiceDiscount"           );
String  payStatus                 = request.getParameter("payStatus"           );
String  dateOne                   = request.getParameter("dateOne"           );
String  dateTwo                   = request.getParameter("dateTwo"           );
String  getInvoice                = request.getParameter("getInvoice"           );
InvBundle invBun = new InvBundle();
if(dateTwo==null&&dateOne==null){
    dateTwo=date;
    dateOne=date;
}
if(getInvoice==null){
  invoiceNum="";
  //dateOne="";
  //dateTwo="";
  userName="";
  accountNum="";
  invoiceDate="";
}
if(getInvoice!=null){
  
  ArrayList<String> columnList = new ArrayList<String>();

  if(invoiceNum                !=null){columnList.add( invoiceNum                );} 
  if(accountNum                !=null){columnList.add( accountNum                );}
  if(invoiceDate               !=null){columnList.add( invoiceDate               );}
  if(locationNum               !=null){columnList.add( locationNum               );}
  if(userName                  !=null){columnList.add( userName                 );}
  if(invoiceTotal              !=null){columnList.add( invoiceTotal              );}
  if(invoiceTax                !=null){columnList.add( invoiceTax                );}
  if(invoiceShipTotal          !=null){columnList.add( invoiceShipTotal          );}
  if(invoicePaid               !=null){columnList.add( invoicePaid               );}
  if(paymentMethod1            !=null){columnList.add( paymentMethod1            );}
  if(paymentMethod2            !=null){columnList.add( paymentMethod2            );}
  if(invoicePaid1              !=null){columnList.add( invoicePaid1              );}
  if(invoicePaid2              !=null){columnList.add( invoicePaid2              );}
  if(invoiceReceivedBy         !=null){columnList.add( invoiceReceivedBy         );}
  if(invoiceContactNum         !=null){columnList.add( invoiceContactNum         );}
  if(invoiceReferenceNum       !=null){columnList.add( invoiceReferenceNum       );}
  if(invoiceChargeStatus       !=null){columnList.add( invoiceChargeStatus       );}
  if(invoiceChargeDate         !=null){columnList.add( invoiceChargeDate         );}
  if(invoiceChargePaymentMethod!=null){columnList.add( invoiceChargePaymentMethod);}
  if(invoiceDiscount           !=null){columnList.add( invoiceDiscount           );}
//  if(payStatus                 !=null){columnList.add( payStatus                 );}
//  if(dateOne                   !=null){columnList.add( dateOne                   );}
//  if(dateTwo                   !=null){columnList.add( dateTwo                   );}
//  if(getInvoice                !=null){columnList.add( getInvoice                );}
  //DBObj dbobj = new DBObj();
  
  invBun = dbobj.getInvoices(columnList, pid, dateOne, dateTwo, payStatus);

}

//System.out.println("invoiceNum                : " + invoiceNum                );
//System.out.println("accountNum                : " + accountNum                );
//System.out.println("invoiceDate               : " + invoiceDate               );
//System.out.println("locationNum               : " + locationNum               );
//System.out.println("username2                  : " + username2                  );
//System.out.println("invoiceTotal              : " + invoiceTotal              );
//System.out.println("invoiceTax                : " + invoiceTax                );
//System.out.println("invoiceShipTotal          : " + invoiceShipTotal          );
//System.out.println("invoicePaid               : " + invoicePaid               );
//System.out.println("paymentMethod1            : " + paymentMethod1            );
//System.out.println("paymentMethod2            : " + paymentMethod2            );
//System.out.println("invoicePaid1              : " + invoicePaid1              );
//System.out.println("invoicePaid2              : " + invoicePaid2              );
//System.out.println("invoiceReceivedBy         : " + invoiceReceivedBy         );
//System.out.println("invoiceContactNum         : " + invoiceContactNum         );
//System.out.println("invoiceReferenceNum       : " + invoiceReferenceNum       );
//System.out.println("invoiceChargeStatus       : " + invoiceChargeStatus       );
//System.out.println("invoiceChargeDate         : " + invoiceChargeDate         );
//System.out.println("invoiceChargePaymentMethod: " + invoiceChargePaymentMethod);
//System.out.println("invoiceDiscount           : " + invoiceDiscount           );




            
            ArrayList<DBObj> acct = dbobj.getAccount( pid );
            ArrayList<String> colValue = acct.get(0).getRow();
            
		%>
		<form name="myform" method="post" action="./viewAccountInvoices.jsp">
		<table align=center> 
		  <tr><td center colspan=8>
      	  View invoices for account 
		  <font color=blue><%=colValue.get(1)%></font> account number 
		  <font color=blue><%=colValue.get(0)%></font>.
		  <br><br>
		  </td>
		  </tr>
		  <tr>
      <%if(invoiceNum!=null){%>
      <td><input type="checkbox" checked=checked name="invoiceNum"                 value="invoiceNum">invoiceNum                
      <%}else{%>
       <td><input type="checkbox" name="invoiceNum"                 value="invoiceNum">invoiceNum                
      <%}%>
               
      <%if(accountNum!=null){%>
        </td><td><input type="checkbox"  checked=checked name="accountNum"                 value="accountNum">accountNum                
      <%}else{%>
        </td><td><input type="checkbox" name="accountNum"                 value="accountNum">accountNum                
      <%}%>
      <%if(invoiceDate!=null){%>
        </td><td><input type="checkbox"  checked=checked name="invoiceDate"                value="invoiceDate">invoiceDate               
      <%}else{%>
        </td><td><input type="checkbox" name="invoiceDate"                value="invoiceDate">invoiceDate               
      <%}%>
      <%if(locationNum!=null){%>
        </td><td><input type="checkbox"  checked=checked  name="locationNum"                value="locationNum">locationNum               
      <%}else{%>
        </td><td><input type="checkbox" name="locationNum"                value="locationNum">locationNum               
      <%}%>
      <%if(userName!=null){%>
        </td></tr><tr><td> <input type="checkbox"  checked=checked  name="userName"                   value="userName">username                  
      <%}else{%>
        </td></tr><tr><td> <input type="checkbox" name="userName"                   value="userName">username                  
      <%}%>
      <%if(invoiceTotal!=null){%>
        </td><td><input type="checkbox"  checked=checked  name="invoiceTotal"               value="invoiceTotal">invoiceTotal              
      <%}else{%>
        </td><td><input type="checkbox" name="invoiceTotal"               value="invoiceTotal">invoiceTotal              
      <%}%>
      <%if(invoiceTax!=null){%>
        </td><td><input type="checkbox"  checked=checked  name="invoiceTax"                 value="invoiceTax">invoiceTax                
      <%}else{%>
        </td><td><input type="checkbox" name="invoiceTax"                 value="invoiceTax">invoiceTax                
      <%}%>
      <%if(invoiceShipTotal!=null){%>
        </td><td><input type="checkbox"  checked=checked  name="invoiceShipTotal"           value="invoiceShipTotal">invoiceShipTotal          
      <%}else{%>
        </td><td><input type="checkbox" name="invoiceShipTotal"           value="invoiceShipTotal">invoiceShipTotal          
      <%}%>
      <%if(invoicePaid!=null){%>
        </td></tr><tr><td> <input type="checkbox"  checked=checked  name="invoicePaid"                value="invoicePaid">invoicePaid  	           
      <%}else{%>
        </td></tr><tr><td> <input type="checkbox" name="invoicePaid"                value="invoicePaid">invoicePaid  	           
      <%}%>
      <%if(paymentMethod1!=null){%>
        </td><td><input type="checkbox"  checked=checked  name="paymentMethod1"             value="paymentMethod1">paymentMethod1            
      <%}else{%>
        </td><td><input type="checkbox" name="paymentMethod1"             value="paymentMethod1">paymentMethod1            
      <%}%>
      <%if(paymentMethod2!=null){%>
        </td><td><input type="checkbox"  checked=checked  name="paymentMethod2"             value="paymentMethod2">paymentMethod2            
      <%}else{%>
        </td><td><input type="checkbox" name="paymentMethod2"             value="paymentMethod2">paymentMethod2            
      <%}%>
      <%if(invoicePaid1!=null){%>
        </td><td><input type="checkbox"  checked=checked  name="invoicePaid1"               value="invoicePaid1">invoicePaid1              
      <%}else{%>
        </td><td><input type="checkbox" name="invoicePaid1"               value="invoicePaid1">invoicePaid1              
      <%}%>
      <%if(invoicePaid2!=null){%>
        </td></tr><tr><td> <input type="checkbox"  checked=checked  name="invoicePaid2"               value="invoicePaid2">invoicePaid2              
      <%}else{%>
        </td></tr><tr><td> <input type="checkbox" name="invoicePaid2"               value="invoicePaid2">invoicePaid2              
      <%}%>
      <%if(invoiceReceivedBy!=null){%>
        </td><td><input type="checkbox"  checked=checked  name="invoiceReceivedBy"          value="invoiceReceivedBy">invoiceReceivedBy  	     
      <%}else{%>
        </td><td><input type="checkbox" name="invoiceReceivedBy"          value="invoiceReceivedBy">invoiceReceivedBy  	     
      <%}%>
      <%if(invoiceContactNum!=null){%>
        </td><td><input type="checkbox"  checked=checked  name="invoiceContactNum"          value="invoiceContactNum">invoiceContactNum         
      <%}else{%>
        </td><td><input type="checkbox" name="invoiceContactNum"          value="invoiceContactNum">invoiceContactNum         
      <%}%>
      <%if(invoiceReferenceNum!=null){%>
        </td><td><input type="checkbox"  checked=checked  name="invoiceReferenceNum"        value="invoiceReferenceNum">invoiceReferenceNum       
      <%}else{%>
        </td><td><input type="checkbox" name="invoiceReferenceNum"        value="invoiceReferenceNum">invoiceReferenceNum       
      <%}%>
      <%if(invoiceChargeStatus!=null){%>
        </td></tr><tr><td> <input type="checkbox"  checked=checked  name="invoiceChargeStatus"        value="invoiceChargeStatus">invoiceChargeStatus       
      <%}else{%>
        </td></tr><tr><td> <input type="checkbox" name="invoiceChargeStatus"        value="invoiceChargeStatus">invoiceChargeStatus       
      <%}%>
      <%if(invoiceChargeDate!=null){%>
        </td><td><input type="checkbox"  checked=checked  name="invoiceChargeDate"          value="invoiceChargeDate">invoiceChargeDate         
      <%}else{%>
        </td><td><input type="checkbox" name="invoiceChargeDate"          value="invoiceChargeDate">invoiceChargeDate         
      <%}%>
      <%if(invoiceChargePaymentMethod!=null){%>
        </td><td><input type="checkbox"  checked=checked  name="invoiceChargePaymentMethod" value="invoiceChargePaymentMethod">invoiceChargePaymentMethod
      <%}else{%>
        </td><td><input type="checkbox" name="invoiceChargePaymentMethod" value="invoiceChargePaymentMethod">invoiceChargePaymentMethod
      <%}%>
      <%if(invoiceDiscount!=null){%>
        </td><td><input type="checkbox"  checked=checked  name="invoiceDiscount"            value="invoiceDiscount">invoiceDiscount 
      <%}else{%>
        </td><td><input type="checkbox" name="invoiceDiscount"            value="invoiceDiscount">invoiceDiscount 
      <%}%>



        </td></tr>
         <tr><td colspan=8 align=right>
		 <input type="button" class="btn" name="CheckAll" value="Check All" onclick="checkAll()">
		 <input type="button" class="btn" name="UnCheckAll" value="Uncheck All" onClick="uncheckAll()">
		 </td></tr>
		 <tr><td colspan=8><table>
				<tr>
            	<TD align=left>Get invoices from (exclusive): </td><td>
            	<A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateOne"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>">
            	<img src="images/calendar.jpg" width="25" border="0"/></A>&nbsp;</TD>
      			<TD align=left><input size=10 type="text" value="<%=dateOne%>" name="dateOne">
      			</td>
      			</tr><tr>
            	<TD align=left>  to end date (exclusive): </td><td>
            	<A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateTwo"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>">
            	<img src="images/calendar.jpg" width="25" border="0"/></A>&nbsp;</TD>
      			<TD align=left>
      			<input size=10 type="text" value="<%=dateTwo%>" name="dateTwo">
      			</td><td>
      			Payment Status: &nbsp;	  
      			<select name="payStatus">
      			<option value="">Any</option>
      			<option value="paid">Paid</option>
      			<option value="charged">Charged</option>
      			</select>
      			</td></tr>
      			</table></td>      			</tr>		  
		    <tr><td colspan=8 align=right>
		    <input type="hidden" name="pid" value="<%=pid %>">
		    <input type="hidden" name="getInvoice" value="true">
		 	<input type="button" class="btn" onclick="ValidateForm()" name="Enter" value="Enter" >
			 </td></tr>
        <tr><td colspan=8> 
		  <hr>
		</td>
		</tr>
		</table>
		</form>
		<%if( getInvoice != null){
      if(invBun!=null&&invBun.getInvCollection()!=null&&!invBun.getInvCollection().isEmpty()){
		    getInvoice = null;
			ArrayList<String>colnms = invBun.getColNames();
		    %>
		
		<table class="sortable" id="viewAccounts" border="1" align="center"
			cellpadding="0" cellspacing="0">
			<tr>
        	<th>&nbsp;#&nbsp;</th>
			<%for(String tempCol: colnms){ %>
        	<th>&nbsp;<%=" "+tempCol%>&nbsp;</th>
            <%}
			int count = 0;
			ArrayList<Invoice> tempInv = invBun.getInvCollection();
        	for(Invoice tInv: tempInv){
        	    count++;
        %>
        <tr>
        <td><%=" "+count %></td>
        <%if(tInv.getInvoiceNum                ().length()>0){ %><td><%=" "+tInv.getInvoiceNum                () %></td><%} %>
        <%if(tInv.getAccountNum                ().length()>0){ %><td><%=" "+tInv.getAccountNum                () %></td><%} %>
        <%if(tInv.getInvoiceDate               ().length()>0){ %><td><%=" "+tInv.getInvoiceDate               () %></td><%} %>
        <%if(tInv.getLocationNum               ().length()>0){ %><td><%=" "+tInv.getLocationNum               () %></td><%} %>
        <%if(tInv.getUsername2                 ().length()>0){ %><td><%=" "+tInv.getUsername2                 () %></td><%} %>
        <%if(tInv.getInvoiceTotal              ().length()>0){ %><td><%=" "+InvoiceUtil.trunDouble(tInv.getInvoiceTotal              ()) %></td><%} %>
        <%if(tInv.getInvoiceTax                ().length()>0){ %><td><%=" "+InvoiceUtil.trunDouble(tInv.getInvoiceTax                ()) %></td><%} %>
        <%if(tInv.getInvoiceShipTotal          ().length()>0){ %><td><%=" "+InvoiceUtil.trunDouble(tInv.getInvoiceShipTotal          ()) %></td><%} %>
        <%if(tInv.getInvoicePaid               ().length()>0){ %><td><%=" "+InvoiceUtil.trunDouble(tInv.getInvoicePaid               ()) %></td><%} %>
        <%if(tInv.getPaymentMethod1            ().length()>0){ %><td><%=" "+tInv.getPaymentMethod1            () %></td><%} %>
        <%if(tInv.getPaymentMethod2            ().length()>0){ %><td><%=" "+tInv.getPaymentMethod2            () %></td><%} %>
        <%if(tInv.getInvoicePaid1              ().length()>0){ %><td><%=" "+InvoiceUtil.trunDouble(tInv.getInvoicePaid1              ()) %></td><%} %>
        <%if(tInv.getInvoicePaid2              ().length()>0){ %><td><%=" "+InvoiceUtil.trunDouble(tInv.getInvoicePaid2              ()) %></td><%} %>
        <%if(tInv.getInvoiceReceivedBy         ().length()>0){ %><td><%=" "+tInv.getInvoiceReceivedBy         () %></td><%} %>
        <%if(tInv.getInvoiceContactNum         ().length()>0){ %><td><%=" "+tInv.getInvoiceContactNum         () %></td><%} %>
        <%if(tInv.getInvoiceReferenceNum       ().length()>0){ %><td><%=" "+tInv.getInvoiceReferenceNum       () %></td><%} %>
        <%if(tInv.getInvoiceChargeStatus       ().length()>0){ %><td><%=" "+tInv.getInvoiceChargeStatus       () %></td><%} %>
        <%if(tInv.getInvoiceChargeDate         ().length()>0){ %><td><%=" "+tInv.getInvoiceChargeDate         () %></td><%} %>
        <%if(tInv.getInvoiceChargePaymentMethod().length()>0){ %><td><%=" "+tInv.getInvoiceChargePaymentMethod() %></td><%} %>
        <%if(tInv.getInvoiceDiscount           ().length()>0){ %><td><%=" "+InvoiceUtil.trunDouble(tInv.getInvoiceDiscount           ()) %></td><%} %>
        </tr>
        
        
        
        
        <%}%>
        <tr><td>Total</td>
        <%
        HashMap<String, String> amtList = invBun.getColTot();
        	int i = 0;
	        for(String tempAmt: colnms){ 
        	      	
        	    //System.out.println(tempAmt);
        	%>
        	<%      if(tempAmt.equalsIgnoreCase("invoiceTotal")){     %><td><%=InvoiceUtil.trunDouble((String)amtList.get(invoiceTotal))%><%i++;%></td>
        	<%}else if(tempAmt.equalsIgnoreCase("invoiceTax")){       %><td><%=InvoiceUtil.trunDouble((String)amtList.get(invoiceTax))%><%i++;%></td>
        	<%}else if(tempAmt.equalsIgnoreCase("invoiceShipTotal")){ %><td><%=InvoiceUtil.trunDouble((String)amtList.get(invoiceShipTotal))%><%i++;%></td>
        	<%}else if(tempAmt.equalsIgnoreCase("invoicePaid")){      %><td><%=InvoiceUtil.trunDouble((String)amtList.get(invoicePaid))%><%i++;%></td>
        	<%}else if(tempAmt.equalsIgnoreCase("invoicePaid1")){     %><td><%=InvoiceUtil.trunDouble((String)amtList.get(invoicePaid1))%><%i++;%></td>
        	<%}else if(tempAmt.equalsIgnoreCase("invoicePaid2")){     %><td><%=InvoiceUtil.trunDouble((String)amtList.get(invoicePaid2))%><%i++;%></td>
        	<%}else if(tempAmt.equalsIgnoreCase("invoiceDiscount")){  %><td><%=InvoiceUtil.trunDouble((String)amtList.get(invoiceDiscount))%><%i++;%></td>
        	<%}else{%><td>&nbsp;</td><%}%>
            
            <%}%>
        <%}else{ %>
        		<table align=center> 
		  <tr><td center colspan=8>
		  Search returned no results. Please refine your search parameters and try again. Be sure that your dates correspond to realistic days (ie. there is no 31st day in April).</td>
		  </tr></table>
        <%} %>
        
        <%}//end of if getInvoice!=null %>
<%
		if ( message != null ) {
		%>
		<tr>
		<td><%=message%></td>
		</tr>
		<%
		}}
		%>
</table>
<%@include file="bottomLayout.jsp"%>
