package org.yuhanxun.libcommonutil.baseClass;

import android.util.Log;

public  abstract class BaseDispatcherHandler {
    String TAG = "dispathcher_xunxun";

    public void onCommonSuccess(String apiUrl, String resJson) {
        Log.d(TAG, "onCommonSuccess API_URL=" + apiUrl + "\n" + "JSON=" + resJson);
        onSuccess(apiUrl,resJson);
    }

    public void onCommonFail(String apiUrl, Throwable throwable) {
        String msg = null;
        if (throwable != null) {
            Log.d(TAG, "onCommonFail API_URL=" + apiUrl, throwable);
            throwable.printStackTrace();
            msg = throwable.getMessage();
        }
        onFail(msg);
    }


    protected abstract void onFail(String msg);

    protected abstract void onSuccess(String apiurl, String json);
}