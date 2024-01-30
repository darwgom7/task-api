package com.darwgom.taskapi.domain.repositories;

import com.darwgom.taskapi.domain.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    @NonNull
    @Override
    List<User> findAll();
}