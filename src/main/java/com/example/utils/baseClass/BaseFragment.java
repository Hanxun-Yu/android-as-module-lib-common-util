package com.example.utils.baseClass;


import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by crashxun on 2015/6/28.
 */
public class BaseFragment extends Fragment {
    protected String TAG;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
    }
    protected void setLogFilter(String filter) {
        TAG = TAG+"_"+filter;
    }
}
