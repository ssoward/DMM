package com.soward.enums;

/**
 * Created by ssoward on 5/2/15.
 */
public enum ProductCacheEnum {
    MOVE("move"),
    DONE("done");

    private String name;

    ProductCacheEnum( String value ) {
        this.name = value;
    }

    public String getName() {
        return name;
    }
}
