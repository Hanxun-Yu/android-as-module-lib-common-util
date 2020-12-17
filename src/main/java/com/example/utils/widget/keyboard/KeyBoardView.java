package com.example.utils.widget.keyboard;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.utils.R;

import java.lang.reflect.Method;



/**
 * Created by yuhanxun on 15/7/18.
 */
public class KeyBoardView extends LinearLayout implements View.OnClickListener {
    private final static String mKeys_1[] = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "q", "w", "e", "r", "t", "y", "u", "i", "o", "p",
            "a", "s", "d", "f", "g", "h", "j", "k", "l", "'",
            "z", "x", "c", "v", "b", "n", "m", ",", ".", "?"};

    private final static String mKeys_2[] = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
            "A", "S", "D", "F", "G", "H", "J", "K", "L", "\"",
            "Z", "X", "C", "V", "B", "N", "M", "-", "_", "/"};


    private final static String mKeys_digital[] = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    private final static String clearKey = "清空";

    private final static int _26Letter_number_parent_ID = 0x0eee;

    int textSize = 10;
    int mPadding;
    private KeyBoardBtnView delBtn;

    public KeyBoardView(Context context) {
        super(context);
        initAttribute(TYPE_ENGLISH);
    }

    public KeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttribute(TYPE_ENGLISH);
    }

    public KeyBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(TYPE_ENGLISH);
    }



    public static final int TYPE_ENGLISH = 0x0010;
    public static final int TYPE_DIGITAL = 0x0020;

    public void setKeyboardType(int type) {
        initAttribute(type);
    }
    public void initAttribute(int type) {
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyBoardView);
//        _columnNum = a.getInteger(R.styleable.KeyBoardView_columnNum, 0);
//        _columnSpace = a.getDimensionPixelOffset(R.styleable.KeyBoardView_columnSpace, 0);
//        _rowSpace = a.getDimensionPixelOffset(R.styleable.KeyBoardView_rowSpace, 0);
//
//        _width = getResources().getDimensionPixelOffset(R.dimen.search_keyboard_width);
//        _height = this.getMeasuredHeight();
        removeAllViews();
        if(type == TYPE_ENGLISH) {
            setClickable(true);

            //获取屏幕宽高
            int[] screenWH = getScreenWH();
            mWidth = screenWH[0] * 3 / 5;
            mHeight = screenWH[1] * 2 / 3;
            mSmallBtnWidth = mWidth / 11;
            mSmallBtnHeight = mHeight / 9;
            mBtnSpace = 2;
            textSize = 13;
            mPadding  = 20;

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mWidth, mHeight);
            setLayoutParams(params);
            setBackgroundResource(R.drawable.keyboard_bg);
            setOrientation(VERTICAL);
            setGravity(Gravity.CENTER);
            setClipChildren(false);
            setClipToPadding(false);
            setPadding(mPadding, mPadding, mPadding, mPadding);

            initKeyBoardBig();
        } else if(type == TYPE_DIGITAL) {

            //配置宽高



            initKeyboardDigital();
        }

    }

    int mWidth;
    int mHeight;
    int mSmallBtnWidth;
    int mSmallBtnHeight;
    int mBtnSpace;

    public void setInitText(String title,String initText) {
        titleText.setText(title+":");
        editText.setText(initText);
        editText.setSelection(initText.length());
    }

    public void setInputType(int type) {
        if(type == 1)
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        else
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

    }

    private int[] getScreenWH() {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        int mScreenHeight = dm.heightPixels;
        return new int[]{mScreenWidth, mScreenHeight};
    }


    EditText editText;
    TextView titleText;
    KeyBoardBtnView closeBtn;
    KeyBoardBtnView confirmBtn;
    View firstFocus = null;

    private LinearLayout getEditClosePart() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setClipChildren(false);
        linearLayout.setOrientation(HORIZONTAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -2);
        linearLayout.setLayoutParams(params);


        titleText = new TextView(getContext());
        titleText.setFocusable(false);
        titleText.setTextColor(getResources().getColor(R.color.menu_color));
        titleText.setPadding(10, 0, 0, 0);
        titleText.setClickable(false);
        titleText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        params = new LayoutParams(-2, mSmallBtnHeight);
        linearLayout.addView(titleText, params);

        editText = new EditText(getContext());
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        disableShowSoftInput();
//        editText.setFocusable(false);
        editText.setTextColor(getResources().getColor(R.color.menu_color));
        editText.setPadding(10, 0, 0, 0);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        editText.setCursorVisible(true);
        editText.setClickable(false);
        editText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        editText.setBackgroundResource(R.drawable.keyboard_edit_bg);
        editText.requestFocus();



        params = new LayoutParams(0, mSmallBtnHeight);
        params.weight = 1;
        linearLayout.addView(editText, params);

        delBtn = new KeyBoardBtnView(getContext());
        delBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        delBtn.setBackgroundResource(R.drawable.keyboard_btn_bg_1);
        delBtn.setText("Del");
        delBtn.setTextColor(getResources().getColor(R.color.menu_color));
        params = new LayoutParams(mSmallBtnWidth, mSmallBtnHeight);
        params.leftMargin = mBtnSpace;
        linearLayout.addView(delBtn, params);



        closeBtn = new KeyBoardBtnView(getContext());
        closeBtn.setImage(R.drawable.close_22_22);
        closeBtn.setBackgroundResource(R.drawable.keyboard_btn_bg_3);
        params = new LayoutParams(mSmallBtnWidth, mSmallBtnHeight);
        params.leftMargin = mBtnSpace;
        linearLayout.addView(closeBtn, params);

        return linearLayout;

    }

    private RelativeLayout get40BtnPart() {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        LayoutParams params = new LayoutParams(-2, -2);
        params.topMargin = mBtnSpace;
        relativeLayout.setLayoutParams(params);
        relativeLayout.setClipChildren(false);
        relativeLayout.removeAllViews();
        //根据设定的列数，计算每个按钮的边长
        int _columnNum = 10;
        int length = mKeys_1.length;
        int div = length % _columnNum;
        int lines = (div == 0) ? length / _columnNum : length / _columnNum + 1;
        int count = 1;
        int column = _columnNum;
        KeyBoardBtnView v;
        RelativeLayout.LayoutParams paramsBtn;
        for (int i = 0; i < lines; i++) {

            if (i == lines - 1 && div != 0) {
                column = div;
            }
            for (int j = 0; j < column; j++) {
                v = new KeyBoardBtnView(getContext());
                v.setId(count);
                v.setFocusable(true);
//                v.setFocusableInTouchMode(true);
                v.setText(mKeys_1[count - 1]);
                if(v.getText().equals("g"))
                    firstFocus = v;


                v.setOnClickListener(this);
                v.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                v.setBackgroundResource(R.drawable.keyboard_btn_bg_1);
                v.setTextColor(getResources().getColor(R.color.menu_color));

                paramsBtn = new RelativeLayout.LayoutParams(mSmallBtnWidth, mSmallBtnHeight);
                if (j != column - 1)
                    paramsBtn.rightMargin = mBtnSpace;
                paramsBtn.topMargin = mBtnSpace;

                if (i != 0) {
                    paramsBtn.addRule(RelativeLayout.BELOW, count - _columnNum);
                }
                if (j != 0) {
                    paramsBtn.addRule(RelativeLayout.RIGHT_OF, count - 1);
                }

                relativeLayout.addView(v, paramsBtn);
                count++;
            }
        }

        return relativeLayout;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean handled = false;
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ||
                    event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE ||
                    event.getKeyCode() == KeyEvent.KEYCODE_DEL
                    ) {

                handled = true;
                dealingBackEvent();
            }
        }
        if (!handled)
            return super.dispatchKeyEvent(event);
        else
            return handled;
    }

    private LinearLayout getShiftSpaceSpart() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setClipChildren(false);
        linearLayout.setOrientation(HORIZONTAL);
        LayoutParams params = new LayoutParams(-2, -2);
        params.topMargin = mBtnSpace;
        linearLayout.setLayoutParams(params);

        KeyBoardBtnView shiftBtn = new KeyBoardBtnView(getContext());
        shiftBtn.setImage(R.drawable.shift_40_40);
        shiftBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        shiftBtn.setBackgroundResource(R.drawable.keyboard_btn_bg_1);
        shiftBtn.setClickable(true);
        params = new LayoutParams(mSmallBtnWidth, mSmallBtnHeight);
        linearLayout.addView(shiftBtn, params);


        KeyBoardBtnView symbolBtn = new KeyBoardBtnView(getContext());
        symbolBtn.setText("@#:");
        symbolBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        symbolBtn.setClickable(true);

        params = new LayoutParams(mSmallBtnWidth, mSmallBtnHeight);
        params.leftMargin = mBtnSpace;
        symbolBtn.setBackgroundResource(R.drawable.keyboard_btn_bg_1);
        symbolBtn.setTextColor(getResources().getColor(R.color.menu_color));
        linearLayout.addView(symbolBtn, params);

        KeyBoardBtnView space = new KeyBoardBtnView(getContext());
        space.setText("空格");
        space.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        space.setBackgroundResource(R.drawable.keyboard_btn_bg_1);
        space.setTextColor(getResources().getColor(R.color.menu_color));
        params = new LayoutParams(mSmallBtnWidth * 4 + 3 * mBtnSpace, mSmallBtnHeight);
        params.leftMargin = mBtnSpace * 2 + mSmallBtnWidth;
        linearLayout.addView(space, params);
        space.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dealingKeyboardEvent(" ");
            }
        });

        confirmBtn = new KeyBoardBtnView(getContext());
        confirmBtn.setBackgroundResource(R.drawable.keyboard_btn_bg_4);
        confirmBtn.setText("确定");
        confirmBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        confirmBtn.setTextColor(getResources().getColor(R.color.menu_color));
        params = new LayoutParams(mSmallBtnWidth * 2 + mBtnSpace, mSmallBtnHeight);
        params.leftMargin = mBtnSpace * 2 + mSmallBtnWidth;
        linearLayout.addView(confirmBtn, params);

        return linearLayout;
    }

