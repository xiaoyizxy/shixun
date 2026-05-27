package com.example.demo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MPcesi {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testLambdaQuery() {
        System.out.println("====== LambdaQueryWrapper 查询测试 ======");
        
        // 使用 LambdaQueryWrapper 组装查询条件
        // 条件1：年龄 >= 20
        // 条件2：邮箱包含 @qq.com
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(User::getAge, 20)
                   .like(User::getEmail, "@qq.com");
        
        // 执行查询
        List<User> userList = userMapper.selectList(queryWrapper);
        
        // 打印结果
        System.out.println("查询条件：年龄 >= 20 且 邮箱包含 @qq.com");
        System.out.println("查询到 " + userList.size() + " 条记录：");
        System.out.println("------------------------");
        
        if (userList.isEmpty()) {
            System.out.println("未查询到符合条件的用户");
        } else {
            userList.forEach(user -> {
                System.out.println("ID：" + user.getId());
                System.out.println("用户名：" + user.getUsername());

            });
        }


    }
}