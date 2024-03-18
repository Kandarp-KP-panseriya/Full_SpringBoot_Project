package com.example.crud2.decorater;

import lombok.*;

import java.util.Date;

@Data
public class UserResponse
{
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String fullName;
    private Date registrationDate;
}
