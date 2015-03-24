<%@page import="com.soward.object.Instrument"%><jsp:directive.page import="com.soward.util.BugsUtil" />
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
 String pid = request.getParameter("pid");
 String srhName = request.getParameter("srhName");
 Instrument instrument = new Instrument();

 if(nt!=null){
    instrument.setName           (request.getParameter("name"    ));
    instrument.setItemNumber     (request.getParameter("number"  ));
    instrument.setSupplier       (request.getParameter("supplier"));
    instrument.setType           (request.getParameter("type"    ));
    instrument.setPid            (request.getParameter("instrument_pid" ));
    try{
    InstrumentUtils.saveOrUpdate(instrument);
    	if(StringUtils.isBlank(instrument.getPid())){
    		message = "Successfully created Instruments";
    	}else{
    		message = "Successfully updated Instruments";
    	}
    	instrument = new Instrument();
    }catch(Exception e){
        message = e.toString();
    }
}
  List<Instrument> pList = new ArrayList<Instrument>();
  if(!StringUtils.isBlank(pid)){
	     try{
	     instrument = InstrumentUtils.fetchForPid(pid);
	     }catch(Exception e){
	         e.printStackTrace();
	     }
	 }
  if(srhName!=null){
	  pList = InstrumentUtils.searchForName(srhName);
	  if(pList!=null&&pList.size()<2){
		  instrument = pList.get(0);
	  }
  }else{
	  pList = InstrumentUtils.fetchAll();
  }
  
%>
<br/>
<h1>Add New Instrument</h1>
<form name="Instruments" method="post"  action="createInstruments.jsp">
<input type=hidden value="saveUpdatedProduct" name="saveUpdatedProduct">
		<%if(message!=null){%>
		<p class="message"><%=message%></p>
		 <%}%>
		<p class="text">To create a new Instrument fill out the fields below.</p>
<table cellspacing="0" cellpadding="0" align="center" class="simple">
  <tr><td>
      <table border="0" cellspacing="3" cellpadding="3">
        <tr><td>
            <table border="0" cellspacing="3" cellpadding="3">
              <tr><td>

                  <table border="0" align="center" cellspacing="3" cellpadding="3">
                    <tr><td>Name      </td><td><input type="text" value="<%=instrument.getName       ()%>" name="name"    > </td></tr>
                    <tr><td>Item #    </td><td><input type="text" value="<%=instrument.getItemNumber ()%>" name="number"  > </td></tr>
                    <tr><td>Supplier  </td><td><input type="text" value="<%=instrument.getSupplier   ()%>" name="supplier"> </td></tr>
                    <tr><td>Type      </td><td><input type="text" value="<%=instrument.getType       ()%>" name="type"    > </td></tr>
                    <tr><td><input type="hidden" value="nt" name="nt"> </td></tr>
                    <tr><td><input type="hidden" value="<%=instrument.getPid() %>" name="instrument_pid"> </td></tr>
                  </table>
              </td></tr>  
            </table>
        </td></tr>  

      </table>
  </td></tr>
</table>
<center><br>
  <input type="button" value="Reset" onClick="this.form.reset()" class="btn"/>&nbsp;&nbsp;&nbsp;
  <input type="button" value="Save"  onClick="this.value='Loading...'; this.disabled =true;this.form.submit()" class="btn" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <br/>
</form>
<br/>
<br/>
<div class="scrollTable">
  <table border=1 width="70%" cellspacing="1" cellpadding="3" bgcolor="white">
    <th>Name</th>
    <th>Number</th>
    <th>Supplier</th>
    <th>Type</th>
    <%
    for(Instrument per: pList){
    %>    	
    <tr>
      <td valign="top"><a href="./createInstruments.jsp?pid=<%=per.getPid()%>"><%=per.getName    ()%></a></td>
      <td valign="top"><%=per.getItemNumber()%></td>
      <td valign="top"><%=per.getSupplier  ()%></td> 
      <td valign="top"><%=per.getType      ()%></td>
    </tr>

    <%} %>
  </table>
</div>

</center>
<form action="createInstruments.jsp" method="Post">
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
