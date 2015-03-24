<%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.TransUtil"%>
<%@page import="com.soward.object.Invoice"%>
<%@page import="com.soward.object.Account"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<script src="lib/js/PurchaseOrderApp.js"></script>
<script src="lib/js/controllers/PurchaseOrderController.js"></script>

<jsp:directive.page import="com.soward.enums.LocationsDBName"/>
<iframe src="purchaseOrder.html" width="100%" height="500px"></iframe>
<%@include file="bottomLayout.jsp"%>
