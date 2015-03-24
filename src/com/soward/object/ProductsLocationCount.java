package com.soward.object;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.soward.util.ProductsLocationCountUtil;

public class ProductsLocationCount {
	public int plcNum = 0;
	public int productNum = 0;
	public int OREM = 0;
	public int LEHI = 0;
	public int MURRAY = 0;
	public int loc01 = 0;
	public int loc02 = 0;
	public int DAYVIOLIN = 0;
	/**
	 * @return the plcNum
	 */
	public int getPlcNum() {
		return plcNum;
	}
	/**
	 * @param plcNum the plcNum to set
	 */
	public void setPlcNum(int plcNum) {
		this.plcNum = plcNum;
	}
	/**
	 * @return the productNum
	 */
	public int getProductNum() {
		return productNum;
	}
	/**
	 * @param productNum the productNum to set
	 */
	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}
	/**
	 * @return the oREM
	 */
	public int getOREM() {
		return OREM;
	}
	/**
	 * @param orem the oREM to set
	 */
	public void setOREM(int orem) {
		OREM = orem;
	}
	/**
	 * @return the lEHI
	 */
	public int getLEHI() {
		return LEHI;
	}
	/**
	 * @param lehi the lEHI to set
	 */
	public void setLEHI(int lehi) {
		LEHI = lehi;
	}
	/**
	 * @return the mURRAY
	 */
	public int getMURRAY() {
		return MURRAY;
	}
	/**
	 * @param murray the mURRAY to set
	 */
	public void setMURRAY(int murray) {
		MURRAY = murray;
	}
	/**
	 * @return the loc01
	 */
	public int getLoc01() {
		return loc01;
	}
	/**
	 * @param loc01 the loc01 to set
	 */
	public void setLoc01(int loc01) {
		this.loc01 = loc01;
	}
	/**
	 * @return the loc02
	 */
	public int getLoc02() {
		return loc02;
	}
	
	public int getDV() {
		return DAYVIOLIN;
	}
	public int getDAYVIOLIN() {
		return DAYVIOLIN;
	}
	public void setDAYVIOLIN(int dayviolin) {
		DAYVIOLIN = dayviolin;
	}
	/**
	 * @param loc02 the loc02 to set
	 */
	public void setLoc02(int loc02) {
		this.loc02 = loc02;
	}
	//returns count for specific location
	@SuppressWarnings("unchecked")
	public int getLocation(String loca) {
		Method m;
		String cc;
		try {
			Class c = ProductsLocationCount.class;
			m = c.getMethod("get"+loca, new Class[0]);
			cc =  (m.invoke(this,new Object[0]))+"";
			return Integer.parseInt(cc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static void main(String args[]){
		ProductsLocationCount plc = ProductsLocationCountUtil.fetchForPid(12);
		System.out.println(plc.getLocation("MURRAY"));
		System.out.println(plc.getLocation("LEHI"));
		System.out.println(plc.getLocation("OREM"));
		System.out.println(plc.getLocation("DAYVIOLIN"));
	}
	
	
}