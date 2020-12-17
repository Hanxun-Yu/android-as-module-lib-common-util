package com.example.utils.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;

import com.example.utils.log.XLog;

/**
 * Created by yuhanxun on 15/6/17.
 */
public class TwoLinesGridView extends GridView {
    String TAG = "TwoLinesGridView";
    public TwoLinesGridView(Context context) {
        super(context);
        init();
    }

    public TwoLinesGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwoLinesGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        this.setOnScrollListener(onScrollListener);
    }


    int dy = 0;
    boolean scrollOrientionUp = false;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        XLog.d(TAG, "dispatchKeyEvent event:"+event.getAction());
        if (isScroll) {
            XLog.d(TAG, "dispatchKeyEvent isScroll return true");
            return true;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            XLog.e(TAG, "isScroll=" + isScroll);
//                dy =  getSelectedView().getHeight()*2;
            dy = getHeight() - getPaddingTop() - getPaddingBottom();
            XLog.d(TAG, "dispatchKeyEvent dy="+dy);

            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_DOWN:

                    if (isScroll) {
                        XLog.d(TAG, "dispatchKeyEvent down isScroll");
                        return true;
                    }
                    if (ifNeedScroll(false)) {
                        unSelected();
                        smoothScrollBy(dy, 500);
                        isScroll = true;
                        scrollOrientionUp = false;
                        XLog.d(TAG, "dispatchKeyEvent down return");
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (isScroll){
                        XLog.d(TAG, "dispatchKeyEvent up isScroll");
                        return true;
                    };

                    if (ifNeedScroll(true)) {
                        unSelected();
                        smoothScrollBy(-dy, 500);
                        isScroll = true;
                        scrollOrientionUp = true;
                        XLog.d(TAG, "dispatchKeyEvent up return");
                        return true;
                    }
                    break;
            }
        }
        boolean ret = super.dispatchKeyEvent(event);
        XLog.d(TAG, "dispatchKeyEvent return:"+ret);
        return ret;
    }

    void unSelected() {
        View view = getSelectedView();
        view.setSelected(false);
    }

    boolean ifNeedScroll(boolean up) {
        int selectPosition = getSelectedItemPosition();
        int columns = getNumColumns();
        int line = selectPosition / columns + 1;
        int totalLine = (getAdapter().getCount() - 1) / getNumColumns() + 1;
        boolean ret = false;
        if ((line + 2) % 2 == 1) {
            //单数行
            if (up) {
                if (line != 1 && line != totalLine)
                    ret =  true;
                else
                    ret =  false;
            }
        } else {
            //双数行
            if (up) {
                //在最后一页，并且双数行在上部
                if (totalItemCount % (columns * 2) <= columns && totalItemCount % (columns * 2) != 0
                        && firstVisibleItem + visibleItemCount == totalItemCount) {


                    ret =  true;
                }
            } else {
                if (line != totalLine) {
                    //如果当前显示第一行是（数据第二行）
                    if (totalItemCount % (columns * 2) <= columns && totalItemCount % (columns * 2) != 0
                            && firstVisibleItem + visibleItemCount == totalItemCount)
                        ret = false;
                    else
                        ret  = true;
                }
            }
        }
        XLog.d(TAG, "ifNeedScroll ret:"+ret);

        return ret;
    }


    boolean isScroll = false;
    OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                XLog.d(TAG, "onScrollListener SCROLL_STATE_IDLE");
                isScroll = false;
            }

            if (scrollState == SCROLL_STATE_FLING) {
//                if (!scrollOrientionUp)
//                    setSelection(Math.min(getSelectedItemPosition() + getNumColumns(), getCount() - 1));
//                else
//                    setSelection(Math.max(getSelectedItemPosition() - getNumColumns(), 0));
                XLog.d(TAG, "SCROLL_STATE_FLING");
            }
            if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                XLog.d("TwoLinesGridView", "SCROLL_STATE_TOUCH_SCROLL");
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            TwoLinesGridView.this.firstVisibleItem = firstVisibleItem;
            TwoLinesGridView.this.visibleItemCount = visibleItemCount;
            TwoLinesGridView.this.totalItemCount = totalItemCount;
            XLog.d("TwoLinesGridView", "firstVisibleItem:" + firstVisibleItem + " visibleItemCount:" + visibleItemCount + " totalItemCount:" + totalItemCount);
        }
    };
    int firstVisibleItem;
    int visibleItemCount;
    int totalItemCount;

    /**
     * 屏蔽android4.4 setAdapter时View抢焦点的BUG
     */
    @Override
    public boolean isInTouchMode() {
        if (19 == Build.VERSION.SDK_INT) {
            return !(hasFocus() && !super.isInTouchMode());
        } else {
            return super.isInTouchMode();
        }
    }
}
