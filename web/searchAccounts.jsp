<%@page import="com.soward.enums.SearchTypeEnum"%><%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.object.AccountType"%>
<%@page import="com.soward.object.User"%>
<%@page import="com.soward.util.AccountTypesUtil"%>
<%@page import="com.soward.util.AccountUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<script type="text/javascript" src="js/inventoryAjax.js"></script>
<script type="text/javascript" src="js/comboBox.js"></script>
<script type="text/javascript" src="js/accountSearchAjax.js"></script>
<script type='text/javascript' src='js/jquery.js'></script>
<script type='text/javascript' src='js/jquery.simplemodal.js'></script>
<script type='text/javascript' src='js/basic.js'></script>
<link type='text/css' href='css/basic.css' rel='stylesheet' media='screen' />
<script type="text/javascript" src="js/invoiceUtilAjax.js"></script>
<script type="text/javascript" src="js/utilsAjax.js"></script>
<%
    String message = request.getParameter( "message" );
    String resultMess = "";
    List<AccountType> accTListAll = AccountTypesUtil.getAccountTypes();
    List<AccountType> accTList = new ArrayList<AccountType>();
    for(AccountType al: accTListAll){
        try{
            Integer.parseInt(al.getTypeCode());
            accTList.add(al);
        }catch(Exception e){/*not numeric*/}
    }
    String queryTxt = request.getParameter( "queryTxt" );
    String objType = request.getParameter( "object" );
    String accType = request.getParameter( "accType" );
    String searchType = request.getParameter("searchType");
    String sortType = request.getParameter( "sortType" );
    String allQuery = request.getParameter( "allQuery" );
    String allAccType = request.getParameter( "allAccType" );
    String allUpdate = request.getParameter( "allUpdate" );
    String updateAllAccountType2 = request.getParameter( "updateAllAccountType2" );
    String resultSize = request.getParameter( "resultSize" );
    boolean balanceSort = (!StringUtils.isBlank(sortType)&&sortType.equals("balance"))||StringUtils.isBlank(sortType);

    User user = new User();

    ArrayList<Account> accountList = new ArrayList<Account>();
    //System.out.println("pc: "+pc);
    boolean str = false;
    int resultSizeInt = -0;
    try{
        if(resultSize!=null){
            resultSizeInt = Integer.parseInt(resultSize);
        }else{
            resultSize = "10";
        }
    }catch(Exception e){
    }
    try {
        if ( !StringUtils.isBlank( queryTxt ) || accType!=null) {

            accountList = AccountUtil.fetchAccounts(searchType, queryTxt, accType, balanceSort, resultSizeInt );
            if ( accountList == null || accountList.size() < 1 ) {
                message="No account found for "+queryTxt+ (StringUtils.isBlank(accType)?" accountType: "+accType:"")+" please consider refining your search criteria.";
            }
        }else if(!StringUtils.isBlank(accType)){
            message="Account name or number required.";
        }
    } catch ( Exception e ) {
        //response.sendRedirect("searchAccounts.jsp?message=An error occured, try again :).");
    }
%>

<h1>Account Search</h1>

<p align="center">Search for an account by either name or account
    number and then edit the account or view corresponding invoices.</p>

<FORM name="searchAccountName" ACTION="./searchAccounts.jsp" method="post">
    <table cellpadding="0" cellspacing="0" border="1" align="center">
        <tr>
            <td>
                <table cellpadding="2" cellspacing="9" border="0" align="center">
                    <tr>
                        <td align="right">Search:</td>
                        <%
                            String prompt = StringUtils.isBlank(queryTxt)?"":queryTxt;
                        %>
                        <td><input type="text" id="searchTextBox" value="<%=prompt%>" name="queryTxt">
                        </td>
                    </tr>
                    <tr>
                        <td align="right" colspan="1">Search Type:</td>
                        <td align="left" colspan="1">
                            <select name="searchType">
                                <%
                                    for ( SearchTypeEnum enu : SearchTypeEnum.values() ) {
                                        String selected = "";
                                        if(searchType!=null&&searchType.equals(enu.getName())){
                                            selected = "SELECTED";
                                        }
                                %>
                                <option <%=selected%> value="<%=enu.getName()%>"><%=enu.getName()%></option>
                                <%
                                    }
                                %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" colspan="1">Account Type:</td>
                        <td align="left" colspan="1">
                            <select name="accType">
                                <option value=""></option>
                                <%
                                    for ( AccountType accT : accTList ) {
                                        String selected = "";
                                        if(accType!=null&&accType.equals(accT.getTypeCode())){
                                            selected = "SELECTED";
                                        }
                                %>
                                <option <%=selected%> value="<%=accT.getTypeCode()%>"><%=accT.getTypeDescription()%></option>
                                <%
                                    }
                                %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" colspan="1">Result Size:</td>
                        <td align="left" colspan="1">
                            <%
                                List<String> nList = Arrays.asList("10","50","100","500","1000","2000","5000","All");
                            %>
                            <select name="resultSize">
                                <% for(String nn: nList){
                                    String selected = "";
                                    if(resultSize!=null&&resultSize.equals(nn)){
                                        selected = "SELECTED";
                                    }

                                %>
                                <option <%=selected%> value="<%=nn%>"  ><%=nn%> </option>
                                <%}%>

                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" colspan="8">Sort By:&nbsp;
                            Balance<input type="radio" name="sortType" <%=(balanceSort?"CHECKED=CHECKED":"") %> value="balance">
                            Name<input type="radio" name="sortType"    <%=(balanceSort?"":"CHECKED=CHECKED") %> value="name">
                            <input class="btn" type="submit"
                                   value="Search" onclick="startSpinner(1)"/></td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</FORM>

