package org.yuhanxun.libcommonutil.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

public class HorizonScrollTextView1 extends TextView implements OnClickListener {
	private float textLength = 0f;// 文本长度
	private float viewWidth = 0f;
	private float step = 0f;// 文字的横坐标
	private float y = 0f;// 文字的纵坐标
	private float temp_view_plus_text_length = 0.0f;// 用于计算的临时变量
	private float temp_view_plus_two_text_length = 0.0f;// 用于计算的临时变量
	public boolean isStarting = false;// 是否开始滚动
	private Paint paint = null;// 绘图样式
	private String text = "";// 文本内容

	public HorizonScrollTextView1(Context context) {
		super(context);
		initView();
	}

	public HorizonScrollTextView1(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HorizonScrollTextView1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		paint = getPaint();
		//设置滚动字体颜色
		paint.setColor(Color.WHITE);
//		setOnClickListener(this);
	}

	public void addText(String t, float size) {
		text = t;
		setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		if (!isStarting)
			nextText();
	}

	public void init(float width) {
		viewWidth = width;
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

	public void nextText() {
//		if (text != null && !text.equals("")) {
			isStarting = true;
			textLength = paint.measureText(text);
			step = textLength;
			temp_view_plus_text_length = viewWidth + textLength - getPaddingRight();
			temp_view_plus_two_text_length = viewWidth + textLength * 2 - getPaddingRight();
			y = getTextSize() + getPaddingTop();
//		} else {
//			isStarting = false;
//		}
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Log.d("HorizonScrollTextView1","onSaveInstanceState");

		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);

		ss.step = step;
		ss.isStarting = isStarting;

		return ss;

	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		Log.d("HorizonScrollTextView1","onRestoreInstanceState");
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
			out.writeBooleanArray(new boolean[] { isStarting });
			out.writeFloat(step);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

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
			boolean[] b = new boolean[1];
			if (in != null) {
				in.readBooleanArray(b);
				if (b != null && b.length > 0)
					isStarting = b[0];
				step = in.readFloat();
			}
		}
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

	@Override
	public void onDraw(Canvas canvas) {
		canvas.save();
		canvas.clipRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
		canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
		canvas.restore();
		if (!isStarting) {
			return;
		}
		step += 0.8;// 0.5为文字滚动速度。
		if (step > temp_view_plus_two_text_length) {
			step = textLength;
//			nextText();
		}
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
}
