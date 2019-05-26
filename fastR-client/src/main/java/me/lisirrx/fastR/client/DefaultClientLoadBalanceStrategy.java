package me.lisirrx.fastR.client;

import me.lisirrx.fastR.api.Address;

import java.util.List;
import java.util.Random;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/5/27
 */
public class DefaultClientLoadBalanceStrategy implements ClientLoadBalanceStrategy {

    @Override
    public Address loadBalance(List<Address> addresses) {
        Random random = new Random();
        return addresses.get(random.nextInt(addresses.size()));
    }
}
