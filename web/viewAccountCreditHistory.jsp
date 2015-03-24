
<jsp:directive.page import="com.soward.object.CreditHistory"/>
<jsp:directive.page import="com.soward.util.AccountUtil"/>
<jsp:directive.page import="com.soward.object.Account"/>
<jsp:directive.page import="com.soward.enums.CreditType"/>
<jsp:directive.page import="com.soward.util.InvoiceUtil"/>
<jsp:directive.page import="com.soward.object.Invoice"/><%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.DBObj"%>

<%
  String message = request.getParameter( "message" );
  String editUserPid = request.getParameter( "pid" );
  String updateCredit = request.getParameter( "updateCredit" );
  AccountUtil acctUtil = new AccountUtil();
  if(editUserPid==null||editUserPid.length()<1){
  	response.sendRedirect("searchAccounts.jsp?message=An error occured, try again :).");
  }
  Account acct = acctUtil.getAccount( editUserPid );
  //update the credit/balance for this account
  if(updateCredit!=null){
  	
	String creditType = request.getParameter( "creditType" );
	String creditSum = request.getParameter( "creditSum" );
	String creditDesc = request.getParameter( "creditDesc" );
  	AccountUtil.updateCredit(editUserPid, creditType, creditSum, creditDesc);
  }
  ArrayList<Invoice> chargedInvList = InvoiceUtil.getChargedInvoices(acct.getAccountNum());
  ArrayList<CreditHistory> credList = AccountUtil.getAccountCreditHistory(acct.getAccountNum());
  %>
  <table width="754" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td>&nbsp;</td>
	<tr>
		<td align="" valign="top">
			<form action="viewAccountCreditHistory.jsp">
				<table id="viewAccounts" align="center" cellpadding="3" cellspacing="3" border="1">
					<tr><td>
						Credit Type:</td><td>
						<select name="creditType">
						<%
							for(CreditType lname : CreditType.values()){%>
					            <option name=<%=lname.name()%>" value="<%=lname.userFriendly()%>"><%=lname.userFriendly()%></option>
	        				<%}
						 %>
	     				</select>
					</td></tr>
					<tr><td>New Balance:</td><td><input type="text" name="creditSum" value="<%=acct.getAccountBalance()%>" ></td></tr>
					<tr><td>Description:</td><td><textarea name="creditDesc" value="" cols="25" rows="5"></textarea></td></tr>
					<input type="hidden" name="pid" value="<%=acct.getAccountNum()%>">
					<input type="hidden" name="updateCredit" value="updateCredit">
					<tr><td><input type="submit" vakue="Save"></td></tr>
				</table>
			</form>
		</td>
		</tr>
	<tr>
		<td align="" valign="top">
		<table class="sortable, common" id="viewAccounts" align="center" cellpadding="0" cellspacing="0">
			<tr>
        <th>#</th>
        <th>Type</th>
        <th>Balance</th>
        <th>Description</th>
        <th>Date</th>
        </tr>
        <% 
        int count = 1;
        for(CreditHistory credHist: credList){ %>
        	<tr><td>
        	<%=count %>
        	</td><td>
        	<%=credHist.getCreditType() %>
        	</td><td>
        	<%=credHist.getCreditDate() %>
        	</td><td>
        	<%=credHist.getCreditDesc() %>
        	</td></tr>
        <%
        	count++;
        } %>
  <%
if ( message != null ) {
%>
<td><%=message%></td>
<%
}
%>
	</tr>
</table>
<%@include file="bottomLayout.jsp"%>