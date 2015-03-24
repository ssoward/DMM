<jsp:directive.page import="com.soward.util.SupplierUtil"/>
<jsp:directive.page import="com.soward.object.Supplier"/>
<%@page import="com.soward.object.SupplierData"%>
<%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<jsp:directive.page import="com.soward.object.Supplier"/>
<jsp:directive.page import="com.soward.util.SupplierUtil"/>
<script type="text/javascript" src="js/inventoryAjax.js"></script>
<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>

<!-- Load jQuery, SimpleModal and Basic JS files -->
<script type='text/javascript' src='js/jquery.js'></script>
<script type='text/javascript' src='js/jquery.simplemodal.js'></script>
<script type='text/javascript' src='js/basic.js'></script>
<link type='text/css' href='css/basic.css' rel='stylesheet' media='screen' />

<%
  String supId = request.getParameter( "supId" );
  ArrayList<Supplier> allSups = SupplierUtil.getAllSuppliersNumAndName();
  SupplierData supData = null;
  String message = request.getParameter( "message" );
  String supName = request.getParameter( "supId" );
  String discount = request.getParameter( "discount" );
  String supNum = request.getParameter( "supNum" );
  Supplier supplier = null;
  if(supName!=null){
  	supplier =  SupplierUtil.fetchSupForNameOrNum(supName).get(0);
  	supData = SupplierUtil.fetchSuppliersData(supName);
    supData = supData!=null?supData:new SupplierData();
  }
  if(supNum!=null&&discount!=null){
  	message = SupplierUtil.updateAllProdsDiscountForSupNum(supNum, discount); 
  }
  supName = supName!=null?supName:"";
%>
<h1>Supplier Search</h1>
<p align="center">Search for a supplier and edit discount. <br />By setting a discount number and clicking update, 
		  you will set the discount for <b>ALL</b> products for this supplier.</p>
<center>
<FORM name="searchSupplierName" ACTION="./searchSupplier.jsp">
  <table border=0 cellspacing="3" cellpadding="3">
    <tr><td>Supplier:      </td><td>
        <select name="supId">
          <%for(Supplier sup: allSups){ 
            String sel = "";
            if(supId!=null&&supId.equals(sup.getSupplierNum())){
              sel = "SELECTED=SELECTED";
            }
          
          %>
          <option <%=sel%> value="<%= sup.getSupplierNum() %>"><%=sup.getSupplierName() %></option>
          <%} %>
        </select> 
        </td><td>
        <input type="submit" value="Go"/>
  </tr>
    
  </table>
