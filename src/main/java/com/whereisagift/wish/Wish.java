package com.whereisagift.wish;

import com.whereisagift.user.User;
import com.whereisagift.wish.price.Price;
import com.whereisagift.wish.product.ProductSource;
import com.whereisagift.wishlist.Wishlist;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "wishes")
@NoArgsConstructor
public class Wish {
    @ManyToMany
    @JoinTable(
            name = "wishes_to_wishlists",
            joinColumns = @JoinColumn(name = "wish_id"),
            inverseJoinColumns = @JoinColumn(name = "wishlist_id")
    )
    private Set<Wishlist> wishlists = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductSource type;

    @Column(length = 300)
    private String link;

    @Column(length = 300)
    private String img;

    @Column(length = 300)
    private String description;

    @Embedded
    private Price price;

    @Column()
    @Check(constraints = "rate BETWEEN 0 AND 5")
    private Integer rate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
