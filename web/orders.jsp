
<%@page import="com.soward.object.Order"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.soward.util.OrderUtils"%>
<%@page import="com.soward.util.SendEmail"%>
<%@page import="com.soward.object.SupplierData"%>
<%@include file="jspsetup.jsp"%>

<jsp:directive.page import="com.soward.object.Supplier"/>
<jsp:directive.page import="com.soward.util.SupplierUtil"/>

<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.TransUtil"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<jsp:directive.page import="com.soward.enums.LocationsDBName"/>
<script type="text/javascript" src="js/inventoryAjax.js"></script>

<!-- Load jQuery, SimpleModal and Basic JS files -->
<script type='text/javascript' src='js/jquery.js'></script>
<script type='text/javascript' src='js/jquery.simplemodal.js'></script>
<script type='text/javascript' src='js/basic.js'></script>
<link type='text/css' href='css/basic.css' rel='stylesheet' media='screen' />
<SCRIPT>
    function checkArrows (field, evt) {
        var keyCode =
                document.layers ? evt.which :
                        document.all ? event.keyCode :
                                document.getElementById ? evt.keyCode : 0;
        //var r = '';
        if (keyCode == 40){
            //r += 'arrow down';
            document.getElementById('productOptions').focus();
        }
        return true;
    }
</SCRIPT>

<%
    String message = request.getParameter( "message" );
    String supId = request.getParameter( "supId" );
    //email start
    String eContent = request.getParameter( "eContent" );
    String eSubject = request.getParameter( "eSubject" );
    String eTo      = request.getParameter( "eTo" );
    String eCc      = request.getParameter( "eCc" );
    String sendEmail= request.getParameter( "sendEmail" );
    String reset= request.getParameter( "locationNameReset" );
    String locationName= request.getParameter( "locationName" );
    if(!StringUtils.isBlank(reset)){
        locationName = reset;
        supId = null;
    }
    HashMap<String, String> orderSumBySupNum = new HashMap<String,String>();
    ArrayList<Supplier> allSups = new ArrayList<Supplier>();
    ArrayList<Order> oList = new ArrayList<Order>();
    Supplier supplier = null;
    if(!StringUtils.isBlank(locationName)||!StringUtils.isBlank(sendEmail)){
        boolean eSuccess = false;
        if(!StringUtils.isBlank(sendEmail)){
            eSuccess = SendEmail.sendOrderEmail(eSubject, eContent, eTo, eCc);
            message = "Email failed to send";
            if(eSuccess){
                OrderUtils.updateSupForEmailSent(supId, locationName, username);
                message = "Email sent successfully";
            }
        }
        orderSumBySupNum = OrderUtils.getOrderSumsBySupNum(locationName);
        allSups = SupplierUtil.getAllSuppliersNumAndName();
        //email end
        if(!StringUtils.isBlank(supId)){
            supplier =  SupplierUtil.fetchSupForNameOrNum(supId).get(0);
            oList = OrderUtils.getAllForSup(supId, true, true, locationName);
        }
    }
