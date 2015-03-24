package com.soward.wsdl;


import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by ssoward on 11/11/14.
 */
public class MagentoSync {

    public static void main(String[] args) {
        MagentoSync magentoSync = new MagentoSync();
        magentoSync.getInvoices();
//        magentoSync.getProductAttributes();
    }

    public void getProductAttributes(){
        MagentoServiceLocator msl = new MagentoServiceLocator();
        try {
            Mage_Api_Model_Server_V2_HandlerPortType type = msl.getMage_Api_Model_Server_V2_HandlerPort();
            String log = type.login("DMM_API", "sheetmu1");

            CatalogAttributeEntity[] catList = type.catalogProductAttributeList(log, 4);

            for (CatalogAttributeEntity cat : catList) {
                System.out.println(cat.getCode());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void getInvoices(){
        MagentoServiceLocator msl = new MagentoServiceLocator();
        try {
            Mage_Api_Model_Server_V2_HandlerPortType type = msl.getMage_Api_Model_Server_V2_HandlerPort();
            String log = type.login("DMM_API", "sheetmu1");

            AssociativeEntity ae = new AssociativeEntity();
            ae.setKey("gt");
            ae.setValue("100000000");//todo get last increment_id
            ComplexFilter cf = new ComplexFilter();
            cf.setKey("increment_id");
            cf.setValue(ae);

            Filters filters = new Filters();
            ComplexFilter[] aeList = new ComplexFilter[1];
            aeList[0] = cf;
            filters.setComplex_filter(aeList);

            SalesOrderListEntity[] saleList = type.salesOrderList(log, filters);
            int count = 1;
            for(SalesOrderListEntity sale: saleList){
                SalesOrderEntity entity = type.salesOrderInfo(log, sale.getIncrement_id());

                ObjectWriter ow = new ObjectMapper().writer();//.withDefaultPrettyPrinter();
                try {
                    System.out.println("---------------------------------------------------------------------------------------------");
                    String entityJson = ow.writeValueAsString(entity);
                    ObjectMapper mapper = new ObjectMapper();
                    SalesOrderEntity user = mapper.readValue(entityJson, SalesOrderEntity.class);
                    System.out.println(user);
                    System.out.println(entityJson);
                    for(SalesOrderItemEntity item: entity.getItems()){
                        String itemJson = ow.writeValueAsString(item);
                        //System.out.println(itemJson);
                    }
                    count++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void getProducts(){
        MagentoServiceLocator msl = new MagentoServiceLocator();
        try {
            Mage_Api_Model_Server_V2_HandlerPortType type = msl.getMage_Api_Model_Server_V2_HandlerPort();
            String log = type.login("DMM_API", "sheetmu1");
//            String sessionStr = type.startSession();
            AssociativeEntity ae = new AssociativeEntity();
//            ae.setKey("in");
//            ae.setValue("188087");
//            ae.setKey("gt");
//            ae.setValue("188000");
            ae.setKey("lt");
            ae.setValue("500");
            ComplexFilter cf = new ComplexFilter();
            cf.setKey("product_id");
            cf.setValue(ae);

            Filters filters = new Filters();
            ComplexFilter[] aeList = new ComplexFilter[1];
            aeList[0] = cf;
            filters.setComplex_filter(aeList);
            filters.getFilter();
            CatalogProductEntity[] catList = type.catalogProductList(log, filters, null);

            for(CatalogProductEntity cat: catList){
                System.out.println(cat.getName());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
