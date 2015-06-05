package com.soward.controller;

import com.soward.object.Invoice;
import com.soward.util.AccountUtil;
import com.soward.util.InvoiceUtil;
import com.soward.util.LoginUtil;
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
 * Created by ssoward on 9/23/14.
 */
public class AccountsController extends HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Log log = new SimpleLog(AccountsController.class.getName());
        String username = (String) request.getSession().getAttribute("username");
        String searchStr = request.getParameter("searchStr");
        String location = request.getParameter("location");
        String productNum = request.getParameter("productNum");
        String acct1 = request.getParameter("acct1");
        String acct2 = request.getParameter("acct2");
        LoginUtil.checkAccess(request, response);
        String endpoint = request.getParameter("funct");
        Invoice inv = null;
        List list = null;

        log.info(InvoiceUtil.sdf.format(new Date())+ " Accounts endpoint: "+endpoint+" accessed by: "+username);
        if(endpoint != null) {
            switch (Endpoint.valueOf(endpoint)) {
                case ACCT_SEARCH:
                    list = AccountUtil.searchAccounts(searchStr);
                    break;
                case ACCT_MERGE:
                    AccountUtil.mergeAccountsDelete(acct1, acct2);
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
