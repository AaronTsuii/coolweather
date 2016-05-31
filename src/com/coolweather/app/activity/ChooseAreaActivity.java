package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.models.City;
import com.coolweather.app.models.CoolWeatherDB;
import com.coolweather.app.models.County;
import com.coolweather.app.models.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity{

	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView ;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>(); 
	//ʡ�б�
	private List<Province> provinceList;
	//���б�
	private List<City> cityList;
	//���б�
	private List<County> countyList;
	//ѡ�е�ʡ��
	private Province selectedProvince;
	//ѡ�е���
	private City selectedCity;
	//ѡ�е���
	private County selectedCounty;
	private int currentLevel;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
				
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int index, long arg3) {
				Log.d("text", "a");
				// TODO Auto-generated method stub
				if(currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(index);
					//Log.d("1", "a"+ selectedProvince.getId());
					queryCities();
				}else if(currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(index);
					queryCounties();
				}
			}

		});
		queryProvinces();
	}
	//��ѯ���е�ʡ�����ȴ����ݿ��ѯ�����û����ȥ�������ϲ�ѯ
	private void queryProvinces() {
		// TODO Auto-generated method stub
		provinceList = coolWeatherDB.loadProvinces();
		//Log.d("1","5");
		if(provinceList.size() > 0) {
			//Log.d("1","1");
			dataList.clear();
			for(Province province: provinceList) {
				//Log.d("1",province.getProvinceName());
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		}else {
			queryFromServer(null,"province");
			//Log.d("1","6");
		}
	}
	//��ѯѡ�з�ʡ�����е��У����ȴ����ݿ��ѯ�����û����ȥ�������ϲ�ѯ
	protected void queryCities() {
		// TODO Auto-generated method stub
		//Log.d("2", "a"+ selectedProvince.getId());
		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
		if(cityList.size() > 0) {
			dataList.clear();
			for(City city: cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		}else {
			queryFromServer(selectedProvince.getProvinceCode(),"city");
		}
	}
	//��ѯѡ���������е��أ����ȴ����ݿ��ѯ�����û����ȥ�������ϲ�ѯ
	protected void queryCounties() {
		// TODO Auto-generated method stub
		countyList = coolWeatherDB.loadCounties(selectedCity.getId());
		if(countyList.size() > 0) {
			dataList.clear();
			for(County county: countyList) {
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		}else {
			queryFromServer(selectedCity.getCityCode(),"county");
		}
	}
	//	���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯ����
	private void queryFromServer(final String code ,final  String type) {
		// TODO Auto-generated method stub
		String address;
		if(!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code +".xml";
		} else {
			Log.d("1","8");
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;
				if("province".equals(type)) {
					result = Utility.handleProvinceResponse(coolWeatherDB, response);
					Log.d("1","10");
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getId());
				} else if ("county".equals(type)) {
					result = Utility.handleCountiesResponse(coolWeatherDB, response, selectedCity.getId());
				}
				if(result){
					runOnUiThread (new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread (new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}); 
	}
	private void showProgressDialog() {
		// TODO Auto-generated method stub
		if(progressDialog== null ){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ��ء�����");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	private void closeProgressDialog() {
		// TODO Auto-generated method stub
		if(progressDialog != null) 
			progressDialog.dismiss();
	}
	public void onBackPressed() {
		if(currentLevel == LEVEL_COUNTY ) {
			queryCities();
		} else if(currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else finish();
	}

}