
<%@page import="org.apache.commons.lang.StringUtils"%>

<%@page import="com.soward.object.ProductsLocationCount"%>
<%@page import="com.soward.util.ProductsLocationCountUtil"%>
<jsp:directive.page import="com.soward.object.Supplier" />
<jsp:directive.page import="com.soward.util.SupplierUtil" />
<jsp:directive.page import="com.soward.object.Departments" />
<jsp:directive.page import="com.soward.object.Descriptions" />
<jsp:directive.page import="com.soward.object.Events" />
<jsp:directive.page import="com.soward.util.EventsUtil" />
<jsp:directive.page import="com.soward.util.DescriptionsUtil" />
<jsp:directive.page import="com.soward.util.DepartmentsUtil" />
<jsp:directive.page import="com.soward.object.Product" />
<jsp:directive.page import="com.soward.util.ProductUtils" />
<jsp:directive.page import="com.soward.util.Utils" />
<%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.object.Invoice"%>
<script type="text/javascript" src="js/comboBox.js">
</script>
<script type="text/javascript">
    function genList(obj){
        document.clickList.offSet.value = obj;
        document.clickList.submit();
    }
    function refreshDescriptions(){
        document.getProduct.resetDescrips.value = '';
        document.getProduct.submit();
    }
    function sf(string){

    } </script>
