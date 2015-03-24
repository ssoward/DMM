package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.soward.db.MySQL;
import com.soward.object.AccountType;

public class AccountTypesUtil {

    public static List<AccountType> getAccountTypes(  ) {
        List<AccountType> acc = new ArrayList<AccountType>();
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from AccountTypes";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            acc = constructObj(rset);
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return acc;
    }

    private static List<AccountType> constructObj( ResultSet rset ) throws Exception{
        List<AccountType> accList = new ArrayList<AccountType>();
        while ( rset.next()){
            AccountType acc = new AccountType();
            acc.setTypeCode( rset.getString("typeCode"));        
            acc.setTypeDescription(  rset.getString("typeDescription"));        
            acc.setTypeDiscount( rset.getDouble( "typeDiscount"));        
            acc.setTypeDiscount2( rset.getDouble("typeDiscount2"));
            accList.add(acc);
        }
        return accList;
    }



}
