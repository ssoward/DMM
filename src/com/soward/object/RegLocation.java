/**
 * Title:        RegLocation.java
 * Description:  RegLocation.java 
 * Copyright:    Copyright (c)  2009
 * Company:      Meridias Capital Inc.
 * @author 		 Scott Soward     
 */
package com.soward.object;

public class RegLocation {
    public String regDesc;
    public String regName;
    public String regIP;
    public String regNum;
    
    public RegLocation(){
        this.regDesc = "";
        this.regName = "";
        this.regIP   = "";
        this.regNum  = "";
            
    }
    
    public String getRegDesc() {
        return regDesc;
    }
    public void setRegDesc( String regDesc ) {
        this.regDesc = regDesc;
    }
    public String getRegName() {
        return regName;
    }
    public void setRegName( String regName ) {
        this.regName = regName;
    }
    public String getRegIP() {
        return regIP;
    }
    public void setRegIP( String regIP ) {
        this.regIP = regIP;
    }
    public String getRegNum() {
        return regNum;
    }
    public void setRegNum( String regNum ) {
        this.regNum = regNum;
    }
    
    
}
