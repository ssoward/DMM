<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.soward.object.RegLocation"%>
<%@page import="com.soward.util.RegLocationUtil"%>
<jsp:directive.page import="com.soward.object.Departments" />
<jsp:directive.page import="com.soward.util.DepartmentsUtil" />
<%@page import="com.soward.object.ProductsLocationCount"%>
<%@page import="com.soward.util.ProductsLocationCountUtil"%>
<%@page import="com.soward.ajax.InventoryUtilAjax"%><jsp:directive.page import="com.soward.object.Product"/>
<jsp:directive.page import="com.soward.util.InventoryReport"/>
<jsp:directive.page import="java.text.NumberFormat"/>
<jsp:directive.page import="java.text.DecimalFormat"/>
<jsp:directive.page import="com.soward.util.SalesReport"/>
<jsp:directive.page import="com.soward.util.Utils"/>
<jsp:directive.page import="com.soward.enums.LocationsDBName"/>
<jsp:directive.page import="java.math.BigDecimal"/>
<%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.InvBundle"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.util.AccountUtil"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.util.TransUtil"%>
<link href="css/accordion.css" rel="stylesheet" type="text/css" media="all" />
<link href="css/stylesheet.css" rel="stylesheet" type="text/css" media="all" />
<script type="text/javascript" src="js/comboBox.js"></script>
<script type="text/javascript" src="js/inventoryAjax.js"></script>
<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>

<style type="text/css">
    .slideToggle{
        float:right;
    }
    .slideTogglebox{
    }
    .clear{
        clear:both;
    }
</style>

<script type="text/javascript">
    $(document).ready(function() {
        $("#slideToggle").click(function () {
            $('.slideTogglebox').slideToggle();
        });
    });
</script>
<SCRIPT LANGUAGE="JavaScript">
    function IsNumeric(strString)
        //  check for valid numeric strings
    {
        var strValidChars = "0123456789.- AMPM:";
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
            alert('Date must be in form: MM-dd-yyyy \nNot Numeric in: '+dt.value);
            dt.focus();
            return;
        }
        else if(dt.value.charAt(2)=="-"&& dt.value.charAt(5)=="-"){
            if(dtt.value.charAt(2)=="-"&& dtt.value.charAt(5)=="-"){
                document.myform.submit();
            }
            else{
                alert('Date must be in form: MM-dd-yyyy \nMisplaced - in: ' +dtt.value);
                dtt.focus();
                return;
            }
        }
        else{
            alert('Date must be in form: MM-dd-yyyy \nMisplaced - in: '+dt.value);
            dt.focus();
            return;
        }
    }

</script>
<SCRIPT LANGUAGE="JavaScript" SRC="css/_CalendarPopup.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"> var cal = new CalendarPopup(); </SCRIPT>


