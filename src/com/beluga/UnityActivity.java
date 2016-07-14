package com.beluga;

//import org.json.JSONObject;

import com.beluga.belugakeys.Keys;
import com.beluga.payment.iab.InAppBillingActivity;
import com.beluga.payment.mol.MOLActivity;
import com.beluga.payment.mycard.MyCardActivity;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class UnityActivity extends UnityPlayerActivity{
	
	
	static Context mContext = null;
	//static String unityGameObjName;
	//static String unityMethod;
	String unityGameObjName;
	String unityMethod;
	static Activity act;
	
	static String  Appid, Apikey, PackageID, DialogTitle, DialogMessage;  
	static boolean InMaintain;
	static int AuthChannel;  
	static byte[] GameLogo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        act = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        Log.i("Main Demo", "onActivityResult start...");
        Log.i("Main Demo", "requestCode:" + requestCode);
        Log.i("Main Demo", "resultCode:" + Activity.RESULT_OK);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            try
            {
            	
                Bundle bundle = data.getExtras();
                String type = bundle.getString("type");
                String sendUnityStr;
                Log.i("Main Demo", "type is "+ type);
                if(type.equals("LOGIN") ){
                	Log.i("Main Demo", "Is "+ type + "do if condition...");
                	String jsonData = bundle.getString(Keys.JsonData.toString());
					//JSONObject jObj = new JSONObject( jsonData );
					//String token = jObj.getString("token");
                	sendUnityStr = "json Data:" + jsonData +"\n";
                    Log.i("sent to unity Login", sendUnityStr);
                   
                    UnityPlayer.UnitySendMessage(unityGameObjName,unityMethod,sendUnityStr);

                    
                }else if(type.equals("PAYMENT") ){
                	Log.i("Main Demo", "Is "+ type + "do else if condition...");
    	           
    	            
                	String order = bundle.getString("order");
    	            String sign = bundle.getString("sign");
    	            sendUnityStr = "json order: \n"+ order +"\n"+"sign:\n"+sign+"\n";
                	Log.i("sent to unity payment", sendUnityStr);
    	            UnityPlayer.UnitySendMessage(unityGameObjName,unityMethod ,sendUnityStr ); 
                }
                
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            
        }
        Log.i("Main Demo", "onActivityResult end...");
        
    }
 
	public void StartAuthClient(String UnityGameObj, String UnityMethod, int authChannel,String appid, String apikey,  
								byte[] gameLogo, String packageID,  boolean inMaintain, String dialogTitle, String dialogMessage) {
			Log.i("UnityActivity", "StartAuthClient...");
			Intent intent; 
			this.unityGameObjName = UnityGameObj;
			this.unityMethod = UnityMethod;
			//unityGameObjName = UnityGameObj;
			//unityMethod = UnityMethod;
	        intent = new Intent(this, com.beluga.loginpage.AuthClientActivity.class);
	        intent.putExtra(Keys.AuthChannel.toString(), authChannel);
	        intent.putExtra(Keys.AppID.toString(), appid);
	        intent.putExtra(Keys.ApiKey.toString(), apikey);
	        intent.putExtra(Keys.PackageID.toString(), packageID);
	        intent.putExtra(Keys.GameLogoForByteArray.toString(), gameLogo);//
	        intent.putExtra(Keys.ActiveMaintainDialog.toString(), inMaintain);
	        intent.putExtra(Keys.DialogMessage.toString(), dialogMessage);
	        intent.putExtra(Keys.DialogTitle.toString(), dialogTitle);
	        Log.i("pID", packageID);
	        startActivityForResult(intent, 100);
	        //act.startActivityForResult(intent, 100);
	        //startActivity(intent);
	}
	/*
	
	public static void StartAuthClient(String UnityGameObj, String UnityMethod, int authChannel,String appid, String apikey,  
			byte[] gameLogo, String packageID,  boolean inMaintain, String dialogTitle, String dialogMessage) {
				Log.i("UnityActivity", "StartAuthClient...");
				Intent intent; 
				//this.unityGameObjName = UnityGameObj;
				//this.unityMethod = UnityMethod;
				unityGameObjName = UnityGameObj;
				unityMethod = UnityMethod;
				intent = new Intent(mContext, com.beluga.loginpage.AuthClientActivity.class);
				intent.putExtra(Keys.AuthChannel.toString(), authChannel);
				intent.putExtra(Keys.AppID.toString(), appid);
				intent.putExtra(Keys.ApiKey.toString(), apikey);
				intent.putExtra(Keys.PackageID.toString(), packageID);
				intent.putExtra(Keys.GameLogoForByteArray.toString(), gameLogo);//
				intent.putExtra(Keys.ActiveMaintainDialog.toString(), inMaintain);
				intent.putExtra(Keys.DialogMessage.toString(), dialogMessage);
				intent.putExtra(Keys.DialogTitle.toString(), dialogTitle);
				Log.i("pID", packageID);
				//startActivityForResult(intent, 100);
				act.startActivityForResult(intent, 100);
				//startActivity(intent);
		}
*/
    /*
    public static void setArgs(String UnityGameObj, String UnityMethod, int authChannel,String appid, String apikey,  
			byte[] gameLogo, String packageID,  boolean inMaintain, String dialogTitle, String dialogMessage) {
			Log.i("UnityActivity", "setArgs...");
			
			unityGameObjName = UnityGameObj;
			Log.i("UnityActivity", "setArgs unityGameObjName"+ unityGameObjName);
			unityMethod = UnityMethod;
			Log.i("UnityActivity", "setArgs unityMethod"+ unityMethod);
			Appid = appid; 
			Log.i("UnityActivity", "setArgs Appid"+ Appid);
			Apikey =apikey;
			Log.i("UnityActivity", "setArgs Apikey"+ Apikey);
			PackageID= packageID;
			Log.i("UnityActivity", "setArgs PackageID"+ PackageID);
			DialogTitle=dialogTitle;
			Log.i("UnityActivity", "setArgs DialogTitle"+ DialogTitle);
			DialogMessage= dialogMessage;  
			Log.i("UnityActivity", "setArgs DialogMessage"+ DialogMessage);
			InMaintain = inMaintain;
			//Log.i("UnityActivity", "setArgs unityMethod"+ unityMethod);
			AuthChannel = authChannel;  
			//Log.i("UnityActivity", "setArgs unityMethod"+ unityMethod);
			GameLogo = gameLogo;
			//Log.i("UnityActivity", "setArgs unityMethod"+ unityMethod);
			
    }
    */
    /*
    public void StartAuthClient() {
		Log.i("UnityActivity", "StartAuthClient...");
		Intent intent; 
		//this.unityGameObjName = UnityGameObj;
		//this.unityMethod = UnityMethod;
		//unityGameObjName = UnityGameObj;
		//unityMethod = UnityMethod;
		intent = new Intent(this, com.beluga.loginpage.AuthClientActivity.class);
		intent.putExtra(Keys.AuthChannel.toString(), AuthChannel);
		Log.i("UnityActivity", "StartAuthClient Appid"+ Appid);
		intent.putExtra(Keys.AppID.toString(), Appid);
		Log.i("UnityActivity", "StartAuthClient Apikey"+ Apikey);
		intent.putExtra(Keys.ApiKey.toString(), Apikey);
		intent.putExtra(Keys.PackageID.toString(), PackageID);
		intent.putExtra(Keys.GameLogoForByteArray.toString(), GameLogo);//
		intent.putExtra(Keys.ActiveMaintainDialog.toString(), InMaintain);
		intent.putExtra(Keys.DialogMessage.toString(), DialogMessage);
		intent.putExtra(Keys.DialogTitle.toString(), DialogTitle);
		//Log.i("pID", packageID);
		startActivityForResult(intent, 100);
		//act.startActivityForResult(intent, 100);
		//startActivity(intent);
    }
	*/
	
	public void startGooglePaymentButtonPress(String UnityGameObj, String UnityMethod, String SKU_GAS, String base64, String userId,
			String serverId, String role, String orderId){ 
		//String SKU_GAS = "beluga.gold";
		//String base64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn2J6q0hd9FhArBYBcKSJabarKunSudfg/LUAwstUY/6UN581eoXEKBo7U2Kd2IA1GaAAXS3vAx4Nv9DAJrurBNof6JpCaEKjhzHLI8TWRqXh77K9dwM8mNMBnN83pP05pRLOMUz33Q/gd1wpQgFzumjl2ai/wAaIqb2YLCvOCUKPIBz5F4RedIySdMfSvIVsDt1FrIOxmPgyL7PFfU42nJMGle7o01hB+vvcMoOaOJu6Kmjkgbru6X6TRWXFfVXY/27iTbCmF1ASsS6btJgQAZr49Km23lZUlV4T+Po9CFfy04PS+uBXJvleUJPuKQe4GMLtcEfUkhQDZpllUvEI7wIDAQAB";
		//String UserId = "1030176"; //user id
		Log.i("Unity Activity", "IAB Start...");
		
		this.unityGameObjName = UnityGameObj;
		this.unityMethod = UnityMethod;
		Intent i = new Intent(this, com.beluga.payment.iab.InAppBillingActivity.class);
		Bundle b = new Bundle();
		b.putString(InAppBillingActivity.base64EncodedPublicKey, base64);
		b.putString(InAppBillingActivity.ItemID, SKU_GAS);
		b.putString(InAppBillingActivity.User_ID, userId);

	
		b.putString(InAppBillingActivity.Server, serverId); 
		b.putString(InAppBillingActivity.Role, role); 
		b.putString(InAppBillingActivity.Order, orderId);
		i.putExtras(b);
		startActivityForResult(i, InAppBillingActivity.GBilling_REQUEST);
		Log.i("Unity Activity", "IAB end...");
	}
	
	public void startMOLPaymentButtonPress(String UnityGameObj, String UnityMethod, String user_id, String game_id, String app_id,
			String PackageID, String server_id, String role) {
		//String user_id = "1000005"; 
		//String game_id = "04101786";
		//String app_id = "herinv";
		//String PackageID = this.getClass().getPackage().getName();
		//String server_id = "999";
		//String role = "leo";
		this.unityGameObjName = UnityGameObj;
		this.unityMethod = UnityMethod;
		Intent i = new Intent(this, MOLActivity.class);
		Bundle b = new Bundle();
		b.putString("UserId",user_id);
		b.putString("gamerID", game_id);
		b.putString("AppID", app_id);
		b.putString("PackageID", PackageID);
		b.putString("ServerID", server_id);
		b.putString("RoleName", role);
		i.putExtras(b);
		startActivity(i);
	}
	
	public void startMyCardSmallPaymentButtonPress(String UnityGameObj, String UnityMethod, String apikey,String appid, String uid, 
			String server_id, String role, String itemId, String orderId) {
		
		this.unityGameObjName = UnityGameObj;
		this.unityMethod = UnityMethod;
		Intent i = new Intent(this, MyCardActivity.class);
		Bundle b = new Bundle();
		//String serviceType = MyCardActivity.TYPE_SMALL_PAYMENT;
		//String apikey = "412c1bd510967dce3b050842a35fae18";
		//String appid = "kilmasa";
		//String uid = "1040714";
		b.putString("type",  MyCardActivity.TYPE_SMALL_PAYMENT); 
		b.putString(Keys.ApiKey.toString(), apikey);
		b.putString(Keys.AppID.toString(), appid);
		b.putString("uid", uid);
		
		
		b.putString("tserver", server_id); 
		b.putString("trol", role); 
		b.putString("titem", itemId);
		b.putString("order", orderId);
		i.putExtras(b);
		startActivity(i);
	}
	
	public void startMyCardSerialNumberButtonPress(String UnityGameObj, String UnityMethod, String apikey,String appid, String uid, 
			String server_id, String role, String itemId, String orderId) {
		
		this.unityGameObjName = UnityGameObj;
		this.unityMethod = UnityMethod;
		Intent i = new Intent(this, MyCardActivity.class);
		Bundle b = new Bundle();
		b.putString("type", MyCardActivity.TYPE_SERIAL_NUMBER); 
		b.putString(Keys.ApiKey.toString(), "4fbc7b551f8ab63d4dadd8694ff261bf");
		b.putString(Keys.AppID.toString(), "fanadv3");
		b.putString("uid","1000448");
		
		b.putString("tserver", server_id); 
		b.putString("trol", role); 
		b.putString("titem", itemId);
		b.putString("order", orderId);
		i.putExtras(b);
		startActivity(i);
	}
	
	
}
