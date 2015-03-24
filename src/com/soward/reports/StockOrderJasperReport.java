/**
 * Title:        DailySalesReport.java
 * Description:  DailySalesReport.java 
 * Copyright:    Copyright (c)  2008
 * Company:      Meridias Capital Inc.
 * @author 		 Scott Soward     
 */
package com.soward.reports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.soward.util.StockOrderReport;

public class StockOrderJasperReport implements ReportCollection {

	/* (non-Javadoc)
	 * @see com.soward.reports.ReportCollection#getData(java.util.Map)
	 */
	public Collection getData( Map<String, Object> parameters ) {
		String outputType = (String)parameters.get("outputType");
		boolean pdf = outputType!=null&&outputType.equals( "PDF" );

		String dateOne = (String)parameters.get( "dOne" );
		String dateTwo = (String)parameters.get( "dTwo" );
		String locationName = (String)parameters.get( "lName" );
		String supplier = (String)parameters.get( "supplier" );
		parameters.put("title", locationName);
		String loc1 = "";
		String loc2 = "";
		//create columns for invoice list
		parameters.put("column001", "Product");
		parameters.put("column002", "DMM#");
		parameters.put("column003", "Cat#");
		parameters.put("column004", "Count");


		Collection<Map> reportData = new Vector<Map>();
		List<Map<String,String>> prodList = StockOrderReport.getInventorySoldReport(dateOne, dateTwo, locationName, supplier);

		for(Map<String, String> map: prodList){
			String count = map.get("count");
			String productName = map.get("productName");
			String productNum = map.get("productNum");
			String productCatalogNum = map.get("productCatalogNum");
			Map<String, String> row = new HashMap<String, String>();;
			row.put("value004", count);
			row.put("value001", productName);
			row.put("value002", productNum);
			row.put("value003", productCatalogNum);
			reportData.add( row );
		}
		try {
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse( dateOne );
			Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse( dateTwo );
			parameters.put("fromDate", date1);
			parameters.put("toDate", date2);
			parameters.put("supplier", supplier);
		} catch ( ParseException e ) {
			e.printStackTrace();
		}
		return reportData;
	}

	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		// TODO Auto-generated method stub

	}

}
