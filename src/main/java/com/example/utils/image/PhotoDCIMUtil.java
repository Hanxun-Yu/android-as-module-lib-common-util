package com.example.utils.image;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by yuhanxun
 * 2018/6/5
 */
public class PhotoDCIMUtil {
    public static void notifySystem(Context context,File file) {
        if(context == null)
            return;
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    file.getAbsolutePath(), file.getName(), null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Uri localUri = Uri.fromFile(file);
        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
        context.sendBroadcast(localIntent);
    }
}
