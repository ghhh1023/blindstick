package com.blindstick.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private  Integer id;

    private Integer uid;

    @Length(max = 11,min = 11,message = "手机号长度必须是11位")
    private String phoneNum;

    
    private String eid;
}
