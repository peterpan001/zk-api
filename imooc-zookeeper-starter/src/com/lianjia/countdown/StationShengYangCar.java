package com.lianjia.countdown;

import java.util.concurrent.CountDownLatch;

public class StationShengYangCar extends MonitorCentor{

    public StationShengYangCar(CountDownLatch countDown){
        super(countDown,"沈阳车控中心");
    }

    @Override
    public void check() {
        System.out.println("正在检查 [" + this.getStation() + "]...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("检查 [" + this.getStation() + "] 完毕，可以发车~");
    }
}
