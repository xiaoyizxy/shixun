package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.User;

import java.util.List;

public interface UserService extends IService<User> {


    User login(User user);

    User findByUsername(String username);

    List< User> listByCondition(String username, String password);

    List< User> listByCondition(User user);
}
