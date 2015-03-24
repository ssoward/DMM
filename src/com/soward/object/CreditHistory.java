package com.soward.object;

public class CreditHistory {
    String pid;
    String accountNum;
    String creditType;
    String creditDesc;
    String creditDate;
    String creditSum;
    public String getCreditSum() {
        return creditSum;
    }
    public void setCreditSum( String creditSum ) {
        this.creditSum = creditSum;
    }
    public String getAccountNum() {
        return accountNum;
    }
    public void setAccountNum( String accountNum ) {
        this.accountNum = accountNum;
    }
    public String getCreditDate() {
        return creditDate;
    }
    public void setCreditDate( String creditDate ) {
        this.creditDate = creditDate;
    }
    public String getCreditDesc() {
        return creditDesc;
    }
    public void setCreditDesc( String creditDesc ) {
        this.creditDesc = creditDesc;
    }
    public String getCreditType() {
        return creditType;
    }
    public void setCreditType( String creditType ) {
        this.creditType = creditType;
    }
    public String getPid() {
        return pid;
    }
    public void setPid( String pid ) {
        this.pid = pid;
    }
}
