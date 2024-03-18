package com.example.crud2.model;

import com.example.crud2.enums.Roles;
import com.example.crud2.enums.UserState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


@Document(collection = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Date registrationDate;
    private String address;
    private String city;
    private String state;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private  boolean softDelete;
    @JsonIgnore
    private List<Roles> roles;
    @JsonIgnore
    private Roles userRole;
    @JsonIgnore
    private UserState userState;
    @JsonIgnore
    private List<String> oldPasswords;
    @JsonIgnore
    private int loginFailCount;

}
