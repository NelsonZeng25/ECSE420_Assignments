package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersNoDeadlock {

    public static ReentrantLock global = new ReentrantLock();

    public static void main(String[] args) {
        int numberOfPhilosophers = 5;
        Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
        Object[] chopsticks = new Object[numberOfPhilosophers];

        ExecutorService executor = Executors.newFixedThreadPool(numberOfPhilosophers);

        for (int i = 0; i < numberOfPhilosophers; i++) {
            chopsticks[i] = new Object();
        }

        for (int i = 0; i < numberOfPhilosophers; i++) {
            philosophers[i] = new Philosopher(i, chopsticks[i], chopsticks[(i + 1) % numberOfPhilosophers]);
            executor.execute(philosophers[i]);
        }
        executor.shutdown();
        while (!executor.isTerminated());
    }

    public static class Philosopher implements Runnable {
        int i;
        final Object leftChopstick;
        final Object rightChopstick;

        public Philosopher(int i, Object leftChopstick, Object rightChopstick) {
            this.i = i;
            this.leftChopstick = leftChopstick;
            this.rightChopstick = rightChopstick;
        }

        private void doAction(String action) throws InterruptedException {
            System.out.println("Philosopher " + (i+1) + " " + action);
            Thread.sleep(((int) (Math.random() * 100)));
        }

        @Override public void run() {
            try {
                while (true) {
                    doAction("thinking");
                    System.out.println("Philosopher " + (i+1) + " is hungry!");
                    global.lock();
                    synchronized (leftChopstick) {
                        doAction("picked up left chopstick");
                        synchronized (rightChopstick) {
                            doAction("picked up right chopstick");

                            global.unlock();

                            doAction("eating");
                        }
                        doAction("put down right chopstick");
                    }
                    doAction("put down left chopstick");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


    }

}
