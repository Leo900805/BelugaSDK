package com.beluga.LoginPage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.beluga.LoginPage.AuthHttpClient.OnAuthEventListener;
import com.beluga.LoginPage.datacontrol.Saveaccountandpassword;
import com.beluga.LoginPage.datacontrol.UsedString;
import com.beluga.R;
/**
 * Created by Leo on 2015/10/5.
 */
public class Registration extends Activity implements OnClickListener {

    EditText inputaccount,inputpassword,inputdeterminepassword;
    // 連結SERVER用
    AuthHttpClient authhttpclient;
    public Button signUpComfirmBtn, signUpReturnBtn;
    public CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.sign_up_page);

        inputpassword = (EditText)this.findViewById(R.id.signuppwdeditText);
        inputaccount = (EditText)this.findViewById(R.id.signupacctextView);
        inputdeterminepassword = (EditText)this.findViewById(R.id.signupnewpwdeditText);
        this.signUpComfirmBtn = (Button)this.findViewById(R.id.signupcomfirmbtn);
        this.signUpComfirmBtn.setOnClickListener(this);
        this.signUpReturnBtn = (Button)this.findViewById(R.id.signupreturnbtn);
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
            finish();

        } else if (i == R.id.signupcomfirmbtn) {/* Changed by Leo Ling */
            if (inputaccount.getText().toString().compareTo("") == 0) {

                //Toast.makeText(Registration.this, "請輸入帳號", Toast.LENGTH_LONG).show();
                Toast.makeText(Registration.this,
                        this.getString(R.string.Enter_Ac_Type),
                        Toast.LENGTH_LONG).show();

                return;
            }
            if (inputpassword.getText().toString().equals(inputdeterminepassword.getText().toString())) {
                //一樣的話就直接註冊
                authhttpclient.Auth_RegisterAccount(inputaccount.getText().toString(),
                        inputpassword.getText().toString());
            } else {
                //Toast.makeText(Registration.this, "密碼與確認密碼不相同", Toast.LENGTH_LONG)
                Toast.makeText(Registration.this,
                        this.getString(R.string.Pwd_Not_Match_Type),
                        Toast.LENGTH_LONG).show();
            }

			/* Changed by Leo Ling end */
        }
    }

    private void CreateHttpClient() {
        // 建立監聽事件
        // 網路處理_自已寫的類別 __手術用 "吁叫點 按下按鈕吁叫到"
        authhttpclient = new AuthHttpClient(this);
        // 接收到網路回來資料 ----- "吁叫監聽事件放的地方"
        // 當按下按鈕時 吁叫到它 "SERVER傳回資料時會吁叫到它---------"
        authhttpclient.AuthEventListener(new OnAuthEventListener() {
            public void onProcessDoneEvent(int Code, String Message, Long uid,
                                           String Account, String Pwd) {
                String CodeStr = UsedString.getFastRegistrationGenerateString(getApplicationContext(), Code);
                if(CodeStr.compareTo("") == 0 && Code != 1){
                    //Looper.prepare();
                    Toast.makeText(Registration.this, Message, Toast.LENGTH_SHORT).show();
                    //Looper.loop();
                }else if(Code == 1){
                    Saveaccountandpassword.saveaccountpassword(inputaccount.getText()
                            .toString(), inputpassword.getText().toString(), Registration.this);
                    Saveaccountandpassword.saveUserUid(Long.toString(uid),
                            Registration.this);
                    SetFinish(inputaccount.getText().toString(), inputpassword
                            .getText().toString());
                }else{
                    Toast.makeText(Registration.this, CodeStr, Toast.LENGTH_LONG).show();
                }
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
        // this.setResult(Activity.RESULT_OK, resultdata); //回傳RESULT_OK
        // this.finish();
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, resultdata);
        } else {
            getParent().setResult(Activity.RESULT_OK, resultdata);
        }
        finish();

    }

}
