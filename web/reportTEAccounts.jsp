<%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.InvBundle"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.util.AccountUtil"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<SCRIPT LANGUAGE="JavaScript">
    function IsNumeric(strString)
        //  check for valid numeric strings
    {
        var strValidChars = "0123456789.-";
        var strChar;
        var blnResult = true;

        if (strString.length == 0) return false;

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
        var dt=document.myform.dateOne;
        var dtt=document.myform.dateTwo;
        if(!IsNumeric(dt.value)){
            alert('Date must be in form: 2007-05-10 \nNot Numeric in: '+dt.value);
            dt.focus();
            return;
        }
        else if(!IsNumeric(dtt.value)){
            alert('Date must be in form: 2007-05-10 \nNot Numeric in: '+dtt.value);
            dtt.focus();
            return;
        }
        else if(dt.value.charAt(4)=="-"&& dt.value.charAt(7)=="-"){
            if(dtt.value.charAt(4)=="-"&& dtt.value.charAt(7)=="-"){
                document.myform.submit();
            }
            else{
                alert('Date must be in form: 2007-05-10 \nMisplaced - in: ' +dtt.value);
                dtt.focus();
                return;
            }
        }
        else{
            alert('Date must be in form: 2007-05-10 \nMisplaced - in: '+dt.value);
            dt.focus();
            return;
        }
    }

</script>
<SCRIPT LANGUAGE="JavaScript" SRC="css/_CalendarPopup.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"> var cal = new CalendarPopup(); </SCRIPT>
<%
    AccountUtil au = new AccountUtil();
    ArrayList<Account> acc = null;
    TimeZone tz = TimeZone.getTimeZone("America/Salt_Lake");
    Calendar c = Calendar.getInstance();

    int year = c.get(Calendar.YEAR);
    int mon  = c.get(Calendar.MONTH);
    int day  = c.get(Calendar.DAY_OF_MONTH);
//System.out.println(year+" / "+(mon+1)+" / "+day);

    String date = year+"-";
    if(mon<10){date+="0"+(mon+1);}else{date+=(mon+1);}
    if(day<10){date+="-0"+day;}else{date+="-"+day;}

//message = (String)session.getAttribute("message");
    String message = request.getParameter( "message" );
    String dateOne = request.getParameter("dateOne");
    String dateTwo = request.getParameter("dateTwo");
    String utNon   = request.getParameter("utNon");
    String schNon  = request.getParameter("schNon");
    if(dateTwo==null&&dateOne==null){
        dateTwo=date;
        dateOne=date;
    }
    else{
        acc = au.getAllTaxExemptAccounts(dateOne, dateTwo, utNon, schNon);
        if(acc==null){
            message = "Invalid parameters/dates, please check the input and try again. If the problem persists please inform the system admin.";
        }
    }
%>

