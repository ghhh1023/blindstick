package com.blindstick.mapper;


import com.blindstick.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from user where user_name=#{userName}")
    public User getUserByUserName(@Param("userName") String userName);

    @Select("select * from user where id=#{id}")
    public User getUserByUserId(@Param("id") Integer id);


    @Insert({"insert into user(user_name,password,salt) values(#{userName},#{password},#{salt})"})
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public void register(User user);


    @Select("select * from user where id=#{id}")
    public User getUserById(int id);

    @Select("select id from user")
    public Integer[] getAllUserIdList();
}
