package com.example.achivementsserver;

import com.example.achivementsserver.model.Role;
import com.example.achivementsserver.model.User;
import com.example.achivementsserver.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootTest
class AchivementsServerApplicationTests {
    @Autowired
    private UserRepo userRepo;

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
}
