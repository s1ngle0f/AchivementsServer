package com.example.achivementsserver.repo;

import com.example.achivementsserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Integer> {
    List<User> findUsersByUsernameContaining(String username);
    User findUserByUsername(String username);
    User findUserById(int id);
}
