package com.example.crud2.repository;

import com.example.crud2.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String>{
    Optional<User> findByIdAndSoftDeleteIsFalse(String id);
    User findByEmailAndSoftDeleteIsFalse(String email);
    Boolean existsByEmailAndSoftDeleteIsFalse(String email);

    List<User> findAllBySoftDeleteIsFalse();
}
