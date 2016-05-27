package com.coolweather.app.models;

public class Province {
	
	private int id;
	private String provinceName;
	private String provinceCode;
	public int getId() {
		 return id;
	 }
	public void setId(int id) {
		this.id = id;
	}
	
	public String getProvinceName() {
		return provinceName;
	}
	
	public void setProviceName(String proviceName) {
		this.provinceName = provinceName;
	}
	
	public String getProvinceCode() {
		return provinceCode;
	}
	
	public void setProviceCode(String proviceCode) {
		this.provinceCode = provinceCode;
	}
}
