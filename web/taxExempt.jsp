<%@include file="jspsetup.jsp"%>


<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>


<%
			
            String message = request.getParameter( "message" );
            String currCount = request.getParameter( "currCount" );
            String query = request.getParameter( "query" );
		    User user = new User();
			DBObj dbobj = new DBObj();
			
			int pc = 0;
			int picNext = 20;
			int picBack = 0;
			boolean goBack = false;
			boolean goNext = true;
            try{
			if(currCount!=null&&currCount.length()>0){
			  goBack = true;
			  pc = Integer.parseInt(currCount);
			  picNext += pc;
			  picBack = pc-20;
			  if(picBack<0){
			    goBack = false;
			  }
			}
            }catch(Exception e){
                //no currCount or invalid string.
            }
            ArrayList<DBObj> uu = new ArrayList<DBObj>();
            System.out.println("pc: "+pc);
            uu = dbobj.getTEAccounts(pc);
%>
<table width="754" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td>&nbsp;</td>
	<tr>
		<td align="" valign="top">
		<table class="sortable, common" id="viewAccounts" border="1" align="center"
			cellpadding="0" cellspacing="0" width="780">
			<tr>
        <th>&nbsp;#   &nbsp;</th>
        <th>&nbsp;accountNum   &nbsp;</th>
        <th>&nbsp;accountName   &nbsp;</th>
        <th>&nbsp;accountContact&nbsp;</th>
        <th>&nbsp;accountEmail  &nbsp;</th>
        <th>&nbsp;accountBalance&nbsp;</th>
        <th>&nbsp;Edit&nbsp;</th>
        <th>&nbsp;Delete&nbsp;</th>
			</tr>
			<%
			int count = 0+pc;
               for ( DBObj tt : uu ) {
                   ArrayList<String> temp =  tt.getRow();
                   count++;
			%>
			<tr>
				<td>&nbsp;<%=count%>&nbsp;</td>
				<td>&nbsp;<%=temp.get(0)%>&nbsp;</td>
				<td>&nbsp;<%=temp.get(1)%>&nbsp;</td>
				<td>&nbsp;<%=temp.get(2)%>&nbsp;</td>
				<td>&nbsp;<%=temp.get(3)%>&nbsp;</td>
				<td>&nbsp;<%=temp.get(4)%>&nbsp;</td>
				<td><a href="./editAccounts.jsp?pid=<%= temp.get(0)%>">view acct</a></td>
				<td><a href="./index.jsp?message=Delete account is not currently implemented.">del acct</a></td>
				<%
                   
                   }
				%>
			</tr>
		</table>
		<table border="0" align="center" cellpadding="0"
	cellspacing="0">
			 <%
   if(goBack&&goNext){ %>
  <tr> <td colspan=17 align=center>
    <a class="menutext" href="./taxExempt.jsp?<%="currCount="+picBack%>"><-- BACK</a>
    &nbsp;&nbsp;|&nbsp;&nbsp;
    <a class="menutext" href="./taxExempt.jsp?<%="currCount="+picNext%>">NEXT --></a>
  </td> </tr>
  <%}
  else if(goBack){ %>
  <tr> <td colspan=17 align=center>
    <a class="menutext" href="./taxExempt.jsp?<%="currCount="+picBack%>"><-- BACK</a>
  </td> </tr>
  <%}

  else if(goNext){ %>
  <tr> <td colspan=17 align=center>
    <a class="menutext" href="./taxExempt.jsp?<%="currCount="+picNext%>">NEXT --></a>
  </td> </tr>
  <%}%>
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
