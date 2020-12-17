package com.example.utils.device;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by yuhanxun on 15/7/29.
 */
public class NetworkStatusChecker {
    public static boolean isNetworkEnable(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();


        boolean Ethernet = false;
        if(con.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET)!=null) {
            Ethernet = con.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).isConnectedOrConnecting();
        }

//        boolean internet =  con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if (wifi || Ethernet) {
            //执行相关操作
            return true;
        } else {
            return false;
        }
    }
}