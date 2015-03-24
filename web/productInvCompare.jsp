<jsp:directive.page import="com.soward.object.Product"/>
<jsp:directive.page import="com.soward.util.ProductUtils"/>
<%@page import="com.soward.util.UserUtil"%>
<%@page import="com.soward.object.User"%>
<%@ page import="java.sql.*,java.util.*,java.util.Date.*"%>
<%@ page import="com.soward.object.Client"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.PreparedStatement"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="javax.naming.Context"%>
<%@ page import="javax.naming.InitialContext"%>
<%@ page import="javax.naming.NamingException"%>
<%@ page import="javax.servlet.ServletException"%>
<%@ page import="javax.servlet.http.HttpServlet"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<%@ page import="javax.servlet.http.HttpServletResponse"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%@ page import="javax.sql.DataSource"%>
<%@ page import="com.soward.*"%>
<script type="text/javascript" src="js/inventoryAjax.js"></script>
<%
	java.util.Date now = new java.util.Date();
	String Uid = (String) session.getAttribute("Uid");
    UserUtil userAd = new UserUtil();
    int count = 1000;
    
    boolean isAdmin = false;
    try{
        isAdmin = userAd.isAdmin(Uid);
    }catch(Exception e){
        //
    }
	String username = (String) session.getAttribute("username");
	//System.out.println(Uid);
	if (Uid == null) {
		request.getSession().invalidate();
		if (session != null) {
			session = null;
		}
		response.sendRedirect("home.jsp?message=Please Login");
	}
%>
<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>
<%@page import="com.soward.util.InvoiceUtil"%>
<%@page import="com.soward.object.Invoice"%>
<script type="text/javascript" src="js/inventoryAjax.js"></script>

