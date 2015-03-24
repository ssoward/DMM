package com.soward.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.soward.object.Order;
import com.soward.object.Product;
import com.soward.object.Supplier;
import com.soward.object.SupplierData;
import com.soward.util.AccountUtil;
import com.soward.util.OrderUtils;
import com.soward.util.ProductUtils;
import com.soward.util.SupplierUtil;
import com.soward.util.TransUtil;
import com.soward.util.Utils;

public class InventoryUtilAjax extends HttpServlet {
    public static final String key = "invIndexVisited";
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String inventoryIndex = request.getParameter("inventoryIndex");
        String accountTypeUpdate = request.getParameter("accountTypeUpdate");
        String prodNum = request.getParameter("prodNum");
        String supNum = request.getParameter("supNum");
        String function       = request.getParameter("function");
        String note       = request.getParameter("note");
        String orderNum       = request.getParameter("orderNum");
        String locationName   = request.getParameter("locationName");
        String userName       = request.getParameter("userName");
        String prodStr        = request.getParameter("prodStr");
        String orderId        = request.getParameter("orderId");
        String checked        = request.getParameter("checked");

        String mCount         = request.getParameter("mCount");
        String locaMove       = request.getParameter("locaMove");

        String count = request.getParameter("count");
        String invCount = request.getParameter("invCount");

        PrintWriter out = response.getWriter();
        response.setContentType("text/xml");
        HashMap<String, HashMap<String, String>> mapAll = null;
        if(!StringUtils.isBlank(locationName)){
            mapAll = (HashMap)request.getSession().getAttribute( locationName );
        }
        HashMap<String, String> map = new HashMap<String, String>();
        if(mapAll==null){
            mapAll = new HashMap<String, HashMap<String, String>>();
        }
        if(mapAll.containsKey(supNum)){
            map = mapAll.get(supNum);
        }
        HashSet<String> set = (HashSet)request.getSession().getAttribute( key );
        if(set==null){
            set = new HashSet<String>();
        }

        // run checkbox
        if(!StringUtils.isBlank( inventoryIndex )&&!StringUtils.isBlank(checked)){
            System.out.println("================== "+inventoryIndex);
            boolean bool = Boolean.parseBoolean(checked);
            if(bool){
                set.add( inventoryIndex );
            }
            else if(set.contains(inventoryIndex)){
                set.remove(inventoryIndex);
            }
            request.getSession().setAttribute( key, set );
            //Iterator<String> iter = set.iterator();
            //out.println("<img src=\"images/blueCheck.gif\"/>");
            out.println("<font color=\"red\">*</font>");
            out.flush();
            out.close();
        }
        // run update account type2
        else if(!StringUtils.isBlank( accountTypeUpdate )){
            String acct = request.getParameter("acct");
            //System.out.println("================== "+accountTypeUpdate+" account: "+acct);
            AccountUtil.saveType1ForId(acct, accountTypeUpdate);
            out.println("<font color=\"blue\">Saved</font>");
            out.flush();
            out.close();
        }
        else if(!StringUtils.isBlank(function)&&function.equals("fetchProdHist")){
            int phCc = TransUtil.getTransHistForProd(prodNum, locationName);
            out.println(getYearSold(phCc, 12));
            out.flush();
            out.close();
        }
        else if(!StringUtils.isBlank(function)&&function.equals("saveNewProd")){
            List<Order>oList = OrderUtils.getAllForSup(supNum, locationName);
            Order o = new Order();
            boolean createOrder = true;
            for(Order or: oList){
                if(or.getProdNum() == (Integer.parseInt(prodNum))){
                    createOrder = false;
                    System.out.println("ERROR, this product has already been added for this supplier.");
                }
            }
            if(createOrder){
                o.setCount(0);
                o.setProdNum(Integer.parseInt(prodNum));
                o.setSupplierNum(Integer.parseInt(supNum));
                o.setLocation(locationName);
                OrderUtils.saveUpdateOrderList(Arrays.asList(o));
            }
            createOrderTable(supNum, out, locationName);

        }

