package com.example.achivementsserver.repo;

import com.example.achivementsserver.model.Achivement;
import com.example.achivementsserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchivementRepo extends JpaRepository<Achivement, Integer> {
//    List<Achivement> findAchivementsByUser(User user);
}
