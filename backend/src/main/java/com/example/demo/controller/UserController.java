package com.example.demo.controller;

import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.entity.Result;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserMapper mapper;
    @Autowired
    private UserService service;

    @GetMapping("/demo")
    public String demo(){
        return "";
    }

    @GetMapping("/login")
    public Result login(User user){
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return Result.error("密码不能为空");
        }
        User loginUser = service.login(user);
        if (loginUser != null){
            return Result.success("登录成功", loginUser);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return Result.error("密码不能为空");
        }
        // 检查用户名是否已存在
        User exist = service.findByUsername(user.getUsername());
        if (exist != null) {
            return Result.error("用户名已存在");
        }
        user.setRole("user");
        user.setStatus("active");
        user.setCreateTime(new java.util.Date());
        user.setUpdateTime(new java.util.Date());
        service.save(user);
        return Result.success("注册成功", user);
    }

    @GetMapping("/find")
    public List<User> find(User user){
        return service.listByCondition(user);
    }

    // ---- 用户管理 CRUD ----

    @GetMapping("/user/list")
    public Result userList(@RequestParam(required = false) String keyword) {
        List<User> list = service.listByCondition(new User());
        if (keyword != null && !keyword.isEmpty()) {
            list = list.stream().filter(u ->
                u.getUsername().contains(keyword) || u.getRealname().contains(keyword)
            ).toList();
        }
        return Result.success(list);
    }

    @GetMapping("/user/{id}")
    public Result getUser(@PathVariable Long id) {
        return Result.success(service.getById(id));
    }

    @PostMapping("/user")
    public Result addUser(@RequestBody User user) {
        if (user.getRole() == null) user.setRole("护理人员");
        service.save(user);
        return Result.success(user);
    }

    @PutMapping("/user")
    public Result updateUser(@RequestBody User user) {
        service.updateById(user);
        return Result.success(user);
    }

    @DeleteMapping("/user/{id}")
    public Result deleteUser(@PathVariable Long id) {
        service.removeById(id);
        return Result.success("删除成功", null);
    }
}
