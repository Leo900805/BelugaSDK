package com.beluga.loginpage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.beluga.belugakeys.Keys;
import com.beluga.loginpage.AuthHttpClient.OnAuthEventListener;
import com.beluga.loginpage.datacontrol.Saveaccountandpassword;
import com.beluga.loginpage.datacontrol.UsedString;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import com.beluga.R;


/**
 * Created by Leo on 2015/10/5.
 */
public class AuthClientActivity extends Activity implements OnClickListener,TextWatcher{

    //密碼元件
    public EditText inputpassword;
    //帳號元件
    public EditText inputaccount;
    public Button quickSignUpBtn;
    public Button signUpBtn;
    public Button modPwdBtn;
    public LoginButton fbLoginButton;
    public Button loginBtn;
    public ImageView logoView;

    //Edit控制
    int EditEnd;
    int EditTextAccountMax = 16;
    int EditTextPassMax = 16;
    private int img_GameLogo;

    //存在資料庫的帳密
    String saveacc = "";
    String savepwd = "";

    //Server控制
    AuthHttpClient authhttpclient;
    private final static String TAG = "AuthClient";
    int mMorphCounter1 = 1;
    boolean inMaintain;
    String dialogTitle;
    String dialogMessage;
    
    CallbackManager callbackManager;
    private AccessToken accessToken;
    boolean pressFbButton = false;
    private String fbId;
    private String fbName;
    private String fbEmail;
    
    
    SharedPreferences settings = getSharedPreferences("setting", 0);
    SharedPreferences.Editor editor = settings.edit();
    
    @Override
    protected void onResume() {
      super.onResume();

      // Logs 'install' and 'app activate' App Events.
      AppEventsLogger.activateApp(this);
    }
    
    @Override
    protected void onPause() {
      super.onPause();

      // Logs 'app deactivate' App Event.
      AppEventsLogger.deactivateApp(this);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); 
        this.setContentView(R.layout.login_page);
       
