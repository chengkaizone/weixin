package org.lance.weixin.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.lance.weixin.util.Prefs;

import android.content.Context;

/**
 * 调用.NET平台的webservice 只需要wsdl地址； 从而得知命名空间 soapAction一定是命名空间+"/"+方法名；一定需要wsdl地址
 * 
 * @author lance
 * 
 */
public class NetService2 {
	private final static String TAG = "NetService";
	static String wsdl = Prefs.getWsdl();
	static String namespace = Prefs.getNamespace();

	
	//==========================================微信接口方法====================================
	//未认证的账号只有三个接口权限
	/////////////////////////////生成带参数的二维码///////////////////////////////
	
	/**
	 * 通过ticket获取二维码图片 GET
	 * @param ticket  ok 返回码是200 ticket非法返回404
	 * @return 提醒：TICKET记得进行UrlEncode
	 */
	public static String getQRCode(String ticket){
		String url="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
		return getRemoteInfo(url, false);
	}
	/**
	 * 创建二维码 POST
	 * @param accessToken
	 * @param expire_seconds 该二维码有效时间，以秒为单位。 最大不超过1800。 
	 * @param action_name  	二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久 
	 * @param action_info  	二维码详细信息 
	 * @param scene_id  场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000） 
	 * @return
	 */
	public static String createTempQRCode(String accessToken,int expire_seconds,String action_name,
			int scene_id){
		String json="{'expire_seconds': "+expire_seconds+", 'action_name': '"+action_name
				+"', 'action_info': {'scene': {'scene_id': "+scene_id+"}}}";
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken;
		return doPost(url,json,"UTF-8");
	}
	/** 创建永久二维码 */
	public static String createForeverQRCode(String accessToken,String action_name,
			int scene_id){
		String json="{\"action_name\": \""+action_name+"\", \"action_info\": {\"scene\": {\"scene_id\": "+scene_id+"}}}";
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken;
		return doPost(url,json,"UTF-8");
	}
	/////////////////////////////自定义菜单//////////////////////
	//自定义菜单事件推送
	/**
	 * 删除自定义菜单 GET
	 * @param accessToken
	 * @return
	 */
	public static String deleteMenu(String accessToken){
		String url="https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+accessToken;
		return getRemoteInfo(url, false);
	}
	/**
	 * 自定义菜单查询接口 GET
	 * @param accessToken
	 * @return
	 */
	public static String getMenuList(String accessToken){
		String url="https://api.weixin.qq.com/cgi-bin/menu/get?access_token="+accessToken;
		return getRemoteInfo(url, false);
	}
	/**
	 * 创建自定义菜单列表 POST
	 * @param accessToken
	 * @param button 一级菜单数组，个数应为1~3个 
	 * @param sub_button 二级菜单数组，个数应为1~5个 
	 * @param type 菜单的响应动作类型，目前有click、view两种类型 
	 * @param name 菜单标题，不超过16个字节，子菜单不超过40个字节 
	 * @param key  click类型必须 	菜单KEY值，用于消息接口推送，不超过128字节 
	 * @param url 网页链接，用户点击菜单可打开链接，不超过256字节 
	 * @return
	 */
	public static String createMenuList(String accessToken,List<Map<String,String>> menus){
		try {
			JSONObject obj=new JSONObject();
			JSONArray jsonArr=new JSONArray();
			for(int i=0;i<menus.size();i++){
				Map<String,String> menu=menus.get(i);
				Set<String> keys = menu.keySet();
				JSONObject subObj=new JSONObject();
				for (String key : keys) {
					subObj.put(key, menu.get(key));
				}
				jsonArr.put(subObj);
			}
			obj.put("button", jsonArr);
			String dourl="https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;
			System.out.println(obj.toString());
			return doPost(dourl, obj.toString(),"UTF-8");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	/** 
	 * 获取用户地理位置
	 * 获取用户地理位置的方式有两种，一种是仅在进入会话时上报一次，
	 * 一种是进入会话后每隔5秒上报一次。公众号可以在公众平台网站中设置。
	 * 用户同意上报地理位置后，每次进入公众号会话时，都会在进入时上报地理位置，或在进入会话后每5秒上报一次地理位置，
	 * 上报地理位置以推送XML数据包到开发者填写的URL来实现。 
	 * @return
	 */
	public static String getLocation(){
		return null;
	}
	
	/**
	 * 获取关注者列表  GET
	 * @param accessToken
	 * @param next_openid 第一个拉取的OPENID，不填默认从头开始拉取
	 * @return
	 */
	public static String getAttentionList(String accessToken,String next_openid){
		String url="https://api.weixin.qq.com/cgi-bin/user/get?access_token="+
				accessToken+"&next_openid="+next_openid;
		return getRemoteInfo(url, true);
	}
	/**
	 * 获取用户基本信息 GET请求
	 * @param accessToken
	 * @param openid
	 * @return
	 */
	public static String getUserInfo(String accessToken,String openid){
		String url="https://api.weixin.qq.com/cgi-bin/user/info?access_token="
				+accessToken+"&openid="+openid+"&lang=zh_CN";
		return getRemoteInfo(url, false);
	}
	
	///////////////////////分组管理 start//////////////////////
	/**
	 * 移动用户分组 POST
	 * @param accessToken
	 * @param openid
	 * @param to_groupid
	 * @return
	 */
	public static String removeToGroup(String accessToken,String openid,int to_groupid ){
		String url="https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token="+accessToken;
		HashMap<String,String> params=new HashMap<String, String>();
		params.put("openid", openid);
		params.put("to_groupid", to_groupid+"");
		return doPost(url,params,"UTF-8");
	}
	/**
	 * 修改分组名 POST
	 * @param accessToken
	 * @param id 由微信分配
	 * @param newGroupName
	 * @return {"errcode": 0, "errmsg": "ok"}, {"errcode":40013,"errmsg":"invalid appid"}
	 */
	public static String renameGroup(String accessToken,int id,String newGroupName){
		String json="{\"group\":{\"id\":"+id+",\"name\":\""+newGroupName+"\"}}";
		String url="https://api.weixin.qq.com/cgi-bin/groups/update?access_token="+accessToken;
		return doPost(url,json,"UTF-8");
	}
	/**
	 * 查询用户所在分组 POST
	 * @param accessToken
	 * @param openid
	 * @return
	 */
	public static String queryGroupForUser(String accessToken,String openid ){
		String json="{\"openid\":\""+openid+"\"}";
		String url="https://api.weixin.qq.com/cgi-bin/groups/getid?access_token="+accessToken;
		return doPost(url, json,"UTF-8");
	}
	
	/**
	 * 查询分组 GET
	 * @param accessToken
	 * @param groupName
	 * @return 失败返回 {"errcode":40013,"errmsg":"invalid appid"}
	 */
	public static String queryGroup(String accessToken){
		String url="https://api.weixin.qq.com/cgi-bin/groups/get?access_token="+accessToken;
		return getRemoteInfo(url,true);
	}

	//分组管理接口
	/**
	 * 创建分组 POST
	 * @param accessToken
	 * @param groupName
	 * @return failed {"errcode":40001,"errmsg":"invalid credential"}
	 */
	public static String createGroup(String accessToken,String groupName){
		String json="{\"group\":{\"name\":\""+groupName+"\"}}";
		String url="https://api.weixin.qq.com/cgi-bin/groups/create?access_token="+accessToken;
		return doPost(url,json,"UTF-8");
	}

	//发送被动响应消息对于每一个POST请求，开发者在响应包（Get）中返回特定XML结构，
	//对该消息进行响应（现支持回复文本、图片、图文、语音、视频、音乐)请注意，回复图片等多媒体消息时需要预先
	//上传多媒体文件到微信服务器，
	//只支持认证服务号。 
	//包含回复 文本,图片,语音,视频,音乐,图文消息
	
	
	//发送客服消息  包含文本,图片,语音,视频,音乐,图文  POST
	/**
	 * 发送图文消息 POST
	 * @param accessToken
	 * @param appid
	 * @param url 点击后跳转的url
	 * @param picurl
	 * @return
	 */
	public static String sendImageAndTextMesg(String accessToken,String appid,
			List<Map<String,String>> articles){
		try {
			JSONObject obj=new JSONObject();
			obj.put("touser", appid);
			obj.put("msgtype", "news");
			JSONArray jsonArr=new JSONArray();
			for(int i=0;i<articles.size();i++){
				Map<String,String> content=articles.get(i);
				JSONObject subObj=new JSONObject();
				subObj.put("title", content.get("title"));
				subObj.put("description", content.get("description"));
				subObj.put("url",  content.get("url"));
				subObj.put("picurl",  content.get("picurl"));
				jsonArr.put(subObj);
			}
			JSONObject artObj=new JSONObject();
			artObj.put("articles", jsonArr);
			obj.put("news", artObj);
			String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
			return doPost(url, obj.toString(), "UTF-8");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 发送音乐消息 POST
	 * @param accessToken
	 * @param appid
	 * @param media_id
	 * @param musicurl
	 * @param hqmusicurl
	 * @param thumb_media_id
	 * @param title
	 * @param description
	 * @return
	 */
	public static String sendMusicMesg(String accessToken,String appid,
			String musicurl,String hqmusicurl,String thumb_media_id,
			String title,String description){
		try {
			JSONObject obj=new JSONObject();
			obj.put("touser", appid);
			obj.put("msgtype", "music");
			JSONObject subObj=new JSONObject();
			subObj.put("title", title);
			subObj.put("description", description);
			subObj.put("musicurl", musicurl);
			subObj.put("hqmusicurl", hqmusicurl);
			subObj.put("thumb_media_id", thumb_media_id);
			obj.put("music", subObj);
			String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
			return doPost(url, obj.toString(), "UTF-8");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 发送视频消息 POST
	 * @param accessToken
	 * @param appid
	 * @param media_id
	 * @return
	 */
	public static String sendVideoMesg(String accessToken,String appid,String media_id,
			String title,String description){
		try {
			JSONObject obj=new JSONObject();
			obj.put("touser", appid);
			obj.put("msgtype", "video");
			JSONObject subObj=new JSONObject();
			subObj.put("media_id", media_id);
			subObj.put("title", title);
			subObj.put("description", description);
			obj.put("video", subObj);
			String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
			return doPost(url, obj.toString(), "UTF-8");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 发送语音消息 POST
	 * @param accessToken
	 * @param appid
	 * @param media_id
	 * @return
	 */
	public static String sendVoiceMesg(String accessToken,String appid,String media_id ){
		try {
			JSONObject obj=new JSONObject();
			obj.put("touser", appid);
			obj.put("msgtype", "voice");
			JSONObject subObj=new JSONObject();
			subObj.put("media_id", media_id);
			obj.put("voice", subObj);
			String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
			return doPost(url, obj.toString(), "UTF-8");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 发送图片消息 POST
	 * @param accessToken
	 * @param appid
	 * @param media_id
	 * @return
	 */
	public static String sendImageMesg(String accessToken,String appid,String media_id ){
		try {
			JSONObject obj=new JSONObject();
			obj.put("touser", appid);
			obj.put("msgtype", "image");
			JSONObject subObj=new JSONObject();
			subObj.put("media_id", media_id);
			obj.put("image", subObj);
			String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
			return doPost(url, obj.toString(), "UTF-8");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 发送文本消息 POST
	 * @param accessToken
	 * @param appid
	 * @param content
	 * @return
	 */
	public static String sendTextMesg(String accessToken,String appid,String content){
		try {
			JSONObject obj=new JSONObject();
			obj.put("touser", appid);
			obj.put("msgtype", "text");
			JSONObject subObj=new JSONObject();
			subObj.put("content", content);
			obj.put("text", subObj);
			String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
			return doPost(url, obj.toString(), "UTF-8");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	////////////////接收普通消息  返回的是网页////////////////////////////
	//包含 文本消息,图片消息,语音消息,视频消息,地理位置消息,链接消息
	/**
	 * 下载多媒体文件  GET
	 * @param accessToken
	 * @return
	 */
	public static String downloadMediaFile(String accessToken,String media_id){
		String url="http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
				+accessToken+"&media_id="+media_id;
		return getRemoteInfo(url, false);
	}
	/**
	 * 上传多媒体文件  POST/FORM 请求
	 * @param accessToken
	 * @param type
	 * form-data中媒体文件标识，有filename、filelength、content-type等信息 
	 * @return 
	 */
	public static String uploadMediaFile(String accessToken,String type,String mediaFile){
		String url="http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token="+accessToken+"&type=type";
		HashMap<String,String> params=new HashMap<String, String>();
		params.put("media", "");
		return doPost(url, params, "UTF-8");
	}
	/**
	 * 获取access_token GET请求
	 * @param 三个参数为必填 grant_type在获取access_token时必须时client_credential
	 * @return
	 */
	public static String getAccessToken(String appid,String secret){
		String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
				+appid+"&secret="+secret;
		return getRemoteInfo(url, false);
	}
	
//======================================end 微信========================================
	/**
	 * 调用webservice soap 方法 通用方法
	 * @param methodName
	 *            方法名
	 * @param params
	 *            映射文件
	 * @return 返回响应结果
	 */
	public static String callServiceWithMethodName(String methodName,
			HashMap<String, String> params) {
		String result = null;
		String SOAP_ACTION = namespace + "/" + methodName;
		SoapObject request = new SoapObject(namespace, methodName);
		if (params != null) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				request.addProperty(key, params.get(key));
			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		// envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		try {
			HttpTransportSE hts = new HttpTransportSE(wsdl);
			hts.call(SOAP_ACTION, envelope);
			result = envelope.getResponse().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 得到返回结果
		return result;
	}

	/**
	 * 根据快递公司和运单号查询物流信息
	 * 
	 * @param context
	 * @param com
	 *            快递公司代码 yunda
	 * @param flowCode
	 *            运单号 1600280377159 type:json/htm/text/xml encode:gbk/utf8
	 *            ord:asc/desc lang:中文/en返回英文结果，目前仅支持部分快递（EMS、顺丰、DHL）
	 * @return 物流json
	 */
	public static String getFlowInfo(Context context, String com,
			String flowCode) {
		String key = Prefs.getExpressKey();
		// 使用默认的可以不填写
		String url = "http://api.ickd.cn/?com=" + com + "&nu=" + flowCode
				+ "&id=" + key + "&type=&encode=&ord=&lang=";
		String result = "";
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "GBK"));
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				result += temp;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 获取远程url地址信息 */
	public static String getRemoteInfo(String url,boolean isPost) {
		String result = "";
		HttpClient client = new DefaultHttpClient();
		HttpUriRequest request=null;
		if(isPost){
			request=new HttpPost(url);
		}else{
			request = new HttpGet(url);
		}
		HttpResponse response;
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"));
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				result += temp;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/** 以post方式提交表单 */
	public static String doPost(String reqUrl,String params,
			String recvEncoding) {
		HttpURLConnection conn = null;
		String responseContent = null;
		try {
			URL url = new URL(reqUrl);
			HttpURLConnection url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("POST");
			url_con.setConnectTimeout(5000);
			url_con.setReadTimeout(5000);
			url_con.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			url_con.getOutputStream().write(b, 0, b.length);
			url_con.getOutputStream().flush();
			url_con.getOutputStream().close();

			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return responseContent;
	}
	
	/** 以post方式提交表单 */
	public static String doPost(String reqUrl, Map<String, String> parameters,
			String recvEncoding) {
		HttpURLConnection conn = null;
		String responseContent = null;
		try {
			StringBuffer params = new StringBuffer();
			for (Iterator iter = parameters.entrySet().iterator(); iter
					.hasNext();) {
				Entry element = (Entry) iter.next();
				params.append(element.getKey().toString());
				params.append("=");
				params.append(URLEncoder.encode(element.getValue().toString(),
						recvEncoding));
				params.append("&");
			}

			if (params.length() > 0) {
				params = params.deleteCharAt(params.length() - 1);
			}
			URL url = new URL(reqUrl);
			HttpURLConnection url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("POST");
			url_con.setConnectTimeout(5000);
			url_con.setReadTimeout(5000);
			url_con.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			url_con.getOutputStream().write(b, 0, b.length);
			url_con.getOutputStream().flush();
			url_con.getOutputStream().close();

			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return responseContent;
	}
}