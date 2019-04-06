package com.lianjia.zk.demo06;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class Children2CallBack implements AsyncCallback.Children2Callback {
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        System.out.println("节点路径:" + path);
        for(String child : children){
            System.out.println(child);
        }
        System.out.println(stat);
    }
}
