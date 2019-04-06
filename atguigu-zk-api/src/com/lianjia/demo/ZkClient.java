package com.lianjia.demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: panli004
 * @date: 2019/3/18
 * Description: No Description
 */
public class ZkClient {

    private ZooKeeper zkClient;
    private String connectString = "lianjia02:2181,lianjia03:2181,lianjia04:2181";
    private int sessionTimeout = 2000;
    private String parentNode = "/servers";

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZkClient zkCli = new ZkClient();
        //获取连接
        zkCli.getConnection();
        //监听服务器节点路径
        zkCli.getServerList();
        //业务处理
        zkCli.business();

    }

    /**
     * 业务逻辑处理
     * @throws InterruptedException
     */
    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 获取服务器子节点列表
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void getServerList() throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren(parentNode,true);
        ArrayList<String> serverList = new ArrayList<String>();
        for(String child : children){
            byte[] data = zkClient.getData(parentNode+"/"+child,false,null);
            serverList.add(new String(data));
        }
        System.out.println(serverList);
    }

    /**
     * 获取连接
     * @throws IOException
     */
    private void getConnection() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    getServerList();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
