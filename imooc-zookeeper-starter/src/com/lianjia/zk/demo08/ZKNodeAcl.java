package com.lianjia.zk.demo08;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;

/**
 * acl - 默认匿名权限
 */
public class ZKNodeAcl implements Watcher {
    private ZooKeeper zooKeeper = null;
    private static final String connectionPath = "192.168.2.131:2181";
    private static final Integer sessionTimeout = 5000;
    public ZKNodeAcl(){}
    public ZKNodeAcl(String connectionPath){
        try {
            zooKeeper = new ZooKeeper(connectionPath, sessionTimeout, new ZKNodeAcl());
        } catch (IOException e) {
            e.printStackTrace();
            if(zooKeeper!=null){
                try {
                    zooKeeper.close();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void createZKNode(String path, byte[] data, List<ACL> acls) {

        String result = "";
        try {
            /**
             * 同步或者异步创建节点，都不支持子节点的递归创建，异步有一个callback函数
             * 参数：
             * path：创建的路径
             * data：存储的数据的byte[]
             * acl：控制权限策略
             * 			Ids.OPEN_ACL_UNSAFE --> world:anyone:cdrwa
             * 			CREATOR_ALL_ACL --> auth:user:password:cdrwa
             * createMode：节点类型, 是一个枚举
             * 			PERSISTENT：持久节点
             * 			PERSISTENT_SEQUENTIAL：持久顺序节点
             * 			EPHEMERAL：临时节点
             * 			EPHEMERAL_SEQUENTIAL：临时顺序节点
             */
            result = zooKeeper.create(path, data, acls, CreateMode.PERSISTENT);
            System.out.println("创建节点：\t" + result + "\t成功...");
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ZKNodeAcl zkServer = new ZKNodeAcl(connectionPath);
        zkServer.createZKNode("/lianjia", "1234lianjia".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);
    }

    @Override
    public void process(WatchedEvent event) {
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }
    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }
}
