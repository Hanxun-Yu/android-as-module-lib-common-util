package org.yuhanxun.libcommonutil.log;

public interface Logger {
    enum Level {
        v, d, i, w, e;
    }

    void v(String tag, String msg);

    void v(String tag, String msg, Throwable tr);

    void e(String tag, String msg);

    void e(String tag, String msg, Throwable tr);

    void w(String tag, String msg);

    void w(String tag, String msg, Throwable tr);

    void d(String tag, String msg);

    void d(String tag, String msg, Throwable tr);

    void i(String tag, String msg);

    void i(String tag, String msg, Throwable tr);

    void log(String level, String tag, String msg);

    boolean changePath(String newPath);

}