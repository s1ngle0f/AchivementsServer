package com.example.achivementsserver.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Achivement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = 0;
    private String text;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    private String image;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "achivement_id")
    private Set<Comment> comments = new HashSet<>();

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    @JsonBackReference
//    private User user;

    public void addComment(Comment comment){
        if(!comments.stream().anyMatch(_comment -> comment.getId() == _comment.getId()))
            comments.add(comment);
    }

    @Override
    public String toString() {
        return "Achivement{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
