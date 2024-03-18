package com.example.crud2.controller;

import com.example.crud2.AllMessages.ResponseConstant;
import com.example.crud2.decorater.*;
import com.example.crud2.model.User;
import com.example.crud2.services.UserService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("crud")
public class UserController {
    private final UserService userService;
    private final MongoTemplate mongoTemplate;

    public UserController(UserService userService, MongoTemplate mongoTemplate) {
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;
    }

    @PostMapping("/adduser")
    public DataResponse<User> addUser(@RequestBody UserRequest userRequest) {
        DataResponse<User> dataResponse = new DataResponse<>();
        try {
            dataResponse.setData(userService.addUser(userRequest));
            dataResponse.setStatus(Response.getSuccessResponse(ResponseConstant.SUCCESS));
        } catch (Exception e) {
            dataResponse.setStatus(Response.getInvalidRequestResponse(e.getMessage()));
        }
        return dataResponse;
    }

    @GetMapping("/getalluser")
    public DataResponse<List<User>> fatchDataAllUser() {
        DataResponse<List<User>> dataResponse = new DataResponse<>();
        dataResponse.setData(userService.getAllUser());
        dataResponse.setStatus(Response.getSuccessResponse(ResponseConstant.SUCCESS));
        return dataResponse;
    }

    @DeleteMapping("/{id}")
    public DataResponse<?> deleteById(@PathVariable String id) {
        DataResponse<User> dataResponse = new DataResponse<>();
        try
        {
            userService.deleteById(id);
            dataResponse.setStatus(Response.getDeletedResponse(ResponseConstant.DELETED));
        }
        catch(Exception e)
        {
            dataResponse.setStatus(Response.getNotFoundResponse(e.getMessage() ));
        }
        return dataResponse;
    }

    @PutMapping("/{id}")
    public DataResponse<User> updateUser(@PathVariable String id, @RequestBody UserRequest userRequest) {
        DataResponse<User> dataResponse = new DataResponse<>();
        try {
            dataResponse.setData(userService.updateUser(id, userRequest));
            dataResponse.setStatus(Response.getUpdatedResponse(ResponseConstant.UPDATED));
        } catch (Exception e) {
            dataResponse.setStatus(Response.getNotFoundResponse(e.getMessage()));
        }
        return dataResponse;
    }

    @PostMapping("getDataWithPagination")
    public PageResponse getDataWithPagination(@RequestBody PaginationRequest paginationRequest){
        PageResponse<User> dataResponse = new PageResponse<>();
        dataResponse.setData(userService.getDataWithPagination(paginationRequest));
        dataResponse.setStatus(Response.getSuccessResponse(ResponseConstant.SUCCESS));
        return dataResponse;
    }

    @PostMapping("forgotPassword/{id}")
    public DataResponse forgotPassword(@RequestBody ForgotPassword forgotPassword, @PathVariable String id) {

        DataResponse<User> dataResponse = new DataResponse<>();
        try
        {
        dataResponse.setData(userService.setForgotPassword(id,forgotPassword));
        dataResponse.setStatus(Response.getSuccessResponse(ResponseConstant.SUCCESS));
        }
        catch (Exception e)
        {
            dataResponse.setStatus(Response.getNotFoundResponse(e.getMessage()));
        }
        return dataResponse;
    }

    @PostMapping("login")
    public TokenResponse logInPassword(@RequestBody LoginDecorater loginDecorater)
    {
        TokenResponse tokenResponse = new TokenResponse();
        try
        {
            UserTokenResponse userTokenResponse = userService.logIn(loginDecorater);
            tokenResponse.setToken(userTokenResponse.getToken());
            tokenResponse.setData(userTokenResponse);
            tokenResponse.setStatus(Response.getSuccessResponse(ResponseConstant.VERIFY));
        }
        catch (Exception e)
        {
            tokenResponse.setStatus(Response.getNotFoundResponse(e.getMessage()));
        }
        return tokenResponse;
    }
    @PostMapping("unblockedUser/{id}")
    public DataResponse unblockedUser(@PathVariable String id)
    {
        DataResponse dataResponse = new DataResponse<>();
        try
        {
            dataResponse.setData(userService.unblock(id));
            dataResponse.setStatus(Response.getSuccessResponse(ResponseConstant.VERIFY));
        }
        catch (Exception e)
        {
            dataResponse.setStatus(Response.getNotFoundResponse(e.getMessage()));
        }
        return dataResponse;
    }


}
