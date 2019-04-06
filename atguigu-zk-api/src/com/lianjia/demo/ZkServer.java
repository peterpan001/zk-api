package com.lianjia.demo;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: panli004
 * @date: 2019/3/18
 * Description: No Description
 */
public class ZkServer {

    private String connectString = "lianjia02:2181,lianjia03:2181,lianjia04:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;
    private String parentNode = "/servers";
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        ZkServer zkServer = new ZkServer();
        //获取连接zkServer
        zkServer.getConnect();

        //注册服务器节点信息
        zkServer.regist(str);

        //业务逻辑处理
        zkServer.business(str);


    }

    /**
     * 业务逻辑处理
     */
    private void business(String hostname) throws InterruptedException {
        System.out.println(hostname + " is online!");
        Thread.sleep(5000);
    }

    /**
     * 注册服务器节点信息
     * @param hostname
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void regist(String hostname) throws KeeperException, InterruptedException {
        String path = zkClient.create(parentNode+"/server",hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(path);
    }

    /**
     * 获取连接zkServer
     * @throws IOException
     */
    private void getConnect() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
            }
        });
    }
}