        else if(!StringUtils.isBlank(function)&&function.equals("deleteSupProdOrderCount")){
            if(StringUtils.isBlank(orderId)){
                System.out.println("Non valid input: "+orderId);
            }else{
                System.out.println("deleted order: "+orderId);
                OrderUtils.deleteSupProdOrder(orderId);
                createOrderTable(supNum, out, locationName);
            }
        }
        else if(!StringUtils.isBlank(function)&&function.equals("deleteAllSupProdOrderCount")){
            if(StringUtils.isBlank(supNum)){
                System.out.println("Non valid input: "+supNum);
            }else{
                System.out.println("deleted all supplier orders: "+supNum);
                OrderUtils.deleteAllSupProdOrder(supNum, locationName);
                createOrderTable(supNum, out, locationName);
            }
        }
        else if(!StringUtils.isBlank(function)&&function.equals("showSupOrdersEmail")){
            createEmailOrderTable(supNum, out, locationName);
        }

        else if(!StringUtils.isBlank(function)&&function.equals("fetchSupProdStr")){
            List<Product> pList = ProductUtils.fetchSupProdsForStr(prodStr, supNum);
            if(pList!=null&&!pList.isEmpty()){
                out.println("<select size=\"10\" id=\"productOptions\" ondblclick=\"selectedProdDbClick();\" onkeypress=\"selectedProd(event);\">");
                for(Product p: pList){
                    String space = "";
                    for(int i = 0; (p.getProductCatalogNum().length()+i)<10;i++){
                        space +="&nbsp;";
                    }
                    out.println("<option value=\""+p.getProductNum()+"\">"+p.getProductCatalogNum()+space+p.getProductName()+"</option>");
                }
                out.println("</select>");
            }else{
                out.println("Nothing Found");
            }
            out.flush();
            out.close();
        }
        else if(!StringUtils.isBlank(function)&&function.equals("saveSupProdOrderCount")){
            try{
                int xcc = Integer.parseInt(count);
                Order sData = OrderUtils.fetchOrder(orderNum);
                if(sData.getCount()<1&&xcc<1){
                    out.println("<font color=\"red\">"+(sData.getCount())+"</font>");
                }else{
                    sData.setCount(xcc+sData.getCount());
                    OrderUtils.saveUpdateOrder(sData);
                    out.println("<font color=\"blue\">"+(sData.getCount())+"</font>");
                }
            }catch(Exception e){
                out.println("<font color=\"red\">Failed</font>");
            }
            out.flush();
            out.close();

        }
        else if(!StringUtils.isBlank(function)&&function.equals("moveInv")){
            boolean tri = true;
            String ssave = "<font color=\"blue\">Saved.</font>";
            if(StringUtils.isBlank(mCount)||StringUtils.isBlank(prodNum)||StringUtils.isBlank(locaMove)){
                tri = false;
                ssave = "<font color=\"red\">Failed to save for count: "+mCount+", prod# "+prodNum+", and move: "+locaMove+"</font>";
            }
            if(tri){
                ProductUtils.moveProdQtyFromTo(prodNum, mCount, locaMove, userName);
            }
            out.println(ssave);
            out.flush();
            out.close();

        }
        else if(!StringUtils.isBlank(function)&&function.equals("toggleOrder")){
            Order o = OrderUtils.toggleOrderArrvied(orderNum, userName);
            String ssave = Utils.d(o.getDateReceived());
            out.println(ssave);
            out.flush();
            out.close();

        }
        else if(!StringUtils.isBlank(function)&&function.equals("saveOrderNotes")){
            Order o = OrderUtils.fetchOrder(orderNum);//(orderNum, userName);
            o.setNotes(note);
            OrderUtils.saveUpdateOrder(o);
            out.println("SAVED:  "+o.getNotes());
            out.flush();
            out.close();

        }
        else if(!StringUtils.isBlank(function)&&function.equals("getOrderNotes")){
            Order o = OrderUtils.fetchOrder(orderNum);//(orderNum, userName);
            out.println("<textarea id=\"nnn\" name=\"eContent\" cols=\"60\" rows=\"13\">");
            out.println(o.getNotes()!=null?o.getNotes():"");
            out.println("</textarea>");
            out.println("<div id=\"notesSave\"></div>");
            out.println("<input type=\"button\" onclick=\"saveNotes(document.getElementById('nnn').value,'"+o.getId()+"');\" value=\"Save\"/>");
            out.flush();
            out.close();
        }
        else if(!StringUtils.isBlank(function)&&function.equals("supSend")){
            //save all session orders for this supplier and compare total with threshold value
            if(mapAll.containsKey(supNum)&&!map.isEmpty()){
                Set<String> sett = map.keySet();
                Iterator<String> iter = sett.iterator();
                ArrayList<Order> oList = new ArrayList<Order>();
                while(iter.hasNext()){
                    Order o = new Order();
                    String pNum = iter.next();
                    //checks checkbox is checked for prod ie. 'Done' checkbox=true
                    //					if(!set.contains(pNum)){
                    //						continue;
                    //					}
                    String cNum = map.get(pNum);
                    o.setDateEval(new Date());
                    //check set to see if the user has checked/finalized this prod
                    try{
                        o.setProdNum(Integer.parseInt(pNum));
                        o.setCount(Integer.parseInt(cNum));
                        o.setSupplierNum(Integer.parseInt(supNum));
                        o.setLocation(locationName);
                        oList.add(o);
                    }catch(Exception e){
                        e.printStackTrace();
                        /*not a valid number*/}
                }
                OrderUtils.saveUpdateOrderList(oList);
                out.println("<font color=\"blue\">Saved "+oList.size()+"</font>");

                //				what I need to do now is to check the set (users checkbox on reportSales) before I save the values
                //				finish Order page with all current Orders,
                //				find a way to remove check of those that have been saved.
                mapAll.remove(supNum);
                request.getSession().setAttribute( locationName, mapAll );

            }else{
                out.println("<font color=\"blue\">Nothing to save "+supNum+"</font>");
            }
        }

