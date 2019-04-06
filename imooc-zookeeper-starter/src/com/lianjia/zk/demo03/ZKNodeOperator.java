package com.lianjia.zk.demo03;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 修改zk节点信息
 */
public class ZKNodeOperator implements Watcher {

    //连接地址
    private static final String connectPath = "192.168.2.131:2181";

    //sessionTimeout时间设置
    private static final Integer sessionTimeout = 5000;

    public ZKNodeOperator(){
    }

    private ZooKeeper zooKeeper = null;

    public ZKNodeOperator(String connectPath){
        try {
            zooKeeper = new ZooKeeper(connectPath,sessionTimeout,new ZKNodeOperator());
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

    public static void main(String[] args) {
        ZKNodeOperator zkServer = new ZKNodeOperator(connectPath);
        try {
            //同步修改zk节点信息
//            Stat status = zkServer.getZooKeeper().setData("/testnode", "abcd".getBytes(), 3);
            //得到zk节点数据版本信息
//            System.out.println(status.getVersion());

            //异步修改zk节点信息
            String ctx = "{'update':'success'}";
            zkServer.getZooKeeper().setData("/testnode","123".getBytes(),0,new UpdateCallBack(), ctx);
            //异步方法一定要调用Thread.sleep进行睡眠等待
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
