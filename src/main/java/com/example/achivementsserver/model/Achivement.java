package com.example.achivementsserver.model;

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

    public String getStatusText() {
        switch (status){
            case ACTIVE:
                return "Active";
            case COMPLETED:
                return "Completed";
            case FAILED:
                return "Failed";
            default:
                return "Not stated";
        }
    }
    @Override
    public String toString() {
        return "Achivement{" +
                ", id=" + id +
                ", text='" + text + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
