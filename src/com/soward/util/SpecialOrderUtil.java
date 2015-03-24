package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URLDecoder;
import java.net.URLEncoder;
import com.soward.db.DB;
import com.soward.object.Account;
import com.soward.object.Descriptions;
import com.soward.object.ProdOrderGto;
import com.soward.object.Product;
import com.soward.object.SpecialOrderGto;

public class SpecialOrderUtil {


    public static void main(String[] ars){
        String details ="{orderby:person,droppedLocation:murray,shipMethod:002,droppedShip:overnight,shipNotes:'scoas asdf asfd asf assdfasfdsfas;jk ;lkj ;lkj l;aksdf;ioasne ',today:'Wed May 11 21:14:06 MDT 2011',createdBy:scott,accountNum:48986,delievery:12-01-2999}";
        String sorder ="{supplier00a:128,quanity001a:13,itemTextSelection001a:00060090,verified001a:true,itemEmailed001a:false,prodNum001a:19321,itemStatus001a:null,}"; 
        SpecialOrderGto s = SpecialOrderUtil.fetchForId( 18l);

        List<ProdOrderGto> pList =  SpecialOrderUtil.fetchSPOForId( s.getId() );
        if(pList!=null && !pList.isEmpty()){
            System.out.println(s.getId());
            s.setProdList( pList );
            for(ProdOrderGto p: pList){
                System.out.println(p.toString());
            }
        }
        System.out.println(s.toString());


        //                try {
        //                    JSONObject object=new JSONObject();
        //                    object.put("name","Amit Kumar");
        //                    object.put("Max.Marks",new Integer(100));
        //                    object.put("Min.Marks",new Double(40));
        //                    object.put("Scored",new Double(66.67));
        //                    object.put("nickname","Amit");
        //                    System.out.println(object);
        //                } catch ( JSONException e ) {
        //                    // TODO Auto-generated catch block
        //                    e.printStackTrace();
        //                }





        //        List<ProdOrderGto> gtos = SpecialOrderUtil.fetchSPOForId( 1l);
        //SpecialOrderGto s = createSpecialOrder( sorder, details );
        //        System.out.println(s.toString());
        //        for(ProdOrderGto gtto: gtos){
        //            gtto.setStatus( "BallsOut" );
        //            System.out.println("PList "+gtto.toString());
        //            SpecialOrderUtil.saveUpdate( gtto );
        //        }
        //        s.setPickUp( "testPickUp" );
        //        SpecialOrderUtil.saveUpdate( s );
        //            JSONArray locations = object.getJSONArray("taba");
        //System.out.println(query);
        //System.out.println(query2);

    }