<br/>
<% if(accountList!=null&& !accountList.isEmpty()){%>
<% if(isAdmin){ %>
<center>
    <FORM name="searchAccountName" ACTION="./searchAccounts.jsp" method="post">
        <select id="updateAcctType" name="updateAllAccountType2">
            <option value="">-Account Type</option>
            <%
                for ( AccountType accT : accTList ) {
            %>
            <option value="<%=accT.getTypeCode()%>"><%=accT.getTypeDescription()%></option>
            <%
                }
            %>
        </select>
        <input type="button" value="Update Selected" onclick="updateAccountTypes();" />
    </form>
    <%} %>
    <br/>
    <font color="blue"> <div id="ajaxMessage"><%=resultMess!=null&&resultMess.length()>0?resultMess:""%></div> </font>
    <br/>
</center>
<%
    //    String accEmail = "";
//    for ( Account account : accountList ) {
//        if(account!=null&&!StringUtils.isBlank(account.getAccountEmail())){
//            accEmail += account.getAccountEmail()+",";
//        }
//    }
//    if(!StringUtils.isBlank(accEmail)){
//        accEmail = accEmail.substring(0, accEmail.length()-1);
//    }

%>

<div class="ajaxResponseAccounts">
    <div class="ajaxResponse" id="description"></div>
</div>
<!-- modal content -->
<div id="basic-modal-content">
    <div id="sendEmailModal">
        <div id="modalLabel">Send Email</div>
        <div id="modalMessage"></div>
        <div class="modalElement">
            <div class="label">BCC:</div>
            <div class="labelValue">
                <textarea rows="2" id="bCC" cols="45"></textarea>
            </div>
        </div>

        <div class="modalElement">
            <div class="label">Recipients:</div>
            <div class="labelValue">
                <input type="text" name="eRecipients" id="eRecipients"/>
            </div>
        </div>

        <div class="modalElement">
            <div class="label">CC:</div>
            <div class="labelValue">
                <input type="text" name="eCC" id="eCC"/>
            </div>
        </div>
        <div class="modalElement">
            <div class="label"      id="label">Subject:</div>
            <div class="labelValue" id="labelValue">
                <input type="text" name="eSubject" id="eSubject"/>
            </div>
        </div>
        <div class="modalElement" id="addNotes">
            <div class="label"      id="addLabel">Email Body:</div>
            <div class="labelValue">
                <textarea rows="5" id="eBody" cols="45"></textarea>
            </div>
        </div>
        <div style="clear: both;"></div>
        <div id="delButton">
            <input type="button" onclick="sendEmail()" class="btn" value="Send Email"/>
            <a class="cancelLink" href="#" onclick="closeModal()">cancel</a>
        </div>
        <div class="modalElement">
            <div class="label warning">Warning: This action will email to all addresses listed.</div>
        </div>
    </div>
</div>

<table class="clear" width="754" border="0" align="center" cellpadding="0"
       cellspacing="0">
    <tr>
        <td>
        </td>
    <tr>
        <td valign="top">
            <div>
                <div class="checkUncheck">
                    Check/Uncheck&nbsp;<input onclick="toggleCheck(this)" type="checkbox" style="padding-left:100px;"/>
                    <img style="cursor: pointer; cursor: hand; display: inline-block; vertical-align: left"
                         src="images/PDF.png" title="Get PDF Report of Addresses"
                         onclick="getCreatPDFAddresses()"
                         value="Hist"/>
                </div>
                <div id='basic-modal' class="accountSendEmail">
                    <img style="cursor: pointer; cursor: hand; display: inline-block; vertical-align: left"
                         src="images/emailed.png" title="Send Mass Email"
                         onclick="getCheckedEmails()"
                         value="Hist" class='basic'/>
                </div>
            </div>
            <table width="100%" class="sortable, common" id="viewAccounts" align="center"
                   cellpadding="1" cellspacing="1">
                <tr>
                    <th>#</th>
                    <th>Edit</th>
                    <th>Name</th>
                    <th>Acc Type</th>
                    <th>Balance</th>
                    <th>Edit Account</th>
                    <th>View Invoices</th>
                    <!--
                    <th>Credit Hist</th>
                    -->
                    <th>Delete</th>
                </tr>
                <%
                    int count = 0;
                    for ( Account account : accountList ) {
                        count++;
                %>
                <tr>
                    <td><%=count%></td>
                    <td><input type="checkbox" id="<%="update"+count%>" /></td>
                    <td>
                        <%=account.getAccountName()%><br/>
                        <%if(!StringUtils.isBlank(account.getAccountAddress())){%>
                        <%=account.getAccountAddress()%><br/>
                        <%}%>
                        <%if(!StringUtils.isBlank(account.getAccountEmail())){%>
                        <%=account.getAccountEmail()%><br/>
                        <%}%>
                        <%if(!StringUtils.isBlank(account.getAccountPhone1())){%>
                        <%=account.getAccountPhone1()%>
                        <%}%>

                    </td>

                    <td align="left" colspan="1">
                        <div class="updateAccountType2">
                            <select onclick="document.getElementById('<%="description"+account.getAccountNum()%>').innerHTML='Unsaved';"
                                    id="<%="accountType2"+account.getAccountNum()%>" name="accountType2">
                                <option value=""></option>
                                <%
                                    String acType = account.getAccountType1();
                                    for ( AccountType accT : accTList ) {
                                        String selected = "";
                                        if(acType!=null&&acType.equals(accT.getTypeCode())){
                                            selected = "SELECTED";
                                        }
                                %>
                                <option <%=selected%> value="<%=accT.getTypeCode()%>"><%=accT.getTypeDescription()%></option>
                                <%
                                    }
                                %>
                            </select>
                            <input class="updateAccountType2Input" type="image"
                                   onclick="makeGetRequest5(document.getElementById('<%="accountType2"+account.getAccountNum()%>').value,'<%=account.getAccountNum()%>') "
                                   style="display: inline-block; vertical-align: middle"
                                   title="Save price" alt="Save" width="20" src="images/save.gif">
                            <div class="updateAccountType2AjaxResult" id="<%="description"+account.getAccountNum()%>"></div>
                        </div>
                    </td>
                    <%
                        String dub = "0.0";
                        try{
                            dub = bdec.format(Double.parseDouble(account.getAccountBalance()));
                        }catch(Exception e){}

                    %>
                    <input type="hidden" id="<%="accountSendEmail"+count%>" value="<%=account.getAccountEmail()%>"/>
                    <td align="center"><%=dub%></td>
                    <td align="center"><div id="<%="accountNum"+count%>"><%=account.getAccountNum()%></div>&nbsp;
                        <img src="images/edit.gif"
                             style="cursor: pointer; cursor: hand" title="Edit this product"
                             onclick="jsfPopupPage('./editAccounts.jsp?pid=<%=account.getAccountNum()%>&location=<%="Products" %>', 800, 500);" />
                    </td>
                    <td><a href="./viewAccountInvoices.jsp?pid=<%=account.getAccountNum()%>">view&nbsp;invoices</a></td>
                    <td><a onclick="alert('Delete account is not currently implemented.\nIf this is a needed functionality, please advise sys admin\nOr in other words, send Scott an email.');" href="#">del&nbsp;acct</a></td>
                    <%
                        }
                    %>
                </tr>
            </table>

        </td>
    </tr>
</table>
<%}%>
<br />
<%
    if ( message != null ) {
%>
<br>
<center>
    <td align="center"><font color="red"><%=message%></font></td>
</center>
<%
    }
%>

<a style="display:none;" href="AccountAddresses.pdf?reportCollection=accountAddresses&function=AccountAddresses">PDF</a>
<script type="text/javascript">
    function lf(){
        document.getElementById('searchTextBox').focus();
        stopSpinner(1);
    }

    onLoad=lf();
</script>
</tr>
</table>
<%@include file="bottomLayout.jsp"%>
