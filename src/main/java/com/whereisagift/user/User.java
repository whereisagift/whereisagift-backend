package com.whereisagift.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "users")
public class User {

    public User() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 50)
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
//    @Email
//    @Column(nullable = false, unique = true)
//    private String email;
//    @Column(name = "dateOfBirth")
//    private LocalDate dateOfBirth;
//    @Column(name = "profilePicture")
//    private String profilePicture;
//    @Column(name = "dateOfCreate")
//    private LocalDateTime dateOfCreate;
//
//    @PrePersist
//    protected void onCreate() {
//        dateOfCreate = LocalDateTime.now();
//    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}


