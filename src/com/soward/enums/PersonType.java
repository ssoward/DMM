package com.soward.enums;

public enum PersonType {
    
    
    TEACHER ( "TEACHER" );

    private String displayName;

    PersonType( String value ) {
        this.displayName = value;
    }

    public String userFriendly() { 
        return displayName; 
    }
    public static void main( String args[] ) {
        System.out.println(PersonType.valueOf( "PAYMENT" ).userFriendly());
        for(PersonType lname : PersonType.values()){
            System.out.println( "value: "+lname.userFriendly() );
        }
    }

}
