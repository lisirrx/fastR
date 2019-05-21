package me.lisirrx.fastR.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/13
 */
public class Message{
    public final static String SERVICE = "serviceName";
    public final static String METHOD = "methodName";


    private Map<String, String> headers = new HashMap<>();
    private Object data;

    public Message(Object data) {
        this.data = data;
    }

    public Message(Map<String, String> headers, Object data) {
        this.headers = headers;
        this.data = data;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setHeader(String key, String value){
        headers.put(key, value);
    }

    public String getHeader(String key){
        return headers.get(key);
    }
}
