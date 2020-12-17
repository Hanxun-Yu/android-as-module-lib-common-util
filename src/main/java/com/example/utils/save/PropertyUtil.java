package com.example.utils.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.example.utils.file.FileMover;

/**
 * Created by Haibo on 2015/5/7.
 */
public class PropertyUtil {
    private String filePath;

    private Properties properties;

    public PropertyUtil(String filePath) {
        this.filePath = filePath;
        properties = new Properties();
        try {
            File file = new File(filePath);
            if (file.exists()) {
                properties.load(new FileInputStream(filePath));
            } else {
                if (!file.getParentFile().exists()) {
                    if (!file.getParentFile().mkdirs()) {
                        throw new IllegalArgumentException("Create property file's parent dics fail.");
                    }
                }
                boolean isSucceed = file.createNewFile();
                if (!isSucceed) {
                    throw new IllegalArgumentException("Create property file fail.");
                } else {
                    properties.load(new FileInputStream(filePath));
                }
            }
            FileMover.setFileRWX(filePath);
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public PropertyUtil(InputStream inputStream) {
        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param key
     * @return if key not founa return ""
     */
    public String getValue(String key) {
        return properties.getProperty(key, "");
    }

    /**
     * @param key
     * @param defaultVal
     * @return if key not found return defaultVal
     */
    public String getValue(String key, String defaultVal) {
        return properties.getProperty(key, defaultVal);
    }

    public void setValue(String key, String value) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            properties.setProperty(key, value);
            properties.store(fos, "update values");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
            }
        }
    }

}
