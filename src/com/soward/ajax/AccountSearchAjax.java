package com.soward.ajax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.soward.object.Account;
import com.soward.object.AccountInfo;
import com.soward.object.Descriptions;
import com.soward.object.Product;
import com.soward.util.AccountInfoUtil;
import com.soward.util.AccountUtil;
import com.soward.util.DescriptionsUtil;
import com.soward.util.ProductUtils;

public class AccountSearchAjax extends HttpServlet{
    /**
     * 
     */
    private static final long serialVersionUID = -2123121175540820070L;
    public int count = 0;
    public void service( final HttpServletRequest request, HttpServletResponse response ) {
        try {                        
            count++;
            String productNum = (request.getParameter( "productNum" ));
            String acctId = (request.getParameter( "acctId" ));
            String function = request.getParameter( "function" )!=null?request.getParameter( "function" ):"";

            String accNum = request.getParameter("accountNum"     );
            String accNam = request.getParameter("accountName"    );
            String accEma = request.getParameter("accountEmail"   );
            String accPh1 = request.getParameter("accountPhone1"  );
            String accPh2 = request.getParameter("accountPhone2"  );
            String accStr = request.getParameter("accountStreet"  );
            String accCit = request.getParameter("accountCity"    );
            String accSta = request.getParameter("accountState"   );
            String accZip = request.getParameter("accountZip"     );
            String accCon = request.getParameter("accountContact"     );
            String supNum = request.getParameter("supNum"     );
            String orderPublisherNum = request.getParameter("supNumorderPublisherNum"     );
            String tabNum = request.getParameter("tabNum"     );
            String acctType = request.getParameter("acctType"     );
            String accList = request.getParameter("accList"     );

            //cc info -----
            String ccType   = request.getParameter("ccType");
            String ccMonth  = request.getParameter("ccMonth");
            String ccYear   = request.getParameter("ccYear");
            String ccCSC    = request.getParameter("ccCSC");
            String ccPid    = request.getParameter("ccPid");
            String ccNum    = request.getParameter("ccNum");
            //cc info -----


            if(function.equals("updateAccountTypes")){
                if(!StringUtils.isBlank(acctType)&&
                        !StringUtils.isBlank( accList )){
                    boolean a = AccountUtil.saveType1ForId( accList, acctType );
                    if(a){
                        response.getWriter().print("Successfully updated "+(accList.split( "," )).length+" accounts.");
                    }else{
                        response.getWriter().print("Update failed, please consult sys admin.");
                    }
                }
                else{
                    response.getWriter().print("Error with account type update, invalid data: Account List: "+accList+" New account type: "+acctType);
                }
            }
            if(function.equals("fetchAccount")){
                if(!StringUtils.isBlank(acctId)){
                    Account a = AccountUtil.fetchAccount(acctId);
                    if(a!=null){
                        //						DailyOrderGto doGto = new DailyOrderGto();
                        //						doGto.setAccount(a);
                        //						JSONObject jo = new JSONObject();
                        //						jo.put("a", "helloWorld");
                        response.getWriter().print( new JSONObject(a).toString());
                    }
                }
            }
            if(function.equals("fetchCCInfo")){
                if(!StringUtils.isBlank(accNum)){
                    //System.out.println("AccNum: "+accNum);
                    AccountInfo ai = AccountInfoUtil.fetchForAccountNum( accNum );
                    //System.out.println(ai.toString());
                    response.getWriter().print( new JSONObject(ai).toString());
                }
            }
            if(function.equals("saveCCInfo")){
                if(!StringUtils.isBlank(accNum)){
                    AccountInfo ai = new AccountInfo();
                    ai.setPid( ccPid );
                    ai.setCode( ccCSC );
                    ai.setMonth( ccMonth );
                    ai.setYear( ccYear );
                    ai.setName( accNam );
                    ai.setType( ccType );
                    ai.setNumber( ccNum );
                    ai.setAccount(accNum );
                    AccountInfoUtil.saveUpdate( ai );
                    response.getWriter().print( "Successfully updated billing information");
                }
            }
            else if(function.equals("saveUpdateAccount")){
                Account acct = new Account();
                if(!StringUtils.isBlank(accNum)){
                    acct = AccountUtil.fetchAccount(accNum);
                }
                acct.setAccountName(accNam);
                acct.setAccountEmail(accEma);
                acct.setAccountPhone1(accPh1);
                acct.setAccountPhone2(accPh2);
                acct.setAccountStreet(accStr);
                acct.setAccountCity(accCit);
                acct.setAccountState(accSta);
                acct.setAccountZip(accZip);
                acct.setAccountContact( accCon );
                Account a = AccountUtil.saveUpdateAccount(acct);
                if(a!=null){
                    response.getWriter().print( new JSONObject(a).toString());
                }

            }
            else if(function.equals("searchAccounts")){

                ArrayList<Account> aList = AccountUtil.searchAccounts( productNum );
                request.getSession().setAttribute( "searchHash", AccountUtil.createMap(aList));
                StringBuffer cList = new StringBuffer("<select  id=\"productOptions\" onkeypress=\"runSubmit(event);\" onchange=\"operSelected(this)\" ondblclick=\"closeDropDownAccounts();\"size=\"5\" name=\"sval\" value=\"\">");
                for(Account aa: aList){
                    String value =  aa.getAccountNum()+": "+ aa.getAccountName()+" "+ aa.getAccountPhone1(); 
                    cList.append( "<option value=\""+aa.getAccountNum()+"\">"+
                            value+"</option>");
                }
                cList.append( "</select>");
                response.getWriter().print( cList.toString() );
            }
            else if(function.equals("fetchSupplierItemList")){
                HashMap<String, String> aList = ProductUtils.fetchProductsForSupplier(supNum);
                response.getWriter().print(new JSONObject( aList ).toString()  );
            }
            else if(function.equals("fetchProductForItem")){
                Product prod = ProductUtils.fetchProductForNum(productNum, null);

                try{
                    String key = prod.getCategory().charAt(0)+"";
                    HashMap<String, String> descHash = new HashMap<String, String>();
                    List<Descriptions> allDescriptions = DescriptionsUtil.getAll2CodeDescriptions(key);
                    for(Descriptions desc: allDescriptions){
                        descHash.put(desc.getDescriptionCode(), desc.getDescriptionName());
                    }
                    String keyyy = null;
                    try{keyyy = prod.getCategory().charAt(5)+"";}catch(Exception e){/*do nothing*/}
                    if(keyyy!=null){
                        String nameee = (String)descHash.get(keyyy);
                        prod.setCategory(nameee);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                response.getWriter().print(new JSONObject( prod ).toString()  );
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
