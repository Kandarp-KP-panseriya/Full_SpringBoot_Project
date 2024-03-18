package com.example.crud2.services;

import com.example.crud2.decorater.*;
import com.example.crud2.model.User;
import org.springframework.data.domain.Page;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface UserService {

    public User addUser(UserRequest user) ;

    public List<User> getAllUser();
    public UserTokenResponse logIn(LoginDecorater loginDecorater);
    public User unblock(String id);
    public void deleteById(String did);

    public User updateUser(String id, UserRequest userRequest) throws InvocationTargetException, IllegalAccessException;

    public Page<User> getDataWithPagination(PaginationRequest paginationRequest);


    public User setForgotPassword(String id, ForgotPassword forgotPassword);
}
