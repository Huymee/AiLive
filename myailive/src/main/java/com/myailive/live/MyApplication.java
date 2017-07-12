package com.myailive.live;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.myailive.live.util.CrashHandler;
import com.myailive.live.util.NetworkUtil;
import com.ucloud.ulive.UStreamingContext;

import org.xutils.x;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * 应用程序
 */

public class MyApplication extends Application {
    public static MyApplication instance;
    public static String internetIp;
    public static boolean isLogin = false;
    public static boolean isRelease = false;
    public static int loginType=0;
    public static final int TYPE_NOT_LOGIN=0;
    public static final int TYPE_LOGIN=1;
    public static final int TYPE_NOT_ONLINE=2;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //注册xUtils
        x.Ext.init(this);
        NetworkUtil.getInterNetIp();
        //如果程序已经发布
        if (isRelease) {
            //告诉框架，出了异常没加catch，去执行crashHandler
            CrashHandler crashHandler = new CrashHandler(this);
            Thread.setDefaultUncaughtExceptionHandler(crashHandler);
        }
        UStreamingContext.init(getApplicationContext(), "publish3-key");
        //初始化Bmob云
        Bmob.initialize(this, "3df2dbc5f727c6e7e29b991c0da57a67", "Bmob");

    }



}