<%
    String department = request.getParameter("department")!=null?request.getParameter("department"):"0";
    List<Descriptions> allDescriptions = DescriptionsUtil.getAll2CodeDescriptions(department);
    List<Departments> allDepartments = DepartmentsUtil.getAllDepartments();
    List<Events> allEvents = EventsUtil.getAllEvents();
    HashMap<String, String> eventsHash = new HashMap<String, String>();
    HashMap<String, String> deptHash = new HashMap<String, String>();
    HashMap<String, String> descHash = new HashMap<String, String>();
    for(Descriptions desc: allDescriptions){
        descHash.put(desc.getDescriptionCode(), desc.getDescriptionName());
    }
    for(Events event: allEvents){
        eventsHash.put(event.getEventCode(), event.getEventName());
    }
    for(Departments dept: allDepartments){
        deptHash.put(dept.getDepartmentCode(), dept.getDepartmentName());
    }

    String message =                         Utils.getP(request, "message");
    String moveQ =                           Utils.getP(request, "moveQ" );
    String updateAvailableCountProductNum =  Utils.getP(request, "updateAvailableCountProductNum" );
    String updateLastInvDateProductNum =     Utils.getP(request, "updateLastInvDateProductNum" );
    String deleteProduct =                   Utils.getP(request, "deleteProduct" );
    String deleteProdNum =                   Utils.getP(request, "deleteProdNum" );
    String location =                        Utils.getP(request, "location");
    String updatedCount =                    Utils.getP(request, "updatedCount" );
    String moveCount =                       Utils.getP(request, "moveCount" );
    String moveLocation =                    Utils.getP(request, "moveLocation" );
    String resetDescrips =                   Utils.getP(request, "resetDescrips" );

    String DMM                         =     Utils.getP(request, "DMM"          );
    String catalog                     =     Utils.getP(request, "catalog"      );
    String barCode                     =     Utils.getP(request, "barCode"      );
    String name                        =     Utils.getP(request, "name"         );
    String artist                      =     Utils.getP(request, "artist"       );
    String author                      =     Utils.getP(request, "author"       );
    String arranger                    =     Utils.getP(request, "arranger"     );
    String description                 =     Utils.getP(request, "description"  );
    String productSupplier1            =     Utils.getP(request, "productSupplier1"  );
    String productNum                  =     Utils.getP(request, "productNum"   );
    String descriptions                =     Utils.getP(request, "descriptions"   );
    String events1                     =     Utils.getP(request, "events1"   );
    String events2                     =     Utils.getP(request, "events2"   );
    String events3                     =     Utils.getP(request, "events3"   );
    String events4                     =     Utils.getP(request, "events4"   );
    String offSet                      =     Utils.getP(request, "offSet"   );
    String gotoClicked                       =     Utils.getP(request, "gotoClicked"   );
    String inxCl                       =     Utils.getP(request, "indexClicked"   );
    String keepIdList                  =     request.getParameter("keepIdList"   );

    int offset = StringUtils.isBlank(offSet)?0:Integer.parseInt(offSet);
    int indexClicked = StringUtils.isBlank(inxCl)?0:Integer.parseInt(inxCl);
    int gotoCl = -1;
    int gotoOrg = 0;
    try{
        gotoCl = StringUtils.isBlank(gotoClicked)?-1:Integer.parseInt(gotoClicked);
        gotoOrg = gotoCl;

        while((gotoCl%20)!=0){
            gotoCl = gotoCl -1;
            if(gotoCl<1){
                break;
            }
        }
    }catch(Exception e){}
    indexClicked = gotoCl<0?indexClicked:gotoCl;
    offset = gotoOrg<0?offset:gotoOrg-1;
    offset = offset<1?indexClicked:offset;

    String prodNumberUpdatePrice  =          Utils.getP(request, "prodNumberUpdatePrice"   );
    String sortBy                 =          Utils.getP(request, "sortBy"   );
    String priceUpdate  =                    Utils.getP(request, "priceUpdate"   );
    String prodNumberUpdateLastInv  =        Utils.getP(request, "prodNumberUpdateLastInv"   );
    String prodNumberUpdateLastDO  =         Utils.getP(request, "prodNumberUpdateLastDO"   );
    ArrayList<String> idList = null;
    //move quantiy from X to Y
    if(!StringUtils.isBlank(prodNumberUpdateLastInv)){
        message = ProductUtils.updateLastInv(prodNumberUpdateLastInv);
    }
    if(!StringUtils.isBlank(prodNumberUpdateLastDO)){
        message = ProductUtils.updateLastDO(prodNumberUpdateLastDO);
    }
    if(!StringUtils.isBlank(prodNumberUpdatePrice)&&!StringUtils.isBlank(priceUpdate)){
        Product pprod = ProductUtils.fetchProductForNum(prodNumberUpdatePrice, "MURRAY");
        if(pprod!=null){
            pprod.setProductCost1(priceUpdate);
            message = ProductUtils.save( pprod );
        }
    }

    if(!StringUtils.isBlank(moveQ)){
        message =ProductUtils.moveProdQtyFromTo(moveQ, moveCount, moveLocation, username);
    }
    //update uvailable count
    if(!StringUtils.isBlank(updateAvailableCountProductNum)&&!StringUtils.isBlank(location)&&!StringUtils.isBlank(updatedCount)){
        try{
            Integer.parseInt(updatedCount);
            message = ProductsLocationCountUtil.updateCountForLocation(updatedCount, updateAvailableCountProductNum, location );
        }catch(Exception e){//do nothing
        }
    }
    if(!StringUtils.isBlank(deleteProdNum)&&!StringUtils.isBlank(deleteProduct)){
        message = ProductUtils.deleteProdForNumAllLocas(deleteProdNum);
    }
    //update lastinvdate
    //updateLastInvDate(String prodNum, String location){
    if(!StringUtils.isBlank(updateLastInvDateProductNum)){
        message = ProductUtils.updateLastInvDate(updateLastInvDateProductNum, location );
    }
    ArrayList<Product> prodList = null;
    if(!StringUtils.isBlank(resetDescrips)){
        String idListString = null;
        //reset master id list
        if(keepIdList==null){
            idList = ProductUtils.getIDList(
                    DMM ,catalog ,barCode ,name ,artist ,author ,arranger ,
                    description ,productSupplier1 ,productNum ,descriptions ,
                    department ,events1, events2, events3, events4, sortBy);
            try{
                request.getSession().setAttribute("idList", idList);
            }catch(IllegalStateException ise){
                //response.sendRedirect("home.jsp?message=Your session has timed out");
            }
        }
        //next or prev was clicked, fetch current idList
        else{
            idList = (ArrayList<String>)request.getSession().getAttribute("idList");
        }
        if(idList!=null&&idList.size()>0){
            if(idList.size()>offset){
                idListString = idList.get(offset);
            }else{
                idListString = idList.get(idList.size()-1);
                indexClicked = idList.size();
                offset = indexClicked;
                while((indexClicked%20)!=0&&indexClicked!=0){
                    indexClicked = indexClicked -1;
                }
            }
        }
        prodList = ProductUtils.searchProductForIDs(idListString);
    }

    int returnedSize = 0;
    if(idList!=null){
        for(String str: idList){
            returnedSize += str.split(",").length;
        }
    }
    int count = 1000;
