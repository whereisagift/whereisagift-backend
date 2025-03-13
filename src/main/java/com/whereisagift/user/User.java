package com.whereisagift.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Entity
@Data
@Component
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 50)
    @Column(nullable = false)
    private String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

}


