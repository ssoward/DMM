<%@page import="com.soward.object.Person"%><jsp:directive.page import="com.soward.util.BugsUtil" />
<jsp:directive.page import="com.soward.object.Bugs" />
<jsp:directive.page import="com.soward.object.BugThreads"/>
<jsp:directive.page import="com.soward.util.BugThreadUtil"/>
<%@include file="adamJspsetup.jsp"%>

<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.*"%>
<%@page import="com.soward.enums.*"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<style type="text/css">
  .scrollTable{ 
    overflow: auto; 
      width: 100%; 
        height: 200px;  
          padding: 0px; 
            margin: 0px;  
            }
  </style>
<script type="text/javascript" src="js/comboBox.js"></script>
<script type="text/javascript" src="js/inventoryAjax.js"></script>
<link  href="css/app.css" title="CompHealth" rel="stylesheet" type="text/css" media="all"/>
<script src="js/prototype-1.6.0.3.js" type="text/javascript"></script>
<script src="js/effects.js" type="text/javascript"></script> 
<script src="js/app.js" type="text/javascript"></script>


<SCRIPT LANGUAGE="JavaScript">                       
  function refreshDescriptions(){ 
  	   document.productForm.saveUpdatedProduct.value = '';	                                                                 
       document.productForm.submit();      
  }                                                                                       
</script>

<%
 String message = request.getParameter( "message" );
 String nt = request.getParameter("nt");
 String srhCou = request.getParameter("srhCou");
 String srhTeach = request.getParameter("srhTeach");
 String pid = request.getParameter("pid");
 Person person = new Person();

 if(nt!=null){
    person.setFirstName      (request.getParameter("firstName"  ));
    person.setLastName       (request.getParameter("lastName"   ));
    person.setAddress1       (request.getParameter("address1"   ));
    person.setCity           (request.getParameter("city"       ));
    person.setCounty         (request.getParameter("county"     ));
    person.setState          (request.getParameter("state"      ));
    person.setPhone          (request.getParameter("phone"      ));
    person.setCell           (request.getParameter("cell"       ));
    person.setEmail          (request.getParameter("email"      ));
    person.setNotes          (request.getParameter("notes"      ));
    person.setPerson_type    (PersonType.TEACHER.name());
    person.setPerson_pid     (request.getParameter("person_pid"));
    try{
    PersonUtils.saveOrUpdate(person);
    	if(StringUtils.isBlank(person.getPerson_pid())){
    		message = "Successfully created Teacher";
    	}else{
    		message = "Successfully updated Teacher";
    	}
    	person = new Person();
    }catch(Exception e){
        message = e.toString();
    }
}
  List<Person> pList = new ArrayList<Person>();
  if(srhTeach!=null||srhCou!=null){
    pList = PersonUtils.fetchForParams(PersonType.TEACHER, srhTeach,srhCou);
    //if only one is returned get pid and display in edit field
    if(!pList.isEmpty()&&pList.size()<2){
    	pid = pList.get(0).getPerson_pid();
    }else{
    	message = "No results found from search.";
    }
  }else{
    pList = PersonUtils.fetchAllForType(PersonType.TEACHER);
  }
  if(!StringUtils.isBlank(pid)){
	     try{
	     person = PersonUtils.fetchForPid(pid);
	     }catch(Exception e){
	         e.printStackTrace();
	     }
	 }