%>
<br />
<h1>Product & Inventory Management</h1>
<p class="text">Search <!-- for a product with product name, number, catalog number, or category-->
    for product to update inventory and/or product details. Max search size 10000, page size 10.</p>
<table class="simple" cellpadding="0" cellspacing="0">
    <tr>
        <td>

            <table cellpadding="0" border="0" cellspacing="0">
                <form method="post" name="getProduct"
                      ACTION="./inventoryManagement.jsp">
                    <tr>
                        <td colspan="2" align="right"></td>
                    </tr>
                    <tr>
                        <td><input type="submit" value="Search" class="btn" /></td>
                    </tr>
                    <%
                        String focus = "";
                        String[] strL = new String[]{DMM,catalog,barCode,name,artist,author,arranger,productSupplier1,productNum};
                        String[] strName = new String[]{"DMM","catalog","barCode","name","artist","author","arranger","productSupplier1","productNum"};
                        int ii = 0;
                        for(String ss: strL){
                            if(!StringUtils.isBlank(ss)){
                                focus = "document.getProduct."+strName[ii]+".focus();";
                                break;
                            }
                            ii++;
                        }
                    %>
                    <tr>
                        <td>
                            <table border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td>DMM# <br />
                                        <input type="text" size="15" value="" name="DMM"></td>
                                    <td>Catalog# <br />
                                        <input type="text" size="15" value="" name="catalog"></td>
                                    <td>Bar Code <br />
                                        <input type="text" size="15" value="" name="barCode"></td>
                                </tr>
                                <tr>
                                    <td>Name <br />
                                        <input type="text" size="15" value="" name="name"></td>
                                    <td>Artist <br />
                                        <input type="text" size="15" value="" name="artist"></td>
                                    <td>Author <br />
                                        <input type="text" size="15" value="" name="author"></td>
                                </tr>
                                <tr>
                                    <td>Arranger <br />
                                        <input type="text" size="15" value="" name="arranger"></td>
                                    <td>Supplier Name <br />
                                        <input type="text" size="15" value="" name="productSupplier1">
                                    </td>
                                    <td>Product# <br />
                                        <input type="text" size="15" value="" name="productNum">
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td>
                            <table border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td>Department: *</td>
                                    <td><select name="department"
                                                onchange="refreshDescriptions();">
                                        <%
                                            String xname = "";
                                            String key = "";
                                            try{
                                                key = department;
                                                xname = (String)deptHash.get(key);
                                            }catch(Exception e){
                                            }
                                            if(!StringUtils.isBlank(xname)){%>
                                        <option value="<%=key%>"><%= xname %></option>
                                        <option value="">N/A</option>
                                        <%}else{ %>
                                        <option value="">N/A</option>
                                        <%} %>
                                        <%for(Departments sup: allDepartments){
                                            if(StringUtils.isBlank(sup.getDepartmentName())){
                                                continue;}
                                        %>
                                        <option value="<%= sup.getDepartmentCode() %>"><%=sup.getDepartmentName()%></option>
                                        <%} %>
                                    </select></td>
                                </tr>
                                <tr><td colspan="2"><font size="1"><b>Select department to reload descriptions.</b></font></td></tr>
                                <tr>
                                    <td>Descriptions:</td>
                                    <td><select name="descriptions">
                                        <%
                                            xname = "";
                                            key = "";
                                            try{
                                                key = descriptions;
                                                xname = (String)descHash.get(key);
                                            }catch(Exception e){
                                            }
                                            if(!StringUtils.isBlank(xname)){%>
                                        <option value="<%=key%>"><%= xname %></option>
                                        <option value="">N/A</option>
                                        <%}else{ %>
                                        <option value="">N/A</option>
                                        <%} %>
                                        <%for(Descriptions sup: allDescriptions){
                                            if(StringUtils.isBlank(sup.getDescriptionName())){continue;}
                                        %>
                                        <option value="<%= sup.getDescriptionCode() %>"><%=sup.getDescriptionName()%></option>
                                        <%} %>
                                    </select></td>
                                </tr>
                                <tr>
                                    <td>Events:</td>
                                    <td><select name="events1">
                                        <%  xname = ""; key = ""; try{ key = events1; xname = (String)eventsHash.get(key);
                                        }catch(Exception e){ }
                                            if(!StringUtils.isBlank(xname)){%>
                                        <option value="<%=key%>"><%= xname %></option>
                                        <option value="">N/A</option>
                                        <%}else{ %>
                                        <option value="">N/A</option>
                                        <%} %>
                                        <%for(Events sup: allEvents){ %>
                                        <option value="<%= sup.getEventCode() %>"><%=sup.getEventName()%></option>
                                        <%} %>
                                    </select></td>
                                </tr>

                                <tr>
                                    <td>Events:</td>
                                    <td><select name="events2">
                                        <%  xname = ""; key = ""; try{ key = events2; xname = (String)eventsHash.get(key);
                                        }catch(Exception e){ }
                                            if(!StringUtils.isBlank(xname)){%>
                                        <option value="<%=key%>"><%= xname %></option>
                                        <option value="">N/A</option>
                                        <%}else{ %>
                                        <option value="">N/A</option>
                                        <%} %>
                                        <%for(Events sup: allEvents){ %>
                                        <option value="<%= sup.getEventCode() %>"><%=sup.getEventName()%></option>
                                        <%} %>
                                    </select></td>
                                </tr>

                                <tr>
                                    <td>Events:</td>
                                    <td><select name="events3">
                                        <%  xname = ""; key = ""; try{ key = events3; xname = (String)eventsHash.get(key);
                                        }catch(Exception e){ }
                                            if(!StringUtils.isBlank(xname)){%>
                                        <option value="<%=key%>"><%= xname %></option>
                                        <option value="">N/A</option>
                                        <%}else{ %>
                                        <option value="">N/A</option>
                                        <%} %>
                                        <%for(Events sup: allEvents){ %>
                                        <option value="<%= sup.getEventCode() %>"><%=sup.getEventName()%></option>
                                        <%} %>
                                    </select></td>
                                </tr>

                                <tr>
                                    <td>Events:</td>
                                    <td><select name="events4">
                                        <%  xname = ""; key = ""; try{ key = events4; xname = (String)eventsHash.get(key);
                                        }catch(Exception e){ }
                                            if(!StringUtils.isBlank(xname)){%>
                                        <option value="<%=key%>"><%= xname %></option>
                                        <option value="">N/A</option>
                                        <%}else{ %>
                                        <option value="">N/A</option>
                                        <%} %>
                                        <%for(Events sup: allEvents){ %>
                                        <option value="<%= sup.getEventCode() %>"><%=sup.getEventName()%></option>
                                        <%} %>
                                    </select></td>
                                </tr>
                            </table>
                        </td>

                    </tr>
                    <tr>
                        <td>Sort by Quantity: &nbsp; <%
                            String qChecked = StringUtils.isBlank(sortBy)?"":sortBy;
                            qChecked = qChecked.equals("quantity")?"CHECKED":"";
                        %> <input type="checkbox" <%=qChecked%> name="sortBy"
                                  value="quantity" /></td>
                    </tr>
                    <input type=hidden value="resetDescrips" name="resetDescrips">
                </FORM>
            </table>
        </td>
    </tr>
