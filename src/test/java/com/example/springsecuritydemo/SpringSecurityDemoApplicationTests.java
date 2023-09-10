package com.example.springsecuritydemo;

import com.example.springsecuritydemo.entity.User;
import com.example.springsecuritydemo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringSecurityDemoApplicationTests {


    @Autowired
    private UserMapper userMapper;

    @Test
    public void testUserMapper() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }
}
