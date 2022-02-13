package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Question 2: Deadlock
 *
 * @author arneetkalra
 */
public class Deadlock {

    public static Object one = new Object();
    public static Object two = new Object();

    public static void main(String[] args) {

        int threads = 2;
        Object[] resources = new Object[threads];

        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            resources[i] = new Object();
        }

        for (int i = 0; i < threads; i++) {
            if (i == 0) {
                executor.execute(new DeadlockThread(i, one, two));
            } else {
                executor.execute(new DeadlockThread(i, two, one));
            }
        }

        executor.shutdown();
        while (!executor.isTerminated());
    }

    public static class DeadlockThread implements Runnable {
        private int threads;
        private Object resource1;
        private Object resource2;

        public DeadlockThread(int threads, Object one, Object two) {
            this.threads = threads;
            this.resource1 = one;
            this.resource2 = two;
        }

        public void run() {
            try {
                while (true) {
                    synchronized (resource1) {
                        double holdTime1 = Math.random() * 200;
                        System.out.println("Thread " + (threads + 1) + " holding resource 1 for " + holdTime1 + " ms.");
                        Thread.sleep((int) holdTime1);

                        System.out.println("Thread " + (threads + 1) + " waiting resource 2 for " + holdTime1 + " ms.");

                        synchronized (resource2) {
                            double holdTime2 = Math.random() * 200;
                            System.out.println("Thread " + (threads + 1) + " holding resource 1 & 2 for " + holdTime2 + " ms.");
                            Thread.sleep((int) holdTime2);
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
