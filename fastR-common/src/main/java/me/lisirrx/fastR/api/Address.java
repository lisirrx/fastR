package me.lisirrx.fastR.api;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/18
 */
public class Address {
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
}
