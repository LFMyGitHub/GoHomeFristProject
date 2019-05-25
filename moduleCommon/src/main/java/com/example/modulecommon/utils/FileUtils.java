package com.example.modulecommon.utils;

import java.io.File;

public class FileUtils {
    /**
     * 没有文件夹。创建文件夹
     *
     * @param filePath
     */
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                Boolean isTrue = file.mkdir();
            }
        } catch (Exception e) {
        }
    }

    public static String getFileSize(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return "0 MB";
        } else {
            long size = f.length();
            return (size / 1024f) / 1024f + "MB";
        }
    }
}
