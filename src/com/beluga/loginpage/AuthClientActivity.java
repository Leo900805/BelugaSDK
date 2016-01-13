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
import com.beluga.loginpage.datacontrol.Saveaccountandpassword;
import com.beluga.loginpage.datacontrol.UsedString;
import com.beluga.R;

/**
 * Created by Leo on 2015/10/5.
 */
public class AuthClientActivity extends Activity implements OnClickListener,TextWatcher{

    //撖Ⅳ�辣
    public EditText inputpassword;
    //撣唾��辣
    public EditText inputaccount;
    public Button quickSignUpBtn;
    public Button signUpBtn;
    public Button modPwdBtn;
    public Button loginBtn;
    //public MorphingButton loginBtn;
    public ImageView logoView;

    //Edit�批
    int EditEnd;
    int EditTextAccountMax = 16;
    int EditTextPassMax = 16;
    private int img_GameLogo;

    //摮鞈�摨怎�撣喳�
    String saveacc = "";
    String savepwd = "";

    //Server�批
    AuthHttpClient authhttpclient;
    private final static String TAG = "AuthClient";
    int mMorphCounter1 = 1;
    boolean inMaintain;
    String dialogTitle;
    String dialogMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.login_page);
        GetDataSetting();

        this.quickSignUpBtn = (Button)this.findViewById(R.id.quick_sign_up_btn);
        Log.i("In login page", "quickSignUpBtn v is " + quickSignUpBtn);
        this.quickSignUpBtn.setOnClickListener(this);

        this.signUpBtn = (Button)this.findViewById(R.id.sign_up_btn);
        //this.signUpBtn = (MorphingButton)this.findViewById(R.id.sign_up_btn);
        Log.i("In login page", "SignUpBtn v is " + this.signUpBtn);
        this.signUpBtn.setOnClickListener(this);


        //R.drawable.cbimage
        //this.loginBtn = (MorphingButton)this.findViewById(R.id.login_btn);
        this.loginBtn = (Button)this.findViewById(R.id.login_btn);
        Log.i("In login page", "loginBtn v is " + this.loginBtn);
        this.loginBtn.setOnClickListener(this);


        this.modPwdBtn = (Button)this.findViewById(R.id.modify_btn);
        Log.i("In login page", "modPwdBtn v is " + this.modPwdBtn);
        this.modPwdBtn.setOnClickListener(this);

        //this.morphToSquare(loginBtn, 0);
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
            CreateHttpClient(); //閮剖�http���拐辣
            SetDefaultText(); //閮剖�text box�身��
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
        //霈��憭�
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
        //撱箇���鈭辣
        //蝬脰楝��_�芸歇撖怎�憿 __����   "�澆暺������澆��
        authhttpclient = new AuthHttpClient(this);
        //�交�啁雯頝臬�靘��� ----- "�澆��鈭辣�曄��唳"
        //�嗆�銝���  �澆�啣�   "SERVER�喳�鞈����澆�啣�---------"
        //�兄ERVER�靘�鞈� ��摰�
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
        });
    }
    //���餃��怎��唳
    public void PressLogin()
    {
        String accid = inputaccount.getText().toString();
        String accpwd = inputpassword.getText().toString();
    	/* Changed by Leo Ling */
    	/*
    	  if(accid.equals("隢撓�亙董��))
		{	Toast.makeText(AuthClientActivity.this, "隢撓�交�董��, Toast.LENGTH_LONG).show();return;}
    	if(accpwd.equals("隢撓�亙�蝣�))
		{	Toast.makeText(AuthClientActivity.this, "隢撓�交��蝣�, Toast.LENGTH_LONG).show();return;}
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
        //�喲�鞈��訕ERVER 撣唾�/撖Ⅳ
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
                    .getInstance());// 閮剔蔭撖Ⅳ�箏閬�
        }else{
            inputpassword.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());// 閮剔蔭撖Ⅳ�箔��航�
        }
    }

    private void GetAccountAndPasswordFromData(){
        saveacc = Saveaccountandpassword.GetaccountString(this);
        savepwd = Saveaccountandpassword.GetpasswordString(this);
    }

    //摮���撣喳�
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
        //撣唾�霈�銝隞亦�
        //蝚砌�甈∪�鋆��曇�頛詨/撣�撖�摮�
        if(saveacc.length() > 0 && savepwd.length() > 0)
        {
            //�澆����撣�撖�閮剖��蚩ditText
            inputaccount.setText(saveacc);
            inputpassword.setText(savepwd);
            SetPasswordShowable(false);
        }
    }
    //�� 銝血��喳董��uid
    private void SetFinish(String thisuserid,String thisuid,String token,String thispwd)
    {
        Intent resultdata = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("userid", thisuserid);
        bundle.putString("uid", thisuid);
        bundle.putString("pwd", thispwd);
        resultdata.putExtras(bundle);
        //onMorphButton1Clicked(loginBtn);
        setResult(Activity.RESULT_OK, resultdata); //�RESULT_OK

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
        /* Developer by Leo Ling   Facebook login */
    	/*
    		if(this.pressFbButton == true){
    			this.pressFbButton = false;
    			callbackManager.onActivityResult(requestCode, resultCode, data);
    		}
    	*/
    	/* Developer by Leo Ling   Facebook login end */

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            try
            {	//撠ctivity�����蝚砌��ctivity
                Bundle bundle = data.getExtras();
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
                    authhttpclient.Auth_UserLogin(userid,userpwd);//�餃�航炊�恍
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
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        else
        {
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

		/* Developer by Leo Ling   Facebook login */
		/*
		case 0000: //fb login
			LoginButton loginButton = (LoginButton)this.findViewById(0000);
			//Button loginButton = (Button)this.findViewById(0000);
			loginFB(loginButton);
			this.pressFbButton = true;
			break;
		*/
		/* Developer by Leo Ling   Facebook login end */
        }
    }

}
