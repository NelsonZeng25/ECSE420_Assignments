package ca.mcgill.ecse420.a2;

/**
 * ThreadID Class
 * @author arneetkalra
 *
 */

public class ThreadID {

    private static volatile int currentID = 0;
    private static ThreadLocalID threadID = new ThreadLocalID();
    
    private static class ThreadLocalID extends ThreadLocal<Integer> {
        protected synchronized Integer initialValue(){
            return currentID++;
        }
    }
    
    public static int get(){
        return threadID.get();
    }

    public static void set(int index){
        threadID.set(index);
    }

    public static void reset() {
    	currentID = 0;
    }
}
