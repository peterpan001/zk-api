package com.lianjia.zk.demo02;


import org.apache.zookeeper.AsyncCallback;

/**
 * 设置回调函数
 */
public class CreateCallBack implements AsyncCallback.StringCallback {
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("创建节点:" + path);
        System.out.println((String)ctx);
    }
}
