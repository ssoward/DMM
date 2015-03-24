package com.soward.reports;

import com.soward.object.Departments;
import com.soward.object.Product;
import com.soward.object.gto.ProductGto;
import com.soward.util.DepartmentsUtil;
import com.soward.util.InventoryReport;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InvProductSoldReport implements ReportCollection {

    public Collection getData( Map<String, Object> parameters ) {
        String outputType = (String)parameters.get("outputType");
        boolean pdf = outputType!=null&&outputType.equals( "PDF" );

        parameters.put("column001", "DMM#");
        parameters.put("column002", "Catalog#");
        parameters.put("column003", "Name");
        parameters.put("column004", "Dept");
//        if(pdf){
        parameters.put("column005", "Sold/Price");
        parameters.put("column006", "Qty");
        parameters.put("column007", "Murray");
        parameters.put("column008", "Lehi");
        parameters.put("column009", "Orem");
        parameters.put("column010", "Total");
//        }else{
//            parameters.put("column005", "Catagory#");
//            parameters.put("column006", "Price");
//            parameters.put("column007", "Qty");
//            parameters.put("column008", "LastSold");
//            parameters.put("column009", "LastInvDt");
//            parameters.put("column010", "Total");
//        }

        Collection<Map> reportData = new Vector<Map>();
        Map<String, String> row = new HashMap<String, String>();

        List<Departments> allDepartments = DepartmentsUtil.getAllDepartments();
        HashMap deptHash = new HashMap<String, String>();
        for (Departments dept : allDepartments) {
            deptHash.put(dept.getDepartmentCode(), dept.getDepartmentName());
        }


        ArrayList<Departments> depts = (ArrayList)((HttpSession)parameters.get("session")).getAttribute("selDept");
        String dateOne = (String)parameters.get( "dateOne" );
        String dateTwo = (String)parameters.get( "dateTwo" );

        InventoryReport invUtil = new InventoryReport();
        List<ProductGto> invList = null;
        invList = invUtil.getInventorySoldReport(dateOne, dateTwo, depts);
        double teTotal = 0.0;
//        String grandTot = "";
        double grandTot = 0.0;
        double grandM = 0.0;
        double grandL = 0.0;
        double grandO = 0.0;
        String deptTotalsString = "";

        ArrayList<String> deptTotals = new ArrayList<String>();
        for(ProductGto gto: invList){
            String total = "Department: "+gto.getName()
                    +" Murray: "  +new java.text.DecimalFormat( "$0.00" ).format( gto.getmTotal())
                    +" Lehi:   "  +new java.text.DecimalFormat( "$0.00" ).format( gto.getlTotal())
                    +" Orem:   "  +new java.text.DecimalFormat( "$0.00" ).format( gto.getoTotal())
                    +" Total:  "  +new java.text.DecimalFormat( "$0.00" ).format( gto.getTotalValue());
            deptTotalsString += total;
            deptTotals.add(total);
            for(Product temp: gto.getProdList()){
                double tempTotal = 0;
                String tTotal = "";
                String cTotal = "";
                String lastSold = "";
                String lastInv = "";
                try{
                    tempTotal = (Double.valueOf(temp.getProductCost1()).doubleValue())*(Integer.parseInt(temp.getNumAvailable()));
                    teTotal += tempTotal;
                    tTotal = new java.text.DecimalFormat("$0.00").format(tempTotal);
                    tempTotal = Double.valueOf(temp.getProductCost1()).doubleValue();
                    cTotal =  new java.text.DecimalFormat("$0.00").format(tempTotal);
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
                row.put("value003", pdf&&prodName.length()>15?prodName.substring( 0,15 ):prodName);

                String name = "";
                String key = "";
                double ddd = 0.0;
                String md = "";
                String ld = "";
                String od = "";
                try{
                    key = temp.getCategory().charAt(0)+"";
                    name = (String)deptHash.get(key);
                    ddd = Double.parseDouble(temp.getProductCost4());
                    if(ddd > 0)
                        grandTot += ddd;

                    double ddm = Double.parseDouble(temp.getProductSupplier1());
                    double ddl = Double.parseDouble(temp.getProductSupplier2());
                    double ddo = Double.parseDouble(temp.getProductSupplier3());
                    md = new java.text.DecimalFormat( "$0.00" ).format( ddm);
                    ld = new java.text.DecimalFormat( "$0.00" ).format( ddl);
                    od = new java.text.DecimalFormat( "$0.00" ).format( ddo);

                    grandM += ddm;
                    grandL += ddl;
                    grandO += ddo;
                }catch(Exception e){
                    e.printStackTrace();
                }

                row.put("value004", name);
                row.put("value005", temp.getProductCost2()+"/"+temp.getProductCost1());
                row.put("value006", "("+temp.getNumAvailable()+")");
                row.put("value007", md+"");
                row.put("value008", ld+"");
                row.put("value009", od+"");
                row.put("value010", new java.text.DecimalFormat( "$0.00" ).format(ddd));
                reportData.add( row );
            }
        }
        try {
            Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse( dateOne );
            Date date2 = new SimpleDateFormat("MM/dd/yyyy").parse( dateTwo );
            parameters.put("fromDate", date1);
            parameters.put("toDate", date2);
        } catch ( ParseException e ) {
            e.printStackTrace();
        }
        parameters.put("title", "Inventory Report Title set in java class");
        parameters.put("mTotal", "Murray Total: "+new java.text.DecimalFormat( "$0.00" ).format(grandM));
        parameters.put("lTotal", "Lehi Total: "+new java.text.DecimalFormat( "$0.00" ).format(grandL));
        parameters.put("oTotal", "Orem Total: "+new java.text.DecimalFormat( "$0.00" ).format(grandO));
        parameters.put("gTotal", "Grand Total: "+new java.text.DecimalFormat( "$0.00" ).format(grandTot));
        for(int i = 0; i< deptTotals.size(); i++){
            String p = deptTotals.get(i);
            if(!StringUtils.isBlank(p)){
                parameters.put("d"+(i+1), p);
            }
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
