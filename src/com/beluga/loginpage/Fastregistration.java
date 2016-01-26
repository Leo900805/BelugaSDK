package com.beluga.loginpage;

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

import com.beluga.loginpage.AuthHttpClient.OnAuthEventListener;
import com.beluga.loginpage.datacontrol.Saveaccountandpassword;
import com.beluga.loginpage.datacontrol.UsedString;
import com.beluga.R;
/**
 * Created by Leo on 2015/10/5.
 */
public class Fastregistration extends Activity implements OnClickListener{

    EditText inputaccount,inputpassword;
    AuthHttpClient authhttpclient,authhttpclient_confirm;
    public Button qsModpwdBtn, qsReturnBtn, qsComfirmBtn;
    public CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.quick_sign_up_page);
        this.qsComfirmBtn = (Button)this.findViewById(R.id.qscomfirmbtn);
        this.qsComfirmBtn.setOnClickListener(this);
        this.qsReturnBtn = (Button)this.findViewById(R.id.qsreturnbtn);
        this.qsReturnBtn.setOnClickListener(this);
        this.qsModpwdBtn = (Button)this.findViewById(R.id.qsmodbtn);
        this.qsModpwdBtn.setOnClickListener(this);
        inputpassword = (EditText)this.findViewById(R.id.qspwdeditText);
        inputaccount = (EditText)this.findViewById(R.id.qsacceditText);
        this.checkBox = (CheckBox)this.findViewById(R.id.qscheckBox);
        String source= this.getString(R.string.I_Agree_and_Read_Type)+ "<u><font color='red'>"+
                this.getString(R.string.Membership_Policy_Type)+ "</font></u>";
        checkBox.setText(Html.fromHtml(source));
        checkBox.setOnClickListener(this);
        CreateHttpClient();
        authhttpclient.Auth_QuickAccount();
    }

    private void CreateHttpClient(){

      
        authhttpclient = new AuthHttpClient(this);
      
        authhttpclient.AuthEventListener(new OnAuthEventListener() {
            public void onProcessDoneEvent(int Code, String Message, Long uid, String Account, String Pwd) {
                String CodeStr = UsedString.getFastRegistrationGenerateString(getApplicationContext(), Code);
                if (CodeStr.compareTo("") == 0 && Code != 1) {
                    //Looper.prepare();
                    Toast.makeText(Fastregistration.this, Message, Toast.LENGTH_SHORT).show();
                    //Looper.loop();
                } else if (Code == 1) {
                    Log.i("FastResg","CrateHttpClient got Account value is:"+Account);
                    inputaccount.setText(Account);
                    Log.i("FastResg", "CrateHttpClient got password value is:" + Pwd);
                    inputpassword.setText(Pwd);
                } else {
                    Toast.makeText(Fastregistration.this, CodeStr, Toast.LENGTH_LONG).show();
                }

                //�交�����珞��摰�
                System.out.println("Code " + Code + "  message   " + Message + "  uid " + uid + "  Account " + Account + " pwd  " + Pwd);
            }

			@Override
			public void onProcessDoneEvent(int Code, String Message, Long uid, String Account, String Pwd,
					String accountBound) {
				// TODO Auto-generated method stub
				
			}

        });
    }

    public void AccountConfirm()
    {
        CreateHttpClientConfirm();
        authhttpclient_confirm.Auth_RegisterAccount(inputaccount.getText().toString(), inputpassword.getText().toString());
    }

    private void CreateHttpClientConfirm()
    {
      
        authhttpclient_confirm = new AuthHttpClient(this);
      
        authhttpclient_confirm.AuthEventListener(new OnAuthEventListener() {
            public void onProcessDoneEvent(int Code, String Message, Long uid, String Account, String Pwd) {
                String CodeStr = UsedString.getFastRegistrationGenerateString(getApplicationContext(), Code);
                if (CodeStr.compareTo("") == 0 && Code != 1) {
                    //Looper.prepare();
                    Toast.makeText(Fastregistration.this, Message, Toast.LENGTH_SHORT).show();
                    //Looper.loop();
                } else if (Code == 1) {
                    Toast.makeText(Fastregistration.this, CodeStr, Toast.LENGTH_LONG).show();
                    Saveaccountandpassword.saveUserUid(Long.toString(uid), Fastregistration.this);
                    SetFinish(inputaccount.getText().toString(), inputpassword.getText().toString());
                } else {
                    Toast.makeText(Fastregistration.this, CodeStr, Toast.LENGTH_LONG).show();
                }
            }

			@Override
			public void onProcessDoneEvent(int Code, String Message, Long uid, String Account, String Pwd,
					String accountBound) {
				// TODO Auto-generated method stub
				
			}
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.qscheckBox) {
            if (v.getClass() == CheckBox.class) {
                ((CheckBox) v).setChecked(true);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.belugame.com/clause.asp"));
            startActivity(intent);

        } else if (i == R.id.qscomfirmbtn) {
            AccountConfirm();

        } else if (i == R.id.qsreturnbtn) {
            finish();

        } else if (i == R.id.qsmodbtn) {
            inputpassword.setEnabled(true);
            inputpassword.setText("");
            inputpassword.requestFocus();

        }
    }

    private void SetFinish(String thisuserid,String thisuid)
    {
        Intent resultdata = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("ResultType",1);
        bundle.putString("userid", thisuserid);
        bundle.putString("userpwd", thisuid);
        resultdata.putExtras(bundle);
        //this.setResult(Activity.RESULT_OK, resultdata); //�RESULT_OK
        //this.finish();
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, resultdata);
            Log.d("tag", "F resultdata: "+resultdata);
        } else {
            getParent().setResult(Activity.RESULT_OK, resultdata);
        }
        finish();
    }

}
