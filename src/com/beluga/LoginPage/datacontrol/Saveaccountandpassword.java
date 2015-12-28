package com.beluga.LoginPage.datacontrol;

/**
 * Created by deskuser on 2015/10/6.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Saveaccountandpassword {
    //帳號
    private static final String  successaccount= "hsaccount";
    //密碼
    private static final String  successpassword = "hspassword";
    //Uid
    private static final String successUid = "hsUserUid";

    public static boolean ok;


    //存傳進來字串
    public static void saveaccountpassword(String account,String password,Activity act)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(act);
        Editor editor = preferences.edit();

        //key,value
        try {
            //存入資料
            String encacc = encrypt(account, "9@a8i7Az");
            String enpwd = encrypt(password, "9@a8i7Az");
            editor.putString(successaccount,URLEncoder.encode(encacc, "utf-8"));
            editor.putString(successpassword,URLEncoder.encode(enpwd, "utf-8"));
            Settings.System.putString(act.getContentResolver(), successaccount, URLEncoder.encode(encacc, "utf-8"));
            Settings.System.putString(act.getContentResolver(), successpassword, URLEncoder.encode(enpwd, "utf-8"));

        } catch (Exception e) {
        }
        editor.commit();

    }

    // 存Uid
    public static void saveUserUid(String uid, Activity act) {
        if (uid.equalsIgnoreCase("") || uid.equalsIgnoreCase("0") || uid == null ) {
            return;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(act);
        Editor editor = preferences.edit();
        System.setProperty(successUid, uid);

        try {
            String enUid = encrypt(uid, "9@a8i7Az");
            editor.putString(successUid, URLEncoder.encode(enUid, "utf-8"));
            Settings.System.putString(act.getContentResolver(), successUid, URLEncoder.encode(enUid, "utf-8"));
            editor.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //取出帳號
    public static String GetaccountString(Activity act)
    {
        //取得資料
        String getaccountString ;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(act);
        getaccountString = preferences.getString(successaccount, "");//settings.getInt(所對應的key,如果抓不到對應的值要給什麼預設值)

        try {
            if(getaccountString.compareTo("")==0){
                getaccountString = Settings.System.getString(act.getContentResolver(), successaccount);
            }
            if(getaccountString == null){
                getaccountString = "";
            }
            getaccountString = URLDecoder.decode(getaccountString, "utf-8");
            String decacc = decrypt(getaccountString, "9@a8i7Az");
            return decacc;
        } catch (Exception e) {

        }
        return "";
    }

    //取出密碼
    public static String GetpasswordString(Activity act)
    {
        //取得資料
        String getpasswordString ;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(act);
        getpasswordString = preferences.getString(successpassword, "");//settings.getInt(所對應的key,如果抓不到對應的值要給什麼預設值)
        try {
            if(getpasswordString.compareTo("")==0){
                getpasswordString = Settings.System.getString(act.getContentResolver(), successpassword);
            }
            if(getpasswordString == null){
                getpasswordString = "";
            }
            getpasswordString = URLDecoder.decode(getpasswordString, "utf-8");
            String decpwd = decrypt(getpasswordString, "9@a8i7Az");
            return decpwd;
        } catch (Exception e) {
        }
        return "";
    }

    // 取出Uid
    public static String getUserUid(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userUid = preferences.getString(successUid, "");

        try {
            if(userUid.compareTo("")==0){
                userUid = Settings.System.getString(context.getContentResolver(), successUid);
            }
            if(userUid == null){
                userUid = "";
            }
            userUid = URLDecoder.decode(userUid, "utf-8");
            String realUid = decrypt(userUid, "9@a8i7Az");
            return realUid;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    private final static String characterEncoding = "UTF-8";
    private final static String cipherTransformation = "AES/CBC/PKCS5Padding";
    private final static String aesEncryptionAlgorithm = "AES";

    public static  byte[] decrypt(byte[] cipherText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
        cipherText = cipher.doFinal(cipherText);
        return cipherText;
    }

    public static byte[] encrypt(byte[] plainText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        plainText = cipher.doFinal(plainText);
        return plainText;
    }

    private static byte[] getKeyBytes(String key) throws UnsupportedEncodingException{
        byte[] keyBytes= new byte[16];
        byte[] parameterKeyBytes= key.getBytes(characterEncoding);
        System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
        return keyBytes;
    }

    /// <summary>
    /// Encrypts plaintext using AES 128bit key and a Chain Block Cipher and returns a base64 encoded string
    /// </summary>
    /// <param name="plainText">Plain text to encrypt</param>
    /// <param name="key">Secret key</param>
    /// <returns>Base64 encoded string</returns>
    private static String encrypt(String plainText, String key) throws Exception{
        byte[] plainTextbytes = plainText.getBytes(characterEncoding);
        byte[] keyBytes = getKeyBytes(key);
        return Base64.encodeToString(encrypt(plainTextbytes,keyBytes, keyBytes), Base64.DEFAULT);
    }

    /// <summary>
    /// Decrypts a base64 encoded string using the given key (AES 128bit key and a Chain Block Cipher)
    /// </summary>
    /// <param name="encryptedText">Base64 Encoded String</param>
    /// <param name="key">Secret Key</param>
    /// <returns>Decrypted String</returns>
    private static String decrypt(String encryptedText, String key) throws Exception{
        byte[] cipheredBytes = Base64.decode(encryptedText, Base64.DEFAULT);
        byte[] keyBytes = getKeyBytes(key);
        return new String(decrypt(cipheredBytes, keyBytes, keyBytes), characterEncoding);
    }

}