%>
<br/>
<h1>Add New Teacher</h1>
<form name="teacherForm" method="post"  action="createTeacher.jsp">
<input type=hidden value="saveUpdatedProduct" name="saveUpdatedProduct">
		<%if(message!=null){%>
		<p class="message"><%=message%></p>
		 <%}%>
		<p class="text">To create a new teacher fill out the fields below.</p>
    <table cellspacing="0" cellpadding="0" align="center" class="simple">
      <tr><td>
          <table border=0 cellspacing="3" cellpadding="3">
            <tr><td>
                <table border=0 cellspacing="3" cellpadding="3">
                  <tr><td>
                      <table border=0 cellspacing="3" cellpadding="3">
                        <tr><td>FirstName      </td><td><input type="text" value="<%=person.getFirstName      ()%>" name="firstName"  > </td></tr>
                        <tr><td>LastName       </td><td><input type="text" value="<%=person.getLastName       ()%>" name="lastName"   > </td></tr>
                        <tr><td>Address1       </td><td><input type="text" value="<%=person.getAddress1       ()%>" name="address1"   > </td></tr>
                        <tr><td>City           </td><td><input type="text" value="<%=person.getCity           ()%>" name="city"       > </td></tr>
                        <tr><td>County         </td><td>
                            <select name="county">
                              <%
                              String coutt = person.getCounty();
                              if(!StringUtils.isBlank(coutt)){%>
                              <option value="<%= person.getCounty() %>"><%=person.getCounty() %></option>
                              <%}
                              ArrayList<String> couts = AddressUtil.fetchCountyList();
                              for(String cout: couts){
                              %>
                              <option value="<%= cout %>"><%=cout %></option>
                              <%}%>
                            </select> 
                        </td></tr>
                        <tr><td>State          </td>
                          <td>
                            <select name="state">
                              <%
                              coutt = person.getState();
                              if(!StringUtils.isBlank(coutt)){%>
                              <option value="<%= person.getState() %>"><%=person.getState() %></option>
                              <%}
                              ArrayList<String> coutss = AddressUtil.fetchStateList();
                              for(String cout: coutss){
                              %>
                              <option value="<%= cout %>"><%=cout %></option>
                              <%}%>
                            </select> 
                        </td></tr>
                      </table>
                    </td>
                    <td>
                      <table border=0 cellspacing="3" cellpadding="3">
                        <tr><td>Phone          </td><td><input type="text" value="<%=person.getPhone          ()%>" name="phone"      > </td></tr>
                        <tr><td>Cell           </td><td><input type="text" value="<%=person.getCell           ()%>" name="cell"       > </td></tr>
                        <tr><td>Email          </td><td><input type="text" value="<%=person.getEmail          ()%>" name="email"      > </td></tr>
                        <tr><td>Notes          </td><td><textarea cols="25" rows="5" name="notes"   ><%=person.getNotes          ()%></textarea> </td></tr>
                        <tr><td><input type="hidden" value="nt" name="nt"> </td></tr>
                        <tr><td><input type="hidden" value="<%=person.getPerson_pid() %>" name="person_pid"> </td></tr>

                      </table>
                  </td></tr>  
                </table>
            </td></tr>  

          </table>
      </td></tr>
    </table>
    <center><br>
      <input type="button" value="Reset" onClick="this.form.reset()" class="btn"/>&nbsp;&nbsp;&nbsp;
      <input type=button value="Save" onclick="this.value='Loading...'; this.disabled =true;this.form.submit()" class="btn" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <br/>
    </form>
    <br/>
    <br/>
    <div class="scrollTable">
      <table border=1 width="70%" cellspacing="1" cellpadding="3" bgcolor="white">
        <th>Name</th>
        <th>Address</th>
        <th>County</th>
        <th>Phone</th>
        <th>Email</th>
        <th>Notes</th>
        <%
        for(Person per: pList){
        %>    	
        <tr>
          <td valign="top"><a href="./createTeacher.jsp?pid=<%=per.getPerson_pid()%>"><%=per.getFirstName    ()+" "+per.getLastName()%></a></td>
          <td valign="top"><%=per.getAddress1()%></td>
          <td valign="top"><%=per.getCounty  ()%></td> 
          <td valign="top"><%=per.getPhone   ()%></td>
          <td valign="top"><%=per.getEmail   ()%></td>
          <td valign="top"><%=per.getNotes   ()%></td>
        </tr>

        <%} %>
      </table>
    </div>

  </center>
  <form action="createTeacher.jsp" method="Get">
    <div id="searchPanel" class="search_form" >
      <div class="search_handle" onclick="toggle_search('searchPanel')">  
        <div class="search_handleText">
          <br/>
          &nbsp;S<br/>
          &nbsp;E<br/>
          &nbsp;A<br/>
          &nbsp;R<br/>
          &nbsp;C<br/>
          &nbsp;H<br/>          
        </div>         
      </div>	
      <table border="0" width="95%" cellspacing="0" cellpadding="3">
        <tr>
          <td><a href="#" onclick="toggle_search('searchPanel')">Close</a></td>
        </tr>
        <tr><td colspan="3"><h1>Teacher Search</h1></td></tr>
        <tr><td>&nbsp;</td></tr>
        <tr><td><label>Name</label></td>

          <td width="150"><input type="text" name="srhTeach" value="" style="width:90%" /></td>
        </tr>
        <tr>
          <td><label>County:</label></td>
          <td width="150">
            <select name="srhCou" style="width:90%" type="text">
              <option value=""></option>
              <%
              for(String cout: couts){
              %>
              <option value="<%= cout %>"><%=cout %></option>
              <%}%>
            </select> 
        </td></tr>

      </tr>
      <tr>
        <td>&nbsp;</td>
        <td><input type="submit" value="Search" class="btn" /></td>
      </tr>


    </table>
  </div>    		
</form>



<%@include file="bottomLayout.jsp"%>
