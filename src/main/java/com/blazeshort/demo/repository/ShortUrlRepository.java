package com.blazeshort.demo.repository;

import com.blazeshort.demo.model.entity.ShortUrl;
import com.blazeshort.demo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortCodeAndUserId(String shortCode, Long userId);
    List<ShortUrl> findAllByUser(User user);
    boolean existsByShortCode(String shortCode);
    void deleteByShortCode(String shortCode);
}
