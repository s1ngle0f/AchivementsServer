package com.example.achivementsserver;

import com.example.achivementsserver.model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    public static List<User> getUsersForExport(List<User> users){
        List<User> res = new ArrayList<>();
        for (User user : users) {
            res.add(getUserForExport(user));
        }
        return res;
    }

    public static User getUserForExport(User user){
        User newUser = user.cloneWithoutFriends();
        newUser.setFriends(new HashSet<>());
        for (User friend : user.getFriends()) {
            newUser.addFriend(friend.cloneWithoutFriends());
        }
        return newUser;
    }
}
