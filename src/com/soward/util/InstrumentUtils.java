package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.soward.db.DB;
import com.soward.object.Instrument;

public class InstrumentUtils {
	public static Instrument fetchForPid(String pid) throws Exception{
		Connection con = null;
		DB db = new DB();
		Connection conn = db.openConnection();
		Statement stm = conn.createStatement();
		String sql = "";
		sql = "select * from instrument where pid = "+pid;
		ResultSet rset = stm.executeQuery( sql );
		List<Instrument> inList = processList(rset);
		Instrument inst = new Instrument();
		if(inList!=null&&!inList.isEmpty()){
			inst = inList.get(0);
		}
		rset.close();
		conn.close();
		stm.close();
		return inst;
	}
	public static List<Instrument> searchForName(String srhName) throws Exception{
		DB db = new DB();
		Connection conn = db.openConnection();
		PreparedStatement pstmt = null;
		String sql = "";
		sql = "select * from instrument where name like ?";
		pstmt = conn.prepareStatement( sql );
		pstmt.setString( 1,   "%"+srhName+"%" );
		ResultSet rset = pstmt.executeQuery();
		List<Instrument> inList = processList(rset);
		rset.close();
		conn.close();
		pstmt.close();
		return inList;
	}
	private static List<Instrument> processList(ResultSet rset) throws Exception{
		List<Instrument> inList = new ArrayList<Instrument>();
		while(rset.next()){
			Instrument instrument = new Instrument();
			instrument.setName         (rset.getString("name"));
			instrument.setItemNumber   (rset.getString("itemNumber"));
			instrument.setPid          (rset.getString("pid"));
			instrument.setSupplier     (rset.getString("supplier"));
			instrument.setType         (rset.getString("type"));
			inList.add(instrument);
		}
		return inList;
	}
	public static List<Instrument> fetchAll() throws Exception{
		Connection con = null;
		DB db = new DB();
		Connection conn = db.openConnection();
		Statement stm = conn.createStatement();
		String sql = "";
		sql = "select * from instrument";
		ResultSet rset = stm.executeQuery( sql );
		List<Instrument> pList = processList(rset);
		rset.close();
		conn.close();
		stm.close();
		return pList;
	}


	//returns user pid
	public static String saveOrUpdate( Instrument instrument) throws Exception{
		String errorSql = "";
		DB db = new DB();
		String key = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = db.openConnection();
			String sql = "";
			key = "";
			if(!StringUtils.isBlank( instrument.getPid() )){
				key = instrument.getPid();
				sql = "update instrument set "+
				"name            = ?, "+
				"itemNumber      = ?, "+
				"supplier        = ?, "+
				"type            = ?  "+
				"where pid=?";

				pstmt = conn.prepareStatement( sql );
				pstmt.setString( 1,   instrument.getName      ());
				pstmt.setString( 2,   instrument.getItemNumber());
				pstmt.setString( 3,   instrument.getSupplier  ());
				pstmt.setString( 4,   instrument.getType      ());
				pstmt.setString( 5,   instrument.getPid       ());
				pstmt.executeUpdate();
				conn.close();

			}else{
				sql = "insert into instrument (pid, name  ,itemNumber   ,supplier   ,type )"+ 
				"values(null, ?,?,?,?)";
				pstmt = conn.prepareStatement( sql ); 
				pstmt.setString( 1,   instrument.getName      ());
				pstmt.setString( 2,   instrument.getItemNumber());
				pstmt.setString( 3,   instrument.getSupplier  ());
				pstmt.setString( 4,   instrument.getType      ());
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
			Instrument instrument = new Instrument();
			            instrument.setPid( "2" );
			instrument.setName       ("scottLaRock333");
			instrument.setItemNumber ("soward");
			instrument.setSupplier   ("169");
			instrument.setType       ("apple");
//			InstrumentUtils.saveOrUpdate( instrument);
			List<Instrument> inList = InstrumentUtils.searchForName( "scott");
			for(Instrument in: inList){
				System.out.println(in.getName());
			}
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
