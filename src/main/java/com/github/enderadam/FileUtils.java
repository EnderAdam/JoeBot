package com.github.enderadam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {
    /**
     * Returns all jpg images from a directory in an array.
     *
     * @param directory                 the directory to start with
     * @param descendIntoSubDirectories should we include subdirectories
     * @return an ArrayList<String> containing all the files or nul if none are found
     * @throws IOException exception
     */
    public static ArrayList<String> getAllImages(File directory, boolean descendIntoSubDirectories) throws IOException {
        ArrayList<String> resultList = new ArrayList<>(256);
        File[] f = directory.listFiles();
        for (File file : f) {
            if (file != null && (file.getName().toLowerCase().endsWith(".png") || file.getName().toLowerCase().endsWith(".jpg")) && !file.getName().startsWith("tn_")) {
                resultList.add(file.getCanonicalPath());
            }
            if (descendIntoSubDirectories && file.isDirectory()) {
                ArrayList<String> tmp = getAllImages(file, true);
                if (tmp != null) {
                    resultList.addAll(tmp);
                }
            }
        }
        if (resultList.size() > 0)
            return resultList;
        else
            return new ArrayList<>();
    }


}

   
  