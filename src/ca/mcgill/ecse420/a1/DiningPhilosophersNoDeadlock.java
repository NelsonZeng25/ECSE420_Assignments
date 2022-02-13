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

        // Initialize chopstick objects
        for (int i = 0; i < numberOfPhilosophers; i++) {
            chopsticks[i] = new Object();
        }

        // Initialize philosopher runnable tasks and execute them
        for (int i = 0; i < numberOfPhilosophers; i++) {
            philosophers[i] =
                new Philosopher(i, chopsticks[i], chopsticks[(i + 1) % numberOfPhilosophers]);
            executor.execute(philosophers[i]);
        }

        // Shutdown thread pool
        executor.shutdown();
        while (!executor.isTerminated());
    }

    public static class Philosopher implements Runnable {
        int id;
        final Object leftChopstick;
        final Object rightChopstick;

        public Philosopher(int id, Object leftChopstick, Object rightChopstick) {
            this.id = id;
            this.leftChopstick = leftChopstick;
            this.rightChopstick = rightChopstick;
        }

        /**
         * This method sleeps for random amount of time to simulate the time it takes to
         * performing a task as a philosopher
         *
         * @param action - The action the philosopher does
         */
        private void doAction(String action) throws InterruptedException {
            System.out.println("Philosopher " + (id + 1) + " " + action);
            Thread.sleep(((int) (Math.random() * 100)));
        }

        /**
         * Every Philosopher starts by thinking for an amount of time before becoming hungry.
         * Once the Philosopher is hungry, it must take the global lock to stop other Philosophers
         * from taking any chopsticks. Once the lock is acquired, the Philosopher simply takes both
         * chopsticks. It then releases the global lock and eats. After eating, the Philosopher
         * releases both chopsticks
         */
        @Override public void run() {
            try {
                while (true) {
                    doAction("thinking");
                    System.out.println("Philosopher " + (id + 1) + " is hungry!");
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
