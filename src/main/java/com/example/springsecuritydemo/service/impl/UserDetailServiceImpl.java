package com.example.springsecuritydemo.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.example.springsecuritydemo.entity.User;
import com.example.springsecuritydemo.eto.LoginUser;
import com.example.springsecuritydemo.mapper.MenuMapper;
import com.example.springsecuritydemo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
public class UserDetailServiceImpl implements UserDetailsService {



    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户查询信息
        LambdaQueryChainWrapper<User> wrapper = new LambdaQueryChainWrapper<>(userMapper);

        wrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(wrapper);

        if (Objects.isNull(user)){
            throw new RuntimeException("用户名密码错误");
        }


        // TODO 根据用户查询权限添加到user中
//        List<String> list = new ArrayList<>(Arrays.asList("test"));
        List<String> list = menuMapper.selectPermsByUserId(user.getId());
        // 封装成UserDetails对象返回
        return new LoginUser(user, list);
    }
}
