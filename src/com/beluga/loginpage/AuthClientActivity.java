package com.beluga.loginpage;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beluga.belugakeys.Keys;
import com.beluga.loginpage.datacontrol.InformationProcess;
import com.beluga.loginpage.datacontrol.UsedString;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
//import com.facebook.login.LoginManager;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.tendcloud.tenddata.TalkingDataGA;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.beluga.R;

/**
 * Created by Leo on 2015/10/5.
 */
public class AuthClientActivity extends Activity implements OnClickListener,
		TextWatcher, ConnectionCallbacks,OnConnectionFailedListener{
	
	private static final int RC_SIGN_IN = 0;
	private static final float CONSTANT_INCHES = 7;
	
	
    //密碼元件
    private EditText inputpassword;
    //帳號元件
    private EditText inputaccount;
    //private Button quickSignUpBtn;
    //private Button signUpBtn;
    //private Button modPwdBtn;
    private LoginButton fbLoginButton;
    private Button loginBtn;
    private ImageView logoView;
    //private Button menuFabBtn;
    private Button pwdShowableBtn;
    private View mMenuLayout;
    
    private ImageButton menuFabBtn, quickSignUpBtn, signUpBtn, 
    					modPwdBtn, signinButton;  
    
    private Boolean isSelectedFabBtn = false;

    //Edit控制
    private int EditEnd;
    private int EditTextAccountMax = 16;
    private int EditTextPassMax = 16;
    private int img_GameLogo;
    private byte[] GameLogoForByteArray;

    //存在資料庫的帳密
    private String saveacc = "";
    private String savepwd = "";

    //Server control correlation object declare
    private AuthHttpClient authhttpclient;
    private final static String TAG = "AuthClient";
    private boolean inMaintain;
    private String dialogTitle;
    private String dialogMessage;
    
    //Facebook correlation object declare
    private boolean pressFbButton = false;
    private String fbId;
    private FacebookInfoManager fbInfoManager;
    
    // Google client to communicate with Google
 	private GoogleApiClient mGoogleApiClient;
 	
	//private SignInButton signinButton;
	private String gId;
	private String gname;
	private String gmail;
	private String gPhotoUrl;
	private int cancelLogin = -1;
	private GoogleSignInResult result;
	
	//TalkingData Game Analytics variables
	private String analytic_APP_ID = null;
    private String analytic_Channel_ID = null; //custom ID
    
    private TextView loginPageTextView, fabBtnLabel;
    private LinearLayout loginPageLinearLayout;
    
    //Animation declare
    private Animation rotateShow, rotateHide,
    				  scaleShow, scaleHide, scaleShowForFabBtn,  scaleHideForFabBtn,
    				  translateShow;
    
    
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
    protected void onStart() {
        super.onStart();
        Log.i("AuthAct", "onStart ...");
        setButtonEnable(true);
        mGoogleApiClient.connect();
        
        
        if(this.isSelectedFabBtn == true){
        	//menu still open, so input account, password and login button set enabled false 
        	this.inputaccount.setEnabled(false);
    		this.inputpassword.setEnabled(false);
    		this.loginBtn.setEnabled(false);
    		
        	hideMenu();
        } 
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
    }
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Facebook Initialize
        FacebookSdk.sdkInitialize(getApplicationContext()); 
        
        
        Point point = new Point();  
        getWindowManager().getDefaultDisplay().getRealSize(point);  
        DisplayMetrics dm = getResources().getDisplayMetrics();  
        double x = Math.pow(point.x/ dm.xdpi, 2);  
        double y = Math.pow(point.y / dm.ydpi, 2);  
        double screenInches = Math.sqrt(x + y);  
        Log.d(TAG, "Screen inches : " + screenInches); 
        

        
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        	//this.scaleShowForFabBtn = AnimationUtils.loadAnimation(this, R.anim.anim_scale__landscape_show);
            
            if(screenInches > CONSTANT_INCHES){
            	this.setContentView(R.layout.login_page_landscape_large_size);
            }else{
            	this.setContentView(R.layout.login_page_landscape);
            }
            
            
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
           
            if(screenInches > CONSTANT_INCHES){
            	this.setContentView(R.layout.login_page_large_size);
            }else{
            	this.setContentView(R.layout.login_page_v2);
            }
            
        }
        
        
         //get external data 
         GetDataSetting();
        
	     // Configure sign-in to request the user's ID, email address, and basic
	     // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
	     GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
	             .requestEmail()
	             .build();
	  
	     mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).
	     		addOnConnectionFailedListener(this).
	     		addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
		  
        
        // App ID: 在TalkingData Game Analytics创建应用后会得到App ID。 
        // 渠道 ID: 是渠道标识符，可通过不同渠道单独追踪数据。
        TalkingDataGA.init(this, this.analytic_APP_ID, this.analytic_Channel_ID);
        this.scaleShowForFabBtn = AnimationUtils.loadAnimation(this, R.anim.anim_scale_show2);
        this.rotateShow = AnimationUtils.loadAnimation(this, R.anim.anin_rotate_show);
        this.rotateHide = AnimationUtils.loadAnimation(this, R.anim.anim_rotae_hide);
        this.scaleShow = AnimationUtils.loadAnimation(this, R.anim.anim_scale_show);
        this.scaleHide = AnimationUtils.loadAnimation(this, R.anim.anim_scale_hide);
        this.translateShow = AnimationUtils.loadAnimation(this, R.anim.anim_translate_show);
        
        //this.scaleHideForFabBtn = AnimationUtils.loadAnimation(this, R.anim.anim_scale_hide2);
        
        fabBtnLabel = (TextView)this.findViewById(R.id.fab_label); 
        fabBtnLabel.setVisibility(View.INVISIBLE);
      
        
        //floating button menu
        this.menuFabBtn = (ImageButton)this.findViewById(R.id.fab);
        this.menuFabBtn.setOnClickListener(this);
        this.menuFabBtn.setVisibility(View.INVISIBLE);
        //this.menuFabBtn.setAnimation(scaleShow);
        
        this.mMenuLayout = findViewById(R.id.menu_layout);
        //this.belugaMenu = (FloatingActionsMenu) this.findViewById(R.id.beluga_menu);
        
        //Quick register button
        this.quickSignUpBtn = (ImageButton)this.findViewById(R.id.quick_sign_up_btn);
        this.quickSignUpBtn.setOnClickListener(this);
        
        //General register button
        this.signUpBtn = (ImageButton)this.findViewById(R.id.sign_up_btn);
        this.signUpBtn.setOnClickListener(this);
           
        //General login button 
        this.loginBtn = (Button)this.findViewById(R.id.login_btn);
        this.loginBtn.setOnClickListener(this);
        this.loginBtn.setVisibility(View.INVISIBLE);
        //this.loginBtn.startAnimation(scaleShow);
            
        //Facebook login button
        this.fbLoginButton = (LoginButton)this.findViewById(R.id.fblogin_button);
        this.fbLoginButton.setOnClickListener(this);
        
            
        //Modify password button
        this.modPwdBtn = (ImageButton)this.findViewById(R.id.modify_btn);
        Log.i("In login page", "modPwdBtn v is " + this.modPwdBtn);
        this.modPwdBtn.setOnClickListener(this);
              
        //google button 
        
        signinButton = (ImageButton) findViewById(R.id.google_sign_in_button);
		signinButton.setOnClickListener(this);
		
		pwdShowableBtn = (Button)this.findViewById(R.id.pwd_button);
		pwdShowableBtn.setOnClickListener(this);
		pwdShowableBtn.setVisibility(View.INVISIBLE);
		
        //set button enable
		//setButtonEnable(true);
		
		//Game logo image view
        this.logoView = (ImageView)this.findViewById(R.id.advertView);
        this.logoView.setVisibility(View.INVISIBLE);
        //Password input Field
        inputpassword = (EditText)this.findViewById(R.id.loginPwdEditText);
        this.inputpassword.setVisibility(View.INVISIBLE);
        //Account input Field
        inputaccount =  (EditText)this.findViewById(R.id.loginAccEditText);
        this.inputaccount.setVisibility(View.INVISIBLE);
        
       
   	        //loginPageTextView.setVisibility(View.VISIBLE);
   	    	menuFabBtn.setVisibility(View.VISIBLE);
   	    	inputaccount.setVisibility(View.VISIBLE);
   	    	logoView.setVisibility(View.VISIBLE);
   	    	inputpassword.setVisibility(View.VISIBLE);
   	    	loginBtn.setVisibility(View.VISIBLE);
   	    	logoView.setVisibility(View.VISIBLE);
   	    	pwdShowableBtn.setVisibility(View.VISIBLE);
   	    	fabBtnLabel.setVisibility(View.VISIBLE);
   	    	
   	    	//loginPageTextView.startAnimation(translateShow);
   	    	fabBtnLabel.startAnimation(translateShow);
   	    	menuFabBtn.startAnimation(scaleShow);
   	    	inputaccount.startAnimation(translateShow);
   	    	logoView.startAnimation(translateShow);
   	    	inputpassword.setAnimation(translateShow);
   	    	pwdShowableBtn.setAnimation(translateShow);
   	    	loginBtn.startAnimation(scaleShow);
   	    	logoView.startAnimation(translateShow);
   	 
        
        /*
         * Game Logo setup,
         * if img_GameLogo is 0,
         * set default image
         * else set custom image
         */
        if(this.img_GameLogo == 0 && this.GameLogoForByteArray == null){
            Log.d("login page","img_GameLogo value is"+img_GameLogo);
        }else if(this.img_GameLogo != 0){
        	Log.d("login page","img_GameLogo in else if condition");
            this.logoView.setImageResource(this.img_GameLogo);
        }else{
        	Log.d("login page","img_GameLogo in else condition");
        	Bitmap bmp = BitmapFactory.decodeByteArray(this.GameLogoForByteArray, 0, this.GameLogoForByteArray.length);
        	this.logoView.setImageBitmap(bmp);
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
        
        fbInfoManager = new FacebookInfoManager(this, authhttpclient); 
        
        /*
         * determine facebook whether logout?
         * if getCurrentAccessToken() is null, 
         * facebook button status is logout
         * else, facebook button status is login
         * then auto use faccebook login 
         */
        
        if (AccessToken.getCurrentAccessToken() == null) {
        //if (fbInfoManager.getAccessToken().getCurrentAccessToken() == null) {
        	Log.i("Check fb login status", "already logged out");
        	setButtonEnable(true);
        }else{
        	Log.i("Check fb login status", "already logged in");
        	setButtonEnable(false);
        	//developer Facebook logout setting
        	//LoginManager.getInstance().logOut();
        	//get facebook ID
        	fbId = InformationProcess.getThirdPartyInfo(this);
        	Log.i("Check fb login status", "fbID :"+ fbId);
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
        
        this.GameLogoForByteArray = intent.getByteArrayExtra(Keys.GameLogoForByteArray.toString());
        
        //get boolean assign into inMaintain 
        inMaintain = intent.getBooleanExtra(Keys.ActiveMaintainDialog.toString(), false);
        //get dialog title into  dialogTitle global variable  
        dialogTitle = intent.getStringExtra(Keys.DialogTitle.toString());
        //get dialog message into  dialogMessage global variable
        dialogMessage = intent.getStringExtra(Keys.DialogMessage.toString());
        
        this.analytic_APP_ID = intent.getStringExtra(Keys.AnalyticAppID.toString());
        this.analytic_Channel_ID = intent.getStringExtra(Keys.AnalyticChannelID.toString());
        
        if(this.analytic_APP_ID == null && this.analytic_Channel_ID == null){
        	Log.i(TAG, "Please input analytic_APP_ID and analytic_Channel_ID.");
        }
        
        if(this.GameLogoForByteArray == null){
        	Log.i("TAG", "GameLogo for byte is" + this.GameLogoForByteArray);
        }
        
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
                Log.d(TAG,AuthHttpClient.ApiKey.substring(AuthHttpClient.ApiKey.length() - 4
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
                
                //receive Facebook and google correlation information
                //Implement onProcessDoneEvent() method 
				@Override
				public void onProcessDoneEvent(int Code, String Message, Long uid, String Account, String Pwd,
						String accountBound) {
					// TODO Auto-generated method stub
                    String CodeStr = UsedString.getThirdPartnerLoginStatusString(getApplicationContext(), Code);
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
    private void PressLogin()
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
    private void SaveAccountPassword(String accid,String accpwd)
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
        bundle.putString("type", "LOGIN");
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
    			this.fbInfoManager.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    			
    			if (AccessToken.getCurrentAccessToken() == null) {
    		        //if (fbInfoManager.getAccessToken().getCurrentAccessToken() == null) {
    		        	Log.i("Check fb login status", "already logged out");
    		        	setButtonEnable(true);
    		     }else{
    		    	 setButtonEnable(false);
    		     }	
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
        } else if(requestCode == RC_SIGN_IN){
        	if(resultCode != RESULT_CANCELED){
        		result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           	 	Log.i(TAG, "Result status in if:"+result.getStatus().toString());
           	    this.cancelLogin = resultCode;
           	    Log.i(TAG, "Result code in if status: "+ resultCode);
                handleSignInResult(result);
        	}else{
        		Log.i("Auth ", "is RESULT_CANCELED");
        		Log.i(TAG, "Result code in else status: "+ resultCode);
        		this.cancelLogin = resultCode;
        	}
            
        }else{
        	Log.i("Auth ", "else set SetAccountTextFromSave()");
            SetAccountTextFromSave();
        }
        Log.i("Auth ", "end...");
    }
    
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        Log.i(TAG, "gResult status: "+result.getStatus().toString());
        if (result.isSuccess()) {
        	Log.d(TAG, "handleSignInResult: in if condition." );
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            this.gId = acct.getId();
            this.gmail = acct.getEmail();
            this.gname = acct.getDisplayName();
            this.gPhotoUrl = acct.getPhotoUrl().toString();
            Log.i(TAG, "gid:"+ this.gId + " gmail:"+ this.gmail + " gname:"+ this.gname+
            		"photo URL:"+ this.gPhotoUrl);
            InformationProcess.saveGoogleThirdPartyInfo(acct.getId(), AuthClientActivity.this);
            authhttpclient.Auth_GoogleLoignRegister(acct.getId(), acct.getEmail(),
            		acct.getDisplayName(), acct.getPhotoUrl().toString());
            //this.googleLoginStatus = true;
        } else {
        	Log.d(TAG, "handleSignInResult: in else condition." );
        	Log.d(TAG, "handleSignInResult:" + result.isSuccess()+ "Login failed");
        	//this.googleLoginStatus = false;
        }    
    }
    
	@Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login_btn) {
        	//setButtonEnable(false);
            this.PressLogin();
        } else if (i == R.id.modify_btn) {
        	//setButtonEnable(false);
            Intent Changepasswordintent = new Intent();
            Changepasswordintent.setClass(this, Changepassword.class);
            startActivityForResult(Changepasswordintent, 1);
        } else if (i == R.id.quick_sign_up_btn) {
        	//setButtonEnable(false);
            Intent Fastregistrationintent = new Intent();
            Fastregistrationintent.setClass(this, Fastregistration.class);
            startActivityForResult(Fastregistrationintent, 1);
        } else if (i == R.id.sign_up_btn) {
        	//setButtonEnable(false);
            Intent Registrationintent = new Intent();
            Registrationintent.setClass(this, Registration.class);
            startActivityForResult(Registrationintent, 1);
        }else if (i == R.id.fblogin_button){
        	//setButtonEnable(false);
        	this.pressFbButton = true;
        	//loginFB(fbLoginButton);
        	this.fbInfoManager.loginWithFacebook(fbLoginButton);
        }else if (i == R.id.google_sign_in_button){
        	//setButtonEnable(false);
        	signIn();
        }else if(i == R.id.fab){
        	
        	Log.i("Click", "click fab...");
        	 if (this.isSelectedFabBtn == true) {
        		 Log.i("Click", "click fab hide...");
        		 hideMenu();
             } else {
            	 Log.i("Click", "click fab show...");
                 showMenu();
             } 
             
        }else if(i == R.id.pwd_button){
        	
        	
        	 if (v.isSelected()) {
        		 Log.i("Click", "is hide password...");
        		 pwdShowableBtn.setText(R.string.pwd_SHOW_Text);
        		 SetPasswordShowable(false);
             } else {
            	 Log.i("Click", "is show password...");
            	 pwdShowableBtn.setText(R.string.pwd_HIDE_Text);
            	 SetPasswordShowable(true);
             }
             v.setSelected(!v.isSelected());     
        }
    }
	private void showMenu() {
		
		this.inputaccount.setEnabled(false);
		this.inputpassword.setEnabled(false);
		this.loginBtn.setEnabled(false);
		this.isSelectedFabBtn = true;
		
		fabBtnLabel.startAnimation(scaleHide);
		fabBtnLabel.setVisibility(View.INVISIBLE);
		this.menuFabBtn.startAnimation(rotateShow);
	   	this.signUpBtn.startAnimation(scaleShow);
	   	this.fbLoginButton.startAnimation(scaleShow);
	   	this.signinButton.startAnimation(scaleShow);
	   	this.modPwdBtn.startAnimation(scaleShow);
	   	this.quickSignUpBtn.startAnimation(scaleShow);
        mMenuLayout.setVisibility(View.VISIBLE);
        mMenuLayout.startAnimation(scaleShowForFabBtn);
        
/*
        List<Animator> animList = new ArrayList<>();

        for (int i = 0, len = mArcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(mArcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
*/        
    }
	
	private void hideMenu() {
		/*
        List<Animator> animList = new ArrayList<>();
        
  
        for (int i = mArcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(mArcLayout.getChildAt(i)));
        }

      AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mMenuLayout.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();
        */
        //mMenuLayout.startAnimation(scaleHideForFabBtn);
		this.isSelectedFabBtn = false;
		this.signUpBtn.startAnimation(scaleHide);
   	 	this.fbLoginButton.startAnimation(scaleHide);
   	 	this.signinButton.startAnimation(scaleHide);
   	 	this.modPwdBtn.startAnimation(scaleHide);
   	 	this.quickSignUpBtn.startAnimation(scaleHide);
   	 	
   	
   	 	final Handler handler = new Handler();
   	 	handler.postDelayed(new Runnable() {
   	     @Override
   	     public void run() {
   	         // Do something after 700ms
   	    	//unlock can't edit and click
       		 inputaccount.setEnabled(true);
       		 inputpassword.setEnabled(true);
       		 loginBtn.setEnabled(true);
       		 
       		 fabBtnLabel.startAnimation(translateShow);
   	    	 menuFabBtn.startAnimation(rotateHide);
   	    	 mMenuLayout.setVisibility(View.INVISIBLE);
   	     }
   	 	}, 700);
        
        
    }
	
	private void setButtonEnable(Boolean enabled){
		quickSignUpBtn.setEnabled(enabled);
		signUpBtn.setEnabled(enabled);
		loginBtn.setEnabled(enabled);
		fbLoginButton.setEnabled(enabled);
		modPwdBtn.setEnabled(enabled);
		signinButton.setEnabled(enabled);
	}
    
    private void signIn() {
    	 Log.d(TAG, "Signin start ....");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(TAG, "Signin end ....");
    }
   
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Log.i("google info", "info :"+InformationProcess.getGoogleThirdPartyInfo(this));
		if(InformationProcess.getGoogleThirdPartyInfo(this).equals("")){
			Log.i("google info", "not google info, Please Login google account");
		}else{
			if(this.cancelLogin != RESULT_CANCELED){
				setButtonEnable(false);
				signIn();
			}else{
				Log.i("google login status", "canccel login");
			}
		} 
	}
	
	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		mGoogleApiClient.connect();
	}

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
   {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) 
        {
        	Log.i("Back Pressed", "Back Pressed...");
    		if(this.isSelectedFabBtn == true){
    			Log.i("onBackPressed", "isSelectedFabBtn :"+isSelectedFabBtn );
            	//menu still open, so input account, password and login button set enabled false 
            	this.inputaccount.setEnabled(false);
        		this.inputpassword.setEnabled(false);
        		this.loginBtn.setEnabled(false);
        		
            	hideMenu();
            }else{
            	Log.i("onBackPressed", "isSelectedFabBtn :"+isSelectedFabBtn );
            	finish();
            }
            return false; 
        }
        return super.onKeyDown(keyCode, event);
   }
}