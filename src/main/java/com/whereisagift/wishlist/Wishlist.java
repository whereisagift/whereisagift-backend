package com.whereisagift.wishlist;

import com.whereisagift.user.User;
import com.whereisagift.wish.Wish;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@Entity
@Data
@Table(name = "wishlists")
@NoArgsConstructor
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Nullable
    @Column(name = "description", length = 300)
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToMany(mappedBy = "wishlists")
    private List<Wish> wishes;


}
