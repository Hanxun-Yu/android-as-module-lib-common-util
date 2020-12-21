package org.yuhanxun.libcommonutil.baseClass;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by yuhanxun on 15/7/26.
 */
public class BaseActivity extends Activity {
    protected String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
    }

    protected void setLogFilter(String filter) {
        TAG = TAG+"_"+filter;
    }

}
