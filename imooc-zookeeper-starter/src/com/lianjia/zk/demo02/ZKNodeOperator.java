package com.lianjia.zk.demo02;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import java.io.IOException;
import java.util.List;

/**
 * 创建zk节点操作
 */
public class ZKNodeOperator implements Watcher {

    private ZooKeeper zookeeper = null;
    public ZooKeeper getZookeeper() {
        return zookeeper;
    }
    public void setZookeeper(ZooKeeper zookeeper) {
        this.zookeeper = zookeeper;
    }

    //设置zk服务器连接地址
    private static final String connectString = "192.168.2.131:2181";

    //设置zk的sessionTimeOut
    private static final Integer sessionTimeout=5000;

    /**
     * 无参的构造函数
     */
    public ZKNodeOperator(){
    }

    /**
     * 一个参数的构造函数
     * @param connectString
     */
    public ZKNodeOperator(String connectString){
        try {
            zookeeper = new ZooKeeper(connectString, sessionTimeout,new ZKNodeOperator());
        } catch (IOException e) {
            e.printStackTrace();
            if(zookeeper!=null){
                try {
                    zookeeper.close();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
    }

    /**
     * 创建zk的临时节点节点
     * @param path
     * @param data
     * @param acls
     */
    public void createZKEphemralNode(String path, byte[] data, List<ACL> acls){
        String result = "";

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
        //创建临时节点
        try {
            result = zookeeper.create(path, data, acls, CreateMode.EPHEMERAL);
            System.out.println("创建临时节点成功：" + result);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建zk永久节点
     * @param path
     * @param data
     * @param acls
     */
    public void createZKPersistentNode(String path, byte[] data, List<ACL> acls){
        try {
            String ctx = "{'create':'success'}";
            zookeeper.create(path,data,acls,CreateMode.PERSISTENT,new CreateCallBack(),ctx);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ZKNodeOperator zkServer = new ZKNodeOperator(connectString);
        //创建临时节点
        //zkServer.createZKEphemralNode("/testnode","testnode".getBytes(), Ids.OPEN_ACL_UNSAFE);

        //创建永久节点
        zkServer.createZKPersistentNode("/testnode", "testnode".getBytes(),Ids.OPEN_ACL_UNSAFE);
    }
}
