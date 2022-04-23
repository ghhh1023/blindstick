package com.blindstick.service.serviceImpl;

import com.blindstick.service.ImageService;
import com.blindstick.utils.HuaweiAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private HuaweiAPI huaweiAPI;

    @Override
    public String getImageTag(String path) {
        return huaweiAPI.getImageTag(path);
    }
}
