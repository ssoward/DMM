package com.soward.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by ssoward on 9/9/14.
 */
public class LoginUtil {
    public static void checkAccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        java.util.Date now = new java.util.Date();
        NumberFormat bdec = NumberFormat.getCurrencyInstance(Locale.US);
        String Uid = (String) session.getAttribute("Uid");
        UserUtil userAd = new UserUtil();
        String userrole = (String) session.getAttribute("userrole");

        boolean isAdmin = false;
        try{
            isAdmin = userAd.isAdmin(Uid);
        }catch(Exception e){
            //
        }
        String username = (String) session.getAttribute("username");
        String ipaddress = (String) session.getAttribute("ipaddress");
        //System.out.println(Uid);
        if (Uid == null||userrole == null || username == null) {
            request.getSession().invalidate();
            if (session != null) {
                session = null;
            }
//            response.sendRedirect("home.jsp?message=Please Login");
            response.sendError(403);
//            throw new AccessDeniedException("Please Log IN");
        }

        else if(userrole !=null && userrole.equals( "adam" )){
            response.sendRedirect("adamIndex.jsp");
        }

    }
}