%>
<br/>
<h1>Supplier Orders</h1>
<br />
<center>
    <table border="0" cellspacing="3" cellpadding="3">
        <form name="myForm" action="./orders.jsp" method="post">
            <tr>
                <td  align="right">
                    Store:
                </td><td align="left">
                <select onChange="document.myForm.submit();" name="locationNameReset" value="test">
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
            </td></tr>
        </form>
        <form action="./orders.jsp" method="post">
                <% if(!StringUtils.isBlank(locationName)){%>
            <tr><td>Supplier:      </td><td>
                <select name="supId">
                    <%
                        ArrayList<String> wCount = new ArrayList<String>();
                        ArrayList<String> wOCount = new ArrayList<String>();

                        String pink = "style=\"background-color: #ffcccc;\"";;
                        for(Supplier sup: allSups){
                            String sel = "";
                            String nn = "";
                            String ccas = "";
                            double nnn = 0.0;
                            if(orderSumBySupNum.containsKey(sup.getSupplierNum())){

                                SupplierData sd = SupplierUtil.fetchSuppliersData(sup.getSupplierNum());
                                nn = orderSumBySupNum.get(sup.getSupplierNum());

                                if(!StringUtils.isBlank(nn)){
                                    try{
                                        nnn = Double.parseDouble(nn);
                                        nn = bdec.format(nnn);
                                        if(sd!=null){
                                            if(locationName.equals("MURRAY")){
                                                if(nnn>sd.getMurrayThr()){
                                                    ccas = pink;
                                                }
                                            }
                                            if(locationName.equals("LEHI")){
                                                if(nnn>sd.getLehiThr()){
                                                    ccas = pink;
                                                }
                                            }
                                            if(locationName.equals("OREM")){
                                                if(nnn>sd.getOremThr()){
                                                    ccas = pink;
                                                }
                                            }
                                        }
                                    }catch(Exception e){e.printStackTrace();}
                                }
                            }
                            if(supId!=null&&supId.equals(sup.getSupplierNum())){
                                sel = "SELECTED=SELECTED";
                            }
                            if(nnn<1){
                                nn = "";
                            }
                            String s = "<option "+sel+" "+ccas+" value="+sup.getSupplierNum()+">"+nn +" "+ sup.getSupplierName() +"</option>";
                            if(nnn<1){
                                wOCount.add(s);
                            }else{
                                wCount.add(s);
                            }
                        }
                        wCount.addAll(wOCount);
                    %>
                    <option></option>

                    <%
                        for(String wco: wCount){%>
                    <%=wco %>
                    <%} %>
                </select>
            </td><td>
                <input type="hidden" value="<%=locationName%>" name="locationName" />
                <input type="submit" class="btn" value="Go"/>
            </td>
            </tr>
                <%}%>

    </table>
    </form>
    <%if(supplier!=null){ %>
    <div id='basic-modal'>
        <img style="cursor: pointer; cursor: hand; display: inline-block; vertical-align: middle"
             src="images/edit.gif" title="Edit supplier"
             value="Hist" class='basic btn1'/>
    </div>
    <div id="orderTable">
        <table class="common" style="width:50%" border="1" cellspacing="0" cellpadding="3">
            <tr>
                <th>Delete</th>
                <th>Cat#</th>
                <th>Name#</th>
                <th>Count#</th>
                <th>Edit</th>
            </tr>
            <%for(Order o: oList){ %>
            <tr>
                <td align="center"><img style="cursor: pointer; cursor: hand; display: inline-block; vertical-align: middle"
                                        onclick="makeGetRequestDeleteProductSupplierOrder('<%=supplier.getSupplierNum()%>', '<%=o.getId()%>', '<%=locationName %>');"
                                        src="images/delete.gif" title="Delete order for product <%=o.getProdNum() %>" />
                </td>
                <%
                   String pName = o.getProd()!=null?o.getProd().getProductName():"";
                    String pCat = o.getProd()!=null?o.getProd().getProductCatalogNum():"";

                %>
                <td align="center"><%=pCat %></td>
                <td align="center"><%=pName %></td>
                <td align="center"><div id="<%=o.getId()+"orderCount" %>"><%=o.getCount() %></div></td>
                <td align="center">
                    <img width="20" style="cursor: pointer; cursor: hand; display: inline-block; vertical-align: middle"
                         onclick="makeGetRequestSupplierOrderCountSave('<%=supplier.getSupplierNum()%>', '<%=o.getId()%>','<%=1%>', '<%=locationName %>');"
                         src="images/go-up.gif" title="Increase order for product <%=o.getProdNum() %>" />
                    <img width="20" style="cursor: pointer; cursor: hand; display: inline-block; vertical-align: middle"
                         onclick="makeGetRequestSupplierOrderCountSave('<%=supplier.getSupplierNum()%>', '<%=o.getId()%>','<%=-1%>', '<%=locationName %>');"
                         src="images/go-down.gif" title="Decrease order for product <%=o.getProdNum() %>" />
                </td>
            </tr>
            <%} %>
        </table>
    </div>
    <br/>
    <%if(oList!=null&&!oList.isEmpty()){ %>
    <input type="button"  id="deleteAllBtn"
           onclick="makeGetRequestDeleteAllProductSupplierOrder(this, '<%=supplier.getSupplierName()%>', '<%=supplier.getSupplierNum()%>', '<%=locationName %>');"
           value="Clear All" class="btn btn1" />
    <%}%>
    <br/>
    <br/>
    <input type="hidden" id="hiddenProdId" name="" value=""/>
    Add Product <input type="text" size="50" id="addNewProd" value=""
                       ONKEYDOWN="checkArrows(this, event)" onkeyup="if(this.value.length>2){document.getElementById('newSupProd').innerHTML='';makeGetRequestShowSupProdOptions(this.value, '<%=supplier.getSupplierNum()%>')}"/>
    <input class="btn" type="button" id="newProdAdd" onclick="if( document.getElementById('hiddenProdId').value.length>0){document.getElementById('addNewProd').value='';makeGetRequestAddNewProduct('<%=supplier.getSupplierNum()%>', document.getElementById('hiddenProdId').value, '<%=locationName %>');}else{alert('Type a product name or number for the selected supplier.')}" value="Add" />
    <div id="newSupProd"> </div>
    <br/>
    <div id='basic-modal1'>
        <input type="button" class="basic btn" onclick="makeGetRequestShowSupOrdersEmail('<%=supplier.getSupplierNum()%>', '<%=locationName %>', '<%=username %>')" value="Send Email" />
    </div>

    <div id="basic-modal1-content">
    </div>
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
                    <input type="button" class="btn" value="Save"
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
    <%} %>
</center>
<% if ( message != null ) { %>
<td><p class="text"><%=message%></p></td>
<% } %>
</tr>
</table>

<script type="text/javascript">
    //onChange=\"document.getElementById('addNewProd').value=this.value;document.getElementById('newSupProd').innerHTML=''\"
    var selectmenu=document.getElementById("productOptions")
    if(selectmenu!=null){
        selectmenu.onchange=function(){ //run some code when "onchange" event fires
            document.getElementById('addNewProd').value=this.value;
            document.getElementById('newSupProd').innerHTML=''
        }
    }

</script>

<%@include file="bottomLayout.jsp"%>
