<link href="css/stylesheet.css" rel="stylesheet" type="text/css" />

 
<div class="menubar" id="chromemenu">
<ul>
<li><a href="./index.jsp">Home</a></li>
<%if(isAdmin){ %>
<li><a href="#" rel="dropmenu1">Admin</a></li>
<%} %>
<li><a href="#" rel="dropmenu2">Traffic</a></li>
<li><a href="#" rel="dropmenu3">Accounts</a></li>
<li><a href="#" rel="dropmenu4">Invoices</a></li>
<li><a href="#" rel="dropmenu5">Suppliers</a></li>
<li><a href="#" rel="dropmenu6">Reports</a></li>
<li><a href="#" rel="dropmenu7">Products</a></li> 
</ul>
</div>


<%if(isAdmin){ %>
<!--1st drop down menu -->                                                   
<div id="dropmenu1" class="dropmenudiv">
<a href="./viewUser.jsp">Users</a>
<a href="./newUser.jsp">New User</a>
<a href="./editPages.jsp?page=1">Edit Home Page</a>
<a href="./editPages.jsp?page=2">Edit Data Page</a>
<a href="./query.jsp">Query Browser</a></div>
<%} %>

<!--2nd drop down menu -->                                                   
<div id="dropmenu2" class="dropmenudiv"> 
<a href="./viewTraffic.jsp">Web Traffic</a></div>
<!--2nd drop down menu -->                                                   
<div id="dropmenu3" class="dropmenudiv">
<a href="./searchAccounts.jsp">Account Search</a></div>
<!--2nd drop down menu -->                                                   
<div id="dropmenu4" class="dropmenudiv">
<a href="./searchInvoices.jsp">Invoice Search</a></div>
<!--2nd drop down menu -->                                                   
<div id="dropmenu5" class="dropmenudiv">
<a href="./searchSupplier.jsp">Supplier Search</a></div>
<!--2nd drop down menu -->                                                   
<div id="dropmenu6" class="dropmenudiv">
<a href="./reportSales.jsp">Sales</a>
<a href="./reportTEAccounts.jsp">TE Accounts Spending</a>
<a href="./reportHoldBin.jsp">Hold Bin</a>
<a href="./reportSchoolCollection.jsp">School Aging</a>
<a href="./reportNonSchoolCollection.jsp">Non-School Aging</a>
<a href="./reportTEInvoices.jsp">Tax Exempt Invoices</a>
<a href="./reportProductInventory.jsp">Product Inventory</a>
<a href="./reportProductSold.jsp">Product Sold</a></div>
<!--2nd drop down menu -->                                                   
<div id="dropmenu7" class="dropmenudiv">
<a href="./inventoryManagement.jsp">Edit/Inventory</a>
<a href="./createProduct.jsp">Create New</a></div>
<!--script type="text/javascript">

cssdropdown.startchrome("chromemenu")

</script-->
