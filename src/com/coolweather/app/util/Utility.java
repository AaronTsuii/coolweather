package com.coolweather.app.util;

import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.models.City;
import com.coolweather.app.models.CoolWeatherDB;
import com.coolweather.app.models.County;
import com.coolweather.app.models.Province;

public class Utility {
	
	//解析和处理服务器返回的省级数据
	public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB,String response) {
		if(!TextUtils.isEmpty(response)) {
			Log.d("开始解析",response);
			String [] allProvinces = response.split(",");
			Log.d("开始解析",""+ allProvinces.length);
			if(allProvinces != null && allProvinces.length > 0) {
				for(String p : allProvinces) {
					String [] array = p.split("\\|");
//					Log.d("解析单个城市",""+ array.length);
//					Log.d("解析单个城市编码",""+ array[0]);
//					Log.d("解析单个城市名称",""+ array[1]);
					Province province = new Province();
					province.setProviceCode(array[0]);
					province.setProviceName(array[1]);
					//Log.d("解析后单个城市名称",""+ province.getProvinceName());
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	 //解析和处理服务器返回的市级数据
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId) {
		if(!TextUtils.isEmpty(response)) {
			String [] allCities = response.split(",");
			if(allCities != null && allCities.length > 0) {
				for(String c : allCities) {
					String [] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	 //解析和处理服务器返回的县级数据
	public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId) {
		if(!TextUtils.isEmpty(response)) {
			String [] allCounties = response.split(",");
			if(allCounties != null && allCounties.length > 0) {
				for(String c : allCounties) {
					String [] array = c.split("\\|");
					Log.d("解析单个县",""+ array[1]);
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
}
