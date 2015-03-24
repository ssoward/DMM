package com.soward.util;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.soward.db.DB;
import com.soward.enums.TransTypeEnum;
import com.soward.json.InvoiceJSONEnum;
import com.soward.json.TransJSONEnum;
import com.soward.object.ArchivedInvoice;
import com.soward.object.Invoice;
import com.soward.object.RegLocation;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: ssoward
 * Date: 2/14/12
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class ArchivedInvoiceUtil {
    private String json;

    public ArchivedInvoiceUtil(String invoiceJSON) {
        this.json = invoiceJSON;
    }

    public static ArchivedInvoice getReversedForId(Long id){
        String sql = "select * from archivedinvoice where id = ?";
        try {
            DB db = new DB();
            Connection con = db.openConnection();
            PreparedStatement p = null;

            p = con.prepareStatement( sql );
            p.setLong(1, id);

            ResultSet rset = p.executeQuery();
            ArrayList<ArchivedInvoice> ai = rsetToList(rset);
            return ai!=null&&!ai.isEmpty()?ai.get(0):null;

        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return null;


    }
    public static ArrayList<ArchivedInvoice> getReversedList(String cal1, String cal2, String user, String register){
        String d1 = getDate(cal1);
        String d2 = getDate(cal2);

        if(cal1 == null){
            return null;
        }

        String sql = "select * from archivedinvoice";

        String where = "";

        if(d1!=null && d2 !=null){
            where = " where date > '"+d1+"' and date < '"+d2+"'";
        }
        else if (d1!=null){
            where = " where date like '"+d1+"%'";
        }

        if(register!=null && !register.equals("Any")){
            if(!StringUtils.isBlank(where)){
                where += " and register = '"+register+"'";
            }
            else{
                where += " where register = '"+register+"'";
            }
        }

        if(user!=null && !user.equals("Any")){
            if(!StringUtils.isBlank(where)){
                where += " and username = '"+user+"'";
            }
            else{
                where += " where username = '"+user+"'";
            }
        }
        sql += where;

        try {
            DB db = new DB();
            Connection con = db.openConnection();
            PreparedStatement p = null;

            p = con.prepareStatement( sql );

            System.out.println(sql);
            ResultSet rset = p.executeQuery();
            ArrayList<ArchivedInvoice> ai = rsetToList(rset);
            return ai;

        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return null;


    }

    private static ArrayList<ArchivedInvoice> rsetToList(ResultSet rset) {
        ArrayList<ArchivedInvoice> rList = new ArrayList<ArchivedInvoice>();
        try{
            while(rset.next()){
                ArchivedInvoice arc = new ArchivedInvoice();
                arc.setInvoiceJSON(rset.getString("invoice"));
                arc.setUserId(rset.getString("userid"));
                arc.setUserName(rset.getString("username"));
                arc.setInvoiceNum(rset.getString("invoicenum"));
                arc.setReason(rset.getLong("reason"));
                arc.setAdditionComments(rset.getString("comments"));
                arc.setDate(rset.getTimestamp("date"));
                arc.setId(rset.getLong("id"));

                rList.add(arc);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return rList.isEmpty()?null:rList;

    }


    private static String getDate(String date){
        String strd1 = null;
        if(!StringUtils.isBlank(date)){
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                SimpleDateFormat sql = new SimpleDateFormat("yyyy-MM-dd");
                Date d1 = sdf.parse( date );
                strd1 = sql.format(d1);
            } catch (ParseException e) {
                /* bad date */
            }
        }
        return strd1;
    }

    public static String createInvoice(String json, RegLocationUtil rUtil){
        return createInvoice(json, rUtil, false);
    }
    public static String createInvoice(String json, RegLocationUtil rUtil, boolean test){

        ArchivedInvoiceUtil aiu = new ArchivedInvoiceUtil(json);
        String tr = aiu.getJSONStr(InvoiceJSONEnum.TRANSLIST);
        tr = tr.substring(1, tr.length()-1);
        ArchivedInvoiceUtil tList = new ArchivedInvoiceUtil(aiu.getJSONStr(InvoiceJSONEnum.TRANSLIST));
        org.json.JSONArray oList = null;

        String str = "";

        String register = "";
        if(rUtil!=null){ //
            RegLocation reg = rUtil.getRegLocation(aiu.getJSONStr(InvoiceJSONEnum.LOCATIONNUM));
            register = reg!=null?reg.getRegIP()+" ("+reg.getRegName()+")":"";
        }

        String orderType = "";
        try{
            oList = (org.json.JSONArray) new JSONTokener(tList.json).nextValue();
            if(oList!=null && oList.get(0)!=null){
                orderType = ((JSONObject) oList.get(0)).getString(TransJSONEnum.TRANSTYPE.label);
                TransTypeEnum enu = TransTypeEnum.getValueForCode(orderType);
                if(enu!=null){
                    orderType = enu.label;
                }
            }
        }catch(Exception e){
            //e.printStackTrace();
        }




        str += " <b>Invoice #"+ aiu.getJSONStr(InvoiceJSONEnum.INVOICENUM)               +"</b><br/>";
        str += " <b>--------------------------------                                       </b><br/>";
        str += " <b> Clerk:      </b>"+aiu.getJSONStr(InvoiceJSONEnum.USERNAME2)            +"<br/>";
        str += " <b> Register:   </b>"+register                                             +"<br/>";
        str += " <b> Order Type: </b>"+ orderType                                           +"<br/>";
        str += " <b> Invoice:    </b>"+aiu.getJSONStr(InvoiceJSONEnum.INVOICENUM)           +"<br/>";
        str += " <b> Date:       </b>"+Utils.dp(aiu.getJSONStr(InvoiceJSONEnum.INVOICEDATE))+"<br/>";
        str += " <b> Acct:       </b>"+aiu.getJSONStr(InvoiceJSONEnum.ACCOUNTNUM)           +"<br/>";
        str += "<br/>";
        try{
            oList = oList !=null?oList:(org.json.JSONArray) new JSONTokener(tList.json).nextValue();
            double ttotal = Double.parseDouble(aiu.getJSONStr(InvoiceJSONEnum.INVOICETOTAL));
            str += "<table cellpadding=\"0\" cellspacing=\"0\" border=\"1\"><tr>";
            str += "<th class=\"table-header\"><b>Prod#</b></th>";
            str += "<th class=\"table-header\"><b>Name </b></th>";
            str += "<th class=\"table-header\"><b>Qty  </b></th>";
            str += "<th class=\"table-header\"><b>Total</b></th></tr>";
            for(int i = 0; i < oList.length(); i++){
                JSONObject object = (JSONObject) oList.get(i);
                double price = Double.parseDouble(object.getString(TransJSONEnum.TRANSCOST.label));
                int qty = Integer.parseInt(object.getString(TransJSONEnum.PRODUCTQTY.label));
                str += "<tr>";
                str+=  "<td class=\"table-element\">"+object.getString(TransJSONEnum.PRODUCTNUM.label)         +"</td>";
                str+=  "<td class=\"table-element\">"+object.getString(TransJSONEnum.PRODUCTNAME.label)        +"</td>";
                str+=  "<td class=\"table-element\">"+qty                                                      +"</td>";
                str+=  "<td class=\"table-element\">"+new java.text.DecimalFormat( "$0.00" ).format(price    ) +"</td>";
                str += "</tr>";
            }
            str += "</table><br/>";
            str += "--------------------------------<br/>";
            str += " <b>Discount:     </b>"+aiu.getJSONStr(InvoiceJSONEnum.INVOICEDISCOUNTSUM) +"<br/>";
            str += " <b>Tax:          </b>"+aiu.getJSONStr(InvoiceJSONEnum.INVOICETAX) +"<br/>";
            str += " <b>Grand Total:  </b>" +new java.text.DecimalFormat( "$0.00" ).format(ttotal)+"<br/>";
        }catch(Exception e){
            e.printStackTrace();
        }





//        try {
//            JSONObject object = (JSONObject) new JSONTokener(tList.json).nextValue();
//            Iterator iter = object.sortedKeys();
//            while(iter.hasNext()){
//                System.out.println(((String)iter.next()));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//        str += aiu.json;
//        str += "\n";

//        str += tr;

        if(test){
            str = str.replaceAll("<br/>", "\n");
        }

        return str;
    }

    public static String getJSONStr(String json, InvoiceJSONEnum key){
        try {
            JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
            return object.getString(key.label);
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public String getJSONStr(InvoiceJSONEnum key){
        try {
            JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
            return object.getString(key.label);
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static void main (String args[]){
//        ArchivedInvoice ai = ArchivedInvoiceUtil.getReversedForId(39l);
        ArchivedInvoice ai = ArchivedInvoiceUtil.getReversedForId(55l);
        RegLocationUtil rUtil = new RegLocationUtil();
        System.out.println(ArchivedInvoiceUtil.createInvoice(ai.getInvoiceJSON(), rUtil, true));

//        for(ArchivedInvoice gto:  ArchivedInvoiceUtil.getReversedList("02/04/2012", null, "scott", null)){
//            //System.out.println(ArchivedInvoiceUtil.getJSONStr(gto.getInvoiceJSON(), InvoiceJSONEnum.ACCOUNT));
//            //System.out.println(gto.getId()+" "+ArchivedInvoiceUtil.createInvoice(gto.getInvoiceJSON(), null));
//            System.out.println(gto);
////                JSONObject object = (JSONObject) new JSONTokener(gto.getInvoiceJSON()).nextValue();
////                Iterator iter = object.sortedKeys();
////                while(iter.hasNext()){
////                    System.out.println(((String)iter.next()).toUpperCase());
////                }
////
////
////                String query = object.getString(InvoiceJSONEnum.LOCATIONNUM.label);
////                System.out.println(query);
//        }
    }
}
