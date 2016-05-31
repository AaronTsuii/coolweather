package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtil {
	
	public  static void sendHttpRequest (final String address, final HttpCallbackListener listener) {
		new Thread (new Runnable () {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try{
					Log.d("2","1");
					URL url = new URL(address);//����Ŀ��������ַ
					connection = (HttpURLConnection) url.openConnection();
					Log.d("2","2");
					connection.setRequestMethod("GET");//get��ʾ�ӷ��������������ݣ�����post���ʾ�ṩ���ݸ�������
					connection.setConnectTimeout(8000);
					Log.d("2","3");
					connection.setReadTimeout(8000);
					Log.d("2","44");
					InputStream in = connection.getInputStream();
					Log.d("2","4");
					//�Ի�ȡ�������������ж�ȡ
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					Log.d("2","5");
					while((line = reader.readLine()) != null) {
						response.append(line);
					}
					Log.d("�ӷ�������õ�����",response.toString());
					if(listener != null) {
						Log.d("2","6");
						listener.onFinish(response.toString());
					}
				}catch(Exception e) {
					if (listener != null) {
						listener.onError(e);
					}
				}finally {
					if(connection != null) {
						connection.disconnect();
					}
				}
			}
			
		}).start();
	}

}
