package com.ning.repository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AuthenticationRepository {

    private static ConcurrentHashMap<String, Authentication> authentications = new ConcurrentHashMap<>();

    public void add(String key, Authentication authentication) {
        authentications.put(key, authentication);
    }

    public Authentication get(String key) {
        return authentications.get(key);
    }

    public void delete(String key) {
        if (authentications.containsKey(key)) {
            authentications.remove(key);
        }
    }

}
