package org.yuhanxun.libcommonutil.log;

public interface ILog {

    void v(String msg);

    void v(String msg, Throwable tr);

    void e(String msg);

    void e(String msg, Throwable tr);

    void w(String msg);

    void w(String msg, Throwable tr);

    void d(String msg);

    void d(String msg, Throwable tr);

    void i(String msg);

    void i(String msg, Throwable tr);
}