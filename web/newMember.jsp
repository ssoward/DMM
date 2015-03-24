<%@include file="jspsetup.jsp"%>
<%
String message = request.getParameter("message");
//message = (String)session.getAttribute("message");

%>

<h1>New Client</h1>
<br />
<table width="250" class="simple" align="center" cellpadding="0" cellspacing="0">
     <th colspan="2">
          <form method="post" action="NewClient">
               New Client
               </th>
               <tr>
                         <td>First </td>
                         <td><input type="text" name="firstName"/></td>
                    </tr>
               <tr>
                         <td>Last </td>
                         <td><input type="text" name="lastName" /></td>
                    </tr>
               <tr>
                         <td>Address </td>
                         <td><input type="text" name="addr1"    /></td>
                    </tr>
               <tr>
                         <td>Address </td>
                         <td><input type="text" name="addr2"    /></td>
                    </tr>
               <tr>
                         <td>Email </td>
                         <td><input type="text" name="email"    /></td>
                    </tr>
               <tr>
                         <td>Homepage </td>
                         <td><input type="text" name="homepage" /></td>
                    </tr>
               <tr>
                         <td>Company Name </td>
                         <td><input type="text" name="compName" /></td>
                    </tr>
               <tr>
                         <td>Primary Phone </td>
                         <td><input type="text" name="primPhone"/></td>
                    </tr>
               <tr>
                         <td colspan="2" align="center"><input type="submit" value="Add Client" class="btn"/></td>
                    </tr>
          </form>
          </td>
          <% if(message!=null){%>
          <td><%=message %></td>
          <%}%>
     </tr>
</table>
<%@include file="bottomLayout.jsp"%>
