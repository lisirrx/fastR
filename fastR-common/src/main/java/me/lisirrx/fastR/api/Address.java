package me.lisirrx.fastR.api;

import java.io.Serializable;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/18
 */
public class Address implements Serializable {
    private String host;
    private int port;

    public Address(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "Address{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
