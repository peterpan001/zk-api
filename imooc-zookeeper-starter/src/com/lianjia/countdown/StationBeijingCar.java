package com.lianjia.countdown;

import java.util.concurrent.CountDownLatch;

public class StationBeijingCar extends MonitorCentor{

    public StationBeijingCar(CountDownLatch countDown){
        super(countDown,"北京车控中心");
    }

    @Override
    public void check() {
        System.out.println("正在检查 [" + this.getStation() + "]...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("检查 [" + this.getStation() + "] 完毕，可以发车~");
    }
}
