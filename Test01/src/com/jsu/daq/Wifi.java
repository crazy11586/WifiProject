package com.jsu.daq;

import java.util.ArrayList;

public class Wifi {
	
	//探针id
	private String id;
	//探查到的所有数据的集合
	private ArrayList<WifiData> data;
	//手机数据的时间
	private String time;
	//纬度
	private String lat;
	//精度
	private String lon;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<WifiData> getData() {
		return data;
	}
	public void setData(ArrayList<WifiData> data) {
		this.data = data;
	}
	
	
	@Override
	public String toString() {
		
		String str = "Wifi [id=" + id + ", data=" + data + ", time=" + time + ", lat=" + lat + ", lon=" + lon + "]";

		return str;
	}
	
	
	
	
}
