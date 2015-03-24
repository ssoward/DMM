<%@include file="jspsetup.jsp"%>


<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.object.Invoice"%>
<SCRIPT LANGUAGE="JavaScript">
    function IsNumeric(strString)
        //  check for valid numeric strings
    {
        var strValidChars = "0123456789";
        var strChar;
        var blnResult = true;

        if (strString.length < 3) return false;

        //  test strString consists of valid characters listed above
        for (i = 0; i < strString.length && blnResult == true; i++)
        {
            strChar = strString.charAt(i);
            if (strValidChars.indexOf(strChar) == -1)
            {
                blnResult = false;
            }
        }
        return blnResult;
    }
    function ValidateForm(){
        var dt=document.getInvoice.pid;
        if(!IsNumeric(dt.value)){
            alert('Invalid invoice number. \nInput too short or NOT numeric: '+dt.value);
            dt.focus();
            return;
        }
        document.getInvoice.submit();
    }
    function ValidateFormNup(){
        var dt=document.getInvoiceNum.pid;
        if(!IsNumeric(dt.value)){
            alert('Invalid invoice number. \nInput too short or NOT numeric: '+dt.value);
            dt.focus();
            return;
        }
        document.getInvoiceNum.submit();
    }
</script>


<%
    String message = request.getParameter( "message" );
    String pid = request.getParameter( "pid" );
    String currCount = request.getParameter("currCount");
    ArrayList<Invoice> invList = null;
    if(pid!=null){
        InvoiceUtil.searchInvPid(pid);
        pid = null;
    }

%>
<br>
<h1>Invoice Search</h1>
<p align="center">Search for an invoice to generate invoice data, account associated with invoice, and the various corresponding transactions.</p>
<table cellpadding=0 cellspacing=9  border=0 align=center>
    <FORM name="getInvoiceNum" method="post" ACTION="./viewInvoice.jsp">
        <tr>
            <td align=right>
                Get Invoice:
                <input type="text" name="pid">
            </td>
            <td>
                <input type="button" class="btn" onclick="ValidateFormNup()" value="Get Invoice"/>
            </td>
        </tr>
    </FORM>
    <FORM name="getInvoice" method="post" ACTION="./searchInvoices.jsp">
        <tr>
            <td align=right>
                Search For Invoice number:
                <input type="text" name="pid">
            </td>
            <td>
                <input type="button" class="btn" onclick="ValidateForm()" value="Search Invoice" />
            </td>
        </tr>
    </FORM>
</table>
<div class="ajaxResponse viewinvoice" id="description"><%=message!=null?message:""%></div>
<%if(invList!=null){
%>
<br>
<table class="sortable, common" id="viewInvoices" border="1" align="center"
       cellpadding="0" cellspacing="0" width="780">
    <tr>
        <th>&nbsp;#&nbsp;</th>
        <th>&nbsp;Account Number&nbsp;</th>
        <th>&nbsp;Invoice Number&nbsp;</th>
        <th>&nbsp;Invoice Date&nbsp;</th>
        <th>&nbsp;Invoice Total&nbsp;</th>
        <th>&nbsp;Invoice Discount&nbsp;</th>
        <th>&nbsp;Veiw Account&nbsp;</th>
        <th>&nbsp;Veiw Invoice&nbsp;</th>
    </tr>
    <%
        int count = 0;
        boolean flipShade = true;
        for ( Invoice temp : invList ) {
            count++;
    %>
    <% if(flipShade){
        flipShade = false;%>
    <tr bgcolor="#eeeeee">
    <%}else{flipShade =true;%><tr><%}%>
    <td><%= count%></td>
    <td><%= temp.getAccountNum()%></td>
    <td><%= temp.getInvoiceNum()%></td>
    <td><%= temp.getInvoiceDate()%></td>
    <td><%= temp.getInvoiceTotal()%></td>
    <td><%= temp.getInvoiceDiscount()%></td>
    <td><a href="./editAccounts.jsp?pid=<%= temp.getAccountNum()%>">view acct</a></td>
    <td><a href="./viewInvoice.jsp?pid=<%= temp.getInvoiceNum()%>">view inv</a></td>
</tr>
    <%}//end of for loop %>
    <%
        //end of if(acc!=null)
    }else{%><center>

</center>
    <%}//end of else %>
    <%
        if ( message != null ) {
    %>

    <%
        }
    %>
    </tr>
</table>
<%@include file="bottomLayout.jsp"%>

