package com.cq.sdk.utils;

import java.util.Vector;

/**
 * 定时器
 * Created by admin on 2016/10/12.
 */
public final class Timer {
    private static Vector<TimerTaskThread> timerVector=new Vector<>();
    public static int open(TimerTask timerTask, int interval){
        TimerTaskThread timerTaskThread=new TimerTaskThread(timerVector.size(),false,timerTask,interval);
        timerTaskThread.start();
        Timer.timerVector.add(timerTaskThread);
        return Timer.timerVector.size()-1;
    }
    public static int set(int id,int interval){
        Timer.timerVector.get(id).interval=interval;
        return id;
    }
    public static boolean isClose(int id){
        TimerTaskThread timerTaskThread=Timer.timerVector.get(id);
        return timerTaskThread.close;
    }
    public static void close(int id){
        TimerTaskThread timerTaskThread=Timer.timerVector.get(id);
        timerTaskThread.close=true;
    }
    public interface TimerTask {
        void execute(int id);

    }
    private static class TimerTaskThread extends Thread{
        private boolean close;
        private TimerTask timerTask;
        private int interval;
        private int id;
        public TimerTaskThread(int id,boolean close, TimerTask timerTask, int interval) {
            this.id=id;
            this.close = close;
            this.timerTask = timerTask;
            this.interval = interval;
        }

        @Override
        public void run() {
            while (!this.close){
                this.timerTask.execute(this.id);
                try {
                    if(this.interval<0){
                        break;
                    }else {
                        Thread.sleep(interval);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.close=true;
        }
    }
}