<BR>
<%
 String message = request.getParameter( "message" );
 String saveUpdatedProduct = request.getParameter( "saveUpdatedProduct" );
 String prodNum = request.getParameter( "prodNum" );
 String location = request.getParameter( "location" );
 if(saveUpdatedProduct!=null){
 	  Product prod = new Product();
            String productNum           = request.getParameter("murray_productNum");
		        prodNum = productNum;
            String productName          = request.getParameter("murray_productName")!=null?request.getParameter(               "murray_productName" ):""; 
            String productAuthor        = request.getParameter("murray_productAuthor")!=null?request.getParameter(             "murray_productAuthor"):"";
            String productArtist        = request.getParameter("murray_productArtist")!=null?request.getParameter(             "murray_productArtist"):"";
            String productArranger      = request.getParameter("murray_productArranger")!=null?request.getParameter(           "murray_productArranger"):"";
            String productDescription   = request.getParameter("murray_productDescription")!=null?request.getParameter(        "murray_productDescription"):"";
            String category             = request.getParameter("murray_category")!=null?request.getParameter(                  "murray_category"):"";
            String productCost1         = request.getParameter("murray_productCost1")!=null?request.getParameter(              "murray_productCost1"):"";
            String productCost2         = request.getParameter("murray_productCost2")!=null?request.getParameter(              "murray_productCost2"):"";
            String productCost3         = request.getParameter("murray_productCost3")!=null?request.getParameter(              "murray_productCost3"):"";
            String productCost4         = request.getParameter("murray_productCost4")!=null?request.getParameter(              "murray_productCost4"):"";
            String productBarCode       = request.getParameter("murray_productBarCode")!=null?request.getParameter(            "murray_productBarCode"):"";
            String productSKU           = request.getParameter("murray_productSKU")!=null?request.getParameter(                "murray_productSKU"):"";
            String productCatalogNum    = request.getParameter("murray_productCatalogNum")!=null?request.getParameter(         "murray_productCatalogNum"):"";
            String productSupplier1     = request.getParameter("murray_productSupplier1")!=null?request.getParameter(          "murray_productSupplier1"):"";
            String productSupplier2     = request.getParameter("murray_productSupplier2")!=null?request.getParameter(          "murray_productSupplier2"):"";
            String productSupplier3     = request.getParameter("murray_productSupplier3")!=null?request.getParameter(          "murray_productSupplier3"):"";
            String productSupplier4     = request.getParameter("murray_productSupplier4")!=null?request.getParameter(          "murray_productSupplier4"):"";
            String numAvailable         = request.getParameter("murray_numAvailable")!=null?request.getParameter(              "murray_numAvailable"):"";
            String lastSold             = request.getParameter("murray_lastSold")!=null?request.getParameter(                  "murray_lastSold"):"";
            String lastInvDate          = request.getParameter("murray_lastInvDate")!=null?request.getParameter(               "murray_lastInvDate"):"";
            String DCCatalogNum         = request.getParameter("murray_DCCatalogNum")!=null?request.getParameter(              "murray_DCCatalogNum"):"";

            prod.setProductNum        ( productNum        );
            prod.setProductName       ( productName       );
            prod.setProductAuthor     ( productAuthor     );
            prod.setProductArtist     ( productArtist     );
            prod.setProductArranger   ( productArranger   );
            prod.setProductDescription( productDescription);
            prod.setCategory          ( category          );
            prod.setProductCost1      ( productCost1      );
            prod.setProductCost2      ( productCost2      );
            prod.setProductCost3      ( productCost3      );
            prod.setProductCost4      ( productCost4      );
            prod.setProductBarCode    ( productBarCode    );
            prod.setProductSKU        ( productSKU        );
            prod.setProductCatalogNum ( productCatalogNum );
            prod.setProductSupplier1  ( productSupplier1  );
            prod.setProductSupplier2  ( productSupplier2  );
            prod.setProductSupplier3  ( productSupplier3  );
            prod.setProductSupplier4  ( productSupplier4  );
            prod.setNumAvailable      ( numAvailable      );
            prod.setLastSold          ( lastSold          );
            prod.setLastInvDate       ( lastInvDate       );
            prod.setDCCatalogNum      ( DCCatalogNum      );
            message = ProductUtils.save( prod, "murray" );

 	          prod = new Product();

            productName          = request.getParameter("lehi_productName")!=null?request.getParameter(               "lehi_productName" ):""; 
            productAuthor        = request.getParameter("lehi_productAuthor")!=null?request.getParameter(             "lehi_productAuthor"):"";
            productArtist        = request.getParameter("lehi_productArtist")!=null?request.getParameter(             "lehi_productArtist"):"";
            productArranger      = request.getParameter("lehi_productArranger")!=null?request.getParameter(           "lehi_productArranger"):"";
            productDescription   = request.getParameter("lehi_productDescription")!=null?request.getParameter(        "lehi_productDescription"):"";
            category             = request.getParameter("lehi_category")!=null?request.getParameter(                  "lehi_category"):"";
            productCost1         = request.getParameter("lehi_productCost1")!=null?request.getParameter(              "lehi_productCost1"):"";
            productCost2         = request.getParameter("lehi_productCost2")!=null?request.getParameter(              "lehi_productCost2"):"";
            productCost3         = request.getParameter("lehi_productCost3")!=null?request.getParameter(              "lehi_productCost3"):"";
            productCost4         = request.getParameter("lehi_productCost4")!=null?request.getParameter(              "lehi_productCost4"):"";
            productBarCode       = request.getParameter("lehi_productBarCode")!=null?request.getParameter(            "lehi_productBarCode"):"";
            productSKU           = request.getParameter("lehi_productSKU")!=null?request.getParameter(                "lehi_productSKU"):"";
            productCatalogNum    = request.getParameter("lehi_productCatalogNum")!=null?request.getParameter(         "lehi_productCatalogNum"):"";
            productSupplier1     = request.getParameter("lehi_productSupplier1")!=null?request.getParameter(          "lehi_productSupplier1"):"";
            productSupplier2     = request.getParameter("lehi_productSupplier2")!=null?request.getParameter(          "lehi_productSupplier2"):"";
            productSupplier3     = request.getParameter("lehi_productSupplier3")!=null?request.getParameter(          "lehi_productSupplier3"):"";
            productSupplier4     = request.getParameter("lehi_productSupplier4")!=null?request.getParameter(          "lehi_productSupplier4"):"";
            numAvailable         = request.getParameter("lehi_numAvailable")!=null?request.getParameter(              "lehi_numAvailable"):"";
            lastSold             = request.getParameter("lehi_lastSold")!=null?request.getParameter(                  "lehi_lastSold"):"";
            lastInvDate          = request.getParameter("lehi_lastInvDate")!=null?request.getParameter(               "lehi_lastInvDate"):"";
            DCCatalogNum         = request.getParameter("lehi_DCCatalogNum")!=null?request.getParameter(              "lehi_DCCatalogNum"):"";


            prod.setProductNum        ( productNum        );
            prod.setProductName       ( productName       );
            prod.setProductAuthor     ( productAuthor     );
            prod.setProductArtist     ( productArtist     );
            prod.setProductArranger   ( productArranger   );
            prod.setProductDescription( productDescription);
            prod.setCategory          ( category          );
            prod.setProductCost1      ( productCost1      );
            prod.setProductCost2      ( productCost2      );
            prod.setProductCost3      ( productCost3      );
            prod.setProductCost4      ( productCost4      );
            prod.setProductBarCode    ( productBarCode    );
            prod.setProductSKU        ( productSKU        );
            prod.setProductCatalogNum ( productCatalogNum );
            prod.setProductSupplier1  ( productSupplier1  );
            prod.setProductSupplier2  ( productSupplier2  );
            prod.setProductSupplier3  ( productSupplier3  );
            prod.setProductSupplier4  ( productSupplier4  );
            prod.setNumAvailable      ( numAvailable      );
            prod.setLastSold          ( lastSold          );
            prod.setLastInvDate       ( lastInvDate       );
            prod.setDCCatalogNum      ( DCCatalogNum      );
 
            message += "  &&  "+ProductUtils.save( prod, "lehi" );
 }
 Product prodMurray = ProductUtils.fetchProductForNum( prodNum, "MURRAY" ); 
 Product prodLehi = ProductUtils.fetchProductForNum( prodNum, "LEHI" ); 


