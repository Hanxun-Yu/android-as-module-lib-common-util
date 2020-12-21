package org.yuhanxun.libcommonutil.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import org.yuhanxun.libcommonutil.sound.SoundPoolUtil;

/**
 * Created by yuhanxun
 * 2018/4/13
 */
public class TestActivity extends Activity {
    SoundPoolUtil spu;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SoundPoolUtil(this);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        Button button = new Button(this);
        Button button2 = new Button(this);
        Button button3 = new Button(this);


        linearLayout.addView(button);
        linearLayout.addView(button2);
        linearLayout.addView(button3);

        setContentView(linearLayout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spu.shootSound();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spu.videoRecordStartSound();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spu.videoRecordStopSound();
            }
        });

    }
}
