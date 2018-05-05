package com.tjlcast.server;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by tangjialiang on 2018/5/4.
 */
public class ServiceDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class) ;

    private CountDownLatch latch = new CountDownLatch(1) ;

    private volatile List<String> dataList = new ArrayList<>() ;

    private String registerAddress ;

    public ServiceDiscovery(String registerAddress) {
        this.registerAddress = registerAddress ;
    }

    public String discover() {
        String data = null ;
        int size = dataList.size() ;

        if (size > 0) {
            if (size == 1) {
                data = dataList.get(0) ;
                LOGGER.debug("using only data: {}", data);
            } else {
                data = dataList.get(ThreadLocalRandom.current().nextInt(size)) ;
                LOGGER.debug("using random data: {}", data);
            }
        }
        return data ;
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
            latch.await() ;
        } catch (IOException | InterruptedException e) {
            LOGGER.error("", e);
        }

        return null ;
    }

    private void watchNode(final ZooKeeper zk) {
        try {
            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTER_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                        watchNode(zk);
                    }
                }
            });
            ArrayList<String> dataList = new ArrayList<>();
            for (String node : nodeList) {
                byte[] data = zk.getData(Constant.ZK_DATA_PATH + "/" + node, false, null);
                dataList.add(new String(data)) ;
            }
            LOGGER.debug("node data: {}", dataList);
            this.dataList = dataList ;
        } catch (KeeperException | InterruptedException e) {
            LOGGER.error("", e);
        }
    }
}
