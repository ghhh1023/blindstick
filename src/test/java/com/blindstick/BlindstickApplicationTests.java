package com.blindstick;

import com.blindstick.mapper.UserMapper;
import com.blindstick.model.User;
import com.blindstick.netty.service.SocketServer;
import com.blindstick.utils.SpringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class BlindstickApplicationTests {


    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() {
        new SocketServer().start(4001);
        User user=new User();
        user.setUserName("13574522165");
        user.setPassword("wqeqweqwe");
        user.setSalt("qweqweqwe");
        userMapper.register(user);
    }

}
