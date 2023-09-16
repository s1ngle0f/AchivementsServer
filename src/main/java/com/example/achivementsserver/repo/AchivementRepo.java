package com.example.achivementsserver.repo;

import com.example.achivementsserver.model.Achivement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchivementRepo extends JpaRepository<Achivement, Integer> {

}