<%
    List<Departments> allDepartments = DepartmentsUtil.getAllDepartments();
    HashMap<String, String> deptHash = new HashMap<String, String>();
    HashSet invSet = null;

    String department      = request.getParameter("department")!=null?request.getParameter("department"):"0";
    SalesReport salesReport = new SalesReport();
    List<Product> salesList = null;
    TimeZone tz = TimeZone.getTimeZone( "America/Salt_Lake" );
    Calendar cal = Calendar.getInstance();
    cal.set( Calendar.HOUR_OF_DAY, 0 );
    cal.set( Calendar.MINUTE, 0 );
    HashMap<String, String> otherLocationHM = new HashMap<String, String>();
    HashMap<String, String> invGrandTotalList = new HashMap<String, String>();
    HashMap<String, String> invSupplierTotalList = new HashMap<String, String>();
    HashMap<String, ProductsLocationCount> countHash = new HashMap<String, ProductsLocationCount>();
    SimpleDateFormat sdf = new SimpleDateFormat( "MM-dd-yyyy HH:mm" );

    String startDate = sdf.format( cal.getTime() );
    cal.set( Calendar.HOUR_OF_DAY, 23 );
    cal.set( Calendar.MINUTE, 59 );
    String endDate = sdf.format( cal.getTime() );

    for(Departments dept: allDepartments){
        deptHash.put(dept.getDepartmentCode(), dept.getDepartmentName());
    }

    String message = request.getParameter( "message" );
    String regLocation = request.getParameter( "regLocation" );
    String sortBy = request.getParameter( "sortBy" );
    String reportType = request.getParameter( "reportType" );
    String SHOnly = request.getParameter( "SHOnly" );
    String pastYearsSold = request.getParameter( "pastYearsSold" );
    HashMap<String, HashMap<String, String>> invMap = new HashMap<String, HashMap<String, String>>();
    String locationName = request.getParameter( "locationName" );
    try{
        if(!StringUtils.isBlank(locationName)){
            invMap = (HashMap)request.getSession().getAttribute(locationName);
        }
    }catch(Exception e){e.printStackTrace();}
    try{
        invSet = (HashSet)request.getSession().getAttribute(InventoryUtilAjax.key);
    }catch(Exception e){e.printStackTrace();}
    invSet = invSet!=null?invSet:new HashSet();
    invMap = invMap!=null?invMap:new HashMap();

    String dateOne = request.getParameter( "dateOne" );
    String dateTwo = request.getParameter( "dateTwo" );
    BigDecimal taxSupSum = new BigDecimal( 0 );
    BigDecimal totSupSum = new BigDecimal( 0 );
    BigDecimal taxSum = new BigDecimal( 0 );
    BigDecimal totSum = new BigDecimal( 0 );
//HashMap<String, BigDecimal> taxList   = new HashMap<String, BigDecimal>();
    HashMap<String, Invoice> totalList = new HashMap<String, Invoice>();
    boolean isOnline = false;
    boolean shippingOnly = SHOnly!=null&&SHOnly.equals("SHOnly")?true:false;
    boolean pastYearsSoldBool = pastYearsSold!=null&&pastYearsSold.equals("pastYearsSold")?true:false;
    ArrayList<RegLocation> rList = RegLocationUtil.getRegLocations();
    RegLocation blankReg = new RegLocation();
    blankReg.setRegDesc("Or leave blank");
    blankReg.setRegName("Select register");
    rList.add(0,blankReg);
    String orderTypeChecked = "";
    String paymentTypeChecked = "";
    String featureTypeChecked = "";
    String supplierChecked = "checked=\"checked\"";
    String consolidatedChecked ="checked=\"checked\"";
    String invoiceListChecked = "";
    boolean consolidatedList = reportType==null||reportType.equals("consol")?true:false;
    if(!consolidatedList){
        consolidatedChecked = "";
        invoiceListChecked = "checked=\"checked\"";
    }

