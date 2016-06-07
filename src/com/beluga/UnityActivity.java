package com.beluga;

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
	
	
	Context mContext = null;
	String unityGameObjName;
	String unityMethod;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
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
                Log.i("Main Demo", "type is "+ type);
                if(type.equals("LOGIN") ){
                	Log.i("Main Demo", "Is "+ type + "do if condition...");
                	String uid = bundle.getString("uid");
                    String useruid = bundle.getString("userid");
                    String userpwd = bundle.getString("pwd");
                    String thirdPartnerId = bundle.getString("token");
                    
                    Log.i("sent to unity Login", "UID:" + uid + "\nuseruid: " + useruid + "\n"+ "\nthirdPartnerId: " + thirdPartnerId + "\n");
                   
                    UnityPlayer.UnitySendMessage(unityGameObjName,unityMethod
                    		,"Login info :\nLogin User UID:"+uid +"\nuseruid: "+useruid+"\npwd:"+userpwd+"\nthirdPartnerId:"+thirdPartnerId);

                    
                }else if(type.equals("PAYMENT") ){
                	Log.i("Main Demo", "Is "+ type + "do else if condition...");
    	           
    	            
    	            String tid = bundle.getString("tradeid");
                	int code = bundle.getInt("code");
                	String m = bundle.getString("message");
                	Log.i("sent to unity payment","trade id: \n"+ tid +"\n"+
    	            			"code: \n"+ code +"\n"+
    	            			"message:\n"+m+"\n" );
    	            UnityPlayer.UnitySendMessage(unityGameObjName,unityMethod
                    		,"Receipt info :\ntrade id: \n"+ tid +"\n"+
    	            			"code: \n"+ code +"\n"+
    	            			"message:\n"+m+"\n"); 
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
			
			Intent intent; 
			this.unityGameObjName = UnityGameObj;
			this.unityMethod = UnityMethod;
	        intent = new Intent(this, com.beluga.loginpage.AuthClientActivity.class);
	        intent.putExtra(Keys.AuthChannel.toString(), authChannel);
	        intent.putExtra(Keys.AppID.toString(), appid);
	        intent.putExtra(Keys.ApiKey.toString(), apikey);
	        intent.putExtra(Keys.PackageID.toString(), packageID);
	        intent.putExtra(Keys.GameLogoForByteArray.toString(), gameLogo);
	        intent.putExtra(Keys.ActiveMaintainDialog.toString(), inMaintain);
	        intent.putExtra(Keys.DialogMessage.toString(), dialogMessage);
	        intent.putExtra(Keys.DialogTitle.toString(), dialogTitle);
	        Log.i("pID", packageID);
	        startActivityForResult(intent, 100);
	}
	
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
