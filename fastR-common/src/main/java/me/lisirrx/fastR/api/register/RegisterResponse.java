package me.lisirrx.fastR.api.register;

import java.io.Serializable;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/27
 */
public class RegisterResponse implements Serializable {

    private String msg;

    public RegisterResponse(String msg) {
        this.msg = msg;
    }

    public static RegisterResponse ok(){
        return new RegisterResponse("OK");
    }

    public String getMsg() {
        return msg;
    }
}
