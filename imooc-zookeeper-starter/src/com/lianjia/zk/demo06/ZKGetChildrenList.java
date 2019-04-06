package com.lianjia.zk.demo06;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * zk监听子节点列表demo演示
 */
public class ZKGetChildrenList implements Watcher {

    private static final String connectionPath = "192.168.2.131:2181";
    private static final Integer sessionTimeout = 5000;
    private ZooKeeper zooKeeper = null;
    private ZKGetChildrenList(){}
    private ZKGetChildrenList(String connectionPath){
        try {
            zooKeeper = new ZooKeeper(connectionPath, sessionTimeout, new ZKGetChildrenList());
        } catch (Exception e) {
            e.printStackTrace();
            if(zooKeeper!=null){
                try {
                    zooKeeper.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static CountDownLatch countDown = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{

        ZKGetChildrenList zkServer = new ZKGetChildrenList(connectionPath);

        //同步调用
//        List<String> listPath = zkServer.getZooKeeper().getChildren("/testnode", true);
//        for(String path : listPath){
//            System.out.println(path);
//        }

        //异步调用
        String ctx = "{'callBack':'ChildrenCallBack'}";
//        zkServer.getZooKeeper().getChildren("/testnode", true, new ChildrenCallBack(),ctx);

        zkServer.getZooKeeper().getChildren("/testnode", true, new Children2CallBack(), ctx);
        countDown.await();
    }


    @Override
    public void process(WatchedEvent event) {
        try{
            if(event.getType() == Event.EventType.NodeChildrenChanged){
                System.out.println("NodeChildrenChanged");
                ZKGetChildrenList zkServer = new ZKGetChildrenList(connectionPath);
                List<String> paths = zkServer.getZooKeeper().getChildren("/testnode", false);
                for(String path : paths){
                    System.out.println(path);
                }
                countDown.countDown();
            }else if(event.getType() == Event.EventType.NodeCreated){
                System.out.println("NodeCreated");
            }else if(event.getType() == Event.EventType.NodeDataChanged){
                System.out.println("NodeDataChanged");
            }else if(event.getType() == Event.EventType.NodeDeleted){
                System.out.println("NodeDeleted");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }
    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }
}
