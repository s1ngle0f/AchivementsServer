package com.example.achivementsserver.controller;

import com.example.achivementsserver.model.AuthentificationRequest;
import com.example.achivementsserver.model.AuthentificationResponse;
import com.example.achivementsserver.repo.AchivementRepo;
import com.example.achivementsserver.repo.CommentRepo;
import com.example.achivementsserver.repo.UserRepo;
import com.example.achivementsserver.service.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
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
}
