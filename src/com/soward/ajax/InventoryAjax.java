package com.soward.ajax;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.soward.object.Product;
import com.soward.util.ProductUtils;

public class InventoryAjax  extends HttpServlet { 

    private String outputStr="";
    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
    throws ServletException, IOException {
        try{
            HttpSession session = request.getSession();
            Product prod = new Product();
            String location           = request.getParameter("location");
            String productNum           = request.getParameter("productNum");
            String productName          = request.getParameter("productName")!=null?request.getParameter( "productName" ):""; 
            String productAuthor        = request.getParameter("productAuthor")!=null?request.getParameter("productAuthor"):"";
            String productArtist        = request.getParameter("productArtist")!=null?request.getParameter("productArtist"):"";
            String productArranger      = request.getParameter("productArranger")!=null?request.getParameter("productArranger"):"";
            String productDescription   = request.getParameter("productDescription")!=null?request.getParameter("productDescription"):"";
            String category             = request.getParameter("category")!=null?request.getParameter("category"):"";
            String productCost1         = request.getParameter("productCost1")!=null?request.getParameter("productCost1"):"";
            String productCost2         = request.getParameter("productCost2")!=null?request.getParameter("productCost2"):"";
            String productCost3         = request.getParameter("productCost3")!=null?request.getParameter("productCost3"):"";
            String productCost4         = request.getParameter("productCost4")!=null?request.getParameter("productCost4"):"";
            String productBarCode       = request.getParameter("productBarCode")!=null?request.getParameter("productBarCode"):"";
            String productSKU           = request.getParameter("productSKU")!=null?request.getParameter("productSKU"):"";
            String productCatalogNum    = request.getParameter("productCatalogNum")!=null?request.getParameter("productCatalogNum"):"";
            String productSupplier1     = request.getParameter("productSupplier1")!=null?request.getParameter("productSupplier1"):"";
            String productSupplier2     = request.getParameter("productSupplier2")!=null?request.getParameter("productSupplier2"):"";
            String productSupplier3     = request.getParameter("productSupplier3")!=null?request.getParameter("productSupplier3"):"";
            String productSupplier4     = request.getParameter("productSupplier4")!=null?request.getParameter("productSupplier4"):"";
            String numAvailable         = request.getParameter("numAvailable")!=null?request.getParameter("numAvailable"):"";
            String lastSold             = request.getParameter("lastSold")!=null?request.getParameter("lastSold"):"";
            String lastInvDate          = request.getParameter("lastInvDate")!=null?request.getParameter("lastInvDate"):"";
            String DCCatalogNum         = request.getParameter("DCCatalogNum")!=null?request.getParameter("DCCatalogNum"):"";

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



            response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
            response.setHeader("Pragma","no-cache"); //HTTP 1.0
            response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
            response.setContentType("text/html");

            PrintWriter out = response.getWriter();
            System.out.println("getting prod list");
            //String output = getOutput( month, year );
            outputStr = "<center>No results found for your search.</center>";
//            outputStr = ProductUtils.save( prod, location , false);

            out.println(outputStr);
            out.close();
            System.out.println("got in the inventoryAJax servlet...searchParam: "+productNum);
            response.sendRedirect( "./productInvEdit.jsp" );
        }catch (Exception e){
            e.printStackTrace();
        }
    }













}
