/**
 * 
 */
package com.soward.object;

import java.util.Date;

/**
 * @author ssoward
 *
 */
public class Order {

	private int prodNum;
	private int count;
	private Date   dateEval;
	private Date   dateEmail;
	private Date   dateReceived;
	private String user;
	private String notes;
	private String userReceived;
	private String location;
	private int    id            ;
	private int    supplierNum   ;
	private Product prod;
	private Date   deliveryDate;
	private String orderMethod;
	private String dropped;
	private String shippedMethod;
	private String accountNum;
	
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getOrderMethod() {
		return orderMethod;
	}

	public void setOrderMethod(String orderMethod) {
		this.orderMethod = orderMethod;
	}

	public String getDropped() {
		return dropped;
	}

	public void setDropped(String dropped) {
		this.dropped = dropped;
	}

	public String getShippedMethod() {
		return shippedMethod;
	}

	public void setShippedMethod(String shippedMethod) {
		this.shippedMethod = shippedMethod;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public Order() {
	}
	
	public Product getProd() {
		return prod;
	}
	public void setProd(Product prod) {
		this.prod = prod;
	}
	public int getProdNum() {
		return prodNum;
	}
	public void setProdNum(int prodNum) {
		this.prodNum = prodNum;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Date getDateEval() {
		return dateEval;
	}
	public void setDateEval(Date dateEval) {
		this.dateEval = dateEval;
	}
	public Date getDateEmail() {
		return dateEmail;
	}
	public void setDateEmail(Date dateSent) {
		this.dateEmail = dateSent;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSupplierNum() {
		return supplierNum;
	}
	public void setSupplierNum(int supplierNum) {
		this.supplierNum = supplierNum;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getDateReceived() {
		return dateReceived;
	}
	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}
	public String getUserReceived() {
		return userReceived;
	}
	public void setUserReceived(String userReceived) {
		this.userReceived = userReceived;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString(){
		String str = this.location;
		str += "\nuser: "+this.user;
		str += "\nuserReceived: "+this.userReceived;
		str += "\ncount: "+this.count;
		str += "\ndateEmail: "+this.dateEmail;
		str += "\ndateEval: "+this.dateEval;
		str += "\ndateReceived: "+this.dateReceived;
		str += "\nid: "+this.id;
		str += "\nprodNum: "+this.prodNum;
		str += "\nsupplierNum: "+this.supplierNum;
		return str;
		
	}
}
