package com.soward.util;

import com.soward.db.MySQL;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created with IntelliJ IDEA.
 * User: ssoward
 * Date: 12/31/13
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBUtil {

    public static void main(String args[]){
        //code run 12/31/2013 DBUtil.updateProductInfo();
        System.out.println("done");
    }

    public static void updateProductInfo(){
        //update Products set productCost2 = 50 where productSupplier1 = 27 and productCost2 = 0
        String sql = "update Products set productCost2 = ? where productSupplier1 = ? and productCost2 = 0";
        Connection con = null;
        MySQL sdb = new MySQL();
        try {
            PreparedStatement pstmt = null;
            for(SupplierDiscountEnum e: SupplierDiscountEnum.values()){
                con = sdb.getConn();
                pstmt = con.prepareStatement(sql);
                pstmt.setLong(1, e.discount);
                pstmt.setLong(2, e.supId);
                pstmt.executeUpdate();
            }
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
enum SupplierDiscountEnum{
    Alfred           (27l, 50l ),
    AMSI             (29l, 30l ),
    Boosey           (43l, 40l ),
    Boston           (44l, 50l ),
    Carl_Fischer     (58l, 40l ),
    F_J_H            (108l, 40l ),
    Hal_Leonard      (128l, 50l ),
    Hinshaw          (138l, 40l ),
    Hope             (140l, 40l ),
    International    (145l, 40l ),
    Jackman          (149l, 40l ),
    Lorenz           (169l, 40l ),
    Music_Sales      (206l, 50l ),
    Myklas_Music     (209l, 50l ),
    Kjos             (213l, 40l ),
    Shawnee          (263l, 50l ),
    Theodore_Presser (292l, 40l ),
    Warner           (309l, 50l ),
    Willis           (313l, 50l ),
    Professional     (405l, 40l ),
    Sound_Forth      (703l, 40l ),
    Oxford           (1147l, 40l ),
    Walton_Music     (1163l, 50l ),
    Brilee           (2901l, 40l ),
    Prime            (3215l, 40l ),
    G_Schirmer       (3553l, 50l );

    Long supId;
    Long discount;

    SupplierDiscountEnum(Long i, Long d){
        this.discount = d;
        this.supId = i;
    }


}
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
