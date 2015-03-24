<jsp:directive.page import="com.soward.util.BugsUtil" />
<jsp:directive.page import="com.soward.object.Bugs" />
<jsp:directive.page import="com.soward.object.BugThreads"/>
<jsp:directive.page import="com.soward.util.BugThreadUtil"/>
<%@include file="adamJspsetup.jsp"%>
<script type="text/javascript" src="js/comboBox.js"></script>
<script type="text/javascript" src="js/ajaxThread.js"></script>
<%
      String message = request.getParameter( "message" );
      String bugReporter = request.getParameter( "bugReporter" );
      String bugTitle = request.getParameter( "bugTitle" );
      String bugDesc = request.getParameter( "bugDesc" );

%>

<%
       if ( message != null ) {
     %>
<p class="message"> <%=message%> </p>
<%}%>
<br />

     <center>
<h1> Welcome to DMM Dashboard </h1>
</center>

<br/>

<%@include file="bottomLayout.jsp"%>
