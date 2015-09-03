package com.soward.util;

import com.soward.db.DB;
import com.soward.db.MySQL;
import com.soward.enums.LocationsDBName;
import com.soward.enums.ProductColsEnum;
import com.soward.enums.ProductWeight;
import com.soward.object.Product;
import com.soward.object.ProductSold;
import com.soward.object.ProductsLocationCount;
import org.apache.commons.discovery.log.SimpleLog;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProductUtils {
    static Log log = new SimpleLog(ProductUtils.class.getName());


    /**
     * Add X qty from one product db and sub X from the other product db.
     * @param prodNum
     * @param moveCount
     * @param moveLocation
     * @return
     */public static String moveProdQtyFromTo(String prodNum, String moveCount, String moveLocation, String username){
        String message = "";
        if(prodNum==null||!StringUtils.isNumeric(moveCount)
                ||!StringUtils.isNumeric(prodNum)){
            return null;
        }
        String sql = "";
        String moveLog = "";
        Connection con = null;
        MySQL sdb = new MySQL();
        DB db = new DB();
        try{
            int add = Utils.parseInt(moveCount);
            int pn = Utils.parseInt(prodNum);
            String moveLogToFrom = "";

            //moveLocation == 0 --> Murray to Lehi
            if(moveLocation.equals( "0" )){
                sql = "update ProductsLocationCount set MURRAY=MURRAY-?, LEHI=LEHI+? where productNum=?";
                message = moveCount + " from Murray to Lehi for prod: "+prodNum;
                moveLogToFrom = "'MURRAY','LEHI'";
            }
            //moveLocation == 1 --> Murray to Orem
            else if(moveLocation.equals( "1" )){
                sql = "update ProductsLocationCount set MURRAY=MURRAY-?, OREM=OREM+? where productNum=?";
                message = moveCount + " from Murray to Orem for prod: "+prodNum;
                moveLogToFrom = "'MURRAY','OREM'";
            }
            //moveLocation == 2 --> Orem to Lehi
            else if(moveLocation.equals( "2" )){
                sql = "update ProductsLocationCount set OREM=OREM-?, LEHI=LEHI+? where productNum=?";
                message = moveCount + " from Orem to Lehi for prod: "+prodNum;
                moveLogToFrom = "'OREM','LEHI'";
            }
            //moveLocation == 3 --> Orem to Murray
            else if(moveLocation.equals( "3" )){
                sql = "update ProductsLocationCount set OREM=OREM-?, MURRAY=MURRAY+? where productNum=?";
                message = moveCount + " from Orem to Murray for prod: "+prodNum;
                moveLogToFrom = "'OREM','MURRAY'";
            }
            //moveLocation == 4 --> Lehi to Murray
            else if(moveLocation.equals( "4" )){
                sql = "update ProductsLocationCount set LEHI=LEHI-?, MURRAY=MURRAY+? where productNum=?";
                message = moveCount + " from Lehi to Murray for prod: "+prodNum;
                moveLogToFrom = "'LEHI','MURRAY'";
            }
            //moveLocation == 5 --> Lehi to Orem
            else if(moveLocation.equals( "5" )){
                sql = "update ProductsLocationCount set LEHI=LEHI-?, OREM=OREM+? where productNum=?";
                message = moveCount + " from Lehi to Orem for prod: "+prodNum;
                moveLogToFrom = "'LEHI','OREM'";
            }
            moveLog = "insert into productMoves values(null, now(),'"+username+"','"+moveCount+"',"+moveLogToFrom+",'"+prodNum+"')";
            message = "Successfully moved " + message;
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setInt(1, add);
            pstmt.setInt(2, add);
            pstmt.setInt(3, pn);
            pstmt.executeUpdate();
            //update move logs
            con = db.openConnection();
            pstmt = con.prepareStatement( moveLog );
            pstmt.executeUpdate();

            pstmt.close();
            con.close();
        }catch(Exception e){
            message = "Input: "+moveCount+" is an invalid number! Update failed.";
            System.out.println("Add: "+moveCount);
            System.out.println("ProductNum: "+prodNum);
            System.out.println("SQL: "+sql);
            e.printStackTrace();
        }
        return message;
    }

    public static String getCatElement(HashMap<String, String> hm, String str, int x){
        String name = "";
        String key = "";
        try{
            key = str.charAt(x)+"";
            name = (String)hm.get(key);
        }catch(Exception e){
            //e.printStackTrace();
        }
        return name;
    }

    public static ArrayList<Product> sortList(ArrayList<Product>prodList, String sortBy ){
        if(sortBy.equals( "quantity" )){
            return ProductsLocationCountUtil.sortByTotalCount(prodList);

        }
        return prodList;
    }

    public static boolean consolidateProducts(String prodToDelete, String prodToStay){
        //update transactions set prodNum = prodToStay where prodNum = prodToDelete
        boolean success = true;

        String errorSql = "";
        Connection con = null;
        MySQL sdb = new MySQL();
        ArrayList<String> sqlList = new ArrayList<String>();
        sqlList.add( "update Transactions set productNum="+prodToStay+" where productNum="+prodToDelete);
        for(LocationsDBName lname: LocationsDBName.values()){
            sqlList.add( "delete from "+lname.dbName()+" where productNum="+prodToDelete);
        }
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            for(String sql: sqlList){
                errorSql = sql;
                pstmt = con.prepareStatement( sql );
                pstmt.executeUpdate();
            }
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            success = false;
            System.out.println("SQL in productUtils.consolidateProducts: "+errorSql);
            e.printStackTrace();
        }
        return success;
    }

    public static String save( Product product) {
        String errorSql = "";
        Connection con = null;
        MySQL sdb = new MySQL();
        String result = "Successfully updated : " + product.getProductNum();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-mm-dd" );
        String date = sdf.format( cal.getTime() );
        if(StringUtils.isBlank(product.getProductNum())){
            return (ProductUtils.saveNewToAllLocations(product, "0", "0", "0"))?result:"Error occurred creating a new product-->creating productsLocationCount entry.";
        }
        String sql = "update Products set "
                + "productName=         ?," //+ product.getProductName()
                + "productAuthor=       ?," //+ product.getProductAuthor()
                + "productArtist=       ?," //+ product.getProductArtist()
                + "productArranger=     ?," //+ product.getProductArranger()
                + "productDescription=  ?," //+ product.getProductDescription()
                + "category=            ?," //+ product.getCategory()
                + "productCost1=        ?," //+ product.getProductCost1()
                + "productCost2=        ?," //+ product.getProductCost2()
                + "productCost3=        ?," //+ product.getProductCost3()
                + "productCost4=        ?," //+ product.getProductCost4()
                + "productBarCode=      ?," //+ product.getProductBarCode()
                + "productCatalogNum=   ?," //+ product.getProductCatalogNum()
                + "productSupplier1=    ?," //+ product.getProductSupplier1()
                + "productSupplier2=    ?," //+ product.getProductSupplier2()
                + "productSupplier3=    ?," //+ product.getProductSupplier3()
                + "productSupplier4=    ? ";//+ product.getProductSupplier4()
        sql += " where productNum = ?";//" + product.getProductNum() + " ";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            errorSql = sql;
            pstmt = con.prepareStatement( sql );
            pstmt.setString( 1,   product.getProductName()       );
            pstmt.setString( 2,   product.getProductAuthor()     );
            pstmt.setString( 3,   product.getProductArtist()     );
            pstmt.setString( 4,   product.getProductArranger()   );
            pstmt.setString( 5,   product.getProductDescription());
            pstmt.setString( 6,   product.getCategory()          );
            pstmt.setDouble( 7,   Utils.parseDouble(product.getProductCost1()  )   );
            pstmt.setDouble( 8,   Utils.parseDouble( product.getProductCost2()  )   );
            pstmt.setDouble( 9,   Utils.parseDouble( product.getProductCost3()  )   );
            pstmt.setDouble( 10,  Utils.parseDouble( product.getProductCost4()  )   );
            pstmt.setString( 11,  product.getProductBarCode()    );
            pstmt.setString( 12,  product.getProductCatalogNum() );
            pstmt.setInt( 13,  Utils.parseInt(product.getProductSupplier1() ) );
            pstmt.setInt( 14,  Utils.parseInt(product.getProductSupplier2() ) );
            pstmt.setInt( 15,  Utils.parseInt(product.getProductSupplier3() ) );
            pstmt.setInt( 16,  Utils.parseInt(product.getProductSupplier4() ) );
            pstmt.setInt( 17,  Utils.parseInt(product.getProductNum()       ) );
            pstmt.executeUpdate();

            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            System.out.println("ERROR SQL in productUtils.save: "+errorSql);
            System.out.println(product.toString());
            result = "error occured: " + e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    /**
     * gets total sold for product for 5 years back and returns hash<String year, String total>
     * @param productNum
     * @param location
     * @return
     */
    public static HashMap<String, String> getProductYearlySold(String productNum, String location){
        Calendar now = Calendar.getInstance();
        HashMap<String, String> yearlySales = new HashMap<String, String>();
        Connection con = null;
        int totalSales = 0;
        MySQL sdb = new MySQL();
        int year = now.get( Calendar.YEAR );
        String exclude = "";
        for(LocationsDBName ll: LocationsDBName.values()){
            if(location.equals( ll.name() )){
                exclude += ll.account();
                break;
            }
        }
        try {
            con = sdb.getConn();
            for(int i = 0; i<4; i++){
                String sql =
                        "select trans.invoiceNum, trans.transType, trans.productQty, trans.transDate "+
                                "from Transactions trans, Invoices inv "+
                                "where trans.invoiceNum = inv.invoiceNum and accountNum  "+
                                "= ("+exclude+")  and  trans.productNum="+productNum+" " +
                                "and transDate like '%"+(year-i)+"%' order by transType, transDate";
                PreparedStatement pstmt = null;
                //System.out.println(sql);
                pstmt = con.prepareStatement( sql );
                ResultSet rset = pstmt.executeQuery();
                int yearTotal = 0;
                while ( rset.next() ) {
                    try{
                        yearTotal += Utils.parseInt( rset.getString( "productQty" ) );
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                yearlySales.put( year-i+"", yearTotal+"" );
                totalSales += yearTotal;
                pstmt.close();
            }
            yearlySales.put( "Total", totalSales+"" );
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return yearlySales;

    }

    public static String updateLastInvDate( String prodNum, String location ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String prodLocation = location;// .equalsIgnoreCase( "lehi" ) ?
        String result = "Successfully update last inv date for productNumber: " + prodNum;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        String date = sdf.format( cal.getTime() );

        String sql = "update " + prodLocation + " set lastInvDate = convert(datetime, '" + date + "') where productNum = " + prodNum + " ";
        System.out.println(sql);
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            //pstmt.executeUpdate();

            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            result = "error occured: " + e.getMessage();
            System.out.println( "SQL: " + sql );
            e.printStackTrace();
        }
        return result;
    }

    public static List<Product> getProducts(List<Long> pIds) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Products where productNum in ("+pIds.toString().replace("[", "").replace("]", "")+")";
        ArrayList<Product> pList = new ArrayList<Product>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset =  pstmt.executeQuery();
            pList = getForRSet(rset, null);
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return pList;
    }

    public static ArrayList<Product> getAstricsList( ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Products where productName like '%`%'";
        ArrayList<Product> pList = new ArrayList<Product>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset =  pstmt.executeQuery();
            pList = getForRSet(rset, "MURRAY");
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return pList;
    }

    public static ArrayList<Product> getProducts(String searchType, String productNumOffSet) {
        Connection con = null;
        ArrayList<Product> pList = new ArrayList<Product>();
        MySQL sdb = new MySQL();
        productNumOffSet = StringUtils.isBlank( productNumOffSet )?"0":productNumOffSet;
        if(StringUtils.trimToEmpty( searchType ).equals( "missingEvents" )){
            String sql = "select * from Products where substring(category, 2, 4) = '0000' " +
                    "and productNum>"+productNumOffSet+" limit 25";
            try {
                con = sdb.getConn();
                PreparedStatement pstmt = null;
                pstmt = con.prepareStatement( sql );
                ResultSet rset =  pstmt.executeQuery();
                pList = getForRSet(rset, null);
                pstmt.close();
                con.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        return pList;
    }


    /**
     * update a products given column with the newvalue
     * @param columnName
     * @param newValue
     * @param productNum
     * @param location
     * @param isNewValueString
     */
    public static void updateProductForColumnName(String columnName, String newValue, String productNum, String location, boolean isNewValueString){
        Connection con = null;
        MySQL sdb = new MySQL();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-mm-dd" );
        String date = sdf.format( cal.getTime() );
        String sql = "update "+location+" set "+columnName+" = ? "+
                "where productNum = " + productNum;

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            System.out.println(sql);
            pstmt = con.prepareStatement( sql );
            if(isNewValueString){
                pstmt.setString( 1, newValue );
            }else{
                pstmt.setInt( 1, Utils.parseInt( newValue ));
            }
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
    public void removeAstrics(){
        ArrayList<Product> pList = ProductUtils.getAstricsList();
        for(Product prod: pList){
            System.out.println(prod.getProductName()+"  "+prod.getProductName().replace( "`", "'" ));
            ProductUtils.updateProductForColumnName( "productName", prod.getProductName().replace( "`", "'" ),
                    prod.getProductNum(), "Products", true );
        }
    }

    public static String updateAvailableCount( String newCount, String prodNum, String location ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String result = "Added "+newCount+" to" + prodNum +" for "+location;
        String sql = "update " + location + " set numAvailable = " + newCount + " where productNum = " + prodNum + " ";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.executeUpdate();

            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            result = "error occured: " + e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    public static Product fetchProductForNum( String prodNum, String location ) {
        return fetchProductForColumn(ProductColsEnum.NUM, prodNum, location);
    }

    public static Product fetchProductForColumn( ProductColsEnum pEnum, String prodNum, String location ) {
        String prodLocation = location;// .equalsIgnoreCase( "lehi" ) ?
        Product prod = new Product();
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Products where "+pEnum.getLabel()+" = '" + prodNum + "' ";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            ArrayList<Product> prodList = getForRSet(rset, location);
            prod = prodList.isEmpty()?null:prodList.get(0);
            pstmt.close();
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return prod;
    }

    public static List<Product> fetchProductsForProdNums( String prodNums, String location ) {
        String prodLocation = location;// .equalsIgnoreCase( "lehi" ) ?
        List<Product> pList = new ArrayList<Product>();
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Products where productNum in (" + prodNums + ") ";
        try {
            //System.out.println(sql);
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            pList = getForRSet(rset, location);
            pstmt.close();
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return pList;
    }

    public static ArrayList<Product> fetchProductForColumn( String column, String value, String location, boolean isValueInt ) {
        String prodLocation = location;// .equalsIgnoreCase( "lehi" ) ?
        Product prod = new Product();
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Products where "+column+ " = ";;
        sql += isValueInt? value:"'" + value + "' ";
        ArrayList<Product> prodList = null;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            prodList = getForRSet(rset, location);
            pstmt.close();
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return prodList;
    }

    public static ArrayList<Product> getSupplierList( String value ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select productNum from Products where productSupplier1 = "+value;
        ArrayList<Product> prodList = new ArrayList<Product>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            while(rset.next()){
                Product prod = new Product();
                prod.setProductNum(rset.getString("productNum"));
                prodList.add(prod);
            }
            pstmt.close();
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return prodList;
    }

    private static ArrayList<Product> getForRSet(ResultSet rset, String location) throws Exception {
        return getForRSet(rset, location, true);
    }

    private static ArrayList<Product> getForRSet(ResultSet rset, String location, boolean setChildObject) throws Exception {
        ArrayList<Product> prodList = new ArrayList<Product>();
        while(rset.next()){
            Product prod = new Product();
            prod.setProductNum( rset.getString( "productNum" ) );
            prod.setProductName( rset.getString( "productName" ) );
            prod.setProductAuthor( rset.getString( "productAuthor" ) );
            prod.setProductArtist( rset.getString( "productArtist" ) );
            prod.setProductArranger( rset.getString( "productArranger" ) );
            prod.setProductDescription( rset.getString( "productDescription" ) );
            prod.setCategory( rset.getString( "category" ) );
            prod.setProductCost1( rset.getString( "productCost1" ) );
            prod.setProductCost2( rset.getString( "productCost2" ) );
            prod.setProductCost3( rset.getString( "productCost3" ) );
            prod.setProductCost4( rset.getString( "productCost4" ) );
            prod.setProductBarCode( rset.getString( "productBarCode" ) );
            prod.setProductSKU( rset.getString( "productSKU" ) );
            prod.setProductCatalogNum( rset.getString( "productCatalogNum" ) );
            prod.setProductSupplier1( rset.getString( "productSupplier1" ) );
            prod.setProductSupplier2( rset.getString( "productSupplier2" ) );
            prod.setProductSupplier3( rset.getString( "productSupplier3" ) );
            prod.setProductSupplier4( rset.getString( "productSupplier4" ) );
            prod.setWeight( rset.getString( "weight" ) );
            if(!StringUtils.isBlank( location ))
                prod.setNumAvailable( ProductsLocationCountUtil.getForProdNum(prod.getProductNum()).getLocation(location)+"" );
            try{prod.setLastSold( rset.getString( "lastSold" ) );}catch(Exception e){/*java.sql.SQLException: Cannot convert value '0000-00-00 00:00:00' from column 20 to TIMESTAMP.*/}
            prod.setLastInvDate( rset.getString( "lastInvDate" ) );
            prod.setDCCatalogNum( rset.getString( "DCCatalogNum" ) );
            if(setChildObject) {
                prod.setSupplier(SupplierUtil.getSupplierForNum(rset.getString("productSupplier1")));
            }
            prodList.add(prod);
        }
        return prodList;
    }

    public static Product fetchProductForString( String prodNum, String location ) {
        String prodLocation = location;// .equalsIgnoreCase( "lehi" ) ?
        Product prod = null;
        Connection con = null;
        MySQL sdb = new MySQL();

        String sql = "select * from " + prodLocation + " where productNum = '" + prodNum + "' ";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            if ( rset.next() ) {
                prod = new Product();;
                prod.setProductNum( rset.getString( "productNum" ) );
                prod.setProductName( rset.getString( "productName" ) );
                prod.setProductAuthor( rset.getString( "productAuthor" ) );
                prod.setProductArtist( rset.getString( "productArtist" ) );
                prod.setProductArranger( rset.getString( "productArranger" ) );
                prod.setProductDescription( rset.getString( "productDescription" ) );
                prod.setCategory( rset.getString( "category" ) );
                prod.setProductCost1( rset.getString( "productCost1" ) );
                prod.setProductCost2( rset.getString( "productCost2" ) );
                prod.setProductCost3( rset.getString( "productCost3" ) );
                prod.setProductCost4( rset.getString( "productCost4" ) );
                prod.setProductBarCode( rset.getString( "productBarCode" ) );
                prod.setProductSKU( rset.getString( "productSKU" ) );
                prod.setProductCatalogNum( rset.getString( "productCatalogNum" ) );
                prod.setProductSupplier1( rset.getString( "productSupplier1" ) );
                prod.setProductSupplier2( rset.getString( "productSupplier2" ) );
                prod.setProductSupplier3( rset.getString( "productSupplier3" ) );
                prod.setProductSupplier4( rset.getString( "productSupplier4" ) );
                prod.setNumAvailable( rset.getString( "numAvailable" ) );
                prod.setLastSold( rset.getString( "lastSold" ) );
                prod.setLastInvDate( rset.getString( "lastInvDate" ) );
                prod.setDCCatalogNum( rset.getString( "DCCatalogNum" ) );
                prod.setSupplier( SupplierUtil.getSupplierForNum( rset.getString( "productSupplier1" ) ) );

            }
            pstmt.close();
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return prod;
    }
    public static String fetchLastPOForProdNum( String prodNum ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String maxDate = "";
        String sql = "select max(transDate) mdate from Transactions where transType = 'PO' and productNum = " + prodNum ;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            if ( rset.next() ) {
                maxDate = ( rset.getString( "mdate" ) );
            }
            pstmt.close();
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return maxDate;
    }
    public static String updateLastInv( String prodNum ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String maxDate = "";
        String sql = "update Products set lastInvDate = getdate() where productNum = " + prodNum ;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return "Successfully updated the lastinv date for prod#: "+prodNum;
    }
    public static String updateLastDO( String prodNum ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String maxDate = "";
        String sql = "update Products set lastDODte = getdate() where productNum = " + prodNum ;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return "Successfully updated the lastDO date for prod#: "+prodNum;
    }
    public static List<Product> searchProductsForCatalog(String catalog){
        Connection con = null;
        MySQL sdb = new MySQL();
        try {
            String sql = "select * from Products where productCatalogNum like '"+catalog.replaceAll( "'", "''" )+"%' limit 20";
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            pstmt = con.prepareStatement(sql);
            rset = pstmt.executeQuery();
            return getForRSet(rset, null, false);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Product> searchProduct(String DMM, String catalog, String barCode,
                                                   String name, String artist, String author, String arranger, String description,
                                                   String productSupplier1, String productNum, String descriptions, String department
            , String events1, String events2, String events3
            , String events4, String idList){
        return searchProduct( DMM,  catalog,  barCode,
                name,  artist,  author,  arranger,  description,
                productSupplier1,  productNum,  descriptions,  department
                ,  events1,  events2,  events3
                ,  events4,  idList, null);

    }
    public static ArrayList<Product> searchProduct(String DMM, String catalog, String barCode,
                                                   String name, String artist, String author, String arranger, String description,
                                                   String productSupplier1, String productNum, String descriptions, String department
            , String events1, String events2, String events3
            , String events4, String idList, String productSupplier1Num){

        DMM = DMM.replaceAll( "'", "''" );
        catalog = catalog.replaceAll( "'", "''" );
        barCode = barCode.replaceAll( "'", "''" );
        name = name.replaceAll( "'", "''" );
        artist = artist.replaceAll( "'", "''" );
        author = author.replaceAll( "'", "''" );
        arranger = arranger.replaceAll( "'", "''" );
        description = description.replaceAll( "'", "''" );

        ArrayList<Product> prodList = new ArrayList<Product>();
        Connection con = null;
        boolean pidIsInt = true;
        MySQL sdb = new MySQL();
        String sql = "select * from Products ";
        String prep = "where";

        if ( !StringUtils.isBlank( productNum )){
            sql += prep+" productNum = '" + productNum + "' ";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( catalog )){
            sql += prep+" productCatalogNum = '" + catalog.toUpperCase() + "'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( DMM )){
            sql += prep+" productSKU = '" + DMM.toUpperCase() + "'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( barCode )){
            sql += prep+" productBarCode = '" + barCode.toUpperCase() + "'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( name )){
            sql += prep+" upper(productName) like '%" + name.toUpperCase() + "%'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( artist )){
            sql += prep+" upper(productArtist) like '%" + artist.toUpperCase() + "%'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( author )){
            sql += prep+" upper(productAuthor) like '%" + author.toUpperCase() + "%'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( arranger )){
            sql += prep+" upper(productArranger) like '%" + arranger.toUpperCase() + "%'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( description )){
            sql += prep+" upper(productDescription) like '%" + description.toUpperCase() + "%'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( productSupplier1 )){
            sql += prep+" productSupplier1 in (select supplierNum from Suppliers where supplierName like '%" + productSupplier1.toUpperCase() + "%') ";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( productSupplier1Num )){
            sql += prep+" productSupplier1 =" + productSupplier1Num;
            prep = " and ";
        }
        // EVENTS ###############################################
        if ( events1!= null && events1.length() > 0 ) {
            sql += prep+"  category like '%" + events1 + "%'";
            if(StringUtils.isBlank( descriptions ) || !descriptions.equals( events1 )){
                sql += " and category not like '%"+events1+"' ";
            }
            if(StringUtils.isBlank( department ) || !department.equals( events1 )){
                sql += " and category not like '"+events1+"%' ";
            }
            prep = " and ";
        }
        if ( events2!= null && events2.length() > 0 ) {
            sql += prep+"  category like '%" + events2 + "%'";
            if(StringUtils.isBlank( descriptions ) || !descriptions.equals( events2 )){
                sql += " and category not like '%"+events2+"' ";
            }
            if(StringUtils.isBlank( department ) || !department.equals( events2 )){
                sql += " and category not like '"+events2+"%' ";
            }
            prep = " and ";
        }
        if ( events3!= null && events3.length() > 0 ) {
            sql += prep+"  category like '%" + events3 + "%'";
            if(StringUtils.isBlank( descriptions ) || !descriptions.equals( events3 )){
                sql += " and category not like '%"+events3+"' ";
            }
            if(StringUtils.isBlank( department ) || !department.equals( events3 )){
                sql += " and category not like '"+events3+"%' ";
            }
            prep = " and ";
        }
        if ( events4!= null && events4.length() > 0 ) {
            sql += prep+"  category like '%" + events4 + "%'";
            if(StringUtils.isBlank( descriptions ) || !descriptions.equals( events4 )){
                sql += " and category not like '%"+events4+"' ";
            }
            if(StringUtils.isBlank( department ) || !department.equals( events4 )){
                sql += " and category not like '"+events4+"%' ";
            }
            prep = " and ";
        }
        // EVENTS ###############################################
        if (department != null &&department.length() > 0 && !department.equals("0") ) {
            sql += prep+"  category like '" + department + "%'";
            prep = " and ";
        }
        if ( descriptions!= null &&descriptions.length() > 0 ) {
            sql += prep+"  category like '%" + descriptions + "'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( idList )) {
            sql += prep+"  productNum in (" + idList + ") ";
            prep = " and ";
        }
        if(prep.equals( " and " )){
            sql = sql+" limit 25 order by productName ";
            System.out.println("SQL: "+sql);
            try {
                con = sdb.getConn();
                PreparedStatement pstmt = null;
                ResultSet rset = null;
                pstmt = con.prepareStatement( sql );
                rset = pstmt.executeQuery();
                while ( rset.next() ) {
                    Product prod = new Product();

                    prod.setProductNum( rset.getString( "productNum" ) );
                    prod.setProductName( rset.getString( "productName" ) );
                    prod.setProductAuthor( rset.getString( "productAuthor" ) );
                    prod.setProductArtist( rset.getString( "productArtist" ) );
                    prod.setProductArranger( rset.getString( "productArranger" ) );
                    prod.setProductDescription( rset.getString( "productDescription" ) );
                    prod.setCategory( rset.getString( "category" ) );
                    prod.setProductCost1( rset.getString( "productCost1" ) );
                    prod.setProductCost2( rset.getString( "productCost2" ) );
                    prod.setProductCost3( rset.getString( "productCost3" ) );
                    prod.setProductCost4( rset.getString( "productCost4" ) );
                    prod.setProductBarCode( rset.getString( "productBarCode" ) );
                    prod.setProductSKU( rset.getString( "productSKU" ) );
                    prod.setProductCatalogNum( rset.getString( "productCatalogNum" ) );
                    prod.setProductSupplier1( rset.getString( "productSupplier1" ) );
                    prod.setProductSupplier2( rset.getString( "productSupplier2" ) );
                    prod.setProductSupplier3( rset.getString( "productSupplier3" ) );
                    prod.setProductSupplier4( rset.getString( "productSupplier4" ) );
                    prod.setNumAvailable( rset.getString( "numAvailable" ) );
                    prod.setLastSold( rset.getString( "lastSold" ) );
                    prod.setLastInvDate( rset.getString( "lastInvDate" ) );
                    prod.setLastDODte( rset.getString( "lastDODte" ) );
                    prod.setDCCatalogNum( rset.getString( "DCCatalogNum" ) );
                    prod.setLocation( "Products" );
                    //                     prodListHash.put( prod.getProductNum(), prod );
                    prodList.add( prod );

                }
                if(pstmt!=null)
                    pstmt.close();
                con.close();
            } catch ( Exception e ) {
                System.out.println("ERROR SQL: "+sql   );
                e.printStackTrace();
            }
        }
        // put resultset in a hash map to remove dups
        // then put hashmap into an arraylist...dont ask me why:)
        //         Set set = prodListHash.keySet();
        //         Iterator<String> iter = set.iterator();
        //         while ( iter.hasNext() ) {
        //             Product prod = prodListHash.get( iter.next() );
        //             prodList.add( prod );
        //         }
        return prodList;
    }
    public static ArrayList<Product> searchProductForIDs(String idList){
        ArrayList<Product> prodList = new ArrayList<Product>();
        if ( StringUtils.isBlank( idList )) {
            return prodList;
        }
        Connection con = null;
        boolean pidIsInt = true;
        MySQL sdb = new MySQL();
        String sql = "select wpi.productFeature prodFeature, p.* from Products p\n ";
        sql += "left join WebProductInfo wpi \n" +
                "on p.productNum = wpi.productNum\n" +
                "where p.productNum in (" + idList + ") \n" +
                "order by productName limit 25";
        //System.out.println("SQL: "+sql);
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            pstmt = con.prepareStatement( sql );
            rset = pstmt.executeQuery();
            while ( rset.next() ) {
                Product prod = new Product();

                prod.setProductNum( rset.getString( "productNum" ) );
                prod.setProductName( rset.getString( "productName" ) );
                prod.setProductAuthor( rset.getString( "productAuthor" ) );
                prod.setProductArtist( rset.getString( "productArtist" ) );
                prod.setProductArranger( rset.getString( "productArranger" ) );
                prod.setProductDescription( rset.getString( "productDescription" ) );
                prod.setCategory( rset.getString( "category" ) );
                prod.setProductCost1( rset.getString( "productCost1" ) );
                prod.setProductCost2( rset.getString( "productCost2" ) );
                prod.setProductCost3( rset.getString( "productCost3" ) );
                prod.setProductCost4( rset.getString( "productCost4" ) );
                prod.setProductBarCode( rset.getString( "productBarCode" ) );
                prod.setProductSKU( rset.getString( "productSKU" ) );
                prod.setProductCatalogNum( rset.getString( "productCatalogNum" ) );
                prod.setProductSupplier1( rset.getString( "productSupplier1" ) );
                prod.setProductSupplier2( rset.getString( "productSupplier2" ) );
                prod.setProductSupplier3( rset.getString( "productSupplier3" ) );
                prod.setProductSupplier4( rset.getString( "productSupplier4" ) );
                prod.setNumAvailable( rset.getString( "numAvailable" ) );
                prod.setLastSold( rset.getString( "lastSold" ) );
                prod.setLastInvDate( rset.getString( "lastInvDate" ) );
                prod.setLastDODte( rset.getString( "lastDODte" ) );
                prod.setDCCatalogNum( rset.getString( "DCCatalogNum" ) );
                prod.setLocation( rset.getString( "prodFeature"  ));
                //                     prodListHash.put( prod.getProductNum(), prod );
                prodList.add( prod );

            }
            if(pstmt!=null)
                pstmt.close();
            con.close();
        } catch ( Exception e ) {
            System.out.println("ERROR SQL: "+sql   );
            e.printStackTrace();
        }
        return prodList;
    }

    public static String deleteProdForNum( String num, String location ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "delete from " + location + " where productNum = " + num;
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
            return "Successfully deleted product with prodNum: " + num;
        } catch ( Exception e ) {
            e.printStackTrace();
            return "Failed to delete product";
        }
    }


    public static ArrayList<String> getIDList(String DMM, String catalog, String barCode, String name, String artist,
                                              String author, String arranger, String description, String productSupplier1,
                                              String productNum, String descriptions, String department,
                                              String events1, String events2, String events3,
                                              String events4, String sortBy){
        DMM = DMM.replaceAll( "'", "''" );
        catalog = catalog.replaceAll( "'", "''" );
        barCode = barCode.replaceAll( "'", "''" );
        name = name.replaceAll( "'", "''" );
        artist = artist.replaceAll( "'", "''" );
        author = author.replaceAll( "'", "''" );
        arranger = arranger.replaceAll( "'", "''" );
        description = description.replaceAll( "'", "''" );

        ArrayList<String> prodList = new ArrayList<String>();
        Connection con = null;
        boolean pidIsInt = true;
        boolean quantitySort = false;
        MySQL sdb = new MySQL();
        //ArrayList<String> sqlList = new ArrayList<String>();
        if(sortBy.equals( "quantity" )){
            quantitySort = true;
        }


        String prep = " and ";
        String sql = "(select productNum from Products prod ";
        if(quantitySort){
            sql = " (select prod.productNum, (OREM+LEHI+MURRAY) as tot" +
                    " from Products prod, ProductsLocationCount plc where plc.productNum = prod.productNum ";
        }
        else{
            prep = "where";
        }

        if ( !StringUtils.isBlank( productNum )){
            sql += prep+" productNum = '" + productNum + "' ";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( catalog )){
            sql += prep+" productCatalogNum = '" + catalog.toUpperCase() + "'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( DMM )){
            sql += prep+" productSKU = '" + DMM.toUpperCase() + "'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( barCode )){
            sql += prep+" productBarCode = '" + barCode.toUpperCase() + "'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( name )){
            sql += prep+" upper(productName) like '%" + name.toUpperCase() + "%'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( artist )){
            sql += prep+" upper(productArtist) like '%" + artist.toUpperCase() + "%'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( author )){
            sql += prep+" upper(productAuthor) like '%" + author.toUpperCase() + "%'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( arranger )){
            sql += prep+" upper(productArranger) like '%" + arranger.toUpperCase() + "%'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( description )){
            sql += prep+" upper(productDescription) like '%" + description.toUpperCase() + "%'";
            prep = " and ";
        }
        if ( !StringUtils.isBlank( productSupplier1 )){
            sql += prep+" productSupplier1 in (select supplierNum from Suppliers where supplierName like '%" + productSupplier1.toUpperCase() + "%') ";
            prep = " and ";
        }
        // EVENTS ###############################################
        if ( events1!= null && events1.length() > 0 ) {
            sql += prep+"  category like '%" + events1 + "%'";
            if(StringUtils.isBlank( descriptions ) || !descriptions.equals( events1 )){
                sql += " and category not like '%"+events1+"' ";
            }
            if(StringUtils.isBlank( department ) || !department.equals( events1 )){
                sql += " and category not like '"+events1+"%' ";
            }
            prep = " and ";
        }
        if ( events2!= null && events2.length() > 0 ) {
            sql += prep+"  category like '%" + events2 + "%'";
            if(StringUtils.isBlank( descriptions ) || !descriptions.equals( events2 )){
                sql += " and category not like '%"+events2+"' ";
            }
            if(StringUtils.isBlank( department ) || !department.equals( events2 )){
                sql += " and category not like '"+events2+"%' ";
            }
            prep = " and ";
        }
        if ( events3!= null && events3.length() > 0 ) {
            sql += prep+"  category like '%" + events3 + "%'";
            if(StringUtils.isBlank( descriptions ) || !descriptions.equals( events3 )){
                sql += " and category not like '%"+events3+"' ";
            }
            if(StringUtils.isBlank( department ) || !department.equals( events3 )){
                sql += " and category not like '"+events3+"%' ";
            }
            prep = " and ";
        }
        if ( events4!= null && events4.length() > 0 ) {
            sql += prep+"  category like '%" + events4 + "%'";
            if(StringUtils.isBlank( descriptions ) || !descriptions.equals( events4 )){
                sql += " and category not like '%"+events4+"' ";
            }
            if(StringUtils.isBlank( department ) || !department.equals( events4 )){
                sql += " and category not like '"+events4+"%' ";
            }
            prep = " and ";
        }
        // EVENTS ###############################################
        if (department != null &&department.length() > 0 && !department.equals("0") ) {
            sql += prep+"  category like '" + department + "%'";
            prep = " and ";
        }
        if ( descriptions!= null &&descriptions.length() > 0 ) {
            sql += prep+"  category like '%" + descriptions + "'";
            prep = " and ";
        }
        if(quantitySort&&prep.equals( " and " )){
            sql += ") order by tot desc ";
        }
        else if(prep.equals( " and " )){
            sql = sql+") order by productName ";
        }
        //System.out.println("SQL: "+sql);
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            pstmt = con.prepareStatement( sql );
            rset = pstmt.executeQuery();
            String prodNum = "";
            int cc= 0;
            while ( rset.next() ) {
                prodNum += ( rset.getString( "productNum" ) )+",";
                cc++;
                if(cc>9){
                    prodNum = prodNum.substring( 0, prodNum.length()-1);
                    prodList.add( prodNum );
                    cc = 0;
                    prodNum = "";
                }

            }
            //get anything thats left
            if(!StringUtils.isBlank( prodNum )){
                prodNum = prodNum.substring( 0, prodNum.length()-1 );
                prodList.add( prodNum );
            }
            if(pstmt!=null)
                pstmt.close();
            con.close();
        } catch ( Exception e ) {
            System.out.println("ERROR SQL: "+sql   );
            e.printStackTrace();
        }

        //         for(String dd: prodList){
        //         System.out.println(dd);
        //         }
        return prodList;
    }
    public static String deleteProdForNumAllLocas( String num ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String mlocation = LocationsDBName.MURRAY.dbName();
        String message = "";
        //only have to check one location b/c all transactions prodnums will be the same
        String check  = "select productNum from Transactions " +
                "where productNum ="+num;
        //       +
        //       "or productNum in " +
        //       "(select productNum from "+llocation+" where productNum="+num+")";
        String msql = "delete from " + mlocation + " where productNum = " + num;

        try {
            message =  "Successfully delete product with prodNum: " + num;
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( check );
            ResultSet rset = pstmt.executeQuery();
            if(!rset.next()){
                pstmt = con.prepareStatement( msql );
                pstmt.executeUpdate();
            }else{
                message = "This product has history associated with it and cannot be deleted.";
            }
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
            message =  "Failed to delete product";
        }
        return message;
    }

    public static boolean saveNewToAllLocations( Product product, String lehiQty, String murrayQty, String oremQty ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        boolean saveResult = true;

        String sql = "insert into Products ";

        sql +=" ( productNum            , productName           , productAuthor         , "
                + "productArtist         , productArranger       , productDescription    , category              , "
                + "productCost1          , productCost2          , productCost3          , productCost4          , "
                + "productBarCode        , productSKU            , productCatalogNum     , productSupplier1      , "
                + "productSupplier2      , productSupplier3      , productSupplier4	  , lastInvDate      ) ";

        sql += "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            product.setProductNum(ProductUtils.getMaxNextProductNumber());
            product.setProductSKU( "DMM" + product.getProductNum());
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setInt   (  1,  Utils.parseInt(product.getProductNum()       ) );
            pstmt.setString(  2,  product.getProductName()       );
            pstmt.setString(  3,  product.getProductAuthor()     );
            pstmt.setString(  4,  product.getProductArtist()     );
            pstmt.setString(  5,  product.getProductArranger()   );
            pstmt.setString(  6,  product.getProductDescription());
            pstmt.setString(  7,  product.getCategory()          );
            pstmt.setDouble(  8,  Utils.parseDouble( product.getProductCost1()  )   );
            pstmt.setDouble(  9,  Utils.parseDouble( product.getProductCost2()  )   );
            pstmt.setDouble(  10, Utils.parseDouble( product.getProductCost3()  )   );
            pstmt.setDouble(  11, Utils.parseDouble( product.getProductCost4()  )   );
            pstmt.setString(  12, product.getProductBarCode()    );
            pstmt.setString(  13, product.getProductSKU() );
            pstmt.setString(  14, product.getProductCatalogNum() );
            pstmt.setInt   (  15, Utils.parseInt(product.getProductSupplier1() ) );
            pstmt.setInt   (  16, Utils.parseInt(product.getProductSupplier2() ) );
            pstmt.setInt   (  17, Utils.parseInt(product.getProductSupplier3() ) );
            pstmt.setInt   (  18, Utils.parseInt(product.getProductSupplier4() ) );
            String ddt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
            pstmt.setString(  19, ddt);
            pstmt.executeUpdate();

            pstmt.close();
            con.close();
            ProductsLocationCount plc = new ProductsLocationCount();
            plc.setLEHI(Utils.toInt(lehiQty));
            plc.setOREM(Utils.toInt(oremQty));
            plc.setMURRAY(Utils.toInt(murrayQty));
            plc.setProductNum(Utils.toInt(product.getProductNum()));
            ProductsLocationCountUtil.save(plc);
        } catch ( SQLException e ) {
            saveResult = false;
            System.out.println( sql );
            e.printStackTrace();

        }
        return saveResult;

    }


    private static String getMaxNextProductNumber( ) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select max(productNum) as productMax from Products";
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

    private static Product dupProduct( Product tempProd ) {
        Product prod = new Product();
        prod.setProductNum( tempProd.getProductNum() );
        prod.setProductName( tempProd.getProductName() );
        prod.setProductAuthor( tempProd.getProductAuthor() );
        prod.setProductArtist( tempProd.getProductArtist() );
        prod.setProductArranger( tempProd.getProductArranger() );
        prod.setProductDescription( tempProd.getProductDescription() );
        prod.setCategory( tempProd.getCategory() );
        prod.setProductCost1( tempProd.getProductCost1() );
        prod.setProductCost2( tempProd.getProductCost2() );
        prod.setProductCost3( tempProd.getProductCost3() );
        prod.setProductCost4( tempProd.getProductCost4() );
        prod.setProductBarCode( tempProd.getProductBarCode() );
        prod.setProductSKU( tempProd.getProductSKU() );
        prod.setProductCatalogNum( tempProd.getProductCatalogNum() );
        prod.setProductSupplier1( tempProd.getProductSupplier1() );
        prod.setProductSupplier2( tempProd.getProductSupplier2() );
        prod.setProductSupplier3( tempProd.getProductSupplier3() );
        prod.setProductSupplier4( tempProd.getProductSupplier4() );
        prod.setNumAvailable( tempProd.getNumAvailable() );
        prod.setLastSold( tempProd.getLastSold() );
        prod.setLastInvDate( tempProd.getLastInvDate() );
        prod.setDCCatalogNum( tempProd.getDCCatalogNum() );
        prod.setLocation( tempProd.getLocation() );
        return prod;
    }

    /**
     * fetch all products from murray table Products and put into hash<String
     * productNum, Product>
     *
     * @return
     */
    private HashMap<String, Product> fetchAllFromMurray() {
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Product";
        HashMap<String, Product> prodList = new HashMap<String, Product>();

        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            while ( rset.next() ) {
                Product prod = new Product();
                prod.setProductNum( rset.getString( "productNum" ) != null ? rset.getString( "productNum" ) : "" );
                prod.setProductName( rset.getString( "productName" ) );
                prod.setProductAuthor( rset.getString( "productAuthor" ) );
                prod.setProductArtist( rset.getString( "productArtist" ) );
                prod.setProductArranger( rset.getString( "productArranger" ) );
                prod.setProductDescription( rset.getString( "productDescription" ) );
                prod.setCategory( rset.getString( "category" ) );
                prod.setProductCost1( rset.getString( "productCost1" ) );
                prod.setProductCost2( rset.getString( "productCost2" ) );
                prod.setProductCost3( rset.getString( "productCost3" ) );
                prod.setProductCost4( rset.getString( "productCost4" ) );
                prod.setProductBarCode( rset.getString( "productBarCode" ) );
                prod.setProductSKU( rset.getString( "productSKU" ) );
                prod.setProductCatalogNum( rset.getString( "productCatalogNum" ) );
                prod.setProductSupplier1( rset.getString( "productSupplier1" ) );
                prod.setProductSupplier2( rset.getString( "productSupplier2" ) );
                prod.setProductSupplier3( rset.getString( "productSupplier3" ) );
                prod.setProductSupplier4( rset.getString( "productSupplier4" ) );
                prod.setNumAvailable( rset.getString( "numAvailable" ) );
                prod.setLastSold( rset.getString( "lastSold" ) );
                prod.setLastInvDate( rset.getString( "lastInvDate" ) );
                prod.setDCCatalogNum( rset.getString( "DCCatalogNum" ) );
                prodList.put( prod.getProductNum(), prod );

            }
            pstmt.close();
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return prodList;
    }

    public static List<Product> fetchSupProdsForStr(String prodStr, String supNum) {
        ArrayList<Product> prodList = new ArrayList<Product>();
        if ( StringUtils.isBlank( supNum )||
                StringUtils.isBlank( prodStr )) {
            return prodList;
        }
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = " ("+
                //		 " select * from Products where convert(varchar(12), productNum) like '"+prodStr.toUpperCase()+"%' and  productSupplier1 ="+supNum+
                //		 " union "+
                " select * from Products where upper(productCatalogNum) like '"+prodStr.toUpperCase()+"%' and  productSupplier1 ="+supNum+
                //		 " union "+
                //		 " select * from Products where productBarCode like '"+prodStr.toUpperCase()+"%' and  productSupplier1 ="+supNum+
                " union "+
                " select * from Products where  upper(productName) like '%"+prodStr.toUpperCase()+"%' and  productSupplier1 ="+supNum+") limit 10 order by productName";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            pstmt = con.prepareStatement( sql );
            rset = pstmt.executeQuery();
            while ( rset.next() ) {
                Product prod = new Product();
                prod.setProductNum( rset.getString( "productNum" ) );
                prod.setProductName(rset.getString("productName"));
                prod.setProductAuthor(rset.getString("productAuthor"));
                prod.setProductArtist(rset.getString("productArtist"));
                prod.setProductArranger(rset.getString("productArranger"));
                prod.setProductDescription(rset.getString("productDescription"));
                prod.setCategory(rset.getString("category"));
                prod.setProductCost1(rset.getString("productCost1"));
                prod.setProductCost2(rset.getString("productCost2"));
                prod.setProductCost3(rset.getString("productCost3"));
                prod.setProductCost4(rset.getString("productCost4"));
                prod.setProductBarCode(rset.getString("productBarCode"));
                prod.setProductSKU(rset.getString("productSKU"));
                prod.setProductCatalogNum(rset.getString("productCatalogNum"));
                prod.setProductSupplier1(rset.getString("productSupplier1"));
                prod.setProductSupplier2(rset.getString("productSupplier2"));
                prod.setProductSupplier3(rset.getString("productSupplier3"));
                prod.setProductSupplier4(rset.getString("productSupplier4"));
                prod.setNumAvailable(rset.getString("numAvailable"));
                prod.setLastSold(rset.getString("lastSold"));
                prod.setLastInvDate(rset.getString("lastInvDate"));
                prod.setLastDODte(rset.getString("lastDODte"));
                prod.setDCCatalogNum(rset.getString("DCCatalogNum"));
                prod.setWeight(rset.getString("weight"));
                prodList.add( prod );
            }
            if(pstmt!=null)
                pstmt.close();
            con.close();
        } catch ( Exception e ) {
            System.out.println("ERROR SQL: "+sql   );
            e.printStackTrace();
        }
        return prodList;
    }

    public static HashMap<String, String> fetchProductsForSupplier(String supNum) {
        HashMap<String, String> prodList = new HashMap<String, String>();
        if ( StringUtils.isBlank( supNum )) {
            return prodList;
        }
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select productNum, productCatalogNum from Products where productSupplier1 ="+supNum+" limit 200";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            pstmt = con.prepareStatement( sql );
            rset = pstmt.executeQuery();
            while(rset.next()){
                String one = rset.getString(1);
                String two = rset.getString(2);
                if(!StringUtils.isBlank(one)&& !StringUtils.isBlank(two))
                    prodList.put(one, two);
            }
            if(pstmt!=null)
                pstmt.close();
            con.close();
        } catch ( Exception e ) {
            System.out.println("ERROR SQL: "+sql   );
            e.printStackTrace();
        }
        return prodList;
    }

    public static List<Product> searchForItemNum(String itemNum, String supNum) {
        ArrayList<Product> prodList = new ArrayList<Product>();
        if ( StringUtils.isBlank( itemNum )) {
            return prodList;
        }
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select productNum, productCatalogNum from Products where productCatalogNum like '"+itemNum.toUpperCase()+"%' and productSupplier1 = "+supNum+"  limit 200";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            pstmt = con.prepareStatement( sql );
            rset = pstmt.executeQuery();
            while(rset.next()){
                Product prod = new Product();
                prod.setProductNum( rset.getString( "productNum" ) );
                prod.setProductCatalogNum( rset.getString( "productCatalogNum" ) );
                prodList.add(prod);
            }
            if(pstmt!=null)
                pstmt.close();
            con.close();
        } catch ( Exception e ) {
            System.out.println("ERROR SQL: "+sql   );
            e.printStackTrace();
        }
        return prodList;
    }

    public static List<Product> searchProducts(String searchStr) {
        ArrayList<Product> prodList = new ArrayList<Product>();
        if ( StringUtils.isBlank( searchStr )) {
            return null;
        }
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Products " +
                "where productCatalogNum like '"+searchStr+"%' " +
                "or productSupplier1 like '"+searchStr+"%' " +
                "or category like '"+searchStr+"%' " +
                "or DCCatalogNum like '"+searchStr+"%' " +
                "or productSKU like '"+searchStr+"%' " +
                "or productAuthor like '"+searchStr+"%' " +
                "or productArranger like '"+searchStr+"%' " +
                "or productArtist like '"+searchStr+"%' " +
                "or productName like '%"+searchStr+"%' " +
                "limit 25";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            pstmt = con.prepareStatement( sql );
            rset = pstmt.executeQuery();
            prodList = getForRSet(rset, null, false);
            if(pstmt!=null)
                pstmt.close();
            con.close();
        } catch ( Exception e ) {
            System.out.println("ERROR SQL: "+sql   );
            e.printStackTrace();
        }
        return prodList;
    }

    public static void updateProductWeight(String productNum, ProductWeight pw) {
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "";
        try {
            PreparedStatement pstmt = null;
            con = sdb.getConn();
            sql = "update Products set weight = ? where productNum=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, pw.name());
            pstmt.setInt(2, Integer.parseInt(productNum));
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private static void getHistoryForYear(List<Long> prodIds, String location, int year, Map<Long, ProductSold> map){
        String sql = "";
        try {
            Connection con = null;
            MySQL sdb = new MySQL();
            con = sdb.getConn();
            String pids = StringUtils.join(prodIds, ',');
            sql = "Select sum(trans.productQty) as count, trans.productNum from\n" +
                    " Transactions trans join InvoiceLocation invLoc \n" +
                    "   on trans.invoiceNum = invLoc.invoiceNum \n" +
                    "   where trans.productNum in ("+pids+") and invLoc.location = ? and year(transDate) = ? group by productNum";
            System.out.println();
            PreparedStatement pstmt = con.prepareStatement( sql );
            pstmt.setString(1, location);
            pstmt.setInt   (2, year);
            ResultSet rset = pstmt.executeQuery();
            while ( rset.next() ) {
                Long pId = Long.parseLong(rset.getString("productNum"));
                ProductSold ps = map.get(pId)!=null?map.get(pId):new ProductSold();
                ps.setId(pId);
                String c = rset.getString("count");
                ps.getYearsCount().put(new Long(year), new Long(c));
                map.put(pId, ps);
            }
            pstmt.close();
            con.close();
        }catch (Exception e){
            log.error("SQL: "+ sql);
            e.printStackTrace();
        }
    }

    public static Map<Long, ProductSold> fetchPastThreeYearsSold(List<Long> prodIds, String location){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        Map<Long, ProductSold> map = new HashMap<Long, ProductSold>();
        for(int i = 0; i<3; i++){
            getHistoryForYear(prodIds, location, year-i, map);
        }
        return map;
    }

    public static void main( String args[] ) {
//        int test = 744;
//        ProductUtils.fetchSupProdsForStr("chri", "27");
        //System.out.println((test/20)/20);
        //		 ArrayList<Product> prodList = getSupplierList("169");
        //		 for(Product prod: prodList){
        //			 System.out.println(prod.toString());
        //		 }
        //		 		 Product prod = ProductUtils.fetchProductForNum( "176494", "MURRAY" );
        //		 		 System.out.println(prod.getProductName());
        //		 		 prod.setProductName( prod.getProductName()+" 'test' yep' and' " );
        //ProductUtils.save( prod );
        //ProductUtils.saveNewToAllLocations(prod, "10", "20", "1" );
        //System.out.println(ProductUtils.fetchLastPOForProdNum("86907" ));
        Map map = ProductUtils.fetchPastThreeYearsSold(Arrays.asList(9263L,174893L), "MURRAY");
        System.out.println(map);

    }

}
