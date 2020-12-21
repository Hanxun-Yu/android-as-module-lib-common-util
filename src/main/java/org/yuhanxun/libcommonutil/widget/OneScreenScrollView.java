package org.yuhanxun.libcommonutil.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by yuhanxun on 15/6/30.
 */
public class OneScreenScrollView extends ScrollView {
    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
    }

    int topMargin;

    public OneScreenScrollView(Context context) {
        super(context);
    }

    public OneScreenScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OneScreenScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);


    }

//    //累计滑动距离，当达到一屏距离时清零
//    int changedDistance = 0;
//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
////        super.onScrollChanged(l, t, oldl, oldt);
//        Log.e("yhx","l="+l+" t="+t+" oldl="+oldl+" oldt="+oldt);
//        int height = getHeight();
//        //计算出应该滚动的距离倍数
//        int needDy = height - topMargin;
//
//        //实际滚动距离
//        int currentDy = t-oldt;
//        //取反
//        if(currentDy <0)
//            needDy = -needDy;
//
//        //计算需要补上的距离
//        int patchDy = needDy - (currentDy+changedDistance);
//        changedDistance = currentDy;
//
//        Log.e("yhx","patchDy = " +patchDy + " changeDistance="+changedDistance);
//
//        if(patchDy == 0) {
//            //说明已补满
//            changedDistance = 0;
//            Log.e("yhx","已补满");
//        } else {
//            Log.e("yhx","smoothScrollBy :"+patchDy);
//            smoothScrollBy(0, patchDy);
//        }
//    }


    int oldy = 0;

    synchronized void scroll(boolean up) {
//        Log.e("yhx", "oldy:" + (oldy));
//        Log.e("yhx","up:"+up+" isHalf:"+isHalf);
        int dy = getHeight();
        dy = dy - topMargin;

        if (up) {
            dy = -dy;
        }
//        if (isHalf)
//            dy = dy / 2;
//        dy = oldy + dy;

        Log.e("yx", "smoothScrollTo:" + dy);
//        smoothScrollBy(0, dy);
        pageScroll(up ? ScrollView.FOCUS_UP : ScrollView.FOCUS_DOWN);
//        oldy = dy;
    }

    public void scrollResume() {
        oldy = 0;
        smoothScrollTo(0, 0);
    }

    /**
     * Handle scrolling in response to an up or down arrow click.
     *
     * @param direction The direction corresponding to the arrow key that was
     *                  pressed
     * @return True if we consumed the event, false otherwise
     */
    @Override
    public boolean arrowScroll(int direction) {

        View currentFocused = findFocus();
        if (currentFocused == this) currentFocused = null;

        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);

        final int maxJump = getMaxScrollAmount();

        if (nextFocused != null && isWithinDeltaOfScreen(nextFocused, maxJump, getHeight())) {
            nextFocused.getDrawingRect(mTempRect);
            offsetDescendantRectToMyCoords(nextFocused, mTempRect);
            int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);

            int itemHeight = nextFocused.getHeight();
            int twoLineDetalY = scrollDelta < 0 ? scrollDelta - itemHeight : scrollDelta + itemHeight;
            smoothScrollBy(0, scrollDelta == 0 ? 0 : twoLineDetalY);
            nextFocused.requestFocus(direction);
        } else {
            // no new focus
            int scrollDelta = maxJump;

            if (direction == View.FOCUS_UP && getScrollY() < scrollDelta) {
                scrollDelta = getScrollY();
            } else if (direction == View.FOCUS_DOWN) {
                if (getChildCount() > 0) {
                    int daBottom = getChildAt(0).getBottom();
                    int screenBottom = getScrollY() + getHeight() - this.getPaddingBottom();
//                            ngBottom;
                    if (daBottom - screenBottom < maxJump) {
                        scrollDelta = daBottom - screenBottom;
                    }
                }
            }
            if (scrollDelta == 0) {
                return false;
            }
            smoothScrollBy(0, direction == View.FOCUS_DOWN ? scrollDelta : -scrollDelta);
        }

        if (currentFocused != null && currentFocused.isFocused()
                && isOffScreen(currentFocused)) {
            // previously focused item still has focus and is off screen, give
            // it up (take it back to ourselves)
            // (also, need to temporarily force FOCUS_BEFORE_DESCENDANTS so we are
            // sure to
            // getJSONObjFromFile it)
            final int descendantFocusability = getDescendantFocusability();  // save
            setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            requestFocus();
            setDescendantFocusability(descendantFocusability);  // restore
        }
        return true;
    }

    private final Rect mTempRect = new Rect();

    /**
     * @return whether the descendant of this scroll view is within delta
     * pixels of being on the screen.
     */
    private boolean isWithinDeltaOfScreen(View descendant, int delta, int height) {
        descendant.getDrawingRect(mTempRect);
        offsetDescendantRectToMyCoords(descendant, mTempRect);

        return (mTempRect.bottom + delta) >= getScrollY()
                && (mTempRect.top - delta) <= (getScrollY() + height);
    }

    /**
     * @return whether the descendant of this scroll view is scrolled off
     * screen.
     */
    private boolean isOffScreen(View descendant) {
        return !isWithinDeltaOfScreen(descendant, 0, getHeight());
    }
}
