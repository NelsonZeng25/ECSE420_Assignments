package ca.mcgill.ecse420.a3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implement a Bounded lock based queue implementation
 * with an array instead of a linked list
 * 
 * Allows parallelism by using 2 seperate locks for head and tail
 * @author arneetkalra
 * @param <T>
 */
public class BoundedLockBasedQueueArray<T> {
	
    private ReentrantLock deqLock, enqLock;
    private Condition notEmptyCondition, notFullCondition;
    private AtomicInteger queue_size;
    private int head, tail;
    private T[] items;

   
    public BoundedLockBasedQueueArray(int capacity) {
    	
        items = (T[]) new Object[capacity];

        deqLock = new ReentrantLock();
        enqLock = new ReentrantLock();
        notEmptyCondition = deqLock.newCondition();
        notFullCondition = enqLock.newCondition();

        head = 0;
        tail = 0;
        queue_size = new AtomicInteger(0);
    }
    
    /**
     * enqueue method
     * @param item
     * @throws InterruptedException
     */
    public void enqueue(T item) throws InterruptedException {
    	
        boolean mustWakeDequeuers = false;
        enqLock.lock();

        try{
            // Wait for space in queue
            while(queue_size.get() == items.length){
                try {
                    notFullCondition.await();
                } 
                catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            
            //Add the item to the queue
            item = items[tail % items.length];
            tail++;

            if(queue_size.getAndIncrement() == 0){
                mustWakeDequeuers = true;
            }
            
        } 
        finally {
            enqLock.unlock();
        }

        if (mustWakeDequeuers){
            deqLock.lock();
            try {
                notEmptyCondition.signal();
            } 
            finally{
                deqLock.lock();
            }
        }
    }

    /**
     * dequeue method 
     * @return T
     * @throws InterruptedException
     */
    public T dequeue() throws InterruptedException {
    	
        T result;
        boolean mustWakeEnqueuers = false;
        deqLock.lock();

        try {
        	// Only continue if the queue is not empty 
            while(queue_size.get() == 0){
                try{
                    notEmptyCondition.await();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }

            // Increment head index, return prev value 
            result = items[head % items.length];
            head++;

            if(queue_size.getAndIncrement() == items.length){
                mustWakeEnqueuers = true;
            }
        }
        
        finally {
            deqLock.unlock();
        }

        if (mustWakeEnqueuers) {
            enqLock.lock();
            
            try {
                notFullCondition.signalAll();
            }
            finally {
                enqLock.unlock();
            }
        }
        return result;
    }
}
