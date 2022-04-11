package ca.mcgill.ecse420.a3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Node Class 
 * @author arneetkalra
 * @param <T>
 */
public class Node<T> {
	 Lock lock = new ReentrantLock();
     T item;
     int key;
     Node next;

     Node(T var) {
         key = var.hashCode();
         this.item = item;
     }
}
