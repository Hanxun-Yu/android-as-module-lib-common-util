package com.example.utils;

import android.app.Application;
import android.content.Intent;
import android.os.Looper;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.example.utils.file.MD5;
import com.example.utils.file.MD5Handler;
import com.example.utils.test.TestActivity;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    String TAG = "ApplicationTest_dbstar";

    public void testMd5Handler() {
        String file_1 = "/data/dbstar/film%2Fxiju%2Fshentanjiadao.jpg";
        String file_1_realMD5 = "293619b38bc95203492bc8dbbb3a5de6";
        String file_2 = "/data/dbstar/film%2Fxiju%2Fshitoudemeng.png";
        String file_2_realMD5 = "80cf511e240ae9e02bd22baadf200d92";
        String file_3 = "/data/dbstar/film%2Fxiju%2Fxiaogongzhengwulin.jpg";
        String file_3_realMD5 = "ec0971eb7cb1b04eed3d91b2a0ce7a94";
        MD5Handler md5Handler = new MD5Handler();
//        md5Handler.checkMD5(file_1, file_1_realMD5+"a",
//                new MD5Handler.SingleFileListener() {
//                    @Override
//                    public void onCorrect(MD5Handler.MD5Bean bean) {
//                        super.onCorrect(bean);
//                        Log.d(TAG,"onCorrect:"+bean.toString());
//
//                    }
//
//                    @Override
//                    public void onIncorrect(MD5Handler.MD5Bean bean) {
//                        super.onIncorrect(bean);
//                        Log.d(TAG,"onIncorrect:"+bean.toString());
//                    }
//                });
        List<MD5Handler.MD5Bean> beans = new ArrayList<>();
        beans.add(new MD5Handler.MD5Bean(file_1, file_1_realMD5 + "a"));
        beans.add(new MD5Handler.MD5Bean(file_2, file_2_realMD5));
        beans.add(new MD5Handler.MD5Bean(file_3, file_3_realMD5 + "a"));
        md5Handler.checkMD5(beans, false, new MD5Handler.FileListListener() {
            @Override
            public void onFinish(List<MD5Handler.MD5Bean> beans) {
                Log.d(TAG, "onFinish:" + beans.toString());
            }

            @Override
            public void onAllCorrect() {
                Log.d(TAG, "onAllCorrect");
            }

            @Override
            public void onIncorrect(MD5Handler.MD5Bean bean) {
                Log.d(TAG, "onIncorrect:" + bean.toString());
            }
        });
        Looper.loop();
    }

    public void testActivity() {
        Intent intent = new Intent(getContext(), TestActivity.class);
        getContext().startActivity(intent);
        Looper.loop();
    }

    public void testMd5() {
        String path = "/data/dbstar/res/ch/movie.json";
        String path2 = "/data/dbstar/res/upload/apk/DataSync-dev_multiple_device_c6_v1.4.1.apk";
        String lastMD5 = null;
        String lastMD52 = "436fb6955fb8d96cbbcd09be9dff1800";
        //error "304340d8de32099180395ab6d82ad2df"
        String md5;
        String md52;
        MD5Handler handler = new MD5Handler();
        List<MD5Handler.MD5Bean> beans = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            beans.add(new MD5Handler.MD5Bean(path2, lastMD52));
        }
        handler.checkMD5(beans, true, new MD5Handler.FileListListener() {
            @Override
            public void onFinish(List<MD5Handler.MD5Bean> beans) {
                super.onFinish(beans);
                Log.d(TAG,"onfinish");
            }

            @Override
            public void onAllCorrect() {
                super.onAllCorrect();
                Log.d(TAG,"onAllCorrect");
            }

            @Override
            public void onIncorrect(MD5Handler.MD5Bean bean) {
                super.onIncorrect(bean);
                Log.d(TAG,"onIncorrect bean:"+bean.toString());
            }
        });
//        try {
//            for (int i = 0; i < 1000; i++) {
//                md5 = MD5.getFileMD5String(new File(path));
//                md52 = MD5.getFileMD5String(new File(path2));
//                if (i != 0 && !md5.equals(lastMD5)) {
//                    Log.d(TAG,"diff last:"+lastMD5+" now:"+md5);
//                }
//                if(i != 0 && !md52.equals(lastMD52)) {
//                    Log.d(TAG,"diff last:"+lastMD52+" now:"+md52);
//                }
//                lastMD5 = md5;
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Looper.loop();
    }

    /**
     * 文件对象
     *
     * @param file
     * @return
     */
    public static String getMD5ByFile(File file) {
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length = -1;
            System.out.println("开始算");
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            System.out.println("算完了");
            return bytesToString(md.digest());
        } catch (IOException ex) {
//            LOGGER.info(ex.getMessage(), ex);
            return null;
        } catch (NoSuchAlgorithmException e) {
//            LOGGER.info(e.getMessage(), e);
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
//                LOGGER.info(ex.getMessage(), ex);
            }
        }
    }


    /**
     * bytesToString
     *
     * @param data
     * @return
     */
    public static String bytesToString(byte[] data) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f'};
        char[] temp = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            temp[i * 2] = hexDigits[b >>> 4 & 0x0f];
            temp[i * 2 + 1] = hexDigits[b & 0x0f];
        }
        return new String(temp);
    }
}