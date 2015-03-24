<%@include file="jspsetup.jsp"%>


<%@page import="com.soward.object.User"%>


<%
            User user = new User();
            String message = request.getParameter( "message" );
            String deleteUser = request.getParameter( "deleteUser" );
            if ( deleteUser != null ) {
                user.deleteUser( deleteUser );
            }
            User masterUser = user.getUser( "scott" );
            String masterPid = masterUser.getPid();
            ArrayList<User> uu = user.getAllUser();
%>

		<table class="sortable, common" id="viewClients" border="1" align="center" cellpadding="0" cellspacing="0" width="780">
			<tr>
				<th>&nbsp;Login&nbsp;</th>
				<th>&nbsp;Role&nbsp;</th>
				<th>&nbsp;First Name&nbsp;</th>
				<th>&nbsp;Last Name&nbsp;</th>
				<th>&nbsp;Email&nbsp;</th>
				<th>&nbsp;Phone&nbsp;</th>
				<%
				if ( isAdmin ) {
				%>
				<th>&nbsp;Edit&nbsp;</th>
				<th>&nbsp;Delete&nbsp;</th>
				<%
				}
				%>
			</tr>
			<%
			                if ( uu != null ) {
			                for ( User temp : uu ) {
			%>
			<tr>
				<td>&nbsp;<%=temp.getName()%>&nbsp;</td>
				<td>&nbsp;<%=temp.getRole()%>&nbsp;</td>
				<td>&nbsp;<%=temp.getFirst()%>&nbsp;</td>
				<td>&nbsp;<%=temp.getLast()%>&nbsp;</td>
				<td>&nbsp;<%=temp.getEmail()%>&nbsp;</td>
				<td>&nbsp;<%=temp.getPhone()%>&nbsp;</td>
				<%
				if ( isAdmin && ( !temp.getPid().equals( "8" ) ) ) {
				%>
				<form name="editUser" method="post" action="./editUser.jsp">
				<td>&nbsp;
					<input type=hidden name="pid" value="<%=temp.getPid()%>"/>
					<input type="submit" value="Edit" />&nbsp;</td>
					</form>
				<form name="deleteUser" method="post" action="viewUser.jsp">
				<input type="hidden" value="<%=temp.getPid() %>" name="deleteUser">
				<td>&nbsp;<input type="submit" value="Del">&nbsp;</td>
				</form>
				<%
				} else {
				%>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<%
				                }
				                }
				%>
			</tr>
			<%
			}
			%>
		</table>

		<%
		if ( message != null ) {
		%>
		<p class="message"><%=message%></p>
		<%
		}
		%>

<%@include file="bottomLayout.jsp"%>
