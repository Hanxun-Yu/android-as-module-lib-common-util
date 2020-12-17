package com.example.utils.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dbstar-mac on 2016/10/17.
 */

public class ApkInstaller {
    List<String> apkList;
    Handler handler;

    public void setOnApkInstallListener(OnApkInstallListener onApkInstallListener) {
        this.onApkInstallListener = onApkInstallListener;
    }

    private OnApkInstallListener onApkInstallListener;

    public ApkInstaller(List<String> list) {
        apkList = list;
        handler = new Handler(Looper.getMainLooper());
    }

    public void startInstall() {
        if (apkList != null) {
            new Thread() {
                @Override
                public void run() {
                    for (String path : apkList) {
                        doInstall(path);
                    }
                    if (onApkInstallListener != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onApkInstallListener.onFinish();
                            }
                        });
                    }
                }
            }.start();
        }
    }

    public void startSuInstall() {
        if (apkList != null) {
            new Thread() {
                @Override
                public void run() {
                    for (String path : apkList) {
                        doSuInstall(path);
                    }
                    if (onApkInstallListener != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onApkInstallListener.onFinish();
                            }
                        });
                    }
                }
            }.start();
        }
    }

    public String doInstall(final String apk) {
        String[] args = {"pm", "install", "-r", apk};
        if (onApkInstallListener != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onApkInstallListener.onStart(apk);
                }
            });
        }
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (IOException e) {
            result += e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                result += e.getMessage();
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        Log.v("szj", result);
        if (onApkInstallListener != null) {
            final String finalResult = result;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (finalResult.toLowerCase().contains("success")) {
                        onApkInstallListener.onSuccess(apk);
                    } else {
                        onApkInstallListener.onFailed(apk, finalResult);
                    }
                }
            });
        }
        return result;
    }

    public String doSuInstall(final String apk) {
//        String[] args = {"pm", "install", "-r", apk};
        String cmd = "pm install -r "+apk;
        if (onApkInstallListener != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onApkInstallListener.onStart(apk);
                }
            });
        }
        String result = "";
        ProcessBuilder pb = null;
        InputStream errIs = null;
        InputStream inIs = null;
        OutputStream out;
        Process process = null;

        try {

            pb = new ProcessBuilder("/system/xbin/su");
            //othre pb setting,such as dir, env
            process = pb.start();

            out = process.getOutputStream();
            out.write(cmd.getBytes());


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (IOException e) {
            result += e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                result += e.getMessage();
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        Log.v("szj", result);
        if (onApkInstallListener != null) {
            final String finalResult = result;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (finalResult.toLowerCase().contains("success")) {
                        onApkInstallListener.onSuccess(apk);
                    } else {
                        onApkInstallListener.onFailed(apk, finalResult);
                    }
                }
            });
        }
        return result;
    }

    public static class OnApkInstallListener {
        public void onStart(String apk) {
        }

        public void onSuccess(String apk) {
        }

        public void onFinish() {
        }

        public void onFailed(String apk, String msg) {
        }
    }

    public static List<PackageInfo> getInstalledApp(Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        List<PackageInfo> ret = new ArrayList<>();
        PackageInfo packageInfo;
        for (int i = 0; i < packages.size(); i++) {
            packageInfo = packages.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                ret.add(packageInfo);//如果非系统应用，则添加至appList
            }
        }
        return ret;
    }

    public static List<PackageInfo> getAllApp(Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        List<PackageInfo> ret = new ArrayList<>();
        PackageInfo packageInfo;
        for (int i = 0; i < packages.size(); i++) {
            packageInfo = packages.get(i);
            ret.add(packageInfo);//如果非系统应用，则添加至appList
        }
        return ret;
    }
}
