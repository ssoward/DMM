
<jsp:directive.page import="com.soward.object.Product"/>
<jsp:directive.page import="com.soward.util.ProductUtils"/>
<jsp:directive.page import="com.soward.enums.LocationsDBName"/>
<jsp:directive.page import="com.soward.object.Transaction"/>
<jsp:directive.page import="com.soward.util.TransUtil"/>
<jsp:directive.page import="com.soward.enums.TransactionType"/>
<%@include file="jspsetup.jsp"%>


<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.object.Invoice"%>
<SCRIPT LANGUAGE="JavaScript">
</script>


<%
   String message = request.getParameter( "message" );
   String prodNum1 = request.getParameter( "prodNum1" );
   String prodNum2 = request.getParameter( "prodNum2" );
   String consolidateOne = request.getParameter( "consolidateOne" );
   String consolidateTwo = request.getParameter( "consolidateTwo" );
   if(consolidateOne!=null){
    if(ProductUtils.consolidateProducts(prodNum1, prodNum2)){
     message = "Successfully consolidated history into "+prodNum2+" from "+prodNum1+" and deleted "+prodNum1+".";
     prodNum1 = null;
    }
   }else if(consolidateTwo!=null){
    if(ProductUtils.consolidateProducts(prodNum2, prodNum1)){
      message = "Successfully consolidated history into "+prodNum1+" from "+prodNum2+" and deleted "+prodNum2+".";
     prodNum2 = null;
    }
   }
   ArrayList<Transaction> transListOne = null;
   ArrayList<Transaction> transListTwo = null;
   Product one = null;
   Product two = null;
   if(prodNum1!=null&&prodNum1.length()>1){
	for(LocationsDBName lName: LocationsDBName.values()){
   		one = ProductUtils.fetchProductForString(prodNum1, lName.dbName());
   		if(one!=null){
	   		transListOne = TransUtil.getAllTransForProd( one.getProductNum());
   			break;
   		}
   	}
   }
   if(prodNum2!=null&&prodNum2.length()>1){
	for(LocationsDBName lName: LocationsDBName.values()){
   		two = ProductUtils.fetchProductForString(prodNum2, lName.dbName());
   		if(two!=null){
	   		transListTwo = TransUtil.getAllTransForProd( two.getProductNum());
   			break;
   		}
   	}
   }
%>
<br>
<h1>
	Consolidate Products
</h1>
<p align="center">
  Consolidate two identical or simular products. The need for this occurs when a products is created that previously existed. Because products 
  have invoices and transactions (history) associated with them, a product cannot be deleted until its history is removed first.
  This page provides the ability to move the invoices of one product to another.
	

