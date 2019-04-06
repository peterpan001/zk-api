package com.lianjia.curator.demo04;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: panli004
 * @date: 2019/2/26
 * Description: 使用curator的zk watcher
 */
public class MyCuratorWatcher implements CuratorWatcher {

    @Override
    public void process(WatchedEvent watchedEvent) throws Exception {
        System.out.println("触发CuratorWacher,节点的路径为:" + watchedEvent.getPath());
    }
}
