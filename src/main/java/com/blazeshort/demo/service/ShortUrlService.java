package com.blazeshort.demo.service;

import com.blazeshort.demo.model.dto.ShortenRequest;
import com.blazeshort.demo.model.entity.ShortUrl;
import com.blazeshort.demo.model.entity.User;
import com.blazeshort.demo.model.enums.UrlStatus;
import com.blazeshort.demo.repository.ShortUrlRepository;
import com.blazeshort.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final UserRepository userRepository;


    public ShortUrl createShortUrl(ShortenRequest request, Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        String shortCode;
        do{
            shortCode = UUID.randomUUID().toString().replaceAll("-", "").substring(0,8);
        }while (shortUrlRepository.existsByShortCode(shortCode));
        ShortUrl shorturl = ShortUrl.builder().originalUrl(request.getOriginalUrl()).shortCode(shortCode).status(UrlStatus.ACTIVE).createdAt(LocalDateTime.now()).expiresAt(request.getExpiresAt()).user(user).build();
        return shortUrlRepository.save(shorturl);
    }

    public ShortUrl getAndValidate(String code){
        ShortUrl url = shortUrlRepository.findByShortCode(code).orElseThrow(()-> new RuntimeException("Url not found"));
        if(url.getStatus() != UrlStatus.ACTIVE){
            throw new RuntimeException("Url is disabled");
        }
        if(url.getExpiresAt()!=null && url.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Url is expired");
        }
        return url;
    }

    public List<ShortUrl> getUserUrls(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        return shortUrlRepository.findAllByUser(user);
    }
}
