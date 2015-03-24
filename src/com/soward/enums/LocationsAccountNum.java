package com.soward.enums;

public enum LocationsAccountNum {
    
    MURRAY ( "101" ),
    LEHI ( "102" ),
    ONLINE ( "103" );

    private String displayName;

    LocationsAccountNum( String value ) {
        this.displayName = value;
    }

    public String value() { 
        return displayName; 
    }

}
