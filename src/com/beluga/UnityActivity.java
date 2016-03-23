package com.beluga;

import com.beluga.belugakeys.Keys;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
//import android.widget.TextView;

public class UnityActivity extends UnityPlayerActivity{
	
	
	Context mContext = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }
    /*
    String uid;
    String appid =  "fanadv3";
    String apikey = "4fbc7b551f8ab63d4dadd8694ff261bf";
    String packageID = this.getClass().getPackage().getName();
    Intent intent; 
    boolean inMaintain = false;
    String dialogTitle = "Warnings";// if inMaintain is false setDialog title null
    String dialogMessage = "server in maintain...";// if inMaintain is false setDialog message null
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            try
            {
                Bundle bundle = data.getExtras();
                uid = bundle.getString("uid");
                String useruid = bundle.getString("userid");
                String userpwd = bundle.getString("pwd");
                Log.i("AuthClient", "UID:" + uid + "\nuseruid: " + useruid + "\n");
                UnityPlayer.UnitySendMessage("Main Camera","messgae"
                		,"Login User UID:"+uid +"\nuseruid: "+useruid+"\npwd:"+userpwd);

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        */
        Log.i("Main Demo", "onActivityResult start...");
        Log.i("Main Demo", "requestCode:" + requestCode);
        Log.i("Main Demo", "resultCode:" + Activity.RESULT_OK);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            try
            {
            	//TextView v = (TextView)this.findViewById(R.id.tv1);
                Bundle bundle = data.getExtras();
                String type = bundle.getString("type");
                Log.i("Main Demo", "type is "+ type);
                if(type.equals("LOGIN") ){
                	Log.i("Main Demo", "Is "+ type + "do if condition...");
                	String uid = bundle.getString("uid");
                    String useruid = bundle.getString("userid");
                    String userpwd = bundle.getString("pwd");
                    String thirdPartnerId = bundle.getString("token");
                    //v.setText("Login info :\nLogin User UID:"+uid +"\nuseruid: "+useruid+"\npwd:"+userpwd+"\nthirdPartnerId:"+thirdPartnerId);
                    UnityPlayer.UnitySendMessage("Main Camera","message"
                    		,"Login info :\nLogin User UID:"+uid +"\nuseruid: "+useruid+"\npwd:"+userpwd+"\nthirdPartnerId:"+thirdPartnerId);

                    Log.i("AuthClient", "UID:" + uid + "\nuseruid: " + useruid + "\n"+ "\nthirdPartnerId: " + thirdPartnerId + "\n");
                }else if(type.equals("PAYMENT") ){
                	Log.i("Main Demo", "Is "+ type + "do else if condition...");
                	String tid = bundle.getString("tradeid");
    	            String r = bundle.getString("receipt");
    	            String o = bundle.getString("order");
    	            String os = bundle.getString("ordersign");
    	            Log.i("ActivityResult trade id", tid);
    	            Log.i("ActivityResult receipt", r);
    	            Log.i("ActivityResult order", o);
    	            Log.i("ActivityResult orderSign", os);
    	            
    	            UnityPlayer.UnitySendMessage("Main Camera","message"
                    		,"Receipt info :\ntrade id: \n"+ tid +"\n"+ "receipt: \n"+ r +"\n"+"order:\n"+o+"\n" );
    	            /*
    	              v.setText("trade id: \n"+ tid +"\n"+
    	             
    	            			"receipt: \n"+ r +"\n"+
    	            			"order:\n"+o+"\n" );
    	            */
                }
                
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            
        }
        Log.i("Main Demo", "onActivityResult end...");
        
    }
 
	public void StartAuthClient() {
			
		    String appid =  "fanadv3";
		    String apikey = "4fbc7b551f8ab63d4dadd8694ff261bf";
		    String packageID = this.getClass().getPackage().getName();
		    Intent intent; 
		    boolean inMaintain = false;
		    String dialogTitle = "Warnings";// if inMaintain is false setDialog title null
		    String dialogMessage = "server in maintain...";// if inMaintain is false setDialog message null
	
	        intent = new Intent(this, com.beluga.loginpage.AuthClientActivity.class);
	        intent.putExtra(Keys.AppID.toString(), appid);
	        intent.putExtra(Keys.ApiKey.toString(), apikey);
	        intent.putExtra(Keys.PackageID.toString(), packageID);
	        //intent.putExtra(Keys.GameLogo.toString(), R.drawable.cbimage);
	        intent.putExtra(Keys.ActiveMaintainDialog.toString(), inMaintain);
	        intent.putExtra(Keys.DialogMessage.toString(), dialogMessage);
	        intent.putExtra(Keys.DialogTitle.toString(), dialogTitle);
	        Log.i("pID", packageID);
	        startActivityForResult(intent, 100);
	    }
	
	
}
