package com.soward.enums;

/**
 * Created by ssoward on 5/16/15.
 */
public enum ProductColsEnum {
    NUM("productNum"),
    CAT_NUM("productCatalogNum");

    String label;

    ProductColsEnum(String s){
        this.label = s;
    }

    public String getLabel() {
        return label;
    }
}
