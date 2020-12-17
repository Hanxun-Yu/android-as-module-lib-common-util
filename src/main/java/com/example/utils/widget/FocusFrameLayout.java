package com.example.utils.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengyuntao on 16/3/21.
 */
public class FocusFrameLayout extends RelativeLayout implements ViewTreeObserver.OnGlobalFocusChangeListener {
    private LayoutParams mFocusLayoutParams;
    private ImageView mFocusView;
    List<MoveMsg> moveMsgsList = new ArrayList<>();
    final String TAG = "FocusLayout";

    private int frameBorderSize;
    private int frameImgID;

    public void setFrameMoveable(boolean moveAnimable) {
        this.frameMoveable = moveAnimable;
    }

    private boolean frameMoveable = true;
    public void setFrameImgSpeed(int frameImgSpeed) {
        this.frameImgSpeed = frameImgSpeed;
    }

    private int frameImgSpeed = 200;

    public void setFrameImg(int id, int borderSize) {
        frameImgID = id;
        frameBorderSize = borderSize;
        this.mFocusView.setImageResource(id);
    }
    public void setFrameImgVisible(boolean visible) {
        if(visible)
            this.mFocusView.setVisibility(View.VISIBLE);
        else
            this.mFocusView.setVisibility(View.INVISIBLE);
    }
    public void setFrameImg(int id) {
        setFrameImg(id,0);
    }



    private OnFocusFrameMoved onFocusFrameMoved;

    public FocusFrameLayout(Context context) {
        super(context);
        init(context);
    }
    public void bindListener(Activity act) {
        //获取根元素
        View mContainerView = act.getWindow().getDecorView();//.findViewById(android.R.id.content);
        //得到整个view树的viewTreeObserver
        ViewTreeObserver viewTreeObserver = mContainerView.getViewTreeObserver();
        //给观察者设置焦点变化监听
        viewTreeObserver.addOnGlobalFocusChangeListener(this);
    }
    public void bindListener(View parent) {
        //得到整个view树的viewTreeObserver
        ViewTreeObserver viewTreeObserver = parent.getViewTreeObserver();
        //给观察者设置焦点变化监听
        viewTreeObserver.addOnGlobalFocusChangeListener(this);
    }

    public FocusFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FocusFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mFocusLayoutParams = new LayoutParams(0, 0);
        this.mFocusView = new ImageView(context);
        this.mFocusView.setPadding(0, 0, 0, 0);
        this.mFocusView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(this.mFocusView, this.mFocusLayoutParams);
    }
    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        Log.d(TAG, "--------------onGlobalFocusChanged------------");
        Log.d(TAG, "oldFocus:"+oldFocus+" newFocus:"+newFocus);
        if(newFocus == null) {
            Log.d(TAG, "newFocus:"+newFocus + " return");
            return;
        }
        int[] newPosi = getLTRB(newFocus);
        if (oldFocus == null) {
            FocusFrameLayout.this.setFocusLocation(newPosi[0], newPosi[1], newPosi[2], newPosi[3]);
        } else {
            if(onFocusFrameMoved != null)
                onFocusFrameMoved.onStart(oldFocus,newFocus);
            if(frameMoveable) {
                int[] oldPosi = getLTRB(oldFocus);
                addAnim(new MoveMsg(oldPosi,newPosi,oldFocus,newFocus));
                if (mFocusView.getAnimation() == null) {
                    startAnim();
                }
            } else {
                FocusFrameLayout.this.setFocusLocation(newPosi[0], newPosi[1], newPosi[2], newPosi[3]);
                if(onFocusFrameMoved != null)
                    onFocusFrameMoved.onEnd(oldFocus,newFocus);
            }
        }

    }

    private int getFocusViewTop() {
        return mFocusLayoutParams.topMargin;
    }
    private int getFocusViewLeft() {
        return mFocusLayoutParams.leftMargin;
    }

    private class MoveMsg {
        int[] startPosi;
        int[] endPosi;
        View startView;
        View endView;

        public MoveMsg(int[] startPosi, int[] endPosi, View startView, View endView) {
            this.startPosi = startPosi;
            this.endPosi = endPosi;
            this.startView = startView;
            this.endView = endView;
        }
    }

    private int[] getLTRB(View v) {
        int[] ret = new int[4];
        Rect viewRect = new Rect();
        v.getGlobalVisibleRect(viewRect);
        correctLocation(viewRect);
        ret[0] = viewRect.left;
        ret[1] = viewRect.top;
        ret[2] = viewRect.right;
        ret[3] = viewRect.bottom;

        Log.d(TAG, "getLTRB l:" + ret[0] + " t:" + ret[1] + " r:" + ret[2] + " b:" + ret[3]);
        Log.d(TAG, "tarView w:" + v.getWidth() + " h:" + v.getHeight());
        return ret;
    }

