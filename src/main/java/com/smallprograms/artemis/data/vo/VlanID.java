package com.smallprograms.artemis.data.vo;

public class VlanID {
	
	private String operator; // 运营商
	
	private String lat; // 纬度
	
	private String lon; // 经度
	
	private String vlanid;
	
	private int state;//// 0是关闭，1是开启

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getVlanid() {
		return vlanid;
	}

	public void setVlanid(String vlanid) {
		this.vlanid = vlanid;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
}
