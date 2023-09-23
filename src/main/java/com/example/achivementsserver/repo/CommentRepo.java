package com.example.achivementsserver.repo;

import com.example.achivementsserver.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
//    List<Comment> findCommentsByUserId(int id);
}
