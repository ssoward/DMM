package com.soward.object;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class Account implements Serializable {

    public String pid;

    public String accountNum;

    public String accountName;

    public String accountPassword;

    public String accountContact;

    public String accountEmail;

    public String accountPhone1;

    public String accountPhone2;

    public String accountFax;

    public String accountStreet;

    public String accountCity;

    public String accountState;

    public String accountPostalCode;

    public String accountCountry;

    public String accountType1;

    public String accountType2;

    public String accountOpenDate;

    public String accountCloseDate;

    public String accountBalance;

    public String accountTotalSpent;

    public String accountTotalDisc;

    public String accountStartDate;

    public String accountEndDate;
    public Invoice accountInv;
    public String accountZip;

    public Account() {
    }

    public Invoice getAccountInv() {
        return accountInv;
    }
    public void setAccountInv(Invoice inv) {
        this.accountInv = inv;
    }
    public String getAccountNum() {
        return StringUtils.trimToEmpty( accountNum);
    }

    public String getAccountName() {
        return StringUtils.trimToEmpty( accountName);
    }

    public String getAccountPassword() {
        return StringUtils.trimToEmpty( accountPassword);
    }

    public String getAccountContact() {
        return StringUtils.trimToEmpty( accountContact);
    }

    public String getAccountEmail() {
        return StringUtils.trimToEmpty( accountEmail);
    }

    public String getAccountPhone1() {
        return StringUtils.trimToEmpty( accountPhone1);
    }

    public String getAccountPhone2() {
        return StringUtils.trimToEmpty( accountPhone2);
    }

    public String getAccountFax() {
        return StringUtils.trimToEmpty( accountFax);
    }

    public String getAccountStreet() {
        return StringUtils.trimToEmpty( accountStreet);
    }

    public String getAccountCity() {
        return StringUtils.trimToEmpty( accountCity);
    }

    public String getAccountState() {
        return StringUtils.trimToEmpty( accountState);
    }

    public String getAccountPostalCode() {
        return StringUtils.trimToEmpty( accountPostalCode);
    }

    public String getAccountCountry() {
        return StringUtils.trimToEmpty( accountCountry);
    }

    public String getAccountType1() {
        return StringUtils.trimToEmpty( accountType1);
    }

    public String getAccountType2() {
        return StringUtils.trimToEmpty( accountType2);
    }

    public String getAccountOpenDate() {
        return StringUtils.trimToEmpty( accountOpenDate);
    }

    public String getAccountCloseDate() {
        return StringUtils.trimToEmpty( accountCloseDate);
    }

    public String getAccountBalance() {
        return StringUtils.trimToEmpty( accountBalance);
    }

    public String getAccountTotalSpent() {
        return StringUtils.trimToEmpty( accountTotalSpent);
    }

    public String getAccountTotalDisc() {
        return StringUtils.trimToEmpty( accountTotalDisc);
    }

    public String getAccountStartDate() {
        return StringUtils.trimToEmpty( accountStartDate);
    }

    public String getAccountEndDate() {
        return StringUtils.trimToEmpty( accountEndDate);
    }

    public void setAccountTotalDisc( String str ) {
        this.accountTotalDisc = str;
    }

    public void setAccountTotalSpent( String str ) {
        this.accountTotalSpent = str;
    }

    public void setAccountStartDate( String str ) {
        this.accountStartDate = str;
    }

    public void setAccountEndDate( String str ) {
        this.accountEndDate = str;
    }

    public void setAccountNum( String str ) {
        this.accountNum = str;
    }

    public void setAccountName( String str ) {
        this.accountName = str;
    }

    public void setAccountPassword( String str ) {
        this.accountPassword = str;
    }

    public void setAccountContact( String str ) {
        this.accountContact = str;
    }

    public void setAccountEmail( String str ) {
        this.accountEmail = str;
    }

    public void setAccountPhone1( String str ) {
        this.accountPhone1 = str;
    }

    public void setAccountPhone2( String str ) {
        this.accountPhone2 = str;
    }

    public void setAccountFax( String str ) {
        this.accountFax = str;
    }

    public void setAccountStreet( String str ) {
        this.accountStreet = str;
    }

    public void setAccountCity( String str ) {
        this.accountCity = str;
    }

    public void setAccountState( String str ) {
        this.accountState = str;
    }

    public void setAccountPostalCode( String str ) {
        this.accountPostalCode = str;
    }

    public void setAccountCountry( String str ) {
        this.accountCountry = str;
    }

    public void setAccountType1( String str ) {
        this.accountType1 = str;
    }

    public void setAccountType2( String str ) {
        this.accountType2 = str;
    }

    public void setAccountOpenDate( String str ) {
        this.accountOpenDate = str;
    }

    public void setAccountCloseDate( String str ) {
        this.accountCloseDate = str;
    }

    public void setAccountBalance( String str ) {
        this.accountBalance = str;
    }

    public static void main( String args[] ) {

    }

	/**
	 * @return StringUtils.trimToEmpty( the accountZip
	 */
	public String getAccountZip() {
		return StringUtils.trimToEmpty( StringUtils.trimToEmpty( accountZip));
	}

	/**
	 * @param accountZip the accountZip to set
	 */
	public void setAccountZip(String accountZip) {
		this.accountZip = accountZip;
	}

    public String getAccountAddress(){
        String add = "";
        add += StringUtils.isBlank(accountStreet)?"":accountStreet;
        add += StringUtils.isBlank(add)?"":"<br/>";
        add += StringUtils.isBlank(accountCity)?"":accountCity+", ";
        add += StringUtils.isBlank(accountState)?"":accountState+" ";
        add += StringUtils.isBlank(accountPostalCode)?"":accountPostalCode;
        add += StringUtils.isBlank(accountCountry)?"":"<br/>"+accountCountry;
        return add;
    }
	
	public String toString() {
        return 
        "Account[pid="+        pid               +
        "\n,accountNum="+        accountNum        +
        "\n,accountName="+       accountName       +
        "\n,accountPassword="+   accountPassword   +
        "\n,accountContact="+    accountContact    +
        "\n,accountEmail="+      accountEmail      +
        "\n,accountPhone1="+     accountPhone1     +
        "\n,accountPhone2="+     accountPhone2     +
        "\n,accountFax="+        accountFax        +
        "\n,accountStreet="+     accountStreet     +
        "\n,accountCity="+       accountCity       +
        "\n,accountState="+      accountState      +
        "\n,accountPostalCode="+ accountPostalCode +
        "\n,accountCountry="+    accountCountry    +
        "\n,accountType1="+      accountType1      +
        "\n,accountType2="+      accountType2      +
        "\n,accountOpenDate="+   accountOpenDate   +
        "\n,accountCloseDate="+  accountCloseDate  +
        "\n,accountBalance="+    accountBalance    +
        "\n,accountTotalSpent="+ accountTotalSpent +
        "\n,accountTotalDisc="+  accountTotalDisc  +
        "\n,accountStartDate="+  accountStartDate  +
        "\n,accountEndDate="+    accountEndDate    +
        "\n,accountInv="+        accountInv        +
        "\n,accountZip="+        accountZip        +"]";
    }

    public String getAccountAddressFormated() {
        String add = "";
        add += StringUtils.isBlank(accountName)?"":accountName+"\n";
        add += StringUtils.isBlank(accountContact)?"":accountContact+"\n";
        add += StringUtils.isBlank(accountStreet)?"":accountStreet+"\n";
        add += StringUtils.isBlank(accountCity)?"":accountCity;
        add += StringUtils.isBlank(accountState)?"":" "+accountState+" ";
        add += StringUtils.isBlank(accountPostalCode)?"":accountPostalCode;
        add += StringUtils.isBlank(accountCountry)?"":" "+accountCountry;
        return add;
    }
}