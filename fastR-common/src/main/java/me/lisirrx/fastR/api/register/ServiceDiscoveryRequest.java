package me.lisirrx.fastR.api.register;

import java.io.Serializable;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/27
 */
public class ServiceDiscoveryRequest implements Serializable {
    private String serviceName;

    public ServiceDiscoveryRequest(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
