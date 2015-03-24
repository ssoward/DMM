<%@include file="jspsetup.jsp"%>


<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>


<%
    String message = request.getParameter( "message" );
    String currCount = request.getParameter( "currCount" );
    String query = request.getParameter( "queryTxt" );
    String objType = request.getParameter( "object" );
    try{
	    if ( query == null ) {
	        query = (String) session.getAttribute( "query" );
	    } else {
	        session.setAttribute( "query", query );
	    }
    }catch(Exception e){}
    
    User user = new User();
    DBObj dbobj = new DBObj();

    int pc = 0;
    int picNext = 20;
    int picBack = 0;
    boolean goBack = false;
    boolean goNext = false;
    try {
        if ( currCount != null && currCount.length() > 0 ) {
            goBack = true;
            pc = Integer.parseInt( currCount );
            picNext += pc;
            picBack = pc - 20;
            if ( picBack < 0 ) {
                goBack = false;
            }
        }
    } catch ( Exception e ) {
        //no currCount or invalid string.
    }
    ArrayList<DBObj> uu = new ArrayList<DBObj>();
    //System.out.println("pc: "+pc);
    boolean str = false;
    try{
	    if(objType==null || objType.length()<1){
			response.sendRedirect("searchAccounts.jsp?message=An error occured, try again :).");
	    }
	    if(objType.equals("string")){
	    	str = true;
	    }
	    uu = dbobj.getAccountList( query, pc, str ); 
	    if(uu==null || uu.size()<1){
			response.sendRedirect("searchAccounts.jsp?message=No account found for '"+query+"' please consider refining your search criteria.");
	    }
	    if ( uu.size() > 18 ) {
	        goNext = true;
    }
    }catch(Exception e){
		response.sendRedirect("searchAccounts.jsp?message=An error occured, try again :).");
    }
%>
<table width="754" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td>&nbsp;</td>
	<tr>
		<td align="" valign="top">
		<table class="sortable, common" id="viewAccounts" align="center" cellpadding="0" cellspacing="0">
			<tr>
        <th>#</th>
        <th>Name</th>
        <th>Contact</th>
        <th>Email</th>
        <th>Balance</th>
        <th>Edit Account</th>
        <th>View Invoices</th>
        <!-- 
        <th>Credit Hist</th>
        -->
        <th>Delete</th>
			</tr>
			<%
			            int count = 0 + pc;
			            for ( DBObj tt : uu ) {
			                ArrayList<String> temp = tt.getRow();
			                count++;
			%>
			<tr>
				<td>&nbsp;<%=count%>&nbsp;</td>
				<td>&nbsp;<%=temp.get( 1 )%>&nbsp;</td>
				<td>&nbsp;<%=temp.get( 2 )%>&nbsp;</td>
				<td>&nbsp;<%=temp.get( 3 )%>&nbsp;</td>
				<td>&nbsp;<%=temp.get( 4 )%>&nbsp;</td>
				<td><a href="./editAccounts.jsp?pid=<%=temp.get( 0 )%>"><%=temp.get( 0 )%></a></td>
				<td><a href="./viewAccountInvoices.jsp?pid=<%=temp.get( 0 )%>">view&nbsp;invoices</a></td>
				<!-- 
				<td align="center"><a href="./viewAccountCreditHistory.jsp?pid=<%=temp.get( 0 )%>">hist</a></td>
				-->
				<td><a href="./queryAccounts.jsp?object=<%=objType %>&query=<%=query%>&message=Delete account is not currently implemented.">del&nbsp;acct</a></td>
				<%
				}
				%>
			</tr>
		</table>
		<table border="0" align="center" cellpadding="0"
	cellspacing="0">
			 <%
			 if ( goBack && goNext ) {
			 %>
  <tr> <td colspan=17 align=center>
    <a class="menutext" href="./queryAccounts.jsp?object=<%=objType %>&query=<%=query%>&<%="currCount=" + picBack%>"><-- BACK</a>
    &nbsp;&nbsp;|&nbsp;&nbsp;
    <a class="menutext" href="./queryAccounts.jsp?object=<%=objType %>&query=<%=query%>&<%="currCount=" + picNext%>">NEXT --></a>
  </td> </tr>
  <%
  } else if ( goBack ) {
  %>
  <tr> <td colspan=17 align=center>
    <a class="menutext" href="./queryAccounts.jsp?object=<%=objType %>&query=<%=query%>&<%="currCount=" + picBack%>"><-- BACK</a>
  </td> </tr>
  <%
              }

              else if ( goNext ) {
  %>
  <tr> <td colspan=17 align=center>
    <a class="menutext" href="./queryAccounts.jsp?object=<%=objType %>&query=<%=query%>&<%="currCount=" + picNext%>">NEXT --></a>
  </td> </tr>
  <%
  }
  %>
		</table>

		</td>
	</tr>
</table>
		<br/>
		<center>
		<%
		if ( message != null ) {
		%>
		<td><font color="red"><%=message%></font></td>
		<%
		}
		%>
<%@include file="bottomLayout.jsp"%>
