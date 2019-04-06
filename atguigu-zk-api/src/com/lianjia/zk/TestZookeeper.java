package com.lianjia.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: panli004
 * @date: 2019/3/17
 * Description: No Description
 */
public class TestZookeeper {

    /**
     * 连接zkServer
     */
    private String connectString = "lianjia02:2181,lianjia03:2181,lianjia04:2181";

    /**
     * 设置超时时间
     */
    private int sessionTimeout = 2000;

    private ZooKeeper zkClient;
    /**
     * 创建zk客户端
     * @throws Exception
     */
    @Before
    public void initClient()throws Exception{
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getType()+"\t"+event.getPath());

//                try {
//                    List<String> childs = zkClient.getChildren("/",true);
//                    System.out.println("-----------start------------");
//                    for(String child : childs){
//                        System.out.println(child);
//                    }
//                    System.out.println("-----------end--------------");
//                } catch (KeeperException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                try {
                    Stat stat = zkClient.exists("/lianjia02",true);
                    System.out.println(stat==null?"not exist":"exist");
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    /**
     * 创建子节点
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void create() throws KeeperException, InterruptedException {
        String path = zkClient.create("/lianjia01","lianjia-data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(path);
    }

    /**
     * 获取子节点
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getChildNode() throws KeeperException, InterruptedException {
        List<String> childrens = zkClient.getChildren("/",true);
        for(String child : childrens){
            System.out.println(child);
        }
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 判断节点是否存在
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void isExist() throws KeeperException, InterruptedException {
        Stat stat = zkClient.exists("/lianjia02",false);
        System.out.println(stat==null?"not exist":"exist");
        Thread.sleep(Long.MAX_VALUE);
    }

}