</form>


		</td>
		</tr>
		<%
		if ( message != null ) {
		%>
		<tr><td colspan=3><font color="blue"><%=message%></font></td></tr>
		<%
		}
		%>
		</table>
		<%if(supplier!=null){ %>
<br>
<!-- modal content -->
	<div id="basic-modal-content">
      <table border="0" cellspacing="2">
        <tr><td align="center" colspan="3"><b>Edit Supplier #<%=supplier.getSupplierNum       () %></b></td> </tr>
        <tr><td align="right">Name       </td><td><input size="50" type="text" id="supplierName"       name="supplierName"          value="<%=supplier.getSupplierName      () %>"  /></td> </tr>
        <tr><td align="right">Contact    </td><td><input size="50" type="text" id="supplierContact"    name="supplierContact"       value="<%=supplier.getSupplierContact   () %>"  /></td> </tr>
        <tr><td align="right">Street     </td><td><input size="50" type="text" id="supplierStreet"     name="supplierStreet"        value="<%=supplier.getSupplierStreet    () %>"  /></td> </tr>
        <tr><td align="right">City       </td><td><input size="50" type="text" id="supplierCity"       name="supplierCity"          value="<%=supplier.getSupplierCity      () %>"  /></td> </tr>
        <tr><td align="right">State      </td><td><input size="30" type="text" id="supplierState"      name="supplierState"         value="<%=supplier.getSupplierState     () %>"  /></td> </tr>
        <tr><td align="right">PostalCode </td><td><input size="30" type="text" id="supplierPostalCode" name="supplierPostalCode"    value="<%=supplier.getSupplierPostalCode() %>"  /></td> </tr>
        <tr><td align="right">Country    </td><td><input size="30" type="text" id="supplierCountry"    name="supplierCountry"       value="<%=supplier.getSupplierCountry   () %>"  /></td> </tr>
        <tr><td align="right">Phone      </td><td><input size="30" type="text" id="supplierPhone"      name="supplierPhone"         value="<%=supplier.getSupplierPhone     () %>"  /></td> </tr>
        <tr><td align="right">Fax        </td><td><input size="30" type="text" id="supplierFax"        name="supplierFax"           value="<%=supplier.getSupplierFax       () %>"  /></td> </tr>
        <tr><td align="right">Email      </td><td><input size="50" type="text" id="supplierEmail"      name="supplierEmail"         value="<%=supplier.getSupplierEmail     () %>"  /></td> </tr>
        <tr><td align="right">Site       </td><td><input size="50" type="text" id="supplierSite"       name="supplierSite"          value="<%=supplier.getSupplierSite      () %>"  /></td> </tr>
        <tr><td colspan="2"><div class="invEval002" id="descriptionSUPPLIER"></div></td>
          <td align="right" colspan="1">
            <input type="button" value="Save"
               onclick="document.getElementById('descriptionSUPPLIER').innerHTML = '';makeGetRequestSupplierSave('<%=supplier.getSupplierNum()%>'
               ,document.getElementById('supplierName').value
               ,document.getElementById('supplierContact').value
               ,document.getElementById('supplierStreet').value
               ,document.getElementById('supplierCity').value
               ,document.getElementById('supplierState').value
               ,document.getElementById('supplierPostalCode').value
               ,document.getElementById('supplierCountry').value
               ,document.getElementById('supplierPhone').value
               ,document.getElementById('supplierFax').value
               ,document.getElementById('supplierEmail').value
               ,document.getElementById('supplierSite').value 
               ,'SUPPLIER'
                );" /> </td></tr>
      </table>  
	</div>
		
	<table class="common" id="viewInvoices" align="center" cellpadding="0"
		cellspacing="0" width="780">
		<tr>
			<th> &nbsp;#&nbsp; </th>
			<th> &nbsp;Name&nbsp; </th>
			<th> &nbsp;Number&nbsp; </th>
			<th> &nbsp;Phone&nbsp; </th>
			<th> &nbsp;Email&nbsp; </th>
			<th> &nbsp;Product Discount&nbsp; </th>
		</tr>
		<tr>
      <td align="center">
		    <div id='basic-modal'>
        <img width="20"
					style="cursor: pointer; cursor: hand; display: inline-block; vertical-align: middle"
					src="images/edit.gif" title="Edit supplier"
          value="Hist" class='basic'/>
		</div>
	
			</td>
			<td><%= supplier.getSupplierName()%></td>
			<td align="center"><%= supplier.getSupplierNum()%></td>
			<td><%= supplier.getSupplierPhone()%></td>
			<td><%= supplier.getSupplierEmail()%></td>
			<td align="center">
				<form action="./searchSupplier.jsp">
					<input type="text" name="discount" size="10">
					<input type="hidden" name="supName" value="<%=supName%>">
					<input type="hidden" name="supNum"
						value="<%=supplier.getSupplierNum()%>">
						<%if(isAdmin){ %>
					<input type="submit" value="Update"
						onclick="return confirm('Are you sure you want to update the discount for ALL products for this supplier?'); ">
						<%} %>
				</form>
			</td>
		</tr>
    <tr>
      <td colspan="3" align="left">
        <table width="100%" align="left" cellspacing="3" cellpadding="0">
          <tr><td colspan="3"> <b>Dollar Amount Threshold<b></td>
              </tr>
              <tr> <td align="right"><b>Murray</b></td><td><input type="text" id="murrayVal" value="<%=supData.getMurrayThr()!=null?supData.getMurrayThr():""%>"/>
                </td><td>
                <input onclick="makeGetRequestSupplierDataSave('<%=supplier.getSupplierNum()%>', 'MURRAY', document.getElementById('murrayVal').value);" type="image" style="display: inline-block; vertical-align: middle" title="Save  threshold for Murray" alt="Save" width="20" src="images/save.gif"> </td></tr>
              <tr> <td align="right"><b>Lehi</b></td><td><input type="text" id="lehiVal" value="<%=supData.getLehiThr()!=null?supData.getLehiThr():""%>"/>
                </td><td>
                <input onclick="makeGetRequestSupplierDataSave('<%=supplier.getSupplierNum()%>', 'LEHI', document.getElementById('lehiVal').value);" type="image" style="display: inline-block; vertical-align: middle" title="Save  threshold for Lehi" alt="Save" width="20" src="images/save.gif"> </td></tr>
              <tr> <td align="right"><b>DV</b></td><td><input type="text" id="oremVal" value="<%=supData.getOremThr()!=null?supData.getOremThr():""%>"/>
                <div class="invEval002" id="descriptionOREM"></div>
                </td><td>
                <input onclick="makeGetRequestSupplierDataSave('<%=supplier.getSupplierNum()%>', 'OREM', document.getElementById('oremVal').value);" type="image" style="display: inline-block; vertical-align: middle" title="Save threshold for DV" alt="Save" width="20" src="images/save.gif"> </td></tr>
            <tr><td colspan="3">
                <div class="invEval002" id="descriptionLOCATION">&nbsp;</div>
            </td></tr>
          </table>
          </td><td colspan="5">
        <table width="100%" align="center" cellspacing="3" cellpadding="0">
              <tr>
      <td align="left"><b>Email Text<b></td>
          </tr><tr>
      <td colspan="5"><textarea rows="5" cols="50" id="EContent" name="supEContent"><%=supData!=null&&supData.getEContent()!=null?supData.getEContent():""%></textarea>
      <div class="invEval002" id="descriptionEContent"></div>
      <input onclick="makeGetRequestSupplierDataSave('<%=supplier.getSupplierNum()%>', 'EContent', document.getElementById('EContent').value);" 
      	type="image" style="display: inline-block; vertical-align: middle" title="Save price" alt="Save" width="20" src="images/save.gif"> </td></tr>
   
    </tr>
  </table>
</td>
</tr>
	</table>
			<% }else{%><center>
				Enter supplier name for search.
			</center>
			<%}//end of else %>


	<%@include file="bottomLayout.jsp"%>
