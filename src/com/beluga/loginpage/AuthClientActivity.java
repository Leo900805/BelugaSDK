package com.beluga.loginpage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.beluga.loginpage.datacontrol.InformationProcess;
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
//import com.facebook.login.LoginManager;
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

    //Server control correlation object declare
    AuthHttpClient authhttpclient;
    private final static String TAG = "AuthClient";
    boolean inMaintain;
    String dialogTitle;
    String dialogMessage;
    
    //Facebook correlation object declare
    CallbackManager callbackManager;
    private AccessToken accessToken;
    boolean pressFbButton = false;
    private String fbId;
    
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
        //Facebook Initialize
        FacebookSdk.sdkInitialize(getApplicationContext()); 
        this.setContentView(R.layout.login_page);
        
        //deef link...
        AppLinkData.fetchDeferredAppLinkData(this, 
        		  new AppLinkData.CompletionHandler() {
        		     @Override
        		     public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
        		         // Process app link data
        		    	 Log.i("deef link", "...");
        		     }
        		 }
        		);
        
        //get external data 
        GetDataSetting();
        
        //Quick register button
        this.quickSignUpBtn = (Button)this.findViewById(R.id.quick_sign_up_btn);
        Log.i("In login page", "quickSignUpBtn v is " + quickSignUpBtn);
        this.quickSignUpBtn.setOnClickListener(this);
        
        //General register button
        this.signUpBtn = (Button)this.findViewById(R.id.sign_up_btn);
        Log.i("In login page", "SignUpBtn v is " + this.signUpBtn);
        this.signUpBtn.setOnClickListener(this);
        
        //General login button 
        this.loginBtn = (Button)this.findViewById(R.id.login_btn);
        Log.i("In login page", "loginBtn v is " + this.loginBtn);
        this.loginBtn.setOnClickListener(this);
        
        //Facebook login button
        this.fbLoginButton = (LoginButton)this.findViewById(R.id.fblogin_button);
        Log.i("In login page", "fbloginBtn v is " + this.fbLoginButton);
        this.fbLoginButton.setOnClickListener(this);
        
        //Modify password button
        this.modPwdBtn = (Button)this.findViewById(R.id.modify_btn);
        Log.i("In login page", "modPwdBtn v is " + this.modPwdBtn);
        this.modPwdBtn.setOnClickListener(this);
        
        //Password input Field
        inputpassword = (EditText)this.findViewById(R.id.loginPwdEditText);
        //Account input Field
        inputaccount =  (EditText)this.findViewById(R.id.loginAccEditText);
        //Game logo image view
        this.logoView = (ImageView)this.findViewById(R.id.advertView);
        
        /*
         * Game Logo setup,
         * if img_GameLogo is 0,
         * set default image
         * else set custom image
         */
        if(this.img_GameLogo == 0){
            Log.d("login page","img_GameLogo value is"+img_GameLogo);
        }else{
            this.logoView.setImageResource(this.img_GameLogo);
        }
        
        /*
         * Maintain setup
         * inMaintain is true then,
         * active maintain show maintain dialog
         * else execute CreateHttpClient(); method
         * and set default text to text fields
         */
        if(this.inMaintain == true){
            Log.i("onCreate","in maintain");
            showDialog();
        }else{
            CreateHttpClient(); //設定http連接物件
            SetDefaultText(); //設定text box預設值
        }
        
        /*
         * determine facebook whether logout?
         * if getCurrentAccessToken() is null, 
         * facebook button status is logout
         * else, facebook button status is login
         * then auto use faccebook login 
         */
        if (AccessToken.getCurrentAccessToken() == null) {
        	Log.i("Check fb login status", "already logged out");
        }else{
        	Log.i("Check fb login status", "already logged in");
        	//developer Facebook logout setting
        	//LoginManager.getInstance().logOut();
        	//get facebook ID
        	fbId = InformationProcess.getThirdPartyInfo(this);
        	//auto login
        	authhttpclient.Auth_FacebookLoignRegister(fbId);
        }    
    }
    
    //Maintain Dialog show method
    private void showDialog(){
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set dialog title
        builder.setTitle(dialogTitle);
        //set dialog content message
        builder.setMessage(dialogMessage);
        //set confirm button in dialog and set button click event
        builder.setPositiveButton(R.string.Confirm_Button_Text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }
    
    //get external data method 
    private void GetDataSetting(){
        String APIUrl = "http://api.belugame.com/api/";
        Log.d("tag", "versionCode :" + AuthHttpClient.version);
        //讀取外部參數
        Intent intent = getIntent();
        //assign Api Url into AuthHttpClient.ApiUrl
        AuthHttpClient.ApiUrl = APIUrl;
        //get App ID assign into AuthHttpClient.AppID
        AuthHttpClient.AppID = intent.getStringExtra(Keys.AppID.toString());
        //get App Key assign into AuthHttpClient.ApiKey
        AuthHttpClient.ApiKey = intent.getStringExtra(Keys.ApiKey.toString());
        //get Package ID assign into AuthHttpClient.PackageID
        AuthHttpClient.PackageID = intent.getStringExtra(Keys.PackageID.toString());
        
        //get Game logo assign into img_GameLogo global variable 
        this.img_GameLogo = intent.getIntExtra(Keys.GameLogo.toString(),0);
        Log.i("Login page", "img_GameLogo value is:" + img_GameLogo);
        
        //get boolean assign into inMaintain 
        inMaintain = intent.getBooleanExtra(Keys.ActiveMaintainDialog.toString(), false);
        //get dialog title into  dialogTitle global variable  
        dialogTitle = intent.getStringExtra(Keys.DialogTitle.toString());
        //get dialog message into  dialogMessage global variable
        dialogMessage = intent.getStringExtra(Keys.DialogMessage.toString());
        
        /*
         * AppID, ApiKey and PackageID is null show "params error" string 
         */
        if(AuthHttpClient.AppID == null || AuthHttpClient.ApiKey == null || AuthHttpClient.PackageID == null){
            Toast.makeText(this, R.string.Params_ERROR, Toast.LENGTH_LONG).show();
            Log.e(TAG,"appid is "+AuthHttpClient.AppID + " .");
            Log.e(TAG,"apikey is "+AuthHttpClient.ApiKey + " .");
            Log.e(TAG,"packageid is "+AuthHttpClient.PackageID + " .");
        }
        
        Log.d(TAG, " APPID: " + AuthHttpClient.AppID); 
        /*
         * AuthHttpClient.ApiKey cannot be null 
         */
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
    
    //Create connection method
    private void CreateHttpClient(){
        //建立監聽事件
        //網路處理_自已寫的類別 __手術用    "呼叫點 按下按鈕呼叫到"
        authhttpclient = new AuthHttpClient(this);
        	//接收到網路回來資料  ----- "呼叫監聽事件放的地方"
            //當按下按鈕時  呼叫到它   "SERVER傳回資料時會呼叫到它---------"
            //接SERVER回傳來的資料 處理它
            authhttpclient.AuthEventListener(new AuthHttpClient.OnAuthEventListener() {
            	//receive general correlation information
            	//Implement onProcessDoneEvent() method 
                public void onProcessDoneEvent(int Code, String Message, Long uid, String Account, String token) {
                    String CodeStr = UsedString.getLoginstring(getApplicationContext(), Code);
                    if (CodeStr.compareTo("") == 0) {
                        Toast.makeText(AuthClientActivity.this, Message, Toast.LENGTH_SHORT).show();
                    } else if (Code == 1) {
                        Toast.makeText(AuthClientActivity.this, CodeStr, Toast.LENGTH_LONG).show();
                        SaveAccountPassword(inputaccount.getText().toString(), inputpassword.getText().toString());
                        InformationProcess.saveUserUid(Long.toString(uid), AuthClientActivity.this);
                        SetFinish(inputaccount.getText().toString(), uid.toString(), token, inputpassword.getText().toString());
                    } else {
                        Toast.makeText(AuthClientActivity.this, CodeStr, Toast.LENGTH_LONG).show();
                    }
                }
                
                //receive Facebook correlation information
                //Implement onProcessDoneEvent() method 
				@Override
				public void onProcessDoneEvent(int Code, String Message, Long uid, String Account, String Pwd,
						String accountBound) {
					// TODO Auto-generated method stub
                    String CodeStr = UsedString.getFacebookLoginstring(getApplicationContext(), Code);
                    if (CodeStr.compareTo("") == 0) {
                        Toast.makeText(AuthClientActivity.this, Message, Toast.LENGTH_SHORT).show();
                    } else if (Code == 1) {
                    	Log.i("info", "Uid:"+ uid +", Account:"+ Account);
                        Toast.makeText(AuthClientActivity.this, CodeStr, Toast.LENGTH_LONG).show();
                        InformationProcess.saveUserUid(Long.toString(uid), AuthClientActivity.this);
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
        /* 
         * if t use EditText.settxt to change text  and the user has no
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
        saveacc = InformationProcess.getAccountString(this);
        savepwd = InformationProcess.getPasswordString(this);
    }

    //存成功的帳密
    public void SaveAccountPassword(String accid,String accpwd)
    {
        if(accid.length() > 0 && accpwd.length() > 0)
        {
        	InformationProcess.saveAccountPassword(accid, accpwd, this);
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
    	Log.i("Auth ", "start...");
        super.onActivityResult(requestCode, resultCode, data);
        
        /* Developer by Leo Ling   Facebook login */
    		if(this.pressFbButton == true){
    			this.pressFbButton = false;
    			callbackManager.onActivityResult(requestCode, resultCode, data);
    		}
    	/* Developer by Leo Ling   Facebook login end */
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
        	Log.i("Auth ", "requestCode == 1 && resultCode == Activity.RESULT_OK");
            try
            {	
            	Bundle bundle = data.getExtras();
            	//將Activity的資料收集傳送到第二個Activity
                int ResultType = bundle.getInt("ResultType");
                if(ResultType == 1)
                {
                	Log.i("Auth ", "ResultType == 1");
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
                	Log.i("Auth ", "ResultType == 2");
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
        	Log.i("Auth ", "else set SetAccountTextFromSave()");
            SetAccountTextFromSave();
        }
        Log.i("Auth ", "end...");
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
   	                            Log.d("FB",fbId);
   	                            InformationProcess.saveThirdPartyInfo(fbId, AuthClientActivity.this);
   	                            authhttpclient.Auth_FacebookLoignRegister(fbId);        
   							}
                     });
                  //包入你想要得到的資料 送出request
                  Bundle parameters = new Bundle();
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