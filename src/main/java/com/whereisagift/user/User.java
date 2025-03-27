package com.whereisagift.user;

import com.whereisagift.wish.Wish;
import com.whereisagift.wishlist.Wishlist;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionIdMutability;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 50)
    @Column(name = "telegram_id", nullable = false)
    private Long telegram_id;
    @Column(name = "first_name", nullable = false)
    private String first_name;
    @Column(name = "last_name", nullable = false)
    private String last_name;
    @Column(name = "telegram_username", nullable = false)
    private String telegram_username;
    @Column(name = "photo_url")
    private String photo_url;

//    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Wishlist> wishlists;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Wish> wishes;
}


