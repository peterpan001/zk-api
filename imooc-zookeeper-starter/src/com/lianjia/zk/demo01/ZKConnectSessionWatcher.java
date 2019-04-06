package com.lianjia.zk.demo01;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * zookeeper 恢复之前的会话连接demo演示
 */
public class ZKConnectSessionWatcher implements Watcher {

    //打印日志
    final static Logger log = LoggerFactory.getLogger(ZKConnectSessionWatcher.class);

    //服务器地址
    public static final String zkServerPath="192,168,2,131:2181";
    //sessionTimeout时间设置
    public static final Integer sessionTimeout = 5000;

    public static void main(String[] args) throws Exception{

        ZooKeeper zk = new ZooKeeper(zkServerPath,sessionTimeout,new ZKConnectSessionWatcher());

        //得到sessionId
        long sessionId = zk.getSessionId();
        //将sessionId转换成16进制
        String ssid = "0x" + Long.toHexString(sessionId);
        System.out.println(ssid);

        //得到会话密码
        byte[] sessionPassword = zk.getSessionPasswd();
        log.warn("客户端开始连接zk服务器");

        log.warn("客户端连接状态: " + zk.getState());
        //睡眠5秒
        Thread.sleep(1000);
        log.warn("客户端连接状态: " + zk.getState());
        //睡眠2秒
        Thread.sleep(200);
        //重新连接会话
        log.warn("开始会话重连");

        ZooKeeper zkSession = new ZooKeeper(zkServerPath,sessionTimeout, new ZKConnectSessionWatcher(),sessionId,sessionPassword);

        log.warn("重新连接状态zkSession{}",zkSession.getState());
        Thread.sleep(1000);
        log.warn("重新连接状态zkSession{}",zkSession.getState());
    }

    @Override
    public void process(WatchedEvent event) {
        log.warn("接收到watcher通知" + event);
    }
}


