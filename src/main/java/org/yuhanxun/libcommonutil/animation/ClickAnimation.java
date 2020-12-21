package org.yuhanxun.libcommonutil.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;

public class ClickAnimation {
    public static void animSmallBig(final View view, final Animation.AnimationListener listener) {
        Animation fade = AnimationCreator.getAlphaAnim(1f, 0.4f, 200, true);
        Animation zoomIn = AnimationCreator.getScaleAnim(1f, 0.8f, 1f, 0.8f, 0.5f, 0.5f, 200, false, null);
        AnimationSet animationSet = new AnimationSet(true);
        //添加动画
        animationSet.addAnimation(fade);
        animationSet.addAnimation(zoomIn);
        animationSet.setInterpolator(new OvershootInterpolator());
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (listener != null)
                    listener.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation display = AnimationCreator.getAlphaAnim(0.4f, 1f, 200, true);
                Animation zoomOut = AnimationCreator.getScaleAnim(0.8f, 1f, 0.8f, 1f, 0.5f, 0.5f, 200, false, null);
                AnimationSet animationSet = new AnimationSet(true);
                //添加动画
                animationSet.addAnimation(display);
                animationSet.addAnimation(zoomOut);
                animationSet.setInterpolator(new OvershootInterpolator());
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (listener != null)
                            listener.onAnimationEnd(animation);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animationSet);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animationSet);
    }

    public static void animSmallBig(final View view) {
        animSmallBig(view,null);
    }
}