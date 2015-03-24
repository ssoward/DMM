package com.soward.enums;

public enum CreditType {
    
    
    PAYMENT ( "Payment" ),
    CREDIT ( "Credit" ),
    CHARGE ( "Charge" );

    private String displayName;

    CreditType( String value ) {
        this.displayName = value;
    }

    public String userFriendly() { 
        return displayName; 
    }
    public static void main( String args[] ) {
        System.out.println(CreditType.valueOf( "PAYMENT" ).userFriendly());
        for(CreditType lname : CreditType.values()){
            System.out.println( "value: "+lname.userFriendly() );
        }
    }

}
