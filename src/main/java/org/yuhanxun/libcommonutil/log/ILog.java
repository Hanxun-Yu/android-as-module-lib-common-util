package org.yuhanxun.libcommonutil.log;

/**
 * Created by yuhanxun
 * 2018/5/21
 */
public interface ILog {
    void e(String tag, String content,Throwable tr);
    void w(String tag, String content,Throwable tr);
    void d(String tag, String content,Throwable tr);
    void i(String tag, String content,Throwable tr);
    void v(String tag, String content,Throwable tr);
}
