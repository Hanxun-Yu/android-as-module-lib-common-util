package com.example.utils.widget.keyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.utils.baseClass.BaseFragment;
import com.example.utils.log.XLog;


/**
 * Created by xunxun on 2018/2/24.
 */

public class KeyboardFragment extends BaseFragment {
    KeyBoardView keyBoardView;
    ViewGroup rootView;
    String initTitle;
    String initText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setLogFilter("xunxun");
        rootView = new RelativeLayout(getContext());
        rootView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        rootView.setClickable(true);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        keyBoardView = new KeyBoardView(getContext());
        rootView.addView(keyBoardView, params);
//        rootView.setVisibility(View.INVISIBLE);
//        rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setEnabled(false);
//                hideKeyboard(null);
//            }
//        });
        initTitle = getArguments().getString(argTitle);
        initText = getArguments().getString(argEdit);
        keyBoardView.setInitText(initTitle,initText);
        return rootView;
    }

    public void setInputType(int type) {
        keyBoardView.setInputType(type);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
//        Log.d(TAG,"onSaveInstanceState outState:"+outState);
//        outState.putString("title",initTitle);
//        outState.putString("text",initText);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XLog.d(TAG, "onCreate savedInstanceState:" + savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
        XLog.d(TAG, "onResume");

    }

    private static final String argTitle = "title";
    private static final String argEdit = "edit";

    public static KeyboardFragment newInstance(String title, String edit) {
        Bundle args = new Bundle();
        args.putString(argTitle, title);
        args.putString(argEdit, edit);
        KeyboardFragment fragment = new KeyboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        keyBoardView.setOnKeyboardListener(new KeyBoardView.OnKeyboardListener() {
            @Override
            public void onClose() {
                if(keyboardEventListener != null)
                    keyboardEventListener.onClose();
            }

            @Override
            public void onConfirm(String text) {
                if(keyboardEventListener != null) {
                    keyboardEventListener.onConfirm(text);
                }
            }

            @Override
            public void onTextChanged(String text) {
            }
        });
    }


    public void setKeyboardEventListener(KeyboardEventListener keyboardEventListener) {
        this.keyboardEventListener = keyboardEventListener;
    }

    KeyboardEventListener keyboardEventListener;

    public interface KeyboardEventListener {
        void onClose();

        void onShow();

        void onConfirm(String text);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
