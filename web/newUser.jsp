<%@include file="jspsetup.jsp"%>

<SCRIPT LANGUAGE="JavaScript">                       
  function checkValid(){                                                                  
    if(document.newUserForm.userPass1.value==document.newUserForm.userPass2.value){
       document.newUserForm.submit();      
    }
    else{                               
      alert('Passwords dont match');
    }
  }                                                                                       
</script>                 
 


<%
String message = request.getParameter("message");
//message = (String)session.getAttribute("message");

%>
<table cellpadding="0" cellspacing="0" class="simple">
  <tr>
  <form name="newUserForm" method="post" action="UserUtil">
    <th colspan=2>New User</th>
  </tr>
   <tr> <td>Login Name:         </td><td><input type="text" name="loginName"       /></td>  </tr>         
   <tr> <td>First Name:         </td><td><input type="text" name="userFirst"       /></td>  </tr>         
   <tr> <td>Last Name:          </td><td><input type="text" name="userLast"        /></td>  </tr>         
   <tr> <td>Role:               </td><td>
   <select name="userRole">
   <option value="">Select a role below.</option>
   <option value="admin">Admin</option>
   <option value="client">Client</option>
   </select>
   </td>  </tr>
   <tr> <td>Email:              </td><td><input type="text" name="userEmail"       /></td>  </tr>
   <tr> <td>Phone:              </td><td><input type="text" name="userPhone"       /></td>  </tr>
   <tr> <td>Password:           </td><td><input type="password" name="userPass1"       /></td>  </tr>
   <tr> <td>Reenter Password:   </td><td><input type="password" name="userPass2"       /></td>  </tr>
   <tr><td colspan="2" align="center"><input type="button" onclick="checkValid()" value="Add User" class="btn"/></td></tr>
   </form>
</table>


<% if(message!=null){%>
<p class="message"><%=message %>
  <%}%>
</p>
<%@include file="bottomLayout.jsp"%>
