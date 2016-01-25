package com.beluga.loginpage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;

import com.beluga.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URLConnection;
import java.security.MessageDigest;
import 	java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Leo on 2015/10/6.
 */
@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public class AuthHttpClient {

    Activity MainActivity;
    OnAuthEventListener AuthEventListener;
    public static String AppID = "";
    public static String ApiKey = "";
    public static String ApiUrl = "";
    public static String PackageID = "";
    public static String version="10405121054";

    private ProgressDialog loadingProgress;

    // 撱箸�撘�
    public AuthHttpClient(Activity act) {
        MainActivity = act;
    }

    /*
     * public AuthHttpClient(Activity act,String appid,String apikey) {
     * MainActivity = act; this.SetApiInfo(appid, apikey); }
     *
     * public void SetApiInfo(String appid,String apikey) { AppID = appid;
     * ApiKey = apikey; }
     */
    public boolean isApiInfoExists() {
        if (AppID.length() == 0) {
            return false;
        }
        if (ApiKey.length() == 0) {
            return false;
        }
        if (ApiKey.length() != 32) {
            return false;
        }
        return true;
    }

    // 鈭辣Listener摰��
    public interface OnAuthEventListener {
        public void onProcessDoneEvent(int Code, String Message, Long uid, String Account, String Pwd);
    }

    // 閫貊鈭辣�
    private void OnAuthEvent(int Code, String Message, long i, String Account,String Pwd) {
        //
        if (AuthEventListener != null) {
            AuthEventListener.onProcessDoneEvent(Code, Message, i, Account, Pwd);
        }
    }

    // 0000000
    public void AuthEventListener(OnAuthEventListener onAuthEventListener) {

        AuthEventListener = onAuthEventListener;
    }

    // 蝬脰楝�臬�舐
    public boolean isInternetAvailable() {
        ConnectivityManager manager = (ConnectivityManager) MainActivity
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if(manager == null)	return false;

        NetworkInfo Wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo Mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // For WiFi Check
        if(Wifi != null){
            if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .isConnectedOrConnecting()) {
                return true;
            }
        }
        // For Data network check
        if(Mobile != null){
            if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    // HttpPost撖阡��寞�
    private void httpPOST(AuthCommandType t, String url, List<NameValuePair> list) {
        MainActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (loadingProgress == null) {
                    loadingProgress = new ProgressDialog(MainActivity);
                    loadingProgress.setMessage("Loading");
                }
                loadingProgress.show();
            }
        });

        HttpPost post = new HttpPost(url);
        try {
            DefaultHttpClient httpClient;
            if (url.indexOf("https") == 0) {
                SchemeRegistry registry = new SchemeRegistry();
                // SSL All Allow
                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                DefaultHttpClient client = new DefaultHttpClient();
                SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
                httpClient = new DefaultHttpClient(mgr, client.getParams());
                // Set verifier
                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            } else {
                httpClient = new DefaultHttpClient();
            }
            // �HTTP request
            // 撠lient鞈� �SERVER
            for(int i=0;i<list.size();i++){
                Log.d("list:", list.get(i).toString());
            }
            post.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));

            // ��HTTP response
            HttpResponse httpResponse = httpClient.execute(post);
            // 瑼Ｘ���蝣潘�200銵函內OK
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                // *********���摮葡 ��SERVER�喳�靘�鞈�
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                // �啣����~~
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("AuthValue", strResult);
                data.putInt("AuthType", t.getIntValue());
                // ����
                msg.setData(data);
                // �����亙鞈���ㄐ
                HttpPostHandler.sendMessage(msg);
            } else {
                OnAuthEvent(-101, "ServerHttpStatusError"
                        + httpResponse.getStatusLine().getStatusCode(), -1, "", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            OnAuthEvent(-100, "HttpPostError", -1, "", "");
        }
    }
    private void httpGET(AuthCommandType t, String url){
        try {
        	 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            //inputStream = httpResponse.getEntity().getContent();
            String strResult = EntityUtils.toString(httpResponse.getEntity());
            Log.d("httpGet", "Result:"+strResult);
            
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("AuthValue", strResult);
            data.putInt("AuthType", t.getIntValue());
            msg.setData(data);
            HttpPostHandler.sendMessage(msg);
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
    }


    // HttpPost摰�鈭辣�� "�亦雯頝臭��喳�靘���
    Handler HttpPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // ��蝬脰楝銝���

            if (loadingProgress != null) {
                loadingProgress.dismiss();
            }

            String cmdstr = msg.getData().getString("AuthValue");
            int cmdtyp = msg.getData().getInt("AuthType");
            System.out.println("HttpPostHandler get data----- " + cmdstr);
            try {
                AuthBackDataProcess(AuthCommandType.values()[cmdtyp], cmdstr);
                System.out.println("getDtaFromJSON--------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // Server蝡臬隞日��亙�蝢�
    public enum AuthCommandType {
        Login(0), QuickAccount(1), RegisterAccount(2), ChangePassword(3),
        FacebookLoginRegister(4);

        private final int AuthTypevalue;

        private AuthCommandType(int value) {
            this.AuthTypevalue = value;
        }

        public int getIntValue() {
            return AuthTypevalue;
        }
    }

    // MD5 ????
    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    private static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
    private String getPackgetName() {
        return MainActivity.getApplicationContext().getPackageName();
    }

    public static boolean isAccountRuleString(String target) {

        boolean result = false;

        result = target.matches("^[A-Za-z0-9]+$");

        return result;
    }
    // 撣唾�閬��摨�
    public static boolean isAccountRuleLength(String target) {

        if (target.length() < 6 || target.length() > 32) {
            return false;
        }
        return true;
    }

    // ==============================�箏�� End==============================
    // �餃 撣唾� 撖Ⅳ
    public void Auth_UserLogin(String UserID, String UserPassword) {
        // 撣唾� ��駁擐偏蝛箇蝚西���摮葡
        UserID = UserID.trim();
        // 撖Ⅳ
        UserPassword = UserPassword.trim();
        // 憒�銝泵�董�摨�
        if (!isAccountRuleLength(UserID)) {
            //OnAuthEvent(-2, "撣唾��瑕漲�航炊", -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Ac_Length_Err_Type), -1, "", "");
            return;
        }
        if (!isAccountRuleLength(UserPassword)) {
            //OnAuthEvent(-2, "撖Ⅳ�瑕漲�航炊", -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Pwd_Length_Err_Type) , -1, "", "");
            return;
        }
        if (!isAccountRuleString(UserID)) {
            //OnAuthEvent(-2, "撣唾��芾�刻�摮�, -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Ac_Only_Char_And_Num_Type), -1, "", "");
            return;
        }
        if (!isAccountRuleString(UserPassword)) {
            //OnAuthEvent(-2, "撖Ⅳ�芾�刻�摮�, -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Pwd_Only_Char_And_Num_Type), -1, "", "");
            return;
        }
        if (!isApiInfoExists()) {
            //OnAuthEvent(-501, "Appid or Apikey error", -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Appid_Or_Apikey_Err_Type), -1, "", "");
            return;
        }
        if (!isInternetAvailable()) {
            //OnAuthEvent(-500, "蝬脰楝���憭望�", -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Network_Connection_Failure_Type), -1, "", "");
            return;
        }

        final String UrlAction = "MemberLogin";
			/*
			 * appid userid pwd devid sign
			 */
        String devid = Secure.getString(MainActivity.getContentResolver(),
                Secure.ANDROID_ID);
        // apikey+appid+userid+pwd+apikey
        String sign = MD5(ApiKey + AppID + UserID + UserPassword + ApiKey );
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("appid", AppID));
        params.add(new BasicNameValuePair("userid", UserID));
        params.add(new BasicNameValuePair("pwd", UserPassword));
        params.add(new BasicNameValuePair("devid", devid));
        params.add(new BasicNameValuePair("packageid", PackageID));
        Log.i("login funciuon", PackageID);
        params.add(new BasicNameValuePair("sign", sign));

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // �SER 憛怠�董��撖Ⅳ鞈��喳�喟雯頝臭�
                // ����餃��
                httpPOST(AuthCommandType.Login, ApiUrl + UrlAction, params);

            }
        };
        new Thread(runnable).start();
    }

    //Facebook login and Register
    public void Auth_FacebookLoignRegister(String fbID){
        
    	Long tsLong = System.currentTimeMillis()/1000;
    	String ts = tsLong.toString();
        Log.i("fb timestamp", "ts " + ts);
        String sign = MD5(AppID + fbID + ApiKey + ts);
        final String UrlAction = "http://belugame.com/api/facebook/"+"?"+AppID+"-"+fbID+"-"+ApiKey+"-"+ts+"-"+sign+".html";

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                httpGET(AuthCommandType.FacebookLoginRegister, UrlAction);
            }
        };
        new Thread(runnable).start();
    }

    public void Auth_QuickAccount() {

        if (!isApiInfoExists()) {
            //OnAuthEvent(-501, "Appid or Apikey error", -1, "", "");
            OnAuthEvent(-501, MainActivity.getString(R.string.Appid_Or_Apikey_Err_Type), -1, "", "");
            return;
        }
        if (!isInternetAvailable()) {
            //OnAuthEvent(-500, "蝬脰楝���憭望�", -1, "", "");
            OnAuthEvent(-500, MainActivity.getString(R.string.Network_Connection_Failure_Type), -1, "", "");
            return;
        }
        final String UrlAction = "MemberMake";

        String devid = Secure.getString(MainActivity.getContentResolver(),
                Secure.ANDROID_ID);
        System.out.println("devid ----" + devid);
        String devtype = getDeviceName();
        System.out.println("devtype ----" + devtype);
        String packid = getPackgetName();
        System.out.println("packid ----" + packid);
        String sign = MD5(ApiKey + AppID + devid + ApiKey);
        System.out.println("sign ----" + sign);
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("appid", AppID));
        params.add(new BasicNameValuePair("devid", devid));
        params.add(new BasicNameValuePair("devtype", devtype));
        params.add(new BasicNameValuePair("packid", packid));
        params.add(new BasicNameValuePair("packageid", PackageID));
        params.add(new BasicNameValuePair("sign", sign));
        // 銝�芾��頝銵�
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // �唾����
                httpPOST(AuthCommandType.QuickAccount, ApiUrl + UrlAction,
                        params);
            }
        };
        new Thread(runnable).start();
    }

    // 閮餃� _�亦�Ｖ�頛詨�董��撖Ⅳ
    public void Auth_RegisterAccount(String UserID, String UserPassword) {
        UserID = UserID.trim();
        UserPassword = UserPassword.trim();
        // 瑼Ｘ�臬ERROR �交����瑁�蝺�
        if (!isAccountRuleLength(UserID)) {
            //OnAuthEvent(-2, "撣唾��瑕漲�航炊", -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Ac_Length_Err_Type), -1, "", "");
            return;
        }
        if (!isAccountRuleLength(UserPassword)) {
            //OnAuthEvent(-2, "撖Ⅳ�瑕漲�航炊", -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Pwd_Length_Err_Type), -1, "", "");
            return;
        }
        if (!isAccountRuleString(UserID)) {
            //OnAuthEvent(-2, "撣唾��芾�刻�摮�, -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Ac_Only_Char_And_Num_Type), -1, "", "");
            return;
        }
        if (!isAccountRuleString(UserPassword)) {
            //OnAuthEvent(-2, "撖Ⅳ�芾�刻�摮�, -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Pwd_Only_Char_And_Num_Type), -1, "", "");
            return;
        }
        if (!isApiInfoExists()) {
            //OnAuthEvent(-501, "Appid or Apikey error", -1, "", "");
            OnAuthEvent(-501, MainActivity.getString(R.string.Appid_Or_Apikey_Err_Type), -1, "", "");
            return;
        }
        if (!isInternetAvailable()) {
            //OnAuthEvent(-500, "蝬脰楝���憭望�", -1, "", "");
            OnAuthEvent(-500, MainActivity.getString(R.string.Network_Connection_Failure_Type), -1, "", "");
            return;
        }

        final String UrlAction = "MemberCreate";

        String devid = Secure.getString(MainActivity.getContentResolver(),
                Secure.ANDROID_ID);
        String devtype = "";
        String packid = "";
        try {
            devtype = TextUtils.htmlEncode(getDeviceName());
            packid = TextUtils.htmlEncode(getPackgetName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }

        String sign = MD5(ApiKey + AppID + UserID + devid + ApiKey);
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("appid", AppID));
        params.add(new BasicNameValuePair("userid", UserID));
        params.add(new BasicNameValuePair("pwd", UserPassword));
        params.add(new BasicNameValuePair("devid", devid));
        params.add(new BasicNameValuePair("devtype", devtype));
        params.add(new BasicNameValuePair("ostype", "1"));
        params.add(new BasicNameValuePair("packid", packid));
        params.add(new BasicNameValuePair("packageid", PackageID));
        params.add(new BasicNameValuePair("sign", sign));

        // �����
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // �SER �喳���蝬脰楝銝�
                httpPOST(AuthCommandType.RegisterAccount, ApiUrl + UrlAction,
                        params);
            }
        };
        new Thread(runnable).start();

    }

    // 靽格撖Ⅳ
    public void Auth_ChangePassword(String UserID, String OldPassword,
                                    String NewPassword) {
        UserID = UserID.trim();
        OldPassword = OldPassword.trim();
        NewPassword = NewPassword.trim();
        Log.i("Auth_ChangePassword","UserID "+UserID + " OldPassword "+OldPassword +" NewPassword "+NewPassword);
        if (!isAccountRuleLength(UserID)) {
            //OnAuthEvent(-2, "撣唾��瑕漲�航炊", -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Ac_Length_Err_Type), -1, "", "");
            return;
        }
        if (!isAccountRuleLength(OldPassword)) {
            //OnAuthEvent(-2, "��蝣潮摨阡隤�, -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Old_Password_Length_Err_Type), -1, "", "");
            return;
        }
        if (!isAccountRuleLength(NewPassword)) {
            //OnAuthEvent(-2, "�啣�蝣潮摨阡隤�, -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.New_Pwd_Length_Err_Type), -1, "", "");
            return;
        }
        if (!isAccountRuleString(UserID)) {
            //OnAuthEvent(-2, "撣唾��芾�刻�摮�, -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Ac_Only_Char_And_Num_Type), -1, "", "");
            return;
        }
        if (!isAccountRuleString(OldPassword)) {
            //OnAuthEvent(-2, "��蝣澆�賜�望��詨�", -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.Old_Password_Only_Char_And_Num_Type), -1, "", "");
            return;
        }
        if (!isAccountRuleString(NewPassword)) {
            //OnAuthEvent(-2, "�啣�蝣澆�賜�望��詨�", -1, "", "");
            OnAuthEvent(-2, MainActivity.getString(R.string.New_Password_Only_Char_And_Num_Type), -1, "", "");
            return;
        }
        if (!isApiInfoExists()) {
            //OnAuthEvent(-501, "Appid or Apikey error", -1, "", "");
            OnAuthEvent(-501, MainActivity.getString(R.string.Appid_Or_Apikey_Err_Type), -1, "", "");
            return;
        }
        if (!isInternetAvailable()) {
            //OnAuthEvent(-500, "蝬脰楝���憭望�", -1, "", "");
            OnAuthEvent(-500, MainActivity.getString(R.string.Network_Connection_Failure_Type), -1, "", "");
            return;
        }
        if (!isApiInfoExists()) {
            //OnAuthEvent(-501, "Appid or Apikey error", -1, "", "");
            OnAuthEvent(-501, MainActivity.getString(R.string.Appid_Or_Apikey_Err_Type), -1, "", "");
            return;
        }


        final String UrlAction = "MemberModPwd";

        String devid = Secure.getString(MainActivity.getContentResolver(),
                Secure.ANDROID_ID);
        String sign = MD5(ApiKey + AppID + UserID + OldPassword + ApiKey);
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("appid", AppID));
        params.add(new BasicNameValuePair("userid", UserID));
        params.add(new BasicNameValuePair("pwd", OldPassword));
        params.add(new BasicNameValuePair("newpwd", NewPassword));
        params.add(new BasicNameValuePair("devid", devid));
        params.add(new BasicNameValuePair("packageid", PackageID));
        params.add(new BasicNameValuePair("sign", sign));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                httpPOST(AuthCommandType.ChangePassword, ApiUrl + UrlAction,
                        params);
            }
        };
        new Thread(runnable).start();
    }

    // Server蝡臬��唾�����__handle���交�訕erver蝡臬��唾����唳 //�餃 敹恍�閮餃� 閮餃� �寡�撖Ⅳ�����
    private void AuthBackDataProcess(AuthCommandType t, String Data) {
        switch (t) {
            case Login:
                AuthBackDataProc_Login(Data);
//				Log.i("tag", "AuthBackDataProc_Login:"+Data.toString());
                break;
            case QuickAccount:
                AuthBackDataProc_QuickAccount(Data);
//				Log.i("tag", "QuickAccount:"+Data.toString());
                break;
            case RegisterAccount:
                AuthBackDataProc_RegisterAccount(Data);
                break;
            case ChangePassword:
                AuthBackDataProc_ChangePassword(Data);
                break;
            case FacebookLoginRegister:
            	Log.d("AuthBackDataProcess","Case FacebookLoginRegister start");
                AuthBackDataProc_FacebookLoginRegister(Data);
                Log.d("AuthBackDataProcess","Case FacebookLoginRegister end");
                break;
            default:
                AuthBackDataProc_UnknowType();
                break;
        }
    }

    // 隞乩��航���_"����� �亦雯頝臬�靘�JSON鞈� 閫��JSON
    private void AuthBackDataProc_Login(String Data) {
        try {
            JSONObject jObj = new JSONObject(Data);
            String code = jObj.getString("Code");
            String msg = jObj.getString("Message");
            String uid = jObj.getString("uid");
            //String pid = jObj.getString(name)
            OnAuthEvent(Integer.parseInt(code), msg, Long.parseLong(uid), "","");
        } catch (Exception e) {
            //OnAuthEvent(-102, "DataParseError", -1, "", "");
            OnAuthEvent(-102,  MainActivity.getString(R.string.Data_Parse_Error_Type), -1, "", "");
        }

    }

    private void AuthBackDataProc_QuickAccount(String Data) {
        try {
            JSONObject jObj = new JSONObject(Data);
            String code = jObj.getString("Code");
            String msg = jObj.getString("Message");
            String uid = jObj.getString("useruid");
            String userid = jObj.getString("userid");
            String userpwd = jObj.getString("upd");
            OnAuthEvent(Integer.parseInt(code), msg, Long.parseLong(uid), userid, userpwd);
        } catch (Exception e) {
            //OnAuthEvent(-102, "DataParseError", -1, "", "");
            OnAuthEvent(-102,  MainActivity.getString(R.string.Data_Parse_Error_Type), -1, "", "");
        }
    }

    private void AuthBackDataProc_RegisterAccount(String Data) {
        try {
            JSONObject jObj = new JSONObject(Data);
            String code = jObj.getString("Code");
            String msg = jObj.getString("Message");
            String uid = jObj.getString("uid");
            OnAuthEvent(Integer.parseInt(code), msg, Long.parseLong(uid), "",
                    "");

        } catch (Exception e) {
            //OnAuthEvent(-102, "DataParseError", -1, "", "");
            OnAuthEvent(-102,  MainActivity.getString(R.string.Data_Parse_Error_Type), -1, "", "");
        }
    }

    private void AuthBackDataProc_ChangePassword(String Data) {
        try {
            JSONObject jObj = new JSONObject(Data);
            String code = jObj.getString("Code");
            String msg = jObj.getString("Message");
            OnAuthEvent(Integer.parseInt(code), msg, 0, "", "");
        } catch (Exception e) {
            //OnAuthEvent(-102, "DataParseError", -1, "", "");
            OnAuthEvent(-102,  MainActivity.getString(R.string.Data_Parse_Error_Type), -1, "", "");
        }
    }

    private void AuthBackDataProc_FacebookLoginRegister(String Data) {
    	Log.i("AuthBackDataProc_FacebookLoginRegister", "Start...");
        try {
        	/* check JSON
        	 * fisrt time {"uid":1030068, "fbid":936544699763939, "QuickReg":"BAFQJ044", "Quickpw":"B722541", "code":1, "message":"Succeed!"} 
        	 * second time */
            JSONObject jObj = new JSONObject(Data);
            String code = jObj.getString("code");
            String msg = jObj.getString("message");
            String uid = jObj.getString("uid");
            String userid = jObj.getString("QuickReg");
            String userpwd = jObj.getString("Quickpw");
            String fbId = jObj.getString("fbid");
            OnAuthEvent(Integer.parseInt(code), msg, Long.parseLong(uid), userid, userpwd);
        } catch (Exception e) {
            //OnAuthEvent(-102, "DataParseError", -1, "", "");
        	Log.i("AuthBackDataProc_FacebookLoginRegister", "Data_Parse_Error_Type");
            OnAuthEvent(-102,  MainActivity.getString(R.string.Data_Parse_Error_Type), -1, "", "");
        }
        Log.i("AuthBackDataProc_FacebookLoginRegister", "end...");
    }

    private void AuthBackDataProc_UnknowType() {
        //OnAuthEvent(-102, "DataParseError", -1, "", "");
    	Log.i("AuthBackDataProc_UnknowType", "Unknow");
        OnAuthEvent(-102,  MainActivity.getString(R.string.Data_Parse_Error_Type), -1, "", "");
    }

}
