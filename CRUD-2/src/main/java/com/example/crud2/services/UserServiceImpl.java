package com.example.crud2.services;

import com.example.crud2.AllMessages.ResponseConstant;
import com.example.crud2.decorater.*;
import com.example.crud2.enums.Roles;
import com.example.crud2.enums.UserState;
import com.example.crud2.exceptions.AlreadyExistsException;
import com.example.crud2.exceptions.InvalidRequestException;
import com.example.crud2.exceptions.NotFoundExceptions;
import com.example.crud2.model.AdminConfig;
import com.example.crud2.model.User;
import com.example.crud2.repository.AdminConfigRepository;
import com.example.crud2.repository.UserRepository;
import com.example.crud2.repository.UserRepositoryCustom;
import com.example.crud2.utils.JWTUtils;
import com.example.crud2.utils.MustacheClass;
import com.example.crud2.utils.NullAwareBeanUtilBeans;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final NullAwareBeanUtilBeans beanUtilBeans;
    private final EmailService emailService;
    private final UserRepositoryCustom userRepositoryCustom;
    private final AdminConfigRepository adminConfigRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, NullAwareBeanUtilBeans beanUtilBeans, EmailService emailService, UserRepositoryCustom userRepositoryCustom, AdminConfigRepository adminConfigRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.beanUtilBeans = beanUtilBeans;
        this.emailService = emailService;
        this.userRepositoryCustom = userRepositoryCustom;
        this.adminConfigRepository = adminConfigRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    private void emailValidation(String email)
    {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_]+[@]+[a-z]+[.][a-z]{2,3}");
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()==false)
        {
         throw new InvalidRequestException(ResponseConstant.ENTER_VALID_EMAIL_ADDRESS);
        }
    }


    private void phoneNumberValidation(String phoneNumber) {
        if (phoneNumber.length() != 10) {
            throw new InvalidRequestException(ResponseConstant.ENTER_PROPER_PHONE_NUMBER);
        }
        for (char charachter : phoneNumber.toCharArray()) {
            if (!Character.isDigit(charachter)) {
                throw new InvalidRequestException(ResponseConstant.ENTER_PROPER_PHONE_NUMBER);
            }
        }
    }

    private User findById(String id) {
        return userRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundExceptions(ResponseConstant.USER_NOT_FOUND));
    }

    private String createFullName(String firstName, String lastName) {
        return (firstName + " " + lastName);
    }


    private void passwordValidation(String password) {

        int count = 0, count1 = 0;
        if (password.length() < 8 || password.length() > 12) {
            throw new InvalidRequestException(ResponseConstant.PASSWORD_MINIMUM_8_AND_MAXIMUM_12_CHARACTER);
        }
        if (password.contains(" ")) {
            throw new InvalidRequestException(ResponseConstant.PASSWORD_WITHOUT_SPACE);
        }
        if (!(password.contains("@") || password.contains("#") || password.contains("!") ||
                password.contains("~") || password.contains("$") || password.contains("%") ||
                password.contains("^") || password.contains("&") || password.contains("*") ||
                password.contains("(") || password.contains(")") || password.contains("-") || password.contains("+") || password.contains("/") || password.contains(":") || password.contains(".") || password.contains(", ") || password.contains("<") || password.contains(">") || password.contains("?") || password.contains("|"))) {
            throw new InvalidRequestException(ResponseConstant.PASSWORD_NEED_ONE_SPECIAL_CHARACTERS);
        }
        if (!(password.contains("0") || password.contains("1") || password.contains("2") || password.contains("3") || password.contains("4") || password.contains("5") || password.contains("6") || password.contains("7") || password.contains("8") || password.contains("9"))) {
            throw new InvalidRequestException(ResponseConstant.PASSWORD_NEED_ONE_NUMERICAL_NUMBER);
        }
        for (char charachter : password.toCharArray()) {
            if (Character.isUpperCase(charachter)) {
                count = count + 1;
            }
            if (Character.isLowerCase(charachter)) {
                count1 = count1 + 1;
            }
        }
        if (count < 1) {
            throw new InvalidRequestException(ResponseConstant.PASSWORD_NEED_ONE_UPPERCASE_AND_LOWERCASE);
        }
        if (count1 < 1) {
            throw new InvalidRequestException(ResponseConstant.PASSWORD_NEED_ONE_UPPERCASE_AND_LOWERCASE);
        }
    }

    private void emailForRegistrationSuccessfully(User user, String email) {
        URL url = Resources.getResource("templates/" + "emailTemplate.html");
        String path;
        try {
            path = Resources.toString(url, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MustacheClass mustachClass = new MustacheClass();
        String m = mustachClass.getTemplate(path, user);
        emailService.sendMessageWithAttachment(email, "Registration Completed Successfully", m);

        List<AdminConfig> adminConfigs = adminConfigRepository.findAll();
        if (!CollectionUtils.isEmpty(adminConfigs)) {
            List<String> list = adminConfigs.get(0).getEmailsList();
            for (String s : list) {
                emailService.sendMessageWithAttachment(s, "New Registration", m);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    @Override
    public User addUser(UserRequest userRequest) {
        // user is null or not
        if (ObjectUtils.isEmpty(userRequest.getEmail()) || ObjectUtils.isEmpty(userRequest.getUserName()) || ObjectUtils.isEmpty(userRequest.getPassword()) || ObjectUtils.isEmpty(userRequest.getPhoneNumber()) || ObjectUtils.isEmpty(userRequest.getFirstName()) || ObjectUtils.isEmpty(userRequest.getLastName()) || ObjectUtils.isEmpty(userRequest.getAddress()) || ObjectUtils.isEmpty(userRequest.getCity()) || ObjectUtils.isEmpty(userRequest.getState())) {
            throw new InvalidRequestException(ResponseConstant.USER_DETAILS_IS_EMPTY);
        }

        // FOR EMAIL VALIDATION
        if (userRepository.existsByEmailAndSoftDeleteIsFalse(userRequest.getEmail())) {
            throw new AlreadyExistsException(ResponseConstant.EMAIL_ALREADY_EXISTS);
        }
        //For Email Validation
        emailValidation(userRequest.getEmail());
        // For Password Validation
        passwordValidation(userRequest.getPassword());
        // For PhoneNumber Validation
        phoneNumberValidation(userRequest.getPhoneNumber());
        User user = modelMapper.map(userRequest, User.class);
        // FOR CURRENT DATE
        user.setRegistrationDate(new Date());
        // FOR USER FULL NAME
        user.setFullName(createFullName(userRequest.getFirstName(), userRequest.getLastName()));
        // For Password
        user.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));


        user.setUserRole(userRequest.getUserRole());
        if (userRequest.getUserRole().equals(Roles.ADMIN))
        {
            user.setRoles(List.of(Roles.ADMIN,Roles.USER));

            List<AdminConfig> adminConfigs = adminConfigRepository.findAll();
            AdminConfig adminConfig = new AdminConfig();
            List<String> list2 = new ArrayList<>();
            if (!CollectionUtils.isEmpty(adminConfigs)) {
                adminConfig = adminConfigs.get(0);
                list2 = adminConfig.getEmailsList();
            }
            list2.add(userRequest.getEmail());
            adminConfig.setEmailsList(list2);
            adminConfigRepository.save(adminConfig);
        }


        if(!userRequest.getUserRole().equals(Roles.ADMIN))
        {
            user.setRoles(List.of(userRequest.getUserRole()));
            emailForRegistrationSuccessfully(user, user.getEmail());
        }

        user.setUserState(UserState.DRAFT);
        user.setOldPasswords(List.of(bCryptPasswordEncoder.encode(userRequest.getPassword())));
        userRepository.save(user);
        return user;
    }


    @Override
    public List<User> getAllUser() {
        return userRepository.findAllBySoftDeleteIsFalse();
    }

    @Override
    public void deleteById(String id) {
        User user;
        try {
            user = findById(id);
            user.setSoftDelete(true);
        } catch (Exception e) {
            throw new NotFoundExceptions(ResponseConstant.USER_NOT_FOUND);
        }
        userRepository.save(user);
    }

    @Override
    public User updateUser(String id, UserRequest userRequest) throws InvocationTargetException, IllegalAccessException {
        User user = findById(id);
        if (ObjectUtils.isEmpty(userRequest.getEmail())) {
            throw new AlreadyExistsException(ResponseConstant.INVALID_REQUEST_DESCRIPTION);
        }
        if (userRepository.existsByEmailAndSoftDeleteIsFalse(userRequest.getEmail())) {
            throw new AlreadyExistsException(ResponseConstant.EMAIL_ALREADY_EXISTS);
        }

        if (userRequest.getFirstName() != null) {
            user.setFullName(createFullName(userRequest.getFirstName(), user.getLastName()));
        }
        if (userRequest.getLastName() != null) {
            user.setFullName(createFullName(user.getFirstName(), userRequest.getLastName()));
        }
        beanUtilBeans.copyProperties(user, userRequest);
        return userRepository.save(user);
    }


    public Page<User> getDataWithPagination(PaginationRequest paginationRequest) {
        return userRepositoryCustom.GetAllDataWithPagination(paginationRequest);
    }

    @Override
    public User setForgotPassword(String id, ForgotPassword forgotPassword) {
        User user = findById(id);
        if (!forgotPassword.getNewPassword().equals(forgotPassword.getConfirmPasseord())) {
            throw new InvalidRequestException(ResponseConstant.ENTER_PROPER_CONFIRM_PASSWORD);
        }
        passwordValidation(forgotPassword.getNewPassword());
        List<String> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(user.getOldPasswords())) {
            list = user.getOldPasswords();
            for (String oldPassword : list) {
                if (bCryptPasswordEncoder.matches(forgotPassword.getNewPassword(), oldPassword)) {
                    throw new InvalidRequestException(ResponseConstant.YOU_HAVE_ALREADY_SELECTED_THIS_PASSWORD);
                }
            }
        }
        if (!bCryptPasswordEncoder.matches(forgotPassword.getOldPassword(), user.getPassword())) {
            throw new InvalidRequestException(ResponseConstant.OLD_PASSWORD_IS_WRONG);
        }
        user.setPassword(bCryptPasswordEncoder.encode(forgotPassword.getNewPassword()));
        list.add(bCryptPasswordEncoder.encode(forgotPassword.getOldPassword()));
        user.setOldPasswords(list);
        userRepository.save(user);

        return null;
    }

    @Override
    public UserTokenResponse logIn(LoginDecorater loginDecorater)
    {
        if(ObjectUtils.isEmpty(loginDecorater.getPassword()) || ObjectUtils.isEmpty(loginDecorater.getEmail()))
        {
            throw new NotFoundExceptions(ResponseConstant.USER_DETAILS_IS_EMPTY);
        }
        User user = userRepository.findByEmailAndSoftDeleteIsFalse(loginDecorater.getEmail());
        if(ObjectUtils.isEmpty(user))
        {
            throw new InvalidRequestException(ResponseConstant.ENTER_VALID_EMAIL_ADDRESS);
        }
        if(!bCryptPasswordEncoder.matches(loginDecorater.getPassword(),user.getPassword()))
        {
            AdminConfig adminConfig = adminConfigRepository.findAll().get(0);
            if(user.getLoginFailCount() >=adminConfig.getMaxLogin())
            {
                user.setUserState(UserState.BLOCK);
                userRepository.save(user);
                throw new InvalidRequestException(ResponseConstant.YOUR_ACCOUNT_IS_BLOCKED );
            }
             int logintrycount = user.getLoginFailCount()+1;
             user.setLoginFailCount(logintrycount);
             userRepository.save(user);
             throw new InvalidRequestException(ResponseConstant.Password_IS_Worng);
        }
        JWTUtils jwtUtils = new JWTUtils();
        List list = new ArrayList();
        list.add(Roles.USER);
        String token = jwtUtils.generateToken(user.getId(), list);

        UserTokenResponse userTokenResponse = new UserTokenResponse();
        userTokenResponse = modelMapper.map(user, UserTokenResponse.class);
        userTokenResponse.setToken(token);

        return userTokenResponse;
    }

    @Override
    public User unblock(String id)
    {
        if(id==(null))
        {
            throw new InvalidRequestException(ResponseConstant.USER_DETAILS_IS_EMPTY);
        }
        User user = findById(id);
        if(UserState.BLOCK.equals(user.getUserState()))
        {
            user.setUserState(UserState.DRAFT);
            user.setLoginFailCount(0);
        }
        userRepository.save(user);
        return user;
    }
}

/*
        byte[] decodedBytes = Base64.getDecoder().decode(encodedpassword);
        String decodepassword = new String(decodedBytes);
        log.info("-------->>>>>> {} " , decodepassword);
        log.info("-------->>>>>> {} " , decodepassword);
 */

/*
   No -2
            List list = new ArrayList();
            list.add("Varified");
            user.setUserRoleIs(list);

    public String updateUserPassword(String id ,String newPassword , String confirmPassword)
    {
        if(!newPassword.equals(confirmPassword))
        {
            throw new InvalidRequestException(ResponseConstant.CONFIRM_PASSWORD_NOT_SAME);
        }
        User user;
            user = findById(id);
            String encodedpassword = Base64.getEncoder().encodeToString(newPassword.getBytes());
            user.setPassword(encodedpassword);
            userRepository.save(user);
        return "Password is Updated";
    }
 */
/*
int count = 0,count1 = 0,count3 = 0,count4 = 0;
        if(password.length() < 8 || password.length() >12 )
        {
            throw new InvalidRequestException(ResponseConstant.PASSWORD_MINIMUM_8_AND_MAXIMUM_12_CHARACTER);
        }
        for(int j=0;j<password.length();j++)
        {
            for(int a=65;a<=90;a++)
            {
                char b = (char)a;
                if(password.charAt(j)==b)
                {
                    count = count+1;
                }
            }
        }
        for(int k=0;k<password.length();k++)
        {
            for(int a1=97;a1<=122;a1++)
            {
                char b1 = (char)a1;
                if(password.charAt(k)==b1)
                {
                    count1 = count1 + 1;
                }
            }
        }
        for(int i=0;i<password.length();i++)
        {
            for(int a3=48;a3<=57;a3++)
            {
                char b3 = (char)a3;
                if(password.charAt(i)==a3)
                {
                    count3 = count3 + 1;
                }
            }
            for(int a3=33;a3<=47;a3++)
            {
                char b3 = (char)a3;
                if(password.charAt(i)==a3)
                {
                    count4 = count4 + 1;
                }
            }
            for(int a3=58;a3<=64;a3++)
            {
                char b3 = (char)a3;
                if(password.charAt(i)==a3)
                {
                    count4 = count4 + 1;
                }
            }
        }
        if(count < 1)
        {
            throw new InvalidRequestException(ResponseConstant.PASSWORD_NEED_ONE_UPPERCASE_AND_LOWERCASE);
        }
        if(count1 < 1)
        {
            throw new InvalidRequestException(ResponseConstant.PASSWORD_NEED_ONE_UPPERCASE_AND_LOWERCASE);
        }
        if(count3 < 1)
        {
            throw new InvalidRequestException(ResponseConstant.PASSWORD_NEED_ONE_NUMERICAL_NUMBER);
        }
        if(count4 < 1)
        {
            throw new InvalidRequestException(ResponseConstant.PASSWORD_NEED_ONE_SPECIAL_CHARACTERS);
        }
 */