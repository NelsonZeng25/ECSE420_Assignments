package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiningPhilosophersNoStarvation {
    public static int numberOfPhilosophers = 5;

    public static void main(String[] args) {
        Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
        Object[] chopsticks = new Object[numberOfPhilosophers];

        ExecutorService executor = Executors.newFixedThreadPool(numberOfPhilosophers);

        // Initialize chopstick objects
        for (int i = 0; i < numberOfPhilosophers; i++) {
            chopsticks[i] = new Object();
        }

        // Initialize philosopher runnable tasks and execute them
        for (int i = 0; i < numberOfPhilosophers; i++) {
            if (i == philosophers.length - 1) {
                // The last philosopher picks up the right fork first
                philosophers[i] = new Philosopher(i, chopsticks[(i + 1) % numberOfPhilosophers], chopsticks[i]);
            } else {
                philosophers[i] = new Philosopher(i, chopsticks[i], chopsticks[(i + 1) % numberOfPhilosophers]);
            }
            executor.execute(philosophers[i]);
        }

        // Shutdown thread pool
        executor.shutdown();
        while (!executor.isTerminated());
    }

    public static class Philosopher implements Runnable {
        int id;
        final Object chopstick1;
        final Object chopstick2;

        public Philosopher(int id, Object chopstick1, Object chopstick2) {
            this.id = id;
            this.chopstick1 = chopstick1;
            this.chopstick2 = chopstick2;
        }

        /**
         * This method sleeps for random amount of time to simulate the time it takes to
         * performing a task as a philosopher
         * @param action - The action the philosopher does
         */
        private void doAction(String action) throws InterruptedException {
            System.out.println("Philosopher " + (id +1) + " " + action);
            Thread.sleep(((int) (Math.random() * 100)));
        }

        /**
         * This is the same method as the first DiningPhilosopher but since the last Philosopher
         * picks the other chopstick first, it breaks symmetry which stops the deadlock loop
         */
        @Override public void run() {
            try {
                while (true) {
                    doAction("thinking");
                    System.out.println("Philosopher " + (id +1) + " is hungry!");
                    synchronized (chopstick1) {
                        doAction("picked up " + (id == numberOfPhilosophers - 1 ? "right" : "left") + " chopstick");
                        synchronized (chopstick2) {
                            doAction("picked up " + (id == numberOfPhilosophers - 1 ? "left" : "right") + " chopstick");
                            doAction("eating");
                        }
                        doAction("put down " + (id == numberOfPhilosophers - 1 ? "left" : "right") + " chopstick");
                    }
                    doAction("put down " + (id == numberOfPhilosophers - 1 ? "right" : "left") + " chopstick");

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


    }

}
