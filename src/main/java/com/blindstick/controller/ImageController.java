package com.blindstick.controller;

import com.blindstick.service.ImageService;
import com.blindstick.service.ObsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author 许金涛
 * 与图像识别有关的控制器,根据path参数传入的图片路径进行识别
 */
@RestController
public class ImageController {
    @Autowired
    private ImageService imageService;

    @Autowired
    private ObsService obsService;

    @GetMapping("/upload")
    public String getImageTag(@RequestParam(value="path",defaultValue="1a5ce2300dbd9c48b1bac6c9dca199a46a182525.jpg",required = false)
                                      String path){
//        String localPath="C://Users//lenovo//Desktop//ai//test.jpg";
//        String obsPath="bind/.jpg";
//        obsService.uploadImage(localPath,obsPath);
        System.out.println(imageService.getImageTag(path));
        return imageService.getImageTag(path);
    }
}
