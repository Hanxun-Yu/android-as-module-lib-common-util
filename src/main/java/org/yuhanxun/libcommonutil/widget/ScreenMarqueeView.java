package org.yuhanxun.libcommonutil.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

public class ScreenMarqueeView extends TextView implements OnClickListener {
    private float textLength = 0f;// 文本长度
    private float viewWidth = 0f;
    private float step = 0f;// 文字的横坐标
    private float y = 0f;// 文字的纵坐标
    private float temp_view_plus_text_length = 0.0f;// 用于计算的临时变量
    private float temp_view_plus_two_text_length = 0.0f;// 用于计算的临时变量
    public boolean isStarting = false;// 是否开始滚动
    private Paint paint = null;// 绘图样式
    private String text = "";// 文本内容
    private String id = "";
    private long clear_ver = 0;

    public ScreenMarqueeView(Context context) {
        super(context);
        initView();
    }

    public ScreenMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ScreenMarqueeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        paint = getPaint();
        //设置滚动字体颜色
        paint.setColor(Color.WHITE);
//		setOnClickListener(this);
    }


    public void init(WindowManager windowManager) {
        viewWidth = getWidth();
        if (viewWidth == 0) {
            if (windowManager != null) {
                Display display = windowManager.getDefaultDisplay();
                viewWidth = display.getWidth();
            }
        }

    }

    int times;
    public static final int STATE_READY = 0x0010;
    public static final int STATE_RUNNING = 0x0020;
    public static final int STATE_END = 0x0030;
    int state = STATE_END;

    public synchronized boolean canReset() {
        return getState() == STATE_END;
    }

    public synchronized void setText(String id,String text, int times,long clear_ver) {
        changeState(STATE_READY);
        this.text = text;
        this.times = times;
        this.id = id;
        this.clear_ver = clear_ver;
        isStarting = true;

        int textSize = 30;
        switch (textSize) {
            case 0:
                setTextSize(15f);
                break;
            case 1:
                setTextSize(30f);
                break;
            case 2:
                setTextSize(45f);
                break;
            default:
                setTextSize(30f);
                break;
        }
        textLength = paint.measureText(text);
        step = textLength;
        temp_view_plus_text_length = viewWidth + textLength - getPaddingRight();
        temp_view_plus_two_text_length = viewWidth + textLength * 2 - getPaddingRight();
        y = getTextSize() + getPaddingTop();
    }


    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.step = step;
        ss.isStarting = isStarting;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        step = ss.step;
        isStarting = ss.isStarting;
    }

    public static class SavedState extends BaseSavedState {
        public boolean isStarting = false;
        public float step = 0.0f;

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(new boolean[]{isStarting});
            out.writeFloat(step);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
        };

        private SavedState(Parcel in) {
            super(in);
            boolean[] b = null;
            in.readBooleanArray(b);
            if (b != null && b.length > 0)
                isStarting = b[0];
            step = in.readFloat();
        }
    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }

    //	public void startScroll() {
//		isStarting = true;
//		invalidate();
//	}
//
//	public void stopScroll() {
//		isStarting = false;
//		invalidate();
//	}
    OnStateChangedListener onStateChangedListener;
    public interface OnStateChangedListener {
        void onChanged(String id, int state, long clearVer);
    }

    private synchronized void changeState(int state) {
        if(this.state != state) {
            this.state = state;
            if (onStateChangedListener != null)
                onStateChangedListener.onChanged(id,state,clear_ver);
        }
    }

    private int getState() {
        return this.state;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (getState() == STATE_END)
            return;

        if(getState() == STATE_READY)
            changeState(STATE_RUNNING);

        if(getState() ==  STATE_RUNNING) {
            canvas.save();
            canvas.clipRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
            canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
            canvas.restore();
            step += 0.8;// 0.5为文字滚动速度。

            if (step > temp_view_plus_two_text_length) {
                if (times > 1) {
                    times--;
                    step = textLength;
                } else {
                    changeState(STATE_END);
                    setVisibility(INVISIBLE);
                }
            }
            invalidate();
        }
    }

    public void start() {
        setVisibility(VISIBLE);
        invalidate();
    }


    //控制点击停止或者继续运行
    @Override
    public void onClick(View v) {
//		if (isStarting)
//			stopScroll();
//		else
//			startScroll();

    }


    /**
     * Created by szj on 2016/7/7.
     */
    public static class MarqueeTextBean {

        public String flyId;
        public String flyText;
        public String startDate;
        public String endDate;
        public String startTime;
        public String endTime;
        public String locationInfo;
        public String list;
        public int times;
        public String isSend;
        public String versionCode;
        public String flyTextSize;
        public int interval;

        public boolean hasFlied = false;

        public void setBean(MarqueeTextBean bean) {
            flyId = bean.flyId;
            flyText = bean.flyText;
            startDate = bean.startDate;
            endDate = bean.endDate;
            startTime = bean.startTime;
            endTime = bean.endTime;
            locationInfo = bean.locationInfo;
            list = bean.list;
            times = bean.times;
            isSend = bean.isSend;
            versionCode = bean.versionCode;
            flyTextSize = bean.flyTextSize;
            hasFlied = bean.hasFlied;
            interval = bean.interval;

        }

        @Override
        public String toString() {
            return "MarqueeTextBean{" +
                    "flyId='" + flyId + '\'' +
                    ", flyText='" + flyText + '\'' +
                    ", startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", locationInfo='" + locationInfo + '\'' +
                    ", list='" + list + '\'' +
                    ", times='" + times + '\'' +
                    ", isSend='" + isSend + '\'' +
                    ", versionCode='" + versionCode + '\'' +
                    ", flyTextSize='" + flyTextSize + '\'' +
                    ", interval='" + interval + '\'' +
                    ", hasFlied=" + hasFlied +
                    '}';
        }
    }

}
