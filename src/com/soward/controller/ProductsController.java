package com.soward.controller;

import com.soward.enums.ProductWeight;
import com.soward.object.Invoice;
import com.soward.util.InvoiceUtil;
import com.soward.util.LoginUtil;
import com.soward.util.ProductUtils;
import com.soward.util.ProductsLocationCountUtil;
import org.apache.commons.discovery.log.SimpleLog;
import org.apache.commons.logging.Log;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by ssoward on 9/23/14.
 */
public class ProductsController extends HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Log log = new SimpleLog(InvoiceController.class.getName());

        String username = (String) request.getSession().getAttribute("username");
        String searchStr = request.getParameter("searchStr");
        String location = request.getParameter("location");
        String productNum = request.getParameter("productNum");
        LoginUtil.checkAccess(request, response);
        String endpoint = request.getParameter("funct");
        Invoice inv = null;
        List list = null;

        log.info(InvoiceUtil.sdf.format(new Date())+ " ProductsController endpoint: "+endpoint+" accessed by: "+username);
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
                    ProductsLocationCountUtil.updateCountForLocation(numAvailable, productNum, location);
                    break;
//                case PROD_SOLD_HISTORY:
//                    list = ProductUtils.fetchPastThreeYearsSold(Arrays.asList(Long.parseLong(productNum)), location);
//                    break;
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
                        Map<String, Object> hmap = new HashMap<String, Object>();
                        hmap.put("name", pw.name());
                        hmap.put("desc", pw.getDesc());
                        list.add(hmap);
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
        }else {
            response.getWriter().print("OK");
        }
    }
}
