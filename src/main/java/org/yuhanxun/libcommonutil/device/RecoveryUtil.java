package org.yuhanxun.libcommonutil.device;

import android.content.Context;
import android.os.Handler;
import android.os.RecoverySystem;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dbstar-mac on 16/7/19.
 */
public class RecoveryUtil {
    static String TAG = "OTAUtil_yhx";

    public interface OTAListener {
        void onSuccess();

        void onFailed(String msg);

        void onProgress(int progress);
    }

    Context context;
    OTAListener otaListener;
    String packagePath;
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);
    Handler handler;

    public RecoveryUtil(Context context, String packagePath, OTAListener otaListener) {
        this.context = context;
        this.otaListener = otaListener;
        this.packagePath = packagePath;
        handler = new Handler(context.getMainLooper());
    }

    public void doUpgrade() {
        executorService.execute(new CheckRunnable(otaListener, packagePath));
        //设置为上电直接开机
//                try {
//                    TvManager.getInstance().seten
//                    fm.setEnvironmentPowerMode(EnumAcOnPowerOnMode.values()[0]);
//                } catch (TvCommonException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }

    }

    private class CheckRunnable implements Runnable {
        OTAListener progressListener;
        String packagePath;

        public CheckRunnable(OTAListener progressListener, String packagePath) {
            this.progressListener = progressListener;
            this.packagePath = packagePath;
        }

        @Override
        public void run() {
            try {
                RecoverySystem.verifyPackage(new File(packagePath), new RecoverySystem.ProgressListener() {
                    @Override
                    public void onProgress(final int progress) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (otaListener != null)
                                    otaListener.onProgress(progress);
                            }
                        });

                    }
                }, null);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (otaListener != null)
                            otaListener.onSuccess();
                    }
                });
                RecoverySystem.installPackage(context,new File(packagePath));

            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (otaListener != null)
                            otaListener.onFailed(e.getMessage());
                    }
                });
            }
        }
    }
}
