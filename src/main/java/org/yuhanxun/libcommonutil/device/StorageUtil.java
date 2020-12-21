package org.yuhanxun.libcommonutil.device;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

/**
 * Created by Crashxun dbstar-mac on 2017/1/12.
 */

public class StorageUtil {
    public static long readSDCard() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSizeLong();
            long blockCount = sf.getBlockCountLong();
            long availCount = sf.getAvailableBlocksLong();
            Log.d("", "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
            Log.d("", "可用的block数目：:" + availCount + ",可用大小:" + availCount * blockSize / 1024 + "KB");
            return availCount * blockSize;
        }
        return 0;
    }

    public static long readSystem() {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = 0;
        long blockCount = 0;
        long availCount = 0;
        try {
            blockSize = sf.getBlockSizeLong();
            blockCount = sf.getBlockCountLong();
            availCount = sf.getAvailableBlocksLong();
            Log.d("", "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
            Log.d("", "可用的block数目：:" + availCount + ",可用大小:" + availCount * blockSize / 1024 + "KB");
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
        }
        return availCount * blockSize;
    }
}
