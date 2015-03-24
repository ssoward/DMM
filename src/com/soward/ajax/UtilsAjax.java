package com.soward.ajax;

import com.soward.util.SendEmail;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 2/21/12
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class UtilsAjax extends HttpServlet {
    public void service( final HttpServletRequest request, HttpServletResponse response ) {
        try {
            String reList   = (request.getParameter( "reList" ));
            String eCC      = (request.getParameter( "eCC" ));
            String bCC      = (request.getParameter( "bCC" ));
            String eSubject = (request.getParameter( "eSubject" ));
            String eBody    = (request.getParameter( "eBody" ));
            String function = (request.getParameter( "function" ));

            if("sendEmail".equals(function)){
//                System.out.println(reList  );
//                System.out.println(eCC     );
//                System.out.println(eSubject);
//                System.out.println(eBody   );
                boolean success = SendEmail.sendOrderEmail(eSubject, eBody, reList, eCC, bCC);

                PrintWriter out = response.getWriter();
                response.setContentType("text/xml");
                if(success){
                    out.println("Successfully sent email/s.");
                }else{
                    out.println("Failed to sent email/s.");
                }
                out.flush();
                out.close();
            }

        } catch ( Exception e ) {
            try {
                response.sendRedirect("SystemError.html");
            } catch ( IOException e1 ) {
                e1.printStackTrace();
            }
        }
    }

}
