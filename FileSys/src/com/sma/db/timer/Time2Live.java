package com.sma.db.timer;

import java.util.Timer;
import java.util.TimerTask;

import com.sma.db.FileStorageManager;


public class Time2Live {
    Timer timer;

    public Time2Live(int seconds, String key) {
        timer = new Timer();
        timer.schedule(new RemindTask(key), seconds*1000);
	}

    class RemindTask extends TimerTask {
    	private String key;
    	public RemindTask(String key) {
    		this.key = key;
    	}
    	
        public void run() {
            System.out.println("Time's up!");
    		FileStorageManager fileStorage = FileStorageManager.instantiate();
    		fileStorage.deleteData(key);
            timer.cancel(); //Terminate the timer thread
        }
    }
//    public static void main(String args[]) {
//        new Time2Live(5);
//        System.out.println("Task scheduled.");
//    }
}
