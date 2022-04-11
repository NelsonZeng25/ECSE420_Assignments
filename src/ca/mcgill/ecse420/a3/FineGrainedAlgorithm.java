package ca.mcgill.ecse420.a3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import ca.mcgill.ecse420.a3.Node;

/**
 * Fine Grained Algorithm
 * Defined in Chapter 9 of The art of multicore processing
 * 
 * Goal is to implement the contains() method which was left 
 * as an exercise for the reader
 * @author arneetkalra
 * @param <T>
 */
public class FineGrainedAlgorithm<T> {
	
    private Node<Integer> head;
   // private Lock lock = new ReentrantLock();

    // As defined in textbook
    public FineGrainedAlgorithm() {
        head = new Node<Integer>(Integer.MIN_VALUE);
        head.next = new Node<Integer>(Integer.MAX_VALUE);
    }

    /*
     * Implementation of contains() method
     * 
     */
    public boolean contains(T item) {
        // Get key value
        int key = item.hashCode();
        
        // Lock and set the node to be the precedssor 
        head.lock.lock();
        Node pred = head;

        try {
            // Set as current
            Node curr = pred.next;
            curr.lock.lock();
            
            try {
            	// Search node if it has lock for node and its predecessor
                while (curr.key < key) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }
                
                return ((curr.key == key) ? true : false);

            }
            
            finally { // unlock current node
                curr.lock.unlock();
            }
        }
        
        finally { // unlock predecessor node
            pred.lock.unlock();
        }
    }
    
    /* 
     * Method derived from the textbook
     */
    public boolean remove(T item) {
        Node pred = null, curr = null;
        int key = item.hashCode();
        head.lock.lock();
        try {
            pred = head;
            curr = pred.next;
            curr.lock.lock();
            try {
                while (curr.key < key) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }
                if (curr.key == key) {
                    pred.next = curr.next;
                    return true;
                }
                return false;
            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    /* 
     * Method derived from the textbook
     */
    public boolean add(T item) {
        int key = item.hashCode();
        head.lock.lock();
        Node predNode = head;
        try {
            Node currentNode = predNode.next;
            currentNode.lock.lock();
            try {
                while (currentNode.key < key) {
                    predNode.lock.unlock();
                    predNode = currentNode;
                    currentNode = currentNode.next;
                    currentNode.lock.lock();
                }
                
                if (currentNode.key == key) {
                	return false;
                }
                    
                Node node = new Node(item);
                node.next = currentNode;
                predNode.next = node;
                return true;
                
            } finally {
                currentNode.lock.unlock();
            }
        } finally {
            predNode.lock.unlock();
        }
    }
}