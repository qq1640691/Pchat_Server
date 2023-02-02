package com.example.demo.server;


import sun.misc.BASE64Decoder;

/**
 * 基础加密组件
 */
public abstract class Coder {
  public static byte[] decryptBASE64(String key) throws Exception {
    return (new BASE64Decoder()).decodeBuffer(key);
  }

}
