<%@page import="com.soward.object.Instrument"%>
<%@page import="com.soward.object.InstrumentInventory"%><jsp:directive.page import="com.soward.util.BugsUtil" />
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
  function submitForm(){ 
       document.productForm.submit();      
  }    
</script>

<%
  String message = request.getParameter( "message" );
  List<Instrument> pList = InstrumentUtils.fetchAll();
  String srhName = request.getParameter("srhName");
  String instrSel = request.getParameter( "instrSel");
   if(srhName!=null){
	  List<Instrument> pListt = InstrumentUtils.searchForName(srhName);
	  if(pListt!=null&&pListt.size()>0){
		  instrSel = pListt.get(0).getPid();
	  }}

  
  Instrument editInstr = null;
  HashMap<String,InstrumentInventory> inList = InstrumentInventoryUtil.getAllHash();
%>
<br/>
<h1>Location Inventory</h1>
<form name="productForm" method="get"  action="instrumentInventory.jsp">
		<%if(message!=null){%>
		<p class="message"><%=message%></p>
		 <%}%>
		<p class="text">Edit inventory by location.</p>
<table cellspacing="3" cellpadding="3" border="0" align="center" class="simple">
  <tr><td>
         <table border="0" align="center" cellspacing="3" cellpadding="3">
           <tr><td>Select an instrument: </td><td>
           <select name="instrSel" onchange="submitForm();">
             <option>Instrument List</option>
             <% for(Instrument per: pList){ 
             String sel = "";
             if(!StringUtils.isBlank(instrSel)){
              if(instrSel.equals(per.getPid())){
                 editInstr = per;
                 sel = "selected";
              }
             }
             
             %>    	
             <option <%=sel%> value="<%=per.getPid()%>"><%=per.getName    ()%> --- <%=per.getItemNumber()%></option>
				 <%} %>
         </select>
        </td></tr>  
      </table>
  </td></tr>
</table>
<center><br>
  <%if(editInstr!=null){%> 
  <table border=1 width="70%" cellspacing="1" cellpadding="3" bgcolor="white">
    <th>Name</th>
    <th>Number</th>
    <th>Supplier</th>
    <th>Type</th>
    <tr>
      <td valign="top"><a href="./createInstruments.jsp?pid=<%=editInstr.getPid()%>"><%=editInstr.getName()%></a></td>
      <td valign="top"><%=editInstr.getItemNumber()%></td>
      <td valign="top"><%=editInstr.getSupplier  ()%></td> 
      <td valign="top"><%=editInstr.getType      ()%></td>
    </tr>
  </table>
<center><br>
  <table border=1 width="30%" cellspacing="0" bgcolor="lightblue" cellpadding="3" bgcolor="white">
    <th>Location</th>
    <th>Count</th>
    <th>-</th>
    <th>+</th>
    <th>Before Edit</th>

          <%
              int cc = 0;
              for ( InstrumentLocation lname : InstrumentLocation.values() ) {
            	  InstrumentInventory ini = inList.get(lname.name()+editInstr.getPid());
            	  String start = "0";
            	  if(ini!=null){
            		  start = ini.getCount();
            	  }
          %>
          <tr>
          <td><%=lname.displayName()%></td>
			<td align="center"><div id="<%="description"+cc%>"><%=start %></div></td>
		  	<td>
			<img src="images/go-down.gif" onclick="makeGetRequest2(<%=editInstr.getPid()%>,<%="'"+lname.name()+"'" %>,<%=cc%>, 0);">
		  	<td>
			<img src="images/go-up.gif" onclick="makeGetRequest2(<%=editInstr.getPid()%>,<%="'"+lname.name()+"'" %>,<%=cc%>, 1);">
		  	</td>
		  	<td align="center"><%=start %></td>
          </tr>
          <%
              cc++;
              }
          %>
          
  </table>
    <%} %>

  <br/>
</form>
<br/>
<br/>

</center>

<form action="instrumentInventory.jsp" method="Post">
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
      <tr><td colspan="3"><h1>Instruments Search</h1></td></tr>
      <tr><td>&nbsp;</td></tr>
      <tr><td><label>Name</label></td>
        <td width="150"><input type="text" name="srhName" value="" style="width:90%" /></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td><input type="submit" value="Search" class="btn" /></td>
      </tr>
    </table>
  </div>    		
</form>
<%@include file="bottomLayout.jsp"%>
