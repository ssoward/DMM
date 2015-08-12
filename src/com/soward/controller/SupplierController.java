package com.soward.controller;

import com.soward.util.InvoiceUtil;
import com.soward.util.LoginUtil;
import com.soward.util.SupplierUtil;
import org.apache.commons.discovery.log.SimpleLog;
import org.apache.commons.logging.Log;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by ssoward on 9/6/14.
 */
public class SupplierController extends HttpServlet {

    Log log = new SimpleLog(SupplierController.class.getName());

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = (String) request.getSession().getAttribute("username");
        LoginUtil.checkAccess(request, response);
        String searchStr = request.getParameter("searchStr");
        String endpoint   = request.getParameter("funct");

        List list = null;
        log.info(InvoiceUtil.sdf.format(new Date())+ " Supplier endpoint: "+endpoint+" accessed by: "+username);
        if(endpoint != null) {
            switch (Endpoint.valueOf(endpoint)) {
                case SUPPLIERS_GET:
                    list = SupplierUtil.getAllSuppliersNumAndName();
                    break;
            }
        }else if(searchStr != null) {
            log.info(InvoiceUtil.sdf.format(new Date()) + " Searching suppliers for: " + searchStr);
            list = SupplierUtil.fetchSupForName(searchStr);
        }
        if(list != null) {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(list);
            response.getWriter().print(json);
        }
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().print("back from the server from POST");
    }
}
