package org.yuhanxun.libcommonutil.crashCatcher;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.yuhanxun.libcommonutil.date.DateUtil;
import org.yuhanxun.libcommonutil.file.FileMover;
import org.yuhanxun.libcommonutil.file.FileRW;
import org.yuhanxun.libcommonutil.log.XLog;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hzcaoyanming
 */
public class CrashHandler implements UncaughtExceptionHandler {

    /**
     * TAG
     */
    private static final String TAG = "CrashHandler";

    /**
     * uploadUrl
     * 服务器的地址，根据自己的情况进行更改
     */
    private static final String uploadUrl = "http://3.saymagic.sinaapp.com/ReceiveCrash.php";

    /**
     * logFold
     * 本地log文件的存放地址
     */
    private String logFold = "/sdcard";
    /**
     * mDefaultHandler
     */
    private UncaughtExceptionHandler defaultHandler;

    /**
     * instance
     */
    private static CrashHandler instance = new CrashHandler();


    /**
     * formatter
     */
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * context
     */
    private Context context;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return getInstance(null);
    }

    public static CrashHandler getInstance(String logFold) {
        if (logFold != null) {
            instance.setLogFold(logFold);
        }
        return instance;
    }

    public void setLogFold(String logFold) {
        this.logFold = logFold;
    }

    /**
     * @param ctx 初始化，此处最好在Application的OnCreate方法里来进行调用
     */
    public void init(Context ctx) {
        this.context = ctx;
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        handler = new Handler(ctx.getMainLooper());
    }

    Handler handler;

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        handleException(ex);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, "异常,即将退出.", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        defaultHandler.uncaughtException(thread, ex);

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (defaultHandler != null) {
//                    //如果用户没有处理则让系统默认的异常处理器来处理
//                }
//            }
//        },3000);
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        Map<String, String> infos = collectDeviceInfo(context);
        if (listener != null) {
            listener.onCrash(infos, ex);
        }
        writeCrashInfoToFile(infos, ex);


//        XLog.d("TAG", "收到崩溃");
//
//		restart();
        return true;
    }

    public void setListener(OnCrashListener listener) {
        this.listener = listener;
    }

    OnCrashListener listener;

    public interface OnCrashListener {
        void onCrash(Map<String, String> apkInfo, Throwable throwable);
    }

    /**
     * @param ctx 手机设备相关信息
     */
    public Map<String, String> collectDeviceInfo(Context ctx) {
        /** infos */
        Map<String, String> infos = new HashMap<String, String>();
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
                infos.put("crashTime", formatter.format(new Date()));
            }
        } catch (NameNotFoundException e) {
            XLog.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                XLog.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                XLog.e(TAG, "an error occured when collect crash info", e);
            }
        }
        return infos;
    }

    /**
     * @param ex 将崩溃写入文件系统
     */
    private void writeCrashInfoToFile(Map<String, String> infos, Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        //这里把刚才异常堆栈信息写入SD卡的Log日志里面
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            String sdcardPath = Environment.getExternalStorageDirectory().getPath();
//            String filePath = sdcardPath + "/cym/crash/";
        writeLog(sb.toString(), logFold);
//        }
    }

    /**
     * @param Xlog
     * @param foldPath
     * @return 返回写入的文件路径
     * 写入Log信息的方法，写入到SD卡里面
     */
    private void writeLog(String Xlog, String foldPath) {
//        CharSequence timestamp = new Date().toString().replace(" ", "");

        String prefix = "crash";
        CharSequence timestamp = DateUtil.getTimePrefix2();
        String filename = foldPath + File.separator + prefix + "-" + timestamp + ".txt";

        FileMover.mkParentDirs(filename);
        FileRW.write2File(filename, Xlog);
//        try {
//            XLog.d("TAG", "写入到SD卡里面");
//            //			FileOutputStream stream = new FileOutputStream(new File(filename));
//            //			OutputStreamWriter output = new OutputStreamWriter(stream);
//            file.createNewFile();
//            FileWriter fw = new FileWriter(file, true);
//            BufferedWriter bw = new BufferedWriter(fw);
//            //写入相关Log到文件
//            bw.write(Xlog);
//            bw.newLine();
//            bw.close();
//            fw.close();
//            return filename;
//        } catch (IOException e) {
//            XLog.e(TAG, "an error occured while writing file...", e);
//            e.printStackTrace();
//            return null;
//        }
    }

//	private void restart(){
//		 try{
//             Thread.sleep(2000);
//         }catch (InterruptedException e){
//             XLog.e(TAG, "error : ", e);
//         }
//         Intent intent = new Intent(context.getApplicationContext(), SendCrashActivity.class);
//         PendingIntent restartIntent = PendingIntent.getActivity(
//        		 context.getApplicationContext(), 0, intent,
//                 Intent.FLAG_ACTIVITY_NEW_TASK);
//         //退出程序
//         AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//         mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
//                 restartIntent); // 1秒钟后重启应用
//	}

}
