package com.whereisagift.wish;

import com.whereisagift.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    Iterable<Wish> findByCreator(User creator);
}
