package com.blindstick.service.serviceImpl;

import com.blindstick.service.ObsService;
import com.blindstick.utils.HuaweiAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ObsServiceImpl implements ObsService {
    @Autowired
    private HuaweiAPI huaweiAPI;

    @Override
    public Boolean uploadImage(String localPath, String obsPath) {
           return huaweiAPI.uploadImage(localPath,obsPath);
    }
}
