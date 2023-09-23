package com.example.achivementsserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = 0;
    private String text;
    private int userId;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "achivement_id")
//    private Achivement achivement;
}