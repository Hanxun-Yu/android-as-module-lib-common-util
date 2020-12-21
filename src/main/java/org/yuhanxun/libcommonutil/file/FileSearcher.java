package org.yuhanxun.libcommonutil.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Crashxun dbstar-mac on 2017/1/23.
 */

public class FileSearcher {
    public static List<File> searchFile(String parentPath, final String fileNameKey) {
        FileFilter filefilter = new FileFilter() {
            public boolean accept(File file) {
                //if the file extension is .txt return true, else false
                if (file.getName().endsWith(fileNameKey)) {
                    return true;
                }
                return false;
            }
        };
        return new FileSearcher().myListFiles(parentPath, filefilter);
    }

    public static List<String> searchFilePath(String parentPath, final String fileNameKey) {
        List<File> files = searchFile(parentPath,fileNameKey);
        List<String> ret = null;
        if(files != null && !files.isEmpty()) {
            ret = new ArrayList<>();
            for(File file:files) {
                ret.add(file.getAbsolutePath());
            }
        }
        return ret;
    }


    public List<File> myListFiles(String dir, FileFilter fileFilter) {
        File directory = new File(dir);

        if (!directory.isDirectory()) {
            System.out.println("No directory provided");
            return null;
        }
        List<File> files = new ArrayList<>(Arrays.asList(directory.listFiles(fileFilter)));
        List<File> dirs = new ArrayList<>(Arrays.asList(directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory())
                    return true;
                return false;
            }
        })));
        for (File f : dirs) {
            files.addAll(myListFiles(f.getAbsolutePath(), fileFilter));
        }
        return files;
    }
}
