package ca.mcgill.ecse420.a3;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Bounded Lock-free algorithm
 * 
 * @author arneetkalra
 *
 * @param <T>
 */
public class BoundedLockFreeQueueArray<T> {
    private T[] items;

    private AtomicInteger head;
    private AtomicInteger tail;
    private AtomicInteger capacity;
    private AtomicInteger tailcommit;

    public BoundedLockFreeQueueArray(int capacity){
        items = (T[]) new Object[capacity];
        this.capacity = new AtomicInteger(capacity);
    }

    public void enqueue(T item) {
        int cap = capacity.get();
        
        while (cap <= 0 || !capacity.compareAndSet(cap, cap - 1)) {
            cap = capacity.get();
        }
        
        int i = this.tail.getAndIncrement();
        this.items[i % this.items.length] = item;

        while(this.tailcommit.compareAndSet(i,i++)) {};
    }

    public T dequeue(){
        int i = this.head.getAndIncrement();
        
        while (i >= this.tailcommit.get()) {};

        T item = items[i % this.items.length];
        capacity.incrementAndGet();

        return item;
    }
}
