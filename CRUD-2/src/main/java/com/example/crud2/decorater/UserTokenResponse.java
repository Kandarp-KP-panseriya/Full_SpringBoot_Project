package com.example.crud2.decorater;

import com.example.crud2.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenResponse extends User {

    private String token;
}
