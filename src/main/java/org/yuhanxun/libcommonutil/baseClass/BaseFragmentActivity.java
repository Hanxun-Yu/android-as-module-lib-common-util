package org.yuhanxun.libcommonutil.baseClass;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

/**
 * Created by crashxun on 2015/6/28.
 */
public class BaseFragmentActivity extends FragmentActivity {
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
