package com.soward.object;

import java.util.Date;

public class AccountInfo {

    private String pid;
    private String name;
    private String type;
    private String number;
    private String code;
    private String month;
    private String year;
    private String account;

    public String getPid() {
        return pid;
    }
    public void setPid( String pid ) {
        this.pid = pid;
    }
    public String getName() {
        return name;
    }
    public void setName( String name ) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType( String type ) {
        this.type = type;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber( String number ) {
        this.number = number;
    }
    public String getCode() {
        return code;
    }
    public void setCode( String code ) {
        this.code = code;
    }

    public String getMonth() {
        return month;
    }
    public void setMonth( String month ) {
        this.month = month;
    }
    public String getYear() {
        return year;
    }
    public void setYear( String year ) {
        this.year = year;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount( String account ) {
        this.account = account;
    }
    public String toString(){
        return "AccountInfo[pid="+pid
        +",Name="+this.name
        +",Type="+this.type
        +",Number="+this.number
        +",Code="+this.code
        +",Account="+this.account
        +",Month="+this.month
        +",Year="+this.year+"]";
    }
}
