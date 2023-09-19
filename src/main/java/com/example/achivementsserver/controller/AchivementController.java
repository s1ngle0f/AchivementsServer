package com.example.achivementsserver.controller;

import com.example.achivementsserver.HelpFunctions;
import com.example.achivementsserver.model.User;
import com.example.achivementsserver.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.io.InputStreamResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@RestController
public class AchivementController {
    @Autowired
    private UserRepo userRepo;
    @Value("${file.upload-dir}")
    private String photosFolderPath;

    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getImageAchivement(@PathVariable int id){
        System.out.println("123131");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByUsername(authentication.getName());
        String dirPath = photosFolderPath + File.separator + user.getId();
        String path = dirPath + File.separator + HelpFunctions.findFileWithExtension(dirPath, String.valueOf(id));
        File _file = new File(path);

        try {
            InputStream inputStream = new FileInputStream(path);
            Resource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", _file.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<String> loadImageAchivement(@PathVariable int id, @RequestParam("file") MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByUsername(authentication.getName());
        String name = file.getOriginalFilename();
        String extension = name.substring(name.lastIndexOf('.'));
        String path = photosFolderPath + File.separator + user.getId() + File.separator + id + extension;
        HelpFunctions.createIfNotExistFolder(path);
        try {
            Path filePath = Paths.get(path);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok("Avatar uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload avatar");
//            throw new RuntimeException(e);
        }
    }
}
