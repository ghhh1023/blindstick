package com.blindstick.service;


import com.blindstick.model.User;
import com.blindstick.model.UserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    public Boolean login(String userName, String password);

    public User findUserByUserName(String userName);

    public User getUserByUserName(String userName);

    public Integer[] getAllUserIdList();

    public Boolean logout();

    public void register(User user);


    public boolean alterUserInfo(UserInfo userInfo);



    public UserInfo getUserInfo(Integer id);

    User getUserByUserId(Integer valueOf);


    public boolean alterEquipmentId(Integer eid,Integer id);

    public Integer getUserIdByEid(Integer eid);
}
