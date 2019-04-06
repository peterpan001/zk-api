package com.lianjia.zk.demo06;

import org.apache.zookeeper.AsyncCallback;

import java.util.List;

public class ChildrenCallBack implements AsyncCallback.ChildrenCallback {
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
        System.out.println("节点路径为:" + path);
        for(String str : children){
            System.out.println(str);
        }
        System.out.println((String)ctx);
    }
}
