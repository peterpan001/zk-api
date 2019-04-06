package com.lianjia.curator.demo04;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: panli004
 * @date: 2019/2/26
 * Description: zk原生的watcher
 */
public class MyWatcher implements Watcher{

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("触发watcher，节点路径为：" + watchedEvent.getPath());
    }
}
