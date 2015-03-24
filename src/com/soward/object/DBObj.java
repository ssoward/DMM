/**
 *
 */
package com.soward.object;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.soward.db.MySQL;
import com.soward.util.UpdateAccount;

/**
 * @author amorvivir
 *
 */
public class DBObj {

    public String count;

    public ArrayList<String> row;

    public DBObj() {
        this.count = "";
        this.row = new ArrayList<String>();
    }

    public ArrayList<String> getRow() {
        return this.row;
    }

    public void setRow( ArrayList<String> aa ) {
        this.row = aa;
    }

    public String getCount() {
        return this.count;
    }

    // get all the tex exmempt accounts.
    public ArrayList<DBObj> getTEAccounts( int currCount ) {
        ArrayList<DBObj> dbobj = new ArrayList<DBObj>();
        Connection con = null;
        MySQL sdb = new MySQL();
        boolean resetContinue = true;
        String sql = "select accountNum, accountName, accountContact, accountEmail, "
                + "accountBalance from Accounts where accountType1='21'";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            for ( int i = 0; i < currCount; i++ ) {
                if ( !rset.next() ) {
                    resetContinue = false;
                }
            }
            // only get the next 20 entries
            int twenty = 20;
            while ( rset.next() && resetContinue ) {
                if ( twenty > 0 ) {
                    twenty--;
                    // get the location of the print, if we are at the end exit.
                    DBObj tempRow = new DBObj();
                    int tempCount = 0;
                    try {
                        String a = "";
                        for ( int notNull = 1; notNull < 100; notNull++ ) {
                            try {
                                a = rset.getString( notNull );
                                tempCount++;
                                tempRow.row.add( a );
                            } catch ( Exception e ) {
                                notNull = 120;
                                break;
                            }
                        }
                        tempRow.count = tempCount + "";
                        dbobj.add( tempRow );
                    } catch ( Exception e ) {
                        // set count now that we have gone to end of the row.
                        tempRow.count = tempCount + "";
                        dbobj.add( tempRow );
                    }
                }
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            System.out.println( "DayMuarryMusic.DBObj-->runQuery: " );
            e.printStackTrace();
        }

        return dbobj;
    }