//    private LinearLayout get4DirectionConfirmPart() {
//        LinearLayout linearLayout = new LinearLayout(getContext());
//        linearLayout.setOrientation(HORIZONTAL);
//        LayoutParams params = new LayoutParams(-2, -2);
//        params.topMargin = mBtnSpace;
//        linearLayout.setLayoutParams(params);
//
//        KeyBoardBtnView shiftBtn = new KeyBoardBtnView(getContext());
//        shiftBtn.setImage(R.drawable.shift_40_40);
//        shiftBtn.setBackgroundResource(R.drawable.keyboard_btn_bg_1);
//        params = new LayoutParams(mSmallBtnWidth, mSmallBtnHeight);
//        linearLayout.addView(shiftBtn, params);
//
//
//        KeyBoardBtnView symbolBtn = new KeyBoardBtnView(getContext());
//        symbolBtn.setText("@#:");
//        params = new LayoutParams(mSmallBtnWidth, mSmallBtnHeight);
//        params.leftMargin = mBtnSpace;
//        symbolBtn.setBackgroundResource(R.drawable.keyboard_btn_bg_1);
//        symbolBtn.setTextColor(getResources().getColor(R.color.menu_color));
//        linearLayout.addView(symbolBtn, params);
//
//        KeyBoardBtnView space = new KeyBoardBtnView(getContext());
//        space.setText("空格");
//        space.setBackgroundResource(R.drawable.keyboard_btn_bg_1);
//        space.setTextColor(getResources().getColor(R.color.menu_color));
//        params = new LayoutParams(mSmallBtnWidth*4+3*mBtnSpace, mSmallBtnHeight);
//        params.leftMargin = mBtnSpace*2+mSmallBtnWidth;
//        linearLayout.addView(space, params);
//
//        KeyBoardBtnView delback = new KeyBoardBtnView(getContext());
//        delback.setBackgroundResource(R.drawable.keyboard_btn_bg_1);
//        delback.setImage(R.drawable.delback_32);
//        params = new LayoutParams(mSmallBtnWidth*2+mBtnSpace, mSmallBtnHeight);
//        params.leftMargin = mBtnSpace*2+mSmallBtnWidth;
//        linearLayout.addView(delback, params);
//    }

    public void initKeyBoardBig() {
        //编辑框 与关闭按钮
        addView(getEditClosePart());

        //40个按钮
        addView(get40BtnPart());

        //大小写，符号，空格，删除

        addView(getShiftSpaceSpart());
        //4个方向键 确认


        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onKeyboardListener!=null)
                    onKeyboardListener.onConfirm(editText.getText().toString());
            }
        });

        closeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onKeyboardListener!=null)
                    onKeyboardListener.onClose();
            }
        });

        delBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dealingBackEvent();
            }
        });
    }

    private void initKeyboardDigital() {

    }


    private void dealingKeyboardEvent(String text) {
        String original = editText.getText().toString();
        if (original.length() <= 30) {
//            StringBuilder builder = new StringBuilder();
//            builder.append(original);
//            builder.append(text);
//            editText.setText(builder.toString());
            insertText(editText,text);
            if(onKeyboardListener!=null) {
                onKeyboardListener.onTextChanged(editText.getText().toString());
            }
        }
    }
    /**向EditText指定光标位置插入字符串*/
    private void insertText(EditText mEditText, String mText){
        mEditText.getText().insert(getEditTextCursorIndex(mEditText), mText);
    }
    /**向EditText指定光标位置删除字符串*/
    private void deleteText(EditText mEditText){
        if(!TextUtils.isEmpty(mEditText.getText().toString())){
            if(getEditTextCursorIndex(mEditText) >0) {
                mEditText.getText().delete(getEditTextCursorIndex(mEditText) - 1, mEditText.getSelectionEnd());
            }
        }
    }
    /**获取EditText光标所在的位置*/
    private int getEditTextCursorIndex(EditText mEditText){
        return mEditText.getSelectionStart();
    }

    private void dealingBackEvent() {
        String original = editText.getText().toString();
        if (!TextUtils.isEmpty(original)) {
//            if (original.length() > 1) {
//                original = original.substring(0, original.length() - 1);
//            } else {
//                original = "";
//            }
            deleteText(editText);
        } else {
//            if(onKeyboardListener!=null) {
//                onKeyboardListener.onClose();
//            }
        }
//        editText.setText(original);
        if(onKeyboardListener!=null) {
            onKeyboardListener.onTextChanged(editText.getText().toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case -9:
                break;

            default:
                dealingKeyboardEvent(((KeyBoardBtnView)v).getText());
                break;
        }
    }


    class KeyBoardViewException extends Throwable {
        public KeyBoardViewException(String detailMessage) {
            super(detailMessage);
        }
    }

    public void setOnKeyboardListener(OnKeyboardListener onKeyboardListener) {
        this.onKeyboardListener = onKeyboardListener;
    }

    private OnKeyboardListener onKeyboardListener;
    public interface OnKeyboardListener {
        void onClose();
        void onConfirm(String text);
        void onTextChanged(String text);
    }

    public void notifyFocus() {
        if(firstFocus != null)
            firstFocus.requestFocus();
    }

    public void disableShowSoftInput() {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
            }
        }
    }
}
