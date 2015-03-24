<%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>

<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.soward.object.Account"%>

<%@page import="com.soward.util.AccountUtil"%>
<%@page import="com.soward.object.Supplier"%>
<%@page import="com.soward.util.SupplierUtil"%>

<%@page import="com.soward.object.SpecialOrderGto"%>
<%@page import="com.soward.util.SpecialOrderUtil"%><script type="text/javascript" src="js/accountSearchAjax.js"></script>
<script type="text/javascript" src="js/prodSearchAjax.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="css/_CalendarPopup.js"></SCRIPT>
<script type="text/javascript" src="js/comboBox.js"></script>
<script type='text/javascript' src='js/jquery.simplemodal.js'></script>
<script type='text/javascript' src='js/basic.js'></script>
<script type='text/javascript' src='js/dailyOrder.js'></script>
<link type='text/css' href='css/basic.css' rel='stylesheet' media='screen' />
<link type='text/css' href='css/dailyOrder.css' rel='stylesheet' media='screen' />
<script type="text/javascript" language="javascript" src="js/jquery.validVal-2.3.2.js"></script>
<script type="text/javascript" language="javascript" src="js/jquery.validVal-customValidations.js"></script>

<script type="text/javascript" language="javascript">
    $(function() {
        $('form').validVal();
    });
</script>


<style>
    @media screen and (-webkit-min-device-pixel-ratio:0) { /* hacked for chrome and safari */
        .onehundredpercent { width: 97%; }
    }
</style>
<SCRIPT LANGUAGE="JavaScript"> var cal = new CalendarPopup(); </SCRIPT>

<script type="text/javascript">
    $(function() {
        $("#tabs").tabs();
    });
</script>

<SCRIPT>
    function isDown (evt) {
        var keyCode =
                document.layers ? evt.which :
                        document.all ? event.keyCode :
                                document.getElementById ? evt.keyCode : 0;
        if (keyCode == 40){
            return true;
        }
        return false;
    }
</SCRIPT>


<%
    ArrayList<Supplier> llSups = SupplierUtil.getAllSuppliersNumAndName();
    List<SpecialOrderGto> preOrderList = SpecialOrderUtil.getAllOrders();
    String message = request.getParameter( "message" );
    String san = request.getParameter( "san" );
    String sval = StringUtils.trimToEmpty(request.getParameter( "sval" ));
    try{
        if(!StringUtils.isBlank(sval)){
            san = (StringUtils.isBlank(sval)?"":sval.substring(0,sval.indexOf(":")));
            ArrayList<Account> aList = AccountUtil.searchAccounts( san );
            request.getSession().setAttribute( "searchHash", AccountUtil.createMap(aList));
        }
        san = san.trim();
    }catch(Exception e){}
    Account account = null;
    HashMap<String, Account> aMap = null;
    if(!StringUtils.isBlank(san)){
        try{
            aMap = (HashMap)request.getSession().getAttribute("searchHash");
        }catch(Exception e){}
        try{
            account = (Account)aMap.get(san);
        }catch(Exception e){}
    }
