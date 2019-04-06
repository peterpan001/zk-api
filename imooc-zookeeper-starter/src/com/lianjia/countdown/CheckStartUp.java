package com.lianjia.countdown;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CheckStartUp {

    private static List<MonitorCentor> monitorList;
    private static CountDownLatch countDown;

    public CheckStartUp(){}

    public static boolean checkAllStations() throws Exception {

        // 初始化3个调度站
        countDown = new CountDownLatch(3);

        // 把所有站点添加进list
        monitorList = new ArrayList<>();
        monitorList.add(new StationBeijingCar(countDown));
        monitorList.add(new StationHeNanCar(countDown));
        monitorList.add(new StationShengYangCar(countDown));

        // 使用线程池
        Executor executor = Executors.newFixedThreadPool(monitorList.size());

        for (MonitorCentor center : monitorList) {
            executor.execute(center);
        }

        // 等待线程执行完毕
        countDown.await();

        for (MonitorCentor center : monitorList) {
            if (!center.isOk()) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) throws Exception {
        boolean result = CheckStartUp.checkAllStations();
        System.out.println("监控中心针对所有危化品调度站点的检查结果为：" + result);
    }

}
