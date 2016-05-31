package com.coolweather.app.util;

import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.models.City;
import com.coolweather.app.models.CoolWeatherDB;
import com.coolweather.app.models.County;
import com.coolweather.app.models.Province;

public class Utility {
	
	//�����ʹ�����������ص�ʡ������
	public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB,String response) {
		if(!TextUtils.isEmpty(response)) {
			Log.d("��ʼ����",response);
			String [] allProvinces = response.split(",");
			Log.d("��ʼ����",""+ allProvinces.length);
			if(allProvinces != null && allProvinces.length > 0) {
				for(String p : allProvinces) {
					String [] array = p.split("\\|");
//					Log.d("������������",""+ array.length);
//					Log.d("�����������б���",""+ array[0]);
//					Log.d("����������������",""+ array[1]);
					Province province = new Province();
					province.setProviceCode(array[0]);
					province.setProviceName(array[1]);
					//Log.d("�����󵥸���������",""+ province.getProvinceName());
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	 //�����ʹ�����������ص��м�����
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
	 //�����ʹ�����������ص��ؼ�����
	public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId) {
		if(!TextUtils.isEmpty(response)) {
			String [] allCounties = response.split(",");
			if(allCounties != null && allCounties.length > 0) {
				for(String c : allCounties) {
					String [] array = c.split("\\|");
					Log.d("����������",""+ array[1]);
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
