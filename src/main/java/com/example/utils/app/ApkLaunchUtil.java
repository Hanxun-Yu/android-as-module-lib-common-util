package com.example.utils.app;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xunxun on 2015/8/3.
 */
public class ApkLaunchUtil {
    public static void startActivity(Context src, Class target) {
        startActivity(src, target, -1);
    }

    public static void startActivity(Context src, Class target, Bundle bundle) {
        startActivity(src, target, -1, bundle);
    }

    public static void startActivity(Context src, Class target, int flag) {
        startActivity(src, target, flag, null);
    }

    public static void startActivity(Context src, Class target, int flag, Bundle bundle) {
        Intent intent = getIntent(src, target);
        if (flag != -1) {
            intent.addFlags(flag);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        doLaunch(src, intent);
    }

    public static void startActivity(Context con, String packageName, String activityName) {
        startActivity(con, packageName, activityName, -1, null);
    }

    public static void startActivity(Context con, String packageName, String activityName, Bundle bundle) {
        startActivity(con, packageName, activityName, -1, bundle);
    }

    public static void launchApkNewTask(Context con, String packageName) {
        startActivity(con, packageName, Intent.FLAG_ACTIVITY_NEW_TASK, null);
    }

    public static void launchApkNewTask(Context con, String packageName, Bundle bundle) {
        startActivity(con, packageName, Intent.FLAG_ACTIVITY_NEW_TASK, bundle);
    }

    public static void launchApkNewTask(Context con, String packageName, String activityName) {
        startActivity(con, packageName, activityName, Intent.FLAG_ACTIVITY_NEW_TASK, null);
    }

    public static void launchApkNewTask(Context con, String packageName, String activityName, Bundle bundle) {
        startActivity(con, packageName, activityName, Intent.FLAG_ACTIVITY_NEW_TASK, bundle);
    }

    public static void startActivity(Context con, String packageName, int flag, Bundle bundle) {
        Intent intent = getIntent(con, packageName);
        if (intent != null && flag != -1) {
            intent.setFlags(flag);
        }
        if (intent != null && bundle != null) {
            intent.putExtras(bundle);
        }
        doLaunch(con, intent);
    }

    public static void startActivity(Context con, String packageName, String activityName, int flag, Bundle bundle) {
        Intent intent = getIntent(packageName, activityName);
        if (intent != null && flag != -1) {
            intent.addFlags(flag);
        }
        if (intent != null && bundle != null) {
            intent.putExtras(bundle);
        }
        doLaunch(con, intent);
    }

    private static void doLaunch(Context context, Intent intent) {
        if (intent != null) {
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "该功能未开放，敬请期待", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "该功能未开放，敬请期待", Toast.LENGTH_SHORT).show();
        }
    }

    private static Intent getIntent(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return intent;
    }

    private static Intent getIntent(String packageName, String activityName) {
        Intent mIntent = new Intent();
        ComponentName comp = new ComponentName(packageName, activityName);
        mIntent.setComponent(comp);
        mIntent.setAction("android.intent.action.VIEW");
        return mIntent;
    }

    private static Intent getIntent(Context src, Class target) {
        return new Intent(src, target);
    }

    public void launcherAPKFromHistory(Context con, String packageName) {
        List<HashMap<String, Object>> ret = getRecentTask(con);
        if (ret != null && !ret.isEmpty()) {
            for (int i = 0; i < ret.size(); i++) {
                HashMap<String, Object> map = ret.get(i);
                if (map.get("packageName").equals(packageName)) {
                    Intent intent = (Intent) map.get("tag");
                    intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                    con.startActivity(intent);
                }
            }
        }
    }


    private List<HashMap<String, Object>> getRecentTask(Context context) {
        //用来存放每一个recentApplication的信息，我们这里存放应用程序名，应用程序图标和intent。
        List<HashMap<String, Object>> appInfos = new ArrayList<HashMap<String, Object>>();

        int MAX_RECENT_TASKS = 10; // allow for some discards
        int repeatCount = 10;// 保证上面两个值相等,设定存放的程序个数

        /* 每次加载必须清空list中的内容 */
        appInfos.removeAll(appInfos);
        // 得到包管理器和activity管理器
        final PackageManager pm = context.getPackageManager();
        final ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        // 从ActivityManager中取出用户最近launch过的 MAX_RECENT_TASKS + 1 个，以从早到晚的时间排序，
        // 注意这个 0x0002,它的值在launcher中是用ActivityManager.RECENT_IGNORE_UNAVAILABLE
        // 但是这是一个隐藏域，因此我把它的值直接拷贝到这里
        final List<ActivityManager.RecentTaskInfo> recentTasks = am
                .getRecentTasks(MAX_RECENT_TASKS + 1, 0x0002);

        // 这个activity的信息是我们的launcher
        ActivityInfo homeInfo = new Intent(Intent.ACTION_MAIN).addCategory(
                Intent.CATEGORY_HOME).resolveActivityInfo(pm, 0);
        int numTasks = recentTasks.size();
        for (int i = 0; i < numTasks && (i < MAX_RECENT_TASKS); i++) {
            HashMap<String, Object> singleAppInfo = new HashMap<String, Object>();// 当个启动过的应用程序的信息
            final ActivityManager.RecentTaskInfo info = recentTasks.get(i);

            Intent intent = new Intent(info.baseIntent);
            if (info.origActivity != null) {
                intent.setComponent(info.origActivity);
            }
            /**
             * 如果找到是launcher，直接continue，后面的appInfos.add操作就不会发生了
             */
            if (homeInfo != null) {
                if (homeInfo.packageName.equals(intent.getComponent()
                        .getPackageName())
                        && homeInfo.name.equals(intent.getComponent()
                        .getClassName())) {
                    MAX_RECENT_TASKS = MAX_RECENT_TASKS + 1;
                    continue;
                }
            }
            // 设置intent的启动方式为 创建新task()【并不一定会创建】
            intent.setFlags((intent.getFlags() & ~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            // 获取指定应用程序activity的信息(按我的理解是：某一个应用程序的最后一个在前台出现过的activity。)
            final ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
            if (resolveInfo != null) {
                final ActivityInfo activityInfo = resolveInfo.activityInfo;
                final String title = activityInfo.loadLabel(pm).toString();
                Drawable icon = activityInfo.loadIcon(pm);

                if (title != null && title.length() > 0 && icon != null) {
                    singleAppInfo.put("title", title);
                    singleAppInfo.put("icon", icon);
                    singleAppInfo.put("tag", intent);
                    singleAppInfo.put("packageName", activityInfo.packageName);
                    appInfos.add(singleAppInfo);
                }
            }
        }
        return appInfos;
    }
}
