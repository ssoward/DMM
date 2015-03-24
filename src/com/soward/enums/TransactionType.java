package com.soward.enums;

public enum TransactionType {
    OO  ("Online Order"),   
    SO  ("Special Order"),  
    NS  ("Normal Sale"),    
    HB  ("Hold Bin"),       
    PO  ("Purchase Order"), 
    DS  ("Drop Ship"),      
    WO  ("Web Order"),      
    DO  ("Daily Order"); 

    private String displayName;

    TransactionType( String value ) {
        this.displayName = value;
    }

    public String value() { 
        return displayName; 
    }
}










