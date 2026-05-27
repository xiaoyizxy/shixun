package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
//    /**
//     * 查询所有用户
//     * @return
//     */
//    @Select("select * from user")
//    List<User> selectAll();
//
//    @Select("select * from user where id=#{id}")
//    User selectById(Long id);
//
//    @Select("select * from user where username=#{username} and password=#{password}")
//    List<User> selectByusernameandPassword(String username,String password);
//
//    //@Select("select * from user where username=#{username} and password=#{password}")
//    List<User> selectByConditions(User user);
//
//
//
//    int saveBean(User user);//返回值为受影响的行数
//    int update(User user);
//    int delete(Long id);
//    int insertList(List<User> users);

}
