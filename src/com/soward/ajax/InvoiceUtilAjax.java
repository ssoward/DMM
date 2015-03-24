package com.soward.ajax;

import com.soward.enums.DeleteInvoiceEnum;
import com.soward.enums.LocationsDBName;
import com.soward.object.ArchivedInvoice;
import com.soward.object.Invoice;
import com.soward.object.Transaction;
import com.soward.util.InvoiceUtil;
import com.soward.util.ProductsLocationCountUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 2/4/12
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceUtilAjax extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = -2123121175540820070L;
    public void service( final HttpServletRequest request, HttpServletResponse response ) {
        String function = request.getParameter("function"     );
        String text = request.getParameter("text"     );
        String inum = request.getParameter("inum"     );
        String reason = request.getParameter("reason"     );
        boolean reverseInv = new Boolean(request.getParameter("reverseInv" ));

        try{
            if(function.equals("deleteInvoice")){
//                System.out.println(text);
//                System.out.println(inum);
//                System.out.println(reason);
//                System.out.println(request.getSession().getAttribute("Uid"));

                //1. fetch invoice/transactions
                boolean successfullyBK = false;
                Invoice inv = null;
                if(!StringUtils.isBlank(inum)){
                    inv = InvoiceUtil.getForPid(inum);
                    //2. archive
                    if(inv!=null){
                        String invJSON = new JSONObject(inv).toString();
                        List<Transaction> transList = inv.getTransList();
                        ArchivedInvoice ai = new ArchivedInvoice();
                        ai.setInvoiceJSON(invJSON);
                        ai.setUserId((String)request.getSession().getAttribute("Uid"));
                        ai.setUserName((String)request.getSession().getAttribute("username"));
                        ai.setReason(DeleteInvoiceEnum.getForName(reason).getId());
                        ai.setInvoiceNum(inum);
                        ai.setAdditionComments(text);
                        ai.setRegister(inv.getLocationNum());
                        successfullyBK = InvoiceUtil.saveArchiveInvoice(ai);
                    }

                }
                //3. delete invoice/transactions
                //4. restock inventory
                if(successfullyBK){
                    LocationsDBName location = null;
                    int accNum = Integer.parseInt(inv.getAccountNum());
                    switch (accNum){
                        case 102: //LEHI
                            location = LocationsDBName.LEHI;
                            break;
//                        case 103: //OREM
//                            location = LocationsDBName.OREM;
//                            break;
                        case 105: //DV
                            location = LocationsDBName.DV;
                            break;
                        default:
                            //Could have been a sale at OREM or LEHI that was under an clients account number.
                            //register:
                            //20 == LEHI
                            //30 == OREM
                            int register = Integer.parseInt(inv.getLocationNum());
                            switch(register){
                                case 20:
                                    location = LocationsDBName.LEHI;
                                    break;
                                case 30:
//                                    location = LocationsDBName.OREM;
                                    break;
                                default:
                                    location = LocationsDBName.MURRAY;
                                    break;
                            }
                            break;
                    }
                    if(reverseInv){
                        for(Transaction trans: inv.getTransList()){
                            ProductsLocationCountUtil.addToCountForLocation(location, Integer.parseInt(trans.getProductQty()), trans.getProductNum());
                        }
                    }
                    InvoiceUtil.deleteInvoice(inum);
                }

                response.getWriter().print("Successfully deleted invoice #"+inum+".");
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}