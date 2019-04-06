package com.lianjia.zk.demo03;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

public class UpdateCallBack implements AsyncCallback.StatCallback {
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        System.out.println("修改信息的节点:" + path);
        System.out.println((String)ctx);
    }
}
