<%@ page import="java.sql.*" %>
<%
HttpSession sess = request.getSession(false);
request.getSession().invalidate();
if(sess != null){
    sess =null;
}
    response.sendRedirect("./");
%>
 
