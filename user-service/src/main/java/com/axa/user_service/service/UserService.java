package com.axa.user_service.service;

import com.axa.user_service.entity.User;
import com.axa.user_service.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;


    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);


    public User saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        return userRepo.save(user) ;

    }

    public boolean checkIfUserExsists(String username){
        return userRepo.existsByUsername(username);
    }

    public int fetchUserId(String username){
        return userRepo.findByUsername(username).getId();
    }



}