%>
<%!
    ArrayList<Supplier> llSups = SupplierUtil.getAllSuppliersNumAndName();
    public String createTab(String tabName){
        String l = "";

        l += "<table cellpadding=\"2\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"customerTable\">                           ";
        l += "         <tr>                                                                                                      ";
        l += "           <td align=\"right\" colspan=\"10\" style=\"padding: 10px 10px 10px 0;\">                                                                     ";
        l += "               <b>Publisher</b>  &nbsp; &nbsp;                                                                     ";
        l += "                 <select id=\"productSupplier00"+tabName+"\"  onchange=\"fetchItemNumbers(this, '"+tabName+"');\"> ";
        l += "                   <option value=\"\"></option>                                                                    ";
        for(Supplier sup: llSups){
            if(sup.getSupplierName()!=null&&sup.getSupplierName().length()>1){
                l += "                   <option value=\""+sup.getSupplierNum()+"\">"+sup.getSupplierName() +"</option>                 ";
            }
        }
        l += "                 </select>                                                                                          ";
        l += "           </td>                                                                                                    ";
        l += "         </tr>                                                                                                      ";
        l += "         <tr> <th width=\"5%\" ><img width=\"20\" src=\"images/verified.png\"></th><th width=\"10%\">Voicing</th>";
        l += "<th width=\"5%\">Item#</th> <th width=\"5%\" >Qty</th> <th>Title</th><th width=\"10%\">Total</th><th>Status</th> ";
        l += "<th width=\"5%\" ><img width=\"20\" src=\"images/emailed.png\"></th></tr> ";
        l +=         cTr(tabName);
        l += "       </table>                                                                                                     ";
        return l;
    }



    public String cTr(String a){

        String l = "";
        //l += "\n<select id=\"itemSelec00"+o+""+a+"\" onchange=\"operItemSelected(this,"+o+",'"+a+"')\"><option value=\"\">Select Publisher</option></select></td>";
        for(int o = 1; o < 10; o++){
            l += "\n<tr>";
            l += "\n<td align=\"center\" ><input size=\"15\"type=\"checkbox\" id=\"verified00"+o+""+a+"\"/></td>";
            l += "\n<td><input size=\"25\"type=\"text\" id=\"voicing00"+o+""+a+"\"/></td>";
            l += "\n<td><div id=\"itemSelection00"+o+""+a+"\"></div>";
            l += "\n<input type=\"hidden\" id=\"prodNum00"+o+""+a+"\"/>";
            l += "\n<input type=\"hidden\" id=\"prodOrderId00"+o+""+a+"\"/>";
            l += "\n<input size=\"20\" type=\"text\" style=\"display:block;\" onclick=\"this.value='';\" id=\"itemTextSelection00"+o+""+a+"\" "+
                    " ONKEYDOWN=\"if(isDown(event)){document.getElementById('itemDescSel00"+o+""+a+"').focus();}\" " +
                    " onkeyup=\"if(!(isDown(event))&&this.value.length>3){makeGetRequestItem(this.value,"+o+",'"+a+"')};\"></input>";
            l += "\n<div id=\"itemDesc00"+o+""+a+"\"></div>";
            l += "\n<select id=\"itemSelec00"+o+""+a+"\" style=\"display:none\" onchange=\"operItemSelected(this,"+o+",'"+a+"')\"><option value=\"\">Select Publisher</option></select></td>";
            l += "\n<td><select onchange=\"qtySelect(this, "+o+",'"+a+"');\" id=\"quanity00"+o+""+a+"\" >";
            for(int i = 0; i< 100; i++){
                l += "<option value=\""+i+"\">"+i+"</option>";
            }
            l += "</td><td align=\"center\"><input size=\"40\" type=\"text\" id=\"title00"+o+""+a+"\"  /></td>";
            l += "<input type=\"hidden\" id=\"priceHide00"+o+""+a+"\"  />";
            l += "<td align=\"center\"><div id=\"price00"+o+""+a+"\" /></div></td>";
            l += "<td><select width=\"10px\" id=\"itemStatus00"+o+""+a+"\" />";
            l += "<option value=\"null\"></option>";
            l += "<option value=\"top\">T.O.P.</option>";
            l += "<option value=\"pop\">P.O.P</option>";
            l += "<option value=\"bo\">B.O.</option>";
            l += "<option value=\"recieved\">Recieved</option>";
            l += "<option value=\"ordered\">Ordered</option>";
            l += "</select></td>";
            l += "\n<td align=\"center\" ><input size=\"15\"type=\"checkbox\" id=\"itemEmailed00"+o+""+a+"\"/></td>";
            l += "</tr>";
        }
        l += "<tr><td colspan=\"5\" align=\"right\" >Estimated Total</td><td align=\"center\"><div id=\"priceTotal00"+a+"\">$000.00</div></td><td colspan=\"2\"></td></tr>";
        return l;
    }
%>

<br/>
<h1>Special Order</h1>
<% if ( message != null ) { %>
<td align="center"><font color="red"><div id="message"><%=message%></div></font></td>
<% } System.out.println("specialOrderUser: " + tempUser); %>
<br/>
<div id="page">
<div id="recreateOrder">
    <select id="prevOrder">
        <%
            for(SpecialOrderGto sgto: preOrderList){
        %>
        <option value="<%=sgto.getId() %>"><%=sgto.getAccountName()!=null?sgto.getAccountName():sgto.getId()+" No Account" %></option>
        <%}%>
        <input id="loadOrder" class="btn" type="button" onclick="loadOrder();" value="Get Order"/>
    </select>

