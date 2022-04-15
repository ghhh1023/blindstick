package com.blindstick;

import com.blindstick.netty.service.SocketServer;
import com.blindstick.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BlindstickApplication {

    public static void main(String[] args) {
        ApplicationContext app=SpringApplication.run(BlindstickApplication.class, args);
        SpringUtil.setApplicationContext(app);
        new SocketServer().start(4001);
    }

}
