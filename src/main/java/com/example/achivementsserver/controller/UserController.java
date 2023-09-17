package com.example.achivementsserver.controller;

import com.example.achivementsserver.model.AuthentificationRequest;
import com.example.achivementsserver.model.AuthentificationResponse;
import com.example.achivementsserver.model.User;
import com.example.achivementsserver.repo.AchivementRepo;
import com.example.achivementsserver.repo.CommentRepo;
import com.example.achivementsserver.repo.UserRepo;
import com.example.achivementsserver.service.JwtUtil;
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

import java.util.List;

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

//    @PostMapping("/auth")
//    public User loginn(@RequestBody AuthentificationRequest user){
//        Logger.getLogger(user.getUsername());
//        System.out.println(user.getPassword());
//        System.out.println("00001100000");
//        return null;
//    }

    @PostMapping("/login")
    public AuthentificationResponse login(@RequestBody AuthentificationRequest loginRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return new AuthentificationResponse(jwt);
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        System.out.println(1232131231);
        return userRepo.findAll();
    }

    @PutMapping("/user")
    public User createUser(@RequestBody User user){
        userRepo.saveAndFlush(user);
        return user;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable int id){
        System.out.println("GetUser: " + userRepo.findUserById(id).toString());
        return userRepo.findUserById(id);
    }

    @PostMapping("/user/{id}")
    public User editUser(@RequestBody User user){
        System.out.println("EditUser: " + user.toString());
        userRepo.saveAndFlush(user);
        return user;
    }

    @DeleteMapping("/user/{id}")
    public User deleteUser(@PathVariable int id){
        User user = userRepo.findUserById(id);
        userRepo.delete(user);
        return user;
    }
}
