package com.example.crud2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "admin_config")
public class AdminConfig
{
     @Id
     String id;
     List<String> emailsList;
     int maxLogin;
}
