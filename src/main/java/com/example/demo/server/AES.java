package com.example.demo.server;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES算法进行加密
 *
 * @author xxx
 * @create 2022年2月22日 下午1:52:52
 **/
public class AES {
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    private static byte[] aesEncryptToBytes(byte[] content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        return cipher.doFinal(content);
    }

    public static byte[] encrypt(byte[] content, String encryptKey) throws Exception {
        return aesEncryptToBytes(content, encryptKey);
    }

}

