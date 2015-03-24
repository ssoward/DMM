package com.soward.object;

public class WebProductInfo {
    String productBlurb;
    String productFeature;
    String productNum;
    
    public String getProductBlurb() {
        return productBlurb;
    }

    public void setProductBlurb( String productBlurb ) {
        this.productBlurb = productBlurb;
    }

    public String getProductFeature() {
        return productFeature;
    }

    public void setProductFeature( String productFeature ) {
        this.productFeature = productFeature;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum( String productNum ) {
        this.productNum = productNum;
    }

    public WebProductInfo(){
        this.productBlurb = "";
        this.productFeature = "";
        this.productNum ="";
    }

}
