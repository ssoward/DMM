/**
 * Title:        Person.java
 * Description:  Person.java 
 * Copyright:    Copyright (c)  2009
 * Company:      Meridias Capital Inc.
 * @author 		 Scott Soward     
 */
package com.soward.object;

import org.apache.commons.lang.StringUtils;

public class Person {
    String person_pid  = "";
    String firstName   = "";
    String lastName    = "";
    String address1    = "";
    String address2    = "";
    String city        = "";
    String county      = "";
    String state       = "";
    String phone       = "";
    String cell        = "";
    String email       = "";
    String notes       = "";
    String person_type = "";

    public Person(){
    }

    public String getPerson_pid() {
        return StringUtils.isBlank( this.person_pid )?"":this.person_pid;
    }
    public void setPerson_pid( String person_pid ) { 
        this.person_pid = person_pid;
    }
    public void setPerson_type( String person_type ) { 
        this.person_type = person_type;
    }
    public void setFirstName( String firstName ) { 
        this.firstName = firstName;
    }
    public void setLastName( String lastName ) { 
        this.lastName = lastName;
    }
    public void setAddress1( String address1 ) { 
        this.address1 = address1;
    }
    public void setAddress2( String address2 ) { 
        this.address2 = address2;
    }
    public void setCity( String city ) { 
        this.city = city;
    }
    public void setCounty( String county ) { 
        this.county = county;
    }
    public void setState( String state ) { 
        this.state = state;
    }
    public void setPhone( String phone ) { 
        this.phone = phone;
    }
    public void setCell( String cell ) { 
        this.cell = cell;
    }
    public void setEmail( String email ) { 
        this.email = email;
    }
    public void setNotes( String notes ) { 
        this.notes = notes;
    }
    public String getPerson_type() { 
        return StringUtils.isBlank( this.person_type )?"":this.person_type;
    } 
    public String getFirstName() {   
        return StringUtils.isBlank( this.firstName   )?"":this.firstName  ;
    } 
    public String getLastName() {    
        return StringUtils.isBlank( this.lastName    )?"":this.lastName   ;
    } 
    public String getAddress1() {    
        return StringUtils.isBlank( this.address1    )?"":this.address1   ;
    } 
    public String getAddress2() {    
        return StringUtils.isBlank( this.address2    )?"":this.address2   ;
    } 
    public String getCity() {        
        return StringUtils.isBlank( this.city        )?"":this.city       ;
    } 
    public String getCounty() {      
        return StringUtils.isBlank( this.county      )?"":this.county     ;
    } 
    public String getState() {       
        return StringUtils.isBlank( this.state       )?"":this.state      ;
    } 
    public String getPhone() {       
        return StringUtils.isBlank( this.phone       )?"":this.phone      ;
    } 
    public String getCell() {        
        return StringUtils.isBlank( this.cell        )?"":this.cell       ;
    } 
    public String getEmail() {       
        return StringUtils.isBlank( this.email       )?"":this.email      ;
    } 
    public String getNotes() {       
        return StringUtils.isBlank( this.notes       )?"":this.notes      ;
    }
}