</div>
<div id="newSpecialOrder" class="divLink" onclick="newSpecialOrder();">Clear Order</div>
<div id="specialOrder">
    <tr>
        <td>
            <table width="70%" border="0" cellpadding="0" cellspacing="0" style="margin: 0 0 10px 10px;">
                <tr> <td><b>Ordered Date:</b>&nbsp;</td><td align="left"> <div id="today" ><%=new Date()%></div></td>
                    <td align="right"><b>Created by:</b>&nbsp; </td><td align="left"><div id="createdBy" ><%=tempUser%></div></td> </tr>
            </table>
        </td>
    <tr>
        <td>
            <div class="rcgrad tableBorder" >
                <table cellpadding="2" cellspacing="0" border="0" class="plain">
                    <tr>
                        <td width="12%" class="tlabel"> <b>Order Method</b></td>
                        <td width="25%">
                            <select id="orderBy" class="required"/>
                            <option value="person">Person</option>
                            <option value="phone" >Phone </option>
                            <option value="email" >Email </option>
                            <option value="web"   >Web   </option>
                            <option value="fax"   >Fax   </option>
                            <option value=""></option>
                            </select>
                        </td>
                        <td class="tlabel" width="12%">
                            <b>Ship Method</b>
                        </td>
                        <td width="25%">
                            <select name="shipMethod" class="required" id="shipMethod">
                                <option></option>
                                <optGroup label="USPS">
                                    <option value="1">Priority 2-3 Days</option>
                                    <option value="2">1st Class 3-5 Days</option>
                                    <option value="3">Express 1-2 Days</option>
                                </optGroup>
                                <optGroup label="Fed-Ex">
                                    <option value="4">Ground 3-6 Days</option>
                                    <option value="5">2 Day 2 Days</option>
                                    <option value="6">Next Day Overnight</option>
                                </optGroup>
                            </select>
                        </td>
                        <td class="tlabel" width="12%">
                            <b>Order Status</b>
                        </td>
                        <td width="25%">
                            <select id="orderStatus" class="required">
                                <option value="new">New Order</option>
                                <option value="progress">In Progress</option>
                                <option value="part">Partial</option>
                                <option value="all">Received All</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="tlabel">
                            <b>Drop Ship</b>
                        </td>
                        <td>
                            <select id="droppedShip" class="required" />
                            <option value="" >                    </option>
                            <option value="overnight" >Overnight  </option>
                            <option value="2Days"     >2 Days     </option>
                            <option value="3Days"     >3 Days     </option>
                            <option value="ground"    >Ground     </option>
                            <option value="firstClass">First Class</option>
                            <option value="priority"  >Priority   </option>
                            </select>
                        </td>
                        <td class="tlabel" id="showPickUp">
                            <b>Pick Up At</b>
                        </td>
                        <td>
                            <select id="pickUp" class="required" />
                            <option value="murray"  >Murray  </option>
                            <option value="orem"    >Orem    </option>
                            <option value="lehi"    >Lehi    </option>
                            <option value="mail"    >Mail    </option>
                            </select>
                        </td>
                        <td class="tlabel">
                            <b>Order Notes</b>
                        </td>
                        <td>
                            <div id='basic-modal'>
                                <img style="cursor: pointer; cursor: hand; display: inline-block; vertical-align: left"
                                     src="images/edit.gif" title="Create shipping notes"
                                     onclick="document.getElementById('shipNts').value=document.getElementById('shipNotes').value;"
                                     value="Hist" class='basic'/>
                                <div>
                                    <div id="smallNotes"></div>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="tlabel">
                            <b>Order At</b>
                        </td>
                        <td>
                            <select id="droppedLocation" class="required" />
                            <option value="murray"  >Murray  </option>
                            <option value="orem"    >Orem    </option>
                            <option value="lehi"    >Lehi    </option>
                            </select>
                        </td>
                        <td class="tlabel">
                            <b>Delivery</b>
                            <A HREF="#" onClick="cal.select(document.getElementById('dateOne'),'anchor','MM-dd-yyyy'); return false;" NAME="anchor" ID="anchor">
                                <img width="25" src="images/calendar.jpg" border="0"/></A>

                        </td>
                        <td><div size="7" type="text" value="" id="dateOne">12-01-2999</div></td>

                        <td></td>
                        <td></td>

                    </tr>
                </table>

            </div>
            <table class="rcgrad tableBorder onehundredpercent" >
                <tr>
                    <td colspan="7">
                        <table width="100%" cellpadding="3" cellspacing="1" border="0" align="left">
                            <tr>
                                <td class="tlabel" width="70%" colspan="2">
                                    <h3 style="display: inline;">Customer: </h3>
                                    <input type="text" id="searchString" name="sval" size="35" class="roundlarge" value="Search ..." onfocus="if (this.value == 'Search ...') {this.value = '';}" onblur="if (this.value == '') {this.value = 'Search ...';}"
                                           ONKEYDOWN="if(isDown(event)){document.getElementById('productOptions').focus();}" onclick="this.value='';resetAccount()"
                                           onkeyup="if(!(isDown(event))&&this.value.length>3){makeGetRequest(this.value)};" value="<%=sval %>" />
                                    <input type="hidden" id="selectedAccountNum" value="" name="san" />
                                    <input type="hidden" id="selectedAccountName" value="" name="san" />
                                    <input type="hidden" id="selectedSpecialOrderId" value="" name="san" />
                                    <input type="hidden" id="createdByCurrentUser" value="<%=tempUser %>" name="san" />
                                </td>
                                <td colspan="2" align="left">
                                    <input type="button" onclick="toggleAcctEdit(1);resetAccount();" title="Create a new Account" value="New Account" class="btn" />
                                </td><td>
                                <div type="button" value="New Prod"
                                     class="divLink" title="Create new product" onclick="jsfPopupPage('./newProd.jsp?popup=asdf', 800, 500);" >New Prod</div>
                            </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td align="left" colspan="1"><div id="description"></div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="accountDisplay"></div>
                        <table  width="100%" style="display:none" class="editAccount" id="editAccount" >
                            <tr>
                                <td align="right" width="15%"><b>Name</b></td>
                                <td colspan="1"><input  id="accountName" name="name" size="35" /></td>
                                <td align="right"><b>Acct#</b>  </td>
                                <td><div id="accountNum" size="25"/></div> <input type="hidden" id="hiddenAccountNum" value=""/></td>
