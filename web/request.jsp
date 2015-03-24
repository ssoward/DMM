<jsp:directive.page import="com.soward.util.BugsUtil" />
<jsp:directive.page import="com.soward.object.Bugs" />
<jsp:directive.page import="com.soward.object.BugThreads"/>
<jsp:directive.page import="com.soward.util.BugThreadUtil"/>
<%@include file="jspsetup.jsp"%>
<script type="text/javascript" src="js/comboBox.js"></script>
<script type="text/javascript" src="js/ajaxThread.js"></script>
<%
      String message = request.getParameter( "message" );
      String bugReporter = request.getParameter( "bugReporter" );
      String bugTitle = request.getParameter( "bugTitle" );
      String bugDesc = request.getParameter( "bugDesc" );
      
			HashMap<String, String>commentsHash = BugsUtil.getBugCommentsCounts();
      if ( bugReporter != null && bugTitle != null && bugDesc != null ) {
          Bugs newBug = new Bugs();
          newBug.setBugDesc( bugDesc );
          newBug.setBugTitle( bugTitle );
          newBug.setBugReporter( bugReporter );
          message = BugsUtil.saveBug( newBug );
       }
      ArrayList<Bugs> allBugs = BugsUtil.fetchAll();
%>

<%
       if ( message != null ) {
     %>
<p class="message"> <%=message%> </p>
<%}%>
<br />
<script type="text/javascript">
          djConfig = {
               isDebug: false,
               parseOnLoad: true,
          useXDomain: true,
          usePlainJson:true,
               xdWaitSeconds: 20
          };
</script>
<link  href="css/chg_dojo_admin.css" rel="stylesheet" type="text/css"/>
<link href="/utils/js/global/scripts/dojo/dijit/themes/tundra/tundra.css" rel="stylesheet" type="text/css"/>
<link href="/utils/js/global/scripts/dojo/dojo/resources/dojo.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript"  src="/utils/js/global/scripts/dojo/dojo/dojo.js" ></script>
<script type="text/javascript" src="/utils/js/global/scripts/dojo/dojo/dojo.xd.js" ></script>
<script type="text/javascript" src="js/admin.js" ></script>
<script>
                     dojo.require("dijit.Dialog");
                     dojo.require("dijit.form.Button");
                     dojo.require("chg.dojo.FormDialog");
                     dojo.require("dojo.data.ItemFileReadStore");
                     dojo.require("dijit.Tree");
                     dojo.require("dojo.parser");
     </script>
     <center>
<h1> New Features & Requests and Bugs Fixes </h1>
</center>
<p class="text">This is a simple request tool that we can begin using. I will	add additional functionality soon. </p>
<input type="button" value="Add New Request" onclick="dijit.byId('testDialog').show()" class="btn" />
<br/>
<br/>
<div dojoType="chg.dojo.FormDialog" id="testDialog"  style="display:none"> <br/>
     <center>
          <font size=3> Create a new request for Scott, give your request a title and a description. <br/>
          I will check this page periodically and may comment or ask questions about your request. <br/>
          Please if needed get your request approved.</font> <br/>
          <br/>
          <br/>
          <form action="./request.jsp">
               <table border=0 cellpadding=3 cellspacing=3 valign=top>
                    <tr>
                         <td align="left"> Reporter </td>
                         <td align="left"><input type="hidden" name="bugReporter" value="<%=username%>" />
                              <input type="text" value="<%=username%>" disabled="disabled" />
                         </td>
                    </tr>
                    <tr>
                         <td align="left"> Title: </td>
                         <td align="left"><input type="text" name="bugTitle" />
                         </td>
                    </tr>
                    <tr>
                         <td align="left"> Desc: </td>
                         <td align="left"><textarea cols="40" rows="5" name="bugDesc"></textarea>
                         </td>
                    </tr>
                    <tr>
                         <td colspan="3" align="left"><input type="button" class="btn" value="close" onclick="dijit.byId('testDialog').hide()" />
                              <input type="submit" value="Add Request" />
                         </td>
                    </tr>
               </table>
          </form>
     </center>
