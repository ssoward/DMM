<%
   //String ipid =session.getId();
   //String ipinfo =application.getServerInfo();
   //String ipadd =request.getRemoteAddr();
   //System.out.print("Journal HIT login: " + ipadd);
   //java.util.Date d1 = new java.util.Date();
   //System.out.println(" Time: " + d1);

%>


<style>
</style>

<script type="text/javascript" >
function lf(){document.log.username.focus();}
function checkLogin(){
  if(document.log.username.value!=''&& document.log.password.value!=''){
  	document.log.action="/DMM/Login";  
    document.log.submit();                                                                                                      
  }                                                                                                                                               
  else{                                                                                                                                           
     alert('Invalid new username and password.');                                                                                                
  }                                                                                                                                               
}
</script>
<%
	String message = request.getParameter("message");
	boolean mess = false;
	if(message!=null){
	    mess = true;
	}
    session=null;
            %>
    <FORM name="log"> 
    <div class="login"> 
         <p>User Name:<br />
               <INPUT class="input-box" TYPE="text" NAME="username">
         </p>
         <p>Password:<br />
    			<INPUT class="input-box" TYPE=password NAME="password">
         </p>
         <p align="center"> 
    			<input type=image alt="Enter" src="images/submit-bt.gif"  onclick="checkLogin();" width="83" height="22" border="0" class="submit" />
         </p>
       <%if(mess){ %>
       <p class="warning"><%=message%></p>
       <%} %>
       </div>
    </FORM>

<script language="JavaScript"> 
 onLoad=lf();
</script>