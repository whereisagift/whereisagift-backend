package com.whereisagift.wish;

import com.whereisagift.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
  List<Wish> findByCreator(User creator);
}
