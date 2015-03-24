
<%@page import="com.soward.object.Departments"%>
<%@page import="com.soward.util.DepartmentsUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<jsp:directive.page import="com.soward.object.Product"/>
<jsp:directive.page import="com.soward.util.InventoryReport"/>
<jsp:directive.page import="java.text.NumberFormat"/>
<jsp:directive.page import="java.text.DecimalFormat"/>
<jsp:directive.page import="com.soward.enums.LocationsDBName"/>
<%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.InvBundle"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.util.AccountUtil"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.util.TransUtil"%>
<script type="text/javascript" src="js/comboBox.js">
</script>
<SCRIPT LANGUAGE="JavaScript">
    function IsNumeric(strString)
        //  check for valid numeric strings
    {
        var strValidChars = "0123456789./";
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

    function ValidateFormSingle(){
        var dt=document.singleForm.singleDate;
        if(!IsNumeric(dt.value)){
            alert('Date must be in form: 04/30/2010 \nNot Numeric in: '+dt.value);
            dt.focus();
            return;
        }
        else if(dt.value.charAt(4)=="-"&& dt.value.charAt(7)=="-"){
            document.singleForm.submit();
        }
        else{
            alert('Date must be in form: 04/30/2010 \nMisplaced - in: '+dt.value);
            dt.focus();
            return;
        }
    }


    function ValidateForm(){
        var dt=document.myform.dateOne;
        var dtt=document.myform.dateTwo;
        var invQtyStart=document.myform.invQtyStart;
        if(!IsNumeric(invQtyStart.value)){
            alert('Inventory quantity must be an integer: [1,2,3...] \nNot: '+invQtyStart.value);
            invQtyStart.focus();
            return;
        }
        if(!IsNumeric(dt.value)){
            alert('Date must be in form: 04/30/2010 \nNot Numeric in: '+dt.value);
            dt.focus();
            return;
        }
        else if(!IsNumeric(dtt.value)){
            alert('Date must be in form: 04/30/2010 \nNot Numeric in: '+dtt.value);
            dtt.focus();
            return;
        }
        else if(dt.value.charAt(2)=="/"&& dt.value.charAt(5)=="/"){
            if(dtt.value.charAt(2)=="/"&& dtt.value.charAt(5)=="/"){
                document.myform.submit();
            }
            else{
                alert('Date must be in form: 04/30/2010 \nMisplaced - in: ' +dtt.value);
                dtt.focus();
                return;
            }
        }
        else{
            alert('Date must be in form: 04/30/2010 \nMisplaced - in: '+dt.value);
            dt.focus();
            return;
        }
    }

</script>
<SCRIPT LANGUAGE="JavaScript" SRC="css/_CalendarPopup.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"> var cal = new CalendarPopup(); </SCRIPT>


<%
    List<Departments> allDepartments = DepartmentsUtil.getAllDepartments();
    HashMap deptHash = new HashMap<String, String>();
    for (Departments dept : allDepartments) {
        deptHash.put(dept.getDepartmentCode(), dept.getDepartmentName());
    }
    ArrayList<Departments> selDept = new ArrayList<Departments>();
    for (Departments ddd : allDepartments) {
        String seld = request.getParameter("dept"
                + ddd.getDepartmentCode());
        if (seld != null) {
            selDept.add(ddd);
        }

    }
    session.setAttribute("selDept", selDept);
    InventoryReport invUtil = new InventoryReport();
    List<Product> invList = null;
    TimeZone tz = TimeZone.getTimeZone("America/Salt_Lake");
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -1);

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    String startDate = sdf.format(cal.getTime());
    cal.add(Calendar.YEAR, 1);
    String endDate = sdf.format(cal.getTime());

    String message = request.getParameter("message");
    String dateOne = request.getParameter("dateOne");
    String dateTwo = request.getParameter("dateTwo");
    String invQtyStart = request.getParameter("invQtyStart");
    String invQtyEnd = request.getParameter("invQtyEnd");
    if (dateTwo == null && dateOne == null && invQtyStart == null) {
        dateTwo = endDate;
        dateOne = startDate;
        invQtyStart = "50";
        invQtyEnd = "100";
    } else {
        invList = invUtil.getInventoryReport(dateOne, dateTwo,
                invQtyStart, invQtyEnd, selDept);
    }
