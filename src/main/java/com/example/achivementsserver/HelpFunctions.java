package com.example.achivementsserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelpFunctions {
    public static void createIfNotExistFolder(String path){
        Path directoryPath = Paths.get(path);
        if (!Files.isDirectory(directoryPath))
            directoryPath = directoryPath.getParent();
        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String findFileWithExtension(String folderPath, String fileNameWithoutExtension) {
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    String name = file.getName();
                    String extension = "";

                    int lastDotIndex = name.lastIndexOf(".");
                    if (lastDotIndex > 0) {
                        extension = name.substring(lastDotIndex + 1);
                        String nameWithoutExtension = name.substring(0, lastDotIndex);

                        if (nameWithoutExtension.equals(fileNameWithoutExtension)) {
                            return name;
                        }
                    }
                }
            }
        }
        return null;
    }
}
