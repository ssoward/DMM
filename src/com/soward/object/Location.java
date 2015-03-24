/**
 * Title: Location.java
 * Description: DayMurrayMusic
 * Date: Jul 13, 2007Jun 28, 2007
 * Copyright: Copyright (c) 2007, Soward Inc.
 * @author Scott Soward
 * */
package com.soward.object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.lang.StringUtils;

import com.soward.db.MySQL;

public class Location {
	public String locationNum;
	public String locationIP;
	public String locationName;
	public String locationDescription;

	/**
	 * @param locationNum
	 * @param locationIP
	 * @param locationName
	 * @param locationDescription
	 */

    @Deprecated
	public Location() {
		this.locationNum = "";
		this.locationIP = "";
		this.locationName = "";
		this.locationDescription = "";
	}

	/**
	 * @return the locationDescription
	 */
	public String getLocationDescription() {
		return locationDescription;
	}

	/**
	 * @param locationDescription the locationDescription to set
	 */
	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	/**
	 * @return the locationIP
	 */
	public String getLocationIP() {
		return locationIP;
	}

	/**
	 * @param locationIP the locationIP to set
	 */
	public void setLocationIP(String locationIP) {
		this.locationIP = locationIP;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/**
	 * @return the locationNum
	 */
	public String getLocationNum() {
		return locationNum;
	}

	/**
	 * @param locationNum the locationNum to set
	 */
	public void setLocationNum(String locationNum) {
		this.locationNum = locationNum;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
