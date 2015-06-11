package com.soward.controller;

import com.soward.enums.ProductCacheEnum;
import com.soward.object.DailySalesCache;
import com.soward.object.Invoice;
import com.soward.object.ProductSold;
import com.soward.object.Transaction;
import com.soward.util.InvoiceUtil;
import com.soward.util.LoginUtil;
import com.soward.util.ProductsLocationCountUtil;
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
import java.util.*;

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
        String location = request.getParameter("location");
        String toDateStr  = request.getParameter("toDate");
        String fromDateStr  = request.getParameter("fromDate");
        String salesDate  = request.getParameter("date");
        String invCacheId  = request.getParameter("invCacheId");
        String done  = request.getParameter("done");
        String move  = request.getParameter("move");

        Invoice inv = null;
        List invList = null;
        Map map = null;
        List<Transaction> transList = null;
        Date toDate = getDate(toDateStr);
        Date fromDate = getDate(fromDateStr);
        Date date = getDate(salesDate);
        log.info(InvoiceUtil.sdf.format(new Date())+ " Invoices endpoint: "+endpoint+" accessed by: "+username);
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
                case HOLD_BIN_GET:
                    map = TransUtil.getAllHBHash();
                    break;
                case INV_CACHE_GET:
                    if(toDate != null) {
                        DailySalesCache dsc = InvoiceUtil.getInventoryCacheForDate(toDate, location);
                        map = new HashMap<String, Object>();
                        map.put("DSC", dsc);
                    }
                    break;
                case PROD_COUNT_GET:
                    if(toDate != null && fromDate != null) {
                        map = InvoiceUtil.getProductCountsForSales(fromDate, toDate, location);
                    }
                    break;
                case TRANS_GET:
                    transList = TransUtil.getTransaction(invoiceNum, true);
                    break;
                case TRANS_PUT:
                    TransUtil.addTransactionToInvoice(username, invoiceNum, productNum);
                    break;
                case INV_CACHE_MOVE_PUT:
                    InvoiceUtil.saveInventoryCacheProperty(invCacheId, Integer.parseInt(move), productNum, ProductCacheEnum.MOVE);
                    break;
                case INV_CACHE_DONE_PUT:
                    InvoiceUtil.saveInventoryCacheProperty(invCacheId, "true".equals(done), productNum, ProductCacheEnum.DONE);
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
                case PROD_SOLD_FOR_INVOICES:
                    if(toDate != null && fromDate != null) {
                        map = InvoiceUtil.getProdSoldForInvoices(fromDate, toDate, location);
                        //save invoice to db return id
                    }
                    break;
                case HOURLY_SALES_GET:
                    if(toDate != null && fromDate != null) {
                        map = InvoiceUtil.getHourlyLocatioForDate(fromDate, toDate, location, false);
                    }
                    break;
                case SALES_GET:
                    if(toDate != null && fromDate != null) {
                        invList = InvoiceUtil.getForDate(fromDate, toDate, location, true, true);
                        //save invoice to db return id
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
        }else if(map != null){
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(map);
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

    public Date getDate(String salesDate) {
        if(salesDate != null){
            //"2015-03-11T06:00:00.000Z"
            // 2015-03-18T03:54:55.965Z
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = sdf.parse(salesDate.replace("\"", ""));
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}