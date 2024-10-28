package com.kuang.utils;

import org.junit.Test;

import java.util.UUID;

/**
 * @author 84642
 * @version 1.0
 * @description: TODO
 * @date 2023/8/4 0:04
 */
public class IDutils {
    public static String getId(){
        return UUID.randomUUID().toString().replace("-","");
    }
    @Test
    public void test(){
        System.out.println(IDutils.getId());
    }
}
