package com.soward.controller;

import com.soward.util.InvoiceUtil;
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchStr = request.getParameter("searchStr");
        log.info(InvoiceUtil.sdf.format(new Date())+ " Searching suppliers for: "+searchStr);
        List list = SupplierUtil.fetchSupForName(searchStr);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(list);
        response.getWriter().print(json);
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().print("back from the server from POST");
    }

//    public int count = 0;
//    //  ArrayList hp = new ArrayList<String>();
//    public void service( final HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
//        System.out.println("hello world");
//        response.getWriter().print( "back from the server");
//    }
}
