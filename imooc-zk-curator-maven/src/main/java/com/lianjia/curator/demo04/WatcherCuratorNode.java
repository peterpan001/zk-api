package com.lianjia.curator.demo04;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.RetryNTimes;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: panli004
 * @date: 2019/2/25
 * Description: zk的监听事件
 */
public class WatcherCuratorNode {

    public CuratorFramework client = null;
    private static final String zkServerPath = "10.26.28.81:2181";

    public WatcherCuratorNode(){
        RetryPolicy retryPolicy = new RetryNTimes(3,5000);

        client = CuratorFrameworkFactory.builder()
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

    public static void main(String[] args)throws Exception {
        WatcherCuratorNode wcn = new WatcherCuratorNode();
        boolean isZKCuratorStarted = wcn.client.isStarted();
        System.out.println("当前客户状态:" + (isZKCuratorStarted?"连接中":"已关闭"));

        final String nodPath ="/super/lianjia";

        // watcher 事件  当使用usingWatcher的时候，监听只会触发一次，监听完毕后就销毁
//        wcn.client.getData().usingWatcher(new MyWatcher()).forPath(nodPath);
//        wcn.client.getData().usingWatcher(new MyCuratorWatcher()).forPath(nodPath);


        // 为节点添加watcher
        // NodeCache: 监听数据节点的变更，会触发事件
//        final NodeCache nodeCache = new NodeCache(wcn.client,nodPath);
        // buildInitial : 初始化的时候获取node的值并且缓存,默认值为false，不会获取node值进行缓存
//        nodeCache.start(true);
//        if(nodeCache.getCurrentData()!=null){
//            System.out.println("初始化节点数据为:" + new String(nodeCache.getCurrentData().getData()));
//        }else{
//            System.out.println("节点初始化数据为空...");
//        }
//        nodeCache.getListenable().addListener(new NodeCacheListener() {
//            @Override
//            public void nodeChanged() throws Exception {
//                if(nodeCache.getCurrentData() == null){
//                    System.out.println("空");
//                    return;
//                }
//                String data = new String(nodeCache.getCurrentData().getData());
//                System.out.println("节点路径:" + nodeCache.getCurrentData().getPath() + "数据:" + data);
//            }
//        });

        //为子节点添加watcher,监听数据节点的增删改，会触发事件
        PathChildrenCache childrenCache = new PathChildrenCache(wcn.client,nodPath,true);
        /**
         * StartMode: 初始化方式
         * POST_INITIALIZED_EVENT：异步初始化，初始化之后会触发事件
         * NORMAL：异步初始化
         * BUILD_INITIAL_CACHE：同步初始化
         */
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        List<ChildData> childDataList = childrenCache.getCurrentData();
        System.out.println("当前数据节点的子节点数据列表:");
        for(ChildData childData : childDataList){
            String cd = new String(childData.getData());
            System.out.println(cd);
        }
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {

                if(pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)){
                    System.out.println("子节点初始化ok");
                }else if(pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)){
                    String path = pathChildrenCacheEvent.getData().getPath();
                    if(path.equals(ADD_PATH)){
                        System.out.println("子节点被添加");
                    }
                }else if(pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)){
                    String path = pathChildrenCacheEvent.getData().getPath();
                    if(path.equals(DELETE_PATH)){
                        System.out.println("子节点被删除");
                    }
                }else if(pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)){
                    String path = pathChildrenCacheEvent.getData().getPath();
                    if(path.equals(UPDATE_PATH)){
                        System.out.println("子节点被更新");
                    }
                }
            }
        });
        Thread.sleep(100000);

        wcn.closeZkClient();
        boolean isZKCuratorStarted1 = wcn.client.isStarted();
        System.out.println("当前客户状态:" + (isZKCuratorStarted1?"连接中":"已关闭"));

    }

    private static final String ADD_PATH="/super/lianjia/add";
    private static final String UPDATE_PATH="/super/lianjia/update";
    private static final String DELETE_PATH="/super/lianjia/del";
}