</tr>

<tr>
    <td align="right" width="15%"><b>Contact</b></td>
    <td colspan="1"><input  id="accountContact" name="contact" size="35" /></td>
    <td colspan="3">
        <div id="ccard" style="display:none">
                <%if(isAdmin){%>
            <div id='basic-modal4'>
                <b>Credit Card</b>&nbsp;&nbsp;<img style="cursor: pointer; cursor: hand; display: inline-block; vertical-align: left"
                                                   src="images/edit.gif" title="Edit Billing Info"
                                                   onclick="fetchCCInfo();"
                                                   value="Hist" class='basic'/>
                <div>
                    <div id="basic-modal4-content">
                        <h1>Enter Billing Information</h1>
                        <br/>
                        <table cellpadding="3" cellspacing="3">
                            <tr>
                                <td>Credit Card Number:</td>
                                <td>
                                    <input id="ccNum" type="text" value="" size="30" maxlength="40">
                                    <input id="ccPid" type="hidden" value="">
                                </td>
                            </tr>
                            <tr>
                                <td>Card Type:</td>
                                <td><select id="ccType">
                                    <option value="vc">VISA</option>
                                    <option value="mc">Master Card</option>
                                    <option value="ae">American Express</option>
                                    <option value="dc">Discover</option>
                                </select>
                                </td></tr>
                            <tr>
                                <td>Expiry Date:</td>
                                <td>

                                    <SELECT id="ccMonth" >
                                        <OPTION VALUE="" SELECTED>--Month--</OPTION>
                                        <OPTION VALUE="1">January (01)</OPTION>
                                        <OPTION VALUE="2">February (02)</OPTION>
                                        <OPTION VALUE="3">March (03)</OPTION>
                                        <OPTION VALUE="4">April (04)</OPTION>
                                        <OPTION VALUE="5">May (05)</OPTION>
                                        <OPTION VALUE="6">June (06)</OPTION>
                                        <OPTION VALUE="7">July (07)</OPTION>
                                        <OPTION VALUE="8">August (08)</OPTION>
                                        <OPTION VALUE="9">September (09)</OPTION>
                                        <OPTION VALUE="10">October (10)</OPTION>
                                        <OPTION VALUE="11">November (11)</OPTION>
                                        <OPTION VALUE="12">December (12)</OPTION>
                                    </SELECT> /
                                    <SELECT id="ccYear">
                                        <OPTION VALUE="" SELECTED>--Year--</OPTION>
                                        <%Calendar cal = Calendar.getInstance();
                                            for(int i = 0; i<10;i++){
                                                int yy = cal.get(Calendar.YEAR);
                                        %>
                                        <OPTION VALUE="<%=yy %>"><%=yy %></OPTION>
                                        <%
                                                cal.add(Calendar.YEAR, 1);
                                            }%>
                                    </SELECT>
                                </td>
                            <tr><td>
                                CSC: </td><td><input size="15" id="ccCSC" type="text" value=""  maxlength="5">
                            </td>
                            </tr>
                            <tr>
                                <br/>
                                <br/>
                                <td colspan="5">
                                    <input type="button" value="Save" onclick="saveCC()"/>
                                    <input type="button" class="simplemodal-close" value="Close"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <div id="ccResults"></div>
                                </td>
                            </tr>
                        </table>


                    </div>
                    <%}%>
                </div>
    </td>
