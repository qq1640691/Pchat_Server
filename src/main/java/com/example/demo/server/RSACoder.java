package com.example.demo.server;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;

/**
*RSA安全编码组件
*/
public abstract class RSACoder extends Coder {
  public static final String KEY_ALGORITHM = "RSA";

  /**
   * 解密<br>
   * 用私钥解密
   *
   */
    public static byte[] decryptByPrivateKey(byte[] data, String key)
        throws Exception {
      // 对密钥解密
      byte[] keyBytes = decryptBASE64(key);
      // 取得私钥
      PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
      Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
      // 对数据解密
      Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      return cipher.doFinal(data);
    }
}
