package org.yuhanxun.libcommonutil.log;

import android.util.Log;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.yuhanxun.libcommonutil.date.DateUtil;
import org.yuhanxun.libcommonutil.file.FileRW;

/**
 * Created by Crashxun dbstar-mac on 2017/2/9.
 */

public class LogToFile implements Logger {
    private void set_logPath(String _logPath) {
        this._logPath = _logPath;
    }

    private String _logPath;
    private ExecutorService _executorService;

    private LogToFile() {
        _executorService = Executors.newCachedThreadPool();
    }

    private static final LogToFile instance = new LogToFile();

    public static LogToFile getInstance(String logPath) {
        instance.set_logPath(logPath);
        return instance;
    }

    public synchronized boolean changePath(String newPath) {
        if(!_logPath.equals(newPath)) {
            synchronized (this) {
                File newFile = new File(newPath);
                if (newFile.exists()) {
                    //该日志已存在,把老内容写进该文件
                    String oldContent = FileRW.fileToString(_logPath);
                    FileRW.writeLine2File(newPath, oldContent);
                    new File(_logPath).delete();
                    _logPath = newPath;
                    return true;
                } else {
                    File oldFile = new File(_logPath);
                    boolean ret = oldFile.renameTo(new File(newPath));
                    if (ret) {
                        _logPath = newPath;
                    }
                    return ret;
                }
            }
        } else {
            return false;
        }
    }


    private void write(String content) {
        _executorService.execute(new WriteRunn(content, this));
    }

    @Override
    public void v(String tag, String msg) {
        Log.v(tag, msg);
        write(assembleMsg("V", tag, msg, null));
    }

    @Override
    public void v(String tag, String msg, Throwable tr) {
        Log.v(tag, msg);
        write(assembleMsg("V", tag, msg, null));
    }

    @Override
    public void e(String tag, String msg) {
        Log.e(tag, msg);
        write(assembleMsg("E", tag, msg, null));
    }

    @Override
    public void e(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
        write(assembleMsg("E", tag, msg, tr));
    }

    @Override
    public void w(String tag, String msg) {
        Log.w(tag, msg);
        write(assembleMsg("W", tag, msg, null));

    }

    @Override
    public void w(String tag, String msg, Throwable tr) {
        Log.w(tag, msg, tr);
        write(assembleMsg("W", tag, msg, tr));
    }

    @Override
    public void d(String tag, String msg) {
        Log.d(tag, msg);
        write(assembleMsg("D", tag, msg, null));
    }

    @Override
    public void d(String tag, String msg, Throwable tr) {
        Log.d(tag, msg, tr);
        write(assembleMsg("D", tag, msg, tr));
    }

    @Override
    public void i(String tag, String msg) {
        Log.i(tag, msg);
        write(assembleMsg("I", tag, msg, null));
    }

    @Override
    public void i(String tag, String msg, Throwable tr) {
        Log.i(tag, msg, tr);
        write(assembleMsg("I", tag, msg, tr));
    }

    @Override
    public void log(String level, String tag, String msg) {
        switch (Logger.Level.valueOf(level)) {
            case v:
                v(tag, msg);
                break;
            case d:
                d(tag, msg);
                break;
            case i:
                i(tag, msg);
                break;
            case w:
                w(tag, msg);
                break;
            case e:
                e(tag, msg);
                break;
        }
    }

    private class WriteRunn implements Runnable {
        final Object lock;
        String content;

        public WriteRunn(String content, Object lock) {
            this.lock = lock;
            this.content = content;
        }

        @Override
        public void run() {
            synchronized (lock) {
                FileRW.writeLine2File(_logPath, content);
            }
        }
    }

    private String assembleMsg(String level, String tag, String msg, Throwable tr) {
        StringBuilder mBuilder = new StringBuilder();
        mBuilder.append("[").append(level).append("] ");
        mBuilder.append("[").append(DateUtil.getTimeMillisecondPrefix())
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
}
