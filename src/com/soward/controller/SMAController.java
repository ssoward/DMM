package com.soward.controller;

import com.soward.object.Invoice;
import com.soward.object.Transaction;
import com.soward.service.SMAService;
import com.soward.service.SMAServiceImpl;
import com.soward.util.InvoiceUtil;
import com.soward.util.LoginUtil;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by ssoward on 1/1/15.
 */

public class SMAController  extends HttpServlet {

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SMAService smaService = new SMAServiceImpl();
        String username = (String) request.getSession().getAttribute("username");
        LoginUtil.checkAccess(request, response);
        String endpoint = request.getParameter("funct");
        String invoiceId = request.getParameter("invoiceId");
        String order = null;
        Invoice inv = null;
        List<Invoice> invList = null;
        List<Transaction> transList = null;
        if(endpoint != null) {
            switch (Endpoint.valueOf(endpoint)) {
                case SYNC_INVOICES:
                    invList = smaService.syncInvoices();
                    break;
                case SYNCED_INVOICES:
                    invList = smaService.getSynced();
                    break;
                case ORIGINAL_FOR_INV:
                    order = smaService.getOriginalInvoice(invoiceId);
                    break;
                case DELETE_RECORD_INV:
                    smaService.deleteRecordInvoice(invoiceId);
                    break;
            }
        }
        response.setContentType("application/json");
        if(inv != null) {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(inv);
            response.getWriter().print(json);
        }else if(invList != null){
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(invList);
            response.getWriter().print(json);
        }else if(transList != null){
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(transList);
            response.getWriter().print(json);
        }else if(order != null){
            response.getWriter().print(order);
        }
        else {
            response.getWriter().print("OK");
        }
    }
}
