package com.kuang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

/**
 * @author 84642
 * @version 1.0
 * @description: 实体类
 * @date 2023/7/27 20:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private String password;

}
