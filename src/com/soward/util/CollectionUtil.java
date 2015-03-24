/**
 * 
 */
package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.SimpleFormatter;

import org.apache.commons.lang.StringUtils;

import com.soward.db.MySQL;
import com.soward.object.Account;
import com.soward.object.CollectionRpt;
import com.soward.object.DBObj;
import com.soward.object.Invoice;

/**
 * @author Scott Soward Date: May 17, 2007
 * 
 */
public class CollectionUtil {

    public CollectionUtil() {
        // TODO Auto-generated constructor stub
    }

    public  ArrayList<CollectionRpt> getSchoolCollectionReport() {
        boolean school = true;
        return this.addAccountsToCollectionInvoice(school);
    }
    public  ArrayList<CollectionRpt> getNonSchoolCollectionReport() {
        boolean school = false;
        return this.addAccountsToCollectionInvoice(school);
    }
    public  ArrayList<CollectionRpt> getCollectionReport(String accType, String agingType) {
        boolean school = !StringUtils.isBlank( agingType )&& agingType.equals( "school" );
        return this.addAccountsToCollectionInvoice(school, accType);
    }

    // attach an account to each Holding bin invoice
    public ArrayList<CollectionRpt> addAccountsToCollectionInvoice(boolean isSchool) {
        return addAccountsToCollectionInvoice(isSchool, null);
    }
    
    public ArrayList<CollectionRpt> addAccountsToCollectionInvoice(boolean isSchool, String accType) {
        //total sums for each previous days grouping.
        double past30 = 0.0;
        double past60 = 0.0;
        double past90 = 0.0;
        double past   = 0.0;
        HashMap<String, ArrayList<Invoice>> emptyAccountInv = getCollectionInvoices(isSchool, accType);
        ArrayList<CollectionRpt> collInvoices = new ArrayList<CollectionRpt>();
        Connection con = null;
        MySQL sdb = new MySQL();
        // get all 21 type accounts that have outstanding invoices
        String embSql = "(select Invoices.accountNum from Invoices where invoiceChargeStatus='CHARGED')";

        String sql = "select accountNum, accountName, accountPassword, accountContact, "
            + "accountEmail, accountPhone1, accountPhone2, accountFax, accountStreet, "
            + "accountCity, accountState, accountPostalCode, accountCountry, accountType1, "
            + "accountType2, accountOpenDate, accountCloseDate, accountBalance from Accounts ";
        if(isSchool){
            sql += "where accountType1='21' and accountNum in " + embSql;            
        }
        else{
            sql += "where accountType1!='21' and accountNum in " + embSql;            
        }
        if(!StringUtils.isBlank( accType ) && !accType.equals("ALL")){
            sql += " and accountType1 = '"+accType+"'";
        }

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            System.out.println(sql);
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            int count = 0;
            Calendar endDate= Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            //          String tempnow = (formatter.format( (cal.getTime( ))));
            //          int now = Integer.parseInt( tempnow );
            while ( rset.next() ) {
                count++;
                Account cl = new Account();
                cl.setAccountNum( rset.getString( "accountNum" ) );
                cl.setAccountName( rset.getString( "accountName" ) );
                cl.setAccountPassword( rset.getString( "accountPassword" ) );
                cl.setAccountContact( rset.getString( "accountContact" ) );
                cl.setAccountEmail( rset.getString( "accountEmail" ) );
                cl.setAccountPhone1( rset.getString( "accountPhone1" ) );
                cl.setAccountPhone2( rset.getString( "accountPhone2" ) );
                cl.setAccountFax( rset.getString( "accountFax" ) );
                cl.setAccountStreet( rset.getString( "accountStreet" ) );
                cl.setAccountCity( rset.getString( "accountCity" ) );
                cl.setAccountState( rset.getString( "accountState" ) );
                cl.setAccountPostalCode( rset.getString( "accountPostalCode" ) );
                cl.setAccountCountry( rset.getString( "accountCountry" ) );
                cl.setAccountType1( rset.getString( "accountType1" ) );
                cl.setAccountType2( rset.getString( "accountType2" ) );
                cl.setAccountOpenDate( rset.getString( "accountOpenDate" ) );
                cl.setAccountCloseDate( rset.getString( "accountCloseDate" ) );
                cl.setAccountBalance( rset.getString( "accountBalance" ) );
                // add account to invoice
                // if accountNum is in the hash returned from the
                // getInvoicesMethod,
                // then for each Invoice in the arraylist (which is the hash
                // value)
                // add the account to that invoice
                if ( emptyAccountInv.containsKey( cl.getAccountNum() ) ) {
                    CollectionRpt coll = new CollectionRpt();
                    coll.setAcct( cl );
                    // get each outstanding invoice of the same account
                    double totalDue = 0.0;
                    ArrayList<Invoice> invList = emptyAccountInv.get( cl.getAccountNum() );
                    for ( Invoice tempInv : invList ) {
                        try{
                            totalDue +=  Double.parseDouble( tempInv.getInvoiceTotal() );
                        }catch(Exception e){
                            e.printStackTrace();
                            //do nothing, no total set.
                        }
                        //find out how outstanding the invoice is.
                        Calendar startDate = Calendar.getInstance();
                        startDate.setTime(formatter.parse(tempInv.getInvoiceUnixDate()));
                        int diff = this.getDaysDifference( startDate.getTime(), endDate.getTime() );
                        if((diff)<30){
                            coll.getColl_00_30().add( tempInv );
                            coll.setColl_00_30Sum( coll.getColl_00_30Sum()+Double.parseDouble( tempInv.getInvoiceTotal()) );
                            past30+=Double.parseDouble( tempInv.getInvoiceTotal());
                        }
                        else if((diff)<60){
                            coll.getColl_31_60().add( tempInv );
                            coll.setColl_31_60Sum( coll.getColl_31_60Sum()+Double.parseDouble(tempInv.getInvoiceTotal() ));
                            past60+=Double.parseDouble( tempInv.getInvoiceTotal());
                        }
                        else if((diff)<90){
                            coll.getColl_61_90().add( tempInv );
                            coll.setColl_61_90Sum( coll.getColl_61_90Sum()+Double.parseDouble( tempInv.getInvoiceTotal() ));
                            past90+=Double.parseDouble( tempInv.getInvoiceTotal());
                        }
                        else{
                            coll.getColl_91_up().add( tempInv );
                            coll.setColl_91_upSum( coll.getcoll_91_upSum()+Double.parseDouble( tempInv.getInvoiceTotal() ));
                            past+=Double.parseDouble( tempInv.getInvoiceTotal());
                        }

                    }
                    coll.setCollSum(totalDue);
                    collInvoices.add( coll );
                }
            }

            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return collInvoices;
    }

