package com.example.achivementsserver.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "usr")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = 0;
    private String username;
    private String password;
    private String description;

    private boolean active = true;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private List<Achivement> achivements = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_friend",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<User> friends = new HashSet<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = Set.of(Role.USER);

    public void addFriend(User user){
        if(!friends.stream().anyMatch(_user -> user.id == _user.id))
            friends.add(user);
    }

    public void removeFriend(User user){
        if(friends.stream().anyMatch(_user -> user.id == _user.id))
            friends.remove(user);
    }

    public User clearFriendsRecursive(){
        if(friends != null)
            for (User user : friends)
                user.setFriends(null);
        return this;
    }

//    public void resetAchivements(){
//        for(Achivement achivement : achivements){
//            if(achivement.getUser() != this)
//                achivement.setUser(this);
//        }
//    }

    public User cloneWithoutFriends(){
        return new User(id, username, password, description, active, achivements, null, roles);
    }

    @Override
    public String toString() {
        String friendsString = "[";
        if(friends != null)
            for (User user : friends)
                friendsString += "\n\t" + user.toString(2);
        friendsString += "]";
        return "User{" +
                "\n\tid=" + id +
                ",\n\tusername='" + username + '\'' +
                ",\n\tpassword='" + password + '\'' +
                ",\n\tdescription='" + description + '\'' +
                ",\n\tactive=" + active +
                ",\n\tachivements=" + achivements +
                ",\n\troles=" + roles +
                ",\n\tfriends=" + friendsString +
                "\n}";
    }

    public String toString(int countTab) {
        String userEndTab = "\t".repeat(countTab - 1);
        String tab = "\t".repeat(countTab);
        String friendsString = "[";

//        if (friends != null) {
//            for (User user : friends) {
//                friendsString += "\n" + tab + user.toString(countTab + 1);
//            }
//        }

        friendsString += "]";
        return "User{" +
                "\n" + tab + "id=" + id +
                ",\n" + tab + "username='" + username + '\'' +
                ",\n" + tab + "password='" + password + '\'' +
                ",\n" + tab + "description='" + description + '\'' +
                ",\n" + tab + "active=" + active +
                ",\n" + tab + "achivements=" + achivements +
                ",\n" + tab + "roles=" + roles +
                ",\n" + tab + "friends=" + friendsString +
                "\n" + userEndTab + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        boolean usernameCheck = Objects.equals(username, user.username);
        boolean idCheck = Objects.equals(id, user.id);
        return usernameCheck && idCheck;
    }
}
