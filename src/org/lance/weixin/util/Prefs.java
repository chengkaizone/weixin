package org.lance.weixin.util;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * ����򵥲���
 * @author lance
 *
 */
public class Prefs {
	
	private static final String PREFER_NAME="org.lance.weixin.main";
	
	private static final String ACCESS_TOKEN="access_token";
	
	private static Properties env = new Properties();
	static {
		try {
			// ���·�����������
			InputStream is = Prefs.class
					.getResourceAsStream("/weixin.properties");
			env.load(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** ��ȡ����openid */
	public static String getAppID(){
		return env.getProperty("appid2");
	}
	/** ��ȡ��ȫ�� */
	public static String getAppSecret(){
		return env.getProperty("appsecret2");
	}
	
	public static String getWsdl(){
		return env.getProperty("wsdl");
	}
	public static String getNamespace(){
		return env.getProperty("namespace");
	}
	public static String getServerUrl(){
		return env.getProperty("server_url");
	}
	public static String getServerToken(){
		return env.getProperty("server_token");
	}
	
	
	public static String getExpressKey(){
		return env.getProperty("express_key");
	}
	/**
	 * ����ȫ��access_token 
	 * //access_token�ǹ��ںŵ�ȫ��ΨһƱ�ݣ����ںŵ��ø��ӿ�ʱ����ʹ��access_token��
	 * //���������access_token��Ч��Ϊ7200�룬�ظ���ȡ�������ϴλ�ȡ��access_tokenʧЧ�� 
	 * //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
	 * @param context
	 * @param orderCode
	 * @return
	 */
	public static boolean putAccessToken(Context context, String accessToken) {
		try {
			SharedPreferences prefer = context.getSharedPreferences(
					PREFER_NAME, Context.MODE_PRIVATE);
			Editor editor = prefer.edit();
			editor.putString(ACCESS_TOKEN, accessToken);
			editor.commit();
			prefer = null;
			editor = null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * ����0��ʾû�д�Ÿò���
	 * 
	 * @param context
	 * @return
	 */
	public static String getAccessToken(Context context) {
		try {
			SharedPreferences prefer = context.getSharedPreferences(
					PREFER_NAME, Context.MODE_PRIVATE);
			String result = prefer.getString(ACCESS_TOKEN, "");
			prefer = null;
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
}
