package org.yuhanxun.libcommonutil.file;

import android.content.Context;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yuhanxun
 * 2019/4/16
 * description:
 */
public class AndroidResRW {
    public static String getFromRaw(Context context, int resId) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().openRawResource(resId));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStrFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getByteArrFromAssets(Context context, String fileName) {
        byte[] ret = null;
        ByteArrayOutputStream output = null;
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(fileName);
            output = new ByteArrayOutputStream();
            byte[] temp = new byte[is.available()];
            while (-1 != is.read(temp))
                output.write(temp);
            output.flush();

            ret = output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static InputStream getInputFromRaw(Context context, int resId) {
        return context.getResources().openRawResource(resId);
    }

    public static InputStream getInputFromAssets(Context context, String fileName) {
        try {
            return context.getResources().getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
