package com.darwgom.taskapi.application.config;

import com.darwgom.taskapi.domain.entities.User;
import com.darwgom.taskapi.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        List<User> users = Arrays.asList(
                new User(null, "Jena Doe", "jena.doe@dev.com", passwordEncoder.encode("jen@D03"), null, null),
                new User(null, "Dave Raave", "dave.raave@dev.com", passwordEncoder.encode("dav3R@v3"), null, null)
        );

        for (User user : users) {
            if (!userRepository.findByEmail(user.getEmail()).isPresent()) {
                userRepository.save(user);
            }
        }
    }
}

