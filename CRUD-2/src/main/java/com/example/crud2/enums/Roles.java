package com.example.crud2.enums;

import java.util.List;

public enum Roles
{
    ADMIN("ADMIN",List.of("ADMIN","USER")),
    USER("USER"),
    NON("NO");

    Roles(String name) {
    }

    Roles(String name, List<String> roles) {

    }
}
