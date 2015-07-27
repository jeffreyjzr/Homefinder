package com.evaluation.web.action;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.common.data.SimpleConnetionPool;
import com.common.util.ParameterUtil;
import com.opensymphony.xwork2.ActionSupport;

public class EvaluationAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// city (user input)
	private String city;

	// county (calculate based on city)
	private String county = null;

	// Latitude (user input)
	private String latitude;

	// Longitude (user input)
	private String longitude;

	// total_population (based on user input)
	private double total_population;

	// disabled_population (based on user input)
	private double disabled_population;
	
	// total_address (user input)
	private String total_address;

	private String metroquery = "select percentage from metro where city = ?";

	private String crimequery = "select score from crime where city = ?";

	private String countyquery = "select county from ctu where type='City' and place not like '%historical%' and place = ?";

	private String hosptialquery = "select lat, lng, overall_rating from hospital where county = ?";

	private String housequery = "select lat, lng, hud_score from house where county = ?";

	// database connection
	private Connection conn = null;

	// 1. Housing Score
	double housing;

	/**
	 * message
	 */
	String housingMessage;

	// 2. Mobility Score
	double mobility;

	/**
	 * message
	 */
	String mobilityMessage;

	// 3. get Safety Score
	double safety;

	/**
	 * message
	 */
	String safetyMessage;

	// 4. get Health and Special Services Score
	double health;

	/**
	 * message
	 */
	String healthMessage;

	// 5. get Disability Community Score
	double community;

	/**
	 * message
	 */
	String communityMessage;

	// total
	double total;

	public String execute() throws SQLException {
		
		// get data
		// initialize database
		initialDB();

		// find county
		county = findCountyByCity(city);

		// 1. get Housing Score
		double housingScore = getHousingScore();
		
		BigDecimal b = new BigDecimal(housingScore);
		housing = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

		//System.out.println("housingScore:" + housing);

		// 2. get Mobility Score
		double mobilityScore = getMobilityScore();
		b = new BigDecimal(mobilityScore);
		mobility = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

		//System.out.println("mobilityScore:" + mobility);

		// 3. get Safety Score
		double safetyScore = getSafetyScore();
		b = new BigDecimal(safetyScore);
		safety = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

		//System.out.println("safetyScore:" + safety);
		
		// 4. get Health and Special Services Score
		double healthScore = getHealthScore();
		b = new BigDecimal(healthScore);
		health = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

		//System.out.println("healthScore:" + health);

		// 5. get Disability Community Score
		double communityScore = getCommunityScore();
		b = new BigDecimal(communityScore);
		community = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

		//System.out.println("communityScore:" + community);
		
		
		//total
		double totalScore = 0.2*housing+0.2*mobility+0.2*safety+0.2*health+0.2*community;
		b = new BigDecimal(totalScore);
		total = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

		
		// message
	   if (housing >= 80) {
		    housingMessage = "housingMessage 80<=housing <=100";
		} else if (housing >= 60) {
			housingMessage = "housingMessage 60<=housing <80";
		} else if (housing >= 40) {
			housingMessage = "housingMessage 40=<=housing <60";
		} else if (housing >= 20) {
			housingMessage = "housingMessage 20<=housing <40";
		} else {
			housingMessage = "housingMessage 0<=housing <20";
		}
		
		
		
		// message
		if (mobility >= 80) {
			mobilityMessage = "mobilityMessage 80<=mobility <=100";
		} else if (mobility >= 60) {
			mobilityMessage = "mobilityMessage 60<=mobility <80";
		} else if (mobility >= 40) {
			mobilityMessage = "mobilityMessage 40=<=mobility <60";
		} else if (mobility >= 20) {
			mobilityMessage = "mobilityMessage 20<=mobility <40";
		} else {
			mobilityMessage = "mobilityMessage 0<=mobility <20";
		}

		// message
		if (safety >= 80) {
			safetyMessage = "safetyMessage 80<=safety <=100";
		} else if (safety >= 60) {
			safetyMessage = "safetyMessage 60<=safety <80";
		} else if (safety >= 40) {
			safetyMessage = "safetyMessage 40=<=safety <60";
		} else if (safety >= 20) {
			safetyMessage = "safetyMessage 20<=safety <40";
		} else {
			safetyMessage = "safetyMessage 0<=safety <20";
		}

		// message
		if (health >= 80) {
			healthMessage = "healthMessage 80<=health <=100";
		} else if (health >= 60) {
			healthMessage = "healthMessage 60<=health <80";
		} else if (health >= 40) {
			healthMessage = "healthMessage 40=<=health <60";
		} else if (health >= 20) {
			healthMessage = "healthMessage 20<=health <40";
		} else {
			healthMessage = "healthMessage 0<=health <20";
		}

		// message
		if (community >= 80) {
			communityMessage = "communityMessage 80<=community <=100";
		} else if (community >= 60) {
			communityMessage = "communityMessage 60<=community <80";
		} else if (community >= 40) {
			communityMessage = "communityMessage 40=<=community <60";
		} else if (community >= 20) {
			communityMessage = "communityMessage 20<=community <40";
		} else {
			communityMessage = "communityMessage 0<=community <20";
		}
		
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

	/**
	 * get MetroScore from database
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double getMetroScore() throws SQLException {
		double metroScore = 0.0;
		getConnection();
		PreparedStatement ps = conn.prepareStatement(metroquery);
		ps.setString(1, city);
		ps.execute();
		ResultSet rs = ps.getResultSet();
		while (rs.next()) {
			String s = rs.getString(1);
			metroScore = Double.parseDouble(s.substring(0, s.indexOf("%")));
			break;
		}
		rs.close();
		ps.close();
		return metroScore;
	}

	/**
	 * get WalkabilityScore
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double getWalkabilityScore() throws SQLException {
		double walkabilityScore = 0.0;

		return walkabilityScore;
	}

	/**
	 * get MobilityScore
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double getMobilityScore() throws SQLException {
		double mobilityScore = 0.0;
		// get Walkability Score
		double walkabilityScore = getWalkabilityScore();
		// get Metro Score
		double metroScore = getMetroScore();

		mobilityScore = (0.5 * walkabilityScore + 0.5 * metroScore);
		return mobilityScore;
	}

	/**
	 * get SafetyScore from database
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double getSafetyScore() throws SQLException {
		double safetyScore = 0.0;
		getConnection();
		PreparedStatement ps = conn.prepareStatement(crimequery);
		ps.setString(1, city);
		ps.execute();
		ResultSet rs = ps.getResultSet();
		int i = 0;
		while (rs.next()) {
			safetyScore = rs.getInt(1);
			i++;
			break;
		}
		// there is no record be found for city
		// then we use county score
		if (i == 0) {
			ps = conn.prepareStatement(crimequery);
			ps.setString(1, county + " County Total");
			ps.execute();
			rs = ps.getResultSet();
			while (rs.next()) {
				safetyScore = rs.getInt(1);
				i++;
				break;
			}
		}
		rs.close();
		ps.close();
		return safetyScore;
	}

	/**
	 * find county by city
	 * 
	 * @param city
	 * @return
	 * @throws SQLException
	 */
	private String findCountyByCity(String city) throws SQLException {
		getConnection();
		PreparedStatement ps = conn.prepareStatement(countyquery);
		ps.setString(1, city);
		ps.execute();
		ResultSet rs = ps.getResultSet();
		String county = "";
		while (rs.next()) {
			county = rs.getString(1);
			break;
		}
		rs.close();
		ps.close();
		return county;
	}

	/**
	 * get CommunityScore
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double getCommunityScore() {
		double communityScore = 0.0;
		if (disabled_population != 0 && total_population != 0) {
			communityScore = (disabled_population / total_population) * 100;
		}
		return communityScore;
	}

	/**
	 * get HealthScore from database
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double getHealthScore() throws SQLException {
		double healthScore = 0.0;
		getConnection();
		PreparedStatement ps = conn.prepareStatement(hosptialquery);
		ps.setString(1, county);
		ps.execute();
		ResultSet rs = ps.getResultSet();
		while (rs.next()) {
			double lat = Double.parseDouble(rs.getString(1));
			double lng = Double.parseDouble(rs.getString(2));
			double dis = getDistance(Double.parseDouble(latitude),
					Double.parseDouble(longitude), lat, lng) * 0.0006214;
			// in 5 mile
			if (dis <= 5) {
				double score = rs.getInt(3);
				if (healthScore < score) {
					healthScore = score;
				}
			}

		}
		rs.close();
		ps.close();
		return healthScore;
	}

	/**
	 * get HousingScore from database
	 * 
	 * @return
	 * @throws SQLException
	 */
	private double getHousingScore() throws SQLException {
		double houseScore = 0.0;
		getConnection();
		PreparedStatement ps = conn.prepareStatement(housequery);
		ps.setString(1, county);
		ps.execute();
		ResultSet rs = ps.getResultSet();
		while (rs.next()) {
			double lat = Double.parseDouble(rs.getString(1));
			double lng = Double.parseDouble(rs.getString(2));
			double dis = getDistance(Double.parseDouble(latitude),
					Double.parseDouble(longitude), lat, lng) * 0.0006214;
			// in 5 mile
			if (dis <= 5) {
				double score = rs.getInt(3);
				if (houseScore < score) {
					houseScore = score;
				}
			}

		}
		rs.close();
		ps.close();
		return houseScore;
	}

	/**
	 * get rad
	 * 
	 * @param x
	 * @return
	 */
	private double getRad(double x) {
		return x * Math.PI / 180;
	}

	/**
	 * get distance Haversine formula
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	private double getDistance(double lat1, double lng1, double lat2,
			double lng2) {
		double R = 6378137; // Earth's mean radius in meter
		double dLat = getRad(lat2 - lat1);
		double dLong = getRad(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(getRad(lat1)) * Math.cos(getRad(lat2))
				* Math.sin(dLong / 2) * Math.sin(dLong / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		return d; // returns the distance in meter
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public double getTotal_population() {
		return total_population;
	}

	public void setTotal_population(double total_population) {
		this.total_population = total_population;
	}

	public double getDisabled_population() {
		return disabled_population;
	}

	public void setDisabled_population(double disabled_population) {
		this.disabled_population = disabled_population;
	}

	public double getHousing() {
		return housing;
	}

	public void setHousing(double housing) {
		this.housing = housing;
	}

	public String getHousingMessage() {
		return housingMessage;
	}

	public void setHousingMessage(String housingMessage) {
		this.housingMessage = housingMessage;
	}

	public double getMobility() {
		return mobility;
	}

	public void setMobility(double mobility) {
		this.mobility = mobility;
	}

	public String getMobilityMessage() {
		return mobilityMessage;
	}

	public void setMobilityMessage(String mobilityMessage) {
		this.mobilityMessage = mobilityMessage;
	}

	public double getSafety() {
		return safety;
	}

	public void setSafety(double safety) {
		this.safety = safety;
	}

	public String getSafetyMessage() {
		return safetyMessage;
	}

	public void setSafetyMessage(String safetyMessage) {
		this.safetyMessage = safetyMessage;
	}

	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public String getHealthMessage() {
		return healthMessage;
	}

	public void setHealthMessage(String healthMessage) {
		this.healthMessage = healthMessage;
	}

	public double getCommunity() {
		return community;
	}

	public void setCommunity(double community) {
		this.community = community;
	}

	public String getCommunityMessage() {
		return communityMessage;
	}

	public void setCommunityMessage(String communityMessage) {
		this.communityMessage = communityMessage;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getTotal_address() {
		return total_address;
	}

	public void setTotal_address(String total_address) {
		this.total_address = total_address;
	}
	
	

}
