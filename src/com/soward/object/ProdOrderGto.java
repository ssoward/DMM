package com.soward.object;

public class ProdOrderGto {
    private Long id;
    private String productNum;
    private String verified;
    private String quantity;
    private String status;
    private String emailed;
    private Long sOrderId;
    private String supNum;

    //non persisted fields
    private String category;
    private String productCatalogNum;
    private String productName;
    private String productCost1;


    public String isVerified() {
        return verified;
    }

    public void setVerified( String verified ) {
        this.verified = verified;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity( String quantity ) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public String isEmailed() {
        return emailed;
    }

    public void setEmailed( String emailed ) {
        this.emailed = emailed;
    }

    /**
     * @param args
     */
    public static void main( String[] args ) {
        // TODO Auto-generated method stub

    }
    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getVerified() {
        return verified;
    }

    public String getEmailed() {
        return emailed;
    }

    public Long getSOrderId() {
        return sOrderId;
    }

    public void setSOrderId( Long orderId ) {
        sOrderId = orderId;
    }

    public String getSupNum() {
        return supNum;
    }

    public void setSupNum( String supNum ) {
        this.supNum = supNum;
    }

    public String toString(){
        String s = "";
        s+= "id="+this.id;
        s+= ",sOrderId="+this.sOrderId;
        s+= ",productNum="+this.productNum;
        s+= ",verified="+this.verified;
        s+= ",quantity="+this.quantity;
        s+= ",status="+this.status;
        s+= ",emailed="+this.emailed;
        s+= ",category="+this.category;
        s+= ",productCatalogNum="+this.productCatalogNum;
        s+= ",productName="+this.productName;
        s+= ",supNum="+this.supNum;
        s+= ",productCost1="+this.productCost1;

        return s;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory( String category ) {
        this.category = category;
    }

    public String getProductCatalogNum() {
        return productCatalogNum;
    }

    public void setProductCatalogNum( String productCatalogNum ) {
        this.productCatalogNum = productCatalogNum;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName( String productName ) {
        this.productName = productName;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum( String productNum ) {
        this.productNum = productNum;
    }

    public String getProductCost1() {
        return productCost1;
    }

    public void setProductCost1( String productCost1 ) {
        this.productCost1 = productCost1;
    }



}
