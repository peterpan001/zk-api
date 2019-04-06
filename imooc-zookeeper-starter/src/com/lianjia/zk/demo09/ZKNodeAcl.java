package com.lianjia.zk.demo09;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * acl -自定义用户权限
 */
public class ZKNodeAcl implements Watcher {

    private ZooKeeper zooKeeper = null;

    private static final String connectionPath = "192.168.2.131:2181";
    private static final Integer sessionTimeout = 5000;
    public ZKNodeAcl(){}
    public ZKNodeAcl(String connectionPath){
        try {
            zooKeeper = new ZooKeeper(connectionPath, sessionTimeout, new ZKNodeAcl());
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

    public void createZKNode(String path, byte[] data, List<ACL> acls) {
        String result = "";
        try {
            /**
             * 同步或者异步创建节点，都不支持子节点的递归创建，异步有一个callback函数
             * 参数：
             * path：创建的路径
             * data：存储的数据的byte[]
             * acl：控制权限策略
             * 			Ids.OPEN_ACL_UNSAFE --> world:anyone:cdrwa
             * 			CREATOR_ALL_ACL --> auth:user:password:cdrwa
             * createMode：节点类型, 是一个枚举
             * 			PERSISTENT：持久节点
             * 			PERSISTENT_SEQUENTIAL：持久顺序节点
             * 			EPHEMERAL：临时节点
             * 			EPHEMERAL_SEQUENTIAL：临时顺序节点
             */
            result = zooKeeper.create(path, data, acls, CreateMode.PERSISTENT);
            System.out.println("创建节点：\t" + result + "\t成功...");
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        ZKNodeAcl zkServer = new ZKNodeAcl(connectionPath);
        List<ACL> acls = new ArrayList<ACL>();
        //自定义用户认证访问
//		Id lianjia1 = new Id("digest", AclUtils.getDigestUserPwd("lianjia1:123456"));
//		Id lianjia2 = new Id("digest", AclUtils.getDigestUserPwd("lianjia2:123456"));
//		acls.add(new ACL(ZooDefs.Perms.ALL, lianjia1));
//		acls.add(new ACL(ZooDefs.Perms.READ, lianjia2));
//		acls.add(new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.CREATE, lianjia2));
//		zkServer.createZKNode("/lianjia/testdigest", "testdigest".getBytes(), acls);


        // 注册过的用户必须通过addAuthInfo才能操作节点，参考命令行 addauth
		zkServer.getZooKeeper().addAuthInfo("digest", "lianjia1:123456".getBytes());

//		zkServer.createZKNode("/lianjia/testdigest/childtest", "childtest".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL);


		Stat stat = new Stat();
		byte[] data = zkServer.getZooKeeper().getData("/lianjia/testdigest", false, stat);
		System.out.println(new String(data));
		zkServer.getZooKeeper().setData("/lianjia/testdigest", "now".getBytes(), 0);


    }

    @Override
    public void process(WatchedEvent event) {

    }


    public void setZooKeeper(ZooKeeper zooKeeper){
        this.zooKeeper = zooKeeper;
    }
    public ZooKeeper getZooKeeper(){
        return zooKeeper;
    }

}
