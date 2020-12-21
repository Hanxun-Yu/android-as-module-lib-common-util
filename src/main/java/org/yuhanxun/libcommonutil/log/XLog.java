package org.yuhanxun.libcommonutil.log;

import android.text.TextUtils;

/**
 * Created by yuhanxun
 * 2018/5/25
 */
public class XLog {
    private final static String SEPARATE = "_";
    private static String defTag = SEPARATE + "XLog";
    private static ILog log = new NormalLog();

    public static void setTag(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            defTag = SEPARATE + tag;
        }
    }

    private static String processTag(String tag) {
        String ret = tag;
        if (!TextUtils.isEmpty(tag) && !tag.contains(defTag)) {
            ret += defTag;
        }
        return ret;
    }


    public static void e(String tag, String content, Throwable tr) {
        log.e(processTag(tag), content, tr);
    }

    public static void w(String tag, String content, Throwable tr) {
        log.w(processTag(tag), content, tr);
    }

    public static void i(String tag, String content, Throwable tr) {
        log.i(processTag(tag), content, tr);
    }

    public static void d(String tag, String content, Throwable tr) {
        log.d(processTag(tag), content, tr);
    }

    public static void v(String tag, String content, Throwable tr) {
        log.v(processTag(tag), content, tr);
    }

    public static void e(String tag, String content) {
        e(tag, content, null);
    }

    public static void w(String tag, String content) {
        w(tag, content, null);
    }

    public static void d(String tag, String content) {
        d(tag, content, null);
    }

    public static void i(String tag, String content) {
        i(tag, content, null);
    }

    public static void v(String tag, String content) {
        v(tag, content, null);
    }


    public static void e(String content) {
        e(defTag, content);
    }

    public static void w(String content) {
        w(defTag, content);
    }

    public static void d(String content) {
        d(defTag, content);
    }

    public static void i(String content) {
        i(defTag, content);
    }

    public static void v(String content) {
        v(defTag, content);
    }
}
