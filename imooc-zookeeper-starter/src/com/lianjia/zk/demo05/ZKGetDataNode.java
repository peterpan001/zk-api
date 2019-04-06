package com.lianjia.zk.demo05;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.Watcher.Event.EventType;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * zk获取节点数据的demo演示
 */
public class ZKGetDataNode implements Watcher {
    private ZooKeeper zooKeeper = null;

    private static final String connectionPath = "192.168.2.131:2181";
    private static final Integer sessionTiemout = 5000;
    private static final Stat status = new Stat();
    private static CountDownLatch countDown = new CountDownLatch(1);
    public ZKGetDataNode(){}

    public ZKGetDataNode(String connectionPath){
        try {
            zooKeeper = new ZooKeeper(connectionPath, sessionTiemout, new ZKGetDataNode());
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
        ZKGetDataNode zkServer = new ZKGetDataNode(connectionPath);
        try {
            byte[] resByte = zkServer.getZooKeeper().getData("/testnode", true,status);
            String result = new String(resByte);
            System.out.println("返回的结果值为:" + result);
            countDown.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        try{
            if(event.getType() == EventType.NodeDataChanged){
                ZKGetDataNode zkServer = new ZKGetDataNode(connectionPath);
                byte[] resByte = zkServer.getZooKeeper().getData("/testnode", false, status);
                String result = new String(resByte);
                System.out.println("更改后的值：" + result);
                System.out.println("版本号变化dversion：" + status.getVersion());
                countDown.countDown();
            }else if(event.getType() == EventType.NodeChildrenChanged){

            }else if(event.getType() == EventType.NodeCreated){

            }else if(event.getType() == EventType.NodeDeleted){

            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {

        }

    }



    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }
    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }
}
