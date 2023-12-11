package com.url.shortener.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.url.shortener.model.Tier;
import com.url.shortener.model.User;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RateLimitService {
    private static final String FILEPATH = "src/main/resources/users.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Bucket> userBuckets = new HashMap<>();

    public boolean allowedToMakeRequests(String userId) {
        User user = getUser(userId);

        Bucket bucket = userBuckets.computeIfAbsent(userId, this::createNewBucket);

        return bucket.tryConsume(1);
    }

    private User getUser(String userId) {
        List<User> users = null;
        try {
             users = objectMapper.readValue(new File(FILEPATH), new TypeReference<List<User>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    private Bucket createNewBucket(String userId) {
        Tier tier = getUserTier(userId);

        Refill refill = Refill.intervally(tier.getLimit(), Duration.ofMinutes(1));

        Bandwidth limit = Bandwidth.classic(tier.getLimit(), refill);

        return Bucket.builder().addLimit(limit).build();

    }

    private Tier getUserTier(String userId) {
        if (userId.length() < 3) {
            return Tier.TIER2;
        } else {
            return Tier.TIER1;
        }
    }
}