    // get all the invoices that are in the Holding bin.
    public static HashMap getCollectionInvoices(boolean isSchool, String accType) {
        HashMap<String, ArrayList<Invoice>> buildInvoices = new HashMap<String, ArrayList<Invoice>>();
        Connection con = null;
        MySQL sdb = new MySQL();
        // first get the transactions in the holding bin.
        String embSql = "";
        String sqlAccType = "";
        if(!StringUtils.isBlank( accType ) && !accType.equals("ALL")){
            sqlAccType += " and accountType1 = '"+accType+"'";
        }
        
        if(isSchool){
            embSql = "(select accountNum from Accounts where accountType1='21' "+sqlAccType+")";
        }
        else{
            embSql = "(select accountNum from Accounts where accountType1!='21' "+sqlAccType+")";
        }
        // char(10)== how long of a string to return,
        // 110 is a format mm-dd-ccyy for sybase:
        // http://www.comsci.us/sybase/date_fmt.html
        // convert(char(10),convert(datetime,invoiceDate),110)
        String sql = "select  invoiceNum, accountNum, invoiceDate as unixDte, invoiceDate, locationNum, username, "
            + "invoiceTotal, invoiceTax, invoiceShipTotal, invoicePaid, paymentMethod1, "
            + "paymentMethod2, invoicePaid1, invoicePaid2, invoiceReceivedBy, invoiceContactNum, "
            + "invoiceReferenceNum, invoiceChargeStatus, invoiceChargeDate, "
            + "invoiceChargePaymentMethod, invoiceDiscount, (invoiceTotal - invoicePaid) as remainingSum from Invoices where "
            + "invoiceChargeStatus='CHARGED' and invoiceTotal>0.0 and accountNum in " + embSql + " order by accountNum";

        try {
            con = sdb.getConn();
//            System.out.println("007 "+sql);
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            int count = 0;
            while ( rset.next() ) {
                count++;
                Invoice inv = new Invoice();
                inv.setInvoiceNum( rset.getString( "invoiceNum" ) );
                inv.setAccountNum( rset.getString( "accountNum" ) );
                inv.setInvoiceDate( rset.getString( "invoiceDate" ) );
                inv.setLocationNum( rset.getString( "locationNum" ) );
                inv.setUsername2( rset.getString( "username" ) );
                //                inv.setInvoiceTotal( rset.getString( "invoiceTotal" ) );
                inv.setInvoiceTotal( rset.getString( "remainingSum" ) );
                inv.setInvoiceTax( rset.getString( "invoiceTax" ) );
                inv.setInvoiceShipTotal( rset.getString( "invoiceShipTotal" ) );
                inv.setInvoicePaid( rset.getString( "invoicePaid" ) );
                inv.setPaymentMethod1( rset.getString( "paymentMethod1" ) );
                inv.setPaymentMethod2( rset.getString( "paymentMethod2" ) );
                inv.setInvoicePaid1( rset.getString( "invoicePaid1" ) );
                inv.setInvoicePaid2( rset.getString( "invoicePaid2" ) );
                inv.setInvoiceReceivedBy( rset.getString( "invoiceReceivedBy" ) );
                inv.setInvoiceContactNum( rset.getString( "invoiceContactNum" ) );
                inv.setInvoiceReferenceNum( rset.getString( "invoiceReferenceNum" ) );
                inv.setInvoiceChargeStatus( rset.getString( "invoiceChargeStatus" ) );
                inv.setInvoiceChargeDate( rset.getString( "invoiceChargeDate" ) );
                inv.setInvoiceChargePaymentMethod( rset.getString( "invoiceChargePaymentMethod" ) );
                inv.setInvoiceDiscount( rset.getString( "invoiceDiscount" ) );
                inv.setInvoiceUnixDate( rset.getString( "unixDte" ) );
                // check to see that the hash doesnt already have an entry with this
                // accountNum, if so add this inv to the arraylist, if not, create new
                // entry to hash with key=accountnum and value= new arraylist with
                // inv added to arraylist
                if ( buildInvoices.containsKey( inv.getAccountNum() ) ) {
                    buildInvoices.get( inv.getAccountNum() ).add( inv );
                } else {
                    ArrayList<Invoice> temp = new ArrayList<Invoice>();
                    temp.add( inv );
                    buildInvoices.put( inv.getAccountNum(), temp );
                }
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return buildInvoices;
    }

    // remove dashes from date
    private static String removeDashes( String string ) {
        // date in format: 04-03-2007
        string = string.replaceAll( "-", "" );
        return string;
    }
    public int getDaysDifference(Date startDate, Date endDate)
    {
        long diff = endDate.getTime() - startDate.getTime();

        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTime(startDate);
        startDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startDateCalendar.set(Calendar.MINUTE, 0);
        startDateCalendar.set(Calendar.SECOND, 0);
        startDateCalendar.set(Calendar.MILLISECOND, 0);         

        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.setTime(endDate);
        endDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endDateCalendar.set(Calendar.MINUTE, 0);
        endDateCalendar.set(Calendar.SECOND, 0);
        endDateCalendar.set(Calendar.MILLISECOND, 0);

        int offset = endDateCalendar.get(Calendar.DST_OFFSET) - startDateCalendar.get(Calendar.DST_OFFSET);

        diff += offset;

        int days = (int) (diff/(1000*60*60*24));

        return days;
    }

    public static void main( String[] args ) {
        try {
            Calendar endDate= Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Calendar startDate = Calendar.getInstance();
            String temp = "20070811";
            temp = "20070928";
            startDate.setTime(formatter.parse(temp));
            System.out.println("startDate=" + startDate.getTime());
            System.out.println("endDate=" + endDate.getTime());


            CollectionUtil cu = new CollectionUtil();
            System.out.println("==================");
            System.out.println(cu.getDaysDifference( startDate.getTime(), endDate.getTime() ));
            cu.addAccountsToCollectionInvoice(true);

        } catch ( ParseException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
