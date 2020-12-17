package com.example.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.example.utils.R;

public class HomeItemContainer extends RelativeLayout {

    private Rect mBound;
    private Drawable mDrawable;
    private Rect mRect;
    private int borderSize;
    private boolean animable = true;

    private Animation scaleSmallAnimation;
    private Animation scaleBigAnimation;


    public HomeItemContainer(Context context) {
        super(context);
        init();
    }

    public HomeItemContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
        getShadowAttr(context, attrs);
    }

    public HomeItemContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        getShadowAttr(context, attrs);
    }

    private void getShadowAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HomeItemContainer);
        mDrawable = a.getDrawable(R.styleable.HomeItemContainer_shadow);
//        animable = a.getBoolean(R.styleable.HomeItemContainer_animable,true);
        borderSize = a.getInteger(R.styleable.HomeItemContainer_shadowSize, 0);
    }


    protected void init() {
        setWillNotDraw(false);
        mRect = new Rect();
        mBound = new Rect();

        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        hasFocus() &&
        if (selected && mDrawable != null) {
//            Log.e("yhb", "hasFocus ondraw");
            super.getDrawingRect(mRect);
            mBound.set(mRect.left, mRect.top, mRect.right, mRect.bottom);
            mBound.set(-borderSize + mRect.left, -borderSize + mRect.top, borderSize + mRect.right, borderSize + mRect.bottom);
            mDrawable.setBounds(mBound);
            canvas.save();
            mDrawable.draw(canvas);
            canvas.restore();
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if(animable)
            doOnFocus(gainFocus);

        if(onFocusChangedListener!=null)
            onFocusChangedListener.onFocusChanged(this,gainFocus);
    }

    boolean selected = false;

    public void doOnFocus(boolean selected) {
        this.selected = selected;
        invalidate();
        if (selected) {
//            this.getParent().bringChildToFront(this);
            bringToFront();
            getRootView().requestLayout();
            getRootView().invalidate();
            zoomOut();
        } else {
            zoomIn();
        }
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        if(animable)
            doOnFocus(selected);

        if(onSelectedChangedListener!=null)
            onSelectedChangedListener.onSelectedChanged(this,selected);
    }

    private void zoomIn() {
//        if (scaleSmallAnimation == null) {
//            scaleSmallAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_small);
//        }
//        scaleSmallAnimation.setFillAfter(true);
//        scaleSmallAnimation.setFillEnabled(true);
//        startAnimation(scaleSmallAnimation);
//        Log.e("yhb", "zoomIn");
        android.view.ViewPropertyAnimator animator = animate()
                .scaleX(1.0f).scaleY(1.0f).setDuration(200);
        animator.start();
    }

    private void zoomOut() {
//        if (scaleBigAnimation == null) {
//            scaleBigAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_big);
//        }
//        scaleBigAnimation.setFillAfter(true);
//        scaleBigAnimation.setFillEnabled(true);
//        startAnimation(scaleBigAnimation);
        android.view.ViewPropertyAnimator animator = animate()
                .scaleX(1.1f).scaleY(1.1f).setDuration(200);
        animator.start();
//        Log.e("yhb", "zoomOut");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
    OnSelectedChangedListener onSelectedChangedListener;
    public interface OnSelectedChangedListener {
        void onSelectedChanged(HomeItemContainer homeItemContainer,boolean selected);
    }
    OnFocusChangedListener onFocusChangedListener;
    public interface OnFocusChangedListener {
        void onFocusChanged(HomeItemContainer homeItemContainer,boolean focus);
    }
    public void setOnSelectedChangedListener(OnSelectedChangedListener onSelectedChangedListener) {
        this.onSelectedChangedListener = onSelectedChangedListener;
    }
}