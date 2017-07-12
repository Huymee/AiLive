package com.myailive.live.util;

import android.os.Build;


import com.myailive.live.MyApplication;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by pjy on 2017/5/16.
 */

public class ExceptionUtil {
    public static void handleException(Throwable e) {
        if (MyApplication.isRelease == false) {
            //开发中
            e.printStackTrace();
        } else {
            //把异常信息转成字符串
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            String str = stringWriter.toString();

            //得手机厂商
            String manuFacturer = Build.MANUFACTURER;
            //得手机型号uhj
            String model = Build.MODEL;
            //得手机的操作系统版本
            int sdk_int = Build.VERSION.SDK_INT;
            //得ip
            String intranetIp = NetworkUtil.getIntranetIp(MyApplication.instance);
            //联网发送
            str = manuFacturer + "," + model + "," + sdk_int + "," + intranetIp + "," + MyApplication.internetIp + "," + str;
        }

    }
}
