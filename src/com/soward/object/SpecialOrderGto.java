package com.soward.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpecialOrderGto implements Serializable{
    private static final long serialVersionUID = 1924856140473046406L;
    public Account account;
    private Long id;
    private String orderMethod;
    private String droppedLocation;
    private String shipMethod;
    private String droppedShipped;
    private String pickUp;
    private String orderStatus;
    private String shipNote;
    private String orderDate;
    private String createdBy;
    private String deliveryDate;
    private String accountNum;
    private List<ProdOrderGto> prodList;
    private String accountName;

    public SpecialOrderGto(){
        this.prodList = new ArrayList<ProdOrderGto>();
    }

    public String getOrderMethod() {
        return orderMethod;
    }

    public void setOrderMethod( String orderMethod ) {
        this.orderMethod = orderMethod;
    }

    public String getDroppedLocation() {
        return droppedLocation;
    }

    public void setDroppedLocation( String droppedLocation ) {
        this.droppedLocation = droppedLocation;
    }

    public String getShipMethod() {
        return shipMethod;
    }

    public void setShipMethod( String shipMethod ) {
        this.shipMethod = shipMethod;
    }

    public String getDroppedShipped() {
        return droppedShipped;
    }

    public void setDroppedShipped( String droppedShipped ) {
        this.droppedShipped = droppedShipped;
    }

    public String getPickUp() {
        return pickUp;
    }

    public void setPickUp( String pickUp ) {
        this.pickUp = pickUp;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus( String orderStatus ) {
        this.orderStatus = orderStatus;
    }

    public String getShipNote() {
        return shipNote;
    }

    public void setShipNote( String shipNote ) {
        this.shipNote = shipNote;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate( String orderDate ) {
        this.orderDate = orderDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy( String createdBy ) {
        this.createdBy = createdBy;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate( String deliveryDate ) {
        this.deliveryDate = deliveryDate;
    }

    public List<ProdOrderGto> getProdList() {
        return prodList;
    }

    public void setProdList( List<ProdOrderGto> prodList ) {
        this.prodList = prodList;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    public String getAccountNum() {
        return accountNum;
    }
    public void setAccountNum( String accountNum ) {
        this.accountNum = accountNum;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }
    
//    @Override
//    public String toString(){
//        String str = "specialOrder["+
//        "orderMethod="+ this.orderMethod     +
//        ",droppedLocation="+ this.droppedLocation +
//        ",orderId="+ this.id +
//        ",shipMethod="+ this.shipMethod      +
//        ",droppedShipped="+ this.droppedShipped  +
//        ",pickUp="+ this.pickUp          +
//        ",orderStatus="+ this.orderStatus     +
//        ",shipNote=\'"+ this.shipNote  +"\'"      +
//        ",orderDate=\'"+ this.orderDate +"\'"      +
//        ",createdBy=\'"+ this.createdBy +"\'"      +
//        ",deliveryDate=\'"+ this.deliveryDate+"\'"      +
//        ",accountNum="+ this.accountNum +
//        ",accountName="+ this.accountName;
//        str += ",prodList:[";
//        if(this.prodList!=null && !this.prodList.isEmpty()){
//            for(ProdOrderGto gto: this.prodList){
//                str += "{"+gto.toString()+"}";
//            }
//        }
//        str +="]";
//        str += "]"      ;
//        return str;
//    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName( String accountName ) {
        this.accountName = accountName;
    }
    
}

