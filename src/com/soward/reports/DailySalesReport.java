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

import com.soward.enums.LocationsDBName;
import com.soward.object.Product;
import com.soward.object.ProductsLocationCount;
import com.soward.util.ProductsLocationCountUtil;
import com.soward.util.SalesReport;
import com.soward.util.TransUtil;

public class DailySalesReport implements ReportCollection {

	/* (non-Javadoc)
	 * @see com.soward.reports.ReportCollection#getData(java.util.Map)
	 */
	public Collection getData( Map<String, Object> parameters ) {
		String outputType = (String)parameters.get("outputType");
		boolean pdf = outputType!=null&&outputType.equals( "PDF" );

		String sortBy = (String)parameters.get( "sortBy" );
		String reportType = (String)parameters.get( "reportType" );
		String dateOne = (String)parameters.get( "dOne" );
		String dateTwo = (String)parameters.get( "dTwo" );
		String locationName = (String)parameters.get( "lName" );
		String regLocation = (String)parameters.get( "regLocation" );
		String dept = (String)parameters.get( "department" );
		parameters.put("title", locationName);
		String loc1 = "";
		String loc2 = "";
		boolean consolidatedList = reportType==null||reportType.equals("consol")?true:false;
		//create columns for invoice list
		if(!consolidatedList){
			parameters.put("column001", "Supplier");
			parameters.put("column002", "Catalog #");
			parameters.put("column003", "SKU");
			parameters.put("column004", "Name");
			parameters.put("column005", "Desc");
			parameters.put("column006", "S/Qty/HB");
			parameters.put("column007", "Invoice #");
			parameters.put("column008", "Date");
		}
		//create consolidatedList report
		else{
			boolean firstR = true;
			for ( LocationsDBName lname : LocationsDBName.values() ) {
				if ( !"ONLINE".equals( lname.name() ) && !lname.name().equals( locationName ) && firstR) {
					loc1 += lname.name();
					firstR = false;
				}
				else if ( !"ONLINE".equals( lname.name() ) && !lname.name().equals( locationName ) && !firstR) {
					loc2 += lname.name();
				}
			}
			parameters.put("column001", "Supplier");
			parameters.put("column002", "Catalog #");
			parameters.put("column003", "SKU");
			parameters.put("column004", "Name");
			parameters.put("column005", "Desc");
			parameters.put("column006", "S/Qty/HB");
			parameters.put("column007", loc1);
			parameters.put("column008", loc2);
		}

		Collection<Map> reportData = new Vector<Map>();
		Map<String, String> row = new HashMap<String, String>();
		SalesReport salesReport = new SalesReport();
		List<Product> salesList = null;

		salesList = salesReport.getSalesReport(dateOne, dateTwo, locationName, 0, regLocation, consolidatedList, false, false, dept );
		HashMap<String, ProductsLocationCount> countHash = ProductsLocationCountUtil.getHashForList(salesList);
		HashMap<String, String> hm = TransUtil.getAllHBHash();

		for(Product temp: salesList){
			row = new HashMap<String, String>();
			String supData = temp.getSupplier().getSupplierName()+temp.getSupplier().getSupplierPhone();
			String prodName = temp.getProductName();
			String prodDesc = temp.getProductDescription();
			row.put("value001", supData);//pdf&&supData.length()>20?supData.substring( 0,20 ):supData);
			row.put("value002", temp.getProductCatalogNum());
			row.put("value003", temp.getProductSKU());
			row.put("value004", prodName);//pdf&&prodName.length()>20?prodName.substring( 0,20 ):prodName);
			row.put("value005", prodDesc);//pdf&&prodDesc.length()>20?prodDesc.substring( 0,20 ):prodDesc);

			int count = countHash.get(temp.getProductNum()).getLocation(locationName);
			String hbin = hm.get(temp.getProductNum());
			hbin = hbin!=null?hbin:"0";
			if(!consolidatedList){
				row.put("value006", pdf?temp.getTransaction().getProductQty()+"/"+ count +"/"+hbin:"'"+temp.getTransaction().getProductQty()+"/"+ temp.getNumAvailable()+"'" );
				row.put("value007", temp.getTransaction().getInvoiceNum());
				row.put("value008", temp.getTransaction().getTransDateFormatted());
			}else{
				row.put("value006", pdf?temp.getTransaction().getProductQty()+"/"+ count +"/"+hbin:"'"+temp.getTransaction().getProductQty()+"/"+ temp.getNumAvailable()+"'" );
				row.put("value007", countHash.get( temp.getProductNum() ).getLocation( loc1 )+"");
				row.put("value008", countHash.get( temp.getProductNum() ).getLocation( loc2 )+"");
			}
			reportData.add( row );
		}
		try {
			Date date1 = new SimpleDateFormat("MM-dd-yyyy HH:mm").parse( dateOne );
			Date date2 = new SimpleDateFormat("MM-dd-yyyy HH:mm").parse( dateTwo );
			parameters.put("fromDate", date1);
			parameters.put("toDate", date2);
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
