package org.yuhanxun.libcommonutil.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Crashxun dbstar-mac on 2017/1/10.
 */

public class AppStatus {
    public static String getTopAppPackage(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        String packageName = null;
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            packageName = cn.getPackageName();
        }
        return packageName;
    }

    public static String getTopAppActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        String name = null;
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            name = cn.getClassName();
        }
        return name;
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}
