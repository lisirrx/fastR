package me.lisirrx.fastR.api.register;

import me.lisirrx.fastR.api.Address;

import java.io.Serializable;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/27
 */
public class RegisterRequest implements Serializable {
    private String serviceName;
    private Address address;

    public RegisterRequest(String serviceName, Address address) {
        this.serviceName = serviceName;
        this.address = address;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Address getAddress() {
        return address;
    }
}
