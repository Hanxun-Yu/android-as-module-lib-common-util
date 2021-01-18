package org.yuhanxun.libcommonutil.log;

import android.util.Log;

/**
 * author: wsyuhx
 * created on: 2021/1/7 17:13
 * description:
 */
public class AndroidLog implements ILog {

    private String TAG;
    private final String TAG_SUFFIX_SEPARATE = "_";

    public static String getTagSuffix() {
        return TAG_SUFFIX;
    }

    public static void setTagSuffix(String tagSuffix) {
        TAG_SUFFIX = tagSuffix;
    }

    private static String TAG_SUFFIX = null;

    public AndroidLog(Object tag) {
        this.TAG = tag.getClass().getSimpleName();
    }

    public String getTAG() {
        return TAG;
    }

    private String getTag() {
        return getTagSuffix() == null ? TAG : TAG + TAG_SUFFIX_SEPARATE + getTagSuffix();
    }

    @Override
    public void v(String msg) {
        Log.v(getTag(), msg);
    }

    @Override
    public void v(String msg, Throwable tr) {
        Log.v(getTag(), msg, tr);
    }

    @Override
    public void e(String msg) {
        Log.e(getTag(), msg);
    }

    @Override
    public void e(String msg, Throwable tr) {
        Log.e(getTag(), msg, tr);
    }

    @Override
    public void w(String msg) {
        Log.w(getTag(), msg);
    }

    @Override
    public void w(String msg, Throwable tr) {
        Log.w(getTag(), msg, tr);
    }

    @Override
    public void d(String msg) {
        Log.d(getTag(), msg);
    }

    @Override
    public void d(String msg, Throwable tr) {
        Log.d(getTag(), msg, tr);

    }

    @Override
    public void i(String msg) {
        Log.i(getTag(), msg);
    }

    @Override
    public void i(String msg, Throwable tr) {
        Log.i(getTag(), msg, tr);
    }
}