%>

<h1>Product Inventory Report</h1>
<p class="text">Get inventory totals for all products with X quantity in stock for a given date. Click on the calendar icons or enter dates to specify a date range. If you would like to get data for just one day, use single day calendar.</p>
<table align="center">
    <tr>
        <th>Date Range</th></tr><tr>

    <form name="myform" method="post" action="./reportProductInventory.jsp" >
        <td>
            <table border="1" cellspacing="0" cellpadding="0" width="100%">
                <tr>
                    <td>
                        <table border="0" cellspacing="3" cellpadding="3" width="100%"> <tr>
                            <TD align="right">Start date: </td>
                            <TD align="left">
                                <A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateOne"%>,'<%="anchor"%>','MM/dd/yyyy'); return false;"
                                   NAME="<%="anchor"%>" ID="<%="anchor"%>">
                                    <img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;
                                <input size=10 type="text" value="<%=dateOne%>" name="dateOne">
                            </td></tr>
                            <tr>

                                <TD align="right">  End date: </td>
                                <TD align="left">
                                <A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateTwo"%>,'<%="anchor"%>','MM/dd/yyyy'); return false;"
                                       NAME="<%="anchor"%>" ID="<%="anchor"%>">
                                        <img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;
                                    <input size=10 type="text" value="<%=dateTwo%>" name="dateTwo">
                                </td>
                            </tr>

                            </td>
                            </tr>

                            <tr>
                                <td  align="right">
                                    Inventory Range:
                                </td>
                                <TD align="left">
                                    <input size=3 value="<%=invQtyStart%>" type="text"  name="invQtyStart"/>&nbsp;-&nbsp;
                                    <input size=3 value="<%=invQtyEnd%>" type="text"  name="invQtyEnd"/>
                                </td></tr>
                            <tr>
                                <td align="right">Department:   </td>
                                <td>
                                    <table border="0" cellpadding="2" cellspacing="0">
                                        <!-- START DEPARTMENT ===================================== -->
                                        <tr>
                                            <%
                                                int countt = 0;
                                                for (Departments sup : allDepartments) {
                                                    if (StringUtils.isBlank(sup.getDepartmentName())) {
                                                        continue;
                                                    }
                                                    String cc = "";
                                                    if(selDept.contains(sup)){
                                                        cc = "CHECKED=\"CHECKED\"";
                                                    }
                                                    countt++;
                                            %>
                                            <td align="right"><b><%=sup.getDepartmentName()%></b></td>
                                            <td><input <%=cc%> type="checkbox" value="<%="dept" + sup.getDepartmentCode()%>" name="<%="dept" + sup.getDepartmentCode()%>" />
                                            </td><%
                                            if (countt == 3) {
                                                countt = 0;
                                        %></tr><tr><%
                	}
                %>
                                            <%
                  	}
                  %>
                                    </table>
                                </td>
                                <!-- START DEPARTMENT ===================================== -->
                            </tr>
                            <tr><td colspan="4" align="center">
                                <input type="button" class="btn" onclick="ValidateForm()" name="Enter" value="Enter" >
                            </td></tr>
                        </table></td>      			</tr>

            </table></td>      			</tr>
    <tr>
        <td colspan="8">
            <hr/>
        </td>
    </tr>
