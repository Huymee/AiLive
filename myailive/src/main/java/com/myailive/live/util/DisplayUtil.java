package com.myailive.live.util;

import android.content.Context;

/**
 * Created by 46433 on 17/05/18.
 */

public class DisplayUtil {
    public static int dp2px(Context context,float dp){
        float density=context.getResources().getDisplayMetrics().scaledDensity;
        float px=dp*density;
        //值上加上0.5实现四舍五入
        return (int)(px+0.5);
    }
}
