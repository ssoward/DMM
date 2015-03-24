package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.print.attribute.standard.Chromaticity;

import com.soward.enums.SearchTypeEnum;
import org.apache.commons.lang.StringUtils;

import com.soward.db.MySQL;
import com.soward.enums.LocationsDBName;
import com.soward.object.Account;
import com.soward.object.AccountType;
import com.soward.object.CreditHistory;
import com.soward.object.DBObj;
import com.soward.object.Invoice;
import org.apache.tools.ant.taskdefs.email.EmailAddress;

public class AccountUtil {
    public String query;

    // test case: limit number of TE accounts returned.
    // set to false will not limit accounts returned.
    public boolean testCompilation = false;

    public int maxAccounts = 10;

    public AccountUtil() {
        this.query = "";
    }

    public AccountUtil( String que, String dte1, String dte2 ) {

    }
    public static boolean updateCredit(String editUserPid, String creditType, String creditSum, String creditDesc){
        boolean success = true;
        String nextPid = AccountUtil.getMaxNextCreditHistoryNumber();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        //insert into AccountCreditHistory (pid, creditType, creditDesc, creditDate) values (1, 'newType', 'asome long description', '')
        Connection con = null;
        MySQL sdb = new MySQL();
        String creditDate = sdf.format(Calendar.getInstance().getTime());
        String sql = "insert into AccountCreditHistory (pid, accountNum, creditSum, creditType, creditDesc, creditDate) " +
                "values ("+nextPid+", "+editUserPid+", "+creditSum+", '"+creditType+"', '"+creditDesc+"', '"+creditDate+"')";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            int rset = pstmt.executeUpdate();
            // only get the next 20 entries

            con.close();

        } catch ( Exception e ) {
            System.out.println(sql);
            e.printStackTrace();
            success = false;
            // set count now that we have gone to end of the row.
        }
        return success;
    }
    public static String truncString(String longStr, int len){
        if (longStr!=null){
            if(longStr.length()>len){
                return longStr.substring( 0, len );
            }
        }
        return longStr;
    }
    public static Account fetchAccount(String pid){
        AccountUtil au = new AccountUtil();
        return au.getAccount(pid);
    }

    // get accounts.
    public Account getAccount( String pid ) {
        Account acc = new Account();
        int pidNum = 0;
        if ( pid != null && pid.length() > 1 ) {
            try {
                pidNum = Integer.parseInt( pid );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            Connection con = null;
            MySQL sdb = new MySQL();
            String sql = "select accountNum,accountName,accountPassword,"
                    + "accountContact,accountEmail,accountPhone1,accountPhone2,"
                    + "accountFax,accountStreet,accountCity,accountState,"
                    + "accountPostalCode,accountCountry,accountType1,accountType2,"
                    + "accountOpenDate,accountCloseDate,accountBalance, accountPostalCode from Accounts where accountNum=" + pidNum + "";
            try {
                con = sdb.getConn();
                PreparedStatement pstmt = null;
                pstmt = con.prepareStatement( sql );
                ResultSet rset = pstmt.executeQuery();
                // only get the next 20 entries
                ArrayList<Account> aList = procRSet(rset);
                acc = aList.isEmpty()?null:aList.get(0);

                rset.close();
                con.close();

            } catch ( Exception e ) {
                e.printStackTrace();
                // set count now that we have gone to end of the row.
            }
        }// end of if pid!=null
        return acc;
    }

    private ArrayList<Account> procRSet(ResultSet rset){
        ArrayList<Account> aList = new ArrayList<Account>();
        try{
            while ( rset.next()){
                Account acc = new Account();
                acc.setAccountNum         (rset.getString("accountNum"));
                acc.setAccountName        (rset.getString("accountName"));
                acc.setAccountPassword    (rset.getString("accountPassword"));
                acc.setAccountContact     (rset.getString("accountContact"));
                acc.setAccountEmail       (rset.getString("accountEmail"));
                acc.setAccountPhone1      (rset.getString("accountPhone1"));
                acc.setAccountPhone2      (rset.getString("accountPhone2"));
                acc.setAccountFax         (rset.getString("accountFax"));
                acc.setAccountStreet      (rset.getString("accountStreet"));
                acc.setAccountCity        (rset.getString("accountCity"));
                acc.setAccountState       (rset.getString("accountState"));
                acc.setAccountPostalCode  (rset.getString("accountPostalCode"));
                acc.setAccountCountry     (rset.getString("accountCountry"));
                acc.setAccountType1       (rset.getString("accountType1"));
                acc.setAccountType2       (rset.getString("accountType2"));
                acc.setAccountOpenDate    (rset.getString("accountOpenDate"));
                acc.setAccountCloseDate   (rset.getString("accountCloseDate"));
                acc.setAccountBalance     (rset.getString("accountBalance"));
                acc.setAccountZip         (rset.getString("accountPostalCode"));
                aList.add(acc);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return aList;
    }

    public static String getAccountName( String pid ) {
        String str = null;
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select accountName from Accounts where accountNum=?";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setInt( 1, Integer.parseInt( pid ));
            ResultSet rset = pstmt.executeQuery();
            // only get the next 20 entries
            while ( rset.next()){
                str =  (rset.getString("accountName"));

            }

            rset.close();
            con.close();

        } catch ( Exception e ) {
            e.printStackTrace();
            // set count now that we have gone to end of the row.
        }

        return str;
    }

    public static ArrayList<Account> getAccountAddress( String like ) {
        String str = null;
        Connection con = null;
        MySQL sdb = new MySQL();
        ArrayList<Account> accounts = new ArrayList<Account>();
        String sql = "select accountNum, accountName, accountContact, accountStreet, " +
                "accountCity, accountState, accountPostalCode, accountCountry from Accounts where accountNum in ("+like+")";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
//            pstmt.setString(1, like);
            ResultSet rset = pstmt.executeQuery();
            // only get the next 20 entries
            while ( rset.next()){
                Account cl = new Account();
                cl.setAccountNum(rset.getString("accountNum"));
                cl.setAccountName(rset.getString("accountName"));
                cl.setAccountContact(rset.getString("accountContact"));
                cl.setAccountStreet(rset.getString("accountStreet"));
                cl.setAccountCity(rset.getString("accountCity"));
                cl.setAccountState(rset.getString("accountState"));
                cl.setAccountPostalCode(rset.getString("accountPostalCode"));
                cl.setAccountCountry(rset.getString("accountCountry"));
                accounts.add( cl );
            }
            rset.close();
            con.close();

        } catch ( Exception e ) {
            e.printStackTrace();
            // set count now that we have gone to end of the row.
        }
        return accounts;
    }
    // get account history.
    public static ArrayList<CreditHistory> getAccountCreditHistory( String pid ) {
        ArrayList<CreditHistory> credList = new ArrayList<CreditHistory>();
        int pidNum = 0;
        if ( pid != null && pid.length() > 1 ) {
            try {
                pidNum = Integer.parseInt( pid );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            Connection con = null;
            MySQL sdb = new MySQL();
            String sql = "select * from AccountCreditHistory where accountNum=" + pidNum + "";
            try {
                con = sdb.getConn();
                PreparedStatement pstmt = null;
                pstmt = con.prepareStatement( sql );
                ResultSet rset = pstmt.executeQuery();
                // only get the next 20 entries
                while ( rset.next()){
                    CreditHistory ch = new CreditHistory();
                    ch.setAccountNum( rset.getString("accountNum"));
                    ch.setCreditDate( rset.getString("creditDate" ));
                    ch.setCreditType( rset.getString("creditType" ));
                    ch.setCreditDesc( rset.getString("creditDesc" ));
                    ch.setCreditSum( rset.getString("creditSum" ));
                    ch.setPid( rset.getString("pid" ));
                    credList.add( ch );
                }

                rset.close();
                con.close();

            } catch ( Exception e ) {
                e.printStackTrace();
                // set count now that we have gone to end of the row.
            }
        }// end of if pid!=null
        return credList;
    }

    // get all the sums for the specified date for each TE account.
    public HashMap getTEAccountInvoices( String dteOne, String dteTwo, String endOfSQLfilter ) {
        // get hashmap to put all the account in that have the invoice info
        // filled out, then match the hashmap to the account data.
        HashMap<String, Account> map = new HashMap<String, Account>();

        if ( dteOne != null && dteTwo != null ) {
            try {
                MySQL sdb = new MySQL();
                Connection con = sdb.getConn();
                PreparedStatement pstmt = null;
                ResultSet rset = null;

                String sqlEmb = "select Accounts.accountNum "+endOfSQLfilter;//from Accounts " + "where Accounts.accountType1='21'";


                String sql = "select  accountNum, sum(invoiceTotal) as invTot, sum(invoicePaid), sum(invoicePaid1),"
                        + " sum(invoicePaid2), sum(invoiceDiscount), sum(invoiceTax) "
                        //date_add('2014-02-06', interval 24 hour)
                        + " from Invoices where invoiceDate > '" + dteOne + "' and invoiceDate< date_add('" + dteTwo + "', interval 24 hour)"
                        + " and accountNum in (" + sqlEmb + ") group by accountNum having sum(invoiceTotal)>0.0  ";
//                System.out.println(sql);
                pstmt = con.prepareStatement( sql );
                rset = pstmt.executeQuery();

                while ( rset.next() ) {
                    Account teacc = new Account();
                    Invoice inv = new Invoice();
                    teacc.setAccountNum( rset.getString( 1 ) );
                    inv.setInvoiceTotalSum( rset.getString( 2 ) );
                    inv.setInvoicePaidSum( rset.getString( 3 ) );
                    inv.setInvoicePaid1Sum( rset.getString( 4 ) );
                    inv.setInvoicePaid2Sum( rset.getString( 5 ) );
                    inv.setInvoiceDiscountSum( rset.getString( 6 ) );
                    inv.setInvoiceTaxSum( rset.getString( 7 ) );
                    teacc.setAccountInv( inv );
                    map.put( rset.getString( 1 ), teacc );
                }
                rset.close();

                pstmt.close();
                con.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }
        return map;
    }

    // get all the tax exempt accounts and match them to the loaded hashmap of
    // accounts with the invoice data set but not the account data.
    public ArrayList<Account> getAllTaxExemptAccounts( String dteOne, String dteTwo, String utahNon, String schoolNon ) {
        if(!Utils.validateDate( dteOne, "yyyy-MM-dd" )||!Utils.validateDate( dteTwo, "yyyy-MM-dd" )){
            return null;
        }
        ArrayList<Account> accounts = new ArrayList<Account>();
        Connection con = null;
        MySQL sdb = new MySQL();
        boolean resetContinue = true;
        String TE = "";
        String UTAHFILTER = "";
        String sql = "select accountNum, accountName, accountPassword, accountContact, "
                + "accountEmail, accountPhone1, accountPhone2, accountFax, accountStreet, "
                + "accountCity, accountState, accountPostalCode, accountCountry, accountType1, "
                + "accountType2, accountOpenDate, accountCloseDate, accountBalance ";

        // filter on all states, just utah, or all non utah states
        if(schoolNon.equalsIgnoreCase( "SCHOOL" )){
            // in this case set accountType1='21'"
            TE = "from Accounts where accountType1='21'";
            sql += TE;
        }
        else if(schoolNon.equalsIgnoreCase( "TEACH" )){
            // in this case set accountType1='21'"
            TE = "from Accounts where accountType1='25'";
            sql += TE;
        }
        // filter on all states, just utah, or all non utah states
        else if(schoolNon.equalsIgnoreCase( "ALLTE" )){
            // accountType2 == Tax Status, in this case set use acountType"2"  as in accountType2='21' which get all tex exempt
            TE =  "from Accounts where accountType2='TE'";
            sql += TE;
        }
        sql += " order by accountName";
        // filter on all states, just utah, or all non utah states
        // currently I am unable to filter on utah only states because
        // utah is spelled many differnt ways in the db.
        //      if(utahNon.equalsIgnoreCase( "UTAH" )){
        //      // in this case just take out the where accountType1='21'"
        //      UTAHFILTER = " and accountState like 'UT'";
        //      sql += UTAHFILTER;
        //      }
        //      // filter on all states, just utah, or all non utah states
        //      if(utahNon.equalsIgnoreCase( "UTAH" )){
        //      // in this case just take out the where accountType1='21'"
        //      UTAHFILTER = " and accountState !like 'UT'";
        //      sql += UTAHFILTER;
        //      }

        HashMap map = getTEAccountInvoices( dteOne, dteTwo, TE );


        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            int count = 0;
            while ( rset.next() ) {
                count++;
                Account cl = new Account();
                cl.setAccountNum( rset.getString( "accountNum" ) );
                // if this is a accountNum that was filter through the
                // getTEAccountInvoices select then add it to return set
                if ( map.containsKey( cl.getAccountNum() ) ) {
                    // if this is a member of the set in map, get the 
                    // account and populate the rest of the account feilds.
                    cl = (Account) map.get( cl.getAccountNum() );
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

                    accounts.add( cl );
                }
                if ( testCompilation && count > maxAccounts ) {
                    while ( rset.next() ) {
                    }
                }
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return accounts;
    }

    public String nullToDouble( String isNull ) {
        if ( isNull == null ) {
            return "0.0";
        }
        return isNull;
    }

    private static String getMaxNextCreditHistoryNumber( ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        ArrayList<String> allLocations = new ArrayList<String>();
        for(LocationsDBName lname : LocationsDBName.values()){
            allLocations.add( lname.dbName() );
        }
        String sql = "select max(pid) as pidMax from AccountCreditHistory ";
        int maxNum = 1;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            if ( rset.next() ) {
                String max = rset.getString( "pidMax" );
                int maxInt =Integer.parseInt( max );
                if(maxInt>maxNum){
                    maxNum = maxInt;
                }
            }
            maxNum++;
        } catch ( Exception e ) {
            System.out.println("getMaxNextCreditHistoryNumber: "+sql);
            e.printStackTrace();
        }
        return maxNum + "";
    }

    public static ArrayList<Account> searchAccounts(String searchObj  ) {
        ArrayList<Account> prods = new ArrayList<Account>();
        Connection con = null;
        MySQL sdb = new MySQL();

        String sql = "(select * from Accounts where lower(accountName) like ? " +
                "union all select * from Accounts where accountNum like ? " +
                "union all select * from Accounts where accountPhone1 like ?) limit 10";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString( 1, "%"+searchObj.trim().toLowerCase()+"%" );
            pstmt.setString( 2, searchObj.trim()+"%" );
            pstmt.setString( 3, searchObj+"%" );
            ResultSet rset = pstmt.executeQuery();
            while ( rset.next() ) {
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
                prods.add( cl );
            }

            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return prods;
    }
    /**
     * creates a map for this list with key = accountnum, value == account obj
     * @param list
     * @return
     */
    public static HashMap<String, Account> createMap( ArrayList<Account> list ) {
        HashMap<String,Account> map = new HashMap<String, Account>();
        if(list!=null&&!list.isEmpty()){
            for(Account aa: list){
                map.put( aa.getAccountNum(), aa );
            }
        }
        return map;
    }

    public static boolean saveType1ForId(String acct, String accountTypeUpdate) {
        boolean suc = true;
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "update Accounts set accountType1 = '"+accountTypeUpdate+"' where accountNum in( "+acct+")";
        //System.out.println(sql);
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            int rset = pstmt.executeUpdate();
            con.close();

        } catch ( Exception e ) {
            System.out.println(sql);
            e.printStackTrace();
            suc = false;
        }
        return suc;
    }
    public static String saveAllAccountType1ForQuery(String name, String accType, String updateAllAccountType1) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String selected = "";
        System.out.println("name: "+name +" accType: "+accType+" updateAllAccojuntType1: "+updateAllAccountType1);
        //		List<AccountType> accTListAll = AccountTypesUtil.getAccountTypes();
        //		for ( AccountType accT : accTListAll ) {
        //			if(updateAllAccountType1!=null&&updateAllAccountType1.equals(accT.getTypeCode())){
        //				selected = accT.getTypeDescription();
        //			}
        //		}
        String success = "Update of ALL account types to: "+selected+" successful.";
        //		if((StringUtils.isBlank(name)||name.equals("Name or Number")&&StringUtils.isBlank(accType))||StringUtils.isBlank(updateAllAccountType1)){
        //			return "Failed to update to account type "+selected+", missing filter criteria.";
        //		}
        //		String sql = "update Accounts set accountType1 = '"+updateAllAccountType1+"' where ";
        //		String where = "";
        //		boolean addAnd = false;
        //		if(!StringUtils.isBlank(name)&& !name.equals("Name or Number")){
        //			addAnd = true;
        //			where += "( lower(accountName) like '%"+(name.trim()).toLowerCase()+"%' or " +
        //			"convert(varchar(12), accountNum) like '%"+name.trim()+"%' )";
        //		}
        //		if(!StringUtils.isBlank(accType)){
        //			String at = " accountType2 = '"+accType+"'";
        //			if(addAnd){
        //				where = where+" and "+at;
        //			}else{
        //				where = at;
        //			}
        //		}
        //		sql += where;			
        //		try {
        //			//System.out.println(sql);
        //			con = sdb.getConn();
        //			PreparedStatement pstmt = null;
        //			pstmt = con.prepareStatement( sql );
        //			int rset = pstmt.executeUpdate();
        //			con.close();
        //
        //		} catch ( Exception e ) {
        //			success = "Update failed";
        //			System.out.println(sql);
        //			e.printStackTrace();
        //		}
        return success;
    }

    public static Account saveUpdateAccount(Account acct) {


        String sql = "";
        boolean newAcct = false;
        if(!StringUtils.isBlank(acct.getAccountNum())){
            sql = "update Accounts set accountName=?, accountEmail=?, accountPhone1=?, accountPhone2=?, accountStreet=?, " +
                    "accountCity=?, accountState=?, accountPostalCode=?, accountContact=?  where accountNum=?";
        }else{
            String ddt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
            sql = "insert into Accounts (accountName, accountEmail, accountPhone1, accountPhone2, accountStreet," +
                    "accountCity, accountState, accountPostalCode, accountContact, accountNum, accountType1, accountType2, accountCountry, accountOpenDate) values(?,?,?,?,?,?,?,?,?,?, '10', 'ST', 'USA','"+ddt+"')";
            acct.setAccountNum(getMaxNextAccountNumber());
            newAcct = true;
        }
        System.out.println(sql);
        try{
            Connection con = null;
            MySQL sdb = new MySQL();
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            String an = acct.getAccountName();
            if(!StringUtils.isBlank( an ) && an.contains( "&" )){
                an.replace( "&", "%26");
            }
            acct.setAccountName( an);
            System.out.println("AccountName: "+an);
            pstmt.setString(1, acct.getAccountName());
            pstmt.setString(2, acct.getAccountEmail());
            pstmt.setString(3, acct.getAccountPhone1());
            pstmt.setString(4, acct.getAccountPhone2());
            pstmt.setString(5, acct.getAccountStreet());
            pstmt.setString(6, acct.getAccountCity());
            pstmt.setString(7, acct.getAccountState());
            pstmt.setString(8, acct.getAccountZip());
            pstmt.setString(9, acct.getAccountContact());
            pstmt.setInt(10, Utils.parseInt(acct.getAccountNum()));
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        }catch(Exception e){
            System.out.println("SQL: "+sql);
            e.printStackTrace();
        }
        return acct;
    }
    private static String getMaxNextAccountNumber( ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select max(accountNum) as productMax from Accounts";
        int maxNum = 0;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            if ( rset.next() ) {
                String max = rset.getString( "productMax" );
                try {
                    int maxInt =Utils.parseInt( max );
                    if(maxInt>maxNum){
                        maxNum = maxInt;
                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
            maxNum++;
        } catch ( Exception e ) {
            System.out.println("getMaxNextProductNumber: "+sql);
            e.printStackTrace();
        }
        return maxNum + "";
    }

    public static ArrayList<Account> fetchAccounts(String searchType, String queryTxt, String accType, boolean balanceSort, int resultSizeInt) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String sqlStr = "";

//        sqlStr += " select accountNum, accountName, accountContact, accountEmail, accountBalance, accountType1,\n" +
//                "accountStreet, accountCity, accountState, accountPostalCode from Accounts \n";
        sqlStr += " select * from Accounts \n";


        String whereStmt =  "where ";

        SearchTypeEnum searchTypeEnum = SearchTypeEnum.valueForName(searchType);
        switch(searchTypeEnum){
            case EMAIL:
                whereStmt += "lower(accountEmail) like '%"+(queryTxt.trim()).toLowerCase()+"%'";
                break;
            case ZIPCODE:
                whereStmt += "lower(accountPostalCode) like '%"+(queryTxt.trim()).toLowerCase()+"%'";
                break;
            case ACCOUNTNAME:
                whereStmt += "lower(accountName) like '%"+(queryTxt.trim()).toLowerCase()+"%'";
                break;
            case ACCOUNTNUMBER:
                whereStmt += " accountNum like '"+queryTxt.trim()+"'";
                break;
            case CITY:
                whereStmt += "lower(accountCity) like '%"+(queryTxt.trim()).toLowerCase()+"%'";
                break;
            case STATE:
                whereStmt += "lower(accountState) like '%"+(queryTxt.trim()).toLowerCase()+"%'";
                break;
            default:
                whereStmt = null;
                break;
        }
        if(!StringUtils.isBlank( accType )){
            String where = whereStmt!=null?" and ": "where ";
            where += " accountType1 = '"+accType+"'";
            whereStmt = whereStmt!=null? whereStmt + where: where;
        }

        sqlStr += whereStmt!=null?whereStmt:"";
        sqlStr += " order by "+(balanceSort?"accountBalance desc ":" accountName ");

        if(resultSizeInt>0){
            sqlStr += " limit "+ resultSizeInt;
        }

        //System.out.println(sqlStr);
        ArrayList<Account> aList = new ArrayList<Account>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sqlStr );
            ResultSet rset = pstmt.executeQuery();
            aList = new AccountUtil().procRSet(rset);

        } catch ( Exception e ) {
            System.out.println(sqlStr);
            e.printStackTrace();
        }
        return aList;
    }

    public static void main( String[] args ) {
//        ArrayList<Account> aList = AccountUtil.getAccountAddress("1000,1001,1002,1003,1005,1007,1008,1009,1010,1011,1012,1013,1014,1015,1016");
        ArrayList<Account> aList = AccountUtil.getAccountAddress("40730");
        for(Account a: aList){
            System.out.println(a.getAccountAddressFormated());
        }






        //      AccountUtil.getAccountCreditHistory( "1053" );
        //      System.out.println("done");
        //System.out.println(AccountUtil.getMaxNextCreditHistoryNumber());
//        ArrayList<Account> aList = AccountUtil.searchAccounts( "scott" );
//        for(SearchTypeEnum en: SearchTypeEnum.values()){
//            ArrayList<Account> aList = AccountUtil.fetchAccounts(en.getName(), "scott",null, false, 10 );
//            for(Account a: aList){
//                System.out.println(a.toString());
//                System.out.println("---------------------------------");
//            }
//            System.out.println("#######################################################");
//        }

    }
}
