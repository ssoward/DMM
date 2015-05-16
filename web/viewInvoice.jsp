<%@include file="jspsetup.jsp"%>


<%@ page import="com.soward.enums.DeleteInvoiceEnum" %>
<%@ page import="com.soward.enums.TransTypeEnum" %>
<%@ page import="com.soward.object.AccountType" %>
<%@ page import="com.soward.object.Invoice" %>
<%@ page import="com.soward.object.Location" %>
<%@ page import="com.soward.object.Transaction" %>
<%@ page import="com.soward.util.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<script type='text/javascript' src='js/jquery.js'></script>
<script type='text/javascript' src='js/jquery.simplemodal.js'></script>
<script type='text/javascript' src='js/basic.js'></script>
<link type='text/css' href='css/basic.css' rel='stylesheet' media='screen' />
<script type="text/javascript" src="js/invoiceUtilAjax.js"></script>

<%
    TransUtil tu = new TransUtil();
    String message = request.getParameter( "message" );
    String pid = request.getParameter( "pid" );
    Invoice inv = null;
    if(!StringUtils.isBlank(pid)){
        inv = InvoiceUtil.getForPid(pid, true);
    }
%>

<%if(inv!=null){
    List<Transaction> transList = inv.getTransList();
    Location loc = RegLocationUtil.fetch(inv.getLocationNum());
    List<AccountType> accTListAll = AccountTypesUtil.getAccountTypes();
    Map<String, String> paymentTypes = SalesReport.getPaymentTypeHash();
%>

<div class="ajaxResponse" id="description"></div>

<table cellpadding=0 cellspacing=9  border=0 align=center>
    <FORM name="getInvoiceNum" method="post" ACTION="./viewInvoice.jsp">
        <tr>
            <td align=right>
                Get Invoice:
                <input type="text" name="pid">
            </td>
            <td>
                <input type="submit" class="btn" value="Get Invoice"/>
            </td>
        </tr>
    </FORM>
</table>
<!-- modal content -->
<div id="basic-modal-content">
    <div id="deleteInvoiceModal">
        <div id="modalLabel">Reverse Invoice <%="#"+pid%></div>
        <div><input type="hidden" id="invoiceNum" value="<%=pid%>"/></div>
        <div class="modalElement">
            <div class="label"      id="label">Current User:</div>
            <div class="labelValue" id="labelValue"><%=username%></div>
        </div>
        <div class="modalElement">
            <div class="label">Reverse Product Counts:</div>
            <div class="labelValue deleteModalCheckbox" >
                <input type="checkbox" checked="checked" value="reverseCheckbox" id="reverseCheckbox"/></div>
        </div>
        <div class="modalElement">
            <div class="label"      id="reasonLabel">Reason:</div>
            <div class="labelValue" >
                <select id="reason" name="reason">
                    <%for(DeleteInvoiceEnum ed: DeleteInvoiceEnum.values()){%>
                    <option name="<%=ed.getId()%>"><%=ed.getName()%></option>
                    <%}%>
                </select>
            </div>
        </div>
        <div class="modalElement" id="addNotes">
            <div class="label"      id="addLabel">Additional Notes:</div>
            <div class="labelValue">
                <textarea rows="5" id="text" cols="45"></textarea>
            </div>
        </div>
        <div style="clear: both;"></div>
        <div id="delButton">
            <input type="button" onclick="deleteInvoice()" class="btn" value="Reverse"/>
            <a class="cancelLink" href="#" onclick="closeModal()">cancel</a>
        </div>
        <div class="modalElement">
            <div class="warning">Warning: This action adds quantity back to inventory, deletes the invoice, and can not be undone.</div>
        </div>
    </div>
</div>


<div id='basic-modal' class="delInvoice">
    <img style="cursor: pointer; cursor: hand; display: inline-block; vertical-align: left"
         src="images/delete.gif" title="Delete this Invoice"
         onclick=""
         value="Hist" class='basic'/>
    <div>
        <div id="smallNotes"></div>
    </div>
</div>

<table width="754" border="0" align="center" cellpadding="2" cellspacing="2">
    <tr><td></td></tr><tr><td><br><br></td></tr><tr><td>
    <table width="400px" align=center border="1">
        <tr><td>

            <table align=center bgcolor="" width="100%" border=0>
                <tr><td>&nbsp;</td></tr>
                <tr align=center> <td colspan=8><font size=4 color=>View invoice for #
                    <font color=red><%=pid %></font></font></td>
                <tr width="75%"><td colspan=8><hr></td>
                </tr>
                </tr>
                <tr><td colspan=8>
                    <table align=center border="0">
                        <%
                            String orderType = "";
                            if(inv!=null&&inv.getTransList()!=null&&!inv.getTransList().isEmpty()){
                                Transaction transaction = inv.getTransList().get(0);
                                if(transaction.getTransType()!=null){
                                    TransTypeEnum enu = TransTypeEnum.getValueForCode(transaction.getTransType());
                                    if(enu!=null){
                                        orderType = enu.label;
                                    }
                                }
                            }
                        %>

                        <tr><td>Clerk:     </td><td><%=inv.getUsername2()%></td></tr>
                        <tr><td>Register:  </td><td><%=" "+loc.getLocationIP()+" ("+inv.getLocationNum()+")"%></td>
                        <tr><td>Order Type:</td><td><%=orderType%></td></tr>
                        <tr><td>Invoice:   </td><td><%=inv.getInvoiceNum()%></td>
                        <tr><td>Date:      </td><td><%=Utils.dp(inv.getInvoiceDate())%></td>
                        <%if(inv.getAccount() != null){%>
                        <tr><td><%=inv.getAccount().getAccountName()%></td><td><a href="./editAccounts.jsp?pid=<%= inv.getAccountNum()%>"><%= inv.getAccountNum()%></a></td></tr>
                        <tr><td><%=inv.getAccount().getAccountStreet()%></td></tr>
                        <tr><td><%=inv.getAccount().getAccountCity()%><%=", "+inv.getAccount().getAccountState()%><%=" "+inv.getAccount().getAccountZip()%></td></tr>
                        <tr><td><%=inv.getAccount().getAccountPhone1()%></td></tr>
                        <%}else{%>
                        <tr><td>Missing Account Info</td></tr>
                        <%}%>
                        </tr><td></table>
                <tr align=center><td colspan=8>
                    <table>
                        <tr><td colspan=8>
                            <table width="575px" align=center cellpadding="0" cellspacing="0" border=1>
                                <tr>
                                    <th class="table-header">Catalog Num</th>
                                    <th class="table-header">Name       </th>
                                    <th class="table-header">Qty        </th>
                                    <th class="table-header">Price      </th>
                                    <th class="table-header">Total      </th>
                                </tr>
                                <%

                                    double totalDiscountOfInvoice = 0.0;
                                    for(Transaction trans: transList){

                                        double prodCost = Utils.parseDouble(trans.getProd().getProductCost1());
                                        double transTax = Utils.parseDouble(trans.getTransTax());
                                        double transCost = Utils.parseDouble(trans.getTransCost());
                                        int productQty = Utils.parseInt(trans.getProductQty());
                                        String productNum = trans.getProductNum();
                                        double diff = 0.0;
                                        double discountCost = prodCost;
                                        double discount = 0.0;
                                        boolean discountPrint = false;

                                        if((prodCost - Utils.roundUp((transCost - transTax)/productQty))>0 && !productNum.equals("104749") && !productNum.equals("314")){
                                            diff = prodCost - Utils.roundUp((transCost - transTax)/productQty);
                                            discountCost = Utils.roundUp((transCost - transTax)/productQty);
                                            discount = Utils.roundUp((prodCost - ((transCost - transTax)/productQty))/ prodCost) * 100;
                                            discountPrint = true;
                                        }
                                        totalDiscountOfInvoice = totalDiscountOfInvoice + (diff * productQty);

                                        int qty = 0;
                                        double nonTaxTotal = 0.0;
                                        try{ qty = Integer.parseInt(trans.getProductQty());}catch(Exception e){}
                                        try{ nonTaxTotal = qty * Double.parseDouble(trans.getProd().getProductCost1());}catch(Exception e){}
                                        String noTax = Utils.fD(nonTaxTotal);
                                %>
                                <tr>
                                    <td class="table-element"><%=trans.getProd().getProductCatalogNum()%></td>
                                    <td class="table-element"><%=trans.getProductName()%>
                                        <%
                                            if(discountPrint){%>
                                        <%="<br/>"+Utils.fP(discount)+"% disc, was "+ Utils.fD(prodCost) +", now "+Utils.fD(Utils.roundDown(discountCost))%>
                                        <%  } %>
                                    </td>
                                    <td class="table-element"><%=qty%></td>
                                    <td class="table-element"><%=Utils.fD(Utils.roundDown(discountCost))%></td>
                                    <td class="table-element"><%=Utils.fD(Utils.parseDouble(trans.getTransCost())-Utils.parseDouble(trans.getTransTax()))%></td>
                                </tr>
                                <%} %>
                            </table></td></tr>

                        <tr align=right><td colspan=8>
                            <table border=0>
                                <%
                                    double sub =
                                            Utils.parseDouble(inv.getInvoiceTotal()) -
                                            Utils.parseDouble(inv.getInvoiceTax()) -
                                            Utils.parseDouble(inv.getInvoiceShipTotal());
                                    String taxType = "";
                                    for(AccountType at: accTListAll){
                                        if(inv.getAccount()!=null&&at.getTypeCode().equals(inv.getAccount().getAccountType2())){
                                            taxType = at.getTypeDescription();
                                        }
                                    }
                                    String payMeth1 = "";
                                    payMeth1 = paymentTypes.get(inv.getPaymentMethod1());
                                    payMeth1 = payMeth1!=null?payMeth1:"";
                                    String payMeth2 = "";
                                    payMeth2 = paymentTypes.get(inv.getPaymentMethod2());
                                    payMeth2 = payMeth2!=null?payMeth2:"";

                                %>
                                <tr align="right">   <td colspan="8">Sub Total:             </td><td> <%=Utils.fD(sub)%>                      </td></tr>
                                <tr align="right">   <td colspan="8"><%=taxType%>:          </td><td> <%=Utils.fD(inv.getInvoiceTax())%>      </td></tr>
                                <tr align="right">   <td colspan="8">Shipping:              </td><td> <%=Utils.fD(inv.getInvoiceShipTotal())%></td></tr>
                                <tr align="right">   <td colspan="8">&nbsp;                 </td><td> <hr>                                    </td></tr>
                                <tr align="right">   <td colspan="8">Grand Total:           </td><td> <%=Utils.fD(inv.getInvoiceTotal())%>    </td></tr>
                                <tr align="right">   <td colspan="8">&nbsp;                 </td><td> <hr>                                    </td></tr>
                                <tr align="right">   <td colspan="8">Total Discount Savings:</td><td> <%=Utils.fD(totalDiscountOfInvoice)%>   </td></tr>
                                <tr align="right">   <td colspan="8"><%=payMeth1%>:          </td><td> <%=Utils.fD(inv.getInvoicePaid1())%>    </td></tr>
                                <%if(!StringUtils.isBlank(payMeth2)){%>
                                <tr align="right">   <td colspan="8"><%=payMeth2%>:          </td><td> <%=Utils.fD(inv.getInvoicePaid2())%>    </td></tr>
                                <%}%>
                                <tr align="right">   <td colspan="8">Reference Number:      </td><td> <%=inv.getInvoiceReferenceNum()!=null?inv.getInvoiceReferenceNum():""%>       </td></tr>
                                <tr align="right">   <td colspan="8">-----------------------</td><td> <hr>                                    </td></tr>
                                <tr align="right">   <td colspan="8">Amount Received:       </td><td> <%=Utils.fD(inv.getInvoicePaid1())%>    </td></tr>
                                <tr align="right">   <td colspan="8">Change Due:            </td><td> <%=Utils.fD(Utils.parseDouble(inv.getInvoicePaid1()) - Utils.parseDouble(inv.getInvoiceTotal()))%>     </td></tr>
                            </table> </td></tr>
                    </table> </td></tr>


            </table></td></tr>
    </table>
</td></tr>
    <%} else{
        response.sendRedirect("./searchInvoices.jsp?message=No invoices match your criteria.");

    %>

    <center>No invoice found for pid: <%=pid %></center>
    <%} %>
    </td>
    <%
        if ( message != null ) {
    %>
    <td><%=message%></td>
    <%
        }
    %>
    </tr>
</table>

<%@include file="bottomLayout.jsp"%>

