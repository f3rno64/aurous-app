package me.aurous.task;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import me.aurous.utils.Utils;

public class CollectionTask { //trying to fix high memory used by javafx. temps

	private class LoopTask extends TimerTask {
		@Override
		public void run() {
			
			Utils.doGC();
		}
	}

	long delay = 10000; // 

	LoopTask task = new LoopTask();

	Timer timer = new Timer("CollectionTask");

	public void start() {
		timer.cancel();
		timer = new Timer("CollectionTask");
		final Date executionDate = new Date(); // no params = now
		timer.scheduleAtFixedRate(task, executionDate, delay);
	}

}