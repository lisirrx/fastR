package me.lisirrx.fastR.api.register;

import me.lisirrx.fastR.api.Address;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/27
 */
public class ServiceDiscoveryResponse implements Serializable {

    private List<Address> addresses;

    public ServiceDiscoveryResponse(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
