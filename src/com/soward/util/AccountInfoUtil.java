package com.soward.util;

import com.soward.db.DB;
import com.soward.object.AccountInfo;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountInfoUtil {

    private static String key = "aaa";//AccountSecurity.key;

    public static void saveUpdate(AccountInfo ai){
       
        DB db = new DB();
        boolean isAdmin = true;
        try {
            Connection con = db.openConnection();
            PreparedStatement p = null;
            String sql = "insert into account_info values(null, " +
            		"aes_encrypt(?,?)," +//name 1,2
            		"aes_encrypt(?,?)," +//type 3,4
            		"aes_encrypt(?,?)," +//number 5,6
            		"aes_encrypt(?,?)," +//code 7,8 
            		"               ?," +//month 9
            		"               ?," +//year 10
            		"               ?)"; //account 11
            if(!StringUtils.isBlank( ai.getPid())){
                sql = "update account_info set " +
                		"name=aes_encrypt(?,?)," +
                		"type=aes_encrypt(?,?)," +
                		"number=aes_encrypt(?,?)," +
                		"code=aes_encrypt(?,?)," +
                		"month=?," +
                		"year=?," +
                		"account=?" +
                		"where pid=?";
            }
            p = con.prepareStatement( sql );
            p.setString(1, ai.getName() );
            p.setString(2, key);
            p.setString(3, ai.getType());
            p.setString(4, key);
            p.setString(5, ai.getNumber());
            p.setString(6, key);
            p.setString(7, ai.getCode());
            p.setString(8, key);
            int m = StringUtils.isBlank( ai.getMonth() )?0:Integer.parseInt( ai.getMonth() );
            int y = StringUtils.isBlank( ai.getYear() )?0:Integer.parseInt( ai.getYear() );
            p.setInt( 9,m );
            p.setInt( 10, y );
            p.setString(11, ai.getAccount());

            if(!StringUtils.isBlank( ai.getPid())){
                p.setInt( 12, Integer.parseInt( ai.getPid()));
            }
            System.out.println(sql);
            System.out.println(ai.toString());
            p.execute();
            p.close();
            con.close();
            
        } catch ( Exception e ) {
            System.out.println(ai.toString());
            e.printStackTrace();
        }
    }
    public static AccountInfo fetchForId(Long id){
        try {
            DB db = new DB();
            Connection con = db.openConnection();
            PreparedStatement p = null;
            String sql = "select pid," +
            		"aes_decrypt(name,?) as name," +
            		"aes_decrypt(type,?) as type," +
            		"aes_decrypt(number,?) as number," +
            		"aes_decrypt(code,?) as code," +
            		"month, year, account " +
            		" from account_info where pid=?";
            p = con.prepareStatement( sql );
            p.setString(1, key);
            p.setString(2, key);
            p.setString(3, key);
            p.setString(4, key);
            p.setInt( 5, id.intValue());

            System.out.println(sql);
            ResultSet rset = p.executeQuery();
            List<AccountInfo> ai = rsetToList(rset);
            return ai.isEmpty()?null:ai.get( 0 );

        } catch ( Exception e ) {
            e.printStackTrace();
        } 
        return null;
    }

    private static List<AccountInfo> rsetToList( ResultSet rset ) {
        ArrayList<AccountInfo> al = new ArrayList<AccountInfo>();
        try {
            while(rset.next()){
                AccountInfo ai = new AccountInfo();
                ai.setCode( rset.getString( "code" ) );
                ai.setMonth( rset.getString( "month" ) );
                ai.setYear( rset.getString( "year" ) );
                ai.setName( rset.getString("name") );
                ai.setNumber( rset.getString("number") );
                ai.setPid( rset.getString( "pid" ) );
                ai.setAccount( rset.getString( "account" ) );
                ai.setType( rset.getString("type") );
                al.add(ai);                
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return al;
    }
    public static void main(String args[]){
        AccountInfo ai = new AccountInfo();
//        ai.setName( "Scott" );
//        ai.setAccount( "40730" );
//        AccountInfoUtil.saveUpdate( ai );
        Long z = 5l;
        ai = AccountInfoUtil.fetchForId( z );
        ai.setCode( "SomeCode" );
        ai.setNumber( "452152368521452478965123577" );
        ai.setType( "VISA" );
        AccountInfoUtil.saveUpdate( ai );
        System.out.println(AccountInfoUtil.fetchForId( z ).toString());
    }
    public static AccountInfo fetchForAccountNum( String accNum ) {
        try {
            DB db = new DB();
            Connection con = db.openConnection();
            PreparedStatement p = null;
            String sql = "select pid," +
                    "aes_decrypt(name,?) as name," +
                    "aes_decrypt(type,?) as type," +
                    "aes_decrypt(number,?) as number," +
                    "aes_decrypt(code,?) as code," +
                    "month, year, account " +
                    " from account_info where account=?";
            p = con.prepareStatement( sql );
            p.setString(1, key);
            p.setString(2, key);
            p.setString(3, key);
            p.setString(4, key);
            p.setString( 5, accNum);

            System.out.println(sql);
            ResultSet rset = p.executeQuery();
            List<AccountInfo> ai = rsetToList(rset);
            return ai.isEmpty()?null:ai.get( 0 );

        } catch ( Exception e ) {
            e.printStackTrace();
        } 
        return null;
    }

}
