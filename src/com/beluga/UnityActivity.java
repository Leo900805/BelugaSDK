package com.beluga;

import com.beluga.belugakeys.Keys;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class UnityActivity extends UnityPlayerActivity{
	
	
	Context mContext = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }
    
    String uid;
    String appid =  "fanadv3";
    String apikey = "4fbc7b551f8ab63d4dadd8694ff261bf";
    String packageID = this.getClass().getPackage().getName();
    Intent intent; 
    boolean inMaintain = false;
    String dialogTitle = "Warnings";// if inMaintain is false setDialog title null
    String dialogMessage = "server in maintain...";// if inMaintain is false setDialog message null

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
    }
 
    public void StartNicePlayAuthClient() {
        intent = new Intent(mContext, com.beluga.loginpage.AuthClientActivity.class);
        intent.putExtra(Keys.AppID.toString(), appid);
        intent.putExtra(Keys.ApiKey.toString(), apikey);
        intent.putExtra(Keys.PackageID.toString(), packageID);
        intent.putExtra(Keys.ActiveMaintainDialog.toString(), this.inMaintain);
        intent.putExtra(Keys.DialogMessage.toString(), dialogMessage);
        intent.putExtra(Keys.DialogTitle.toString(), dialogTitle);
        Log.i("pID", this.packageID);
        startActivityForResult(intent, 100);
    }
	
	
}
