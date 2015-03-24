<%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.TransUtil"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.enums.LocationsDBName"%>
<%
			
            String message = request.getParameter( "message" );
		    User user = new User();
		    String locationName = request.getParameter( "locationName" );
			int pc = 0;
			int picNext = 20;
			int picBack = 0;
            ArrayList<Invoice> uu = null;//new ArrayList<Invoice>();
            if(!StringUtils.isBlank(locationName)){
                uu = TransUtil.getHBReport(locationName);
            }
%>




<%@page import="org.apache.commons.lang.StringUtils"%><h1>Current HoldBin Orders</h1>
<br />
<form action="./reportHoldBin.jsp" method="post">
  <center>
<table cellspacing="3" cellpadding="3" border="0">
<tr>
   <td  align="right">
     Store:
     </td><td align="left">
     <select name="locationName" value="test">
       <%
       if ( locationName != null && locationName.length() > 0 ) {
       LocationsDBName lname = LocationsDBName.valueOf( locationName );
       %>
       <option value="<%=lname.name()%>"><%=lname.displayName()%></option>
       <%
       }
       for ( LocationsDBName lname : LocationsDBName.values() ) {
       %>
       <option value="<%=lname.name()%>"><%=lname.displayName()%></option>
       <%
       }
       %>
     </select>
 </td></tr>
 <tr>
 	<td colspan="2" align="right">
 		<input type="submit" value="Enter" />
 	</td>
</tr>
</table>
</form>
<% if(uu!=null){%>
<table class="sortable, common" id="viewAccounts" align="center"	cellpadding="0" cellspacing="0">
     <tr>
          <th>&nbsp;#   &nbsp;</th>
          <th>&nbsp;Date   &nbsp;</th>
          <th>&nbsp;Account Name   &nbsp;</th>
          <th>&nbsp;Account Phone&nbsp;</th>
          <th>&nbsp;Total  &nbsp;</th>
          <th>&nbsp;View Account  &nbsp;</th>
          <th>&nbsp;View Invoice&nbsp;</th>
     </tr>
     <%
			int count = 0;
			double totalSum = 0.0;
			 boolean flipShade = true;
               for ( Invoice tt : uu ) {
                   totalSum = InvoiceUtil.getDoubleSum(totalSum, tt.getInvoiceTotal());
                   Account acct = tt.getAccount();
                   count++;
			%>
     <% if(flipShade){
                    flipShade = false;%>
     <tr>
          <%}else{flipShade =true;%>
     <tr>
          <%}%>
          <td>&nbsp;<%=count%>&nbsp;</td>
          <td>&nbsp;<%=tt.getInvoiceDate()%>&nbsp;</td>
          <td>&nbsp;<%=acct.getAccountName()%>&nbsp;</td>
          <td>&nbsp;<%=acct.getAccountPhone1()%>&nbsp;</td>
          <td>&nbsp;<%=InvoiceUtil.trunDouble(tt.getInvoiceTotal())%>&nbsp;</td>
          <td><a href="./editAccounts.jsp?pid=<%= tt.getAccountNum()%>">view acct</a></td>
          <td><a href="./viewInvoice.jsp?pid=<%= tt.getInvoiceNum()%>">view inv</a></td>
          <%                   
                   }
				%>
     </tr>
     <tr>
          <td colspan=4>Total</td>
          <td><%=totalSum %></td>
          <td></td>
          <td></td>
     </tr>
</table>
<%}%>
</td>
<%
		if ( message != null ) {
		%>
<td><p class="text"><%=message%></p></td>
<%
		}
		%>
</tr>
</table>
<%@include file="bottomLayout.jsp"%>