</p>
<table cellpadding=0 cellspacing=9 border="0" align=center>
    <tr>
      <td>

      <!-- FIRST PRODUCT -->
       <FORM name="cpOne" method="post" ACTION="./consolidateProducts.jsp">
        <table width="50%" cellpadding="3" cellspacing="3" border="1" align=center>
          <tr><td colspan="3" align="center"><font size="5"><b>1</b></font></td></tr>
        	<tr>
        		<td align=right>
        				Product&nbsp;#:
                </td><td>
        				<input type="text" name="prodNum1" value="<%=prodNum1!=null?prodNum1:""%>"/>
                <%if(prodNum2!=null){%>
                <input type="hidden" name="prodNum2" value="<%=prodNum2%>"/>
                <%}%>
        		</td>
        		<td>
        			<input type="submit" class="btn" value="Submit" />
        		</td>
        	</tr>
        </table>
      </form>
      </td>
      <td>
      <!-- SECOND PRODUCT -->
       <FORM name="cpTwo" method="post" ACTION="./consolidateProducts.jsp">
        <table width="50%" cellpadding="3" cellspacing="3" border="1" align=center>
          <tr><td colspan="3" align="center"><font size="5"><b>2</b></font></td></tr>
        	<tr>
        		<td align=right>
        				Product&nbsp;#:
                </td><td>
        				<input type="text" name="prodNum2" value="<%=prodNum2!=null?prodNum2:""%>"/>
                <%if(prodNum1!=null){%>
                <input type="hidden" name="prodNum1" value="<%=prodNum1%>"/>
                <%}%>
        		</td>
        		<td>
        			<input type="submit" class="btn" value="Submit" />
        		</td>
        	</tr>
        </table>
      </form>
      </td>
    </tr>
    <tr><td>
        <table width="50%" cellpadding="3" cellspacing="3" border="1" align=center>
    
    	 <%if(one!=null){%>
       <tr><td>Name:</td><td colspan="2"><%=one.getProductName()%></td></tr>
       <tr><td>Desc:</td><td colspan="2"><%=one.getProductDescription()%></td></tr>
       <tr><td>Number:</td><td colspan="2"><%=one.getProductNum()%></td></tr>
       <tr><td>Cat#:</td><td colspan="2"><%=one.getProductCatalogNum()%></td></tr>
          <%}if(transListOne!=null){%>
          <tr><td>Transaction:</td><td align=left>
     <select name="" size=10>
    <%
      String currentTransType = "";
      for(Transaction trans: transListOne){%>
        <%if(currentTransType.length()<1){%>
    		<option value="0"><%="--- "+TransactionType.valueOf(trans.getTransType()).value()+" -----" %></option>
        <%currentTransType = trans.getTransType();
        }else if(!currentTransType.equalsIgnoreCase(trans.getTransType())){%>
    		<option value="0"><%="--- "+TransactionType.valueOf(trans.getTransType()).value()+" -----" %></option>
        <%currentTransType = trans.getTransType();
        } %>
    		<option value="0"><%=trans.getTransDate()+" "+trans.getTransType()+" - "+trans.getProductQty()+" - "+trans.getInvoiceNum() %></option>
    <%}%>
    </select> 
    </td></tr>
          <%} %>
          </table>
          </td> <td>
        <table width="50%" cellpadding="3" cellspacing="3" border="1" align=center>
    
    	 <%if(two!=null){%>
       <tr><td>Name:</td><td colspan="2"><%=two.getProductName()%></td></tr>
       <tr><td>Desc:</td><td colspan="2"><%=two.getProductDescription()%></td></tr>
       <tr><td>Number:</td><td colspan="2"><%=two.getProductNum()%></td></tr>
       <tr><td>Cat#:</td><td colspan="2"><%=two.getProductCatalogNum()%></td></tr>
          <%}if(transListTwo!=null){%>
          <tr><td>Transaction:</td><td align=left>
     <select name="" size=10>
    <%
      String currentTransType = "";
      for(Transaction trans: transListTwo){%>
        <%if(currentTransType.length()<1){%>
    		<option value="0"><%="--- "+TransactionType.valueOf(trans.getTransType()).value()+" -----" %></option>
        <%currentTransType = trans.getTransType();
        }else if(!currentTransType.equalsIgnoreCase(trans.getTransType())){%>
    		<option value="0"><%="--- "+TransactionType.valueOf(trans.getTransType()).value()+" -----" %></option>
        <%currentTransType = trans.getTransType();
        } %>
    		<option value="0"><%=trans.getTransDateFormatted()+" "+trans.getTransType()+" - "+trans.getProductQty()+" - "+trans.getInvoiceNum() %></option>
    <%}%>
    </select> 
    </td></tr>
          <%} %>
          </table>
      </tr></td>
      <%if(prodNum2!=null&&prodNum1!=null){%> 
      <tr><td colspan="1" align="left">
          <FORM name="cpOne" method="post" ACTION="./consolidateProducts.jsp">
            <input type="submit" name="consolidateOne" 
            onclick="return confirm('Are you sure you want to move transaction \nhistory from product 1 TO 2\nand DELETE the first product?')" 
            value="Consolidate and Delete 1" />
            </td><td align="right">
            <input type="submit" name="consolidateTwo" 
            onclick="return confirm('Are you sure you want to move transaction \nhistory from product 2 TO 1\nand DELETE the second product?')"
            value="Consolidate and Delete 2" />
            <input type="hidden" name="prodNum2" value="<%=prodNum2%>"/>
            <input type="hidden" name="prodNum1" value="<%=prodNum1%>"/>
          </form>
      </td></tr>
      <%}%>
        <tr>
        
        	<%
        	if ( message != null ) {
        	%>
        	<td colspan="3" align="center">
            <font color="red"> <%=message%> </font>
        	</td>
        	<%
        	}
        	%>
       	</tr>
 
</table>

<%@include file="bottomLayout.jsp"%>
