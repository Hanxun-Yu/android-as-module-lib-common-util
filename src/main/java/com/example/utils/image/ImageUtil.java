package com.example.utils.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

public class ImageUtil {

    private static final String TAG = "ImageUtil";
    /**
     * 缓存集合
     */
    private static Hashtable<Integer, SoftReference<Bitmap>> mImageCache //
            = new Hashtable<Integer, SoftReference<Bitmap>>();

    /**
     * 根据id返回一个处理后的图片
     *
     * @param res
     * @param resID
     * @return
     */
    public static Bitmap getImageBitmap(Resources res, int resID) {
        // 先去集合中取当前resID是否已经拿过图片，如果集合中有，说明已经拿过，直接使用集合中的图片返回
        SoftReference<Bitmap> reference = mImageCache.get(resID);
        if (reference != null) {
            Bitmap bitmap = reference.get();
            if (bitmap != null) {// 从内存中取
                Log.i(TAG, "从内存中取");
                return bitmap;
            }
        }
        // 如果集合中没有，就调用getInvertImage得到一个图片，需要向集合中保留一张，最后返回当前图片
        Log.i(TAG, "重新加载");
        Bitmap invertBitmap = getInvertBitmap(res, resID);
        // 在集合中保存一份，便于下次获取时直接在集合中获取
        mImageCache.put(resID, new SoftReference<Bitmap>(invertBitmap));
        return invertBitmap;
    }

    /**
     * 根据图片的id，获取到处理之后的图片
     *
     * @param resID
     * @return
     */
    public static Bitmap getInvertBitmap(Resources res, int resID) {
        // 1.获取原图
        Bitmap sourceBitmap = BitmapFactory.decodeResource(res, resID);

        // 2.生成倒影图片
        Matrix m = new Matrix(); // 图片矩阵
        m.setScale(1.0f, -1.0f); // 让图片按照矩阵进行反转
        Bitmap invertBitmap = Bitmap.createBitmap(sourceBitmap, 0, sourceBitmap.getHeight() / 2, sourceBitmap.getWidth(), sourceBitmap.getHeight() / 2, m,
                false);

        // 3.两张图片合成一张图片
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), (int) (sourceBitmap.getHeight() * 1.5 + 5), Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap); // 为合成图片指定一个画板
        canvas.drawBitmap(sourceBitmap, 0f, 0f, null); // 将原图片画在画布的上方
        canvas.drawBitmap(invertBitmap, 0f, sourceBitmap.getHeight() + 5, null); // 将倒影图片画在画布的下方

        // 4.添加遮罩效果
        Paint paint = new Paint();
        // 设置遮罩的颜色，这里使用的是线性梯度
        LinearGradient shader = new LinearGradient(0, sourceBitmap.getHeight() + 5, 0, resultBitmap.getHeight(), 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // 设置模式为：遮罩，取交集
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0, sourceBitmap.getHeight() + 5, sourceBitmap.getWidth(), resultBitmap.getHeight(), paint);

        return resultBitmap;
    }

    public static Bitmap getInvertBitmap(Bitmap srcMap) {
        if (srcMap == null) {
            throw new IllegalArgumentException("srcMap is null");
        }
        Bitmap sourceBitmap = srcMap;

        // 2.生成倒影图片
        Matrix m = new Matrix(); // 图片矩阵
        m.setScale(1.0f, -1.0f); // 让图片按照矩阵进行反转
        Bitmap invertBitmap = Bitmap.createBitmap(sourceBitmap, 0, sourceBitmap.getHeight() / 2, sourceBitmap.getWidth(), sourceBitmap.getHeight() / 2, m,
                false);

        // 3.两张图片合成一张图片
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), (int) (sourceBitmap.getHeight() * 1.5 + 5), Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap); // 为合成图片指定一个画板
        canvas.drawBitmap(sourceBitmap, 0f, 0f, null); // 将原图片画在画布的上方
        canvas.drawBitmap(invertBitmap, 0f, sourceBitmap.getHeight() + 5, null); // 将倒影图片画在画布的下方

        // 4.添加遮罩效果
        Paint paint = new Paint();
        // 设置遮罩的颜色，这里使用的是线性梯度
        LinearGradient shader = new LinearGradient(0, sourceBitmap.getHeight() + 5, 0, resultBitmap.getHeight(), 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // 设置模式为：遮罩，取交集
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0, sourceBitmap.getHeight() + 5, sourceBitmap.getWidth(), resultBitmap.getHeight(), paint);

        return resultBitmap;
    }

    public static void loadImage(String path, ImageView view, DisplayImageOptions options, ImageLoadingListener listener) {
        if (listener != null)
            ImageLoader.getInstance().displayImage(path, view, options, listener);
        else
            ImageLoader.getInstance().displayImage(path, view, options);
    }

    public static void loadImage(String path, ImageView view, DisplayImageOptions options) {
        loadImage(path, view, options, null);
    }

    public static void loadImageLocalFile(String path, ImageView view, DisplayImageOptions options) {
        File mFile = new File(path);
        Uri uri = Uri.fromFile(mFile);
        loadImage(uri.toString(),view,options);
    }

    public static void loadImageSync(String path, ImageView view, DisplayImageOptions options) {
        File mFile = new File(path);
        Uri uri = Uri.fromFile(mFile);
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(uri.toString(), options);
        view.setImageBitmap(bitmap);
    }


    /**
     * 获取圆角位图的方法
     *
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 获取圆角位图的方法
     *
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCornerFixXY(Bitmap bitmap, int pixels, ImageView iv) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, iv.getWidth(), iv.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public static Drawable createStateListDrawable(String path) {
        StateListDrawable states = new StateListDrawable();
        Drawable d = Drawable.createFromPath(path);
        Drawable d2 = Drawable.createFromPath(path);
        d2.setAlpha(80);

        states.addState(new int[]{android.R.attr.state_pressed}, d2);
        states.addState(new int[]{android.R.attr.state_focused}, d2);
        states.addState(new int[]{android.R.attr.state_selected}, d2);
        states.addState(new int[]{}, d);

        return states;
    }

    public static Drawable createStateListDrawable(String icon, String icon_press) {
        StateListDrawable states = new StateListDrawable();
        Drawable d = Drawable.createFromPath(icon);
        Drawable d2 = Drawable.createFromPath(icon_press);

        states.addState(new int[]{android.R.attr.state_pressed}, d2);
        states.addState(new int[]{android.R.attr.state_focused}, d2);
        states.addState(new int[]{android.R.attr.state_selected}, d2);
        states.addState(new int[]{}, d);

        return states;
    }
}