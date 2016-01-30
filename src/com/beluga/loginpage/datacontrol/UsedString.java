package com.beluga.loginpage.datacontrol;

import android.content.Context;

import com.beluga.R;


/**
 * Created by deskuser on 2015/10/6.
 */
public class UsedString {
   
    public static String getLoginstring(Context context, int id){
        String LoginReturn = "";
        switch(id){
            case -1:
                LoginReturn = context.getResources().getString(R.string.Wrong_AppID_Type);
                break;
            case -2:
                LoginReturn = context.getResources().getString(R.string.Ac_Or_Pwd_Length_Err_Type);
                break;
            case -4:
                LoginReturn = context.getResources().getString(R.string.Ac_Or_Pwd_Err_Type);
                break;
            case -10:
                LoginReturn = context.getResources().getString(R.string.Ac_Has_Been_Baned_Type);
                break;
            case -11:
                LoginReturn = context.getResources().getString(R.string.Ac_Has_Been_Disabled_Type);
                break;
            case 1:
                LoginReturn = context.getResources().getString(R.string.Success_Login_Type);
                break;
            case -97:
                LoginReturn = context.getResources().getString(R.string.Checksum_Err_Type);
                break;
            case -98:
                LoginReturn = context.getResources().getString(R.string.System_Err_Type);
                break;
            case -99:
                LoginReturn = context.getResources().getString(R.string.Program_Err_Type);
                //LoginReturn = "蝔��航炊";
                break;
        }
        return LoginReturn;
    }
    
