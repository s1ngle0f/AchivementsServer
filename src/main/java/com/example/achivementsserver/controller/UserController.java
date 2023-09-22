package com.example.achivementsserver.controller;

import com.example.achivementsserver.HelpFunctions;
import com.example.achivementsserver.model.AuthentificationRequest;
import com.example.achivementsserver.model.AuthentificationResponse;
import com.example.achivementsserver.model.User;
import com.example.achivementsserver.repo.AchivementRepo;
import com.example.achivementsserver.repo.CommentRepo;
import com.example.achivementsserver.repo.UserRepo;
import com.example.achivementsserver.service.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

@RestController
public class UserController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private AchivementRepo achivementRepo;
    @Value("${file.upload-dir}")
    private String photosFolderPath;

    @GetMapping("/users")
    public List<User> getUsers(@RequestParam(name = "username", required = false) String username){
        System.out.println("GETUSSSS");
        List<User> res;
        if(username != null){
            res = userRepo.findUsersByUsernameContaining(username);
        }else{
            res = userRepo.findAll();
        }
        for(User user : res)
            user.clearFriendsRecursive();
        return res;
    }

    @PutMapping("/user")
    public User createUser(@RequestBody User user){
        if(userRepo.findUserByUsername(user.getUsername()) == null) {
//            user.resetAchivements();
            userRepo.saveAndFlush(user);
            return user.clearFriendsRecursive();
        }
        return null;
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable int id){
        System.out.println("GetUser: " + userRepo.findUserById(id).toString());
        return userRepo.findUserById(id).clearFriendsRecursive();
    }

    @GetMapping("/user")
    public User getUserByUsername(@RequestParam(name = "username", required = false) String username){
        if(username != null){
            System.out.println("GetUser: " + userRepo.findUserByUsername(username).toString());
            return userRepo.findUserByUsername(username).clearFriendsRecursive();
        }else{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication != null)
                return userRepo.findUserByUsername(authentication.getName()).clearFriendsRecursive();
        }
        return null;
    }

    @PostMapping("/user")
    public User editUser(@RequestBody User user){
//        user.resetAchivements();
        System.out.println("EditUser: " + user.toString());
        userRepo.saveAndFlush(user);
        return user.clearFriendsRecursive();
    }

    @DeleteMapping("/user/{id}")
    public User deleteUser(@PathVariable int id){
        User user = userRepo.findUserById(id);
        userRepo.delete(user);
        return user.clearFriendsRecursive();
    }

    @GetMapping("/friends")
    public Set<User> getFriends(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByUsername(authentication.getName());
        return user.getFriends();
    }

    @PostMapping("/friend")
    public User addFriend(@RequestBody User friend){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByUsername(authentication.getName());
        user.addFriend(friend);
        userRepo.saveAndFlush(user);
        return user.clearFriendsRecursive();
    }

    @DeleteMapping("/friend")
    public User removeFriend(@RequestBody User friend){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByUsername(authentication.getName());
        user.removeFriend(friend);
        userRepo.saveAndFlush(user);
        return user.clearFriendsRecursive();
    }

    @PostMapping("/friend/{id}")
    public User addFriendById(@PathVariable int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByUsername(authentication.getName());
        User friend = userRepo.findUserById(id);
        if(friend != null)
            user.addFriend(friend);
        userRepo.saveAndFlush(user);
        return user.clearFriendsRecursive();
    }

    @DeleteMapping("/friend/{id}")
    public User removeFriendById(@PathVariable int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByUsername(authentication.getName());
        User friend = userRepo.findUserById(id);
        if(friend != null)
            user.removeFriend(friend);
        userRepo.saveAndFlush(user);
        return user.clearFriendsRecursive();
    }

    @GetMapping("/image/avatar")
    public ResponseEntity<Resource> getAvatar(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByUsername(authentication.getName());
        String dirPath = photosFolderPath + File.separator + user.getId();
        String path = dirPath + File.separator + HelpFunctions.findFileWithExtension(dirPath, "avatar");
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

    @PostMapping("/image/avatar")
    public ResponseEntity<String> loadAvatar(@RequestParam("file") MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findUserByUsername(authentication.getName());
        String name = file.getOriginalFilename();
        String extension = name.substring(name.lastIndexOf('.'));
        String path = photosFolderPath + File.separator + user.getId() + File.separator + "avatar" + extension;
        HelpFunctions.createIfNotExistFolder(path);
        try {
            Path filePath = Paths.get(path);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("CREATED NEW AVATAR");
            return ResponseEntity.ok("Avatar uploaded successfully");
        } catch (IOException e) {
            System.out.println("ERROR NEW AVATAR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload avatar");
//            throw new RuntimeException(e);
        }
    }
}
