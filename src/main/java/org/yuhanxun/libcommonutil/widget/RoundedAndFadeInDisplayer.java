package org.yuhanxun.libcommonutil.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * Created by szj on 2016/9/23.
 */
public class RoundedAndFadeInDisplayer implements BitmapDisplayer {

    protected final int cornerRadius;
    protected final int margin;

    private final int durationMillis;
    private final boolean animateFromNetwork;
    private final boolean animateFromDisk;
    private final boolean animateFromMemory;

    public RoundedAndFadeInDisplayer(int cornerRadiusPixels, int durationMillis) {
        this(cornerRadiusPixels, 0, durationMillis, true, true, true);
    }

    public RoundedAndFadeInDisplayer(int cornerRadiusPixels, int marginPixels, int durationMillis, boolean animateFromNetwork, boolean animateFromDisk, boolean animateFromMemory) {
        this.cornerRadius = cornerRadiusPixels;
        this.margin = marginPixels;
        this.durationMillis = durationMillis;
        this.animateFromNetwork = animateFromNetwork;
        this.animateFromDisk = animateFromDisk;
        this.animateFromMemory = animateFromMemory;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if(!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        } else {
            imageAware.setImageDrawable(new RoundedAndFadeInDisplayer.RoundedDrawable(bitmap, this.cornerRadius, this.margin));
        }

        if(this.animateFromNetwork && loadedFrom == LoadedFrom.NETWORK || this.animateFromDisk && loadedFrom == LoadedFrom.DISC_CACHE || this.animateFromMemory && loadedFrom == LoadedFrom.MEMORY_CACHE) {
            animate(imageAware.getWrappedView(), this.durationMillis);
        }
    }

    public static void animate(View imageView, int durationMillis) {
        if(imageView != null) {
            AlphaAnimation fadeImage = new AlphaAnimation(0.0F, 1.0F);
            fadeImage.setDuration((long)durationMillis);
            fadeImage.setInterpolator(new DecelerateInterpolator());
            imageView.startAnimation(fadeImage);
        }

    }

    public static class RoundedDrawable extends Drawable {
        protected final float cornerRadius;
        protected final int margin;
        protected final RectF mRect = new RectF();
        protected final RectF mBitmapRect;
        protected final BitmapShader bitmapShader;
        protected final Paint paint;

        public RoundedDrawable(Bitmap bitmap, int cornerRadius, int margin) {
            this.cornerRadius = (float)cornerRadius;
            this.margin = margin;
            this.bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            this.mBitmapRect = new RectF((float)margin, (float)margin, (float)(bitmap.getWidth() - margin), (float)(bitmap.getHeight() - margin));
            this.paint = new Paint();
            this.paint.setAntiAlias(true);
            this.paint.setShader(this.bitmapShader);
        }

        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            this.mRect.set((float)this.margin, (float)this.margin, (float)(bounds.width() - this.margin), (float)(bounds.height() - this.margin));
            Matrix shaderMatrix = new Matrix();
            shaderMatrix.setRectToRect(this.mBitmapRect, this.mRect, Matrix.ScaleToFit.FILL);
            this.bitmapShader.setLocalMatrix(shaderMatrix);
        }

        public void draw(Canvas canvas) {
            canvas.drawRoundRect(this.mRect, this.cornerRadius, this.cornerRadius, this.paint);
        }

        public int getOpacity() {
            return -3;
        }

        public void setAlpha(int alpha) {
            this.paint.setAlpha(alpha);
        }

        public void setColorFilter(ColorFilter cf) {
            this.paint.setColorFilter(cf);
        }
    }
}
