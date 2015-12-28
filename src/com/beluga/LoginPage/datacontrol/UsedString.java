package com.beluga.LoginPage.datacontrol;

import android.content.Context;

import com.beluga.R;


/**
 * Created by deskuser on 2015/10/6.
 */
public class UsedString {
    /**
     * 登入後的回傳訊息
     * */
    public static String getLoginstring(Context context, int id){
        String LoginReturn = "";
        switch(id){
            case -1:
                LoginReturn = context.getResources().getString(R.string.Wrong_AppID_Type);
                //LoginReturn = "appid錯誤";
                break;
            case -2:
                LoginReturn = context.getResources().getString(R.string.Ac_Or_Pwd_Length_Err_Type);
                //LoginReturn = "帳號或密碼長度錯誤";
                break;
            case -4:
                LoginReturn = context.getResources().getString(R.string.Ac_Or_Pwd_Err_Type);
                //LoginReturn = "帳號或密碼錯誤";
                break;
            case -10:
                LoginReturn = context.getResources().getString(R.string.Ac_Has_Been_Baned_Type);
                //LoginReturn = "帳號已被停權(違反遊戲規章)";
                break;
            case -11:
                LoginReturn = context.getResources().getString(R.string.Ac_Has_Been_Disabled_Type);
                //LoginReturn = "帳號已經停用";
                break;
            case 1:
                //LoginReturn = "登入成功";
                LoginReturn = context.getResources().getString(R.string.Success_Login_Type);
                break;
            case -97:
                LoginReturn = context.getResources().getString(R.string.Checksum_Err_Type);
                //LoginReturn = "檢查碼錯誤";
                break;
            case -98:
                LoginReturn = context.getResources().getString(R.string.System_Err_Type);
                //LoginReturn = "系統錯誤";
                break;
            case -99:
                LoginReturn = context.getResources().getString(R.string.Program_Err_Type);
                //LoginReturn = "程式錯誤";
                break;
        }
        return LoginReturn;
    }
    /**
     * 取得快速註冊時的帳密後回傳訊息
     * */
    public static String getFastRegistrationGenerateString(Context context, int id){
        String FastRegisGenString = "";
        switch(id){
            case -1:
                FastRegisGenString = context.getResources().getString(R.string.Wrong_AppID_Type);
                //FastRegisGenString =  "appid錯誤";
                break;
            case -2:
                FastRegisGenString =  context.getResources().getString(R.string.Write_DB_failed_Type);
                //FastRegisGenString =  "系統錯誤，DB寫入失敗";
                break;
            case -97:
                FastRegisGenString = context.getResources().getString(R.string.Checksum_Err_Type);
                //FastRegisGenString =  "檢查碼錯誤";
                break;
            case -98://
                FastRegisGenString = context.getResources().getString(R.string.System_Err_Type);
                //FastRegisGenString =  "系統錯誤";
                break;
            case -99://
                FastRegisGenString = context.getResources().getString(R.string.Program_Err_Type);
                //FastRegisGenString =  "程式錯誤";
                break;
            case 1:
                FastRegisGenString = context.getResources().getString(R.string.Success_Quick_Reg_Type);
                //FastRegisString = "快速註冊成功";
                break;

        }
        return FastRegisGenString;
    }
    /**
     * 進行快速註冊後回傳訊息
     * */
    public static String getFastRegistrationString(Context context, int id){
        String FastRegisString = "";
        switch(id){
            case 1:
                FastRegisString = context.getResources().getString(R.string.Success_Quick_Reg_Type);
                //FastRegisString = "快速註冊成功";
                break;
            case -1:
                //FastRegisString = "appid錯誤" ;
                FastRegisString = context.getResources().getString(R.string.Wrong_AppID_Type);
                break;
            case -2:
                FastRegisString = context.getResources().getString(R.string.Pwd_Length_Err_Type);
                //FastRegisString = "密碼長度錯誤,請設定6位數以上英文數字" ;
                break;
            case -97://
                //FastRegisString = "檢查碼錯誤";
                FastRegisString = context.getResources().getString(R.string.Checksum_Err_Type);
                break;
            case -98://
                //FastRegisString = "系統錯誤";
                FastRegisString = context.getResources().getString(R.string.System_Err_Type);
                break;
            case -99://
                //FastRegisString = "程式錯誤";
                FastRegisString = context.getResources().getString(R.string.Program_Err_Type);
                break;

        }
        return FastRegisString;
    }
    /**
     * 進行註冊後回傳訊息
     * */
    public static String getRegisterString(Context context, int id){
        String RegisString = "";
        switch (id) {
            case -1:
                RegisString = context.getResources().getString(R.string.Wrong_AppID_Type);
                //RegisString = "appid錯誤";
                break;
            case -2:
                RegisString = context.getResources().getString(R.string.Ac_Or_Pwd_Length_Err_Type);
                //RegisString = "帳號長度錯誤或密碼長度錯誤";
                break;
            case -3:
                RegisString = context.getResources().getString(R.string.Pwd_Length_Err_Type);
                //RegisString = "密碼長度錯誤";
                break;
            case -4://
                RegisString = context.getResources().getString(R.string.Ac_Has_Been_Used_Type);
                //RegisString = "帳號已經使用過";
                break;
            case -6://
                RegisString = context.getResources().getString(R.string.Ac_With_Illegal_Char_Type);
                //RegisString = "帳號有非法字元";
                break;
            case -7://
                RegisString = context.getResources().getString(R.string.Pwd_With_Illegal_Char_Type);
                //RegisString = "密碼有非法字元";
                break;
            case 1:
                RegisString = context.getResources().getString(R.string.Success_Register_Type);
                //RegisString = "註冊成功";
                break;
            case -97://
                RegisString = context.getResources().getString(R.string.Checksum_Err_Type);
                //RegisString = "檢查碼錯誤";
                break;
            case -98://
                RegisString = context.getResources().getString(R.string.System_Err_Type);
                //RegisString = "系統錯誤";
                break;
            case -99://
                RegisString = context.getResources().getString(R.string.Program_Err_Type);
                //RegisString = "程式錯誤";
                break;
            case -102:
                RegisString = context.getResources().getString(R.string.Data_Analysis_Err_Type);
                //RegisString = "資料分析錯誤";
                break;
        }

        return RegisString;
    }
    public static String getChangePasswordString(Context context, int id){
        String Changepwdstr = "";
        switch(id)
        {
            case -1:
                Changepwdstr = context.getResources().getString(R.string.Wrong_AppID_Type);
                //Changepwdstr = "appid錯誤";
                break;
            case -2:
                Changepwdstr = context.getResources().getString(R.string.Ac_Or_Pwd_Length_Err_Type);
                //Changepwdstr =  "帳號或舊密碼或新密碼長度錯誤";
                break;
            case -3:
                Changepwdstr = context.getResources().getString(R.string.Ac_Or_Pwd_Err_Type);
                //Changepwdstr = "帳號或密碼錯誤";
                break;
            case -4:
                Changepwdstr = context.getResources().getString(R.string.New_Pwd_Length_Err_Type);
                //Changepwdstr = "新密碼長度錯誤";
                break;
            case -5:
                Changepwdstr = context.getResources().getString(R.string.Change_Pwd_Or_Contact_Customer_Service_Type);
                //Changepwdstr =  "密碼錯誤,請成功登入一次以後再修改密碼或洽客服專員";
                break;
            case -6:
                Changepwdstr = context.getResources().getString(R.string.Ac_With_Illegal_Char_Type);
                //Changepwdstr = "帳號有非法字元";
                break;
            case -7:
                Changepwdstr = context.getResources().getString(R.string.Pwd_With_Illegal_Char_Type);
                //Changepwdstr = "密碼有非法字元";
                break;
            case -8:
                Changepwdstr = context.getResources().getString(R.string.New_Pwd_With_Illegal_Char_Type);
                //Changepwdstr =  "新密碼有非法字元";
                break;
            case 1:
                Changepwdstr = context.getResources().getString(R.string.Success_Change_Pwd_Type);
                //Changepwdstr =  "修改成功";
                break;
            case -97:
                Changepwdstr = context.getResources().getString(R.string.Checksum_Err_Type);
                //Changepwdstr =  "檢查碼錯誤";
                break;
            case -98:
                Changepwdstr = context.getResources().getString(R.string.System_Err_Type);
                //Changepwdstr =  "系統錯誤";
                break;
        }
        return Changepwdstr;
    }
}
