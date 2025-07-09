package com.whereisagift.user;

import com.whereisagift.wish.Wish;
import com.whereisagift.wishlist.Wishlist;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(1)
    @Column(name = "telegram_id", nullable = false)
    private Long telegramId;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Nullable
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "telegram_username", nullable = false)
    private String username;
    @Nullable
    @Column(name = "photo_url")
    private String photoUrl;
    @Column(name = "auth_date", nullable = false)
    private Long authDate;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Wishlist> wishlists;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Wish> wishes;
}

