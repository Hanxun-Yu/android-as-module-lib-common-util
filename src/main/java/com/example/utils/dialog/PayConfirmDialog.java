package com.example.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.utils.R;



/**
 * Created by szj on 2017/1/5.
 */

public class PayConfirmDialog extends Dialog {

    Button confirmBtn;
    Button cancelBtn;
    TextView contentTextView;


    public PayConfirmDialog(Context context) {
        super(context, R.style.CustomProgressDialog);
        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return true;
    }


    private boolean respondKeyBack = false;

    public void setBtnListener(BtnListener btnListener) {
        this.btnListener = btnListener;
    }

    private BtnListener btnListener;

    public boolean isRespondKeyBack() {
        return respondKeyBack;
    }

    public void setRespondKeyBack(boolean respondKeyBack) {
        this.respondKeyBack = respondKeyBack;
    }

    public interface BtnListener {
        void confirm(PayConfirmDialog pcf);
        void cancel(PayConfirmDialog pcf);
    }

    public void setContent(String content) {
        contentTextView.setText(content);
    }

    public void init(Context context) {
        setContentView(R.layout.pay_confirm_dialog);
        confirmBtn = (Button)findViewById(R.id.confirm_btn);
        cancelBtn = (Button)findViewById(R.id.cancel_btn);
        contentTextView = (TextView)findViewById(R.id.content_text);
        getWindow().getAttributes().gravity = Gravity.CENTER;

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnListener!=null)
                    btnListener.confirm(PayConfirmDialog.this);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnListener!=null)
                    btnListener.cancel(PayConfirmDialog.this);

                dismiss();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (respondKeyBack)
                dismiss();
            else
                return true;
        }
        return super.onKeyDown(keyCode,event);
    }

}