// supplier = 0;
// order    = 1;
// payment  = 2; 
    int sortByType = 0;
    if ( sortBy != null ) {
        if ( sortBy.equals( "orderType" ) ) {
            sortByType = 1;
            supplierChecked = "";
            orderTypeChecked = "checked=\"checked\"";
        }else if(sortBy.equals( "paymentType" )){
            sortByType = 2;
            supplierChecked = "";
            paymentTypeChecked = "checked=\"checked\"";
        }else if(sortBy.equals( "featureType" )){
            sortByType = 3;
            supplierChecked = "";
            featureTypeChecked = "checked=\"checked\"";
        }
    }

    if ( locationName != null && locationName.equalsIgnoreCase( "103" ) ) {
        isOnline = true;
    }

    if ( dateTwo == null && dateOne == null && locationName == null ) {
        dateTwo = endDate;
        dateOne = startDate;
    }else {
        salesList = salesReport.getSalesReport( dateOne, dateTwo, locationName, sortByType, regLocation, consolidatedList, shippingOnly, pastYearsSoldBool, department );
        totalList = SalesReport.getTotalForInvoices( dateOne, dateTwo, shippingOnly);
        if ( salesList == null ) {
            message = "Date range is too great. 4 days is max range.";
        } else if ( locationName != null && !locationName.equalsIgnoreCase( "ONLINE" ) ) {
            countHash = ProductsLocationCountUtil.getHashForList(salesList);
        }
    }
    try{
%>
<br/>
<h1>Daily Sales Report</h1>
<div class="clear">
    <img src="images/openClose.jpg" class="slideToggle" id="slideToggle"/>
    <div class="slideTogglebox">

        <br/>
        <p class="text">Get Sales report for either Murray or Lehi by selecting a date range and location. Click on the calendar icons or enter dates to specify a date range.</p>
        <table align="center">
            <tr>

                <form name="myform" method="post" action="./reportSales.jsp" >

                    <td colspan="5">
                        <table cellpadding="0" cellspacing="0" border="1" width="400"> <tr>
                            <tr><td>
                                <table cellpadding="3" cellspacing="3" border="0" width="100%"> <tr>
                                    <tr>
                                        <th colspan="2">Date Range</th></tr>
                                    <TD align="right">Start date: </td><td>
                                        <A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateOne"%>,'<%="anchor"%>','MM-dd-yyyy 00:00'); return false;"
                                           NAME="<%="anchor"%>" ID="<%="anchor"%>">
                                            <img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;
                                        <input size=20 type="text" value="<%=dateOne%>" name="dateOne">
                                    </td>
                                    </tr>
                                    <tr>
                                        <TD align="right">  End date: </td><td>
                                        <A HREF="#" onClick="cal.select(document.forms['<%="myform"%>'].<%="dateTwo"%>,'<%="anchor"%>','MM-dd-yyyy 23:59'); return false;"
                                           NAME="<%="anchor"%>" ID="<%="anchor"%>">
                                            <img width="25" src="images/calendar.jpg" border="0"/></A>&nbsp;
                                        <input size=20 type="text" value="<%=dateTwo%>" name="dateTwo">
                                    </td>
                                    </tr>
                                    <tr> <td align="right" colspan="1" valign="top"> Report Type: </td>
                                        <td colspan="1" align="center">
                                            <table border="0" cellspacing="0" cellpadding="2" width="100%">
                                                <tr> <td  align="right" width="25">
                                                    <input onclick="document.getElementById('department').disabled=true;document.myform.sortBy[3].disabled=true;document.myform.sortBy[1].disabled=true;document.myform.sortBy[0].disabled=true;document.myform.sortBy[2].disabled=true;document.myform.sortBy[0].checked=checked" type="radio" <%=consolidatedChecked%> value="consol" name="reportType">
                                                </td> <td  align="left">
                                                    Consolidated Products
                                                </td></tr>
                                                <tr> <td  align="right">
                                                    <input onclick="document.getElementById('department').disabled=false;document.myform.sortBy[3].disabled=false;document.myform.sortBy[1].disabled=false;document.myform.sortBy[0].disabled=false;document.myform.sortBy[2].disabled=false;"
                                                           type="radio" <%=invoiceListChecked%>  value="invoiceListReport" name="reportType">
                                                </td> <td  align="left">
                                                    Invoice List <font size="1">(Use for Sort By & Dept)</font>
                                                </td> </tr>
                                            </table>
                                        </td> </tr>
                                    <tr> <td align="right" colspan="1" valign="top"> Sort By: </td>
                                        <td colspan="1" align="center">
                                            <table border="1" cellspacing="0" cellpadding="0" width="100%">
                                                <tr><td>
                                                    <table border="0" cellspacing="1" cellpadding="2" width="100%">
                                                        <tr> <td  align="right" width="25">
                                                            <input type="radio" <%=invoiceListChecked.length()<1?"disabled":"" %> <%=supplierChecked%> value="supplier" name="sortBy">
                                                        </td> <td  align="left">
                                                            Supplier Name
                                                        </td></tr>
                                                        <tr> <td  align="right">
                                                            <input type="radio" <%=invoiceListChecked.length()<1?"disabled":"" %> <%=orderTypeChecked%>  value="orderType" name="sortBy">
                                                        </td> <td  align="left">
                                                            Order Type
                                                        </td> </tr>
                                                        <tr> <td  align="right">
                                                            <input type="radio" <%=invoiceListChecked.length()<1?"disabled":"" %> <%=paymentTypeChecked%>  value="paymentType" name="sortBy">
                                                        </td> <td  align="left">
                                                            Payment Type
                                                        </td> </tr>
                                                        <tr> <td  align="right">
                                                            <input type="radio" <%=invoiceListChecked.length()<1?"disabled":"" %> <%=featureTypeChecked%>  value="featureType" name="sortBy">
                                                        </td> <td  align="left">
                                                            Room Location
                                                        </td> </tr>
                                                    </table>
                                                </td></tr>
                                            </table>
                                        </td> </tr>
                                    <tr> <td  align="right">
                                        Dept:
                                    </td><td><select <%=invoiceListChecked.length()<1?"disabled":"" %> id="department" name="department">
                                        <%String key = "";
                                            if(!StringUtils.isBlank(department)){
                                                key = department;
                                            }
                                            for(Departments sup: allDepartments){
                                                String ssel = "";
                                                if(StringUtils.isBlank(sup.getDepartmentName())){
                                                    continue;
                                                }
                                                if(sup.getDepartmentCode().equals(key)){
                                                    ssel = "selected=\"selected\"";
                                                }%>
                                        <option <%=ssel%> value="<%= sup.getDepartmentCode() %>"><%=sup.getDepartmentName()%></option>
                                        <%} %>
                                    </select></td></tr>

                                    <tr>
                                        <td  align="right">
                                            Register:
                                        </td><td align="left">
                                        <select name="regLocation" value="test">
                                            <%try{
                                                String regLocationOptions = "";
                                                regLocation= regLocation!=null?regLocation:"";
                                                for(RegLocation regl: rList){
                                                    if(regLocation.equals(regl.getRegNum())){
                                                        regLocationOptions += "<option selected value="+regl.getRegNum()+">"+regl.getRegName()+"</option>";
                                                    }else{
                                                        regLocationOptions +="<option value="+regl.getRegNum()+">"+regl.getRegName()+"</option>";
                                                    }
                                                }%><%=regLocationOptions %>
                                            <%}catch(Exception e){e.printStackTrace();} %>
                                        </select>
                                    </td></tr>

                                    <tr>
                                        <td  align="right">
                                            Store:
                                        </td><td align="left">
                                        <select name="locationName" value="test">
                                            <%
                                                if ( locationName != null && locationName.length() > 0 ) {
                                                    LocationsDBName lname = LocationsDBName.valueOf( locationName );
                                            %>
                                            <option value="<%=lname.name()%>"><%=lname.displayName()%></option>
                                            <%
                                                }
                                                for ( LocationsDBName lname : LocationsDBName.values() ) {
                                            %>
                                            <option value="<%=lname.name()%>"><%=lname.displayName()%></option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </td></tr>

                                    <tr><td colspan=4 align=center>
                                        <input type="button" class="btn" onclick="ValidateForm()" name="Enter" value="Enter" >
                                    </td></tr>

                                    <tr><TD align="right">Show past year's sold</td><td>

                                        <input size=15 type="checkBox" <%if(pastYearsSoldBool){%><%="checked=\"checked\""%><%}%> value="pastYearsSold" name="pastYearsSold">
                                    </td>
                                    </tr>
                                    <tr><TD align="right">  Shipping and Handling ONLY </td><td>

                                        <input size=15 type="checkBox" <%if(shippingOnly){%><%="checked=\"checked\""%><%}%> value="SHOnly" name="SHOnly">
                                    </td>
                                    </tr>
                                </table>
                            </td></tr>
                        </table>

                    </td>      			</tr>
            <tr><td colspan=8>
                <hr>
            </td>
            </tr>
        </table>
        </form>
        </center>
    </div>
</div>
<%
    if ( salesList != null ) {
        HashMap<String, String> hm = TransUtil.getAllHBHash();
        if ( salesList.size() > 0 ) {
            double teTotal = 0.0;
            String grandTot = "";
            String supplierName = "";
            boolean newSupplier = false;
            int supCount = 0;
%>
<center>
    <b>
        <a href="DailySales.pdf?reportCollection=DailySalesReport&dOne=<%=dateOne%>&reportType=<%=reportType%>&dTwo=<%=dateTwo%>&lName=<%=locationName%>">PDF</a>
        &nbsp;
        <a href="DailySales.csv?reportCollection=DailySalesReport&dOne=<%=dateOne%>&reportType=<%=reportType%>&dTwo=<%=dateTwo%>&lName=<%=locationName%>&outputType=CSV">CSV</a>
    </b>
    <br>View this report in either PDF or CSV (Excel), however CSV is only supported in Firefox internet browser.
</center>
<table class="common" id="viewTEInvoices" align="center" cellpadding="0" cellspacing="0" width="780">
    <tr>
        <th colspan="4">
            <div id="weatherReport" class="outputTextArea"></div>
            <b>Report run for <%=locationName%> from <%=dateOne + " to " + dateTwo%></b></th>
    </tr>
    <%
        int count = 0;
        TransUtil tu = new TransUtil();
        boolean flipShade = false;
        boolean firstTable = true;
        // ========= START FOR ===========================================
        // ========= START FOR ===========================================
        for ( int i = 0; i < salesList.size(); i++ ) {
            Product temp = salesList.get( i );
            double tempTotal = 0;
            String tTotal = "";
            String cTotal = "";
            String lastSold = "";
            String lastInv = "";
            newSupplier = supplierName.equals( temp.getSupplier().getSupplierName() ) ? false : true;
            supplierName = supplierName.equals( temp.getSupplier().getSupplierName() ) ? supplierName : temp.getSupplier()
                    .getSupplierName();
            try {
                tempTotal = ( Double.valueOf( temp.getProductCost1() ).doubleValue() ) * ( countHash.get(temp.getProductNum()).getLocation(locationName) );
                teTotal += tempTotal;
                tTotal = new java.text.DecimalFormat( "$0.00" ).format( tempTotal );
                tempTotal = Double.valueOf( temp.getProductCost1() ).doubleValue();
                cTotal = new java.text.DecimalFormat( "$0.00" ).format( tempTotal );
                grandTot = new java.text.DecimalFormat( "$0.00" ).format( teTotal );
            } catch ( Exception e ) {
            }
            try {
                lastSold = temp.getLastSold().substring( 0, temp.getLastSold().length() - 5 );
            } catch ( Exception e ) {
            }
            try {
                lastInv = temp.getLastInvDate().substring( 0, temp.getLastInvDate().length() - 5 );
            } catch ( Exception e ) {
            }
            count++;
    %>

    <%
        if ( newSupplier ) {
            count = 1;
            supCount++;
            newSupplier = false;

            if ( !firstTable){
                if(!consolidatedList ) {
    %><tr><td colspan="10" align="right">Total (for unique invoices):</td>
    <td colspan="1" align="left"><%=bdec.format(taxSupSum)%></td>
    <td colspan="1" align="left"><%=bdec.format(totSupSum)%></td>
    <td colspan="3"></td>

</tr>
    <% }%>
</table></td></tr>
<%}

    taxSupSum = new BigDecimal( 0 );
    totSupSum = new BigDecimal( 0 );
    //reset the sup list only, invoice number dups can occur from multiple suppliers just not within the same supplier.
    invSupplierTotalList = new HashMap<String, String>();
%>
<tr>
    <td align="left"><font color="blue" size="2"><%=temp.getSupplier().getSupplierName() + "    " + temp.getSupplier().getSupplierPhone()%></font>
    </td>
    <%if(temp!=null&&temp.getSupplier()!=null&&!StringUtils.isBlank(temp.getSupplier().getSupplierName())){%>
    <td><div id="<%="description"+temp.getSupplier().getSupplierNum()%>"/></td>
    <td align="right">
    </td>
    <%}else{%>
    <td></td><td></td>
    <%} %>
</tr>
<tr>
<td colspan="4">
<table class="common" id="viewTEInvoicesTWO" align="left" width="100%" cellpadding="0" cellspacing="0" style="width: 100%">
<tr>
    <% if(!consolidatedList){%>
    <th style="width: 100px;">&nbsp;#&nbsp;</th>
    <%}%>
    <th>&nbsp;Catalog #&nbsp;</th>
    <th>&nbsp;Name&nbsp;</th>
    <%if(pastYearsSoldBool){ %>
    <th>&nbsp;Sold&nbsp;</th>
    <%}else{%>
    <th>&nbsp;DMM&nbsp;</th>
    <%}%>
    <th>&nbsp;Desc&nbsp;</th>
    <th>&nbsp;Sld/In&nbsp;</th>
    <th>&nbsp;HB&nbsp;</th>
    <%
        //get qty of other location
        if ( !isOnline ) {
            for ( LocationsDBName lname : LocationsDBName.values() ) {
                if ( !"ONLINE".equals( lname.name() ) && !lname.name().equals( locationName ) ) {
    %>

    <th>&nbsp;<%=lname.name().substring(0,1)%> &nbsp;</th>

    <%
                }
            }
        }
        if(!consolidatedList){
    %>
    <th>&nbsp;Tax&nbsp;</th>
    <th>&nbsp;Total&nbsp;</th>
    <th>&nbsp;Invoice #&nbsp;</th>
    <th>&nbsp;Date&nbsp;</th>
    <th>&nbsp;Account #&nbsp;</th>
    <%} %>
</tr>
<% }
    firstTable = false;
%>

<% //start rows 777 ================================================ %>

<%if ( flipShade ) { %> <tr style="background-color:#aaccff;background-image:none;"> <%} else{%><tr><% }%>
    <%
        String invIndexCheck = "";
        String invIndexCheckFont = Utils.breakString(temp.getProductCatalogNum());
        boolean beenThere = false;
        if(invSet.contains(temp.getProductSKU())){
            invIndexCheck = "CHECKED=CHECKED";
            invIndexCheckFont = "<font color=\"blue\">"+invIndexCheckFont+"</font>";
            beenThere = true;
        }
    %>

    <%if(!consolidatedList){%>
    <td align="right">
        <div class="invEval0010" id="<%="description"+temp.getProductNum()+"history"%>">

        </div>
        <div class="invEval001">

            <div class="invEval002" id="<%="description"+temp.getProductNum()%>"></div>
            <div class="invEval0011">
                <table>
                    <tr><td>
                        <input onclick="makeGetRequestFetchAnnualHist('<%=temp.getProductNum()%>', '<%=locationName%>');" type="image"
                               title="Get past year's sold count for <%=temp.getProductName()%> for <%=locationName %> (from today to last year at this time)." alt="Save" width="20" src="images/history.png">
                </table>
            </div>
        </div>

    </td>
    <%}%>
    <div id="<%="invCount"+temp.getProductNum() %>">
            <%if(!consolidatedList){%>

        <td align="left"><%=invIndexCheckFont%></td>
        <td width="100"><%=Utils.breakString(temp.getProductName())%></td>
        <td align="left">

            <%if(pastYearsSoldBool){
                String m12 = temp.getYearsSold();
                String m06 = temp.getSixMonthsSold();
                String m01 = temp.getOneMonthSold();
                int iiii = 0;
                int rrrr = 0;
                int oooo = 0;
                if(!StringUtils.isBlank(m01)){
                    iiii = Integer.parseInt(m01);
                }
                if(!StringUtils.isBlank(m06)){
                    rrrr = Integer.parseInt(m06);
                }
                if(!StringUtils.isBlank(m12)){
                    oooo = Integer.parseInt(m12);
                }
            %>
            <%=InventoryUtilAjax.getYearSold(iiii, 1) %>
            <%=InventoryUtilAjax.getYearSold(rrrr, 6) %>
            <%=InventoryUtilAjax.getYearSold(oooo, 12) %>

            <%}else{ %>
            <%=Utils.breakString(temp.getProductSKU())%></td>
            <%}%>


        </td>
        <td width="100"><%=Utils.breakString(temp.getProductDescription())%></td>
            <%}else{%>
        <td align="left"><%=invIndexCheckFont%></td>
        <td width="100"> <%=(temp.getProductName())%></td>
        <td align="left">

            <%if(pastYearsSoldBool){
                String m12 = temp.getYearsSold();
                String m06 = temp.getSixMonthsSold();
                String m01 = temp.getOneMonthSold();
                int iiii = 0;
                int rrrr = 0;
                int oooo = 0;
                if(!StringUtils.isBlank(m01)){
                    iiii = Integer.parseInt(m01);
                }
                if(!StringUtils.isBlank(m06)){
                    rrrr = Integer.parseInt(m06);
                }
                if(!StringUtils.isBlank(m12)){
                    oooo = Integer.parseInt(m12);
                }
            %>
            <%=InventoryUtilAjax.getYearSold(iiii, 1) %>
            <%=InventoryUtilAjax.getYearSold(rrrr, 6) %>
            <%=InventoryUtilAjax.getYearSold(oooo, 12) %>

            <%}else{ %>
            <%=Utils.breakString(temp.getProductSKU())%></td>
            <%}%>


        </td>
        <td width="100"> <%=(temp.getProductDescription())%></td>
            <%}

            String hbin = hm.get(temp.getProductNum());
            hbin = hbin!=null?hbin:"0";

            %>
        <td align="center"><%=temp.getTransaction().getProductQty()
                + " / " + countHash.get(temp.getProductNum()).getLocation(locationName)%></td>
        <td align="center"><%=hbin %></td>
            <%
            //get qty of other location
            String qq = "";
            if ( !isOnline ) {
            for ( LocationsDBName lname : LocationsDBName.values() ) {
            if ( !"ONLINE".equals( lname.name() ) && !lname.name().equals( locationName ) ) {
            try {
            qq = countHash.get( temp.getProductNum() ).getLocation( lname.capName() )+"";
            } catch ( Exception e ) {
            qq = "<font size=1 color=red>Not Found</font>";
            }
            %>
        <td align="center">&nbsp;<%=qq%>&nbsp;</td>
            <%
            }
            }
            }
            %>
            <% if(!consolidatedList){
            String iivv = temp.getTransaction().getInvoiceNum();
            BigDecimal tempTaxSum = totalList.get( iivv )!=null?totalList.get( iivv ).getInvoiceTotalTaxBD(): new BigDecimal(0);
            BigDecimal tempTotSum = totalList.get( iivv )!=null?totalList.get( iivv ).getInvoiceTotalBD()   : new BigDecimal(0);
            //place all invoice nums in a hash to prevent adding > 1
            if ( !invSupplierTotalList.containsKey( iivv ) ) {
            invSupplierTotalList.put( iivv, iivv );
            taxSupSum = taxSupSum.add( tempTaxSum );
            totSupSum = totSupSum.add( tempTotSum );
            }

            //place all invoice nums in a hash to prevent adding > 1
            if ( !invGrandTotalList.containsKey( iivv ) ) {
            taxSum = taxSum.add( tempTaxSum );
            totSum = totSum.add( tempTotSum );
            invGrandTotalList.put( iivv, iivv );
            }
            %>
        <td> <%=bdec.format(tempTaxSum)%></td>
        <td> <%=bdec.format(tempTotSum)%></td>
        <td align=center><input name="" class="btn" type=button value="<%=iivv%>" onclick="jsfPopupPage('./viewInvoicePopup.jsp?pid=<%=iivv%>', 700, 500);" ></td>
        <td><%=( temp.getTransaction().getTransDateFormatted() )%></td>
        <td align="center">
            <a href="./editAccounts.jsp?pid=<%=( temp.getTransaction().getAccount().getAccountNum() )%>">
                <%=( temp.getTransaction().getAccount().getAccountNum() )%></a></td>

</tr>

<%}%>
<%if ( flipShade ) { %> <tr style="background-color:#aaccff;background-image:none;"> <%} else{%><tr><% }%>
    <td colspan="1" align="left">
        Done
        <input id="<%=count+""+temp.getProductNum() %>" onclick="makeGetRequest3('<%=temp.getSupplier().getSupplierNum()%>', '<%=temp.getProductNum()%>', '<%=temp.getProductNum() %>', '<%=locationName%>', this.value);" <%=invIndexCheck %>  type="checkbox"/>
    </td><td colspan="2">
        Order
        <select onchange="getElementById('<%=count+""+temp.getProductNum() %>').checked=true;makeGetRequest4('<%=temp.getSupplier().getSupplierNum()%>', '<%=temp.getProductNum()%>', this.value, '<%=locationName%>')">
            <%
                HashMap<String, String> invSupMap = invMap.get(temp.getSupplier().getSupplierNum());
                invSupMap = invSupMap!=null?invSupMap:new HashMap<String, String>();
                for(int v = 0; v < 40; v++){
                    String sss = "";
                    if(invSupMap.containsKey(temp.getProductNum())){
                        if(v == Integer.parseInt((String)invSupMap.get(temp.getProductNum()))){
                            sss = "selected";
                        }
                    }
            %>
            <option <%=sss %> value="<%=v%>"><%=v%></option>
            <%} %>
        </select>
    </td>

    <td colspan="4" align="right">
        Move
        <select id="<%=temp.getProductNum()+"moveC"%>">
            <% for(int v = 0; v < 99; v++){%>
            <option <%=v %> value="<%=v%>"><%=v%></option>
            <%} %>
        </select>
        from
        <%
            String moveStr = "selected=\"selected\" ";
        %>
        <select id="<%=temp.getProductNum()+"moveOption"%>">
            <option value="">Select Move</option>
            <option <%=locationName.equals("LEHI")?moveStr:""%> value="0">Murray to Lehi</option>
            <option <%=locationName.equals("OREM")?moveStr:""%> value="1">Murray to DV</option>
            <option value="2">DV to Lehi</option>
            <option value="4">Lehi to Murray</option>
            <option value="3">DV to Murray</option>
            <option value="5">Lehi to DV</option>
        </select>
        <input onclick="getElementById('<%=count+""+temp.getProductNum() %>').checked=true;document.getElementById('<%="moveInv"+temp.getProductNum()%>').innerHTML = '';makeGetRequestMoveInv('<%=locationName %>', '<%=temp.getProductNum()%>', document.getElementById('<%=temp.getProductNum()+"moveC"%>').value, document.getElementById('<%=temp.getProductNum()+"moveOption"%>').value, '<%=username %>');"
               type="image" style="display: inline-block; vertical-align: middle" title="Save inventory move for <%=temp.getProductName()%>" alt="Save" width="20" src="images/save.gif">
    </td><td colspan="10">
        <div class="invEval002" id="<%="moveInv"+temp.getProductNum()%>"></div>
    </td></tr>
<%
    flipShade = !flipShade;
    //get last object in salesList
    if ( !( i + 1 < salesList.size() ) ) {
        if(!consolidatedList){

%>
<tr><td colspan="9" align="right">Total (for unique invoices):</td>
    <td colspan="1" align="left"><%=bdec.format(taxSupSum)%></td>
    <td colspan="1" align="left"><%=bdec.format(totSupSum)%></td>
    <td colspan="4"></td>
</tr>
<tr><td colspan="9" align="right"><b>Grand Total (for unique invoices):</b></td>
    <td colspan="1" align="left"><%=bdec.format(taxSum)%></td>
    <td colspan="1" align="left"><%=bdec.format(totSum)%></td>
    <td colspan="4"></td>
</tr>
<% }} //end rows 777 ================================================
%>
<%

    }//end of for loop
}//end of resultlist.size>0
else {
%>
<center><font size="2" color="red">No data found for date range.</font> </center>
<%
    }
} else {
%><center>
    Select date range to genterate location specific sales data.
</center>


<%
    }//end of else
%>
<%
    if ( message != null ) {
%>
<br>
<center><font color="red" size="4"><%=message%></font></center>
<%
    }}catch(Exception e){e.printStackTrace();%>An Exception occured: <%=e.toString() %><%}%>
</table>
</table>
</td></tr>
<%@include file="bottomLayout.jsp"%>

