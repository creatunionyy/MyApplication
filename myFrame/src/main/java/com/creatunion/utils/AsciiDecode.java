package com.creatunion.utils;


public class AsciiDecode {


    /**
     * @功能 字节数据转换成字符串
     * @参数 Ascii码
     * @结果 字符串
     */
    public static String ascii2Str(byte[] bytes){
        return new String(bytes);
    }

    /**
     * @功能 字符串转换成ascill码
     * @参数 字符串
     * @结果 Ascii码
     */
    public  static byte[] str2Ascii(String str){
        return str.getBytes();
    }
}