        AppLinkData.fetchDeferredAppLinkData(this, 
        		  new AppLinkData.CompletionHandler() {
        		     @Override
        		     public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
        		         // Process app link data
        		    	 Log.i("deef link", "...");
        		     }
        		 }
        		);
        
        GetDataSetting();

        this.quickSignUpBtn = (Button)this.findViewById(R.id.quick_sign_up_btn);
        Log.i("In login page", "quickSignUpBtn v is " + quickSignUpBtn);
        this.quickSignUpBtn.setOnClickListener(this);

        this.signUpBtn = (Button)this.findViewById(R.id.sign_up_btn);
        //this.signUpBtn = (MorphingButton)this.findViewById(R.id.sign_up_btn);
        Log.i("In login page", "SignUpBtn v is " + this.signUpBtn);
        this.signUpBtn.setOnClickListener(this);


        //R.drawable.cbimage
        this.loginBtn = (Button)this.findViewById(R.id.login_btn);
        Log.i("In login page", "loginBtn v is " + this.loginBtn);
        this.loginBtn.setOnClickListener(this);
        
        this.fbLoginButton = (LoginButton)this.findViewById(R.id.fblogin_button);
        Log.i("In login page", "fbloginBtn v is " + this.fbLoginButton);
        this.fbLoginButton.setOnClickListener(this);


        this.modPwdBtn = (Button)this.findViewById(R.id.modify_btn);
        Log.i("In login page", "modPwdBtn v is " + this.modPwdBtn);
        this.modPwdBtn.setOnClickListener(this);


        inputpassword = (EditText)this.findViewById(R.id.loginPwdEditText);
        inputaccount =  (EditText)this.findViewById(R.id.loginAccEditText);
        this.logoView = (ImageView)this.findViewById(R.id.advertView);
        //this.logoView.setImageResource(R.drawable.cbimage);
        if(this.img_GameLogo == 0){
            Log.d("login page","img_GameLogo value is"+img_GameLogo);
        }else{
            this.logoView.setImageResource(this.img_GameLogo);
        }

        if(this.inMaintain == true){
            Log.i("onCreate","in maintain");
            showDialog();
        }else{
            CreateHttpClient(); //設定http連接物件
            SetDefaultText(); //設定text box預設值
        }
        
        if (AccessToken.getCurrentAccessToken() == null) {
        	Log.i("Check fb login status", "already logged out");
        }else{
        	Log.i("Check fb login status", "already logged in");
        	LoginManager.getInstance().logOut();
        	//this.loginFB(fbLoginButton);
        	fbId = settings.getString("fbId", "");
        	Log.i("Check fb login status", "got fb id on settings"+ settings.getString("fbId", "") );
        	authhttpclient.Auth_FacebookLoignRegister(fbId);
        	Log.i("Check fb login status", "check end");
        }
        
        
        
    }
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);
        builder.setPositiveButton(R.string.Confirm_Button_Text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    private void GetDataSetting(){
//		String APIUrl = "http://23.102.255.253:9000/api/";
        String APIUrl = "http://api.belugame.com/api/";
        Log.d("tag", "versionCode :" + AuthHttpClient.version);
        //讀取外部參數
        Intent intent = getIntent();
        AuthHttpClient.ApiUrl = APIUrl;
        AuthHttpClient.AppID = intent.getStringExtra(Keys.AppID.toString());
        AuthHttpClient.ApiKey = intent.getStringExtra(Keys.ApiKey.toString());
        AuthHttpClient.PackageID = intent.getStringExtra(Keys.PackageID.toString());

        //AuthHttpClient.ApiUrl = APIUrl;
        //AuthHttpClient.AppID = "herinv";
        //AuthHttpClient.ApiKey = "c4a980bfa03286646f173ae63a0b0b6d";
        //AuthHttpClient.PackageID = "Deno";
        this.img_GameLogo = intent.getIntExtra(Keys.GameLogo.toString(),0);
        Log.i("Login page", "img_GameLogo value is:" + img_GameLogo);
        inMaintain = intent.getBooleanExtra(Keys.ActiveMaintainDialog.toString(), false);
        dialogTitle = intent.getStringExtra(Keys.DialogTitle.toString());
        dialogMessage = intent.getStringExtra(Keys.DialogMessage.toString());



        if(AuthHttpClient.AppID == null || AuthHttpClient.ApiKey == null || AuthHttpClient.PackageID == null){
            Toast.makeText(this, "params error", Toast.LENGTH_LONG).show();
            Log.e(TAG,"appid is "+AuthHttpClient.AppID + " .");
            Log.e(TAG,"apikey is "+AuthHttpClient.ApiKey + " .");
            Log.e(TAG,"packageid is "+AuthHttpClient.PackageID + " .");
        }
        Log.d(TAG, " APPID: " + AuthHttpClient.AppID);
        if(AuthHttpClient.ApiKey != null){
            if(AuthHttpClient.ApiKey.compareTo("") != 0){
                Log.d(TAG,AuthHttpClient.ApiKey
                        .substring(AuthHttpClient.ApiKey.length() - 4
                                , AuthHttpClient.ApiKey.length()));
            }else{
                Log.e(TAG, " ApiKey is empty ");
            }
        }
    }
    
    private void CreateHttpClient()
    {
        //建立監聽事件
        //網路處理_自已寫的類別 __手術用    "呼叫點 按下按鈕呼叫到"
        authhttpclient = new AuthHttpClient(this);
        
        	//接收到網路回來資料  ----- "呼叫監聽事件放的地方"
            //當按下按鈕時  呼叫到它   "SERVER傳回資料時會呼叫到它---------"
            //接SERVER回傳來的資料 處理它
            authhttpclient.AuthEventListener(new AuthHttpClient.OnAuthEventListener() {
                public void onProcessDoneEvent(int Code, String Message, Long uid, String Account, String token) {
                    //ok=false;
                    String CodeStr = UsedString.getLoginstring(getApplicationContext(), Code);
                    if (CodeStr.compareTo("") == 0) {
                        //Looper.prepare();
                        Toast.makeText(AuthClientActivity.this, Message, Toast.LENGTH_SHORT).show();
                        //Looper.loop();
                    } else if (Code == 1) {
                        //onMorphButton1Clicked(loginBtn);
                        Toast.makeText(AuthClientActivity.this, CodeStr, Toast.LENGTH_LONG).show();
                        SaveAccountPassword(inputaccount.getText().toString(), inputpassword.getText().toString());
                        Saveaccountandpassword.saveUserUid(Long.toString(uid), AuthClientActivity.this);
                        SetFinish(inputaccount.getText().toString(), uid.toString(), token, inputpassword.getText().toString());
                    } else {
                        Toast.makeText(AuthClientActivity.this, CodeStr, Toast.LENGTH_LONG).show();
                    }
                }

				@Override
				public void onProcessDoneEvent(int Code, String Message, Long uid, String Account, String Pwd,
						String accountBound) {
					// TODO Auto-generated method stub
					//ok=false;
                    String CodeStr = UsedString.getLoginstring(getApplicationContext(), Code);
                    if (CodeStr.compareTo("") == 0) {
                        //Looper.prepare();
                        Toast.makeText(AuthClientActivity.this, Message, Toast.LENGTH_SHORT).show();
                        //Looper.loop();
                    } else if (Code == 1) {
                    	Log.i("info", "Uid:"+ uid +", Account:"+ Account);
                        Toast.makeText(AuthClientActivity.this, CodeStr, Toast.LENGTH_LONG).show();
                        SaveAccountPassword(Account, Pwd);
                        Saveaccountandpassword.saveUserUid(Long.toString(uid), AuthClientActivity.this);
                        SetFinish(Account, uid.toString(), accountBound, Pwd);
                    } else {
                        Toast.makeText(AuthClientActivity.this, CodeStr, Toast.LENGTH_LONG).show();
                    }
					
				}
            });
    }
    //按下登入鈕呼叫的地方
    public void PressLogin()
    {
        String accid = inputaccount.getText().toString();
        String accpwd = inputpassword.getText().toString();
    	/* Changed by Leo Ling */
    	/*
    	  if(accid.equals("請輸入帳號"))
		{	Toast.makeText(AuthClientActivity.this, "請輸入您的帳號", Toast.LENGTH_LONG).show();return;}
    	if(accpwd.equals("請輸入密碼"))
		{	Toast.makeText(AuthClientActivity.this, "請輸入您的密碼", Toast.LENGTH_LONG).show();return;}
    	 */
        if(accid.equals(this.getString(R.string.Enter_Ac_Type))){
            Toast.makeText(AuthClientActivity.this,
                    this.getString(R.string.Enter_Ac_Type),
                    Toast.LENGTH_LONG).show();
            return;
        }
        if(accpwd.equals(this.getString(R.string.Enter_Pwd_Type)))
        {
            Toast.makeText(AuthClientActivity.this,
                    this.getString(R.string.Enter_Pwd_Type),
                    Toast.LENGTH_LONG).show();
            return;
        }
     	/* Changed by Leo Ling end */
        //傳送資料到SERVER 帳號/密碼
        authhttpclient.Auth_UserLogin(accid, accpwd);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        View focView = AuthClientActivity.this.getCurrentFocus();
        /* if t use EditText.settxt to change text  and the user has no
         * CurrentFocus  the focView will be null
         */
        if(focView != null){
            int i = focView.getId();
            if (i == R.id.loginAccEditText) {
                EditEnd = inputaccount.getSelectionEnd();

                //remove textlistener to do setting or it will carsh
                inputaccount.removeTextChangedListener(this);
                Editable thistxt = inputaccount.getText();
                if (thistxt.length() > EditTextAccountMax) {
                    CharSequence thisvalue = thistxt.subSequence(0, EditTextAccountMax);
                    inputaccount.setText(thisvalue);
                    inputaccount.setSelection(EditTextAccountMax);
                } else {
                    inputaccount.setSelection(EditEnd);
                }

                //add textlistenerback after setting
                inputaccount.addTextChangedListener(this);

            } else if (i == R.id.loginPwdEditText) {
                EditEnd = inputpassword.getSelectionEnd();

                //remove textlistener to do setting or it will carsh
                inputpassword.removeTextChangedListener(this);
                Editable pwdtxt = inputpassword.getText();
                if (pwdtxt.length() > EditTextPassMax) {
                    CharSequence thisvalue = pwdtxt.subSequence(0, EditTextPassMax);
                    inputpassword.setText(thisvalue);
                    inputpassword.setSelection(EditTextPassMax);
                } else {
                    inputpassword.setSelection(EditEnd);
                }

                //add textlistenerback after setting
                inputpassword.addTextChangedListener(this);

            } else {
            }

        }
    }
    private void SetPasswordShowable(boolean show) {
        if(show){
            inputpassword.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());// 設置密碼為可見
        }else{
            inputpassword.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());// 設置密碼為不可見
        }
    }

    private void GetAccountAndPasswordFromData(){
        saveacc = Saveaccountandpassword.GetaccountString(this);
        savepwd = Saveaccountandpassword.GetpasswordString(this);
    }

    //存成功的帳密
    public void SaveAccountPassword(String accid,String accpwd)
    {
        if(accid.length() > 0 && accpwd.length() > 0)
        {
            Saveaccountandpassword.saveaccountpassword(accid, accpwd, this);
        }
    }
    private void SetAccountTextFromSave()
    {
        GetAccountAndPasswordFromData();
        try
        {
            EditText inputacc = (EditText)this.findViewById(R.id.loginAccEditText);
            inputacc.setText(saveacc);
            EditText inputpwd = (EditText)this.findViewById(R.id.loginPwdEditText);
            inputpwd.setText(savepwd);
        }
        catch(Exception ex)
        {

        }
    }

    private void SetDefaultText()
    {
        GetAccountAndPasswordFromData();
        //帳號變成不可以看
        //第一次安裝呈現請輸入/帳/密文字
        if(saveacc.length() > 0 && savepwd.length() > 0)
        {
            //呼叫成功的 帳/密 設定到EditText
            inputaccount.setText(saveacc);
            inputpassword.setText(savepwd);
            SetPasswordShowable(false);
        }
    }
    //回到遊戲 並回傳帳號與uid
    private void SetFinish(String thisuserid,String thisuid,String token,String thispwd)
    {
        Intent resultdata = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("userid", thisuserid);
        bundle.putString("uid", thisuid);
        bundle.putString("pwd", thispwd);
        bundle.putString("token", token);
        resultdata.putExtras(bundle);
        setResult(Activity.RESULT_OK, resultdata); //回傳RESULT_OK

        Handler handler = new Handler();
        Runnable delayRunnable =  new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                finish();
            }
        };
        handler.postDelayed(delayRunnable, 700);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        /* Developer by Leo Ling   Facebook login */
        
    		if(this.pressFbButton == true){
    			this.pressFbButton = false;
    			callbackManager.onActivityResult(requestCode, resultCode, data);
    		}
    	
    	/* Developer by Leo Ling   Facebook login end */

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            try
            {	//將Activity的資料收集傳送到第二個Activity
                int ResultType = bundle.getInt("ResultType");
                if(ResultType == 1)
                {
                    //do user login
                    String userid = bundle.getString("userid");
                    String userpwd = bundle.getString("userpwd");
                    EditText inputacc = (EditText)this.findViewById(R.id.loginAccEditText);
                    inputacc.setText(userid);
                    EditText inputpwd = (EditText)this.findViewById(R.id.loginPwdEditText);
                    inputpwd.setText(userpwd);
                    SaveAccountPassword(userid,userpwd);
                    authhttpclient.Auth_UserLogin(userid,userpwd);//登入錯誤畫面
                    return;
                }
                if(ResultType == 2){
                    //do user login
                    String userid = bundle.getString("userid");
                    String userpwd = bundle.getString("userpwd");
                    EditText inputacc = (EditText)this.findViewById(R.id.loginAccEditText);
                    inputacc.setText(userid);
                    EditText inputpwd = (EditText)this.findViewById(R.id.loginPwdEditText);
                    inputpwd.setText(userpwd);
                    SaveAccountPassword(userid,userpwd);
                    return;
                }
                SetAccountTextFromSave();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        } else {
            SetAccountTextFromSave();
            //callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login_btn) {
            this.PressLogin();
        } else if (i == R.id.modify_btn) {

            Intent Changepasswordintent = new Intent();
            Changepasswordintent.setClass(this, Changepassword.class);
            startActivityForResult(Changepasswordintent, 1);

        } else if (i == R.id.quick_sign_up_btn) {

            Intent Fastregistrationintent = new Intent();
            Fastregistrationintent.setClass(this, Fastregistration.class);
            startActivityForResult(Fastregistrationintent, 1);

        } else if (i == R.id.sign_up_btn) {

            Intent Registrationintent = new Intent();
            Registrationintent.setClass(this, Registration.class);
            startActivityForResult(Registrationintent, 1);
        }else if (i == R.id.fblogin_button){
        	
        	this.pressFbButton = true;
        	loginFB(fbLoginButton);
        }
    }
    
    /* Developer by Leo Ling   Facebook login */
   	public void loginFB(LoginButton loginButton){
   	//public void loginFB(Button loginButton){
   		 
   		 //宣告callback Manager
          callbackManager = CallbackManager.Factory.create();
          loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
              //登入成功
              @Override
              public void onSuccess(LoginResult loginResult) {
                  //accessToken之後或許還會用到 先存起來
            	  
                  accessToken = loginResult.getAccessToken();
                  Log.d("FB","access token got.");
                  
                  //send request and call graph api
                  GraphRequest request = GraphRequest.newMeRequest(accessToken, 
                          new GraphRequest.GraphJSONObjectCallback() {
                             //當RESPONSE回來的時候
   							@Override
   							public void onCompleted(JSONObject object, GraphResponse response) {
   								// TODO Auto-generated method stub
   								//讀出姓名 ID FB個人頁面連結
   	                            Log.d("FB","complete");
   	                            fbId = object.optString("id");
   	                            editor.putString("fbId", fbId);
   	                            Log.d("FB",fbId);
   	                            authhttpclient.Auth_FacebookLoignRegister(fbId);        
   							}
                     });
                  //包入你想要得到的資料 送出request
                  Bundle parameters = new Bundle();
                  //parameters.putString("fields", "id,name,link,email");
                  parameters.putString("fields", "id");
                  request.setParameters(parameters);
                  request.executeAsync();
              }

              //登入取消
              @Override
              public void onCancel() {
                  // App code
                  Log.d("FB","CANCEL");
              }

              //登入失敗
   		@Override
   		public void onError(FacebookException error) {
   			// TODO Auto-generated method stub
   			Log.d("FB",error.toString());
   		}
   		
          });
   	}
   	/* Developer by Leo Ling   Facebook login end */

}