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
 * ����.NETƽ̨��webservice ֻ��Ҫwsdl��ַ�� �Ӷ���֪�����ռ� soapActionһ���������ռ�+"/"+��������һ����Ҫwsdl��ַ
 * 
 * @author lance
 * 
 */
public class NetService2 {
	private final static String TAG = "NetService";
	static String wsdl = Prefs.getWsdl();
	static String namespace = Prefs.getNamespace();

	
	//==========================================΢�Žӿڷ���====================================
	//δ��֤���˺�ֻ�������ӿ�Ȩ��
	/////////////////////////////���ɴ������Ķ�ά��///////////////////////////////
	
	/**
	 * ͨ��ticket��ȡ��ά��ͼƬ GET
	 * @param ticket  ok ��������200 ticket�Ƿ�����404
	 * @return ���ѣ�TICKET�ǵý���UrlEncode
	 */
	public static String getQRCode(String ticket){
		String url="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
		return getRemoteInfo(url, false);
	}
	/**
	 * ������ά�� POST
	 * @param accessToken
	 * @param expire_seconds �ö�ά����Чʱ�䣬����Ϊ��λ�� ��󲻳���1800�� 
	 * @param action_name  	��ά�����ͣ�QR_SCENEΪ��ʱ,QR_LIMIT_SCENEΪ���� 
	 * @param action_info  	��ά����ϸ��Ϣ 
	 * @param scene_id  ����ֵID����ʱ��ά��ʱΪ32λ��0���ͣ����ö�ά��ʱ���ֵΪ100000��Ŀǰ����ֻ֧��1--100000�� 
	 * @return
	 */
	public static String createTempQRCode(String accessToken,int expire_seconds,String action_name,
			int scene_id){
		String json="{'expire_seconds': "+expire_seconds+", 'action_name': '"+action_name
				+"', 'action_info': {'scene': {'scene_id': "+scene_id+"}}}";
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken;
		return doPost(url,json,"UTF-8");
	}
	/** �������ö�ά�� */
	public static String createForeverQRCode(String accessToken,String action_name,
			int scene_id){
		String json="{\"action_name\": \""+action_name+"\", \"action_info\": {\"scene\": {\"scene_id\": "+scene_id+"}}}";
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken;
		return doPost(url,json,"UTF-8");
	}
	/////////////////////////////�Զ���˵�//////////////////////
	//�Զ���˵��¼�����
	/**
	 * ɾ���Զ���˵� GET
	 * @param accessToken
	 * @return
	 */
	public static String deleteMenu(String accessToken){
		String url="https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+accessToken;
		return getRemoteInfo(url, false);
	}
	/**
	 * �Զ���˵���ѯ�ӿ� GET
	 * @param accessToken
	 * @return
	 */
	public static String getMenuList(String accessToken){
		String url="https://api.weixin.qq.com/cgi-bin/menu/get?access_token="+accessToken;
		return getRemoteInfo(url, false);
	}
	/**
	 * �����Զ���˵��б� POST
	 * @param accessToken
	 * @param button һ���˵����飬����ӦΪ1~3�� 
	 * @param sub_button �����˵����飬����ӦΪ1~5�� 
	 * @param type �˵�����Ӧ�������ͣ�Ŀǰ��click��view�������� 
	 * @param name �˵����⣬������16���ֽڣ��Ӳ˵�������40���ֽ� 
	 * @param key  click���ͱ��� 	�˵�KEYֵ��������Ϣ�ӿ����ͣ�������128�ֽ� 
	 * @param url ��ҳ���ӣ��û�����˵��ɴ����ӣ�������256�ֽ� 
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
	 * ��ȡ�û�����λ��
	 * ��ȡ�û�����λ�õķ�ʽ�����֣�һ���ǽ��ڽ���Ựʱ�ϱ�һ�Σ�
	 * һ���ǽ���Ự��ÿ��5���ϱ�һ�Ρ����ںſ����ڹ���ƽ̨��վ�����á�
	 * �û�ͬ���ϱ�����λ�ú�ÿ�ν��빫�ںŻỰʱ�������ڽ���ʱ�ϱ�����λ�ã����ڽ���Ự��ÿ5���ϱ�һ�ε���λ�ã�
	 * �ϱ�����λ��������XML���ݰ�����������д��URL��ʵ�֡� 
	 * @return
	 */
	public static String getLocation(){
		return null;
	}
	
	/**
	 * ��ȡ��ע���б�  GET
	 * @param accessToken
	 * @param next_openid ��һ����ȡ��OPENID������Ĭ�ϴ�ͷ��ʼ��ȡ
	 * @return
	 */
	public static String getAttentionList(String accessToken,String next_openid){
		String url="https://api.weixin.qq.com/cgi-bin/user/get?access_token="+
				accessToken+"&next_openid="+next_openid;
		return getRemoteInfo(url, true);
	}
	/**
	 * ��ȡ�û�������Ϣ GET����
	 * @param accessToken
	 * @param openid
	 * @return
	 */
	public static String getUserInfo(String accessToken,String openid){
		String url="https://api.weixin.qq.com/cgi-bin/user/info?access_token="
				+accessToken+"&openid="+openid+"&lang=zh_CN";
		return getRemoteInfo(url, false);
	}
	
