package org.lance.weixin.util;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 保存简单参数
 * @author lance
 *
 */
public class Prefs {
	
	private static final String PREFER_NAME="org.lance.weixin.main";
	
	private static final String ACCESS_TOKEN="access_token";
	
	private static Properties env = new Properties();
	static {
		try {
			// 相对路径获得输入流
			InputStream is = Prefs.class
					.getResourceAsStream("/weixin.properties");
			env.load(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** 获取测试openid */
	public static String getAppID(){
		return env.getProperty("appid2");
	}
	/** 获取安全码 */
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
	 * 保存全局access_token 
	 * //access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token。
	 * //正常情况下access_token有效期为7200秒，重复获取将导致上次获取的access_token失效。 
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
	 * 返回0表示没有存放该参数
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
