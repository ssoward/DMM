/**
 * @author Scott Soward
 * Date: May 12, 2007
 * 
 */
package com.soward.object;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

public class Transaction implements Serializable {

	public String transNum;

	public String invoiceNum;

	public String transType;

	public String productNum;

	public String productName;

	public String productQty;

	public String transCost;

	public String transTax;

	public String transProductStatus;

	public String transDate;

	public String locationNum;

	public String username;

	public String transShipCost;

	public String transShipped;

	public String transShipDate;

	public Account account;

	public Invoice invoice;

	public Product prod;

	public Transaction() {

		this.transNum = "";
		this.invoiceNum = "";
		this.transType = "";
		this.productNum = "";
		this.productName = "";
		this.productQty = "";
		this.transCost = "";
		this.transTax = "";
		this.transProductStatus = "";
		this.transDate = "";
		this.locationNum = "";
		this.username = "";
		this.transShipCost = "";
		this.transShipped = "";
		this.transShipDate = "";
		this.account = new Account();
		this.invoice = new Invoice();

	}

	public void setTransNum( String str ) {
		this.transNum = str;
	}

	public void setInvoiceNum( String str ) {
		this.invoiceNum = str;
	}

	public void setTransType( String str ) {
		this.transType = str;
	}

	public void setProductNum( String str ) {
		this.productNum = str;
	}

	public void setProductName( String str ) {
		this.productName = str;
	}

	public void setProductQty( String str ) {
		this.productQty = str;
	}

	public void setTransCost( String str ) {
		this.transCost = str;
	}

	public void setTransTax( String str ) {
		this.transTax = str;
	}

	public void setTransProductStatus( String str ) {
		this.transProductStatus = str;
	}

	public void setTransDate( String str ) {
		this.transDate = str;
	}

	public String getTransYear(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try{
			if(!StringUtils.isBlank(this.transDate)){
				cal.setTime(sdf.parse(transDate));
				return cal.get(Calendar.YEAR)+"";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public void setLocationNum( String str ) {
		this.locationNum = str;
	}

	public void setUsername( String str ) {
		this.username = str;
	}

	public void setTransShipCost( String str ) {
		this.transShipCost = str;
	}

	public void setTransShipped( String str ) {
		this.transShipped = str;
	}

	public void setTransShipDate( String str ) {
		this.transShipDate = str;
	}

	public void setAccount( Account acct ) {
		this.account = acct;
	}

	public void setInvoice( Invoice inv ) {
		this.invoice = inv;
	}

	public String getTransNum() {
		return this.transNum;
	}

	public String getInvoiceNum() {
		return this.invoiceNum;
	}

	public String getTransType() {
		return this.transType;
	}

	public String getProductNum() {
		return this.productNum;
	}

	public String getProductName() {
		return this.productName;
	}

	public String getProductQty() {
		return this.productQty;
	}

	public String getTransCost() {
		return this.transCost;
	}

	public String getTransTax() {
		return this.transTax;
	}

	public String getTransProductStatus() {
		return this.transProductStatus;
	}

	public String getTransDateFormatted() {
		try{
			//parse date and return MM-dd-yyyy
			this.transDate = new SimpleDateFormat("MM-dd-yyyy").format( new SimpleDateFormat("yyyy-MM-dd").parse( this.transDate ));
		}catch(Exception e){
			//bad date
			this.transDate = "";
		}
		return this.transDate;
	}
	public String getTransDate() {
		return this.transDate;
	}

	public String getLocationNum() {
		return this.locationNum;
	}

	public String getUsername() {
		return this.username;
	}

	public String getTransShipCost() {
		return this.transShipCost;
	}

	public String getTransShipped() {
		return this.transShipped;
	}

	public String getTransShipDate() {
		return this.transShipDate;
	}

	public Account getAccount() {
		return this.account;
	}

	public Invoice getInvoice() {
		return this.invoice;
	}

	public Product getProd() {
		return prod;
	}

	public void setProd( Product prod ) {
		this.prod = prod;
	}

}
