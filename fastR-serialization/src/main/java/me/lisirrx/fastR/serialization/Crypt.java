package me.lisirrx.fastR.serialization;

import javax.crypto.BadPaddingException;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/6/10
 */
public interface Crypt {
    byte[] encrypt(byte[] input);
    byte[] decrypt(byte[] input) throws BadPaddingException;

    String cryptSign();

}
