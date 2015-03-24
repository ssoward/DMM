package com.soward.controller;

import com.soward.enums.ProductWeight;
import com.soward.object.Invoice;
import com.soward.object.Product;
import com.soward.object.TestObj;
import com.soward.util.InvoiceUtil;
import com.soward.util.LoginUtil;
import com.soward.util.ProductUtils;
import com.soward.util.ProductsLocationCountUtil;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ssoward on 9/23/14.
 */
public class ProductsController extends HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        String searchStr = request.getParameter("searchStr");
        String productNum = request.getParameter("productNum");
        LoginUtil.checkAccess(request, response);
        String endpoint = request.getParameter("funct");
        Invoice inv = null;
        List list = null;

        if(endpoint != null) {
            switch (Endpoint.valueOf(endpoint)) {
                case PROD_CAT_GET:
                    list = ProductUtils.searchProductsForCatalog(searchStr);
                    break;
                case PROD_SEARCH:
                    list = ProductUtils.searchProducts(searchStr);
                    break;
                case COUNT_PUT:
                    String numAvailable = request.getParameter("numAvailable");
                    ProductsLocationCountUtil.updateCountForLocation(numAvailable, productNum, "MURRAY");
                    break;
                case WEIGHT_PUT:
                    String weight = request.getParameter("weight");
                    if(weight!=null && productNum != null) {
                        ProductWeight pw = ProductWeight.valueOf(weight);
                        ProductUtils.updateProductWeight(productNum, pw);
                    }
                    break;
                case PROD_WEIGHT:
                    list = new ArrayList();
                    for(ProductWeight pw: ProductWeight.values()){
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("name", pw.name());
                        map.put("desc", pw.getDesc());
                        list.add(map);
                    }
                    break;
            }
        }
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        if(inv != null) {
            String json = mapper.writeValueAsString(inv);
            response.getWriter().print(json);
        }else if(list != null){
            String json = mapper.writeValueAsString(list);
            response.getWriter().print(json);
        }
        else {
            response.getWriter().print("OK");
        }
    }
}
