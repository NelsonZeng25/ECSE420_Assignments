package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiningPhilosophersNoStarvation {
    public static int numberOfPhilosophers = 5;

    public static void main(String[] args) {
        Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
        Object[] chopsticks = new Object[numberOfPhilosophers];

        ExecutorService executor = Executors.newFixedThreadPool(numberOfPhilosophers);

        for (int i = 0; i < numberOfPhilosophers; i++) {
            chopsticks[i] = new Object();
        }

        for (int i = 0; i < numberOfPhilosophers; i++) {
            if (i == philosophers.length - 1) {
                // The last philosopher picks up the right fork first
                philosophers[i] = new Philosopher(i, chopsticks[(i + 1) % numberOfPhilosophers], chopsticks[i]);
            } else {
                philosophers[i] = new Philosopher(i, chopsticks[i], chopsticks[(i + 1) % numberOfPhilosophers]);
            }
            executor.execute(philosophers[i]);
        }
        executor.shutdown();
        while (!executor.isTerminated());
    }

    public static class Philosopher implements Runnable {
        int i;
        final Object chopstick1;
        final Object chopstick2;

        public Philosopher(int i, Object chopstick1, Object chopstick2) {
            this.i = i;
            this.chopstick1 = chopstick1;
            this.chopstick2 = chopstick2;
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
                    synchronized (chopstick1) {
                        doAction("picked up " + (i == numberOfPhilosophers - 1 ? "right" : "left") + " chopstick");
                        synchronized (chopstick2) {
                            doAction("picked up " + (i == numberOfPhilosophers - 1 ? "left" : "right") + " chopstick");
                            doAction("eating");
                        }
                        doAction("put down " + (i == numberOfPhilosophers - 1 ? "left" : "right") + " chopstick");
                    }
                    doAction("put down " + (i == numberOfPhilosophers - 1 ? "right" : "left") + " chopstick");

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


    }

}
