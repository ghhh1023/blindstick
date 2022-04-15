package com.blindstick.controller;

import com.blindstick.common.RetJson;
import com.blindstick.config.Client;
import com.blindstick.model.User;
import com.blindstick.service.RedisService;
import com.blindstick.service.UserService;
import com.blindstick.utils.GenerateVerificationCode;
import com.blindstick.utils.JwtUtils;
import com.blindstick.utils.MobileMessageUtil;
import com.blindstick.utils.ValidatedUtil;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author zeng
 * 与用户操作有关的控制器,如登入注册等
 */
@RestController
@Validated
public class UserController {
    private static final Integer MAX_SIZE=5*1024*1024;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    private Integer userId;
    //登入
    @PostMapping("/login")
    public RetJson login(@RequestBody User user, HttpServletRequest request){
        if (!ValidatedUtil.validate(user)){
            return RetJson.fail(-1,"登录失败，请检查用户名或密码！");
        }
        Boolean b=userService.login(user.getUserName(),user.getPassword());
        if (b==true){
            user=userService.getUserByUserName(user.getUserName());
            request.setAttribute("id",user.getId()+"");
            userId=user.getId();
            //登入成功,则发放token
            if (true){
                try {
                    //生成一个随机的不重复的uuid
                    UUID uuid=UUID.randomUUID();
                    request.setAttribute("uuid",uuid.toString());
                    String token= JwtUtils.createToken(uuid,user.getId().toString());
                    //将uuid和user以键值对的形式存放在redis中
                    user.setPassword(null);
                    user.setSalt(null);
                    redisService.set("user:"+user.getId(),uuid.toString(),60*60*24*7);
                    Map map = new LinkedHashMap();
                    map.put("code",0);
                    map.put("msg","登录成功");
                    map.put("token",token);
                    map.put("id",user.getId());
                    return RetJson.success(map);
                }catch (Exception e){
                    return RetJson.fail(-1,"登录失败,请检查用户名或密码");
                }
            }
            Map<String,Object> map=new LinkedHashMap<>();
            map.put("id",user.getId());
            return RetJson.success(map);
        }else {
            return RetJson.fail(-1,"登录失败,请检查用户名或密码");
        }
    }

    //获取手机验证码
    /*
     如果手机号长度不合要求返回提示信息还没写
     */
    @RequestMapping("/getCode")
    public RetJson sendIdentifyingCode( @Pattern(regexp="^1[0-9]{10}$",message="手机号的长度必须是11位.") @NotNull String phoneNumber){
        if(!ValidatedUtil.validate(phoneNumber)){
            return RetJson.fail(-2,"手机号码不合法");
        }
        if ((userService.findUserByUserName(phoneNumber)!=null)){
            return RetJson.fail(-1,"该用户已经注册");
        }
        String verificationCode = GenerateVerificationCode.generateVerificationCode(4);
        Client.Request request=null;
        Client client=new Client();
        request= MobileMessageUtil.sendIdentifyingCode(phoneNumber, verificationCode);
        //在redis中存入用户的账号和对应的验证码
        redisService.set(phoneNumber,verificationCode,300);
        return RetJson.success(0,"验证码已发送");
    }

    /**
     * 注册
     //     * @param user
     //     * @param code
     * @return
     */
    @RequestMapping("/register")
    public RetJson userRegister(@Pattern(regexp="^1[0-9]{10}$",message="手机号的长度必须是11位.") String userName,  @NotNull
    @Length(max = 16,min = 6,message = "密码不合法") String password , String code) {
        User user=new User();
        user.setUserName(userName);
        user.setPassword(password);
        if (!ValidatedUtil.validate(user)) {
            return RetJson.fail(-3, "请检查参数");
        }
        if (redisService.exists(user.getUserName()) && redisService.get(user.getUserName()).equals(code)) {
            if (userService.findUserByUserName(user.getUserName()) == null) {
                userService.register(user);
                return RetJson.success(0,"注册成功");
            }
            return RetJson.fail(-2, "用户已存在！");
        }
        return RetJson.fail(-1, "验证码不正确！");
    }
}
