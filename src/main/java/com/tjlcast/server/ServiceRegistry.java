package com.tjlcast.server;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by tangjialiang on 2018/5/2.
 */
public class ServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class) ;

    private CountDownLatch latch = new CountDownLatch(1) ;

    private String registerAddress ;

    /**
     * construct
     * @param registerAddress
     */
    public ServiceRegistry(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    /**
     * register info on zk
     * @param data
     */
    public void register(String data) {
        if (data != null) {
            ZooKeeper zk = connectServer() ;
            if (zk != null) {
                createNode(zk, data) ;
            }
        }
    }

    private void createNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();
            String path = zk.create(Constant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            LOGGER.debug("create zookeeper node ({} => {})", path, data);
        } catch (InterruptedException | KeeperException e) {
            LOGGER.error("error occurs when createNode. ", e) ;
        }
    }

    private ZooKeeper connectServer() {
        ZooKeeper zk = null ;
        try {
            zk = new ZooKeeper(registerAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            }) ;

            latch.wait();
        } catch (IOException | InterruptedException e) {
            LOGGER.error("error occus when connectZk. ", e) ;
        }
        return zk ;
    }
}
