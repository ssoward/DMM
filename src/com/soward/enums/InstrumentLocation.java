package com.soward.enums;



public enum InstrumentLocation {

    MURRAY ( "Murray", "101" , "Murray"),
    LEHI ( "Lehi" , "102", "Lehi"),
    OREM ( "Orem" , "103", "Orem"),
    SHOP ( "Shop" , "104", "Shop"),
    SALES_MANAGER ( "Sales_Manager" , "105", "Sales Manager");

    private String dbName;
    private String accountName;
    private String displayName;

    InstrumentLocation( String value, String accNum, String disName ) {
        this.dbName = value;
        this.accountName = accNum;
        this.displayName = disName;
    }
    //returns eg. Murray
    public String displayName() { 
        return displayName; 
    }
    public String dbName() { 
        return dbName; 
    }
    //returns eg. 101
    public String account() { 
        return accountName; 
    }
    //output: dbName: Products name: MURRAY account: 101 displayName: Murray
    public static void main( String args[] ) {
        System.out.println(InstrumentLocation.valueOf( "MURRAY" ).account());
        for(InstrumentLocation lname : InstrumentLocation.values()){
            System.out.println( "dbName: "+lname.dbName()+" | name: "+lname.name()+" | account: "+
                    lname.account()+" | displayName: "+lname.displayName() );
        }
    }


}
