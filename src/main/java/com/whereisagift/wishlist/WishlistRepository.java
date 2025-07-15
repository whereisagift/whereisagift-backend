package com.whereisagift.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
  Iterable<Wishlist> findByCreatorId(Long id);
}
