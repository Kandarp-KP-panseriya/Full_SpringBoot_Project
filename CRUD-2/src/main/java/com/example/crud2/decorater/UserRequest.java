package com.example.crud2.decorater;

import com.example.crud2.enums.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.List;

@Data
public class UserRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String password;
    private String state;
    //private List<Roles> roles;
    private Roles userRole;
}

