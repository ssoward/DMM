package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.soward.db.MySQL;
import com.soward.enums.LocationsDBName;
import com.soward.object.Departments;
import com.soward.object.Descriptions;
import com.soward.object.Events;
import com.soward.object.Product;
import com.soward.object.Transaction;

public class ProductSoldReport {
    public static void main(String args[]){
        //      ProductSoldReport.getProdHash( "2008-12-22", "2008-12-23", "1", "w", "I" );
        //      ProductSoldReport psr = new ProductSoldReport();
        //      System.out.println(psr.getInventorySoldReport(  "2008-12-15", "2008-12-16", null, null, null).size());


        boolean testOne = true;
        boolean testTwo = false;
        if(testOne){
            ProductSoldReport ir = new ProductSoldReport();
            HashMap<String, ArrayList<HashMap<String, String>>> prodList = ir.getQtySales( "03-13-2010 00:00", "03-20-2010 00:00", "MURRAY", "1", "1", null);
            Set set = prodList.keySet();
            Iterator iter = set.iterator();
            while (iter.hasNext()){
                ArrayList<HashMap<String, String>> al = prodList.get( iter.next() );
                for(int i = 0; i<al.size(); i++){
                    System.out.println(al.get( i ));
                }
            }
        }
        //test getting description list and then specific desc based on a particular deptment
        if(testTwo){
            List<Descriptions> allDescriptions = DescriptionsUtil.getAll2CodeDescriptions("9");
            HashMap descHash = new HashMap<String, String>();
            for(Descriptions desc1: allDescriptions){
                String t1 = desc1.getDescriptionCode();
                String t2 = desc1.getDescriptionName();
                System.out.println("code: "+t1+ " name: "+t2);
                descHash.put(desc1.getDescriptionCode(), desc1.getDescriptionName());
            }
            System.out.println("Desc: "+ProductUtils.getCatElement( descHash, "900002", 5 ));
        }


    }
    //  set rowcount 20 select * from Products where lastInvDate >'2007-01-01' and numAvailable>0
    public List<Transaction> getInventorySoldReport( String startDate, String endDate, String department, String descriptions, String events1) {
        try {
            department = department.trim();
            descriptions = descriptions.trim();
            events1 = events1.trim();
        } catch ( NullPointerException e1 ) {

        }
        List<Transaction> prodList = new ArrayList<Transaction>();
        Connection con = null;
        MySQL sdb = new MySQL();
        boolean checkDates = Utils.validateDateRange(startDate, endDate, 7, "yyyy-MM-dd");
        if(checkDates){
            String sql = "select count(*) as soldCount, tt.productNum from Transactions tt where tt.transDate > '" + startDate + 
            "' and tt.transDate< '"+endDate+"' ";
            if(department!=null&&department.length()>0||descriptions!=null&&descriptions.length()>0||events1!=null&&events1.length()>0){
                sql += "and tt.productNum in (select prod.productNum from Products prod where";
                String and = "";
                if ( events1!= null && events1.length() > 0 ) {
                    sql += "  prod.category like '%" + events1 + "%'";
                    and  = and.length()<1?" and ":and;
                }
                if (department != null &&department.length() > 0 ) {
                    sql += and +" prod.category like '" + department + "%'";
                    and  = and.length()<1?" and ":and;
                }
                if ( descriptions!= null &&descriptions.length() > 0 ) {
                    sql += and+" prod.category like '%" + descriptions + "'";
                    and  = and.length()<1?" and ":and;
                }
                sql += "\n and prod.productNum in (select t.productNum from Transactions t where t.transDate > '" + startDate + 
                "' and t.transDate< '"+endDate+"')) ";   
            }
            sql += "group by productNum order by soldCount desc ";
            try {
                con = sdb.getConn();
                PreparedStatement pstmt = null;
                pstmt = con.prepareStatement( sql );
                ResultSet rset = pstmt.executeQuery();
                // only get the next 20 entries
                while ( rset.next() ) {
                    Transaction prod = new Transaction();
                    prod.setProductNum( rset.getString( "productNum" ) );
                    prod.setProductQty( rset.getString( "soldCount" ) );
                    prodList.add( prod );
                }
                pstmt.close();
                con.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            return prodList;
        }
        else{
            try {
                con.close();
            } catch ( Exception e ) {
                //never opened con
                //e.printStackTrace();
            }
            return null;
        }
    } 


    /**
     * 
     * @param startDate
     * @param endDate
     * @param accountNumLocation
     * @param department
     * @param descriptions
     * @param events1
     * @return HashMap<String, ArrayList<HashMap<String, String>>>
     */

    public HashMap<String, ArrayList<HashMap<String, String>>> getQtySales( String startDate, String endDate, 
            String accountNumLocation, String department, String descriptions, String events1 ) {
        HashMap<String, ArrayList<HashMap<String, String>>> soldList = new HashMap<String, ArrayList<HashMap<String, String>>>();
        //hashmap supplierName
        // -- ArrayList 
        // -- HashMap<Keys: prodNum, countSold, description, department>:
        // -- countSold, prodNum
        // -- countSold, prodNum
        // -- countSold, prodNum
        // -- ...
        try {
            department = department.trim();
            descriptions = descriptions.trim();
            events1 = events1.trim();
        } catch ( NullPointerException e1 ) {

        }
        // boolean checkDates = Utils.validateDateRange(startDate, endDate, 7, "yyyy-MM-dd");
        String sqlTwo = "";
        if(department!=null&&department.length()>0||descriptions!=null&&descriptions.length()>0||events1!=null&&events1.length()>0){
            sqlTwo += "and tt.productNum = prod.productNum ";
            if ( events1!= null && events1.length() > 0 ) {
                sqlTwo += " and prod.category like '%" + events1 + "%'";
            }
            if (department != null &&department.length() > 0 ) {
                sqlTwo += " and prod.category like '" + department + "%'";
            }
            if ( descriptions!= null &&descriptions.length() > 0 ) {
                sqlTwo += " and prod.category like '%" + descriptions + "'";
            }
        }
        Connection con = null;
        MySQL sdb = new MySQL();
        if(startDate==null||endDate==null||accountNumLocation==null){
            return null;
        }

        List<Events> allEvents = EventsUtil.getAllEvents();
        HashMap eventsHash = new HashMap<String, String>();
        HashMap deptHash = new HashMap<String, String>();


        HashMap<String, HashMap<String, String>> allDescpts = new HashMap<String, HashMap<String, String>>();
        List<Departments> allDepartments = DepartmentsUtil.getAllDepartments();
        for(Events event: allEvents){
            eventsHash.put(event.getEventCode(), event.getEventName());
        }
        for(Departments dept: allDepartments){
            deptHash.put(dept.getDepartmentCode(), dept.getDepartmentName());
        }

        String productLocation = "Products";
        String sql = "";
        String sqlLocationWhere = "";
        //set murray variables
        if(accountNumLocation.equalsIgnoreCase( "MURRAY" )){
            sqlLocationWhere= "and location = 'MURRAY'\n";
        }
        //set lehi variables
        if(accountNumLocation.equalsIgnoreCase( "LEHI" )){
            sqlLocationWhere= "and location = 'LEHI'\n";
        }
        //set lehi variables
        if(accountNumLocation.equalsIgnoreCase( "OREM" )){
            sqlLocationWhere= "and location = 'OREM'\n";
        }
        //set online variables
        if(accountNumLocation.equalsIgnoreCase( "ONINE" )){
            sqlLocationWhere= "and location = 'ONLINE'\n";
        }
        //set PO variables
        if(accountNumLocation.equalsIgnoreCase( "104" )){
        }
        boolean allLoc = false;
        if(accountNumLocation.equalsIgnoreCase( "ALL" )){
            allLoc = true;
        }
        
        
        sql = "select sum(tt.productQty) totals, location, prod.productCost1 price, prod.productName prodName,\n" +
        "prod.category cat, supplierName, prod.productSKU sku, prod.productNum prodNum\n" +
        "from Transactions tt, Products prod, Suppliers, InvoiceLocation invLoc\n" +
        "where tt.invoiceNum = invLoc.invoiceNum\n" +
        "and invDate>'" + startDate +"' \n"+ 
        "and invDate< '"+endDate+"' \n"+
        (!allLoc?sqlLocationWhere:"") +
        "and tt.productNum = prod.productNum \n" +
        "and productSupplier1 = Suppliers.supplierNum \n" +
        "and transDate>'" + startDate +"' \n"+ 
        "and transDate< '"+endDate+"' \n"+sqlTwo+"\n" +
        //"and tt.productQty > 0 \n"+
        "group by prod.productNum order by totals desc\n";

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            System.out.println("getQtySales: "+sql);
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            int count = 0;
            ProductUtils pu = new ProductUtils();
            while ( rset.next() ) {
                HashMap<String, String> row = new HashMap<String, String>(); 
                String prodCount = ( rset.getString( "totals" ) );
                String supplierName = ( rset.getString( "supplierName" ) );
                String prodName = ( rset.getString( "prodName" ) );
                String sku = ( rset.getString( "sku" ) );
                String cat = ( rset.getString( "cat" ) );
                String price = ( rset.getString( "price" ) );
                String prodNum = ( rset.getString( "prodNum" ) );
                row.put( "prodCount", prodCount );
                row.put( "prodName", prodName );
                row.put( "prodNum", prodNum );
                row.put( "sku", sku );
                row.put( "supplierName", supplierName );
                String dept = pu.getCatElement( deptHash, cat, 0 );
                String desc = "";
                row.put( "dept", dept );
                row.put( "cat", cat );
                row.put( "price", price );
                //so that we dont have to search every time for the to 
                //get the description list that cooresponds to this
                //deptartment, we can store the dept list for resuse
                if(!allDescpts.containsKey( dept )){
                    try {
                        String sttt = cat.charAt( 0 )+"";
                        List<Descriptions> allDescriptions = DescriptionsUtil.getAll2CodeDescriptions(sttt);
                        HashMap descHash = new HashMap<String, String>();
                        for(Descriptions desc1: allDescriptions){
                            descHash.put(desc1.getDescriptionCode(), desc1.getDescriptionName());
                        }
                        allDescpts.put( dept, descHash );
                    } catch ( Exception e ) {
                        //e.printStackTrace();
                    }
                }
                desc = pu.getCatElement( allDescpts.get( dept ), cat, 5 );
                desc = desc!=null?desc:"";
                row.put( "desc", desc );
                //row.add( supplierName );
                if(soldList.get( prodNum )!=null){
                    soldList.get( prodNum ).add( row );
                }else{
                    ArrayList al = new ArrayList();
                    al.add( row );
                    soldList.put( prodNum, al );
                }
                count++;
            }
            //System.out.println(count);
            pstmt.close();
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return soldList;
    }

}