	///////////////////////������� start//////////////////////
	/**
	 * �ƶ��û����� POST
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
	 * �޸ķ����� POST
	 * @param accessToken
	 * @param id ��΢�ŷ���
	 * @param newGroupName
	 * @return {"errcode": 0, "errmsg": "ok"}, {"errcode":40013,"errmsg":"invalid appid"}
	 */
	public static String renameGroup(String accessToken,int id,String newGroupName){
		String json="{\"group\":{\"id\":"+id+",\"name\":\""+newGroupName+"\"}}";
		String url="https://api.weixin.qq.com/cgi-bin/groups/update?access_token="+accessToken;
		return doPost(url,json,"UTF-8");
	}
	/**
	 * ��ѯ�û����ڷ��� POST
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
	 * ��ѯ���� GET
	 * @param accessToken
	 * @param groupName
	 * @return ʧ�ܷ��� {"errcode":40013,"errmsg":"invalid appid"}
	 */
	public static String queryGroup(String accessToken){
		String url="https://api.weixin.qq.com/cgi-bin/groups/get?access_token="+accessToken;
		return getRemoteInfo(url,true);
	}

	//�������ӿ�
	/**
	 * �������� POST
	 * @param accessToken
	 * @param groupName
	 * @return failed {"errcode":40001,"errmsg":"invalid credential"}
	 */
	public static String createGroup(String accessToken,String groupName){
		String json="{\"group\":{\"name\":\""+groupName+"\"}}";
		String url="https://api.weixin.qq.com/cgi-bin/groups/create?access_token="+accessToken;
		return doPost(url,json,"UTF-8");
	}

	//���ͱ�����Ӧ��Ϣ����ÿһ��POST���󣬿���������Ӧ����Get���з����ض�XML�ṹ��
	//�Ը���Ϣ������Ӧ����֧�ֻظ��ı���ͼƬ��ͼ�ġ���������Ƶ������)��ע�⣬�ظ�ͼƬ�ȶ�ý����Ϣʱ��ҪԤ��
	//�ϴ���ý���ļ���΢�ŷ�������
	//ֻ֧����֤����š� 
	//�����ظ� �ı�,ͼƬ,����,��Ƶ,����,ͼ����Ϣ
	
	
	//���Ϳͷ���Ϣ  �����ı�,ͼƬ,����,��Ƶ,����,ͼ��  POST
	/**
	 * ����ͼ����Ϣ POST
	 * @param accessToken
	 * @param appid
	 * @param url �������ת��url
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
	 * ����������Ϣ POST
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
	 * ������Ƶ��Ϣ POST
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
	 * ����������Ϣ POST
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
	 * ����ͼƬ��Ϣ POST
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
	 * �����ı���Ϣ POST
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
	
	
	////////////////������ͨ��Ϣ  ���ص�����ҳ////////////////////////////
	//���� �ı���Ϣ,ͼƬ��Ϣ,������Ϣ,��Ƶ��Ϣ,����λ����Ϣ,������Ϣ
	/**
	 * ���ض�ý���ļ�  GET
	 * @param accessToken
	 * @return
	 */
	public static String downloadMediaFile(String accessToken,String media_id){
		String url="http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
				+accessToken+"&media_id="+media_id;
		return getRemoteInfo(url, false);
	}
	/**
	 * �ϴ���ý���ļ�  POST/FORM ����
	 * @param accessToken
	 * @param type
	 * form-data��ý���ļ���ʶ����filename��filelength��content-type����Ϣ 
	 * @return 
	 */
	public static String uploadMediaFile(String accessToken,String type,String mediaFile){
		String url="http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token="+accessToken+"&type=type";
		HashMap<String,String> params=new HashMap<String, String>();
		params.put("media", "");
		return doPost(url, params, "UTF-8");
	}
	/**
	 * ��ȡaccess_token GET����
	 * @param ��������Ϊ���� grant_type�ڻ�ȡaccess_tokenʱ����ʱclient_credential
	 * @return
	 */
	public static String getAccessToken(String appid,String secret){
		String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
				+appid+"&secret="+secret;
		return getRemoteInfo(url, false);
	}
	
//======================================end ΢��========================================
	/**
	 * ����webservice soap ���� ͨ�÷���
	 * @param methodName
	 *            ������
	 * @param params
	 *            ӳ���ļ�
	 * @return ������Ӧ���
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
		// �õ����ؽ��
		return result;
	}

	/**
	 * ���ݿ�ݹ�˾���˵��Ų�ѯ������Ϣ
	 * 
	 * @param context
	 * @param com
	 *            ��ݹ�˾���� yunda
	 * @param flowCode
	 *            �˵��� 1600280377159 type:json/htm/text/xml encode:gbk/utf8
	 *            ord:asc/desc lang:����/en����Ӣ�Ľ����Ŀǰ��֧�ֲ��ֿ�ݣ�EMS��˳�ᡢDHL��
	 * @return ����json
	 */
	public static String getFlowInfo(Context context, String com,
			String flowCode) {
		String key = Prefs.getExpressKey();
		// ʹ��Ĭ�ϵĿ��Բ���д
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
	
	/** ��ȡԶ��url��ַ��Ϣ */
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

	/** ��post��ʽ�ύ�� */
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
	
	/** ��post��ʽ�ύ�� */
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