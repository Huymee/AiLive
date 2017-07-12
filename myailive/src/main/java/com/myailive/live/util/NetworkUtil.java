package com.myailive.live.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


import com.myailive.live.MyApplication;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class NetworkUtil {


    public static void getInterNetIp() {
        //连谁
        String url = "http://1212.ip138.com/ic.asp";
        //选择框架
        RequestParams requestParams = new RequestParams(url);
        //发数据
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //回调
                long threadId = Thread.currentThread().getId();
                int start = result.indexOf("[");
                int end = result.indexOf("]");
                MyApplication.internetIp = result.substring(start + 1, end);
                Log.i("", "");


            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
        long threadId = Thread.currentThread().getId();

    }

    /**
     * 互联网 internet
     * 局域网 intranet
     *
     * @return
     */
    public static String getIntranetIp(Context context) {
        String ip = "";
        //连接服务
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            //判断当前网络是wifi还是mobile
            NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo
                    (ConnectivityManager.TYPE_WIFI);
            if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
                //得wifiip
                WifiManager wifiManager = (WifiManager)
                        context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = (WifiInfo) wifiManager.getConnectionInfo();
                //把ip转成字符串
                int intIp = wifiInfo.getIpAddress();
                //真正的ip是172.17.100.15
                //15 100 17 172
                // 3  2  1  0
                String ip0 = (intIp & 0xFF) + "";
                String ip1 = (intIp >> 8 & 0xFF) + "";
                String ip2 = (intIp >> 16 & 0xFF) + "";
                String ip3 = (intIp >> 24 & 0xFF) + "";
                ip = ip0 + "." + ip1 + "." + ip2 + "." + ip3;
                Log.i("networkUtil", intIp + "");
            }
        }
        return ip;
    }
}
