package org.lance.weixin.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lance.weixin.service.NetService2;
import org.lance.weixin.util.Prefs;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends BaseActivity implements OnClickListener{

	private Button btn0;
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;
	private Button btn6;
	private Button btn7;
	private Button btn8;
	private Button btn9;
	private Button btn10;
	private Button btn11;
	private Button btn12;
	private Button btn13;
	private Button btn14;
	private Button btn15;
	private Button btn16;
	private Button btn17;
	private Button btn18;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn0=(Button)findViewById(R.id.main_btn0);
		btn1=(Button)findViewById(R.id.main_btn1);
		btn2=(Button)findViewById(R.id.main_btn2);
		btn3=(Button)findViewById(R.id.main_btn3);
		btn4=(Button)findViewById(R.id.main_btn4);
		btn5=(Button)findViewById(R.id.main_btn5);
		btn6=(Button)findViewById(R.id.main_btn6);
		btn7=(Button)findViewById(R.id.main_btn7);
		btn8=(Button)findViewById(R.id.main_btn8);
		btn9=(Button)findViewById(R.id.main_btn9);
		btn10=(Button)findViewById(R.id.main_btn10);
		btn11=(Button)findViewById(R.id.main_btn11);
		btn12=(Button)findViewById(R.id.main_btn12);
		btn13=(Button)findViewById(R.id.main_btn13);
		btn14=(Button)findViewById(R.id.main_btn14);
		btn15=(Button)findViewById(R.id.main_btn15);
		btn16=(Button)findViewById(R.id.main_btn16);
		btn17=(Button)findViewById(R.id.main_btn17);
		btn18=(Button)findViewById(R.id.main_btn18);
		btn0.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		btn9.setOnClickListener(this);
		btn10.setOnClickListener(this);
		btn11.setOnClickListener(this);
		btn12.setOnClickListener(this);
		btn13.setOnClickListener(this);
		btn14.setOnClickListener(this);
		btn15.setOnClickListener(this);
		btn16.setOnClickListener(this);
		btn17.setOnClickListener(this);
		btn18.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.main_btn0://ok
			execTask(new Thread(){
				public void run(){
					String result=NetService2.getAccessToken(Prefs.getAppID(), Prefs.getAppSecret());
					try {
						JSONObject obj=new JSONObject(result);
						String token=obj.getString("access_token");
						if(token!=null){
							Prefs.putAccessToken(MainActivity.this, token);
							Message msg=handler.obtainMessage(MSG_OK);
							msg.obj=token;
							msg.sendToTarget();
						}else{
							handler.obtainMessage(MSG_ERROR).sendToTarget();
						}
					} catch (JSONException e) {
						e.printStackTrace();
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn1://ok
			execTask(new Thread(){
				public void run(){
					String result=NetService2.createGroup(Prefs.getAccessToken(MainActivity.this), "dsfsf");
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn2://ok
			execTask(new Thread(){
				public void run(){
					String result=NetService2.queryGroup(Prefs.getAccessToken(MainActivity.this));
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn3://查询用户所在分组
			execTask(new Thread(){
				public void run(){
					String result=NetService2.queryGroupForUser(Prefs.getAccessToken(MainActivity.this),Prefs.getAppID());
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn4://ok
			execTask(new Thread(){
				public void run(){
					String result=NetService2.renameGroup(Prefs.getAccessToken(MainActivity.this),102,"34534s分组");
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn5:// TODO
			execTask(new Thread(){
				public void run(){
					String result=NetService2.removeToGroup(Prefs.getAccessToken(MainActivity.this),Prefs.getAppID(),105);
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn6:// TODO
			execTask(new Thread(){
				public void run(){
					String result=NetService2.getUserInfo(Prefs.getAccessToken(MainActivity.this),"wxb01584825c66a1b3");
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn7://ok 获取关注者列表
			execTask(new Thread(){
				public void run(){
					//olyOuuL_EI8-NrdtspMEmmGfcSP8
					String result=NetService2.getAttentionList(Prefs.getAccessToken(MainActivity.this),"");
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn8://ok 创建自定义菜单
			execTask(new Thread(){
				public void run(){
					try {
						List<Map<String,String>> menus=new ArrayList<Map<String,String>>();
						Map<String,String> menu=new HashMap<String,String>();
						menu.put("type", "click");
						menu.put("name", "今日之歌");
						menu.put("key", "V1001_TODAY_MUSIC");
//						JSONArray subMenus=new JSONArray();
//						JSONObject subObj=new JSONObject();
//						subObj.put("type", "view");
//						subObj.put("name", "搜索");
//						subObj.put("url", "http://www.soso.com/");
//						subMenus.put(subObj);
//						menu.put("sub_button", subMenus.toString());
						menus.add(menu);
						menu.put("type", "click");
						menu.put("name", "歌手简介");
						menu.put("key", "V1001_TODAY_SINGER");
						menus.add(menu);
						String result=NetService2.createMenuList(Prefs.getAccessToken(MainActivity.this),menus);
						if(result!=null){
							Message msg=handler.obtainMessage(MSG_OK);
							msg.obj=result;
							msg.sendToTarget();
						}else{
							handler.obtainMessage(MSG_ERROR).sendToTarget();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			break;
		case R.id.main_btn9://ok  获取自定义菜单
			execTask(new Thread(){
				public void run(){
					String result=NetService2.getMenuList(Prefs.getAccessToken(MainActivity.this));
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn10://ok  删除自定义菜单
			execTask(new Thread(){
				public void run(){
					String result=NetService2.deleteMenu(Prefs.getAccessToken(MainActivity.this));
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn11:// TODO 发送文本消息
			execTask(new Thread(){
				public void run(){
					String result=NetService2.sendTextMesg(Prefs.getAccessToken(MainActivity.this),
							Prefs.getAppID(),"sdfjdslkfjadsfjadls");
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn12:// TODO 发送图片消息
			execTask(new Thread(){
				public void run(){
					String result=NetService2.deleteMenu(Prefs.getAccessToken(MainActivity.this));
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn13:// TODO 发送语音消息
			execTask(new Thread(){
				public void run(){
					String result=NetService2.deleteMenu(Prefs.getAccessToken(MainActivity.this));
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn14:// TODO 发送视频消息
			execTask(new Thread(){
				public void run(){
					String result=NetService2.deleteMenu(Prefs.getAccessToken(MainActivity.this));
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn15:// TODO 发送音乐消息
			execTask(new Thread(){
				public void run(){
					String result=NetService2.deleteMenu(Prefs.getAccessToken(MainActivity.this));
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn16:// TODO 发送图文消息
			execTask(new Thread(){
				public void run(){
					String result=NetService2.deleteMenu(Prefs.getAccessToken(MainActivity.this));
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn17://ok 创建二维码
			execTask(new Thread(){
				public void run(){
					String result=NetService2.createForeverQRCode(Prefs.getAccessToken(MainActivity.this),
							"QR_SCENE", 2343);
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		case R.id.main_btn18://ok 获取二维码图片  直接返回的图片流
			execTask(new Thread(){
				public void run(){
					String result=NetService2.getQRCode("gQGS7zoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL25VT1ExLWZsZlJrRWNCamszbTNxAAIEeCQXUwMEPAAAAA==");
					if(result!=null){
						Message msg=handler.obtainMessage(MSG_OK);
						msg.obj=result;
						msg.sendToTarget();
					}else{
						handler.obtainMessage(MSG_ERROR).sendToTarget();
					}
				}
			});
			break;
		}
	}
	
	private static final int MSG_OK=0x666;
	private static final int MSG_ERROR=0x999;
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case MSG_OK:
				System.out.println(msg.obj);
				showHintLong("调用成功~"+msg.obj);
				break;
			case MSG_ERROR:
				showHintLong("调用出错!");
				break;
			}
		}
		
	};

}
