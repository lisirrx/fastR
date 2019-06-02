package me.lisirrx.fastR.center;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author lihan lisirrx@gmail.com
 * @date 2019/6/2
 */
@Configuration
public class ZKConfig {
    @Bean
    public ZkClient zkClient(){
        ZkClient zkClient = new ZkClient("127.0.0.1:2181");
        List<String> firstLevel = zkClient.getChildren("/");
        if (zkClient.exists("/fastR")){
            return zkClient;
        }
        zkClient.create("/fastR", "", CreateMode.PERSISTENT);
        return zkClient;
    }
}
