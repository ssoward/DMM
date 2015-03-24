package com.soward.service;

import com.soward.db.MySQL;
import com.soward.enums.TransTypeEnum;
import com.soward.object.Invoice;
import com.soward.object.Product;
import com.soward.object.Transaction;
import com.soward.util.InvoiceUtil;
import com.soward.util.ProductUtils;
import com.soward.util.TransUtil;
import com.soward.wsdl.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by ssoward on 12/20/14.
 */
public class MagentoSyncServiceImp {
    public static Logger log = Logger.getLogger("MagentoSyncServiceImp");
    public static String sma = "99";

    public static void main(String args[]){
        MagentoSyncServiceImp.syncMagento();
    }

    public static List<Invoice> syncMagento(){
        MagentoServiceLocator msl = new MagentoServiceLocator();
        List<Invoice> invoiceList = new ArrayList<Invoice>();
        try {
            log.info("Starting fetch of megento salesOrders: "+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()));
            Mage_Api_Model_Server_V2_HandlerPortType type = msl.getMage_Api_Model_Server_V2_HandlerPort();
            String login = type.login("DMM_API", "sheetmu1");

            AssociativeEntity ae = new AssociativeEntity();
            ae.setKey("gt");
            Long lastIncrementId = getLastIncrementId();
            ae.setValue(lastIncrementId.toString());
            ComplexFilter cf = new ComplexFilter();
            cf.setKey("increment_id");
            cf.setValue(ae);

            Filters filters = new Filters();
            ComplexFilter[] aeList = new ComplexFilter[1];
            aeList[0] = cf;
            filters.setComplex_filter(aeList);

            SalesOrderListEntity[] saleList = type.salesOrderList(login, filters);
            int count = 0;
            for(SalesOrderListEntity sale: saleList){
                SalesOrderEntity entity = type.salesOrderInfo(login, sale.getIncrement_id());
                Invoice inv = convertSalesOrderToInvoice(entity);
                inv = InvoiceUtil.saveInvoice(inv);
                List<Transaction> transList = new ArrayList<Transaction>();
                for(SalesOrderItemEntity item: entity.getItems()){
                    Transaction trans = convertSalesOrderItemToTransaction(item, inv.getInvoiceNum());
                    if(trans!=null && trans.getProductNum() != null) {
                        trans = TransUtil.saveTransaction(trans);
                        transList.add(trans);
                    }
                }
                inv.setTransList(transList);
                invoiceList.add(inv);
                saveSalesOrderEnitity(entity, inv.getInvoiceNum());
                count++;
            }
            log.info("Finished syncing "+count+" SMA invoices at: "+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()));
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return invoiceList;
    }

    private static Transaction convertSalesOrderItemToTransaction(SalesOrderItemEntity item, String invId) {
        Transaction trans = null;
        if(item != null){
            trans = new Transaction();
            trans.setInvoiceNum(invId);
            trans.setTransType(TransTypeEnum.SMA.code);
            Product prod = ProductUtils.fetchProductForNum(item.getProduct_id(), null);
            if(prod != null) {
                trans.setProductNum(prod.getProductNum());
                trans.setProductName(prod.getProductName());
            }
            trans.setProductQty(item.getQty_ordered());
            trans.setTransCost(item.getPrice());
            trans.setTransTax(item.getTax_amount());
            trans.setTransProductStatus("ORDERED");
            trans.setTransDate(item.getCreated_at());
            trans.setLocationNum(sma);
            trans.setUsername(sma);
            trans.setTransShipped("0");
        }
        return trans;
    }

    private static Invoice convertSalesOrderToInvoice(SalesOrderEntity en) {
        Invoice invoice = null;
        if(en != null) {

            en.getIncrement_id();
            invoice = new Invoice();
            invoice.setAccountNum(en.getCustomer_id()!=null?en.getCustomer_id():sma);
            invoice.setInvoiceDate(en.getCreated_at());
            invoice.setLocationNum(sma);
            invoice.setUsername2(sma);
            invoice.setInvoiceTotal(en.getTotal_invoiced());
            invoice.setInvoiceTax(en.getTax_amount());
            invoice.setInvoiceShipTotal(en.getShipping_amount());
            invoice.setInvoicePaid(en.getTotal_paid());
            invoice.setPaymentMethod1(en.getPayment().getPayment_id());
            invoice.setInvoicePaid1(en.getTotal_paid());
            invoice.setInvoiceReceivedBy("SMA");
        }
        return invoice;

    }

    private static boolean saveSalesOrderEnitity(SalesOrderEntity en, String invoiceId) {

        Connection con = null;
        MySQL db = new MySQL();
        boolean result = false;
        try {
            ObjectWriter ow = new ObjectMapper().writer();
            String entityJson = ow.writeValueAsString(en);
            con = db.getConn();
            String sql = "insert into sm_sales_order values (null,?,?,?,?,?,?,?,?,?,?,?,?,null, now(), ?)";
            PreparedStatement pstmt = con.prepareStatement( sql );
            pstmt.setString(1,  en.getIncrement_id());
            pstmt.setString(2,  en.getStore_id());
            pstmt.setString(3,  en.getCreated_at());
            pstmt.setString(4,  en.getCustomer_id());
            pstmt.setString(5,  en.getTax_amount());
            pstmt.setString(6,  en.getShipping_amount());
            pstmt.setString(7,  en.getDiscount_amount());
            pstmt.setString(8,  en.getSubtotal());
            pstmt.setString(9,  en.getGrand_total());
            pstmt.setString(10, en.getTotal_paid());
            pstmt.setString(11, en.getTotal_qty_ordered());
            pstmt.setString(12, entityJson);
            pstmt.setString(13, invoiceId);
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
            result = true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private static Long getLastIncrementId() {
        ArrayList<Invoice> invList = new ArrayList<Invoice>();
        Connection con = null;
        MySQL db = new MySQL();
        String sql = "select max(sm_id) mymax from sm_sales_order";

        try {
            con = db.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            if(rset.next()){
                return rset.getLong("mymax");
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return new Long(0);
    }
//    private static List<SalesOrderListEntity> getSalesOrderListEntityListFromReSet(ResultSet rset) throws SQLException {
//        List<SalesOrderListEntity> invList = new ArrayList<SalesOrderListEntity>();
//        while ( rset.next() ) {
//            SalesOrderListEntity inv = new SalesOrderListEntity();
//            invList.add( inv );
//        }
//        return invList;
//    }
}
