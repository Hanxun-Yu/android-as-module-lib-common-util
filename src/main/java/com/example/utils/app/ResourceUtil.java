package com.example.utils.app;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by yuhanxun on 16/6/24.
 */
public class ResourceUtil {
    public static String getFromRaw(Context context,int id) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().openRawResource(id));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            int lineNum = 0;
            while ((line = bufReader.readLine()) != null) {
                if(lineNum != 0)
                    Result+="\n";
                lineNum++;
                Result += line;
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
