package com.example.wishlistAppG.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "users")
public class User {

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


