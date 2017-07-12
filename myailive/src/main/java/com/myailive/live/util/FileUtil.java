package com.myailive.live.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * 文件写入和读取工具
 */

public class FileUtil {

    public static byte[] readFile(String string) {
        byte[] fileData = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(string);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                ExceptionUtil.handleException(e);
            }
        }
        return fileData;
    }

    public static String writeToSdcard(String path, String fileName, byte[] audioData) {
        FileOutputStream fileOutputStream;
        try {
            //判断SD卡能不能用
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File file = new File(path, fileName);
                if (file.exists()) {
                    long time = Calendar.getInstance().getTimeInMillis();
                    fileName = fileName + "-" + time;
                    file.delete();
                }
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(audioData);
                fileOutputStream.flush();
            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        } finally {
            try {
                fileOutputStream = null;
                fileOutputStream.close();
            } catch (IOException e) {
                ExceptionUtil.handleException(e);
            }
        }
        return fileName;
    }
}