</table>
<%
    if ( message != null ) {
%>
<br>
<center><font color="red" size="3"><%=message%></font></center>
<br>
<%
    }
%>
<%
    if ( prodList != null && prodList.size() > 0 ) {



        String hidden = "";

        hidden += "<input type=\"hidden\" value=\""+DMM+"\" name=\"DMM\">";
        hidden += "<input type=\"hidden\" value=\""+catalog+"\" name=\"catalog\">";
        hidden += "<input type=\"hidden\" value=\""+barCode+"\" name=\"barCode\">";
        hidden += "<input type=\"hidden\" value=\""+name+"\" name=\"name\">";
        hidden += "<input type=\"hidden\" value=\""+artist+"\" name=\"artist\">";
        hidden += "<input type=\"hidden\" value=\""+author+"\" name=\"author\">";
        hidden += "<input type=\"hidden\" value=\""+arranger+"\" name=\"arranger\">";
        hidden += "<input type=\"hidden\" value=\""+description+"\" name=\"description\">";
        hidden += "<input type=\"hidden\" value=\""+sortBy+"\" name=\"sortBy\">";
        hidden += "<input type=\"hidden\" value=\""+productNum+"\" name=\"productNum\">";
        hidden += "<input type=\"hidden\" value=\"resetDescrips\" name=\"resetDescrips\">";
        hidden += "<input type=\"hidden\" value=\"keepIdList\" name=\"keepIdList\">";

        if(!StringUtils.isBlank(sortBy)){
            prodList = ProductUtils.sortList(prodList, sortBy );
        }
%>
<br>
<table width="100%" border="0">
    <tr>
        <% if(indexClicked>19){ %>
        <form method="post" action="inventoryManagement.jsp">
            <%=hidden %>
            <input type="hidden" name="indexClicked" value="<%=indexClicked - 20%>">
            <td width="25%" align="right"><input type="submit" value="Prev"  class="btn" ></td>
        </form>
        <%}else{ %>
        <td width="25%"></td>
        <%} %>
        <form name="clickList" method="post" action="inventoryManagement.jsp">
            <%=hidden %>
            <input type="hidden" name="offSet" value="">
            <%if(idList!=null&&!idList.isEmpty()){
            %><td width="50%" align="center"><%
            int ccs = 0;
            String ahref = "";
            for(int j = indexClicked; j<idList.size(); j++){
                ahref += "<a href=\"#\" onClick=\"genList('"+j+"');\"><b>"+(j+1)+"</b></a>&nbsp;";
                if(ccs>18){break;}
                ccs++;
            }
        %><%=ahref%></td><%
        }else{%><td width="75%"></td><%}%>
            <input type="hidden" name="indexClicked" value="<%=indexClicked%>">
        </form>

        <% if((indexClicked+20)<idList.size()){ %>
        <form method="post" action="inventoryManagement.jsp">
                <%=hidden %>
            <input type="hidden" name="indexClicked" value="<%=indexClicked + 20%>">
            <td align="left" width="25%"><input type="submit" value="Next"  class="btn" ></td></tr>
    </form>
    <%}else{ %>
    <td width="25%"></td>
    <%} %>

</table>
<br/>
<center>Returned <b><%=returnedSize%></b> results. Current page <font size="2" ><b><%=offset+1%></b></font>
<form method="post" action="inventoryManagement.jsp">
    <input type="text" name="gotoClicked" value="" size="5">
    <input type="hidden" name="indexClicked" value="<%=indexClicked%>">
    <%=hidden %>
    <input type="submit" value="GOTO"  class="btn" >&nbsp;Jump to page range.
</form>

<table class="simple" border=1 cellpadding="0" cellspacing="0">
<tr>
    <th>#</th>
    <th colspan="4">Product Information</th>
    <th colspan=1>Update Qty</th>
    <th colspan=1>Hist/Edit</th>
</tr>
<%
    hidden += "<input type=\"hidden\" value=\""+offset+"\" name=\"offSet\">";

    int prodCount = 1;
    Product tempProd = null;
    boolean flip = true;
    String bgColor = "";
    String nameee = "";
    HashMap<String, Supplier> allSups = null;
    if(prodList!=null&&prodList.size()>0){
        allSups = SupplierUtil.getAllSuppliersHashWithNumKey();
    }
    String keyyy = "";
    try{
        for ( int i = 0; i<prodList.size(); i++){
            tempProd = prodList.get(i);
            ProductsLocationCount plc = ProductsLocationCountUtil.getForProdNum(tempProd.getProductNum());
            if(flip){
                bgColor = "bgcolor=#fafafa";
            }else{
                bgColor = "bgcolor=#99ccff";
            }
            flip = !flip;
%>
<tr <%=bgColor%>>
    <td><%=prodCount++ %></td>
    <%
        //get prod desc to display 
        try{
            key = tempProd.getCategory().charAt(0)+"";
            descHash = new HashMap<String, String>();
            allDescriptions = DescriptionsUtil.getAll2CodeDescriptions(key);
            for(Descriptions desc: allDescriptions){
                descHash.put(desc.getDescriptionCode(), desc.getDescriptionName());
            }
            keyyy = tempProd.getCategory().charAt(5)+"";
            nameee = (String)descHash.get(keyyy);
        }catch(Exception e){
        }%>
    <td colspan="15" align="left"><b>Name:</b>&nbsp;<%=tempProd.getProductName() +"  "+tempProd.getProductNum()%>
    </td>
</tr>
<tr <%=bgColor%>>
    <%// //other location below ------------------ -->
        HashMap<String, String> locaMove = new HashMap<String, String>();
        locaMove.put("0", "Murray to Lehi");
        locaMove.put("1", "Murray to DV");
        locaMove.put("2", "DV to Lehi");
        locaMove.put("3", "DV to Murray");
        locaMove.put("4", "Lehi to Murray");
        locaMove.put("5", "Lehi to DV");

        boolean flipLocation = false;
        if(!StringUtils.isBlank(moveLocation)&&Integer.parseInt(moveLocation)==1){
            flipLocation = true;
        }

    %>
    <td colspan="5">
        <table border="0" class="simple" width="100%">
            <tr>
                <td <%=bgColor%>><b>Voicing:&nbsp;</b> <%if(!StringUtils.isBlank(nameee)){%>
                    <%= nameee %> <%} %>
                </td>
                <td <%=bgColor%>><b>Item#:</b>&nbsp;<%=tempProd.getProductCatalogNum() %></td>
                <td <%=bgColor%> valign="bottom">
                    <form method="post" action="inventoryManagement.jsp">
                        <%=hidden %>
                        <input
                                type="hidden" name="prodNumberUpdatePrice"
                                value="<%=tempProd.getProductNum() %>"> <input type="text"
                                                                               value="<%=tempProd.getProductCost1() %>" size="3"
                                                                               name="priceUpdate"> <input type="image"
                                                                                                          style="display: inline-block; vertical-align: middle"
                                                                                                          title="Save price" alt="Save" width="20" src="images/save.gif">
                    </form>
                </td>
                <td align="left" <%=bgColor%>><b>Desc:</b>&nbsp;<%=tempProd.getProductDescription() %></td>
            </tr>
            <tr>
                <td align="left" <%=bgColor%> colspan="3"><b>Publ:</b>&nbsp;<%=allSups.get(tempProd.getProductSupplier1()).getSupplierName() %></td>
                <td align="left" <%=bgColor%> colspan="1"><b>Discount:</b>&nbsp;<%=tempProd.getProductCost2()%></td>
            </tr>
            <tr>
                <td align="left" <%=bgColor%> colspan="4"><b>Composer/Arranger:</b>&nbsp;<%=tempProd.getProductArranger() %></td>
            </tr>
            <tr>
                <%
                    String lastInv = tempProd.getLastInvDate();
                    String lastDO = tempProd.getLastDODte();
                    String lastPO = ProductUtils.fetchLastPOForProdNum(tempProd.getProductNum());
                    String fromFormat = "yyyy-MM-dd hh:mm:ss";
                    String toFormat   = "MM-dd-yyyy hh:mm:ss";
                    lastInv = Utils.formatDt(lastInv, fromFormat, toFormat);
                    lastDO = Utils.formatDt(lastDO, fromFormat, toFormat);
                    lastPO = Utils.formatDt(lastPO, fromFormat, toFormat);
                %>

                <td colspan="4" align="left" <%=bgColor%> colspan="1"><b>Last
                    PO:</b>&nbsp;<%=lastPO%></td>
            </tr>
            <tr>
                <td align="left" <%=bgColor%> colspan="4"><b>Location:</b>&nbsp;<%=tempProd.getLocation() %></td>
            </tr>
            <tr>

                <form method="post" action="inventoryManagement.jsp">
                    <%=hidden %>
                    <td align="left" <%=bgColor%> colspan="2"><input type="image"
                                                                     style="display: inline-block; vertical-align: middle"
                                                                     title="Update lastInv date to current date/time" alt="Save"
                                                                     width="20" src="images/update.png"> <b>Last Inv:</b>&nbsp;<%=lastInv%>
                        <input type="hidden" name="prodNumberUpdateLastInv"
                               value="<%=tempProd.getProductNum() %>">
                    </td>
                </form>
                <form method="post" action="inventoryManagement.jsp">
                    <%=hidden %>
                    <td align="left" <%=bgColor%> colspan="2"><input type="image"
                                                                     style="display: inline-block; vertical-align: middle"
                                                                     title="Update lastDO date to current date/time" alt="Save"
                                                                     width="20" src="images/update.png"> <b>Last DO:</b>&nbsp;<%=lastDO%>
                        <input type="hidden" name="prodNumberUpdateLastDO"
                               value="<%=tempProd.getProductNum() %>"> </td>
                </form>
            </tr>
        </table>
    </td>
    <td colspan="2">
        <table border="0" class="simple" width="100%">
            <tr>

                <td <%=bgColor%> align="left"><font size="1"><b>Murray</b></font></td>

                <form method="post" action="inventoryManagement.jsp">
                    <%=hidden %>
                    <td valign="bottom" <%=bgColor%>><input
                            style="text-align: right" type="text" size="1"
                            value="<%=plc.getMURRAY() %>" name="updatedCount"> <input
                            type="hidden" name="updateAvailableCountProductNum"
                            value="<%=tempProd.getProductNum() %>"> <input
                            type="hidden" name="prodNumberUpdate"
                            value="<%=tempProd.getProductNum() %>"> <input
                            type="hidden" name="location" value="<%="MURRAY" %>"> <input
                            type="image" style="display: inline-block; vertical-align: middle"
                            title="Save new qty for Murray" alt="Save" width="20"
                            src="images/save.gif"></td>
                </form>
                <td <%=bgColor%> align=center><img width="20"
                                                   style="cursor: pointer; cursor: hand; display: inline-block; vertical-align: middle"
                                                   src="images/history.png" title="View history for Murray"
                                                   value="Hist"
                                                   onclick="jsfPopupPage('./productHistory.jsp?prodNum=<%=tempProd.getProductNum()%>&location=<%="MURRAY" %>', 700, 500);" />
                </td>
                <td <%=bgColor%> align=center><img src="images/edit.gif"
                                                   style="cursor: pointer; cursor: hand" title="Edit this product"
                                                   onclick="jsfPopupPage('./productInvEdit.jsp?prodNum=<%=tempProd.getProductNum()%>&location=<%="Products" %>', 800, 500);" />
                </td>

            </tr>
            <tr>
                <td align="left" <%=bgColor%>><font size="1"><b>Lehi</b></font></td>

                <form method="post" action="inventoryManagement.jsp">
                    <%=hidden %>
                    <td <%=bgColor%>><input style="text-align: right" type="text"
                                            size="1" value="<%=plc.getLEHI() %>" name="updatedCount"> <input
                            type="hidden" name="updateAvailableCountProductNum"
                            value="<%=tempProd.getProductNum() %>"> <input
                            type="hidden" name="prodNumberUpdate"
                            value="<%=tempProd.getProductNum() %>"> <input
                            type="hidden" name="location" value="<%="LEHI" %>"> <input
                            type="image" style="display: inline-block; vertical-align: middle"
                            title="Save new qty for Lehi" alt="Save" width="20"
                            src="images/save.gif"> </td>
                </form>
                <td <%=bgColor%> align=center></td>
                <td <%=bgColor%>></td>
            </tr>
            <tr>
                <td align="left" <%=bgColor%>><font size="1"><b>DV</b></font></td>
                <form method="post" action="inventoryManagement.jsp">
                    <%=hidden %>
                    <td <%=bgColor%>><input style="text-align: right" type="text"
                                            size="1" value="<%=plc.getOREM() %>" name="updatedCount"> <input
                            type="hidden" name="updateAvailableCountProductNum"
                            value="<%=tempProd.getProductNum() %>"> <input
                            type="hidden" name="prodNumberUpdate"
                            value="<%=tempProd.getProductNum() %>"> <input
                            type="hidden" name="location" value="<%="OREM" %>"> <input
                            type="image" style="display: inline-block; vertical-align: middle"
                            title="Save new qty for DV" alt="Save" width="20"
                            src="images/save.gif"></td>
                    <td <%=bgColor%> align=center></td>
                    <td <%=bgColor%>></td>
                </form>

            </tr>
        </table>
    </td>


</tr>
<tr>
    <td colspan="15" <%=bgColor%> align="right">
        <table border="0">
            <tr>
                <form method="post" name="move" action="./inventoryManagement.jsp">
                    <td>Move</td>
                    <td><input name="moveCount" size=2></td>
                    <td>from</td>
                    <td><select name="moveLocation">
                        <%if(!StringUtils.isBlank(moveLocation)){ %>
                        <option value="<%=moveLocation%>"><%=locaMove.get(moveLocation) %></option>
                        <%} %>
                        <option value="0">Murray to Lehi</option>
                        <option value="1">Murray to DV</option>
                        <option value="2">DV to Lehi</option>
                        <option value="3">DV to Murray</option>
                        <option value="4">Lehi to Murray</option>
                        <option value="5">Lehi to DV</option>
                    </select></td>
                    <td><input type="hidden" name="moveQ"
                               value="<%=tempProd.getProductNum() %>"> <%=hidden %> <input
                            type="image" style="display: inline-block; vertical-align: middle"
                            title="Transfer quantity from one store to the next" alt="Move"
                            width="20" src="images/save.gif"></td>
                </form>
            </tr>
        </table>
    </td>
</tr>
<%
        }
    }catch(Exception e){
    }
}else{
%>
<center>No results were generated from your search.
    <p>Please refine. <%} %>

        </tr>

            <%= "<script type=\"text/javascript\">"+focus+"</script>"%>
</table>
<script type="text/javascript">  onLoad=sf(); </script> <%@include
        file="bottomLayout.jsp"%>
