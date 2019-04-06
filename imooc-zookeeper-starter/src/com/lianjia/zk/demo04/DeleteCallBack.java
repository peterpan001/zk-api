package com.lianjia.zk.demo04;

import org.apache.zookeeper.AsyncCallback.VoidCallback;

public class DeleteCallBack implements VoidCallback {
    @Override
    public void processResult(int rc, String path, Object ctx) {
        System.out.println("删除节点路径:" + path);
        System.out.println((String)ctx);
    }
}
