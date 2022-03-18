package ca.mcgill.ecse420.a2;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;

/**
 * Class to test mutual exclusion of Filter Lock
 * @author arneetkalra
 *
 */
public class FilterLockTest {
	
	// Global Vars
    private final static int NUMOFTHREADS = 8;
    private final static int COUNT = 100;
    private static int PER_THREAD = COUNT / NUMOFTHREADS;
    
    public static boolean workingLock = true;
    public static int x = 0;
    public static ArrayList<Integer> order = new ArrayList<>();

    public static FilterLock lock = new FilterLock(NUMOFTHREADS);

    public static void main(String[] args){

        Thread[] filterLockThreads = new Thread[NUMOFTHREADS];
        
        // Begin a new thread pool
        ExecutorService executor = Executors.newFixedThreadPool(NUMOFTHREADS);

        for(int i = 0; i < NUMOFTHREADS; i++){
            filterLockThreads[i] = new Thread(new FilterLockThread());
            executor.execute(filterLockThreads[i]);
        }
        
        executor.shutdown();

        while (!executor.isTerminated()){}
        
        if (workingLock) System.out.println("Working Filter Lock!");
        else System.out.println("Filter Lock Error");
    }
    
    // Runnable class for filter lock thread
    public static class FilterLockThread implements Runnable {
    	
        @Override
        public void run() {
            for( int i = 0; i < PER_THREAD; i++){
                lock.lock();
                try{
                    Thread.sleep(15);
                    order.add(x++);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
                
                if(!check()) workingLock = false;
            }
        }
        
        public boolean check(){
            for (int i = 0; i < order.size(); i++){
                if(order.get(i) != i) return false;
            }
            return true;
        }
    }
}

