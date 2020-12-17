package com.example.utils.view;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * Created by yuhanxun on 2018/3/20.
 */

public class ViewUtil {
    private static final String TAG = "ViewUtil";

    public static int[] getLocationInScreen(View view) {
        int[] location = new int[2] ;
        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        return location;
    }

    public static void setViewWH(View view, int width, int height) {
        if(view.getLayoutParams() != null) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if(width != -1)
                params.width = width;
            if(height != -1)
                params.height = height;
            view.setLayoutParams(params);
        }
    }
    public static void setViewW(View view, int width) {
        setViewWH(view,width,-1);
    }
    public static void setViewH(View view, int height) {
        setViewWH(view,-1,height);
    }


    public static void enableSubControls(ViewGroup viewGroup,boolean flag) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                if (v instanceof Spinner) {
                    Spinner spinner = (Spinner) v;
                    spinner.setClickable(flag);
                    spinner.setEnabled(flag);

                    Log.i(TAG, "A Spinner is unabled");
                } else if (v instanceof ListView) {
                    ((ListView) v).setClickable(flag);
                    ((ListView) v).setEnabled(flag);

                    Log.i(TAG, "A ListView is unabled");
                } else {
                    enableSubControls((ViewGroup) v,flag);
                }
            } else if (v instanceof EditText) {
                ((EditText) v).setEnabled(flag);
                ((EditText) v).setClickable(flag);

                Log.i(TAG, "A EditText is unabled");
            } else if (v instanceof Button) {
                ((Button) v).setEnabled(flag);

                Log.i(TAG, "A Button is unabled");
            } else {
                v.setEnabled(flag);
            }
        }
    }
}
