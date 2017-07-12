package com.myailive.live.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;

import com.myailive.live.MyApplication;
import com.myailive.live.activity.FlashActivity;


/**
 * 异常收集处理类
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    //不能用debug方式启动
    MyApplication myApplication;

    public CrashHandler(MyApplication myApplication) {
        this.myApplication = myApplication;
    }

    //程序中出了异常，没加catch
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //发送信息到服务器
        ExceptionUtil.handleException(ex);
        //用toast显示自动重启
        new Thread() {
            @Override
            public void run() {
                //toast用到了队列，子线程没有looper，先准备一个looper
                Looper.prepare();
                Toast.makeText(myApplication, "抱歉，程序将重启", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        //自动重启
        Intent startMainActivity = new Intent(myApplication, FlashActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(myApplication, 100, startMainActivity, 0);

        //2秒后执行自动重启
        //alarm定时
        AlarmManager alarmManager = (AlarmManager)
                myApplication.getSystemService(Context.ALARM_SERVICE);

        //RTC:锁屏，不执行
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, pendingIntent);
        //为了显示toast
        try {
            Thread.currentThread().sleep(1000);
        } catch (Exception e) {
        }
        //结束当前进程
        Process.killProcess(Process.myPid());
    }
}
