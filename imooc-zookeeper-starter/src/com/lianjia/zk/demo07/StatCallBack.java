package com.lianjia.zk.demo07;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

public class StatCallBack implements AsyncCallback.StatCallback {
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        System.out.println("节点路径:" + path);
        System.out.println("节点版本号:" + stat.getVersion());
        System.out.println((String)ctx);
    }
}
