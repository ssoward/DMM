package com.soward.reports;

import com.soward.enums.Department;
import com.soward.object.Departments;
import com.soward.object.Product;
import com.soward.util.InventoryReport;
import com.soward.util.Utils;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProductInventoryReport implements ReportCollection {

    private String title = "Product Inventory Report";
    public Collection getData( Map<String, Object> parameters ) {
        String outputType = (String)parameters.get("outputType");
        boolean pdf = outputType!=null&&outputType.equals( "PDF" );

        parameters.put("column001", "DMM#");
        parameters.put("column002", "Catalog#");
        parameters.put("column003", "Name");
        parameters.put("column004", "Dept");
        if(pdf){
            parameters.put("column005", "Price");
            parameters.put("column006", "Qty");
            parameters.put("column007", "Total");
        }else{
            parameters.put("column005", "Catagory#");
            parameters.put("column006", "Price");
            parameters.put("column007", "Qty");
            parameters.put("column008", "LastSold");
            parameters.put("column009", "LastInvDt");
            parameters.put("column010", "Total");
        }



        Collection<Map> reportData = new Vector<Map>();
        Map<String, String> row = new HashMap<String, String>();
        String invQtyStart = (String)parameters.get("invQtyStart");
        String invQtyEnd = (String)parameters.get("invQtyEnd");
        ArrayList<Departments> depts = (ArrayList)((HttpSession)parameters.get("session")).getAttribute("selDept");

        String dateOne = (String)parameters.get( "dateOne" );
        String dateTwo = (String)parameters.get( "dateTwo" );
        String perDept = (String)parameters.get( "perDept" );


        InventoryReport invUtil = new InventoryReport();
        List<Product> invList = null;
        title = "Product Inventory Report";
        if(perDept!=null){
            title = "Product Per Dept Report";
        }
        invList = invUtil.getInventoryReport(dateOne, dateTwo, invQtyStart, invQtyEnd, depts, perDept);
        double teTotal = 0.0;
        String grandTot = "";
        for(Product temp: invList){
            double tempTotal = 0;
            String tTotal = "";
            String cTotal = "";
            String lastSold = "";
            String lastInv = "";
            try{
                tempTotal = (Double.valueOf(temp.getProductCost1()).doubleValue())*(Integer.parseInt(temp.getProductCost4()));
                teTotal += tempTotal;
                tTotal = new java.text.DecimalFormat("$0.00").format(tempTotal);
                tempTotal = Double.valueOf(temp.getProductCost1()).doubleValue();
                cTotal =  new java.text.DecimalFormat("$0.00").format(tempTotal);
                grandTot =  new java.text.DecimalFormat("$0.00").format(teTotal);
            }catch(Exception e){ }
            try{

                lastSold = temp.getLastSold().substring(0, temp.getLastSold().length() -5);
            }catch(Exception e){ }
            try{
                lastInv = temp.getLastInvDate().substring(0, temp.getLastInvDate().length() -5);
            }catch(Exception e){ }
            row = new HashMap<String, String>();
            String prodName = temp.getProductName();
            String prodDesc = temp.getProductDescription();
            row.put("value001", temp.getProductNum());
            row.put("value002", temp.getProductCatalogNum());
            row.put("value003", pdf&&prodName.length()>20?prodName.substring( 0,20 ):prodName);
            row.put("value004", getDept(temp.getCategory()));
            if(pdf){
                row.put("value005", cTotal);
                row.put("value006", temp.getNumAvailable());
                row.put("value007", tTotal);
            }else{
                row.put("value005", temp.getCategory());
                row.put("value006", cTotal);
                row.put("value007", temp.getNumAvailable() + " ("+temp.getProductCost4()+")");
                row.put("value008", Utils.parseStringDateTo_MMDASHddDASHyyyy_withTimeInAMPM( temp.getLastSold() ));
                row.put("value009", Utils.parseStringDateTo_MMDASHddDASHyyyy_withTimeInAMPM( temp.getLastInvDate() ));
                row.put("value010", tTotal);
            }
            reportData.add( row );
        }
        try {
            Date date1 = null;
            Date date2 = null;
            if(dateOne!=null)
                date1 = new SimpleDateFormat("MM/dd/yyyy").parse( dateOne );
            if(dateTwo!=null)
                date2 = new SimpleDateFormat("MM/dd/yyyy").parse( dateTwo );
            parameters.put("fromDate", date1!=null?date1:new Date());
            parameters.put("toDate", date2!=null?date2:new Date());
        } catch ( ParseException e ) {
            e.printStackTrace();
        }

        parameters.put("title", title);
        parameters.put("grandTotal", "Grand Total: "+grandTot);
        return reportData;
    }

    private String getDept(String cat){
        return Department.getNameForCat(cat);

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
