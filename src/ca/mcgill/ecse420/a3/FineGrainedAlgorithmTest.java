package ca.mcgill.ecse420.a3;
import java.util.concurrent.ThreadLocalRandom;

public class FineGrainedAlgorithmTest extends Thread{
	
	// Range of values
    public static final int MIN = 0;
    public static final int MAX = 5;
    
    //Generate a random value
    int id;
    FineGrainedAlgorithm<Integer> fineGrainedList;
    Integer item;
    Integer check;

    FineGrainedAlgorithmTest(int id, FineGrainedAlgorithm<Integer> fineGrainedList){
        this.id = id++;
        this.fineGrainedList = fineGrainedList;
        item = ThreadLocalRandom.current().nextInt(MIN, MAX+1);
        check = ThreadLocalRandom.current().nextInt(MIN, MAX+1);
    }

    @Override
    public void run() {
    	// Check add()
        if (fineGrainedList.add(item)) {
            System.out.println("Thread " + id + " added " + item);
        } 
        else {
            System.out.println("--- Thread " + id + " could not add " + item);

        }

        // Checks a random item in the list
        if (fineGrainedList.contains(check)) {
            System.out.println("Thread " + id + " found " + check);
        }
        else {
            System.out.println("--- Thread " + id + " could not find " + check);
        }

        if (fineGrainedList.remove(item)) {
            System.out.println("Thread " + id + " removed " + item);
        } 
        else {
            System.out.println("--- Thread " + id + " could not remove " + item);

        }

        if (fineGrainedList.contains(check)) {
            System.out.println("Thread " + id + " found " + check);
        } 
        else {
            System.out.println("--- Thread " + id + " could not find " + check);
        }
        
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
    	
        final int NUM_THREADS = 8;

        FineGrainedAlgorithm<Integer> fineGrainedList = new FineGrainedAlgorithm();
        FineGrainedAlgorithmTest[] fineGrainedTestArr = new FineGrainedAlgorithmTest[NUM_THREADS];

        System.out.println("Initializing Threads...");

        for (int i = 0; i < NUM_THREADS; i++) {
            fineGrainedTestArr[i] = new FineGrainedAlgorithmTest(i, fineGrainedList);
        }
        
        System.out.println("... Threads Initalized");

        System.out.println("--- Populating List with Add() ---");
        for (int i = 0; i < NUM_THREADS; i++) {
            fineGrainedTestArr[i].start();
        }
    }
}
