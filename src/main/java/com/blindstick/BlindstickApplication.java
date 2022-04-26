package com.blindstick;

import com.blindstick.netty.service.SocketServer;
import com.blindstick.utils.HuaweiAPI;
import com.blindstick.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BlindstickApplication {
    public static void main(String[] args) {
        ApplicationContext app=SpringApplication.run(BlindstickApplication.class, args);
        System.out.println(app.getBean("huaweiAPI"));
        HuaweiAPI huaweiAPI = (HuaweiAPI) app.getBean("huaweiAPI");
        String localPath="C://Users//lenovo//Desktop//ai//test.jpg";
        String obsPath="bind/demo.jpg";
        huaweiAPI.uploadImage(localPath,obsPath);
        SpringUtil.setApplicationContext(app);
        new SocketServer().start(4001);

    }

}
