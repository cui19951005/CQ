package com.cq.sdk.utils;

import java.util.Vector;

/**
 * 定时器
 * Created by admin on 2016/10/12.
 */
public class Timer {
    private static Vector<TimerTaskThread> timerVector=new Vector<>();
    public static int open(TimerTask timerTask, int interval){
        TimerTaskThread timerTaskThread=new TimerTaskThread(false,timerTask,interval);
        timerTaskThread.start();
        Timer.timerVector.add(timerTaskThread);
        return Timer.timerVector.size()-1;
    }
    public static void close(int id){
        TimerTaskThread timerTaskThread=Timer.timerVector.get(id);
        timerTaskThread.close=true;
    }
    public interface TimerTask {
        void execute();
    }
    private static class TimerTaskThread extends Thread{
        private boolean close;
        private TimerTask timerTask;
        private int interval;

        public TimerTaskThread(boolean close, TimerTask timerTask, int interval) {
            this.close = close;
            this.timerTask = timerTask;
            this.interval = interval;
        }

        @Override
        public void run() {
            while (!this.close){
                this.timerTask.execute();
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