//    private void layoutToNextView(View nextView) {
//        Rect viewRect = new Rect();
//        nextView.getGlobalVisibleRect(viewRect);
//        correctLocation(viewRect);
//        this.setFocusLocation(
//                viewRect.left - this.mFocusView.getPaddingLeft(),
//                viewRect.top - this.mFocusView.getPaddingTop(),
//                viewRect.right + this.mFocusView.getPaddingRight(),
//                viewRect.bottom + this.mFocusView.getPaddingBottom());
//    }

    private class AnimParams {
        public float scaleX;
        public float scaleY;
        public int toX;
        public int toY;

        public AnimParams(float scaleX, float scaleY, int toX, int toY) {
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.toX = toX;
            this.toY = toY;
        }
    }
    public AnimParams calAnimPamas(int[] oldPosi, int[] newPosi) {
        int old_l = oldPosi[0];
        int old_t = oldPosi[1];
        int old_r = oldPosi[2];
        int old_b = oldPosi[3];
        int new_l = newPosi[0];
        int new_t = newPosi[1];
        int new_r = newPosi[2];
        int new_b = newPosi[3];
        old_l -= frameBorderSize;
        old_t -= frameBorderSize;
        old_r += frameBorderSize;
        old_b += frameBorderSize;

        new_l -= frameBorderSize;
        new_t -= frameBorderSize;
        new_r += frameBorderSize;
        new_b += frameBorderSize;


        int mWidth = mFocusView.getWidth();
        int mHeight = mFocusView.getHeight();

//        Log.d(TAG, "focusImg w:" + mWidth);
//        Log.d(TAG, "focusImg h:" + mHeight);

        float scaleX = (float) (new_r - new_l) / (float) (old_r-old_l);
        float scaleY = (float) (new_b - new_t) / (float) (old_b-old_t);



        //在scrollview中出现一半的情况,oldView的位置将会是滑动后的位置,
        // 所以必须减去(滑动前与滑动后的差),这样焦点框不会滑过头
        int offsetY = getFocusViewTop()-old_t;
        int offsetX = getFocusViewLeft()-old_l;
        Log.d(TAG,"offsetX="+offsetX+" offsetY="+offsetY);
        return new AnimParams(scaleX,scaleY,new_l - old_l-offsetX,new_t - old_t-offsetY);
    }

    public Animation createAnim(AnimParams animParams) {
        Animation scal = getScaleAnim(animParams.scaleX, animParams.scaleY);
        Animation translate = getTranslateAnim(animParams.toX, animParams.toY);
        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(scal);
        animSet.addAnimation(translate);
//        animSet.setFillAfter(true);
        animSet.setDuration(frameImgSpeed);
        return animSet;
    }

    private class MyAnimListener implements Animation.AnimationListener {
        int[] locationArr;
        boolean isFirst = true;
        View oldView;
        View newView;

        public MyAnimListener(View oldView, View newView, int locationArr[]) {
            this.locationArr = locationArr;
            this.oldView = oldView;
            this.newView = newView;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            Log.d(TAG,"onAnimationStart:"+this.toString());
            Log.d(TAG,"top:"+getFocusViewTop());

            if(onFocusFrameMoved != null)
                onFocusFrameMoved.onStart(oldView,newView);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
//            Log.d(TAG,""+animation.getClass().getName());
            if(isFirst) {
                Log.d(TAG,"onAnimationEnd:"+this.toString());
                Log.d(TAG,"top:"+getFocusViewTop());
                FocusFrameLayout.this.setFocusLocation(locationArr[0], locationArr[1], locationArr[2], locationArr[3]);
                if(onFocusFrameMoved != null)
                    onFocusFrameMoved.onEnd(oldView,newView);
                isFirst = false;
                startAnim();
            }
        }
    }


    Animation getScaleAnim(float scaleX, float scaleY) {
        Log.d(TAG, "getScaleAnim scaleX:" + scaleX);
        Log.d(TAG, "getScaleAnim scaleY:" + scaleY);
        ScaleAnimation animation = new ScaleAnimation(1f, scaleX
                , 1f, scaleY,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//        animation.setFillAfter(true);
        animation.setInterpolator(getContext(), android.R.anim.linear_interpolator);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }

    Animation getTranslateAnim(float toX, float toY) {
        Log.d(TAG, "getTranslateAnim toX:" + toX);
        Log.d(TAG, "getTranslateAnim toY:" + toY);
        TranslateAnimation animation = new TranslateAnimation(0
                , toX, 0, toY);
//        animation.setFillAfter(true);
        animation.setInterpolator(getContext(), android.R.anim.linear_interpolator);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }


    private void addAnim(MoveMsg msg) {
        synchronized (this) {
            moveMsgsList.add(msg);
        }
        Log.d(TAG,"addMoveMsg moveMsgsList:"+moveMsgsList.size());

    }

    private void startAnim() {
        synchronized (this) {
            if (moveMsgsList.size() != 0) {
                Log.d(TAG,"startAnim");
                MoveMsg moveMsg = moveMsgsList.get(0);
                moveMsgsList.remove(0);
                AnimParams animParams = calAnimPamas(moveMsg.startPosi, moveMsg.endPosi);
                Animation anim = createAnim(animParams);
                anim.setAnimationListener(new MyAnimListener(moveMsg.startView, moveMsg.endView, moveMsg.endPosi));
                mFocusView.startAnimation(anim);
            }
        }
    }

    /**
     * 由于getGlobalVisibleRect获取的位置是相对于全屏的,所以需要减去FocusLayout本身的左与上距离,变成相对于FocusLayout的
     *
     * @param rect
     */
    private void correctLocation(Rect rect) {
        Rect layoutRect = new Rect();
        this.getGlobalVisibleRect(layoutRect);
        rect.left -= layoutRect.left;
        rect.right -= layoutRect.left;
        rect.top -= layoutRect.top;
        rect.bottom -= layoutRect.top;
    }

    /**
     * 设置焦点view的位置,计算焦点框的大小
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    protected void setFocusLocation(int left, int top, int right, int bottom) {
        Log.d(TAG, "setFocusLocation l:" + left + " t:" + top + " r:" + right + " b:" + bottom);

        left -= frameBorderSize;
        top -= frameBorderSize;
        right += frameBorderSize;
        bottom += frameBorderSize;

        Log.d(TAG, "padding  l:" + left + " t:" + top + " r:" + right + " b:" + bottom);
        int width = right - left;
        int height = bottom - top;

        this.mFocusLayoutParams.width = width;
        this.mFocusLayoutParams.height = height;
        this.mFocusLayoutParams.leftMargin = left;
        this.mFocusLayoutParams.topMargin = top;
        mFocusView.clearAnimation();
        mFocusView.setLayoutParams(mFocusLayoutParams);
//        this.mFocusView.layout(left, top, right, bottom);
//
    }
    public void setOnFocusFrameMoved(OnFocusFrameMoved onFocusFrameMoved) {
        this.onFocusFrameMoved = onFocusFrameMoved;
    }
    public static abstract class OnFocusFrameMoved {
        public void onStart(View oldView, View newView){
        }
        public void onEnd(View oldView, View newView) {
        }
    }
}
