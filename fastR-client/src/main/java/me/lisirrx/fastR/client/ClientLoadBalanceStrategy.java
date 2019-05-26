package me.lisirrx.fastR.client;

import me.lisirrx.fastR.api.Address;

import java.util.List;
import java.util.Set;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/27
 */
public interface ClientLoadBalanceStrategy {
    Address loadBalance(List<Address> addresses);
}
