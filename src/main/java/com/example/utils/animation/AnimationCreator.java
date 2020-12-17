package com.example.utils.animation;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by yuhanxun on 2018/3/19.
 */

public class AnimationCreator {
    public static Animation getAlphaAnim(float srcAlpha, float tarAlpha, int duration, boolean fillAfter) {
        Animation ret = new AlphaAnimation(srcAlpha, tarAlpha);
        ret.setFillAfter(fillAfter);
        ret.setDuration(duration);
        return ret;
    }

    public static Animation getTranslateAnim(float fromXDelta, float toXDelta, float fromYDelta,float toYDelta,int duration,boolean fillAfter) {
        Animation ret = new TranslateAnimation(fromXDelta, toXDelta,fromYDelta,toYDelta);
        ret.setFillAfter(fillAfter);
        ret.setDuration(duration);
        return ret;
    }



    public static Animation getScaleAnim(float fromX, float toX, float fromY, float toY,
                                         float pivotXValue, float pivotYValue, int duration,
                                         boolean fillAfter, Interpolator interpolator) {
        Animation ret = new ScaleAnimation(fromX, toX, fromY, toY,
                Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF, pivotYValue);
        ret.setFillAfter(fillAfter);
        ret.setDuration(duration);
        if (interpolator != null)
            ret.setInterpolator(interpolator);
        return ret;
    }
}
