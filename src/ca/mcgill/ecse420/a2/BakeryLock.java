package ca.mcgill.ecse420.a2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Bakery Lock Implementation
 * @author arneetkalra
 *
 */
public class BakeryLock implements Lock {
	
	volatile boolean flag[];
    volatile Label[] label;
    volatile int n;

    public BakeryLock(int numofThreads) {
        this.n = numofThreads;
        flag = new boolean[n];
        label = new Label[n];
        
        for (int i = 0; i < n; i++) {
            flag[i] = false;
            label[i] = new Label();
        }
    }

    @Override
    public void lock() {
        int id = ThreadID.get();
        flag[id] = true;
        
        int max = Label.max(label);
        label[id] = new Label(max + 1);
        
        //Wait if label is smaller
        while (conflict(id));
        
        System.out.println("ThreadID: " +id+ " has label " +label[id].counter);
    }
    
    private boolean conflict(int id) {
        for (int i = 0; i < label.length; i++) {
            if (i != id && flag[i] && label[id].compareTo(label[i]) > 0 && label[i].counter != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void unlock() {
        int threadID = ThreadID.get();
        flag[threadID] = false;
        System.out.println("ThreadID " +threadID+ ": :finished queuing.");
    }
    
    @Override
    public void lockInterruptibly() throws InterruptedException {
    	return;
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
    
    public int getID(){
        return ThreadID.get();
    }
}

/**
 * Label Class
 * @author arneetkalra
 *
 */
class Label implements Comparable<Label> {
    volatile int counter;
    volatile int id;

    Label() {
        counter = 0;
        id = ThreadID.get();
    }

    Label(int c) {
        counter = c;
        id = ThreadID.get();
    }

    static int max(Label[] labels) {
        int c = 0;
        for (Label label : labels) {
            c = Math.max(c, label.counter);
        }
        return c;
    }

    @Override
    public int compareTo(Label l) {
        if (this.counter < l.counter || (this.counter == l.counter && this.id < l.id)) return -1;
        else if (this.counter > l.counter) return 1;
        else return 0;
    }
}
