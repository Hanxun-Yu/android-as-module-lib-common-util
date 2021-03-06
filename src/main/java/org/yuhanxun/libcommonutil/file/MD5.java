package org.yuhanxun.libcommonutil.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by szj on 2017/1/4.
 */

public class MD5 {
//    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
//            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
//    protected static MessageDigest messagedigest = null;
//
//    public static String getFileMD5String(File file) throws IOException {
//        if (messagedigest == null) {
//            try {
//                messagedigest = MessageDigest.getInstance("MD5");
//            } catch (NoSuchAlgorithmException e) {
//                Log.v("szj_md5", "MD5 messagedigest初始化失败");
//            }
//        }
//        FileInputStream in = new FileInputStream(file);
//        FileChannel ch = in.getChannel();
//        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
//                file.length());
//        messagedigest.update(byteBuffer);
//        return bufferToHex(messagedigest.digest());
//    }
//
//    public static String getMD5String(String s) {
//        return getMD5String(s.getBytes());
//    }
//
//    public static String getMD5String(byte[] bytes) {
//        if (messagedigest == null) {
//            try {
//                messagedigest = MessageDigest.getInstance("MD5");
//            } catch (NoSuchAlgorithmException e) {
//                Log.v("szj_md5", "MD5 messagedigest初始化失败");
//            }
//        }
//        messagedigest.update(bytes);
//        return bufferToHex(messagedigest.digest());
//    }
//
//    private static String bufferToHex(byte bytes[]) {
//        return bufferToHex(bytes, 0, bytes.length);
//    }
//
//    private static String bufferToHex(byte bytes[], int m, int n) {
//        StringBuffer stringbuffer = new StringBuffer(2 * n);
//        int k = m + n;
//        for (int l = m; l < k; l++) {
//            appendHexPair(bytes[l], stringbuffer);
//        }
//        return stringbuffer.toString();
//    }
//
//    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
//        char c0 = hexDigits[(bt & 0xf0) >> 4];
//        char c1 = hexDigits[bt & 0xf];
//        stringbuffer.append(c0);
//        stringbuffer.append(c1);
//    }
//
//    public static boolean checkPassword(String password, String md5PwdStr) {
//        String s = getMD5String(password);
//        return s.equals(md5PwdStr);
//    }
    /**
     * 文件对象
     *
     * @param file
     * @return
     */
    public static String getFileMD5String(File file) throws IOException {
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
//        } catch (IOException ex) {
//            LOGGER.info(ex.getMessage(), ex);
//            return null;
        } catch (NoSuchAlgorithmException e) {
//            LOGGER.info(e.getMessage(), e);
            e.printStackTrace();
            return null;
        } finally {
            try {
                if(fis != null)
                fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
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
