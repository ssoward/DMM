package com.soward.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


import com.sybase.jdbc3.jdbc.SybDriver;

public class PopulateWebProductInfo {

	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		System.out.println("hello world");
		//        new PopulateWebProductInfo().getFiles(args[0]);    
		new PopulateWebProductInfo().getFiles("C:/sandbox/workspace/features");    
	}
	public void getFiles(String path){
		try {
			File dir = new File(path);
			//          File dir = new File("/Users/scottsoward/sandbox/Workspace/DMM/blurb");

			String[] children = dir.list();
			BufferedReader in = null;
			if (children == null) {
				System.out.println("children are empty");
				// Either dir does not exist or is not a directory
			} else {
				int count = 0;
				int totCount = 0;
				System.out.println("FileName: "+path);
				for (int i=0; i<children.length; i++) {
					// Get filename of file or directory
					String filename = children[i];
					if(!filename.equals( "deleteMe.sh" )){
						System.out.println("==========================================================");
						try {
							in = new BufferedReader(new FileReader(dir.getAbsolutePath()+"/"+filename));
							String str = "";
							String totStr = "";
							while ((str = in.readLine()) != null) {
								totStr+=str;
							}
							if(totStr!=null&&totStr.length()>0){
								String prodNum = filename.substring( 0, filename.indexOf(".html") );
								String prodFeat = totStr.replaceAll( "'", "''" );
								System.out.println("productNum: '"+prodNum+"'");
								System.out.println("productFeature: "+prodFeat);
								WebProductInfo webProdInfo = new WebProductInfo();
								//webProdInfo.setProductBlurb( prodBlurb );
								webProdInfo.setProductNum( prodNum );
								webProdInfo.setProductFeature( prodFeat );
								if(prodFeat!=null&&prodFeat.length()>0){
									saveOrUpdate( webProdInfo, new MySQL() );
								}
								count++;
								System.out.println(count);
								if(count>0)
								System.exit(1);
							}
							in.close();
						} catch ( Exception e ) {
							//not a file
						}
					}
					totCount++;
				}
				System.out.print(" count:"+count);
				System.out.println(" totalCount: "+totCount);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static String saveOrUpdate( WebProductInfo webProdInfo, MySQL sdb) {
        Connection con = null;
        
        String result = "Successfully updated FEATURE text for productNumber: " + webProdInfo.getProductNum();
        try {
            String sql = "SELECT    *   "
                + " FROM WebProductInfo where productNum=?";
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString( 1, webProdInfo.getProductNum() );
            ResultSet rset = pstmt.executeQuery();
            if(rset.next()){
                //update
                sql = "update WebProductInfo set productFeature =? where productNum =?";
                pstmt.close();
                pstmt = con.prepareStatement( sql );
//                pstmt.setString( 1, webProdInfo.getProductBlurb() );
                pstmt.setString( 1, webProdInfo.getProductFeature() );
                pstmt.setString( 2, webProdInfo.getProductNum() );
                pstmt.executeUpdate();
                System.out.print("updated  "+sql+" 1: '"+ webProdInfo.getProductFeature()+"' 2: "+webProdInfo.getProductNum());
            }
            else{
                //insert
                sql = "insert into WebProductInfo (productBlurb, productFeature, productNum)" +
                "values (?,?,?)";
                pstmt.close();
                pstmt = con.prepareStatement( sql );
                pstmt.setString( 1, webProdInfo.getProductBlurb() );
                pstmt.setString( 2, webProdInfo.getProductFeature() );
                pstmt.setString( 3, webProdInfo.getProductNum() );
                pstmt.executeUpdate();
                System.out.print("insert  "+sql+" 1: "+ webProdInfo.getProductBlurb()+" 2: "+ webProdInfo.getProductFeature()+" 3: "+webProdInfo.getProductNum());
            }

            pstmt.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return result;
    }
	

	public class WebProductInfo {
	    String productBlurb;
	    String productFeature;
	    String productNum;
	    
	    public String getProductBlurb() {
	        return productBlurb;
	    }

	    public void setProductBlurb( String productBlurb ) {
	        this.productBlurb = productBlurb;
	    }

	    public String getProductFeature() {
	        return productFeature;
	    }

	    public void setProductFeature( String productFeature ) {
	        this.productFeature = productFeature;
	    }

	    public String getProductNum() {
	        return productNum;
	    }

	    public void setProductNum( String productNum ) {
	        this.productNum = productNum;
	    }

	    public WebProductInfo(){
	        this.productBlurb = "";
	        this.productFeature = "";
	        this.productNum ="";
	    }

	}
	
	private class MySQL {
	    public MySQL() {
	        try {
	        	new SybDriver();
	        	new com.sybase.jdbc3.jdbc.SybDriver();
	            Class.forName( "com.sybase.jdbc3.jdbc.SybDriver" );
	        } catch ( ClassNotFoundException cnfe ) {
	            System.out.println( "TestConnect cnfe: " + cnfe );
	        }
	    }

	    public java.sql.Connection getConn() {

	        Properties SysProps = new Properties();

	        SysProps.put( "user", "sforzando" );
	        SysProps.put( "password", "johnNarlette" );
	        java.sql.Connection con = null;
	        String port = "7110";
	        //local dev
	        boolean dev = false;
	        
	        
	        
	        dev = true;
	        
	        
	        
	        
	        String url = "jdbc:sybase:Tds:192.168.100.1:7110/Sforzando";
	        if(dev)
	            url = "jdbc:sybase:Tds:70.58.38.249:7110/Sforzando";
	        try {
	            con = DriverManager.getConnection( url, SysProps );
	            //          con.setAutoCommit( false ) ;
	        } catch ( SQLException e1 ) {
	            System.out.println( "DMM.MySQL-->getConn:");
	            e1.printStackTrace();
	        }
	        return con;
	    }

	    /**
	     * @param args
	     */
	    //roll back changes in inventoryReport and here.
	}
	
}
