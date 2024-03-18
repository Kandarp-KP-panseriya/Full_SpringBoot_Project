package com.example.crud2.repository;

import com.example.crud2.model.AdminConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminConfigRepository extends MongoRepository<AdminConfig, String>
{

}
