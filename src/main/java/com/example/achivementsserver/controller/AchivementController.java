package com.example.achivementsserver.controller;

import com.example.achivementsserver.AchivementsServerApplication;
import com.example.achivementsserver.HelpFunctions;
import com.example.achivementsserver.model.Achivement;
import com.example.achivementsserver.model.Comment;
import com.example.achivementsserver.model.Status;
import com.example.achivementsserver.model.User;
import com.example.achivementsserver.repo.AchivementRepo;
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
import java.util.*;

@RestController
public class AchivementController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AchivementRepo achivementRepo;
    @Value("${file.upload-dir}")
    private String photosFolderPath;

    @PostMapping("/add_comment/{id}")
    public User addComment(@PathVariable int id, @RequestBody Comment newComment){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByUsername(authentication.getName());

        Optional<Achivement> foundAchivement = user.getAchivements().stream().filter(_achivement -> _achivement.getId() == id).findFirst();
        if(foundAchivement.isPresent()){
            Achivement achivement = foundAchivement.get();
            achivement.addComment(newComment);
        }

        userRepo.saveAndFlush(user);
        return userRepo.findUserByUsername(authentication.getName()).clearFriendsRecursive();
    }

    @PostMapping("/get_new_achivement")
    public User getNewAchivement(@RequestBody Status statusLastAchivement){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByUsername(authentication.getName());

        Achivement achivement = new Achivement();
        achivement.setText(HelpFunctions.getNewAchivement());
        achivement.setStatus(Status.ACTIVE);
        achivement.setOwnerId(user.getId());

        List<Achivement> achivements = user.getAchivements();
        if(!achivements.isEmpty())
            achivements.get(achivements.size()-1).setStatus(statusLastAchivement);
        System.out.println(achivements);
        achivements.add(achivement);
        userRepo.saveAndFlush(user);
        System.out.println("Get new Achivement");
        return user.clearFriendsRecursive();
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getImageAchivement(@PathVariable int id){
        System.out.println("123131");
        Achivement achivement = achivementRepo.findAchivementById(id);
        String dirPath = photosFolderPath + File.separator + achivement.getOwnerId();
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
//            e.printStackTrace();
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
            System.out.println("Avatar uploaded successfully");
            return ResponseEntity.ok("Avatar uploaded successfully");
        } catch (IOException e) {
            System.out.println("Failed to upload avatar");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload avatar");
//            throw new RuntimeException(e);
        }
    }
}
