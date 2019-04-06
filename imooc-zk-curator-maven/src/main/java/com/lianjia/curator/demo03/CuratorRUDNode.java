package com.lianjia.curator.demo03;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: panli004
 * @date: 2019/2/25
 * Description: zkCurator读取更新删除节点
 */
public class CuratorRUDNode {

    public CuratorFramework client = null;
    private static final String zkServerPath = "10.26.28.81:2181";

    public CuratorRUDNode(){
        RetryPolicy retryPolicy = new RetryNTimes(3,5000);
        client = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .namespace("workspace")
                .build();
        client.start();
    }

    /**
     * 关闭zkCurator连接
     */
    public void closeZkClient(){
        if(client!=null){
            client.close();
        }
    }

    public static void main(String[] args)throws Exception {
        CuratorRUDNode crud = new CuratorRUDNode();

        boolean isZkCuratorStart = crud.client.isStarted();
        System.out.println("客户端的状态为:" + (isZkCuratorStart?"连接中":"已关闭"));
        //节点路径
        String nodePath = "/super/lianjia";

        //更新节点信息
//        byte[] newData = "ke-lianjia-data".getBytes();
//        crud.client.setData().withVersion(0).forPath(nodePath, newData);

        //读取节点数据
//        Stat stat = new Stat();
//        byte[] data = crud.client.getData().storingStatIn(stat).forPath(nodePath);
//        System.out.println("节点:" + nodePath + "的数据为：" + new String(data));
//        System.out.println("该节点的版本号为:" + stat.getVersion());

        //删除节点
//        crud.client.delete()
//                .guaranteed() /**如果节点删除失败，那么在后端还是继续会删除，直到成功**/
//                .deletingChildrenIfNeeded()/**如果有子节点，就删除**/
//                .withVersion(0)
//                .forPath(nodePath);

        //查询子节点
//        List<String> childNodes = crud.client.getChildren().forPath(nodePath);
//        for(String path : childNodes){
//            System.out.println(path);
//        }

        //判断节点是否存在，如果不存在则为空
        Stat statExist = crud.client.checkExists().forPath(nodePath+"/a");
        System.out.println(statExist);

        Thread.sleep(3000);

        crud.closeZkClient();
        boolean isZkCuratorStart2 = crud.client.isStarted();
        System.out.println("客户端的状态为:" + (isZkCuratorStart2?"连接中":"已关闭"));


    }

}
