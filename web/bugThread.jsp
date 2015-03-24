
<jsp:directive.page import="com.soward.object.Bugs" />
<jsp:directive.page import="com.soward.util.BugsUtil" />
<jsp:directive.page import="com.soward.util.BugThreadUtil"/>
<jsp:directive.page import="com.soward.object.BugThreads"/>
<jsp:directive.page import="java.util.ArrayList"/>
<%
			String username = (String) session.getAttribute("username");
            String message = request.getParameter( "message" );
            String bugComment = request.getParameter( "bugComment" );
            String bugCommentReporter = request.getParameter( "bugCommentReporter" );
            String bugId = request.getParameter( "bugId" );
            ArrayList<BugThreads> threadList = new ArrayList();
            Bugs bug = new Bugs();
           if(bugComment!=null&&bugCommentReporter!=null){
            	BugThreads newBugThread = new BugThreads();
            	newBugThread.setBugId(bugId);
            	newBugThread.setBugThread_desc(bugComment);
            	newBugThread.setBugThread_reporter(bugCommentReporter);
            	BugThreadUtil.saveBugThread(newBugThread);
            }
            if ( bugId != null ) {
                bug = BugsUtil.fetchForId( bugId );
            	threadList = BugThreadUtil.fetchAll(bugId);
            }
 
%>

<p class="message">
	<%
	if ( message != null ) {
	%><br />
	<%=message%>
	<%
	}
	%>
	</p>
	<table cellpadding=0 cellspacing=0 valign=top align=center class="simple">
		<tr> <th> Id </th>
			<th> Reporter </th>
			<th> Title </th>
			<th> Desc </th>
			<th> Date </th>
			<th> Status </th>
		</tr>
		<tr>
			<td> <%=bug.getBugId()%> </td>
			<td> <%=bug.getBugReporter()%> </td>
			<td> <%=bug.getBugTitle()%> </td>
			<td width="300"> <%=bug.getBugDesc()%> </td>
			<td> <%=bug.getBugDate()%> </td>
			<td> <%=bug.getBugStatus()%> </td>
		</tr>
		<%if(threadList.size()>0){ %>
      <tr><th>Name</th><th colspan=4 align=center><b>COMMENTS:</b></th><th>date</th></tr>
    <%
			for(BugThreads bugt: threadList){
			%>
			<tr><td><%=bugt.getBugThread_reporter() %></td><td colspan="4"><%=bugt.getBugThread_desc() %></td><td><%=bugt.getBugThread_date() %></td></tr>
		<%}} %>
	</table>
	<br>
	<table cellpadding=0 cellspacing=0 valign=top align=center class="simple">
		<form action="./bugThread.jsp">
		<tr>
		<tr>
			<td>
				Reporter:&nbsp;&nbsp; <%=username%>
			</td>
		</tr>
		<input type="hidden" name="bugCommentReporter" value="<%=username%>" />
		<input type="hidden" name="bugId" value="<%=bugId%>">
		<tr>
			<td colspan=2>
				Comment:
			</td>
			</tr>
			<tr>
			<td>
				<textarea cols="30" rows="10" name="bugComment"></textarea>
			</td>
		</tr>
		<tr>
			<td>
				<input type="submit" value="Add Comment" class="btn" />
				&nbsp;&nbsp;&nbsp;
				<input type=button value="Close"
					onClick="javascript:window.close();" class="btn">
			</td>
		</tr>
		</tr>
		</form>
	</table>
