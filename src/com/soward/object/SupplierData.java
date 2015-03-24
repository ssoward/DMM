package com.soward.object;

public class SupplierData {
	private int    id            ;
	private int    supplierNum   ;
	private Double murrayThr     ;
	private Double lehiThr       ;
	private Double oremThr       ;
	private String eContent      ;
	public SupplierData(int supDataId, int supNum) {
		this.id = supDataId;
		this.supplierNum = supNum;
	}
	public SupplierData() {
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
	public Double getMurrayThr() {
		return murrayThr;
	}
	public void setMurrayThr(Double murrayThr) {
		this.murrayThr = murrayThr;
	}
	public Double getLehiThr() {
		return lehiThr;
	}
	public void setLehiThr(Double lehiThr) {
		this.lehiThr = lehiThr;
	}
	public Double getOremThr() {
		return oremThr;
	}
	public void setOremThr(Double oremThr) {
		this.oremThr = oremThr;
	}
	public String getEContent() {
		return eContent;
	}
	public void setEContent(String content) {
		eContent = content;
	}



}
