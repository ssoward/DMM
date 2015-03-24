package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.soward.db.DB;
import com.soward.db.MySQL;
import com.soward.enums.LocationsDBName;
import com.soward.object.Supplier;
import com.soward.object.SupplierData;

public class SupplierUtil {

	public static HashMap<String, Supplier> getAllSuppliersHashWithNumKey(){
		ArrayList<Supplier> supList = SupplierUtil.getAllSuppliersNumAndName();
		HashMap<String, Supplier> supHash = new HashMap<String,Supplier>();

		for(Supplier sup: supList){
			supHash.put( sup.getSupplierNum(), sup );
		}
		return supHash;
	}

	public static ArrayList<Supplier> getAllSuppliersNumAndName(){
		Connection con = null;
		ArrayList<Supplier> allSups = new ArrayList<Supplier>();
		MySQL sdb = new MySQL();
		String sql = "select * from Suppliers order by supplierName";
		try {
			con = sdb.getConn();
			PreparedStatement pstmt = null;
			pstmt = con.prepareStatement( sql );
			ResultSet rset = pstmt.executeQuery();
			while ( rset.next() ) {
				Supplier sup = new Supplier();
				sup.setSupplierNum        (rset.getString("supplierNum"));
				sup.setSupplierName       (rset.getString("supplierName"));
				sup.setSupplierContact    (rset.getString("supplierContact"));
				sup.setSupplierStreet     (rset.getString("supplierStreet"));
				sup.setSupplierCity       (rset.getString("supplierCity"));
				sup.setSupplierState      (rset.getString("supplierState"));
				sup.setSupplierPostalCode (rset.getString("supplierPostalCode"));
				sup.setSupplierCountry    (rset.getString("supplierCountry"));
				sup.setSupplierPhone      (rset.getString("supplierPhone"));
				sup.setSupplierFax        (rset.getString("supplierFax"));
				sup.setSupplierEmail      (rset.getString("supplierEmail"));
				sup.setSupplierSite       (rset.getString("supplierSite"));
				allSups.add(sup);
			}
			pstmt.close();
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return allSups;
	}

	public static HashMap<String, Supplier> getAllSuppliersHash(){
		HashMap<String, Supplier> sMap = new HashMap<String, Supplier>();
		ArrayList<Supplier> sList = getAllSuppliersNumAndName();
		for(Supplier s: sList){
			sMap.put(s.getSupplierNum(), s);
		}
		return sMap;

	}
	public static HashMap<String, SupplierData> getAllSuppliersData(){
		Connection con = null;
		HashMap<String, SupplierData> allSups = new HashMap<String, SupplierData>();
		DB db = new DB();
		String sql = "select * from Suppliers order by supplierName";
		try {
			con = db.openConnection();
			PreparedStatement pstmt = null;
			pstmt = con.prepareStatement( sql );
			ResultSet rset = pstmt.executeQuery();
			while ( rset.next() ) {
				SupplierData sup = new SupplierData();
				sup.setId            (rset.getInt("id"));
				sup.setSupplierNum   (rset.getInt("supplierNum"));
				sup.setMurrayThr     (rset.getDouble("murray_thr"));
				sup.setLehiThr       (rset.getDouble("lehi_thr"));
				sup.setOremThr       (rset.getDouble("orem_thr"));
				sup.setEContent      (rset.getString("email_content"));
				allSups.put(sup.getId()+"", sup);
			}
			pstmt.close();
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return allSups;
	}
	public static SupplierData fetchSuppliersData(String supplierNum){
		Connection con = null;
		HashMap<String, SupplierData> allSups = new HashMap<String, SupplierData>();
		if(StringUtils.isBlank(supplierNum))
			return null;
		DB db = new DB();
		String sql = "select * from supplierdata where supplierNum = ?";
		SupplierData sup = null;
		try {
			con = db.openConnection();
			PreparedStatement pstmt = null;
			pstmt = con.prepareStatement( sql );
			pstmt.setInt(1, Integer.parseInt(supplierNum));
			ResultSet rset = pstmt.executeQuery();
			if ( rset.next() ) {
				sup = new SupplierData();
				sup.setId            (rset.getInt("id"));
				sup.setSupplierNum   (rset.getInt("supplierNum"));
				sup.setMurrayThr     (rset.getDouble("murray_thr"));
				sup.setLehiThr       (rset.getDouble("lehi_thr"));
				sup.setOremThr       (rset.getDouble("orem_thr"));
				sup.setEContent      (rset.getString("email_content"));

			}
			pstmt.close();
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return sup;
	}

	public static Supplier getSupplierForNum(String num){
		Connection con = null;
		MySQL sdb = new MySQL();
		String sql = "select * from Suppliers where supplierNum = "+num;
		Supplier sup = new Supplier();
		try {
			con = sdb.getConn();
			PreparedStatement pstmt = null;
			pstmt = con.prepareStatement( sql );
			ResultSet rset = pstmt.executeQuery();
			if ( rset.next() ) {
				sup.setSupplierNum        (rset.getString("supplierNum"));
				sup.setSupplierName       (rset.getString("supplierName"));
				sup.setSupplierContact    (rset.getString("supplierContact"));
				sup.setSupplierStreet     (rset.getString("supplierStreet"));
				sup.setSupplierCity       (rset.getString("supplierCity"));
				sup.setSupplierState      (rset.getString("supplierState"));
				sup.setSupplierPostalCode (rset.getString("supplierPostalCode"));
				sup.setSupplierCountry    (rset.getString("supplierCountry"));
				sup.setSupplierPhone      (rset.getString("supplierPhone"));
				sup.setSupplierFax        (rset.getString("supplierFax"));
				sup.setSupplierEmail      (rset.getString("supplierEmail"));
				sup.setSupplierSite       (rset.getString("supplierSite"));
			}
			pstmt.close();
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return sup;
	}
	public static ArrayList<Supplier> fetchSupForName(String name){
		Connection con = null;
		MySQL sdb = new MySQL();
		String sql = "select * from Suppliers where supplierName like '%"+name.toUpperCase()+"%'";
		ArrayList<Supplier> supList = new ArrayList<Supplier>();
		try {
			con = sdb.getConn();
			PreparedStatement pstmt = null;
			pstmt = con.prepareStatement( sql );
			ResultSet rset = pstmt.executeQuery();
			while ( rset.next() ) {
				Supplier sup = new Supplier();
				sup.setSupplierNum        (rset.getString("supplierNum"));
				sup.setSupplierName       (rset.getString("supplierName"));
				sup.setSupplierContact    (rset.getString("supplierContact"));
				sup.setSupplierStreet     (rset.getString("supplierStreet"));
				sup.setSupplierCity       (rset.getString("supplierCity"));
				sup.setSupplierState      (rset.getString("supplierState"));
				sup.setSupplierPostalCode (rset.getString("supplierPostalCode"));
				sup.setSupplierCountry    (rset.getString("supplierCountry"));
				sup.setSupplierPhone      (rset.getString("supplierPhone"));
				sup.setSupplierFax        (rset.getString("supplierFax"));
				sup.setSupplierEmail      (rset.getString("supplierEmail"));
				sup.setSupplierSite       (rset.getString("supplierSite"));
				supList.add( sup );
			}
			pstmt.close();
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return supList;
	}
	public static ArrayList<Supplier> fetchSupForNameOrNum(String name){
		Connection con = null;
		MySQL sdb = new MySQL();
		String sql = "select * from Suppliers where supplierName like '%"+name.toUpperCase()+"%' ";
		try{
			sql += " or supplierNum = "+ Integer.parseInt( name );
		}catch(Exception e){
			//no a number
		}
		ArrayList<Supplier> supList = new ArrayList<Supplier>();
		try {
			con = sdb.getConn();
			PreparedStatement pstmt = null;
			pstmt = con.prepareStatement( sql );
			ResultSet rset = pstmt.executeQuery();
			while ( rset.next() ) {
				Supplier sup = new Supplier();
				sup.setSupplierNum        (rset.getString("supplierNum"));
				sup.setSupplierName       (rset.getString("supplierName"));
				sup.setSupplierContact    (rset.getString("supplierContact"));
				sup.setSupplierStreet     (rset.getString("supplierStreet"));
				sup.setSupplierCity       (rset.getString("supplierCity"));
				sup.setSupplierState      (rset.getString("supplierState"));
				sup.setSupplierPostalCode (rset.getString("supplierPostalCode"));
				sup.setSupplierCountry    (rset.getString("supplierCountry"));
				sup.setSupplierPhone      (rset.getString("supplierPhone"));
				sup.setSupplierFax        (rset.getString("supplierFax"));
				sup.setSupplierEmail      (rset.getString("supplierEmail"));
				sup.setSupplierSite       (rset.getString("supplierSite"));
				supList.add( sup );
			}
			pstmt.close();
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return supList;
	}
	public static String updateAllProdsDiscountForSupNum(String supNum, String discount){
		Connection con = null;
		MySQL sdb = new MySQL();
		String msg = "update ";
		String sql = "update "+LocationsDBName.MURRAY.dbName();
		ArrayList<Supplier> supList = new ArrayList<Supplier>();
		Supplier sup = SupplierUtil.getSupplierForNum( supNum );

		try {
			double disc = Double.parseDouble( discount );
			con = sdb.getConn();
			PreparedStatement pstmt = null;
			pstmt = con.prepareStatement( "update "+LocationsDBName.MURRAY.dbName()+" set productCost2= "+disc+"where productSupplier1="+supNum );
			pstmt.executeUpdate();
			pstmt = con.prepareStatement( "update "+LocationsDBName.LEHI.dbName()+" set productCost2= "+disc+"where productSupplier1="+supNum );
			pstmt.executeUpdate();
			msg = "Successfully set ALL products discount (productCost2) to: "+discount+" for "+sup.getSupplierName()+".";
			pstmt.close();
			con.close();
		}catch(Exception e){
			e.printStackTrace();
			msg = "Something went terrible wrong, get under the desk NOW!";
		}
		return msg;
	}
	public static void main(String args[]){
		//		System.out.println(SupplierUtil.updateAllProdsDiscountForSupNum( "25", "2.5" ));
		SupplierData sData = new SupplierData();
		sData.setSupplierNum(28);
		sData.setMurrayThr(400.00);
		sData.setOremThr(1000.00);
		sData.setLehiThr(500.00);
		saveUpdateSupplierData(sData);
		System.out.println(sData.getId());
		System.out.println(sData.getMurrayThr());
	}

	public static void saveUpdateSupplierData(SupplierData sData) {
		Connection con = null;
		HashMap<String, SupplierData> allSups = new HashMap<String, SupplierData>();
		DB db = new DB();

		SupplierData sup = null;
		try {
			String updateSql = "";
			con = db.openConnection();
			PreparedStatement pstmt = null;
			if(sData.getSupplierNum()>0){
				String sql = "select * from supplierdata where supplierNum = ?";
				pstmt = con.prepareStatement( sql );
				pstmt.setInt(1, sData.getSupplierNum());
				ResultSet rset = pstmt.executeQuery();
				if ( rset.next() ) {
					sup = new SupplierData();
					sup.setId            (rset.getInt("id"));
					sup.setSupplierNum   (sData.getSupplierNum()>0?sData.getSupplierNum():rset.getInt("supplierNum"));
					sup.setMurrayThr     (sData.getMurrayThr()!=null?sData.getMurrayThr():rset.getDouble("murray_thr"));
					sup.setLehiThr       (sData.getLehiThr()!=null?sData.getLehiThr():rset.getDouble("lehi_thr"));
					sup.setOremThr       (sData.getOremThr()!=null?sData.getOremThr():rset.getDouble("orem_thr"));
					sup.setEContent      (!StringUtils.isBlank(sData.getEContent())?sData.getEContent():rset.getString("email_content"));
					updateSql = "update supplierdata set murray_thr = ?, orem_thr = ?, lehi_thr=?, email_content=? where id =?";
					pstmt = con.prepareStatement( updateSql );
					pstmt.setDouble(1, sup.getMurrayThr()!=null?sup.getMurrayThr():0.00);
					pstmt.setDouble(2, sup.getOremThr()!=null?sup.getOremThr():0.00);
					pstmt.setDouble(3, sup.getLehiThr()!=null?sup.getLehiThr():0.00);
					pstmt.setString(4, sup.getEContent()!=null?sup.getEContent():"");
					pstmt.setInt(5, sup.getId());
					pstmt.executeUpdate();

				}else{
					updateSql = "insert into supplierdata values(null, ?,?,?,?,?)";
					pstmt = con.prepareStatement( updateSql );
					pstmt.setInt(1, sData.getSupplierNum());
					pstmt.setDouble(2, sData.getMurrayThr()!=null?sData.getMurrayThr():0.00);
					pstmt.setDouble(3, sData.getOremThr()!=null?sData.getOremThr():0.00);
					pstmt.setDouble(4, sData.getLehiThr()!=null?sData.getLehiThr():0.00);
					pstmt.setString(5, sData.getEContent()!=null?sData.getEContent():"");
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static boolean saveUpdateSupplier(Supplier supUpdate) {
		Connection con = null;
		HashMap<String, SupplierData> allSups = new HashMap<String, SupplierData>();
		MySQL sdb = new MySQL();
		boolean success = false;


		String updateSql = "";
		try {
			con = sdb.getConn();
			PreparedStatement pstmt = null;
			if(!StringUtils.isBlank(supUpdate.getSupplierNum())){
				String sql = "select * from Suppliers where supplierNum = ?";
				pstmt = con.prepareStatement( sql );
				pstmt.setInt(1, Integer.parseInt(supUpdate.getSupplierNum()));
				ResultSet rset = pstmt.executeQuery();
				if ( rset.next() ) {
					Supplier sup = new Supplier();
					sup.setSupplierName      (!StringUtils.isBlank(supUpdate.getSupplierName())?supUpdate.getSupplierName():rset.getString("supplierName"));
					sup.setSupplierContact   (!StringUtils.isBlank(supUpdate.getSupplierContact    ())?supUpdate.getSupplierContact    ():rset.getString("supplierContact"));
					sup.setSupplierStreet    (!StringUtils.isBlank(supUpdate.getSupplierStreet     ())?supUpdate.getSupplierStreet     ():rset.getString("supplierStreet"));
					sup.setSupplierCity      (!StringUtils.isBlank(supUpdate.getSupplierCity       ())?supUpdate.getSupplierCity       ():rset.getString("supplierCity"));
					sup.setSupplierState     (!StringUtils.isBlank(supUpdate.getSupplierState      ())?supUpdate.getSupplierState      ():rset.getString("supplierState"));
					sup.setSupplierPostalCode(!StringUtils.isBlank(supUpdate.getSupplierPostalCode ())?supUpdate.getSupplierPostalCode ():rset.getString("supplierPostalCode"));
					sup.setSupplierCountry   (!StringUtils.isBlank(supUpdate.getSupplierCountry    ())?supUpdate.getSupplierCountry    ():rset.getString("supplierCountry"));
					sup.setSupplierPhone     (!StringUtils.isBlank(supUpdate.getSupplierPhone      ())?supUpdate.getSupplierPhone      ():rset.getString("supplierPhone"));
					sup.setSupplierFax       (!StringUtils.isBlank(supUpdate.getSupplierFax        ())?supUpdate.getSupplierFax        ():rset.getString("supplierFax"));
					sup.setSupplierEmail     (!StringUtils.isBlank(supUpdate.getSupplierEmail      ())?supUpdate.getSupplierEmail      ():rset.getString("supplierEmail"));
					sup.setSupplierSite      (!StringUtils.isBlank(supUpdate.getSupplierSite       ())?supUpdate.getSupplierSite       ():rset.getString("supplierSite"));
					updateSql = "update Suppliers set ";
					updateSql += " supplierName        =?,";
					updateSql += " supplierContact     =?,";
					updateSql += " supplierStreet      =?,";
					updateSql += " supplierCity        =?,";
					updateSql += " supplierState       =?,";
					updateSql += " supplierPostalCode  =?,";
					updateSql += " supplierCountry     =?,";
					updateSql += " supplierPhone       =?,";
					updateSql += " supplierFax         =?,";
					updateSql += " supplierEmail       =?,";
					updateSql += " supplierSite        =? ";
					updateSql +=" where supplierNum = ? ";

					pstmt = con.prepareStatement( updateSql );
					pstmt.setString(1, sup.getSupplierName       ());
					pstmt.setString(2, sup.getSupplierContact    ());
					pstmt.setString(3, sup.getSupplierStreet     ());
					pstmt.setString(4, sup.getSupplierCity       ());
					pstmt.setString(5, sup.getSupplierState      ());
					pstmt.setString(6, sup.getSupplierPostalCode ());
					pstmt.setString(7, sup.getSupplierCountry    ());
					pstmt.setString(8, sup.getSupplierPhone      ());
					pstmt.setString(9, sup.getSupplierFax        ());
					pstmt.setString(10, sup.getSupplierEmail      ());
					pstmt.setString(11, sup.getSupplierSite       ());
					pstmt.setInt(12, Integer.parseInt(supUpdate.getSupplierNum()));
					pstmt.executeUpdate();
					success = true;


				}else{
					updateSql = "insert into Supplier values(null, ?,?,?,?,?)";
				}
			}
			pstmt.close();
			con.close();
		}catch(Exception e){
			System.out.println(updateSql);
			e.printStackTrace();
		}
		return success;
	}

}