    public static String getFacebookLoginstring(Context context, int id){
        String facebookLoginReturn = "";
        switch(id){
            case 1:
            	facebookLoginReturn = context.getResources().getString(R.string.FB_Login_Success);
              break;
        }
        return facebookLoginReturn;
    }
    public static String getFastRegistrationGenerateString(Context context, int id){
        String FastRegisGenString = "";
        switch(id){
            case -1:
                FastRegisGenString = context.getResources().getString(R.string.Wrong_AppID_Type);
                //FastRegisGenString =  "appid�航炊";
                break;
            case -2:
                FastRegisGenString =  context.getResources().getString(R.string.Write_DB_failed_Type);
                //FastRegisGenString =  "蝟餌絞�航炊嚗B撖怠憭望�";
                break;
            case -97:
                FastRegisGenString = context.getResources().getString(R.string.Checksum_Err_Type);
                //FastRegisGenString =  "瑼Ｘ蝣潮隤�;
                break;
            case -98://
                FastRegisGenString = context.getResources().getString(R.string.System_Err_Type);
                //FastRegisGenString =  "蝟餌絞�航炊";
                break;
            case -99://
                FastRegisGenString = context.getResources().getString(R.string.Program_Err_Type);
                //FastRegisGenString =  "蝔��航炊";
                break;
            case 1:
                FastRegisGenString = context.getResources().getString(R.string.Success_Quick_Reg_Type);
                //FastRegisString = "敹恍�閮餃���";
                break;

        }
        return FastRegisGenString;
    }
    public static String getFastRegistrationString(Context context, int id){
        String FastRegisString = "";
        switch(id){
            case 1:
                FastRegisString = context.getResources().getString(R.string.Success_Quick_Reg_Type);
                //FastRegisString = "敹恍�閮餃���";
                break;
            case -1:
                //FastRegisString = "appid�航炊" ;
                FastRegisString = context.getResources().getString(R.string.Wrong_AppID_Type);
                break;
            case -2:
                FastRegisString = context.getResources().getString(R.string.Pwd_Length_Err_Type);
                //FastRegisString = "撖Ⅳ�瑕漲�航炊,隢身摰�雿隞乩��望��詨�" ;
                break;
            case -97://
                //FastRegisString = "瑼Ｘ蝣潮隤�;
                FastRegisString = context.getResources().getString(R.string.Checksum_Err_Type);
                break;
            case -98://
                //FastRegisString = "蝟餌絞�航炊";
                FastRegisString = context.getResources().getString(R.string.System_Err_Type);
                break;
            case -99://
                //FastRegisString = "蝔��航炊";
                FastRegisString = context.getResources().getString(R.string.Program_Err_Type);
                break;

        }
        return FastRegisString;
    }
    public static String getRegisterString(Context context, int id){
        String RegisString = "";
        switch (id) {
            case -1:
                RegisString = context.getResources().getString(R.string.Wrong_AppID_Type);
                //RegisString = "appid�航炊";
                break;
            case -2:
                RegisString = context.getResources().getString(R.string.Ac_Or_Pwd_Length_Err_Type);
                //RegisString = "撣唾��瑕漲�航炊��蝣潮摨阡隤�;
                break;
            case -3:
                RegisString = context.getResources().getString(R.string.Pwd_Length_Err_Type);
                //RegisString = "撖Ⅳ�瑕漲�航炊";
                break;
            case -4://
                RegisString = context.getResources().getString(R.string.Ac_Has_Been_Used_Type);
                //RegisString = "撣唾�撌脩�雿輻��;
                break;
            case -6://
                RegisString = context.getResources().getString(R.string.Ac_With_Illegal_Char_Type);
                //RegisString = "撣唾���瘜���;
                break;
            case -7://
                RegisString = context.getResources().getString(R.string.Pwd_With_Illegal_Char_Type);
                //RegisString = "撖Ⅳ��瘜���;
                break;
            case 1:
                RegisString = context.getResources().getString(R.string.Success_Register_Type);
                //RegisString = "閮餃���";
                break;
            case -97://
                RegisString = context.getResources().getString(R.string.Checksum_Err_Type);
                //RegisString = "瑼Ｘ蝣潮隤�;
                break;
            case -98://
                RegisString = context.getResources().getString(R.string.System_Err_Type);
                //RegisString = "蝟餌絞�航炊";
                break;
            case -99://
                RegisString = context.getResources().getString(R.string.Program_Err_Type);
                //RegisString = "蝔��航炊";
                break;
            case -102:
                RegisString = context.getResources().getString(R.string.Data_Analysis_Err_Type);
                //RegisString = "鞈����航炊";
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
                //Changepwdstr = "appid�航炊";
                break;
            case -2:
                Changepwdstr = context.getResources().getString(R.string.Ac_Or_Pwd_Length_Err_Type);
                //Changepwdstr =  "撣唾���撖Ⅳ�撖Ⅳ�瑕漲�航炊";
                break;
            case -3:
                Changepwdstr = context.getResources().getString(R.string.Ac_Or_Pwd_Err_Type);
                //Changepwdstr = "撣唾���蝣潮隤�;
                break;
            case -4:
                Changepwdstr = context.getResources().getString(R.string.New_Pwd_Length_Err_Type);
                //Changepwdstr = "�啣�蝣潮摨阡隤�;
                break;
            case -5:
                Changepwdstr = context.getResources().getString(R.string.Change_Pwd_Or_Contact_Customer_Service_Type);
                //Changepwdstr =  "撖Ⅳ�航炊,隢���乩�甈∩誑敺�靽格撖Ⅳ�晾摰Ｘ�撠";
                break;
            case -6:
                Changepwdstr = context.getResources().getString(R.string.Ac_With_Illegal_Char_Type);
                //Changepwdstr = "撣唾���瘜���;
                break;
            case -7:
                Changepwdstr = context.getResources().getString(R.string.Pwd_With_Illegal_Char_Type);
                //Changepwdstr = "撖Ⅳ��瘜���;
                break;
            case -8:
                Changepwdstr = context.getResources().getString(R.string.New_Pwd_With_Illegal_Char_Type);
                //Changepwdstr =  "�啣�蝣潭���摮�";
                break;
            case 1:
                Changepwdstr = context.getResources().getString(R.string.Success_Change_Pwd_Type);
                //Changepwdstr =  "靽格��";
                break;
            case -97:
                Changepwdstr = context.getResources().getString(R.string.Checksum_Err_Type);
                //Changepwdstr =  "瑼Ｘ蝣潮隤�;
                break;
            case -98:
                Changepwdstr = context.getResources().getString(R.string.System_Err_Type);
                //Changepwdstr =  "蝟餌絞�航炊";
                break;
        }
        return Changepwdstr;
    }
}
