package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public boolean saveBatch(Collection<User> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<User> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<User> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(User entity) {
        return false;
    }

    @Override
    public User getOne(Wrapper<User> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Optional<User> getOneOpt(Wrapper<User> queryWrapper, boolean throwEx) {
        return Optional.empty();
    }

    @Override
    public Map<String, Object> getMap(Wrapper<User> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<User> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public UserMapper getBaseMapper() {
        return null;
    }

    @Override
    public Class<User> getEntityClass() {
        return null;
    }

    @Override
    public User login(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null){
            return null;
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User bean = baseMapper.selectOne(queryWrapper);

        if (bean == null) {
            return null;
        }

        if (bean.getPassword().equals(user.getPassword())) {
            bean.setPassword(null); // 不返回密码
            return bean;
        }

        return null;
    }

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<User> listByCondition(String username, String password) {
        return null;
    }

    @Override
    public List< User> listByCondition(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (user.getPassword() != null&& user.getPassword() != ""){
            queryWrapper.like(User::getPassword, user.getPassword());
        }
        if (user.getUsername() != null&& user.getUsername() != ""){
            queryWrapper.likeLeft(User::getUsername, user.getUsername());
        }

      return baseMapper.selectList(queryWrapper);
    }
}
