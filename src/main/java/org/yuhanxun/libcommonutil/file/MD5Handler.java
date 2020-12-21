package org.yuhanxun.libcommonutil.file;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by szj on 2017/1/4.
 */

public class MD5Handler extends Handler {
    static String TAG = "MD5Handler_dbstar";
    public static abstract class SingleFileListener {
        final void onCommon(MD5Bean bean) {
            Log.d(TAG,"onCommon" +bean);
            if (bean.expectMD5.equals(bean.realMD5))
                onCorrect(bean);
            else
                onIncorrect(bean);
        }
        public void onCorrect(MD5Bean bean) {
        }

        public void onIncorrect(MD5Bean bean) {
        }
    }

    public static abstract class FileListListener {
        public void onFinish(List<MD5Bean> beans) {
        }

        public void onAllCorrect() {
            Log.d(TAG,"onAllCorrect");

        }

        public void onIncorrect(MD5Bean bean) {
            Log.d(TAG,"onIncorrect:"+bean);

        }
    }

    private ExecutorService executorService;

    public MD5Handler() {
        super(Looper.getMainLooper());
        executorService = Executors.newSingleThreadExecutor();
    }

    public void checkMD5(String path, String expectMD5, SingleFileListener listener) {
        List<MD5Bean> list = new ArrayList<>();
        list.add(new MD5Bean(path, expectMD5));
        MakeMD5Runnable checkRunnable = new MakeMD5Runnable(list,this);
        checkRunnable.setSingleFileListener(listener);
        executorService.execute(checkRunnable);
    }

    boolean findIncorrect;
    public void checkMD5(List<MD5Bean> path$md5, final boolean stopOnIncorrect, final FileListListener listener) {
        final MakeMD5Runnable checkRunnable = new MakeMD5Runnable(path$md5,this);
        findIncorrect = false;
        checkRunnable.setSingleFileListener(new SingleFileListener() {
            @Override
            public void onIncorrect(MD5Bean bean) {
                findIncorrect = true;
                if (stopOnIncorrect) {
                    checkRunnable.setStop(true);
                }
                if (listener != null)
                    listener.onIncorrect(bean);
            }
        });
        checkRunnable.setOnEndListener(new MakeMD5Runnable.OnEndListener() {
            @Override
            public void onEnd() {
                if (listener != null) {
                    listener.onFinish(checkRunnable.getBeans());
                    if (!findIncorrect)
                        listener.onAllCorrect();
                }
            }
        });
        executorService.execute(checkRunnable);
    }


    private static class MakeMD5Runnable implements Runnable {
        public List<MD5Bean> getBeans() {
            return beans;
        }

        List<MD5Bean> beans;
        public void setStop(boolean stop) {
            this.stop = stop;
        }

        boolean stop = false;
        Handler handler;

        MakeMD5Runnable(List<MD5Bean> beans,Handler handler) {
            this.beans = beans;
            this.handler = handler;
        }

        public void setSingleFileListener(SingleFileListener singleFileListener) {
            this.singleFileListener = singleFileListener;
        }

        SingleFileListener singleFileListener;

        public void setOnEndListener(OnEndListener onEndListener) {
            this.onEndListener = onEndListener;
        }

        OnEndListener onEndListener;

        @Override
        public void run() {
            if (beans != null) {
                for (final MD5Bean item : beans) {
                    if (!stop) {
                        try {
                            item.realMD5 = MD5.getFileMD5String(new File(item.path));
                        } catch (IOException e) {
                            e.printStackTrace();
                            item.realMD5 = "file not found";
                        }
                        if (singleFileListener != null && !stop) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    singleFileListener.onCommon(item);

                                }
                            });
                        }
                    }
                }
            }
            if(onEndListener != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onEndListener.onEnd();
                    }
                });
            }
        }
        public interface OnEndListener {
            void onEnd();
        }
    }

    public static class MD5Bean {
        public String path;
        public String expectMD5;
        public String realMD5;

        public MD5Bean(String path, String expectMD5) {
            this.path = path;
            this.expectMD5 = expectMD5;
        }

        @Override
        public String toString() {
            return "MD5Bean{" +
                    "path='" + path + '\'' +
                    ", expectMD5='" + expectMD5 + '\'' +
                    ", realMD5='" + realMD5 + '\'' +
                    '}';
        }
    }
}
