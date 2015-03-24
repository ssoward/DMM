package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.soward.db.MySQL;
import com.soward.object.Descriptions;
import com.soward.object.Transaction;

public class StockOrderReport {


	public static void main(String args[]){
		List<Map<String, String>> al = StockOrderReport.getInventorySoldReport(  "2008-12-15", "2009-12-16", "MURRAY", "169");
		for(Map map: al){
			System.out.println(map);
		}

	}
	/**
	 * gets list of counts of products sold within time range for specific supplier
	 * @param startDate
	 * @param endDate
	 * @param location
	 * @param supNum
	 * @return List<Map<String, String>>
	 */
	public static List<Map<String, String>> getInventorySoldReport( String startDate, String endDate, String location, String supNum) {
		
		List<Map<String, String>> prodList = new ArrayList<Map<String, String>>();
		Connection con = null;
		MySQL sdb = new MySQL();
		boolean checkDates = Utils.validateDateRange(startDate, endDate, 400, "yyyy-MM-dd");
		if(checkDates){
			
			String sql = "Select count(trans.productNum) as cnt, prod.productName, prod.productNum, prod.productCatalogNum ";
			sql += " \nfrom Transactions trans, InvoiceLocation inv, InvoiceLocation iloc, Products prod ";
			sql += " \nwhere trans.productNum= prod.productNum ";
			sql += " \nand prod.productSupplier1 = "+supNum;
			sql += " \nand trans.invoiceNum = inv.invoiceNum";
			sql += " \nand inv.invoiceNum = iloc.invoiceNum";
			sql += " \nand iloc.location = '"+location+"'";
			sql += " \nand iloc.invDate > '" + startDate +		"' and iloc.invDate< '"+endDate+"' ";
			sql += " \ngroup by trans.productNum, prod.productName, prod.productNum, prod.productCatalogNum order by prod.productName";
			
			try {
				con = sdb.getConn();
				PreparedStatement pstmt = null;
				pstmt = con.prepareStatement( sql );
				ResultSet rset = pstmt.executeQuery();
				// only get the next 20 entries
				while ( rset.next() ) {
					HashMap<String, String> result = new HashMap<String,String>();
					result.put("count", rset.getString( "cnt" ));
					result.put("productName", rset.getString( "productName" ));
					result.put("productNum", rset.getString( "productNum" ));
					result.put("productCatalogNum", rset.getString( "productCatalogNum" ));
					
					prodList.add( result );
				}
				pstmt.close();
				con.close();
			} catch ( Exception e ) {
				e.printStackTrace();
			}
			return prodList;
		}
		else{
			try {
				con.close();
			} catch ( Exception e ) {
				//never opened con
				//e.printStackTrace();
			}
			return null;
		}
	} 

}
