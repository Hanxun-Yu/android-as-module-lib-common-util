package org.yuhanxun.libcommonutil.device;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by dbstar-mac on 2017/6/16.
 */

public class ScreenUtil {
    public static int[] getScreenWH(Context context) {
        // 获取屏幕的默认分辨率
//        Display display = act.getWindowManager().getDefaultDisplay();
//        int[] ret = new int[2];
//        ret[0] = display.getWidth();
//        ret[1] = display.getHeight();
//        return ret;

                int[] ret = new int[2];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        ret[0] = width;
        ret[1] = height;
        return ret;
    }
}
