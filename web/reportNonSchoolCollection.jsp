<%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.TransUtil"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.util.CollectionUtil"%>
<%@page import="com.soward.object.CollectionRpt"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.soward.util.AccountUtil"%>
<%
			
            String message = request.getParameter( "message" );
		    User user = new User();			
			int pc = 0;
			int picNext = 20;
			int picBack = 0;
			CollectionUtil cu = new CollectionUtil();
            ArrayList<CollectionRpt> uu = cu.getNonSchoolCollectionReport();
%>

<h1>Non School Aging Report</h1>
<br />
<table class="sortable, common" id="viewAccounts" align="center"	cellpadding="0" cellspacing="0">
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
            double past30 = 0.0;
            double past60 = 0.0;
            double past90 = 0.0;
            double past   = 0.0;
            double pastSum   = 0.0;
            DecimalFormat df = new DecimalFormat (fmt);
			 boolean flipShade = true;
               for ( CollectionRpt tt : uu ) {
                    past30  +=tt.getColl_00_30Sum();
                    past60  +=tt.getColl_31_60Sum();
                    past90  +=tt.getColl_61_90Sum();
                    past    +=tt.getcoll_91_upSum();
                    pastSum +=tt.getCollSum();
                   Account acct = tt.getAcct();
                   count++;
			%>
     <% if(flipShade){
                    flipShade = false;%>
     <tr>
          <%}else{flipShade =true;%>
     <tr>
          <%}%>
          <td>&nbsp;<%=count%>&nbsp;</td>
          <td width="200">&nbsp;<%=AccountUtil.truncString(acct.getAccountName(), 30)%>&nbsp;</td>
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
     <tr>
          <td colspan=4>Total</td>
          <td>&nbsp;<%=df.format(past30)%></td>
          <td>&nbsp;<%=df.format(past60)%></td>
          <td>&nbsp;<%=df.format(past90)%></td>
          <td>&nbsp;<%=df.format(past)%></td>
          <td>&nbsp;<%=df.format(pastSum)%></td>
          <td>&nbsp;</td>
     </tr>
</table>
</td>
<%
		if ( message != null ) {
		%>
<td><p class="message"><%=message%></p></td>
<%
		}
		%>
</tr>
</table>
<%@include file="bottomLayout.jsp"%>
