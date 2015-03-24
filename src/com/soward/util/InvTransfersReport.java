package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.soward.db.DB;
import com.soward.db.MySQL;

public class InvTransfersReport {
	public static void main(String args[]){
		List<Map<String, String>> al = InvTransfersReport.getInventoryTransferReport(  "2008-12-15", "2009-12-16", "MURRAY", null);
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
	public static List<Map<String, String>> getInventoryTransferReport( String startDate, String endDate, String location, String locationTo) {

		List<Map<String, String>> prodList = new ArrayList<Map<String, String>>();
		Connection con = null;
		DB db = new DB();

		String sql = "Select * from productMoves where ";
		sql += " \nmoveDate > '" + startDate +"' and moveDate< '"+endDate+"' ";
		if(!StringUtils.isBlank(locationTo)){
			sql += " and locTo ='"+locationTo+"'";
		}
		if(!StringUtils.isBlank(location)){
			sql += "and locFrom = '"+location+"'";
		}

		try {
			con = db.openConnection();
			PreparedStatement pstmt = null;
			pstmt = con.prepareStatement( sql );
			ResultSet rset = pstmt.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy h:mm a");
			SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while ( rset.next() ) {
				HashMap<String, String> result = new HashMap<String,String>();
				result.put("moveDate", sdf.format(sdff.parse(rset.getString("moveDate"))));
				result.put("moveUser", rset.getString( "moveUser" ));
				result.put("moveAmt", rset.getString( "moveAmt" ));
				result.put("moveTo", rset.getString( "locTo" ));
				result.put("moveFrom", rset.getString( "locFrom" ));
				result.put("prodNum", rset.getString( "prodNum" ));

				prodList.add( result );
			}
			pstmt.close();
			con.close();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return prodList;
	} 
}
