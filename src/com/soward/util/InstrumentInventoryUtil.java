package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.soward.db.DB;
import com.soward.object.InstrumentInventory;

public class InstrumentInventoryUtil {
	public static InstrumentInventory fetchForPid(String pid) throws Exception{
		Connection con = null;
		DB db = new DB();
		Connection conn = db.openConnection();
		Statement stm = conn.createStatement();
		String sql = "";
		sql = "select * from instrumentInventory where pid = "+pid;
		ResultSet rset = stm.executeQuery( sql );
		List<InstrumentInventory> inList = processList(rset);
		InstrumentInventory inst = new InstrumentInventory();
		if(inList!=null&&!inList.isEmpty()){
			inst = inList.get(0);
		}
		rset.close();
		conn.close();
		stm.close();
		return inst;
	}
	public static List<InstrumentInventory> searchForLocation(String srhName) throws Exception{
		DB db = new DB();
		Connection conn = db.openConnection();
		PreparedStatement pstmt = null;
		String sql = "";
		sql = "select * from instrumentInventory where location like ?";
		pstmt = conn.prepareStatement( sql );
		pstmt.setString( 1,   "%"+srhName+"%" );
		ResultSet rset = pstmt.executeQuery();
		List<InstrumentInventory> inList = processList(rset);
		rset.close();
		conn.close();
		pstmt.close();
		return inList;
	}
	public static List<InstrumentInventory> searchForLocationInstrPid(String srhName, String instrPid) throws Exception{
		DB db = new DB();
		Connection conn = db.openConnection();
		PreparedStatement pstmt = null;
		String sql = "";
		sql = "select * from instrumentInventory where location = ? and instrPid = ?";
		pstmt = conn.prepareStatement( sql );
		pstmt.setString( 1,   srhName );
		pstmt.setString( 2,   instrPid );
		ResultSet rset = pstmt.executeQuery();
		List<InstrumentInventory> inList = processList(rset);
		rset.close();
		conn.close();
		pstmt.close();
		return inList;
	}
	public static HashMap<String,InstrumentInventory> getAllHash() throws Exception{
		DB db = new DB();
		Connection conn = db.openConnection();
		PreparedStatement pstmt = null;
		String sql = "";
		sql = "select * from instrumentInventory";
		pstmt = conn.prepareStatement( sql );
		ResultSet rset = pstmt.executeQuery();
		List<InstrumentInventory> inList = processList(rset);
		rset.close();
		conn.close();
		pstmt.close();
		HashMap<String,InstrumentInventory> inHash = new HashMap<String,InstrumentInventory>();
		for(InstrumentInventory ii: inList){
			inHash.put(ii.getLocation()+ii.getInstrPid(), ii);
		}
		return inHash;
	}
	private static List<InstrumentInventory> processList(ResultSet rset) throws Exception{
		List<InstrumentInventory> inList = new ArrayList<InstrumentInventory>();
		while(rset.next()){
			InstrumentInventory instrumentInventory = new InstrumentInventory();
			instrumentInventory.setLocation     (rset.getString("location"));
			instrumentInventory.setInstrPid     (rset.getString("instrPid"));
			instrumentInventory.setPid          (rset.getString("pid"));
			instrumentInventory.setCount        (rset.getString("count"));
			inList.add(instrumentInventory);
		}
		return inList;
	}
	public static List<InstrumentInventory> fetchAll() throws Exception{
		Connection con = null;
		DB db = new DB();
		Connection conn = db.openConnection();
		Statement stm = conn.createStatement();
		String sql = "";
		sql = "select * from instrumentInventory";
		ResultSet rset = stm.executeQuery( sql );
		List<InstrumentInventory> pList = processList(rset);
		rset.close();
		conn.close();
		stm.close();
		return pList;
	}


	//returns user pid
	public static String saveOrUpdate( InstrumentInventory instrumentInventory) throws Exception{
		String errorSql = "";
		DB db = new DB();
		String key = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = db.openConnection();
			String sql = "";
			key = "";
			if(!StringUtils.isBlank( instrumentInventory.getPid() )){
				key = instrumentInventory.getPid();
				sql = "update instrumentInventory set "+
				"location            = ?, "+
				"instrPid      = ?, "+
				"count            = ?  "+
				"where pid=?";

				pstmt = conn.prepareStatement( sql );
				pstmt.setString( 1,   instrumentInventory.getLocation());
				pstmt.setString( 2,   instrumentInventory.getInstrPid());
				pstmt.setString( 3,   instrumentInventory.getCount   ());
				pstmt.setString( 4,   instrumentInventory.getPid       ());
				pstmt.executeUpdate();
				conn.close();

			}else{
				sql = "insert into instrumentInventory (pid, location  ,instrPid   ,count )"+ 
				"values(null, ?,?,?)";
				pstmt = conn.prepareStatement( sql ); 
				pstmt.setString( 1,   instrumentInventory.getLocation      ());
				pstmt.setString( 2,   instrumentInventory.getInstrPid());
				pstmt.setString( 3,   instrumentInventory.getCount  ());
				pstmt.executeUpdate();
				ResultSet keys = pstmt.getGeneratedKeys();
				if(keys.next()){
					key = keys.getString( 1 );
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return key;
	}
	
	public static void main(String args[]){
		try {
			InstrumentInventory instrumentInventory = new InstrumentInventory();
			            instrumentInventory.setPid( "2" );
			instrumentInventory.setLocation       ("MURRAY");
			instrumentInventory.setInstrPid   ("scottsowardlouis");
			instrumentInventory.setCount      ("169");
			InstrumentInventoryUtil.saveOrUpdate( instrumentInventory);
//			List<InstrumentInventory> inList = InstrumentInventoryUtil.searchForLocation( "MURRAY");
			List<InstrumentInventory> inList = InstrumentInventoryUtil.searchForLocationInstrPid( "MURRAY", "soward");
			for(InstrumentInventory in: inList){
				System.out.println(in.getPid()+" "+in.getLocation()+" "+in.getInstrPid());
			}
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
