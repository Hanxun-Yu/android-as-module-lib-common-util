package com.example.utils.widget;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhanxun on 15/5/18.
 */
public class TwoLinesScrollUtil {
    private int Download_Line;
    private int Download_Column;
    private int Download_Item_LeftMargin;
    private int Download_Item_TopMargin;
    private OneScreenScrollView scrollView;
    private int layoutID;

    private int itemHeight;
    private int itemWidth;

    public boolean isLayoutInitFinish() {
        return isLayoutInitFinish;
    }

    private boolean isLayoutInitFinish = false;

    public void setFocusChangeListener(View.OnFocusChangeListener focusChangeListener) {
        this.focusChangeListener = focusChangeListener;
    }

    public void setKeyListener(View.OnKeyListener keyListener) {
        this.keyListener = keyListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private View.OnFocusChangeListener focusChangeListener;
    private View.OnClickListener clickListener;
    private View.OnKeyListener keyListener;
    private Context context;

    public List<View> getViewList() {
        return viewList;
    }

    private List<View> viewList;

    /**
     * @param context
     * @param download_Column          列数
     * @param download_Item_LeftMargin item左右间隔
     * @param download_Item_TopMargin  item上下间隔
     * @param download_Line            行数
     * @param scrollView
     * @param layoutID                 item布局
     */
    public TwoLinesScrollUtil(Context context, int download_Column, int download_Item_LeftMargin, int download_Item_TopMargin, int download_Line, OneScreenScrollView scrollView, int layoutID) {
        this.context = context;
        Download_Column = download_Column;
        Download_Item_LeftMargin = download_Item_LeftMargin;
        Download_Item_TopMargin = download_Item_TopMargin;
        Download_Line = download_Line;
        this.scrollView = scrollView;
        scrollView.setTopMargin(Download_Item_TopMargin);
        this.layoutID = layoutID;
        viewList = new ArrayList<View>();

//        itemHeight = (int) context.getResources().getDimension(R.dimen.movie_item_height);

    }

    public static final String VIEW_COUNT_INT_DATA = "count";
    public static final String VIEW_LINE_INT_DATA = "line";
    public static final String PUB_NAME_STRING_DATA = "name";


    /**
     * 初始化双数行个布局好的view
     */
    public void initLayout(ViewGroup parent) {
        Log.e("yx", "initLayout");
        parent.removeAllViews();
//        parent.setFocusable(true);
        int length = Download_Column * Download_Line;
        int lines = Download_Line;
        int count = 1;
        final int column = Download_Column;
        RelativeLayout v = null;
        RelativeLayout.LayoutParams params = null;
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < column; j++) {
                //R.layout.update_film_item_container
                v = (RelativeLayout) LayoutInflater.from(context).inflate(layoutID, null);
                v.setClipChildren(false);
                v.setClipToPadding(false);
                Bundle  b = new Bundle();
                v.setId(count);
                b.putInt(VIEW_LINE_INT_DATA, i + 1);
                b.putInt(VIEW_COUNT_INT_DATA, count);
                v.setTag(b);
//                boolean flag = v.getClipChildren();

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                viewList.add(v);
//                params = (RelativeLayout.LayoutParams) v.getLayoutParams();

                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                if ((i+3) % 2 == 1) {
                    //单数行
                } else {
                    //双数行

                }
                //除第一行
                if( i !=0) {
//                    params.topMargin = Download_Item_TopMargin;
                    params.addRule(RelativeLayout.BELOW, count - Download_Column);
                }

                //除第一列
                if (j != 0) {
                    params.leftMargin = Download_Item_LeftMargin;
                    params.addRule(RelativeLayout.RIGHT_OF, count - 1);
                }

                v.setOnKeyListener(new MyOnKeyListener(count,column));

//                v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChange(View v, boolean hasFocus) {
//                        if (focusChangeListener != null)
//                            focusChangeListener.onFocusChange(v, hasFocus);
//                    }
//                });
//
//                v.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (clickListener != null)
//                            clickListener.onClick(v);
//                    }
//                });

                v.setVisibility(View.INVISIBLE);
                parent.addView(v, params);
                count++;
            }
        }
//        parent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus) {
//                    parent.getChildAt(0).requestFocus();
//                }
//            }
//        });
        isLayoutInitFinish = true;
    }

    class MyOnKeyListener implements View.OnKeyListener {
        int finalCount;
        int column;
        public MyOnKeyListener(int finalCount, int column) {
            this.finalCount = finalCount;
            this.column = column;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
//            Bundle b = (Bundle)v.getTag();
//
//            int line = (int) b.getInt(VIEW_LINE_INT_DATA);
//            if (line % 2 == 1 && line != 1) {
//                //单数行
//                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    //上
//                    scrollView.scroll(true);
//                    focusNearFocusableView(true, v);
//                    return true;
//                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if(line == (Download_Line-1) && finalCount != (line*column) && viewList.getJSONObjFromFile(finalCount).getVisibility()== View.GONE ) {
//                        scrollView.scroll(true);
//                        focusNearFocusableView(true, v);
//                        return true;
//                    }
//                }
//            } else if (line % 2 == 0 && line != Download_Line) {
//                //双数行,按下
//                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
//                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                        scrollView.scroll(false);
//                        focusNearFocusableView(false, v);
//                        return true;
//                    }
//                }
//            }

            if (keyListener != null) {
                keyListener.onKey(v, keyCode, event);
            }
            return false;
        }
    }

//    void focusNearFocusableView(boolean up, View v) {
//        Bundle b = (Bundle)v.getTag();
//
//        int count = (int) b.getInt(VIEW_COUNT_INT_DATA);
//        int index = 0;
//        if (up) {
//            index = count - Download_Column - 1;
//
//            Log.e("yhx", "Line 1-->view requestFoc index:" + (v.getId() - Download_Column - 1));
//            Log.e("yhx", "Line 1-->view id:" + v.getId() + " Download_Line:" + Download_Column);
//        } else {
//            index = count + Download_Column - 1;
//            Log.e("yhx", "Line 2-->view requestFoc index:" + (v.getId() + Download_Column - 1));
//            Log.e("yhx", "Line 2-->view id:" + v.getId() + " Download_Line:" + Download_Column);
//        }
//        while (viewList.getJSONObjFromFile(index).getVisibility() != View.VISIBLE) {
//            index--;
//        }
//        Log.e("yhx", "view requestFoc index:" + index);
//        viewList.getJSONObjFromFile(index).requestFocus();
//    }
}
