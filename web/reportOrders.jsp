
<%@page import="com.soward.util.Utils"%><%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.Order"%>
<%@page import="com.soward.util.OrderUtils"%>
<%@page import="com.soward.object.Supplier"%>
<%@page import="com.soward.util.SupplierUtil"%>
<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.TransUtil"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.enums.LocationsDBName"%>
<!-- Load jQuery, SimpleModal and Basic JS files -->
<script type='text/javascript' src='js/jquery.js'></script>
<script type='text/javascript' src='js/jquery.simplemodal.js'></script>
<script type='text/javascript' src='js/basic.js'></script>
<link type='text/css' href='css/basic.css' rel='stylesheet' media='screen' />


<script type="text/javascript" src="js/inventoryAjax.js"></script>
<script type="text/javascript">
  function toggleImg(obj){
    var theImg = obj.src;
    
    var x = theImg.split("/");
    var t = x.length-1;
    var y = x[t];
    
    if(y=='blueCheck.gif')
    {
      obj.src='images/delete.gif';
    }
    else
    {
      obj.src='images/blueCheck.gif';
    }
  }
</script>
<%

	HashMap<String, Supplier> allSups = null;
	
	String message = request.getParameter( "message" );
	User user = new User();
	String locationName = request.getParameter( "locationName" );
	String rbool = request.getParameter( "rbool" );
	boolean received = false;
	if(!StringUtils.isBlank(rbool)&&!rbool.equals("null")){
		received = true;
	}
	int pc = 0;
	int picNext = 20;
	int picBack = 0;
	HashMap<String, ArrayList<Order>> uu = null;//new ArrayList<Invoice>();
	if(!StringUtils.isBlank(locationName)){
		allSups = SupplierUtil.getAllSuppliersHash();	
	    uu = OrderUtils.getAllOrders(locationName, received );
	}
%>




<%@page import="org.apache.commons.lang.StringUtils"%>
<br/>
<h1>Current Orders</h1>
<br />
<form name="myForm" action="./reportOrders.jsp" method="post">
<input type="hidden" name="rbool" value="<%=rbool%>" />
  <center>
<table cellspacing="3" cellpadding="3" border="0">
<tr>
   <td  align="right">
     Store:
     </td><td align="left">
     <select onChange="document.myForm.submit();" name="locationName" value="test">
          <option>Select Location</option>
          <%
          for ( LocationsDBName lname : LocationsDBName.values() ) {
            String sele = "";
            if(lname.name().equals(locationName)){
              sele = "selected=\"selected\"";
            }
            %>
            <option <%=sele%> value="<%=lname.name()%>"><%=lname.displayName()%></option>
            <%
          }
          %>
        </select>
 </td>
</tr>
</table>
</form>
<% if(uu!=null){%>
<table class="sortable, common" id="viewAccounts" align="center"	cellpadding="0" cellspacing="0">
     <tr>
          <th></th>
          <th>Supplier</th>
          <th>Phone</th>
          <th>Product</th>
          <th>#</th>
          <th>Requested</th>
          <th>By</th>
          <th>Arrived</th>
          <th>In</th>
          <th>Notes</th>
     </tr>
     <%
			boolean flipShade = true;
			//HashMap<String, List<Order>> uu
			Set set = uu.keySet();
			Iterator<String> iter = set.iterator();
			while(iter.hasNext()){
				int count = 1;
				String key = iter.next();
				Supplier sup = allSups.get(key);
				List<Order> oList = uu.get(key);
				for(Order o: oList){
				%>
					<tr>
					<td><%=count%></td>
					<td><%=sup.getSupplierName()==null?"":sup.getSupplierName() %></td>
					<td><%=sup.getSupplierPhone()==null?"":sup.getSupplierPhone() %></td>
					<td><%=o.getProd().getProductCatalogNum()+" "+o.getProd().getProductName() %></td>
					<td><%=o.getCount() %></td>
					<td><%=Utils.d(o.getDateEmail()) %></td>
					<td><%=o.getUserReceived()==null?"":o.getUserReceived() %></td>
				 	<td><div id="<%=o.getId()%>">
		          			<%=Utils.d(o.getDateReceived()) %>
	          			</div>
		          	</td>
          <td>
			
          <img width="15" style="cursor: pointer; cursor: hand; display: inline-block; vertical-align: middle"
            onclick="toggleImg(this);makeGetRequestToggleArrivedOrder('<%=o.getId()%>', '<%=username%>') ;"
			<%if(o.getUserReceived()==null||o.getUserReceived().length()<1){ %>
			src="images/blueCheck.gif" 
			<%}else{ %>
			src="images/delete.gif" 
			<%} %>
			title="Set order to ARRIVED or NOT for product <%=o.getProd().getProductName() %>" />
          
          </td>
          <td align="center">
		    <div id='basic-modal'>
          <img width="15" style="cursor: pointer; cursor: hand; display: inline-block; vertical-align: middle"
            onclick="makeGetRequestNotesModal('<%=o.getId()%>', '<%=username%>') ;"
			      src="images/blueBalloon.gif" class='basic'
            title="Create notes for this order" />
        </td>


				<%
				count++;
				}
			}
		%>
    </table>
			</div>
            <div id="basic-modal-content">
              <div id="notes"> 
              </div>
              <input type="button" class="modalCloseImg simplemodal-close" value="Close"/>
			</div> 
<%}%>
</td>
<%
		if ( message != null ) {
		%>
<td><p class="text"><%=message%></p></td>
<%
		}
		%>
</tr>
</table>
<%@include file="bottomLayout.jsp"%>
