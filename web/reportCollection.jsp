
<%@include file="jspsetup.jsp"%>


<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.TransUtil"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.util.CollectionUtil"%>
<%@page import="com.soward.object.CollectionRpt"%>
<%@page import="java.text.DecimalFormat"%>


<%
			
            String message = request.getParameter( "message" );
		    User user = new User();
			
			int pc = 0;
			int picNext = 20;
			int picBack = 0;
            ArrayList<CollectionRpt> uu = CollectionUtil.getSchoolCollectionReport();
%>

<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td>&nbsp;<br><br>  <center><font size=4>Collection Report</font></center></br></br></td>
	<tr>
		<td align="" valign="top">
		<table class="sortable, common" id="viewAccounts" align="center" cellpadding="0" cellspacing="0" width="780">
			<tr>
        <th>&nbsp;#   &nbsp;</th>
        <th width="200">&nbsp;Name   &nbsp;</th>
        <th>&nbsp;Account   &nbsp;</th>
        <th>&nbsp;Phone&nbsp;</th>
        <th>&nbsp;Current&nbsp;(0-30)  &nbsp;</th>
        <th>&nbsp;Past&nbsp;30&nbsp;(31-60)  &nbsp;</th>
        <th>&nbsp;Past&nbsp;60&nbsp;(61-90)  &nbsp;</th>
        <th>&nbsp;Past&nbsp;90&nbsp;(91-*)  &nbsp;</th>
        <th>&nbsp;Total  &nbsp;</th>
        <th>&nbsp;View Account  &nbsp;</th>
			</tr>
			<%
			int count = 0;
            String fmt = "0.00";
            DecimalFormat df = new DecimalFormat (fmt);
			 boolean flipShade = true;
               for ( CollectionRpt tt : uu ) {
                   Account acct = tt.getAcct();
                   count++;
			%> 	 
			 <% if(flipShade){
                    flipShade = false;%>
                <tr bgcolor="#eeeeee">
                <%}else{flipShade =true;%><tr><%}%>
				<td>&nbsp;<%=count%>&nbsp;</td>
				<td width="200">&nbsp;<%=acct.getAccountName()%>&nbsp;</td>
				<td>&nbsp;<%=acct.getAccountNum()%>&nbsp;</td>
				<td>&nbsp;<%=acct.getAccountPhone1()%>&nbsp;</td>
				<td>&nbsp;<%=df.format(tt.getColl_00_30Sum())%>&nbsp;</td>
				<td>&nbsp;<%=df.format(tt.getColl_31_60Sum())%>&nbsp;</td>
				<td>&nbsp;<%=df.format(tt.getColl_61_90Sum())%>&nbsp;</td>
				<td>&nbsp;<%=df.format(tt.getcoll_91_upSum())%>&nbsp;</td>
				<td>&nbsp;<%=df.format(tt.getCollSum())%>&nbsp;</td>
				<td><a href="./editAccounts.jsp?pid=<%= acct.getAccountNum()%>">view acct</a></td>
				<%
                   
                   }
				%>
			</tr>
		</table>

		</td>
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
