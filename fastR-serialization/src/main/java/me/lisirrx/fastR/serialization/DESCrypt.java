package me.lisirrx.fastR.serialization;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.util.Base64;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/6/10
 */
public class DESCrypt implements Crypt{
    private String key;

    public DESCrypt(String key) {
        this.key = key;
    }

    @Override
    public byte[] encrypt(byte[] input) {
        try {
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance("DES");

            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            return Base64.getEncoder().encode(cipher.doFinal(input));

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] input) throws BadPaddingException {
        try {
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance("DES");

            cipher.init(Cipher.DECRYPT_MODE, securekey);
            input = Base64.getDecoder().decode(input);
            return cipher.doFinal(input);

        } catch (BadPaddingException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String cryptSign() {
        return key;
    }
}
