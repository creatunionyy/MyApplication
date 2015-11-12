package com.creatunion.utils;

import com.library.core.utils.LogUtil;

/**
 * Created by ZhangZhaoCheng on 2015/9/21.
 * Description:
 */
public class SendThread extends Thread {

    private boolean isStop;
    private int count;

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 3; i++) {
            if (isStop) {
                break;
            } else {
                count++;
                //      LogUtil.d("sendThread :" + count);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        LogUtil.d("线程结束:");
    }

    public void stop(boolean isStop) {
        this.isStop = isStop;

    }
}
