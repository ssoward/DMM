<%@include file="jspsetup.jsp"%>


<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>


<%			try{
			String adminId = (String) session.getAttribute("username");
			if(adminId!=null&&!adminId.equals("scott")){
				response.sendRedirect("index.jsp?message=Insufficient permission.");
			}
			}catch(Exception e){
	
			}
			DBObj dbobj = new DBObj();
            String message = request.getParameter( "message" );
            String query = request.getParameter( "query" );
            String table = request.getParameter( "table" );
            String queryType = request.getParameter( "queryType" );
            ArrayList<DBObj> uu = new ArrayList<DBObj>();
            ArrayList<String> list = new ArrayList<String>();
            ArrayList<String> tlist = new ArrayList<String>();
            tlist = dbobj.getTables();
            if ( query != null&&query.length()>6 ) {
                //System.out.println("query not null");
                uu = dbobj.runQuery( query, queryType );
            }
            else{
            	query = "select * from Invoices where invoiceDate >'2007-07-07' and invoiceTax=0.0";
            }
            if ( table != null&&table.length()>6 ) {
                //System.out.println("query not null");
                list = dbobj.getColumns(table);
                tlist = dbobj.getTablesWithFirst(table);
            }
%>
<table width="754" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td>&nbsp;</td>
	<tr>
	 	<td align="" valign="top">
	 	<table>
	 	<tr><td>
	 	<form action="query.jsp" name="queryForm" method="post">
	 	Query:&nbsp;
	 	<textarea cols=40 rows=10 type=text name="query"><%=query%></textarea>
	 	Type:&nbsp;<input type=text value="query" name="queryType">
	 	Table:&nbsp;<select name="table" onchange='document.queryForm.query.value="sp_columns @table_name= " +this.value'>
	 	<%for (String tl: tlist){ %>
  			<option value="<%=tl %>"><%=tl %></option>
  			<%} %>
			</select> 
	 	
	 	<input type=submit value="Enter" class="btn"/>
	 	</form>
	 	</td></tr>
	 	</table>
		</td>
	</tr>	
	<tr>
	 	<td align="" valign="top">
		<table class="sortable" id="viewQuery" border="1" align="center"
			cellpadding="0" cellspacing="0">
			
			<%
			if(!list.isEmpty())%><%{%><tr><%}
			for ( String ll : list) {
			    %>
			<th>&nbsp;<%=ll%>&nbsp;</th>
			<%} %></tr><%
			for ( DBObj ee : uu ) {%><tr><%
			    for(String str: ee.getRow()){
			    %>
			<td>&nbsp;<%=str%>&nbsp;</td>
			<%}
			%></tr><%} %>
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
