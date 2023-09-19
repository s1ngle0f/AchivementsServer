package com.example.achivementsserver;

import com.example.achivementsserver.model.Role;
import com.example.achivementsserver.model.User;
import com.example.achivementsserver.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@SpringBootTest
class AchivementsServerApplicationTests {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${file.upload-dir}")
    private String photosFolderPath;

    @Test
    public void getUsers(){
        List<User> users = userRepo.findAll();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void addUser(){
        String password = new BCryptPasswordEncoder().encode("1");
        User user = new User();
        user.setUsername("u");
        user.setPassword(password);
        User userAdmin = new User();
        userAdmin.setUsername("a");
        userAdmin.setPassword(password);
        userAdmin.setRoles(Set.of(Role.USER, Role.ADMIN));
        if(userRepo.findUserByUsername(user.getUsername()) == null) userRepo.saveAndFlush(user);
        if(userRepo.findUserByUsername(userAdmin.getUsername()) == null) userRepo.saveAndFlush(userAdmin);
        getUsers();
    }

    @Test
    public void deleteAllUsers(){
        userRepo.deleteAll();
    }

    @Test
    public void getResourceFolder(){
        // Указываем путь и имя файла
        String fileName = photosFolderPath + File.separator + "example.txt";
        createIfNotExistFolder(fileName);

        try {
            // Создаем объект File, представляющий файл
            File file = new File(fileName);

            // Создаем FileWriter для записи в файл
            FileWriter writer = new FileWriter(file);

            // Записываем текст в файл
            writer.write("Пример текста в файле.");
            writer.write("\nДополнительная строка текста.");

            // Закрываем FileWriter
            writer.close();

            System.out.println("Файл создан и записан успешно.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
}
