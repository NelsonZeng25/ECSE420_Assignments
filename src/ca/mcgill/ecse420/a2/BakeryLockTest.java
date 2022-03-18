package ca.mcgill.ecse420.a2;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Bakery Lock Test Class
 * @author arneetkalra
 *
 */
public class BakeryLockTest {
	
    public static int numOfThreads = 6;
    public static BakeryLock bakery = new BakeryLock(numOfThreads);
   
    public static boolean workingLock = true;
    public static int x = 0;
    public static ArrayList<Integer> order = new ArrayList<>();

    public static void main(String[] args) {
        ThreadID.reset();
        Thread[] bakeryLockThreads = new Thread[numOfThreads];

        ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);

        for(int i = 0; i < numOfThreads; i++){
            bakeryLockThreads[i] = new Thread(new BakeryThread());
            executor.execute(bakeryLockThreads[i]);
        }
        
        executor.shutdown();
        while (!executor.isTerminated()){}
        
        if (workingLock) System.out.println("Working Bakery Lock!");
    }

    static class BakeryThread implements Runnable{
        @Override
        public void run() {
            for(int i = 0; i < 9; i++){
                bakery.lock();
                try {
                    Thread.sleep(100);
                    order.add(x++);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bakery.unlock();
            }
            
            if(!check()) workingLock = false;
        }

        public boolean check(){
        	
            for(int i = 0; i < order.size(); i++){
                if(order.get(i) != i)
                    return false;
            }
            return true;
        }
    }
}