        // run select for inventory count
        if(!StringUtils.isBlank( invCount )){
            Order o = OrderUtils.fetchOrderForProdLocation(prodNum, locationName);
            o.setDateEval(new Date());
            try{
                o.setProdNum(Integer.parseInt(prodNum));
                o.setCount(Integer.parseInt(count));
                o.setSupplierNum(Integer.parseInt(supNum));
                o.setLocation(locationName);
                OrderUtils.saveUpdateOrderList(Arrays.asList(o));
            }catch(Exception e){
                e.printStackTrace();
                /*not a valid number*/}

//			System.out.println("ProdNum: "+prodNum+" Count: "+count);
//			map.put( prodNum, count );
//			mapAll.put(supNum, map);
        }
        if(!StringUtils.isBlank(locationName)){
            request.getSession().setAttribute( locationName, mapAll );
        }
    }
    private void createEmailOrderTable(String supNum, PrintWriter out, String locationName) {
        Supplier supplier = SupplierUtil.fetchSupForNameOrNum(supNum).get(0);
        SupplierData sData = SupplierUtil.fetchSuppliersData(supNum);
        ArrayList<Order> oList = new ArrayList<Order>();
        oList = OrderUtils.getAllForSup(supNum, true, true, locationName);
        String dSubject = "DAY MURRAY MUSIC ORDER";
        out.println("<form method=\"post\" action=\"orders.jsp\">");
        out.println("<table align=\"left\" border=\"0\" cellspacing=\"2\">");
        out.println("<tr><td align=\"right\">Subject:</td><td><input size=\"50\" type=\"text\" value=\""+dSubject+"\"                    name=\"eSubject\"/></td></tr> ");
        out.println("<tr><td align=\"right\">To:</td><td>     <input size=\"50\" type=\"text\" value=\""+supplier.getSupplierEmail()+"\" name=\"eTo\"/></td></tr>      ");
        out.println("<tr><td align=\"right\">CC:</td><td>     <input size=\"50\" type=\"text\" value=\"\"                                name=\"eCc\"/></td></tr>      ");
        out.println("<tr><td colspan=\"2\">                 ");
        out.println("<textarea name=\"eContent\" cols=\"60\" rows=\"13\">");
        out.println("Hello "+supplier.getSupplierName       ());
        out.println("");
        out.println(sData!=null&&!StringUtils.isBlank(sData.getEContent())?sData.getEContent():"");
        int coun = 1;
        for(Order oo: oList){
            out.println(coun +". Qty("+oo.getCount()+") "+oo.getProd().getProductName()+" "+oo.getProd().getProductCatalogNum());
            coun++;
        }
        out.println(" </textarea>");
        out.println("</td>");
        out.println("</tr>");
        out.println("<input type=\"hidden\" name=\"supId\" value=\""+supplier.getSupplierNum()+"\"/>");
        out.println("<input type=\"hidden\" name=\"sendEmail\" value=\"sendEmail\"/>");
        out.println("<input type=\"hidden\" name=\"locationName\" value=\""+locationName+"\"/>");
        out.println("<tr><td><input type=\"submit\" class=\"btn\" value=\"Send\" " +
                "onclick=\"return confirm('Are you sure you want to send this order to "+supplier.getSupplierName()+"?'); \"/></td></tr>");
        out.println("</table>");
        out.println("</form>");
    }
    private void createOrderTable(String supNum, PrintWriter out, String locationName) {
        Supplier supplier = null;
        supplier =  SupplierUtil.fetchSupForNameOrNum(supNum).get(0);
        ArrayList<Order> oList = new ArrayList<Order>();

        oList = OrderUtils.getAllForSup(supNum, true, true, locationName);
        out.println("<table class=\"common\" style=\"width:50%\" border=\"1\" cellspacing=\"0\" cellpadding=\"3\">");
        out.println("<tr>                                                                                         ");
        out.println("<th>Delete</th>                                                                              ");
        out.println("<th>Cat#</th>                                                                            ");
        out.println("<th>Name#</th>                                                                            ");
        out.println("<th>Count#</th>                                                                              ");
        out.println("<th>Edit</th>                                                                                ");
        out.println("</tr>                                                                                        ");
        for(Order o: oList){
            out.println("<tr>                                                                                                                ");
            out.println("<td align=\"center\"><img style=\"cursor: pointer; cursor: hand; display: inline-block; vertical-align: middle\"    ");
            out.println(" onclick=\"makeGetRequestDeleteProductSupplierOrder('"+supplier.getSupplierNum()+"', '"+o.getId()+"', '"+locationName+"');\"      ");
            out.println("	src=\"images/delete.gif\" title=\"Delete order for product "+o.getProdNum() +"\" />                              ");
            out.println("</td>                                                                                                               ");
            out.println("<td align=\"center\">"+o.getProd().getProductCatalogNum() +"</td>                                                                       ");
            out.println("<td align=\"center\">"+o.getProd().getProductName() +"</td>                                                                       ");
            out.println("<td align=\"center\"><div id=\""+o.getId()+"orderCount" +"\">"+o.getCount() +"</div></td>                      ");
            out.println("<td align=\"center\">                                                                                               ");
            out.println("<img width=\"20\" style=\"cursor: pointer; cursor: hand; display: inline-block; vertical-align: middle\"            ");
            out.println("	onclick=\"makeGetRequestSupplierOrderCountSave('"+supplier.getSupplierNum()+"', '"+o.getId()+"','"+1+"', '"+locationName+"');\" ");
            out.println("		src=\"images/go-up.gif\" title=\"Incresse order for product "+o.getProdNum() +"\" />                         ");
            out.println("<img width=\"20\" style=\"cursor: pointer; cursor: hand; display: inline-block; vertical-align: middle\"            ");
            out.println("	onclick=\"makeGetRequestSupplierOrderCountSave('"+supplier.getSupplierNum()+"', '"+o.getId()+"','"+-1+"', '"+locationName+"');\"");
            out.println("		src=\"images/go-down.gif\" title=\"Decrease order for product "+o.getProdNum() +"\" />                       ");
            out.println("</td>                                                                                                               ");
            out.println("</tr>	                                                                                                             ");
        }
        out.println("</table>");
        out.flush();
        out.close();

    }
    public static String getYearSold(int phCc, int month) {
        String ss = "<div style=\"background-color:"+getColor(phCc)+"\">";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -month);
        SimpleDateFormat sdf = new SimpleDateFormat("M/yy");
        ss += "<font color=\"black\" size=\"1\">Sold </font>";
        ss += "<font color=\"black\" size=\"2\">"+phCc+"</font>";
        ss += "<font color=\"black\" size=\"1\"> since "+sdf.format(cal.getTime())+"</font>";
        ss += "</div>";
        return ss;
    }
    public static String getColor(int phCc) {
        HashMap<Integer, String> colorHash = new HashMap<Integer, String>();
        colorHash.put(0,   "#ffff00");
        colorHash.put(20,  "#ffcc00");
        colorHash.put(40,  "#ff9900");
        colorHash.put(60,  "#ff6600");
        colorHash.put(80,  "#ff3300");
        colorHash.put(100, "#ff0000");
        if(phCc<20){
            return colorHash.get(0);
        }
        if(phCc<40){
            return colorHash.get(20);
        }
        if(phCc<60){
            return colorHash.get(40);
        }
        if(phCc<80){
            return colorHash.get(60);
        }
        if(phCc<100){
            return colorHash.get(80);
        }
        else{
            return colorHash.get(100);
        }
    }
}
