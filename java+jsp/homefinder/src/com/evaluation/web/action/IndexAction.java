package com.evaluation.web.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.data.SimpleConnetionPool;
import com.common.util.ParameterUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * index action is for getting all the cities in MN
 * 
 * @author Jeff
 * 
 */
public class IndexAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Map<String,String>> cities = new ArrayList<Map<String,String>>();

	private String pstatquery = "select distinct place from ctu where type= ? and place not like '%historical%' order by place";

	// database connection
	private Connection conn = null;

	// sql PreparedStatement
	private PreparedStatement ps;

	public String execute() throws SQLException {
		// get data (initialize)
		initialDB();

		// get city data
		getCityData();

		// close database
		closeDB();

		return "SUCCESS";
	}

	/**
	 * initialize the database
	 */
	private void initialDB() {
		SimpleConnetionPool.setDriver(ParameterUtil.readParameter("DB",
				"DB_DRIVER"));
		SimpleConnetionPool.setUrl(ParameterUtil.readParameter("DB",
				"DB_CONNECTION"));
		SimpleConnetionPool.setUser(ParameterUtil
				.readParameter("DB", "DB_USER"));
		SimpleConnetionPool.setPassword(ParameterUtil.readParameter("DB",
				"DB_PASSWORD"));
		SimpleConnetionPool.initDriver();
		conn = SimpleConnetionPool.getConnection();

	}

	/**
	 * get a new connection
	 * 
	 * @throws SQLException
	 */
	private void getConnection() throws SQLException {
		if (conn != null) {
			if (!conn.isClosed()) {
				// do nothing
			} else {
				conn = SimpleConnetionPool.getConnection();
			}
		} else {
			conn = SimpleConnetionPool.getConnection();
		}
	}

	/**
	 * close database
	 */
	private void closeDB() {
		SimpleConnetionPool.close();
	}

	private void getCityData() throws SQLException {
		getConnection();
		ps = conn.prepareStatement(pstatquery);
		ps.setString(1, "City");
		ps.execute();
		ResultSet rs = ps.getResultSet();
		while (rs.next()) {
			String place = rs.getString(1);
			Map<String,String> m = new HashMap<String,String>();
			m.put("place", place);
			cities.add(m);
		}

	}

	public List<Map<String, String>> getCities() {
		return cities;
	}

	public void setCities(List<Map<String, String>> cities) {
		this.cities = cities;
	}
}