</div>

<!-- Start of Request Table -->
<div class="scrollTable">
     <table class="common" border="0" cellpadding="0" cellspacing="0">
          <tr>
               <th> <b>Comments </b></th>
               <th> <b>Reporter</b> </th>
               <th> <b>Title </b></th>
               <th> <b>Desc </b></th>
               <th> <b>Date </b></th>
               <th> <b>Status</b> </th>
          </tr>
          <!-- BEGIN INNER THREAD PAGE ############################################################ -->
          <%
             ArrayList<BugThreads> threadList = new ArrayList();
             HashMap<String , ArrayList<BugThreads>> hMap = BugThreadUtil.getAllBugs();
             for ( Bugs bug : allBugs ) { 
             String bdesc = bug.getBugDesc();
             if(bug.getBugStatus()!=null&&bug.getBugStatus().equals("Complete")&&bug.getBugDesc().length()>40){
             	bdesc = bdesc.substring(0, 40)+" ...";
             }	
             //threadList = BugThreadUtil.fetchAll(bug.getBugId());
             threadList = hMap.get(bug.getBugId());
             %>
          <div dojoType="chg.dojo.FormDialog" id="<%="threadPage"+bug.getBugId()%>"  style="display:none">
               <center>
                    <br/>
                    <b>Title: </b><%="  "+bug.getBugTitle()%> <br/>
                    <b>Reporter: </b><%="  "+bug.getBugReporter()%> <br/>
                    <b>Desc: </b><%="  "+bug.getBugDesc()%> <br/>
                    <b>Date: </b><%="  "+bug.getBugDate()%> <br/>
                    <b>Status: </b><%="  "+bug.getBugStatus()%> <br/>
                    New	Comment: <br/>
                    <textarea cols="50" rows="10" id="<%="bugComment"+bug.getBugId()%>" name="bugComment"></textarea>
                    <br/>
                    <br/>
                    <input type="button" class="btn" value="Close" onclick="dijit.byId('<%="threadPage"+bug.getBugId()%>').hide()" />
                    <input type="button" class="btn" value="Save" onclick="searchHistory('<%=bug.getBugId()%>', '<%=username %>');"/>
                    <br/>
                    <div id="<%="commentThreadList"+bug.getBugId()%>" >
                         <%if(threadList!=null&&threadList.size()>0){ %>
                         <%for(BugThreads bugt: threadList){ %>
                         -------------------------------------- <br>
                         <b>Name:</b> <%="  "+bugt.getBugThread_reporter() %> <br>
                         <b>Date:</b> <%="  "+bugt.getBugThread_date() %> <br>
                         <b>Comment:</b><%="  "+bugt.getBugThread_desc() %> <br>
                         <%}} %>
                    </div>
               </center>
          </div>
          <tr>
               <td align=center><input type="button" class="btn" value="<%="Bug #"+bug.getBugId()%>" onclick="dijit.byId('<%="threadPage"+bug.getBugId()%>').show()" />
               </td>
               <td><%=bug.getBugReporter()%> </td>
               <td><%=bug.getBugTitle()%> </td>
               <td width="300"><%=bdesc%>
                    <%if(commentsHash.containsKey(bug.getBugId())){ %>
                    <br/>
                    <span style="cursor:pointer" onclick="dijit.byId('<%="threadPage"+bug.getBugId()%>').show()"> <u><font color=blue><%="There are "+commentsHash.get(bug.getBugId())+" comments on this request." %></font></u> </span>
                    <%} %>
               </td>
               <td><%=bug.getBugDate()%> </td>
               <td align="center"><%String tempStatus = bug.getBugStatus();
                                if(tempStatus.equals("Complete")){
                                   tempStatus = "<font color=blue>"+tempStatus+"</font>";
                                }else{
                                   tempStatus = "<font color=red>"+tempStatus+"</font>";
                                }
                               %>
                    <%=tempStatus%> </td>
          </tr>
          <% } %>
     </table>
</div>
<%@include file="bottomLayout.jsp"%>
