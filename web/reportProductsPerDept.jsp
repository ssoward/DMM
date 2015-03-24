
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


%>

<h1>Products Per Dept</h1>
<p class="text">Get PDF or CSV of all products for departments</p>
<div id="msg">Select a Department</div>

<table align="center">
    <tr>
        <td>
            <form name="myform" method="post" action="./reportProductsPerDept.jsp" >
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
                    <td align="right">
                        <b><%=sup.getDepartmentName()%></b>
                    </td>
                    <td>
                        <input <%=cc%> type="radio" value="<%=sup.getDepartmentCode()%>" name="department" />
                    </td>
                    <%
                        if (countt == 3) {
                            countt = 0;
                    %>
                </tr><tr>
                <%
                    }
                %>

                <%
                    }
                %>
            </tr>

                <tr>
                    <td colspan="8">
                        <center>
                            <a id="PDF" disabled="disabled" href="#">PDF</a>
                            &nbsp;
                            <a id="CSV" disabled="disabled" href="#">CSV</a>
                            <br>View this report in either PDF or CSV (Excel), however CSV is only supported in Firefox internet browser.
                        </center>
                    </td>
                </tr>
            </form>
        </td>
    </tr>
</table>
<br/>
<script>

    var input = $('[type=radio]').click(function() {
        //alert('Handler for .click() called: '+this.value);
        $("#PDF").attr('href', 'ProductInventoryReport.pdf?reportCollection=ProductInventoryReport&outputType=PDF&perDept='+this.value);
        $("#CSV").attr('href', 'ProductInventoryReport.csv?reportCollection=ProductInventoryReport&outputType=CSV&perDept='+this.value);
        $("#CSV").attr('disabled', '');
        $("#PDF").attr('disabled', '');
        $("#msg").css("color", "#666666");
    });
//    $("#CSV").click(function(e) {
//        e.preventDefault();
//        $("#msg").css("color", "red");
//    });
//    $("#PDF").click(function(e) {
//        e.preventDefault();
//        $("#msg").css("color", "red");
//    });


</script>

</table>
<%@include file="bottomLayout.jsp"%>

