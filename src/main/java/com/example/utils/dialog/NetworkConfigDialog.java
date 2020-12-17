package com.example.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.utils.R;
import com.example.utils.device.NetworkStatusChecker;


/**
 * 自定义dialog
 *
 * @author Mr.Xu
 */
public class NetworkConfigDialog extends Dialog {
    public final int CHECKING = 0;
    public final int SUCCESS = 1;
    public final int FAIL = 2;
    //定义回调事件，用于dialog的点击事件
    public interface OnSuccessListener {
        public void onSuccess();
    }

    private String name;
    private OnSuccessListener listener;
    TextView textview_fail;
    TextView textview_checking;
    ProgressBar progressBar;
    Button tryBtn;
    Button settingBtn;
    LinearLayout btnLayout;

    public NetworkConfigDialog(Context context, String name, OnSuccessListener listener) {
        super(context);
        this.name = name;
        this.listener = listener;
    }

    View.OnClickListener settingBtnListener;
    public void setSettingListener(View.OnClickListener listener) {
        settingBtnListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_check_dialog);
        //设置标题
        setTitle(name);
        initView();


        tryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                doTry();
            }
        });

        settingBtn.setOnClickListener(settingBtnListener);

        doTry();
    }
    void doTry() {
        Log.e("NetworkConfigDialog", this+" doTry");
        new Thread(new CheckRunnable()).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode()== KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

    private void initView() {
        textview_fail = (TextView) findViewById(R.id.textview_fail);
        textview_checking = (TextView)findViewById(R.id.textview_checking);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tryBtn = (Button) findViewById(R.id.try_btn);
        settingBtn = (Button) findViewById(R.id.setting_btn);
        btnLayout = (LinearLayout)findViewById(R.id.btn_layout);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    //成功
                    if(listener!=null)
                        listener.onSuccess();
                    if(NetworkConfigDialog.this.isShowing())
                        dismiss();
                    break;
                case FAIL:
                    //失败
                    onFail();
                    break;

                case CHECKING:
                    onChecking();
                    break;
            }

        }
    };

    void onChecking() {
        progressBar.setVisibility(View.VISIBLE);
        btnLayout.setVisibility(View.INVISIBLE);
        textview_checking.setVisibility(View.VISIBLE);
        textview_fail.setVisibility(View.INVISIBLE);
    }

    void onFail() {
        progressBar.setVisibility(View.INVISIBLE);
        btnLayout.setVisibility(View.VISIBLE);
        textview_checking.setVisibility(View.INVISIBLE);
        textview_fail.setVisibility(View.VISIBLE);
        tryBtn.setEnabled(true);
    }


    class CheckRunnable implements Runnable {
        boolean isConnected = false;
        @Override
        public void run() {
            handler.obtainMessage(CHECKING).sendToTarget();
            int count = 0;
            while(count < 5 && !isConnected) {
                isConnected =  NetworkStatusChecker.isNetworkEnable(getContext());
                Log.e("NetworkConfigDialog", getClass().getSimpleName()+" network check="+isConnected+" threadID:"+ Thread.currentThread());
                count++;
                if(!isConnected) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(isConnected) {
                handler.obtainMessage(SUCCESS).sendToTarget();
            } else {
                handler.obtainMessage(FAIL).sendToTarget();
            }
        }
    }



}