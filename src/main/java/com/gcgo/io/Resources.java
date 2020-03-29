package com.gcgo.io;

import java.io.InputStream;

public class Resources {

    //读入配置文件转换为字节输入流
    public static InputStream getResourceAsStream(String path) {
        InputStream resourceAsStream = Resources.class.getClassLoader().getResourceAsStream(path);
        return resourceAsStream;
    }
}
