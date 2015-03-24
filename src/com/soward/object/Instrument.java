package com.soward.object;

public class Instrument {
	String pid = "";
	String name = "";
	String supplier = "";
	String itemNumber = "";
	String type = "";
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	//Instrument - have a drop down
	//    For =     1/64 violin
	//              1/32 violin
	//              1/16 violin
	//              1/8 violin
	//              1/4 violin
	//              1/2 violin
	//              3/4 violin
	//              4/4 violin
	//              14" viola
	//              14 1/2" viola
	//              15" viola
	//              15 1/2" viola
	//              15 7/8" viola
	//              16" viola
	//              16 1/2" viola
	//              17" viola
	//( please give me a way to add to the instrument list )
	//
	//Product Name
	//Item #
	//Supplier-drop down for=
	//           Jay Haide
	//           Howard Core
	//           Connolly
	//           M & M
	//           International
	//(Please give me a way to add suppliers)
}
