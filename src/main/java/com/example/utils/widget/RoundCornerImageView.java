package com.example.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.utils.R;



public class RoundCornerImageView extends ImageView {

    public RoundCornerImageView(Context context) {
        super(context);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getShadowAttr(context, attrs);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        getShadowAttr(context, attrs);
    }

    private void getShadowAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView);
        radius = a.getFloat(R.styleable.RoundCornerImageView_roundCorner_radius, 0);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }


    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    int strokeWidth = 0;
    float radius = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        Path clipPath = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        clipPath.addRoundRect(new RectF(0, 0, w, h), radius, radius, Path.Direction.CW);
        canvas.save();
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
        canvas.restore();

        Rect rc = new Rect();
        canvas.getClipBounds(rc);
        RectF rf = new RectF();
        rf.set(rc);
//        rf.top-=1;
//        rf.left-=1;
//        rf.bottom=1;
//        rf.right+=1;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#ffffffff"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawRoundRect(rf,radius,radius, paint);
    }
}