    public static SpecialOrderGto createSpecialOrder(String sorder, String details){
        //        sorder ="{quanity001a:0,itemTextSelection001a:Two-Part Mixed,verified001a:false,itemEmailed001a:false,quanity001b:0,itemTextSelection001b:0 Default,verified001b:false,itemEmailed001b:false,}";
        //        details  ="{orderby:person,droppedLocation:murray,shipMethod:003,droppedShip:2Days,}";//shipNotes:asdf adf asdf asdfasdfadsf,today:Sat Apr 09 15:25:17 MDT 2011,createdBy:scott,accountNum:3909,delievery:04-09-2011}";
        //        sorder = "{supplier00a:27,quanity001a:0,itemTextSelection001a:2030S,verified001a:true,itemEmailed001a:false,supplier00b:128,quanity001b:10,itemTextSelection001b:00030001,verified001b:true,itemEmailed001b:false,quanity002b:0,itemTextSelection002b:00050101,verified002b:true,itemEmailed002b:false,prodNum001a:9192}"; 
        //        details ="{orderby:person,droppedLocation:murray,shipMethod:003,droppedShip:3Days,today:'Tue May 10 21:36:41 MDT 2011',createdBy:scott,accountNum:40730,delievery:12-01-2999}";

        try{ 

            JSONObject j1 = null;
            try {
                j1 = (JSONObject) new JSONTokener(sorder).nextValue();
            } catch ( Exception e ) {
                System.out.println(sorder);
                e.printStackTrace();
            }
            JSONObject j2 = null;
            try {
                j2 = (JSONObject) new JSONTokener(details).nextValue();
            } catch ( Exception e ) {
                System.out.println("ERROR==> DETAILS: "+details);
                e.printStackTrace();
            }
            SpecialOrderGto gto = new SpecialOrderGto();
            String[] tabs = new String[]{"a","b","c","d","e","f","g","h","i","j"};
            //            for(String s1: JSONObject.getNames( j2 )){
            //                System.out.println(s1);
            //            }

            //sorder  : {supplier00a:27,quanity001a:4,itemTextSelection001a:30700,verified001a:true,itemEmailed001a:false,prodNum001a:174351,itemStatus001a:top,quanity002a:10,itemTextSelection002a:30703,verified002a:true,itemEmailed002a:false,prodNum002a:174355,itemStatus002a:bo,supplier00b:128,quanity001b:6,itemTextSelection001b:00370176,verified001b:true,itemEmailed001b:false,prodNum001b:186299,itemStatus001b:pop,quanity002b:4,itemTextSelection002b:00300285,verified002b:true,itemEmailed002b:true,prodNum002b:21521,itemStatus002b:top,}


            gto.setOrderDate      (get("today"            ,j2));
            gto.setAccountNum     (get("accountNum"       ,j2));
            gto.setAccountName    (get("accountName"      ,j2));
            gto.setShipMethod     (get("shipMethod"       ,j2));
            gto.setOrderMethod    (get("orderby"          ,j2));
            gto.setShipNote       (get("shipNotes"        ,j2));
            gto.setCreatedBy      (get("createdBy"        ,j2));
            gto.setDroppedShipped (get("droppedShip"      ,j2));
            gto.setDeliveryDate   (get("delievery"        ,j2));
            gto.setDroppedLocation(get("droppedLocation"  ,j2));
            gto.setPickUp         (get("pickUp"           ,j2));
            gto.setOrderStatus    (get("orderStatus"      ,j2));
            String l =             get("orderId"      ,j2);
            Long n = l!=null?new Long(l):null;
            gto.setId             (n);

            for(String tab: tabs){
                String sNum = get("supplier00"+tab, j1);
                for(int o = 1; o < 10; o++){
                    ProdOrderGto pO = new ProdOrderGto();
                    String pNum = get("prodNum00"+o+tab, j1);

                    if(pNum == null)
                        continue;
                    pO.setProductNum ( pNum );
                    pO.setQuantity( get("quanity00"+o+tab, j1));
                    pO.setVerified( get("verified00"+o+tab, j1));
                    pO.setEmailed ( get("itemEmailed00"+o+tab, j1));
                    pO.setStatus  ( get("itemStatus00"+o+tab, j1));
                    String a = get("prodOrderId00"+o+tab, j1);
                    if(a!=null&&!a.equals( "undefined" ))
                        try{pO.setId      ( new Long(a));}catch(Exception e){/*not a number */}
                    pO.setSupNum( sNum );
                    gto.getProdList().add( pO );
                }
            }
            return gto;   
        }catch(Exception e){
            System.out.println("DETAILS: "+details);
            System.out.println("ORDER: "+sorder);
            e.printStackTrace();
        }
        return null;
    }
    private static ArrayList<SpecialOrderGto> parseRSet(ResultSet rset, boolean getName) {
        ArrayList<SpecialOrderGto> oList = new ArrayList<SpecialOrderGto>();
        try {
            while(rset.next()){
                SpecialOrderGto gto = new SpecialOrderGto();
                gto.setId(rset.getLong(1));
                gto.setOrderMethod     (rset.getString("orderMethod"));
                gto.setDroppedLocation (rset.getString("droppedLocation"));
                gto.setShipMethod      (rset.getString("shipMethod"));
                gto.setDroppedShipped  (rset.getString("droppedShipped"));
                gto.setPickUp          (rset.getString("pickUp"));
                gto.setOrderStatus     (rset.getString("orderStatus"));
                gto.setShipNote        (rset.getString("shipNote"));
                gto.setOrderDate       (rset.getString("orderDate"));
                gto.setCreatedBy       (rset.getString("createdBy"));
                gto.setDeliveryDate    (rset.getString("deliveryDate"));
                gto.setAccountNum      (rset.getString("accountNum"));
                gto.setAccountName     (rset.getString("accountName" ));
                if(getName && gto.getAccountNum() != null){
                    String name = AccountUtil.getAccountName(gto.getAccountNum());
                    Account a = new Account();
                    a.setAccountName( name );
                    gto.setAccount( a );
                }
                oList.add(gto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return oList;
    }
    private static ArrayList<ProdOrderGto> parseSOPRSet(ResultSet rset, boolean getProd) {
        ArrayList<ProdOrderGto> oList = new ArrayList<ProdOrderGto>();
        try {

            HashMap<String,String> hash = null;
            if(getProd){
                hash = DescriptionsUtil.getHashCodes();
            }
            while(rset.next()){
                ProdOrderGto gto = new ProdOrderGto();
                gto.setId       (rset.getLong(1));
                gto.setProductNum  (rset.getString("prodNum"));
                gto.setVerified (rset.getString("verified"));
                gto.setQuantity (rset.getString("quantity"));
                gto.setStatus   (rset.getString("status"));
                gto.setEmailed  (rset.getString("emailed"));
                gto.setSupNum   (rset.getString("sup_num"));
                if(getProd){
                    Product prod = ProductUtils.fetchProductForNum( gto.getProductNum(), null );
                    try {
                        String departmentCode = prod.getCategory().charAt(0)+"";
                        String descriptionCode = prod.getCategory().charAt(5)+"";
                        String nameee = hash.get(descriptionCode+departmentCode);
                        gto.setCategory( nameee );
                    } catch ( Exception e ) {
                        //e.printStackTrace();
                    }
                    gto.setProductCatalogNum( prod.getProductCatalogNum() );
                    gto.setProductName( prod.getProductName() );
                    gto.setProductCost1( prod.getProductCost1() );
                }

                oList.add(gto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return oList;
    }
    public static SpecialOrderGto fetchForId(Long id){
        Connection con = null;

        DB db = new DB();
        PreparedStatement pstmt = null;
        try{
            con = db.openConnection();
            String sql = "select * from dmm.special_order where id = ?";
            pstmt = con.prepareStatement( sql );
            pstmt.setInt(1, (id.intValue()));
            ResultSet rset = pstmt.executeQuery();
            ArrayList<SpecialOrderGto> oList = parseRSet(rset, false);
            //System.out.println(sql+" "+orderNum);
            if(oList!=null&&!oList.isEmpty()){
                return oList.get(0);
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static List<ProdOrderGto> fetchSPOForId(Long id){
        Connection con = null;

        DB db = new DB();
        PreparedStatement pstmt = null;
        try{
            con = db.openConnection();
            String sql = "select * from dmm.special_order_prods where s_order_id = ?";
            pstmt = con.prepareStatement( sql );
            pstmt.setInt(1, (id.intValue()));
            ResultSet rset = pstmt.executeQuery();
            ArrayList<ProdOrderGto> oList = parseSOPRSet(rset, true);
            //            System.out.println(sql+" "+oList.size()+" "+id);
            if(oList!=null&&!oList.isEmpty()){
                return oList;
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static List<SpecialOrderGto> getAllOrders(){
        Connection con = null;

        DB db = new DB();
        PreparedStatement pstmt = null;
        List<SpecialOrderGto> sList = null;
        try{
            con = db.openConnection();
            String sql = "select * from dmm.special_order";
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            sList = parseRSet(rset, false);
            //System.out.println(sql+" "+orderNum);
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return sList;
    }

    private static String get( String str, JSONObject j1 ) {
        try {
            return  j1.getString(str);
        } catch ( JSONException e ) {
            //NOT FOUND e.printStackTrace();
        }catch(Exception e){
        }
        return null;
    }


    public static void saveUpdate( ProdOrderGto gto ) {
        Connection con = null;
        DB db = new DB();

        //Order sup = null;
        try {
            String updateSql = "";
            con = db.openConnection();
            PreparedStatement pstmt = null;
            if(gto.getId()!=null){
                updateSql = "update dmm.special_order_prods set "+
                "prodNum    =?,"+
                "verified   =?,"+
                "quantity   =?,"+
                "status     =?,"+
                "emailed    =?, "+
                "s_order_id =?,"+
                "sup_num=? "+
                "where id=?";
            }else{
                updateSql = "insert into dmm.special_order_prods(id,"+
                "prodNum  ,"+
                "verified ,"+
                "quantity ,"+
                "status   ,"+
                "emailed  , "+
                "s_order_id, "+
                "sup_num "+
                ")                "+
                "values(null,?,?,?,?,?,?,?)";
            }
            //System.out.println(updateSql);
            pstmt = con.prepareStatement( updateSql );
            pstmt.setString(1, gto.getProductNum     ());
            pstmt.setString(2, gto.getVerified());
            pstmt.setString(3, gto.getQuantity      ());
            pstmt.setString(4, gto.getStatus  ());
            pstmt.setString(5, gto.getEmailed          ());
            pstmt.setInt(6, gto.getSOrderId().intValue());
            pstmt.setString(7, gto.getSupNum());
            if(gto.getId()!=null){
                pstmt.setInt(8, gto.getId().intValue());
            }
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void saveUpdate(SpecialOrderGto gto){

        Connection con = null;
        DB db = new DB();

        //Order sup = null;
        try {
            String updateSql = "";
            con = db.openConnection();
            PreparedStatement pstmt = null;
            if(gto.getId()!=null){
                updateSql = "update dmm.special_order set "+
                "orderMethod     =?,"+
                "droppedLocation =?,"+
                "shipMethod      =?,"+
                "droppedShipped  =?,"+
                "pickUp          =?,"+
                "orderStatus     =?,"+
                "shipNote        =?,"+
                "orderDate       =?,"+
                "createdBy       =?,"+
                "deliveryDate    =?,"+
                "accountNum      =?,"+
                "accountName     =? "+
                "where id=?";
            }else{
                updateSql = "insert into dmm.special_order(id,"+
                "orderMethod     ,"+
                "droppedLocation ,"+
                "shipMethod      ,"+
                "droppedShipped  ,"+
                "pickUp          ,"+
                "orderStatus     ,"+
                "shipNote        ,"+
                "orderDate       ,"+
                "createdBy       ,"+
                "deliveryDate    ,"+
                "accountNum      ,"+
                "accountName      "+
                ")                "+
                "values(null,?,?,?,?,?,?,?,?,?,?,?,?)";
            }
            //            System.out.println(updateSql);
            pstmt = con.prepareStatement( updateSql );
            pstmt.setString(1, gto.getOrderMethod     ());
            pstmt.setString(2, gto.getDroppedLocation ());
            pstmt.setString(3, gto.getShipMethod      ());
            pstmt.setString(4, gto.getDroppedShipped  ());
            pstmt.setString(5, gto.getPickUp          ());
            pstmt.setString(6, gto.getOrderStatus     ());
            pstmt.setString(7, StringUtils.isBlank( gto.getShipNote        ())?null:URLDecoder.decode( gto.getShipNote        ()));
            pstmt.setString(8, gto.getOrderDate       ());
            pstmt.setString(9, gto.getCreatedBy       ());
            pstmt.setString(10, gto.getDeliveryDate   ());
            pstmt.setString(11, gto.getAccountNum     ());
            pstmt.setString(12, gto.getAccountName    ());
            Long key = null;
            if(gto.getId()!=null){
                key = gto.getId();
                pstmt.setInt(13, gto.getId().intValue());
            }
            pstmt.executeUpdate();
            ResultSet keys = pstmt.getGeneratedKeys();
            pstmt.close();
            con.close();
            if(keys.next()){
                key = keys.getLong( 1 );
            }
            if(key!=null && gto.getProdList()!=null && !gto.getProdList().isEmpty()){
                for(ProdOrderGto prod: gto.getProdList()){
                    prod.setSOrderId( key );
                    SpecialOrderUtil.saveUpdate( prod );
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
