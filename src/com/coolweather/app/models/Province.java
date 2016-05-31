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
	
	public int getId(int id) {
		return id;
	}
	
	public String getProvinceName() {
		return provinceName;
	}
	
	public void setProviceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	public String getProvinceCode() {
		return provinceCode;
	}
	
	public void setProviceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
}
