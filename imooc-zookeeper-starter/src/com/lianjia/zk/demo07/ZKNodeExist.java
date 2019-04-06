package com.lianjia.zk.demo07;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 判断zk节点是否存在
 */
public class ZKNodeExist implements Watcher {

    private static final String connectionPath = "192.168.2.131:2181";
    private static final Integer sessionTimeout = 5000;
    private ZooKeeper zooKeeper = null;
    public ZKNodeExist(){}
    public ZKNodeExist(String connectionPath){
        try {
            zooKeeper = new ZooKeeper(connectionPath,sessionTimeout,new ZKNodeExist());
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
    private static CountDownLatch countDown = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        ZKNodeExist zkServer = new ZKNodeExist(connectionPath);
        //同步调用
//        Stat stat = zkServer.getZooKeeper().exists("/lianjiadata",true);
//        if(stat!=null){
//            System.out.println("该节点的版本号为:" + stat.getVersion());
//        }else{
//            System.out.println("该节点不存在");
//        }

        String ctx = "{'callBack':'callBack'}";
        zkServer.getZooKeeper().exists("/lianjiadata",true, new StatCallBack(), ctx);
        countDown.await();
    }
    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeCreated) {
            System.out.println("节点创建");
            countDown.countDown();
        } else if (event.getType() == Event.EventType.NodeDataChanged) {
            System.out.println("节点数据改变");
            countDown.countDown();
        } else if (event.getType() == Event.EventType.NodeDeleted) {
            System.out.println("节点删除");
            countDown.countDown();
        }
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }
    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }
}
