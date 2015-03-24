<%@include file="jspsetup.jsp"%>




<%
String message = request.getParameter("message");
//message = (String)session.getAttribute("message");

%>
 <table width="754" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr> <td align="" valign="top">
<table>
  <tr>
</table>

</td>
<% if(message!=null){%>
<td><%=message %></td>
  <%}%>
</tr>
</table>
<%@include file="bottomLayout.jsp"%>
