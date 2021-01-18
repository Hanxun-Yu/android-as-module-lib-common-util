package org.yuhanxun.libcommonutil.log;

import android.util.Log;

import org.yuhanxun.libcommonutil.file.FileRW;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


public class FileLogFactory {

    public static ILog getFileLog(Object tagObj, String mLogPath) {
        AndroidLog log = new AndroidLog(tagObj);
        FileLogProxyHandler handler = new FileLogProxyHandler(log, log.getTAG(), mLogPath);


        Object obj = Proxy.newProxyInstance(
                log.getClass().getClassLoader(),
                new Class[]{ILog.class},
                handler
        );
        return (ILog) obj;
    }

    private static class FileLogProxyHandler implements InvocationHandler {
        private final Object mTarget;
        private final String mTag;
        private final WriteRunn mRunn;
        private final String mLogPath;
        private final ExecutorService mExecutorService;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

        public FileLogProxyHandler(Object target, String tag, String logPath) {
            mTarget = target;
            mTag = tag;
            mLogPath = logPath;
            mExecutorService = Executors.newCachedThreadPool();
            mExecutorService.execute(mRunn = new WriteRunn());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            method.invoke(mTarget, args);
            Log.d("TEST", method.getName() + " args:" + Arrays.toString(args));
            String level = method.getName().toUpperCase();
            String msg = (String) args[0];
            Throwable excep = args.length == 2 ? (Throwable) args[1] : null;

            mRunn.addLog(assembleMsg(level, mTag, msg, excep));

            return null;
        }


        private class WriteRunn implements Runnable {
            BlockingQueue<String> queue = new LinkedBlockingQueue<>();

            public WriteRunn() {
            }

            public void addLog(String log) {
                try {
                    queue.put(log);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void run() {
                String log = null;
                while (true) {
                    try {
                        log = queue.take();
                        FileRW.writeLine2File(mLogPath, log);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private String assembleMsg(String level, String tag, String msg, Throwable tr) {
            StringBuilder mBuilder = new StringBuilder();
            mBuilder.append("[").append(level).append("] ");
            mBuilder.append("[").append(getTimeStringYyyyMMddhhmmssSSS())
                    .append("]")
                    .append(" ")
                    .append(tag)
                    .append(" ")
                    .append(msg);
            if (tr != null) {
                mBuilder.append("\n");
                final Writer result = new StringWriter();
                final PrintWriter printWriter = new PrintWriter(result);
                tr.printStackTrace(printWriter);
                String expMessage = result.toString();
                mBuilder.append(expMessage);
            }
            return mBuilder.toString();
        }

        public String getTimeStringYyyyMMddhhmmssSSS() {
            return df.format(new Date());
        }
    }
}
