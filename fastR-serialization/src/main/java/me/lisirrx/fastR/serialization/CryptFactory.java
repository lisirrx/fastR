package me.lisirrx.fastR.serialization;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/6/10
 */
public class CryptFactory {

    public static Crypt createCrypt(String crypt, String key){
        if ("DES".equals(crypt)){
            return new  DESCrypt(key);
        } else {
            return new DESCrypt(key);
        }
    }
}
