<%@page import="com.soward.object.Departments"%>
<%@page import="com.soward.util.DepartmentsUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.soward.util.Utils"%>
<%@page import="com.soward.object.Product"%>
<%@page import="com.soward.util.InventoryReport"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.soward.enums.LocationsDBName"%>
<%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.InvBundle"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.util.AccountUtil"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.util.TransUtil"%>
<%@ page import="com.soward.object.gto.ProductGto" %>
<script type="text/javascript" src="js/comboBox.js">
</script>
<SCRIPT LANGUAGE="JavaScript">
    $(document).ready(function() {
        $("tr:even").addClass("alt");

        $(".content div:odd").addClass("alt");
    });

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
	List<ProductGto> invList = null;
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
	String pdfOnly = request.getParameter("pdfOnly");
	String csvOnly = request.getParameter("csvOnly");

	String ttt = "";
	if (dateTwo == null && dateOne == null ) {
		dateTwo = endDate;
		dateOne = startDate;
	} else {
        if(pdfOnly!=null){
            String url = response.encodeRedirectURL("ProductInventoryReport.pdf?reportCollection=InvProductSoldReport&function=prodSoldInv&dateOne="+dateOne+"&dateTwo="+dateTwo+"&department="+selDept+"&invQtyEnd=SOLD");
            response.sendRedirect(url);
        }
        else if(csvOnly!=null){
            String url = response.encodeRedirectURL("ProductInventoryReport.csv?reportCollection=InvProductSoldReport&function=prodSoldInv&dateOne="+dateOne+"&dateTwo="+dateTwo+"&department="+selDept+"&invQtyEnd=SOLD&outputType=CSV");
            response.sendRedirect(url);
        }else{
		    invList = invUtil.getInventorySoldReport(dateOne, dateTwo, selDept);
        }

	}
%>

<h1>Product Sold Inventory Report</h1>
<p class="text">Generate report detailing product title, quantities, price for all products sold within the date range provided.
</p>
<table align="center">
  <tr>
    <th>Date Range</th></tr><tr>

    <form name="myform" method="post" action="./reportInvProductSold.jsp" >
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
                                CSV <input type="checkbox" name="csvOnly" value="csvOnly"> or PDF <input type="checkbox" name="pdfOnly" value="pdfOnly"> Only
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
<% if (invList != null) {%>
<br/>
<center>
  <a href="ProductInventoryReport.pdf?reportCollection=InvProductSoldReport&function=prodSoldInv&dateOne=<%=dateOne%>&dateTwo=<%=dateTwo%>&department=<%=selDept%>&invQtyEnd=<%="SOLD"%>">PDF</a>
  &nbsp;
  <a href="ProductInventoryReport.csv?reportCollection=InvProductSoldReport&function=prodSoldInv&dateOne=<%=dateOne%>&dateTwo=<%=dateTwo%>&idepartment=<%=selDept%>&invQtyEnd=<%="SOLD"%>&outputType=CSV">CSV</a>
  <br>View this report in either PDF or CSV (Excel), however CSV is only supported in Firefox internet browser.
</center>
<%
    }if (invList != null) {
		double grandTot = 0.0;
		double grandM = 0.0;
		double grandL = 0.0;
		double grandO = 0.0;
%>

<table class="common" id="viewTEInvoices" cellpadding="0" cellspacing="0" width="95%" align="center">
    <tr>
        <th>#</th>
        <th>Title</th>
        <th>Cat#</th>
        <th>Department</th>
        <th>Sold#</th>
        <th>Mur/Lehi/DV</th>
        <th>Price</th>
        <th>Murray</th>
        <th>Lehi</th>
        <th>DV</th>
        <th>Inv Total</th>
    </tr>
    <%
        int count = 1;
        TransUtil tu = new TransUtil();
        boolean flipShade = false;
        for(ProductGto gto: invList){%>
    <tr><td bgcolor="#d999" colspan="11"><%=
    "Department: "+gto.getName()
            +" <b>Murray:</b>"  +new java.text.DecimalFormat( "$0.00" ).format( gto.getmTotal())
            +" <b>Lehi:  </b>"  +new java.text.DecimalFormat( "$0.00" ).format( gto.getlTotal())
            +" <b>DV:  </b>"  +new java.text.DecimalFormat( "$0.00" ).format( gto.getoTotal())
            +" <b>Total: </b>"  +new java.text.DecimalFormat( "$0.00" ).format( gto.getTotalValue())
    %></td></tr>
    <%
        for (Product temp : gto.getProdList()) {

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
        count++;
        String name = "";
        String key = "";
        double ddd = 0.0;
        String md = "";
        String ld = "";
        String od = "";
        String bgColor = "style=\"color:black; border: solid black 1px;background:";
        try{
            key = temp.getCategory().charAt(0)+"";
            name = (String)deptHash.get(key);
            ddd = Double.parseDouble(temp.getProductCost4());
            if(ddd > 0){
                grandTot += ddd;
            }
            double ddm = Double.parseDouble(temp.getProductSupplier1());
            double ddl = Double.parseDouble(temp.getProductSupplier2());
            double ddo = Double.parseDouble(temp.getProductSupplier3());
            md = new java.text.DecimalFormat( "$0.00" ).format( ddm);
            ld = new java.text.DecimalFormat( "$0.00" ).format( ddl);
            od = new java.text.DecimalFormat( "$0.00" ).format( ddo);

            grandM += ddm;
            grandL += ddl;
            grandO += ddo;
            if(ddd>1000){bgColor += "#FF0000\"";}
            if(ddd>500 ){bgColor += "#FFCC00\"";}
            if(ddd>100 ){bgColor += "#FFFF99\"";}
            if(ddd>50  ){bgColor += "#CCFFFF\"";}
            else{bgColor = "";}

        }catch(Exception e){
        }
        if(name!=null){%>
    <%} %>


    <td><%= name %></td>
    <td align="center"><%=temp.getProductCost2()%></td>
    <td align="center"><%=temp.getNumAvailable() +" ("+temp.getProductCost3()+") "%></td>

    <td align="right"><%="$"+temp.getProductCost1()%></td>
    <td align="center"><%=md%></td>
    <td align="center"><%=ld%></td>
    <td align="center"><%=od%></td>

    <td <%=bgColor%> align="right"><%=new java.text.DecimalFormat( "$0.00" ).format( ddd )%></td>
</tr>
    <%
            flipShade = !flipShade;
        }//end of for loop
    %>
    <%
        //get totals of all accounts from individual sums.
        String rfM = new java.text.DecimalFormat( "$0.00" ).format( grandM );
        String rfL = new java.text.DecimalFormat( "$0.00" ).format( grandL );
        String rfO = new java.text.DecimalFormat( "$0.00" ).format( grandO );
        String rft = new java.text.DecimalFormat( "$0.00" ).format( grandTot );
    %>
    <tr>
        <td colspan="7">&nbsp;</td>
        <td align="right"><%=rfM%></td>
        <td align="right"><%=rfL%></td>
        <td align="right"><%=rfO%></td>
        <td align="right"><%=rft%></td>
    </tr>

    <%
            //end of if(acc!=null)
        } }else {
    %><center>
    Select date range to genterate product sold inventory report.
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

<%@include file="bottomLayout.jsp"%>

