<%@include file="jspsetup.jsp"%>



<%
String message = request.getParameter("message");
//message = (String)session.getAttribute("message");

%>
<div>
	<% if(message!=null){%>
	<div>
		<%=message %>
     </div>
  <%}%>
</div>
<!--<%@include file="bottomLayout.jsp"%>-->
