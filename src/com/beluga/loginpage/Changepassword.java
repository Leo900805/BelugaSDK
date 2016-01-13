package com.beluga.loginpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beluga.loginpage.AuthHttpClient.OnAuthEventListener;
import com.beluga.loginpage.datacontrol.Saveaccountandpassword;
import com.beluga.loginpage.datacontrol.UsedString;
import com.beluga.R;
/**
 * Created by Leo on 2015/10/5.
 */
public class Changepassword extends Activity implements OnClickListener{

    EditText  inputpassword ,inputnewpassword,inputdeterminepassword;
    TextView inputaccount;
    AuthHttpClient authhttpclient;
    Button modReturnBtn, modComirmBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.modify_password_page);
        this.modComirmBtn = (Button)this.findViewById(R.id.modcomfirmbtn);
        this.modComirmBtn.setOnClickListener(this);
        this.modReturnBtn = (Button)this.findViewById(R.id.modreturnbtn);
        this.modReturnBtn.setOnClickListener(this);
        inputpassword = (EditText)this.findViewById(R.id.modpwdeditText);
        inputaccount =  (TextView)this.findViewById(R.id.modacctextView);
        inputdeterminepassword = (EditText)this.findViewById(R.id.modretypepwdeditText);
        inputnewpassword = (EditText)this.findViewById(R.id.modnewpwdeditText);

        //�����董/撖�
        inputaccount.setText(Saveaccountandpassword.GetaccountString(this));
        inputpassword.setText(Saveaccountandpassword.GetpasswordString(this));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.modreturnbtn) {
            finish();

        } else if (i == R.id.modcomfirmbtn) {
            ChangeAccountPassword();

        }
    }

    //��蝣箏������SERVER
    public void ChangeAccountPassword()
    {
        //撱箇���鈭辣
        authhttpclient = new AuthHttpClient(this);
        //�交�啁雯頝臬�靘��� ----- "�澆��鈭辣�曄��唳"
        //�嗆�銝���  �澆�啣�   "SERVER�喳�鞈����澆�啣�---------"
        authhttpclient.AuthEventListener(new OnAuthEventListener(){
            public void onProcessDoneEvent(int Code,String Message,Long uid,String Account,String Pwd)
            {
                String CodeStr = UsedString.getChangePasswordString(getApplicationContext(), Code);
                if(CodeStr.compareTo("")==0){
                    Toast.makeText(Changepassword.this, Message, Toast.LENGTH_LONG).show();
                }else if(Code == 1){
                    Toast.makeText(Changepassword.this, CodeStr, Toast.LENGTH_LONG).show();
                    SetFinish(inputaccount.getText().toString(),inputnewpassword.getText().toString());
                }else{
                    Toast.makeText(Changepassword.this, CodeStr, Toast.LENGTH_LONG).show();
                }

            }
        });
        //�喲�鞈��訕ERVER 撣唾�/撖Ⅳ
        authhttpclient.Auth_ChangePassword(this.inputaccount.getText().toString(),this.inputpassword.getText().toString(),this.inputdeterminepassword.getText().toString());
    }

    private void SetFinish(String thisuserid,String thisuid)
    {
        Intent resultdata = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("ResultType",2);
        bundle.putString("userid", thisuserid);
        bundle.putString("userpwd", thisuid);
        resultdata.putExtras(bundle);
        this.setResult(Activity.RESULT_OK, resultdata);
        finish();
    }
}
