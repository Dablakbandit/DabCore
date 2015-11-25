package me.dablakbandit.dabcore.block;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue {

	private static Queue instance = new Queue();

	public static Queue getInstance(){
		return instance;
	}

	private AtomicInteger time_waiting = new AtomicInteger(2);
	private AtomicInteger time_current = new AtomicInteger(0);
	private ArrayDeque<Runnable> runnables = new ArrayDeque<Runnable>();

	private long last;
	private long last2;

	public Queue(){
		TaskManager.getInstance().repeat(new Runnable(){
			@Override
			public void run(){
				long free = 50 + Math.min(50 + last - (last = System.currentTimeMillis()), last2 - System.currentTimeMillis());
				time_current.incrementAndGet();
				do{
					if(isWaiting())return;
					AsyncBlock current = AsyncChunk.getInstance().next();
					if(current == null){
						time_waiting.set(Math.max(time_waiting.get(), time_current.get() - 2));
						tasks();
						return;
					}
				}while((last2 = System.currentTimeMillis()) - last < free);
				time_waiting.set(time_current.get() - 1);
			}
		}, 1);
	}

	public boolean isWaiting(){
		return time_waiting.get() >= time_current.get();
	}

	public boolean isDone(){
		return (time_waiting.get() + 1) < time_current.get();
	}

	private void setWaiting(){
		time_waiting.set(time_current.get() + 1);
	}

	public boolean addTask(Runnable whenDone){
		if(isDone()){
			tasks();
			if(whenDone != null)whenDone.run();
			return true;
		}
		if(whenDone!=null)runnables.add(whenDone);
		return false;
	}

	private boolean tasks(){
		if(runnables.size() == 0)return false;
		ArrayDeque<Runnable> tmp = runnables.clone();
		runnables.clear();
		for(Runnable runnable : tmp){
			runnable.run();
		}
		return true;
	}

	public boolean setBlock(String world, int x, int y, int z, short id, byte data) {
		setWaiting();
		return AsyncChunk.getInstance().setBlock(world, x, y, z, id, data);
	}
	
	public boolean setBlock(String world, int x, int y, int z, short id) {
		setWaiting();
		return AsyncChunk.getInstance().setBlock(world, x, y, z, id, (byte) 0);
	}
}