    // need to write the invoice query and figure out how to print it to the
    // screen and have the columns names correspond with the values
    // select * from Invoices where accountNum=1053 and invoiceDate>'2007-01-01'
    public InvBundle getInvoices( ArrayList<String> columnNames, String pid, String dateOne, String dateTwo,
                                  String isPaid ) {
        double invoiceTotalAmt = 0.0;
        double invoiceTaxAmt = 0.0;
        double invoiceShipTotalAmt = 0.0;
        double invoicePaidAmt = 0.0;
        double invoicePaid1Amt = 0.0;
        double invoicePaid2Amt = 0.0;
        double invoiceDiscountAmt = 0.0;
        ArrayList<String> dbColNames = new ArrayList<String>();
        InvBundle invBun = new InvBundle();
        ArrayList<Invoice> invoiceCollection = new ArrayList<Invoice>();
        String sql = "select ";
        boolean isInvoiceNum = false;
        boolean isAccountNum = false;
        boolean isInvoiceDate = false;
        boolean isLocationNum = false;
        boolean isUsername2 = false;
        boolean isInvoiceTotal = false;
        boolean isInvoiceTax = false;
        boolean isInvoiceShipTotal = false;
        boolean isInvoicePaid = false;
        boolean isPaymentMethod1 = false;
        boolean isPaymentMethod2 = false;
        boolean isInvoicePaid1 = false;
        boolean isInvoicePaid2 = false;
        boolean isInvoiceReceivedBy = false;
        boolean isInvoiceContactNum = false;
        boolean isInvoiceReferenceNum = false;
        boolean isInvoiceChargeStatus = false;
        boolean isInvoiceChargeDate = false;
        boolean isInvoiceChargePaymentMethod = false;
        boolean isInvoiceDiscount = false;
        boolean ispayStatus = false;
        boolean isdateOne = false;
        boolean isdateTwo = false;
        // get all the columnNames
        if ( columnNames.contains( "invoiceNum" ) ) {
            dbColNames.add( "invoiceNum" );
            sql += "invoiceNum                ,";
            isInvoiceNum = true;
        }
        if ( columnNames.contains( "accountNum" ) ) {
            dbColNames.add( "accountNum" );
            sql += "accountNum                ,";
            isAccountNum = true;
        }
        if ( columnNames.contains( "invoiceDate" ) ) {
            dbColNames.add( "invoiceDate" );
            sql += "invoiceDate               ,";
            isInvoiceDate = true;
        }
        if ( columnNames.contains( "locationNum" ) ) {
            dbColNames.add( "locationNum" );
            sql += "locationNum               ,";
            isLocationNum = true;
        }
        if ( columnNames.contains( "userName" ) ) {
            dbColNames.add( "username" );
            sql += "username                  ,";
            isUsername2 = true;
        }
        if ( columnNames.contains( "invoiceTotal" ) ) {
            dbColNames.add( "invoiceTotal" );
            sql += "invoiceTotal              ,";
            isInvoiceTotal = true;
        }
        if ( columnNames.contains( "invoiceTax" ) ) {
            dbColNames.add( "invoiceTax" );
            sql += "invoiceTax                ,";
            isInvoiceTax = true;
        }
        if ( columnNames.contains( "invoiceShipTotal" ) ) {
            dbColNames.add( "invoiceShipTotal" );
            sql += "invoiceShipTotal          ,";
            isInvoiceShipTotal = true;
        }
        if ( columnNames.contains( "invoicePaid" ) ) {
            dbColNames.add( "invoicePaid" );
            sql += "invoicePaid               ,";
            isInvoicePaid = true;
        }
        if ( columnNames.contains( "paymentMethod1" ) ) {
            dbColNames.add( "paymentMethod1" );
            sql += "paymentMethod1            ,";
            isPaymentMethod1 = true;
        }
        if ( columnNames.contains( "paymentMethod2" ) ) {
            dbColNames.add( "paymentMethod2" );
            sql += "paymentMethod2            ,";
            isPaymentMethod2 = true;
        }
        if ( columnNames.contains( "invoicePaid1" ) ) {
            dbColNames.add( "invoicePaid1" );
            sql += "invoicePaid1              ,";
            isInvoicePaid1 = true;
        }
        if ( columnNames.contains( "invoicePaid2" ) ) {
            dbColNames.add( "invoicePaid2" );
            sql += "invoicePaid2              ,";
            isInvoicePaid2 = true;
        }
        if ( columnNames.contains( "invoiceReceivedBy" ) ) {
            dbColNames.add( "invoiceReceivedBy" );
            sql += "invoiceReceivedBy         ,";
            isInvoiceReceivedBy = true;
        }
        if ( columnNames.contains( "invoiceContactNum" ) ) {
            dbColNames.add( "invoiceContactNum" );
            sql += "invoiceContactNum         ,";
            isInvoiceContactNum = true;
        }
        if ( columnNames.contains( "invoiceReferenceNum" ) ) {
            dbColNames.add( "invoiceReferenceNum" );
            sql += "invoiceReferenceNum       ,";
            isInvoiceReferenceNum = true;
        }
        if ( columnNames.contains( "invoiceChargeStatus" ) ) {
            dbColNames.add( "invoiceChargeStatus" );
            sql += "invoiceChargeStatus       ,";
            isInvoiceChargeStatus = true;
        }
        if ( columnNames.contains( "invoiceChargeDate" ) ) {
            dbColNames.add( "invoiceChargeDate" );
            sql += "invoiceChargeDate         ,";
            isInvoiceChargeDate = true;
        }
        if ( columnNames.contains( "invoiceChargePaymentMethod" ) ) {
            dbColNames.add( "invoiceChargePaymentMethod" );
            sql += "invoiceChargePaymentMethod,";
            isInvoiceChargePaymentMethod = true;
        }
        if ( columnNames.contains( "invoiceDiscount" ) ) {
            dbColNames.add( "invoiceDiscount" );
            sql += "invoiceDiscount           ,";
            isInvoiceDiscount = true;
        }

        int pidNum = 0;
        if ( pid != null && pid.length() > 1 ) {
            try {
                pidNum = Integer.parseInt( pid );
            } catch ( Exception e ) {
                System.out.println( "DayMM-->DBObj.getAccount: bad pid " + e.toString() );
                // bad pid exit.
            }
            Connection con = null;
            MySQL sdb = new MySQL();
            // select * from Invoices where accountNum=1053 and
            // invoiceDate>'2007-01-01'
            // remove the last comma if the column list was lest than 5
            if ( sql.charAt( sql.length() - 1 ) == ',' ) {
                sql = sql.substring( 0, sql.length() - 1 );
            }
            sql += " from Invoices where accountNum=" + pidNum;
            // building sql string restrict the where to paid or charged.
            if ( isPaid != null && isPaid.length() > 1 ) {
                if ( isPaid.equalsIgnoreCase( "paid" ) ) {
                    sql += " and invoiceChargeStatus='PAID'";
                } else if ( isPaid.equalsIgnoreCase( "charged" ) ) {
                    sql += " and invoiceChargeStatus='CHARGED'";
                }
            }
            // building sql string restrict the where to paid or charged.
            if ( dateOne != null && dateOne.length() > 1 ) {
                sql += " and invoiceDate>'" + dateOne + "'";
            }
            // building sql string restrict the where to paid or charged.
            if ( dateTwo != null && dateTwo.length() > 1 ) {
                sql += " and invoiceDate<dateadd(hh, 24, '" + dateTwo + "')";
            }
            //System.out.println(sql);
            try {
                con = sdb.getConn();
                PreparedStatement pstmt = null;
                pstmt = con.prepareStatement( sql );
                //System.out.println("DBOBj getInvoices(): "+sql);
                ResultSet rset = pstmt.executeQuery();
                while ( rset.next() ) {
                    Invoice tempRow = new Invoice();

                    if ( isInvoiceNum ) {
                        String val = "";
                        val = rset.getString( "invoiceNum" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoiceNum( val );
                    }
                    if ( isAccountNum ) {
                        String val = "";
                        val = rset.getString( "accountNum" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setAccountNum( val );
                    }
                    if ( isInvoiceDate ) {
                        String val = "";
                        val = rset.getString( "invoiceDate" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoiceDate( val );
                    }
                    if ( isLocationNum ) {
                        String val = "";
                        val = rset.getString( "locationNum" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setLocationNum( val );
                    }
                    if ( isUsername2 ) {
                        String val = "";
                        val = rset.getString( "username" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setUsername2( val );
                    }
                    if ( isInvoiceTotal ) {
                        String val = "";
                        val = rset.getString( "invoiceTotal" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoiceTotal( val );
                        try {
                            double d = Double.valueOf( val ).doubleValue();
                            invoiceTotalAmt += d;
                        } catch ( Exception e ) {
                            System.out.println( val + " " + e );
                        }
                    }
                    if ( isInvoiceTax ) {
                        String val = "";
                        val = rset.getString( "invoiceTax" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoiceTax( val );
                    }
                    if ( isInvoiceShipTotal ) {
                        String val = "";
                        val = rset.getString( "invoiceShipTotal" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoiceShipTotal( val );
                        try {
                            double d = Double.valueOf( val ).doubleValue();
                            invoiceShipTotalAmt += d;
                        } catch ( Exception e ) {
                            System.out.println( val + " " + e );
                        }
                    }
                    if ( isInvoicePaid ) {
                        String val = "";
                        val = rset.getString( "invoicePaid" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoicePaid( val );
                        try {
                            double d = Double.valueOf( val ).doubleValue();
                            invoicePaidAmt += d;
                        } catch ( Exception e ) {
                            System.out.println( val + " " + e );
                        }
                    }
                    if ( isPaymentMethod1 ) {
                        String val = "";
                        val = rset.getString( "paymentMethod1" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setPaymentMethod1( val );
                    }
                    if ( isPaymentMethod2 ) {
                        String val = "";
                        val = rset.getString( "paymentMethod2" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setPaymentMethod2( val );
                    }
                    if ( isInvoicePaid1 ) {
                        String val = "";
                        val = rset.getString( "invoicePaid1" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoicePaid1( val );
                        try {
                            double d = Double.valueOf( val ).doubleValue();
                            invoicePaid1Amt += d;
                        } catch ( Exception e ) {
                            System.out.println( val + " " + e );
                        }
                    }
                    if ( isInvoicePaid2 ) {
                        String val = "";
                        val = rset.getString( "invoicePaid2" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoicePaid2( val );
                        try {
                            double d = Double.valueOf( val ).doubleValue();
                            invoicePaid2Amt += d;
                        } catch ( Exception e ) {
                            System.out.println( val + " " + e );
                        }
                    }
                    if ( isInvoiceReceivedBy ) {
                        String val = "";
                        val = rset.getString( "invoiceReceivedBy" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoiceReceivedBy( val );
                    }
                    if ( isInvoiceContactNum ) {
                        String val = "";
                        val = rset.getString( "invoiceContactNum" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoiceContactNum( val );
                    }
                    if ( isInvoiceReferenceNum ) {
                        String val = "";
                        val = rset.getString( "invoiceReferenceNum" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoiceReferenceNum( val );
                    }
                    if ( isInvoiceChargeStatus ) {
                        String val = "";
                        val = rset.getString( "invoiceChargeStatus" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoiceChargeStatus( val );
                    }
                    if ( isInvoiceChargeDate ) {
                        String val = "";
                        val = rset.getString( "invoiceChargeDate" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoiceChargeDate( val );
                    }
                    if ( isInvoiceChargePaymentMethod ) {
                        String val = "";
                        val = rset.getString( "invoiceChargePaymentMethod" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoiceChargePaymentMethod( val );
                    }
                    if ( isInvoiceDiscount ) {
                        String val = "";
                        val = rset.getString( "invoiceDiscount" );
                        if ( val == null || val.equals( "" ) ) {
                            val = "--";
                        }
                        tempRow.setInvoiceDiscount( val );
                        try {
                            double d = Double.valueOf( val ).doubleValue();
                            invoiceDiscountAmt += d;
                        } catch ( Exception e ) {
                        }
                    }

                    invoiceCollection.add( tempRow );
                }
                invBun.setInvCollection( invoiceCollection );
                invBun.setColNames( dbColNames );
                // ArrayList<String>amtList = new ArrayList<String>();
                HashMap<String, String> amtList = new HashMap<String, String>();
                amtList.put( "invoiceTotal", Double.toString( invoiceTotalAmt ) );
                amtList.put( "invoiceTax", Double.toString( invoiceTaxAmt ) );
                amtList.put( "invoiceShipTotal", Double.toString( invoiceShipTotalAmt ) );
                amtList.put( "invoicePaid", Double.toString( invoicePaidAmt ) );
                amtList.put( "invoicePaid1", Double.toString( invoicePaid1Amt ) );
                amtList.put( "invoicePaid2", Double.toString( invoicePaid2Amt ) );
                amtList.put( "invoiceDiscount", Double.toString( invoiceDiscountAmt ) );

                // amtList.add(Double.toString(invoiceTotalAmt ));
                // amtList.add(Double.toString(invoiceTaxAmt ));
                // amtList.add(Double.toString(invoiceShipTotalAmt ));
                // amtList.add(Double.toString(invoicePaidAmt ));
                // amtList.add(Double.toString(invoicePaid1Amt ));
                // amtList.add(Double.toString(invoicePaid2Amt ));
                // amtList.add(Double.toString(invoiceDiscountAmt ));

                invBun.setColTol( amtList );
                rset.close();
                con.close();
            } catch ( Exception e ) {
                System.out.println( "DayMuarryMusic.DBObj-->getInvoices: " );
                e.printStackTrace();
            }
        }

        return invBun;

    }

    // get accounts.
    public ArrayList<DBObj> getAccount( String pid ) {
        ArrayList<DBObj> dbobj = new ArrayList<DBObj>();
        int pidNum = 0;
        if ( pid != null && pid.length() > 1 ) {
            try {
                pidNum = Integer.parseInt( pid );
            } catch ( Exception e ) {
                System.out.println( "DayMM-->DBObj.getAccount: bad pid " + e.toString() );
                // bad pid exit.
            }
            Connection con = null;
            MySQL sdb = new MySQL();
            boolean resetContinue = true;
            String sql = "select accountNum,accountName,accountPassword,"
                    + "accountContact,accountEmail,accountPhone1,accountPhone2,"
                    + "accountFax,accountStreet,accountCity,accountState,"
                    + "accountPostalCode,accountCountry,accountType1,accountType2,"
                    + "accountOpenDate,accountCloseDate,accountBalance from Accounts where accountNum=" + pidNum + "";
            try {
                con = sdb.getConn();
                PreparedStatement pstmt = null;
                pstmt = con.prepareStatement( sql );
                ResultSet rset = pstmt.executeQuery();
                // only get the next 20 entries
                while ( rset.next() && resetContinue ) {
                    // get the location of the print, if we are at the end exit.
                    DBObj tempRow = new DBObj();
                    int tempCount = 0;
                    try {
                        String a = "";
                        for ( int notNull = 1; notNull < 100; notNull++ ) {
                            try {
                                a = rset.getString( notNull );
                                tempCount++;
                                tempRow.row.add( a );
                            } catch ( Exception e ) {
                                notNull = 120;
                                break;
                            }
                        }
                        tempRow.count = tempCount + "";
                        dbobj.add( tempRow );
                    } catch ( Exception e ) {
                        // set count now that we have gone to end of the row.
                        tempRow.count = tempCount + "";
                        dbobj.add( tempRow );
                    }
                }
                rset.close();
                con.close();
            } catch ( Exception e ) {
                System.out.println( "DayMuarryMusic.DBObj-->runQuery: " );
                e.printStackTrace();
            }
        }
        return dbobj;
    }

    // get generic select result set.
    public ArrayList<DBObj> getAccountList( String sql, String accType, boolean balanceSort, int rowCount ) {
        ArrayList<DBObj> dbobj = new ArrayList<DBObj>();
        Connection con = null;
        MySQL sdb = new MySQL();
        boolean resetContinue = true;
        String ssql = "";
        String master = "";


        if(rowCount>0){
           // ssql = "set rowcount "+rowCount;
        }
        String tempQuery = " select accountNum, accountName, accountContact, accountEmail, accountBalance, accountType1," +
                "accountStreet, accountCity, accountState, accountPostalCode from Accounts ";
        String sql1 = "";
        String sql2 = "";
        //name or number is the default selection, if it has not been changed: set sql to null
        if(!StringUtils.isBlank(sql)&&!sql.equals("Name or Number")){
            sql1 =  "where lower(accountName) like '%"+(sql.trim()).toLowerCase()+"%'";
            sql2 =  "where accountNum like '"+sql.trim()+"'";
        }else{
            sql = null;
        }
        if(!StringUtils.isBlank( accType )){
            String where = "";
            where += StringUtils.isBlank( sql )?" where ": " and ";
            where += " accountType1 = '"+accType+"'";
            sql1 += where;
            sql2 += where;
        }
        master = ssql+ tempQuery + sql1 +" union "+ tempQuery + sql2;
        master += " order by "+(balanceSort?"accountBalance desc ":" accountName ");
        System.out.println( master );
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( master );
            ResultSet rset = pstmt.executeQuery();

            while ( rset.next() && resetContinue ) {
                //
                // get the location of the print, if we are at the end exit.
                DBObj tempRow = new DBObj();
                int tempCount = 0;
                try {
                    String a = "";
                    for ( int notNull = 1; notNull < 100; notNull++ ) {
                        try {
                            a = rset.getString( notNull );
                            tempCount++;
                            tempRow.row.add( a );
                        } catch ( Exception e ) {
                            notNull = 120;
                            break;
                        }
                    }
                    tempRow.count = tempCount + "";
                    dbobj.add( tempRow );
                } catch ( Exception e ) {
                    // set count now that we have gone to end of the row.
                    tempRow.count = tempCount + "";
                    dbobj.add( tempRow );
                }
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return dbobj;
    }

    public ArrayList<DBObj> runQuery( String sql, String type ) {
        ArrayList<DBObj> dbobj = new ArrayList<DBObj>();
        Connection con = null;
        MySQL sdb = new MySQL();
        ResultSet rset = null;
        int rows = 10;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            Statement stmt = null;
            // if ( rowCount != null ) {
            // try {
            // int tts = Integer.parseInt( rowCount );
            // rows = tts;
            // } catch ( Exception e ) {
            // // no row count entered, use default of 10
            // }
            // }
            // pstmt.setMaxRows( rows );
            if ( type.equals( "update" ) ) {
                stmt = con.createStatement();
                stmt.executeUpdate( sql );
                stmt.close();
            } else {
                pstmt = con.prepareStatement( sql );
                rset = pstmt.executeQuery();
                while ( rset.next() ) {
                    DBObj tempRow = new DBObj();
                    int tempCount = 0;
                    try {
                        String a = "";
                        for ( int notNull = 1; notNull < 100; notNull++ ) {
                            try {
                                a = rset.getString( notNull );
                                tempCount++;
                                tempRow.row.add( a );
                            } catch ( Exception e ) {
                                notNull = 120;
                                break;
                            }
                        }
                        tempRow.count = tempCount + "";
                        dbobj.add( tempRow );
                    } catch ( Exception e ) {
                        // set count now that we have gone to end of the row.
                        tempRow.count = tempCount + "";
                        dbobj.add( tempRow );
                    }
                }
                rset.close();
            }
            con.close();
        } catch ( Exception e ) {
            System.out.println( "DayMuarryMusic.DBObj-->runQuery: " );
            e.printStackTrace();
            return null;
        }

        return dbobj;
    }

    // get all the column names from a specified table
    public ArrayList<String> getColumns( String tableName ) {
        ArrayList<String> list = new ArrayList<String>();
        Connection con = null;
        MySQL sdb = new MySQL();
        if ( tableName != null && tableName.length() > 2 ) {
            String sql = "sp_columns @table_name=" + tableName;
            try {
                con = sdb.getConn();
                Statement stat = con.createStatement();

                ResultSet rset = stat.executeQuery( sql );
                while ( rset.next() ) {
                    String a = "";
                    a = rset.getString( 4 );
                    list.add( a );
                }
                rset.close();
                con.close();
            } catch ( Exception e ) {
                System.out.println( "Error DayMurrayMusic-->com.soward.object.DBObj getColumns: " );
                e.printStackTrace();
            }

        }
        return list;
    }

    public void getTableInfo( String tableName ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        if ( tableName != null && tableName.length() > 2 ) {
            String sql = "sp_columns @table_name=" + tableName;
            try {
                con = sdb.getConn();
                Statement stat = con.createStatement();

                ResultSet rset = stat.executeQuery( sql );
                while ( rset.next() ) {
                    for ( int notNull = 1; notNull < 100; notNull++ ) {
                        int tempCount = 0;
                        try {
                            String a = "";
                            a = rset.getString( notNull );
                            System.out.print( " | " + a + " | " );
                            tempCount++;
                        } catch ( Exception e ) {
                            notNull = 120;
                            System.out.println( "--------------------------------------" );
                            break;
                        }
                    }
                }
                rset.close();
                con.close();
            } catch ( Exception e ) {
                System.out.println( "Error DayMurrayMusic-->com.soward.object.DBObj getColumns: " );
                e.printStackTrace();
            }

        }
    }

    // get a list of all tables in the db
    public ArrayList<String> getTables() {
        ArrayList<String> list = new ArrayList<String>();
        Connection con = null;
        MySQL sdb = new MySQL();
        PreparedStatement pstmt = null;
        String sql = "SELECT * FROM sysobjects WHERE type='U'";
        try {
            con = sdb.getConn();
            pstmt = con.prepareStatement( sql );
            // pstmt.setMaxRows( 10 );
            ResultSet rset = pstmt.executeQuery();
            // pstmt = con.prepareStatement( sql );
            while ( rset.next() ) {
                String a = "";
                a = rset.getString( 1 );
                list.add( a );
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            System.out.println( "Error DayMurrayMusic-->com.soward.object.DBObj getColumns: " );
            e.printStackTrace();
        }
        return list;
    }
    // get a list of all tables in the db and place the give value in first select space
    public ArrayList<String> getTablesWithFirst(String firstPlace) {
        ArrayList<String> list = new ArrayList<String>();
        Connection con = null;
        MySQL sdb = new MySQL();
        PreparedStatement pstmt = null;
        String sql = "SELECT * FROM sysobjects WHERE type='U'";
        try {
            con = sdb.getConn();
            pstmt = con.prepareStatement( sql );
            // pstmt.setMaxRows( 10 );
            ResultSet rset = pstmt.executeQuery();
            // pstmt = con.prepareStatement( sql );
            list.add(firstPlace);
            while ( rset.next() ) {
                String a = "";
                a = rset.getString( 1 );
                list.add( a );
            }
            rset.close();
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            System.out.println( "Error DayMurrayMusic-->com.soward.object.DBObj getColumns: " );
            e.printStackTrace();
        }
        return list;
    }

    // run a query
    public void runAndPrintOutQuery( String sql, String type ) {
        ArrayList<DBObj> dbobj = this.runQuery( sql, type );
        for ( DBObj ee : dbobj ) {
            for ( String str : ee.getRow() ) {
                System.out.print( " | " + str );
            }
            System.out.println( "" );
        }
    }

    /**
     * @param args
     */
    public static void main( String[] args ) {
        DBObj tt = new DBObj();
        String sql = "update Accounts set  accountEmail     = 'asdfasdf@casd.com',"
                + " accountPhone2    = 'testtesttest', accountStreet    = '12314 sdfas',"
                + " accountState     = 'sdaf', accountCountry   = 'usa', accountType2     = 'LE',"
                + " accountCloseDate = '', accountName      = 'amorvivir',"
                + " accountContact   = 'sdfaas', accountPhone1    = ' ', accountFax       = ' ',"
                + " accountCity      = 'asdf', accountPostalCode= '84121', accountType1     = '19',"
                + " accountOpenDate  = '2006-02-20 22:12:00.0' " + "  where accountNum=39375";

        tt.runQuery( sql, "update" );
    }

}
