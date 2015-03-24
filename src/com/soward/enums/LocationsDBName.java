package com.soward.enums;



public enum LocationsDBName {

    MURRAY ( "Products",      "101", "Murray", "MURRAY"),
    LEHI   ( "LehiProducts" , "102", "Lehi", "LEHI"),
//    OREM   ( "OremProducts" , "103", "Orem", "OREM"),
    DV   ( "DVProducts" , "105", "DV", "DV"),
    ONLINE ( "Products" ,     "104", "Online", "LOC01");

    private String dbName;
    private String accountName;
    private String displayName;
    private String capLoca;

    LocationsDBName( String value, String accNum, String disName, String capLoca ) {
        this.dbName = value;
        this.accountName = accNum;
        this.displayName = disName;
        this.capLoca = capLoca;
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
    
    public String capName(){
    	return capLoca;
    }
    //output: dbName: Products name: MURRAY account: 101 displayName: Murray
    public static void main( String args[] ) {
        System.out.println(LocationsDBName.valueOf( "MURRAY" ).account());
        for(LocationsDBName lname : LocationsDBName.values()){
            System.out.println( "dbName: "+lname.dbName()+" | name: "+lname.name()+" | account: "+
                    lname.account()+" | displayName: "+lname.displayName() );
        }
        
    }


}
