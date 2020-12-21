package org.yuhanxun.libcommonutil.file;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by yuhanxun on 16/3/15.
 */
public class FileMover {
    public static boolean fileChannelCopy(File s, File t) {
        Log.d("FileMover", "fileChannelCopy src:" + s + " target:" + t);
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        boolean ret = false;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();
            // 得到对应的文件通道
            in.transferTo(0, in.size(), out);
            //连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
            ret = false;
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                ret = false;
            }
        }
        return ret;
    }

    // 复制文件夹
    public static void copyDirectiory(String sourceDir, String targetDir) {
        // 新建目标目录
        mkDirs(targetDir);
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        Log.d("FileMover", "copyDirectiory file.listFile.length:" + file.length);
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                String targetPath = targetDir + File.separator + file[i].getName();
                File targetFile = new File(targetPath);
                fileChannelCopy(sourceFile, targetFile);
//                try {
//                    String srcMd5 = MD5.getFileMD5String(sourceFile);
//                    String tarMd5 = MD5.getFileMD5String(targetFile);
//                    if(!srcMd5.equals(tarMd5)) {
//                        Log.d("copy_dbstar", "copy file src:"+sourceFile.getAbsolutePath()+" tar:"+targetPath);
//                        Log.d("copy_dbstar", "diff md5 src:srcMd5"+srcMd5+" tar:" + tarMd5);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                setFileRWX(targetPath);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static boolean deleteDir(String dir) {
        return deleteDir(new File(dir));
    }

    public static boolean mkDirs(String path, boolean readable, boolean writable) {
        File file = new File(path);
        boolean ret = false;
        if (!file.exists() || !file.isDirectory()) {
            ret = file.mkdirs();
        }
//        if (ret) {
        file.setReadable(readable, false);
        file.setWritable(writable, false);
        file.setExecutable(true, false);
//        }
        return ret;
    }

    //创建资源父目录
    public static boolean mkParentDirs(String fileName) {
        return mkParentDirs(new File(fileName));
    }

    public static boolean mkParentDirs(File file) {
        return FileMover.mkDirs(file.getParent());
    }

    public static boolean mkDirs(String dirName) {
        return mkDirs(dirName, true, true);
    }

    public static boolean mkDirs(String dirName, boolean writable) {
        return mkDirs(dirName, true, writable);
    }

    public static void setFileRWX(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.setReadable(true, false);
            file.setWritable(true, false);
            file.setExecutable(true, false);
        }
    }


}
