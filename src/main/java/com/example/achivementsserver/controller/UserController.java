package com.example.achivementsserver.controller;

import com.example.achivementsserver.model.User;
import com.example.achivementsserver.repo.AchivementRepo;
import com.example.achivementsserver.repo.CommentRepo;
import com.example.achivementsserver.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private AchivementRepo achivementRepo;

    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepo.findAll();
    }

    @PutMapping("/user")
    public User createUser(){
        return null;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable int id){
        return userRepo.findUserById(id);
    }

    @PutMapping("/user/{id}")
    public User editUser(@PathVariable int id){
        return null;
    }

    @DeleteMapping("/user/{id}")
    public User deleteUser(@PathVariable int id){
        User user = userRepo.findUserById(id);
        userRepo.delete(user);
        return user;
    }
}
