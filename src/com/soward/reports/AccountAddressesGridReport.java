package com.soward.reports;

import com.soward.object.Account;
import com.soward.object.Departments;
import com.soward.object.Product;
import com.soward.object.gto.ProductGto;
import com.soward.util.AccountUtil;
import com.soward.util.DepartmentsUtil;
import com.soward.util.InventoryReport;
import org.apache.commons.lang.StringUtils;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 2/23/12
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountAddressesGridReport implements ReportCollection {


    public Collection getData( Map<String, Object> parameters ) {
        String accounts = (String)parameters.get("aList");



        Collection<Map> reportData = new Vector<Map>();
        Map<String, String> row = new HashMap<String, String>();
        parameters.put("column001", "Address1");
        parameters.put("column002", "Address2");
        parameters.put("column003", "Address3");

        int i = 0;
        ArrayList<Account> aList = new ArrayList<Account>();
        String accString = "";
        if(!StringUtils.isBlank(accounts)){
            try{
                String[] eList = accounts.split(",");
                for(String cc: eList){
                    if(!StringUtils.isBlank(cc)){
                        //create like statements under 200 account nums
                        if(i<200){
                            if(accString.length()<1){//first set
                                accString = cc;
                            }else{
                                accString += ","+cc;
                            }
                            i++;
                        }
                        //when i reaches 200 run query and reset accString and i
                        else{
                            aList.addAll(AccountUtil.getAccountAddress(accString));
                            i = 0;
                            accString = "";
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(i>0){
            aList.addAll(AccountUtil.getAccountAddress(accString));
        }
        int az = aList.size();
        for(int j = 0; j<az;){
            row = new HashMap<String, String>();
            if(az>j){
                Account act = aList.get(j);
                row.put("value001", act.getAccountAddressFormated());
                j++;
            }
            if(az>j){
                Account act = aList.get(j);
                row.put("value002", act.getAccountAddressFormated());
                j++;
            }
            if(az>j){
                Account act = aList.get(j);
                row.put("value003", act.getAccountAddressFormated());
                j++;
            }
            reportData.add( row );
        }

        return reportData;
    }

    /**
     * @param args
     */
    public static void main( String[] args ) {
        String dateOne = "2007-12-30";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse( dateOne );
            new SimpleDateFormat("MM/dd/yy").format(date);
        } catch ( ParseException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//format(dateOne);

    }

}
