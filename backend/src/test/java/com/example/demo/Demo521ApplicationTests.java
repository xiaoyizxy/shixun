//package com.example.demo;
//
//import com.example.demo.mapper.UserMapper;
//import entity.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//class Demo521ApplicationTests {
//
//    @Autowired
//    private UserMapper userMapper;
//    @Test
//    <USer>
//    void contextLoads() {
////        User user = userMapper.selectById(1L);
////        System.out.println(user);
//
////        List<User> users =userMapper.selectByusernameandPassword("lihua","123456");
////        System.out.println(users);
//
//        User user = new User();
//        //user.setUsername("lihua");
//        user.setPassword("123456");
//        List<User> result = userMapper.selectByConditions(user);
//        System.out.println(result);
//
//    }
//    @Test
//    public void testInsert(){
//        User user = new User();
//        user.setUsername("liguanglun");
//        user.setPassword("123456");
//        user.setRealname("李光伦");
//        int result = userMapper.saveBean(user);
//        System.out.println("影响行数："+result);
//    }
//    @Test
//    public void testUpdate(){
//        User user = new User();
//        user.setId(1);
//        user.setUsername("lisi");
//        user.setPassword("123456");
//        user.setRealname("李四");
//        int result = userMapper.update(user);
//        System.out.println("影响行数："+result);
//    }
//    @Test
//    public void testDelete(){
//        int result = userMapper.delete(2L);
//        System.out.println("影响行数："+result);
//    }
//    @Test
//    public void testInsertList(){
//        List<User> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            User user = new User();
//            user.setUsername("cesi"+i);
//            user.setPassword("123456");
//            user.setRealname("realname"+i);
//            list.add(user);
//        }
//        int count = userMapper.insertList(list);
//        System.out.println("影响行数"+count);
//    }
//
//}
