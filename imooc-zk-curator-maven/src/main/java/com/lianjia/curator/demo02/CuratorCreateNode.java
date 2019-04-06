package com.lianjia.curator.demo02;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: panli004
 * @date: 2019/2/25
 * Description: 利用curator创建节点
 */
public class CuratorCreateNode {

    public CuratorFramework client = null;
    private static final String zkServerPath = "10.26.28.81:2181";

    public CuratorCreateNode(){
        //策略为重试3次，每次间隔5秒
        RetryPolicy retryPolicy = new RetryNTimes(3,5000);
        client = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .namespace("workspace")
                .build();
        client.start();
    }

    public void closeZKClient(){
        if(client!=null){
            this.client.close();
        }
    }

    public static void main(String[] args) throws Exception{
        CuratorCreateNode ccn = new CuratorCreateNode();
        boolean isZkCuratorStarted = ccn.client.isStarted();
        System.out.println("当前客户的状态：" + (isZkCuratorStarted ? "连接中" : "已关闭"));

        //创建节点
        String nodePath = "/super/lianjia";
        byte[] data = "lianjia-data".getBytes();
        ccn.client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(nodePath,data);



        //睡眠
        Thread.sleep(3000);

        ccn.closeZKClient();
        boolean isZKCuratorStarted2 = ccn.client.isStarted();
        System.out.println("当前客户的状态：" + (isZKCuratorStarted2 ? "连接中":"已关闭"));

    }

}
