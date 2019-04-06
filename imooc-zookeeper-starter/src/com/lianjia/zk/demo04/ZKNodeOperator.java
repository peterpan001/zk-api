package com.lianjia.zk.demo04;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * 删除zk节点
 */
public class ZKNodeOperator implements Watcher {

    private ZooKeeper zookeeper = null;

    //参数信息
    private static final String connectPath = "192.168.2.131:2181";
    private static final Integer sessionTimeout = 5000;

    public ZKNodeOperator(){}

    public ZKNodeOperator(String connectPath){
        try {
            zookeeper = new ZooKeeper(connectPath,sessionTimeout,new ZKNodeOperator());
        } catch (Exception e) {
            e.printStackTrace();
            if(zookeeper!=null){
                try {
                    zookeeper.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ZKNodeOperator zkServer = new ZKNodeOperator(connectPath);
        try {
            //同步删除zk节点信息
//            zkServer.getZookeeper().delete("/testnode", 4);

            //异步删除zk节点信息
            String ctx = "{'delete':'success'}";
            zkServer.getZookeeper().delete("/testnode",1,new DeleteCallBack(),ctx);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {

    }

    public ZooKeeper getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(ZooKeeper zookeeper) {
        this.zookeeper = zookeeper;
    }
}
