package com.example.utils.log;

import android.util.Log;

/**
 * Created by yuhanxun
 * 2018/5/25
 */
public class NormalLog implements ILog{
    @Override
    public void e(String tag, String content,Throwable tr) {
        Log.e(tag,content,tr);
    }

    @Override
    public void w(String tag, String content,Throwable tr) {
        Log.w(tag,content,tr);
    }

    @Override
    public void d(String tag, String content,Throwable tr) {
        Log.d(tag,content,tr);
    }

    @Override
    public void i(String tag, String content,Throwable tr) {
        Log.i(tag,content,tr);
    }

    @Override
    public void v(String tag, String content,Throwable tr) {
        Log.v(tag,content,tr);
    }
}