%>
<center>
     <form name="productForm" action="productInvCompare.jsp">
          <input type=hidden value="<%=location %>" name="location">
          <input type=hidden value="<%=location %>" name="saveUpdatedProduct">
          <%if(message!=null){%>
          <p class="message"><%=message%>
          </p>
          <%}%>
          <br>
          <table border=1 cellspacing="3" cellpadding="3">
               <tr>
                    <td><table class="common" cellpadding="0" cellspacing="0">
                              <th colspan="8">MURRAY</th>
                              <tr>
                                   <td>productNum: </td>
                                   <td><input readonly type="text" value="<%=prodMurray.getProductNum()  %>" name="murray_productNum">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productName: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductName()          %>"     name="murray_productName">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productAuthor: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductAuthor()        %>" name="murray_productAuthor">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productArtist: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductArtist()        %>"     name="murray_productArtist">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productArranger: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductArranger()      %>" name="murray_productArranger">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productDescription: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductDescription()   %>"     name="murray_productDescription">
                                   </td>
                              </tr>
                              <tr>
                                   <td>category: </td>
                                   <td><input type="text" value="<%=prodMurray.getCategory()             %>" name="murray_category">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productCost1: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductCost1()         %>"     name="murray_productCost1">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productCost2: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductCost2()         %>" name="murray_productCost2">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productCost3: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductCost3()         %>"     name="murray_productCost3">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productCost4: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductCost4()         %>" name="murray_productCost4">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productBarCode: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductBarCode()       %>"     name="murray_productBarCode">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productSKU: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductSKU()           %>" name="murray_productSKU">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productCatalogNum: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductCatalogNum()    %>"     name="murray_productCatalogNum">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productSupplier1: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductSupplier1()     %>" name="murray_productSupplier1">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productSupplier2: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductSupplier2()     %>"     name="murray_productSupplier2">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productSupplier3: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductSupplier3()     %>" name="murray_productSupplier3">
                                   </td>
                              </tr>
                              <tr>
                                   <td>productSupplier4: </td>
                                   <td><input type="text" value="<%=prodMurray.getProductSupplier4()     %>"     name="murray_productSupplier4">
                                   </td>
                              </tr>
                              <tr>
                                   <td>numAvailable: </td>
                                   <td><input type="text" value="<%=prodMurray.getNumAvailable()         %>" name="murray_numAvailable">
                                   </td>
                              </tr>
                              <tr>
                                   <td>lastSold: </td>
                                   <td><input type="text" value="<%=prodMurray.getLastSold()             %>"     name="murray_lastSold">
                                   </td>
                              </tr>
                              <tr>
                                   <td>lastInvDate: </td>
                                   <td><input type="text" value="<%=prodMurray.getLastInvDate()          %>" name="murray_lastInvDate">
                                   </td>
                              </tr>
                              <tr>
                                   <td>DCCatalogNum: </td>
                                   <td><input type="text" value="<%=prodMurray.getDCCatalogNum()         %>"     name="murray_DCCatalogNum">
                                   </td>
                              </tr>
                         </table></td>
                    <td><table class="common" cellpadding="0" cellspacing="0">
                              <th colspan="8">LEHI
                           	</th>
                              <tr>
                                   <td>productNum: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductName()          %>"     name="lehi_productName" />
                                        <input readonly type="text" value="<%=prodLehi.getProductNum()  %>" name="lehi_productNum">                                   </td>
                              </tr>
                              <tr>
                                   <td>productName: </td>
                                   <td>&nbsp;</td>
                              </tr>
                              <tr>
                                   <td>productAuthor: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductAuthor()        %>" name="lehi_productAuthor">                                   </td>
                              </tr>
                              <tr>
                                   <td>productArtist: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductArtist()        %>"     name="lehi_productArtist">                                   </td>
                              </tr>
                              <tr>
                                   <td>productArranger: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductArranger()      %>" name="lehi_productArranger">                                   </td>
                              </tr>
                              <tr>
                                   <td>productDescription: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductDescription()   %>"     name="lehi_productDescription">                                   </td>
                              </tr>
                              <tr>
                                   <td>category: </td>
                                   <td><input type="text" value="<%=prodLehi.getCategory()             %>" name="lehi_category">                                   </td>
                              </tr>
                              <tr>
                                   <td>productCost1: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductCost1()         %>"     name="lehi_productCost1">                                   </td>
                              </tr>
                              <tr>
                                   <td>productCost2: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductCost2()         %>" name="lehi_productCost2">                                   </td>
                              </tr>
                              <tr>
                                   <td>productCost3: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductCost3()         %>"     name="lehi_productCost3">                                   </td>
                              </tr>
                              <tr>
                                   <td>productCost4: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductCost4()         %>" name="lehi_productCost4">                                   </td>
                              </tr>
                              <tr>
                                   <td>productBarCode: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductBarCode()       %>"     name="lehi_productBarCode">                                   </td>
                              </tr>
                              <tr>
                                   <td>productSKU: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductSKU()           %>" name="lehi_productSKU">                                   </td>
                              </tr>
                              <tr>
                                   <td>productCatalogNum: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductCatalogNum()    %>"     name="lehi_productCatalogNum">                                   </td>
                              </tr>
                              <tr>
                                   <td>productSupplier1: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductSupplier1()     %>" name="lehi_productSupplier1">                                   </td>
                              </tr>
                              <tr>
                                   <td>productSupplier2: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductSupplier2()     %>"     name="lehi_productSupplier2">                                   </td>
                              </tr>
                              <tr>
                                   <td>productSupplier3: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductSupplier3()     %>" name="lehi_productSupplier3">                                   </td>
                              </tr>
                              <tr>
                                   <td>productSupplier4: </td>
                                   <td><input type="text" value="<%=prodLehi.getProductSupplier4()     %>"     name="lehi_productSupplier4">                                   </td>
                              </tr>
                              <tr>
                                   <td>numAvailable: </td>
                                   <td><input type="text" value="<%=prodLehi.getNumAvailable()         %>" name="lehi_numAvailable">                                   </td>
                              </tr>
                              <tr>
                                   <td>lastSold: </td>
                                   <td><input type="text" value="<%=prodLehi.getLastSold()             %>"     name="lehi_lastSold">                                   </td>
                              </tr>
                              <tr>
                                   <td>lastInvDate: </td>
                                   <td><input type="text" value="<%=prodLehi.getLastInvDate()          %>" name="lehi_lastInvDate">                                   </td>
                              </tr>
                              <tr>
                                   <td>DCCatalogNum: </td>
                                   <td><input type="text" value="<%=prodLehi.getDCCatalogNum()         %>"     name="lehi_DCCatalogNum">                                   </td>
                              </tr>
                         </table>
          </table>
          <center>
          <br>
          <!--  
    <input type=submit value="Save"/>
        -->
     </form>
     <br>
     <form>
          <input type=button value="Close" onClick="javascript:window.close();" class="btn">
          <br>
     </form>
</center>
