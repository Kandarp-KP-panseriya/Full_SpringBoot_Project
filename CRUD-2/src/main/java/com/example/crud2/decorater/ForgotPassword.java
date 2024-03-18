package com.example.crud2.decorater;

import lombok.Data;

@Data
public class ForgotPassword {
    String newPassword;
    String oldPassword;
    String confirmPasseord;
}
