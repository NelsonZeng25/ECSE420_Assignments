package ca.mcgill.ecse420.a2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Filter Lock Implementation
 * @author arneetkalra
 *
 */
public class FilterLock implements Lock {

	volatile int[] level;
	volatile int[] victim;
	volatile int n;

	public FilterLock(int n) {
	    this.n = n;
	    level  = new int[n];
	    victim = new int[n];
	    for(int i = 0; i < n; i++){
	        level[i] = 0;
	    }
	}

	@Override
	public void lock() {
	    int me = ThreadID.get();
	    for (int i = 1; i < n; i++) {
	        level[me]  = i;
	        victim[i] = me;
	        
	        // Spin while conflict exists
	        while (CheckLevel(me, i) && victim[i] == me) {};
	        
	        System.out.println("ThreadID: " +me+ " is at level " +i);
	    }
	}
	
	@Override
	public void unlock() {
		int me = ThreadID.get();
	    level[me] = 0;
	    
	    System.out.println("ThreadID: "+ me + "has finished queueing");
		
	}
	
	private boolean CheckLevel(int currentId, int currentLevel) {
		for (int id = 0; id < n; id++){
	        if (id != currentId && level[id] >= currentLevel) {
	            return true;
	        }
		}
		return false;
	}
	
	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub
		return;
	}
	
	@Override
	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
