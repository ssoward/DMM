<ul id="nav">
    <%if(isAdmin){ %>
    <li><a class="down" href="#">Admin<!--[if gte IE 7]><!--></a><!--<![endif]-->
        <!--[if lte IE 6]><table><tr><td><![endif]--><ul>
            <li><a href="./viewUser.jsp">Users</a></li>
            <li><a href="./newUser.jsp">New User</a></li>
            <li><a href="./editPages.jsp?page=1">Edit Home Page</a></li>
            <li class="last"><a href="./query.jsp">Query Browser</a></li>
        </ul><!--[if lte IE 6]></td></tr></table></a><![endif]-->
    </li>
    <%} %>

    <li><a class="down" href="#">Accounts<!--[if gte IE 7]><!--></a><!--<![endif]-->
        <!--[if lte IE 6]><table><tr><td><![endif]--><ul>
            <li><a href="./ajs_accounts.jsp">Account Merging</a></li>
            <li class="last"><a href="./searchAccounts.jsp">Account Search</a></li>
        </ul><!--[if lte IE 6]></td></tr></table></a><![endif]-->
    </li>
    <li><a class="down" href="#">Invoices<!--[if gte IE 7]><!--></a><!--<![endif]-->
        <!--[if lte IE 6]><table><tr><td><![endif]--><ul>
            <li class="last"><a href="./searchInvoices.jsp">Invoice Search</a></li>
        </ul><!--[if lte IE 6]></td></tr></table></a><![endif]-->
    </li>
    <li><a class="down" href="#">Suppliers<!--[if gte IE 7]><!--></a><!--<![endif]-->
        <!--[if lte IE 6]><table><tr><td><![endif]--><ul>
            <li class="last"><a href="./searchSupplier.jsp">Supplier Search</a></li>
        </ul><!--[if lte IE 6]></td></tr></table></a><![endif]-->
    </li>
    <li><a class="down" href="#">Reports<!--[if gte IE 7]><!--></a><!--<![endif]-->
        <!--[if lte IE 6]><table><tr><td><![endif]--><ul>
            <li> <a href="ajs_dailySales.jsp#/home"         >Daily Sales Phil</a></li>
            <li> <a href="./reportAging.jsp"                  >Aging</a></li>
            <li> <a href="./reportHoldBin.jsp"                >Hold Bin</a></li>
            <li> <a href="./reportInvTransfers.jsp"           >Inv Transfers</a></li>
            <li> <a href="./reportInvProductSold.jsp"         >Inventory Prod Sold</a></li>
            <li> <a href="././reportReversedInv.jsp"          >Invoice Reversed</a></li>
            <li> <a href="./reportProductInventory.jsp"       >Product Inventory</a></li>
            <li> <a href="./reportProductsPerDept.jsp"        >Products Per Dept</a></li>
            <li> <a href="./reportProductValidate.jsp"        >Product Validate</a></li>
            <li> <a href="./reportSales.jsp"                  >Sales</a></li>
            <li> <a href="./reportProductSold.jsp"            >Sold Per Dept</a></li>
            <li> <a href="./reportStockOrder.jsp"             >Stock Order (Kelly's Tab)</a></li>
            <li> <a href="./reportTEAccounts.jsp"             >TE Accounts Spending</a></li>
            <li class="last"> <a href="./reportTEInvoices.jsp">TE Invoices</a></li>
        </ul><!--[if lte IE 6]></td></tr></table></a><![endif]-->
    </li>
    <li><a class="down" href="#">Orders<!--[if gte IE 7]><!--></a><!--<![endif]-->
        <!--[if lte IE 6]><table><tr><td><![endif]--><ul>
            <li><a href="ag.jsp#/home">Purchace Order</a></li>
            <li><a href="./dailyOrderUtil.jsp">Special Order</a></li>
            <li><a href="./orders.jsp">Daily Order</a></li>
            <li><a href="./reportOrders.jsp">Requested</a></li>
            <li class="last"><a href="./reportOrders.jsp?rbool=true">Received</a></li>
        </ul><!--[if lte IE 6]></td></tr></table></a><![endif]-->
    </li>
    <li><a class="down" href="#">Products<!--[if gte IE 7]><!--></a><!--<![endif]-->
        <!--[if lte IE 6]><table><tr><td><![endif]--><ul>
            <li><a href="./inventoryManagement.jsp">Receiving</a></li>
            <li><a href="./consolidateProducts.jsp">Consolidate</a></li>
            <li><a href="ajs_products.jsp#/home">Products</a></li>
            <li class="last"><a href="./createProduct.jsp">Create New</a></li>
        </ul><!--[if lte IE 6]></td></tr></table></a><![endif]-->
    </li>
    <li><a class="down" href="#">SMAuth<!--[if gte IE 7]><!--></a><!--<![endif]-->
        <!--[if lte IE 6]><table><tr><td><![endif]--><ul>
            <li class="last"><a href="./sma.jsp#/home">Sync SMusic</a></li>
        </ul><!--[if lte IE 6]></td></tr></table></a><![endif]-->
    </li>
</ul>
<div id="content-main">
    <div class="margined_REMOVE">
        <div id="notificationMessage" class="notificationMessage">
            <div id="notMessage"></div>
        </div>