</table>
</form>
<%
    if (invList != null) {
        double teTotal = 0.0;
        String grandTot = "";
%>
<br/>
<center>
    <a href="ProductInventoryReport.pdf?reportCollection=ProductInventoryReport&dateOne=<%=dateOne%>&dateTwo=<%=dateTwo%>&invQtyStart=<%=invQtyStart%>&department=<%=selDept%>&invQtyEnd=<%=invQtyEnd%>">PDF</a>
    &nbsp;
    <a href="ProductInventoryReport.csv?reportCollection=ProductInventoryReport&dateOne=<%=dateOne%>&dateTwo=<%=dateTwo%>&invQtyStart=<%=invQtyStart%>&department=<%=selDept%>&invQtyEnd=<%=invQtyEnd%>&outputType=CSV">CSV</a>
    <br>View this report in either PDF or CSV (Excel), however CSV is only supported in Firefox internet browser.
</center>
<table class="sortable, common" id="viewTEInvoices" cellpadding="0" cellspacing="0" width="95%" align="center">
    <tr>
        <th>#</th>
        <th>Name</th>
        <th>Cat#</th>
        <th>Department</th>

        <th>Price</th>
        <th>Mur/Lehi/DV</th>

        <th>Total</th>
    </tr>
    <%
        int count = 0;
        TransUtil tu = new TransUtil();
        boolean flipShade = false;
        for (Product temp : invList) {
            double tempTotal = 0;
            String tTotal = "";
            String cTotal = "";
            String lastSold = "";
            String lastInv = "";
            String trr = temp.getProductCost4();
            try {
                tempTotal = (Double.valueOf(temp.getProductCost1())
                        .doubleValue())
                        * (Integer.parseInt(trr));
                teTotal += tempTotal;
                tTotal = new java.text.DecimalFormat("$0.00")
                        .format(tempTotal);
                tempTotal = Double.valueOf(temp.getProductCost1())
                        .doubleValue();
                cTotal = new java.text.DecimalFormat("$0.00")
                        .format(tempTotal);
                grandTot = new java.text.DecimalFormat("$0.00")
                        .format(teTotal);
            } catch (Exception e) {
            }
            //2007-11-09 14:31:00.0
            SimpleDateFormat sdf2 = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss.0");
            SimpleDateFormat sdf3 = new SimpleDateFormat("MM/dd/yyyy");
            try {
                //lastSold = temp.getLastSold().substring(0, temp.getLastSold().length() -5);
                Date d1 = sdf2.parse(temp.getLastSold());
                lastSold = sdf3.format(d1);
            } catch (Exception e) {
            }
            try {
                //lastInv = temp.getLastInvDate().substring(0, temp.getLastInvDate().length() -5);
                Date d1 = sdf2.parse(temp.getLastInvDate());
                lastInv = sdf3.format(d1);
            } catch (Exception e) {
            }
            count++;
    %>
    <%
        if (flipShade) {
    %>
    <tr bgcolor="#eeeeee">
    <%
    } else {
    %><tr><%
    }
%>
    <td align="right"><%=count%>

        &nbsp;<img src="images/edit.gif"
                   style="cursor: pointer; cursor: hand" title="Edit this product"
                   onclick="jsfPopupPage('./productInvEdit.jsp?prodNum=<%=temp.getProductNum()%>&location=<%="Products"%>', 800, 500);" />


    </td>
    <td align="left"><%=temp.getProductName()%>
    <td align="left"><%=temp.getProductCatalogNum()%>
    </td>
    <%
        String name = "";
        String key = "";
        try{
            key = temp.getCategory().charAt(0)+"";
            name = (String)deptHash.get(key);
        }catch(Exception e){
        }
        if(name!=null){%>
    <%} %>


    <td><%= name %></td>
    <td align="right"><%=temp.getProductCost1()%></td>
    <td align="center"><%=temp.getNumAvailable()
            + " <br/> <font color=\"blue\">(" + trr
            + ")</font>"%></td>

    <td align="right"><%=tTotal%></td>
</tr>
    <%
            flipShade = !flipShade;
        }//end of for loop
    %>
    <%
        //get totals of all accounts from individual sums.
    %>
    <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td><%=" "%></td>
        <td><%=" "%></td>
        <td><%=" "%></td>
        <td><%=" "%></td>
        <td align="right"><%=grandTot%></td>
    </tr>

    <%
        //end of if(acc!=null)
    } else {
    %><center>
    Select date range to genterate tax exempt account data.
</center>
    <%
        }//end of else
    %>
    <%
        if (message != null) {
    %>
    <tr>
        <td><%=message%></td>
    </tr>
    <%
        }
    %>
</table>
<script>
  $(function(){
    $(":checkbox").click(function(){
      if(this.checked){
        $(":checkbox").each(function(){
          this.checked = 0;
        });
        this.checked = 1;
      }
    });
  });

  </script>
<%@include file="bottomLayout.jsp"%>

