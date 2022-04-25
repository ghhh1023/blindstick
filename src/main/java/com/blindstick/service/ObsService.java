package com.blindstick.service;

public interface ObsService {
    // localPath 指定图片的本地路径,obsPath是对OBS存储的图片的重命名
    public Boolean uploadImage(String localPath, String obsPath);
}
