package com.example.utils.baseClass;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by yuhanxun on 16/1/8.
 */
public class BaseService extends Service {
    protected String TAG;

    @Override
    public void onCreate() {
        super.onCreate();
        TAG = getClass().getSimpleName();
    }
    protected void setLogFilter(String filter) {
        TAG = TAG+"_"+filter;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