<form name="myform" method="post" action="./reportTEAccounts.jsp" >
    <h1>Tax Exempt Spending Report</h1>
    <p class="text">Get total spent and total discount given. Click on the calendar icons or enter dates to specify a date range. If you would like to get data for just one day set the range to the surrounding days.</p>
    <table align="center">
        <tr>
            <td colspan=8>
                <table align="center">
                    <tr>
                        <TD align=left>Get data from: </td>
                        <td><A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateOne"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;"
                               NAME="<%="anchor"%>" ID="<%="anchor"%>"> <img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp; </TD>
                        <TD align=left><input size=10 type="text" value="<%=dateOne%>" name="dateOne">
                        </td>
                        <td>&nbsp;&nbsp;&nbsp;&nbsp;Select Utah Non Utah: </td>
                        <td><select name="utNon" disabled>
                            <option value="NA">Any State</option>
                            <option value="UTAH">Utah Only</option>
                            <option value="NON">Non Utah</option>
                        </select>
                        </td>
                    </tr>
                    <tr>
                        <TD align=left> to end date: </td>
                        <td><A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateTwo"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" NAME="<%="anchor"%>" ID="<%="anchor"%>"> <img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp; </TD>
                        <TD align=left><input size=10 type="text" value="<%=dateTwo%>" name="dateTwo">
                        </td>
                        <td>&nbsp;&nbsp;&nbsp;&nbsp;Select School or all TE: </td>
                        <td><select name="schNon">
                            <option <%if(schNon!=null&&schNon.equals("SCHOOL")){%>selected="selected"<%}%>value="SCHOOL">School Only</option>
                            <option <%if(schNon!=null&&schNon.equals("ALLTE")){%>selected="selected"<%}%>value="ALLTE">All Tax Exempt</option>
                            <option <%if(schNon!=null&&schNon.equals("TEACH")){%>selected="selected"<%}%>value="TEACH">Teacher</option>
                        </select>
                        </td>
                    </tr>
                </table></td>
        </tr>
        <tr>
            <td colspan=8 align=center><input type="hidden" name="getInvoice" value="true">
                <input type="button" class="btn" onclick="ValidateForm()" name="Enter" value="Enter" >
            </td>
        </tr>
        <tr>
            <td colspan=8><hr>
            </td>
        </tr>
    </table>
</form>
<%if(acc!=null){
    double totSpent = 0.0;
    double totDisct = 0.0;
%>
<table class="common" id="viewTEAccounts" align="center" cellpadding="0" cellspacing="0" width="780">
    <tr>
        <th>&nbsp;#&nbsp;</th>
        <th>&nbsp;Account Number&nbsp;</th>
        <th>&nbsp;Account Name&nbsp;</th>
        <th>&nbsp;Account State&nbsp;</th>
        <th>&nbsp;Account Phone&nbsp;</th>
        <th>&nbsp;Total Spent&nbsp;</th>
        <th>&nbsp;Total Discount &nbsp;</th>
        <th>&nbsp;Edit Acct &nbsp;</th>
    </tr>
    <%
        int count = 0;
        boolean flipShade = true;
        for ( Account temp : acc ) {
            Invoice inv = temp.getAccountInv();
            AccountUtil accu = new AccountUtil();
            //get double value after checking for null if null return 0.0;
            double totSum = Double.valueOf( accu.nullToDouble(temp.getAccountInv().getInvoiceTotalSum()) ).doubleValue();
            double totDis = Double.valueOf( accu.nullToDouble(temp.getAccountInv().getInvoiceDiscountSum()) ).doubleValue();
            totSpent +=totSum;
            totDisct +=totDis;
            count++;
    %>
    <% if(flipShade){
        flipShade = false;%>
    <tr>
            <%}else{flipShade =true;%>
    <tr>
        <%}%>
        <td><%= count%></td>
        <td><%= temp.getAccountNum()%></td>
        <td><%= temp.getAccountName()%></td>
        <td><%= temp.getAccountState()%></td>
        <td><%= temp.getAccountPhone1()%></td>
        <td><%= totSum %></td>
        <td><%= totDis %></td>
        <td align="center"><a href="./editAccounts.jsp?pid=<%= temp.getAccountNum()%>"><img border="0" src="./images/edit.gif"></a></td>
    </tr>
    <%}//end of for loop %>
    <%
        //get totals of all accounts from individual sums.
    %>
    <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td><%=InvoiceUtil.trunDouble(totSpent+"") %></td>
        <td><%=InvoiceUtil.trunDouble(totDisct+"") %></td>
        <td>&nbsp;</td>
    </tr>
    <%
        //end of if(acc!=null)
    }else{%>
    <center>
        Select date range to genterate tax exempt account data.
    </center>
    <%}//end of else %>
    <%
        if ( message != null ) {
    %>
    <tr>
        <td><p class="message"><%=message%></p></td>
    </tr>
    <%
        }
    %>
</table>
<%@include file="bottomLayout.jsp"%>
