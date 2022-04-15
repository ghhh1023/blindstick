package com.blindstick.service.serviceImpl;

import com.blindstick.mapper.UserInfoMapper;
import com.blindstick.mapper.UserMapper;
import com.blindstick.model.User;
import com.blindstick.model.UserInfo;
import com.blindstick.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    final char []codeSequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
    final int SALT_LENGTH = 8;//盐值长度
    final int ENCRYPT_NUM=1024;//加密次数

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public Boolean login(String userName, String password) {

        Subject currentUser = SecurityUtils.getSubject();


        UsernamePasswordToken token=new UsernamePasswordToken(userName,password);
        try {
            currentUser.login(token);//登入验证
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean logout(){
        Subject currentUser=SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()){
            currentUser.logout();
            return true;
        }else {
            return false;
        }
    }

    @Override
    public User findUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
    }

    @Override
    public User getUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
    }

    @Override
    public Integer[] getAllUserIdList() {
        Integer[] integers=userMapper.getAllUserIdList();
        return integers;
    }


    @Override
    public void register(User user)
    {
        String password = user.getPassword();
        String salt=produceSalt();//生成八位的盐值
        ByteSource byteSource=ByteSource.Util.bytes(salt);
        SimpleHash simpleHash=new SimpleHash("md5",password,byteSource,ENCRYPT_NUM);
        user.setPassword(simpleHash.toHex());
        user.setSalt(salt);
        userMapper.register(user);
    }


    /**
     * 修改
     * @param userInfo
     * @return
     */
    @Override
    public boolean alterUserInfo(UserInfo userInfo) {

        return true;
    }

    @Override
    public UserInfo getUserInfo(Integer id) {
        UserInfo userInfo = userInfoMapper.getUserInfoById(id);
        return userInfo;
//        return new UserInfoEntity(userInfo.getId(),userInfo.getSex(),userInfo.getEmail(),userInfo.getNickName());
    }

    @Override
    public User getUserByUserId(Integer valueOf) {
        return userMapper.getUserByUserId(valueOf);
    }


    @Override
    public boolean alterEquipmentId(Integer eid, Integer id) {
        return userInfoMapper.alterEquipmentId(eid,id);
    }

    @Override
    public Integer getUserIdByEid(Integer eid) {
        return userInfoMapper.getUserIdByEid(eid);
    }





    public String produceSalt()
    {
        StringBuilder randomString= new StringBuilder();
        Random random = new Random();
        for(int i = 0;i < SALT_LENGTH;i++)
        {
            String strRand = null;
            strRand = String.valueOf(codeSequence[random.nextInt(62)]);
            randomString.append(strRand);
        }
        return randomString.toString();
    }
}
