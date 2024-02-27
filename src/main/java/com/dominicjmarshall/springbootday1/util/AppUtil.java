package com.dominicjmarshall.springbootday1.util;

import java.io.File;

public class AppUtil {
    private static final String UPLOAD_DIR = "uploads";

    // prepend /Users/dominicjmarshall/Java/VS Code/spring-boot-day1/uploads/ to the filename.
    public static String getUploadPath(String filename){
        File uploadDir = new File(System.getProperty("user.dir"), UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        return uploadDir.getAbsolutePath() + File.separator + filename;
    }
}
