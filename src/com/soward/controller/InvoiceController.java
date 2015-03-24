package com.soward.controller;

import com.soward.object.Invoice;
import com.soward.object.Transaction;
import com.soward.util.InvoiceUtil;
import com.soward.util.LoginUtil;
import com.soward.util.TransUtil;
import org.apache.commons.discovery.log.SimpleLog;
import org.apache.commons.logging.Log;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by ssoward on 9/9/14.
 */
public class InvoiceController extends HttpServlet {

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Log log = new SimpleLog(InvoiceController.class.getName());

        String username = (String) request.getSession().getAttribute("username");
        LoginUtil.checkAccess(request, response);
        String endpoint   = request.getParameter("funct");
        String invoiceNum = request.getParameter("invoiceNum");
        String productNum = request.getParameter("productNum");
        String transNum   = request.getParameter("transNum");
        String productQty = request.getParameter("productQty");
        String salesDate  = request.getParameter("date");
        Invoice inv = null;
        List<Invoice> invList = null;
        List<Transaction> transList = null;
        log.info("Invoices endpoint: "+endpoint);
        if(endpoint != null) {
            switch (Endpoint.valueOf(endpoint)) {
                case PO_GET:
                    String searchStr = request.getParameter("searchStr");
                    //get list if searchStr
                    if(searchStr != null){
                        invList = InvoiceUtil.invoiceContactNum(searchStr);
                    }
                    break;
                case PO_PUT:
                    String supplierNum = request.getParameter("supplierNum");
                    inv = InvoiceUtil.createNewPO(username, supplierNum);
                    break;
                case TRANS_GET:
                    transList = TransUtil.getTransaction(invoiceNum, true);
                    break;
                case TRANS_PUT:
                    TransUtil.addTransactionToInvoice(username, invoiceNum, productNum);
                    break;
                case TRANS_QTY_PUT:
                    TransUtil.updateTransactionQty(transNum, productQty);
                    break;
                case INV_DELETE:
                    InvoiceUtil.deleteInvoice(invoiceNum);
                    break;
                case TRANS_DELETE:
                    InvoiceUtil.deleteTransaction(transNum);
                    break;
                case SALES_GET:
                    if(salesDate != null){
                        //"2015-03-11T06:00:00.000Z"
                        // 2015-03-18T03:54:55.965Z
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                            Date date = sdf.parse(salesDate.replace("\"", ""));
                            if(date != null){
                                invList = InvoiceUtil.getForDate(date);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        salesDate = null;
                    }
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
        }
        else {
            response.getWriter().print("OK");
        }
    }
    public static void main(String args[]){
        try{
            String salesDate = "2015-03-18T04:12:34.412Z";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = sdf.parse(salesDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}