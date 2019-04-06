package com.lianjia.curator.demo05;

import com.lianjia.utils.AclUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: panli004
 * @date: 2019/2/26
 * Description: No Description
 */
public class CuratorAcl {

    private CuratorFramework client = null;
    private static final String zkServerPath = "10.26.28.81:2181";

    public CuratorAcl(){
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
        client = CuratorFrameworkFactory
                .builder()
                .authorization("digest","lianjia1:123456".getBytes())
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .namespace("workspace")
                .build();
        client.start();
    }

    public void closeZkClient(){
        if(client!=null){
            client.close();
        }
    }

    public static void main(String[] args) throws Exception{
        CuratorAcl ca = new CuratorAcl();
        boolean isZkCuratorStarted = ca.client.isStarted();
        System.out.println("当前客户端状态:" + (isZkCuratorStarted?"连接中":"已关闭"));

        String nodePath = "/super/lianjia/acl/father/sub";

        List<ACL> acls = new ArrayList<ACL>();
        Id lianjia1 = new Id("digest", AclUtils.getDigestUserPwd("lianjia1:123456"));
        Id lianjia2 = new Id("digest",AclUtils.getDigestUserPwd("lianjia2:123456"));
        acls.add(new ACL(ZooDefs.Perms.ALL,lianjia1));
        acls.add(new ACL(ZooDefs.Perms.READ,lianjia2));
        acls.add(new ACL(ZooDefs.Perms.DELETE| ZooDefs.Perms.CREATE,lianjia2));

        //创建节点
//        byte[] data = "spiderman".getBytes();
//        ca.client.create().creatingParentsIfNeeded()
//                .withMode(CreateMode.PERSISTENT)
//                .withACL(acls,true)
//                .forPath(nodePath,data);

        //更新节点数据
//        byte[] newData = "batman".getBytes();
//        ca.client.setData().withVersion(0).forPath(nodePath,newData);

        //读取数据
//        Stat stat = new Stat();
//        byte[] data = ca.client.getData().storingStatIn(stat).forPath(nodePath);
//        System.out.println("该节点的版本号:" + stat.getVersion());
//        System.out.println("该节点的" + nodePath +"的数据为:" + new String(data));

        //删除节点数据
        ca.client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(1).forPath(nodePath);

        ca.closeZkClient();
        boolean isZkCuratorStarted1 = ca.client.isStarted();
        System.out.println("当前客户端状态:" + (isZkCuratorStarted1 ? "连接中" : "已关闭"));
    }
}
