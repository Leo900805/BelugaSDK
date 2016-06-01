package com.beluga.loginpage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.beluga.loginpage.AuthHttpClient.OnAuthEventListener;
import com.beluga.loginpage.datacontrol.InformationProcess;
import com.beluga.loginpage.datacontrol.UsedString;
import com.beluga.loginpage.datacontrol.GameBackground;
import com.beluga.R;
/**
 * Created by Leo on 2015/10/5.
 */
public class Registration extends Activity implements OnClickListener {
	
	final static int DELAY_TIME = 650; 
	private static final float CONSTANT_INCHES = 7;

    private EditText inputaccount,inputpassword,inputdeterminepassword;
   
    private AuthHttpClient authhttpclient;
    private ImageButton signUpComfirmBtn, signUpReturnBtn;
    private CheckBox checkBox;
    private Animation selectedMoveLeft, selectedMoveRight,scaleHide;
    private RelativeLayout registerSideRelativeLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        Point point = new Point();  
        getWindowManager().getDefaultDisplay().getRealSize(point);  
        DisplayMetrics dm = getResources().getDisplayMetrics();  
        double x = Math.pow(point.x/ dm.xdpi, 2);  
        double y = Math.pow(point.y / dm.ydpi, 2);  
        double screenInches = Math.sqrt(x + y);  
        Log.d("sign up OnCreate", "Screen inches : " + screenInches); 
        

        if(screenInches > CONSTANT_INCHES){
        	this.setContentView(R.layout.sign_up_large_size);
        }else{
        	this.setContentView(R.layout.sign_up_v2);
        }
        
        this.registerSideRelativeLayout = (RelativeLayout)findViewById(R.id.signup_bg_side);
        this.registerSideRelativeLayout.setBackgroundResource(GameBackground.GAME_BACKGROUND);
        selectedMoveLeft = AnimationUtils.loadAnimation(this, R.anim.anim_selected_move_left_effect);
        selectedMoveRight= AnimationUtils.loadAnimation(this, R.anim.anim_selected_move_right_effect);
        this.scaleHide = AnimationUtils.loadAnimation(this, R.anim.anim_scale_hide);
        inputpassword = (EditText)this.findViewById(R.id.signuppwdeditText);
        inputaccount = (EditText)this.findViewById(R.id.signupacctextView);
        inputdeterminepassword = (EditText)this.findViewById(R.id.signupnewpwdeditText);
        this.signUpComfirmBtn = (ImageButton)this.findViewById(R.id.signupcomfirmbtn);
        this.signUpComfirmBtn.setOnClickListener(this);
        this.signUpReturnBtn = (ImageButton)this.findViewById(R.id.signupreturnbtn);
        this.signUpReturnBtn.setOnClickListener(this);
        this.checkBox = (CheckBox)this.findViewById(R.id.signupcheckBox);
        String source= this.getString(R.string.I_Agree_and_Read_Type)+ "<u><font color='red'>"+
                this.getString(R.string.Membership_Policy_Type)+ "</font></u>";
        checkBox.setText(Html.fromHtml(source));
        checkBox.setOnClickListener(this);
        CreateHttpClient();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signupcheckBox) {
            Log.i("Sign up page","i == R.id.signupcheckBox is:"+(i == R.id.signupcheckBox) );
            if (v.getClass() == CheckBox.class) {
                ((CheckBox) v).setChecked(true);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.belugame.com/clause.asp"));
            startActivity(intent);

        } else if (i == R.id.signupreturnbtn) {
        	this.signUpReturnBtn.startAnimation(selectedMoveRight);
        	this.signUpComfirmBtn.startAnimation(scaleHide);
        	
        	final Handler handler = new Handler();
       	 	handler.postDelayed(new Runnable() {
       	     @Override
       	     public void run() {
       	         // Do something after 700ms
       	    	//unlock can't edit and click
       	    	finish();
       	     }
       	 	}, DELAY_TIME);

        } else if (i == R.id.signupcomfirmbtn) {/* Changed by Leo Ling */
            if (inputaccount.getText().toString().compareTo("") == 0) {

                Toast.makeText(Registration.this,
                        this.getString(R.string.Enter_Ac_Type),
                        Toast.LENGTH_LONG).show();

                return;
            }
            if (inputpassword.getText().toString().equals(inputdeterminepassword.getText().toString())) {
               
                authhttpclient.Auth_RegisterAccount(inputaccount.getText().toString(),
                        inputpassword.getText().toString());
            } else {
                //Toast.makeText(Registration.this, "撖Ⅳ�Ⅱ隤�蝣潔��詨�", Toast.LENGTH_LONG)
                Toast.makeText(Registration.this,
                        this.getString(R.string.Pwd_Not_Match_Type),
                        Toast.LENGTH_LONG).show();
            }

			/* Changed by Leo Ling end */
        }
    }

    private void CreateHttpClient() {
    
        authhttpclient = new AuthHttpClient(this);
       
        authhttpclient.AuthEventListener(new OnAuthEventListener() {
            public void onProcessDoneEvent(int Code, String Message, Long uid,
                                           String Account, String Pwd) {
                String CodeStr = UsedString.getFastRegistrationGenerateString(getApplicationContext(), Code);
                if(CodeStr.compareTo("") == 0 && Code != 1){
                    //Looper.prepare();
                    Toast.makeText(Registration.this, Message, Toast.LENGTH_SHORT).show();
                    //Looper.loop();
                }else if(Code == 1){
                    InformationProcess.saveAccountPassword(inputaccount.getText()
                            .toString(), inputpassword.getText().toString(), Registration.this);
                    InformationProcess.saveUserUid(Long.toString(uid),
                            Registration.this);
                    
                    signUpComfirmBtn.startAnimation(selectedMoveLeft);
                    signUpReturnBtn.startAnimation(scaleHide);
                	
                	final Handler handler = new Handler();
               	 	handler.postDelayed(new Runnable() {
               	     @Override
               	     public void run() {
               	         // Do something after 700ms
               	    	//unlock can't edit and click
               	    	SetFinish(inputaccount.getText().toString(), inputpassword
                                .getText().toString());
               	     }
               	 	}, DELAY_TIME);
                    
                }else{
                    Toast.makeText(Registration.this, CodeStr, Toast.LENGTH_LONG).show();
                }
            }

			@Override
			public void onProcessDoneEvent(int Code, String Message, Long uid, String Account, String Pwd,
					String accountBound) {
				// TODO Auto-generated method stub
				
			}
        });
    }

    private void SetFinish(String thisuserid, String thisuid) {
        Intent resultdata = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("ResultType", 1);
        bundle.putString("userid", thisuserid);
        bundle.putString("userpwd", thisuid);
        resultdata.putExtras(bundle);
        // this.setResult(Activity.RESULT_OK, resultdata); //�RESULT_OK
        // this.finish();
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, resultdata);
        } else {
            getParent().setResult(Activity.RESULT_OK, resultdata);
        }
        finish();
    }

}
