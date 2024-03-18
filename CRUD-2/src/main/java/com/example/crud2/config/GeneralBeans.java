package com.example.crud2.config;

import com.example.crud2.decorater.Response;
import com.example.crud2.utils.NullAwareBeanUtilBeans;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class GeneralBeans {
    @Bean
    public NullAwareBeanUtilBeans beanUtilBeans() {
        return new NullAwareBeanUtilBeans();
    }

    @Bean
    public Response getResponse() {
        return new Response();
    }

}
