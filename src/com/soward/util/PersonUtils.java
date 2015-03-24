package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.soward.db.DB;
import com.soward.enums.PersonType;
import com.soward.object.Person;

public class PersonUtils {


	public static Person fetchForPid(String pid) throws Exception{
		Connection con = null;
		DB db = new DB();
		Connection conn = db.openConnection();
		Statement stm = conn.createStatement();
		String sql = "";
		Person person = new Person();
		sql = "select * from person where person_pid = "+pid;
		ResultSet rset = stm.executeQuery( sql );
		while(rset.next()){
			person.setFirstName      (rset.getString("firstName"));
			person.setLastName       (rset.getString("lastName"));
			person.setAddress1       (rset.getString("address1"));
			person.setCity           (rset.getString("city"));
			person.setCounty         (rset.getString("county"));
			person.setState          (rset.getString("state"));
			person.setPhone          (rset.getString("phone"));
			person.setCell           (rset.getString("cell"));
			person.setEmail          (rset.getString("email"));
			person.setNotes          (rset.getString("notes"));
			person.setPerson_type    (rset.getString("person_type"));
			person.setPerson_pid    (rset.getString("person_pid"));
		}
		rset.close();
		conn.close();
		stm.close();
		return person;
	}
	public static List<Person> fetchAllForType(PersonType ptype ) throws Exception{
		Connection con = null;
		DB db = new DB();
		Connection conn = db.openConnection();
		Statement stm = conn.createStatement();
		String sql = "";
		sql = "select * from person where person_type = '"+ptype.name()+"'";
		ResultSet rset = stm.executeQuery( sql );
		List<Person> pList = new ArrayList<Person>();
		while(rset.next()){
			Person person = new Person();
			person.setFirstName      (rset.getString("firstName"));
			person.setLastName       (rset.getString("lastName"));
			person.setAddress1       (rset.getString("address1"));
			person.setCity           (rset.getString("city"));
			person.setCounty         (rset.getString("county"));
			person.setState          (rset.getString("state"));
			person.setPhone          (rset.getString("phone"));
			person.setCell           (rset.getString("cell"));
			person.setEmail          (rset.getString("email"));
			person.setNotes          (rset.getString("notes"));
			person.setPerson_type    (rset.getString("person_type"));
			person.setPerson_pid     (rset.getString("person_pid"));
			pList.add( person );
		}
		rset.close();
		conn.close();
		stm.close();
		return pList;
	}

	public static List<Person> fetchForParams(PersonType ptype, String srhTeach,String srhCou) throws Exception{
		if(null==ptype)
			return null;
		DB db = new DB();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Person> pList = null;
		try {
			conn = db.openConnection();
			String sql = "";
			int inx = 1;
			sql = "select * from person where person_type = ? ";//'"+ptype.name()+"'";
			if(!StringUtils.isBlank(srhTeach)){
				sql += " and (lastName = ? or firstName = ?)";
			}
			if(!StringUtils.isBlank(srhCou)){
				sql += " and county=?";
			}
			pstmt = conn.prepareStatement( sql );
			pstmt.setString(inx, ptype.name());
			if(!StringUtils.isBlank(srhTeach)){
				inx++;
				pstmt.setString( inx, srhTeach);
				inx++;
				pstmt.setString( inx, srhTeach);

			}
			if(!StringUtils.isBlank(srhCou)){
				inx++;
				pstmt.setString(inx, srhCou);
			}

			rset = pstmt.executeQuery();
			pList = new ArrayList<Person>();
			while(rset.next()){
				Person person = new Person();
				person.setFirstName      (rset.getString("firstName"));
				person.setLastName       (rset.getString("lastName"));
				person.setAddress1       (rset.getString("address1"));
				person.setCity           (rset.getString("city"));
				person.setCounty         (rset.getString("county"));
				person.setState          (rset.getString("state"));
				person.setPhone          (rset.getString("phone"));
				person.setCell           (rset.getString("cell"));
				person.setEmail          (rset.getString("email"));
				person.setNotes          (rset.getString("notes"));
				person.setPerson_type    (rset.getString("person_type"));
				person.setPerson_pid     (rset.getString("person_pid"));
				pList.add( person );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
				pstmt.close();
				rset.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pList;
	}
	//returns user pid
	public static String saveOrUpdate( Person person) throws Exception{
		String errorSql = "";
		DB db = new DB();
		String key = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = db.openConnection();
			String sql = "";
			key = "";
			if(!StringUtils.isBlank( person.getPerson_pid() )){
				key = person.getPerson_pid();
				sql = "update person set "+
				"firstName       = ?, "+
				"lastName        = ?, "+
				"address1        = ?, "+
				"city            = ?, "+
				"county          = ?, "+
				"state           = ?, "+
				"phone           = ?, "+
				"cell            = ?, "+
				"email           = ?, "+
				"notes           = ?, "+
				"person_type     = ? "+
				"where person_pid=?";

				pstmt = conn.prepareStatement( sql );
				pstmt.setString( 1,   person.getFirstName      ());
				pstmt.setString( 2,   person.getLastName       ());
				pstmt.setString( 3,   person.getAddress1       ());
				pstmt.setString( 4,   person.getCity           ());
				pstmt.setString( 5,   person.getCounty         ());
				pstmt.setString( 6,   person.getState          ());
				pstmt.setString( 7,   person.getPhone          ());
				pstmt.setString( 8,   person.getCell           ());
				pstmt.setString( 9,   person.getEmail          ());
				pstmt.setString( 10,  person.getNotes          ());
				pstmt.setString( 11,  person.getPerson_type    ());
				pstmt.setString( 12,  person.getPerson_pid     ());
				pstmt.executeUpdate();
				conn.close();

			}else{
				sql = "insert into person (person_pid, firstName  ,lastName   ,address1   ,city       ," +
				"county     ,state      ,phone      ,cell       ,email      ,notes      ,person_type )"+ 
				"values(null, ?,?,?,?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement( sql ); 
				pstmt.setString( 1, person.getFirstName      () );
				pstmt.setString( 2, person.getLastName       () );
				pstmt.setString( 3, person.getAddress1       () );
				pstmt.setString( 4, person.getCity           () );
				pstmt.setString( 5, person.getCounty         () );
				pstmt.setString( 6, person.getState          () );
				pstmt.setString( 7, person.getPhone          () );
				pstmt.setString( 8, person.getCell           () );
				pstmt.setString( 9, person.getEmail          () );
				pstmt.setString( 10, person.getNotes          () );
				pstmt.setString( 11, person.getPerson_type    () );
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
			Person person = new Person();
			//            person.setPerson_pid( "2" );
			person.setFirstName      ("scottLaRock")                                                                           ;
			person.setLastName       ("soward")                                                                          ;
			person.setAddress1       ("169")                                                                             ;
			person.setAddress2       ("apple")                                                                           ;
			person.setCity           ("saratoga")                                                                        ;
			person.setCounty         ("utahcounty")                                                                      ;
			person.setState          ("utah")                                                                            ;
			person.setPhone          ("776")                                                                             ;
			person.setCell           ("664")                                                                             ;
			person.setEmail          ("amorvivir")                                                                       ;
			person.setNotes          ("this is an example of a very short note that will be caputured in this field.")   ;
			person.setPerson_type    ("teacher")                                                                         ;
			//PersonUtils.saveOrUpdate( person);
			PersonUtils.fetchForParams(PersonType.TEACHER, "soward", "Beaver");
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