</tr>

<tr>
    <td align="right" width="15%"><b>Address 1</b></td>
    <td colspan="1"><input id="accountStreet" name="address1" size="35"/></td>
    <td align="right"><b>Phone 1</b>  </td>
    <td><input id="accountPhone1" name="phone1" size="25"/></td>
</tr>

<tr>
    <td align="right" width="15%"><b>Email</b></td>
    <td colspan="1"><input id="accountEmail" name="email" size="35"/></td>
    <td align="right"><b>Phone 2</b>  </td>
    <td><input id="accountPhone2" name="phone2" size="25"/></td>
</tr>

<tr>
    <td align="right" width="15%"><b>City</b>     </td>
    <td colspan="1"><input id="accountCity" name="name" size="35"/></td>
    <td colspan="3"></td>
</tr>

<tr>
    <td align="right" width="15%"><b>State</b>    </td>
    <td colspan="1"><input id="accountState" name="name" size="5"/>
        &nbsp;&nbsp;&nbsp;<b>Zip</b> &nbsp;&nbsp;<input id="accountZip" name="name" size="5"/></td>
    <td colspan="3" align="left">
        <input type="image" onclick="makeGetRequestSaveUpdateAccount();toggleAcctEdit(0);"
               style="display: inline-block; vertical-align: middle"
               title="Save/Update Account" alt="Save" width="20" src="images/save.gif">
        &nbsp;&nbsp;&nbsp;<div  onclick="toggleAcctEdit(0);" class="divLink" title="close">close</div>
    </td>
</tr>
</table>
</td>
</tr>
<input type="hidden" id="shipNotes" value=""/>
<tr>
    <td align="right" colspan="10">
        <div id="tabs">
            <ul>
                <li><a class="tabLink" href="#tabs-0"><div id="a">First  </div></a></li>
                <li><a class="tabLink" href="#tabs-1"><div id="b">Second </div></a></li>
                <li><a class="tabLink" href="#tabs-2"><div id="c">Third  </div></a></li>
                <li><a class="tabLink" href="#tabs-3"><div id="d">Forth  </div></a></li>
                <li><a class="tabLink" href="#tabs-4"><div id="e">Fifth  </div></a></li>
                <li><a class="tabLink" href="#tabs-5"><div id="f">Sixth  </div></a></li>
                <li><a class="tabLink" href="#tabs-6"><div id="g">Seventh</div></a></li>
                <li><a class="tabLink" href="#tabs-7"><div id="h">Eighth </div></a></li>
                <li><a class="tabLink" href="#tabs-8"><div id="i">Ninth  </div></a></li>
                <li><a class="tabLink" href="#tabs-9"><div id="j">Tenth  </div></a></li>
            </ul>
            <div id="tabs-0"> <%=createTab("a")%> </div>
            <div id="tabs-1"> <%=createTab("b")%> </div>
            <div id="tabs-2"> <%=createTab("c")%> </div>
            <div id="tabs-3"> <%=createTab("d")%> </div>
            <div id="tabs-4"> <%=createTab("e")%> </div>
            <div id="tabs-5"> <%=createTab("f")%> </div>
            <div id="tabs-6"> <%=createTab("g")%> </div>
            <div id="tabs-7"> <%=createTab("h")%> </div>
            <div id="tabs-8"> <%=createTab("i")%> </div>
            <div id="tabs-9"> <%=createTab("j")%> </div>
        </div>
    </td></tr>
<tr><td align="right" colspan="4">
    <input class="btn" type="button" onclick="createOrder();" name="CreateOrder" id="createOrderBtn" value="Create Order" />
</td>
</tr>
</table>

<!-- modal content -->
<div id="basic-modal-content">
    <b>Shipping and Order Notes</b>
    <br/>
    <br/>
    <textarea cols="70" id="shipNts" rows="10" ></textarea>
    <br/>
    <br/>
    <input type="button" name="Close" onclick="storeShipNotes()" class="simplemodal-close" value="Close"/>
</div>
</div>
</div>

<script>
    //var page = document.getElementById("specialOrder");
    //page.style.display='none';
</script>
<script type="text/javascript">
    window.onload = function() {
        $("#message").fadeOut(5000);
    }
</script>

<%@include file="bottomLayout.jsp"%>
