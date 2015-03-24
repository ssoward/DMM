<%@include file="jspsetup.jsp"%>
<%@ page import="com.soward.util.*" %>
<%@ page import="com.soward.object.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.soward.json.InvoiceJSONEnum" %>
<%@ page import="com.soward.enums.DeleteInvoiceEnum" %>

<!-- DATE STUFF -->
<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.17/themes/base/jquery-ui.css" type="text/css" media="all" />
<link rel="stylesheet" href="http://static.jquery.com/ui/css/demo-docs-theme/ui.theme.css" type="text/css" media="all" />
<link type='text/css' href='css/basic.css' rel='stylesheet' media='screen' />
<script type='text/javascript' src='js/jquery.simplemodal.js'></script>
<script type='text/javascript' src='js/basic.js'></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.17/jquery-ui.min.js" type="text/javascript"></script>
<script src="http://jquery-ui.googlecode.com/svn/tags/latest/external/jquery.bgiframe-2.1.2.js" type="text/javascript"></script>
<script src="http://jquery-ui.googlecode.com/svn/tags/latest/ui/minified/i18n/jquery-ui-i18n.min.js" type="text/javascript"></script>

<script type="text/javascript" src="js/invoiceUtilAjax.js"></script>

<script type="text/javascript">
    function setModal(str){
        var data = document.getElementById('archived-invoice'+str).innerHTML;
        document.getElementById('reversedInvoiceJSON').innerHTML = data;
    }
    $(function() {
        $( "#datepicker1" ).datepicker();
        $( "#datepicker2" ).datepicker();
        ( $.datepicker && $.datepicker.setDefaults($.extend({showMonthAfterYear: false}, $.datepicker.regional[''])) );
    });

    function validateForm() {
        var cal1 = document.getElementById("datepicker1").value;
        var cal2 = document.getElementById("datepicker2").value;
        var registerUser = document.getElementById("registerUser");
        var select1 = document.getElementById("reverseUser");
        var register = registerUser.options[registerUser.selectedIndex].text;
        var user = select1.options[select1.selectedIndex].text;
        if(!cal1){
            setMessage('Missing required fields');
        }else{
            document.getInvoice.submit();
        }

//        alert(
//                cal1+'\n'+
//                cal2+'\n'+
//                register+'\n'+
//                user+'\n');

    }

</script>


<%
    User users = new User();
    ArrayList<User> uu = users.getAllUser();
    String cal1 = request.getParameter("datepicker1");
    String cal2 = request.getParameter("datepicker2");
    String userName = request.getParameter("reverseUser");
    String registerUser = request.getParameter("registerUser");
    boolean message = false;

    ArrayList<ArchivedInvoice> aList = null;
    if(!StringUtils.isBlank(cal1) )  {
        aList = ArchivedInvoiceUtil.getReversedList(cal1, cal2, userName, registerUser);
        message = true;
    }
    RegLocationUtil rUtil = new RegLocationUtil();

%>

<h1>Reversed Invoice Report</h1>
<p class="text">Get reversed invoices, search by date or user.</p>
<div class="ajaxResponseMessage" id="description"></div>
<div class="input-form">

    <FORM name="getInvoice" id="getInvoice" method="post" ACTION="./reportReversedInv.jsp">
        <div class="form-element"><div class="form-key">Date From: </div><div class="form-val"><input class="required" type="text" name="datepicker1" id="datepicker1"></div></div>
        <div class="form-element"><div class="form-key">Date To:   </div><div class="form-val"><input type="text" name="datepicker2" id="datepicker2"></div></div>
        <div class="form-element"><div class="form-key">User:      </div><div class="form-val">
            <select id="reverseUser" name="reverseUser">
                <option>Any</option>
                <%for(User user: uu){%>
                <option><%=user.getName()%></option>
                <%}%>
            </select>
        </div>
        </div>
        <div class="form-element"><div class="form-key">Register: </div><div class="form-val">
            <select id="registerUser" name="registerUser">
                <option>Any</option>
                <%for(RegLocation rLoc: rUtil.getrList()){%>
                <option value="<%=rLoc.getRegNum()%>"><%=rLoc.getRegName()%></option>
                <%}%>
            </select>
        </div>
        </div>
        <div style="clear:both"></div>
        <div class="form-element"><div class="form-key"></div>
            <div class="form-val">
                <input type="button" class="btn" onclick="validateForm()" value="Search" />
            </div>
        </div>

    </form>
</div>
<%if(aList!=null){



%>
<table class="common" id="viewTEAccounts" align="center" cellpadding="0" cellspacing="0" width="780">
    <tr>
        <th>Inv#</th>
        <th>Original Date</th>
        <th>Deleted Date</th>
        <th>Deleted By</th>
        <th>Register</th>
        <th>Reason</th>
        <th>Comment</th>
    </tr>
    <%for(ArchivedInvoice ai: aList){
        ArchivedInvoiceUtil aiu = new ArchivedInvoiceUtil(ai.getInvoiceJSON());
        RegLocation reg = rUtil.getRegLocation(aiu.getJSONStr(InvoiceJSONEnum.LOCATIONNUM));
        String register = reg!=null?reg.getRegName():"";
        DeleteInvoiceEnum reason = DeleteInvoiceEnum.getForId(ai.getReason());
    %>
    <tr>
        <td class="table-element">
            <div id='basic-modal'>
                <a href="#" onclick="setModal(<%=ai.getId()%>);" class="basic"><%=ai.getInvoiceNum()%></a>
                <div id="<%="archived-invoice"+ai.getId()%>" style="display:none"><%=ArchivedInvoiceUtil.createInvoice(ai.getInvoiceJSON(), rUtil)%></div>
            </div>
        </td>
        <td class="table-element"><%=Utils.dp(aiu.getJSONStr(InvoiceJSONEnum.INVOICEDATE))%></td>
        <td class="table-element"><%=Utils.dp(ai.getDate())%></td>
        <td class="table-element"><%=ai.getUserName()%></td>
        <td class="table-element"><%=register%></td>
        <td class="table-element"><%=reason.getName()%></td>
        <td class="table-element"><%=ai.getAdditionComments()%></td>
    </tr>
    <%}%>
</table>

<%}
if(message){%>
<script type="text/javascript">
    setMessage('No results returned for criteria.');
</script>
<%}%>
<div id="basic-modal-content">
    <div class="scrollTable" id="reversedInvoiceJSON"></div>
</div>
<%@include file="bottomLayout.jsp"%>
