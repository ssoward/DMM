
<%@page import="com.soward.util.AccountTypesUtil"%>
<%@page import="com.soward.object.AccountType"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.soward.object.CollectionRpt"%>
<%@page import="com.soward.util.CollectionUtil"%>
<%@page import="com.soward.util.AccountUtil"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.soward.object.Account"%><jsp:directive.page import="com.soward.util.BugsUtil" />
<jsp:directive.page import="com.soward.object.Bugs" />
<jsp:directive.page import="com.soward.object.BugThreads" />
<jsp:directive.page import="com.soward.util.BugThreadUtil" />
<%@include file="jspsetup.jsp"%>
<script type="text/javascript" src="js/comboBox.js"></script>
<script type="text/javascript" src="js/ajaxThread.js"></script>

<%
	String message = request.getParameter( "message" );
	String agingType = request.getParameter( "agingType" );
	String accType = request.getParameter( "accType" );
	List<AccountType> accTList = AccountTypesUtil.getAccountTypes();
	int pc = 0;
	int picNext = 20;
	int picBack = 0;
	CollectionUtil cu = new CollectionUtil();
	ArrayList<CollectionRpt> uu = null;
	if(!StringUtils.isBlank(agingType)){
	    uu = cu.getCollectionReport(accType, agingType);
	}

%>

<%if ( message != null ) {%>
<p class="message"><%=message%></p>
<%}%>
<br />

<center>
  <h1>Aging Reports</h1>
</center>
<p align="center">Select account type and aging type to run reports.</p>
<br />

<table cellpadding="0" cellspacing="0" border="1" align="center">
  <tr>
    <td align="right">
      <FORM name="agingReport" ACTION="./reportAging.jsp" method="post">
        <table border="0" cellspacing="3" cellpadding="3">
          <tr>
            <td align="right" colspan="1">Account Type:</td>
            <td align="left" colspan="1">
              <select name="accType">
                <option value="ALL">All</option>
                <%
                for ( AccountType accT : accTList ) {
                  String selected= "";
                  if(!StringUtils.isBlank(accType)){
                    if(accT.getTypeCode().equals(accType)){
                      selected = "SELECTED=\"SELECTED\"";
                    }
                  }
                %>
                <option value="<%=accT.getTypeCode()%>" <%=selected%>><%=accT.getTypeDescription()%></option>
                <%
                }
                %>
              </select>
            </td>
          </tr>
          <tr>
            <td align="right">Aging Type:</td>
            <%
            String nsls = (agingType!=null&&!agingType.equals("school"))?"SELECTED=\"SELECTED\"":"";
            String sls = (agingType!=null&&agingType.equals("school"))?"SELECTED=\"SELECTED\"":"";
            %>
            <td><select name="agingType">
                <option value="nonSch" <%=nsls%> >Non School</option>
                <option value="school" <%=sls%> >School</option>
              </select>
            </td>
          </tr>
          <tr>
            <td colspan="2" align="right">
              <input type="submit" value="Run Report"/>
            </td>
          </tr>
        </table>

      </FORM>

    </td>
  </tr>
</table>

<%if(uu!=null){ %>
<h1>Aging Report</h1>
		<table class="sortable, simple" id="viewAccounts" border="1" width="780" align="center" cellpadding="0" cellspacing="0">
			<tr>
        <th>#   </th>
        <th>Name   </th>
        <th>Account   </th>
        <th>Phone</th>
        <th>Current(0-30)  </th>
        <th>Past30(31-60)  </th>
        <th>Past60(61-90)  </th>
        <th>Past90(91-*)  </th>
        <th>Total  </th>
        <th>View Account  </th>
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
                <%}else{flipShade =true;%><tr><%}%>
				<td>&nbsp;<%=count%>&nbsp;</td>
				<td>&nbsp;<%=AccountUtil.truncString(acct.getAccountName(), 30)%>&nbsp;</td>
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
				</tr><tr>
				<td colspan="4">Total</td> 

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
		<td><%=message%></td>
		<%
		}
		%>
	</tr>
</table>
<%} %>

<%@include file="bottomLayout.jsp"%>
