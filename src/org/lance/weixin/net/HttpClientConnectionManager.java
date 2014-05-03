package org.lance.weixin.net;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import java.security.cert.CertificateException; 
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager; 

//public class HttpClientConnectionManager {
//
//	/**
//	 * 获取SSL验证的HttpClient
//	 * 
//	 * @param httpClient
//	 * @return
//	 */
//	public static HttpClient getSSLInstance(HttpClient httpClient) {
//		ClientConnectionManager ccm = httpClient.getConnectionManager();
//		SchemeRegistry sr = ccm.getSchemeRegistry();
//		sr.register(new Scheme("https", MySSLSocketFactory.getInstance(), 443));
//		httpClient = new DefaultHttpClient(ccm, httpClient.getParams());
//		return httpClient;
//	}
//
//	/**
//	 * 模拟浏览器post提交
//	 * 
//	 * @param url
//	 * @return
//	 */
//	public static HttpPost getPostMethod(String url) {
//		HttpPost pmethod = new HttpPost(url); // 设置响应头信息
//		pmethod.addHeader("Connection", "keep-alive");
//		pmethod.addHeader("Accept", "*/*");
//		pmethod.addHeader("Content-Type",
//				"application/x-www-form-urlencoded; charset=UTF-8");
//		pmethod.addHeader("Host", "mp.weixin.qq.com");
//		pmethod.addHeader("X-Requested-With", "XMLHttpRequest");
//		pmethod.addHeader("Cache-Control", "max-age=0");
//		pmethod.addHeader("User-Agent",
//				"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
//		return pmethod;
//	}
//
//	/**
//	 * 模拟浏览器GET提交
//	 * 
//	 * @param url
//	 * @return
//	 */
//	public static HttpGet getGetMethod(String url) {
//		HttpGet pmethod = new HttpGet(url);
//		// 设置响应头信息
//		pmethod.addHeader("Connection", "keep-alive");
//		pmethod.addHeader("Cache-Control", "max-age=0");
//		pmethod.addHeader("User-Agent",
//				"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
//		pmethod.addHeader("Accept",
//				"text/html,application/xhtml+xml,application/xml;q=0.9,*/;q=0.8");
//		return pmethod;
//	}
//}
//
//class MySSLSocketFactory extends SSLSocketFactory {
//
//	static {
//		mySSLSocketFactory = new MySSLSocketFactory(createSContext());
//	}
//
//	private static MySSLSocketFactory mySSLSocketFactory = null;
//
//	private static SSLContext createSContext() {
//		SSLContext sslcontext = null;
//		try {
//			sslcontext = SSLContext.getInstance("SSL");
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//		try {
//			sslcontext.init(null,
//					new TrustManager[] { new TrustAnyTrustManager() }, null);
//		} catch (KeyManagementException e) {
//			e.printStackTrace();
//			return null;
//		}
//		return sslcontext;
//	}
//
//	private MySSLSocketFactory(SSLContext sslContext) {
//		super(sslContext);
//		this.setHostnameVerifier(ALLOW_ALL_HOSTNAME_VERIFIER);
//	}
//
//	public static MySSLSocketFactory getInstance() {
//		if (mySSLSocketFactory != null) {
//			return mySSLSocketFactory;
//		} else {
//			return mySSLSocketFactory = new MySSLSocketFactory(createSContext());
//		}
//	}
//
//}
//class TrustAnyTrustManager implements X509TrustManager{ 
//	       
//	        @Override 
//	        public void checkClientTrusted(X509Certificate[] chain, String authType) 
//	                throws CertificateException { 
//	               
//	        } 
//	       
//	        @Override 
//	        public void checkServerTrusted(X509Certificate[] chain, String authType) 
//	                throws CertificateException { 
//	               
//	        } 
//	       
//	        @Override 
//	        public X509Certificate[] getAcceptedIssuers() { 
//	            return null; 
//	        } 
//	       
//	    }