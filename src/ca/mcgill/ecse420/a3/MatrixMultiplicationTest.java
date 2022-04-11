package ca.mcgill.ecse420.a3;

import java.util.concurrent.*;

public class MatrixMultiplicationTest {

    /**
     * Generates a double[][] array of size numRows and numCols with random values
     */
    private static double[][] generateRandomMatrix(int numRows, int numCols) {
        double[][] matrix = new double[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                matrix[row][col] = ThreadLocalRandom.current().nextInt(10);
            }
        }
        return matrix;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int matrixSize = 2000;

        // Generate the data
        double[][] randomMatrix = generateRandomMatrix(matrixSize,matrixSize);
        double[][] randomVec = generateRandomMatrix(matrixSize,1);

        // Initialize the input matrices and the result matrix
        Matrix matrix = new Matrix(matrixSize, matrixSize, randomMatrix);
        Matrix vector = new Matrix(matrixSize, 1, randomVec);
        Matrix seqResult = new Matrix(matrixSize, 1);
        Matrix parResult = new Matrix(matrixSize, 1);

        // Initialize cache thread pool
        ExecutorService executorService = Executors.newCachedThreadPool();

        // Run sequential method
        long startTime = System.currentTimeMillis();
        seqResult.multiply(matrix, vector);
        long endTime = System.currentTimeMillis();
        System.out.println("Sequential Time: " + (endTime - startTime) + "ms");

        // Run parallel task
        startTime = System.currentTimeMillis();
        Future<?> done = executorService.submit(new MatrixMultiplicationParallel.MatrixMulTask(matrix, vector, parResult, executorService));
        done.get();
        endTime = System.currentTimeMillis();
        System.out.println("Parallel Time: " + (endTime - startTime) + "ms");

        // Stop executor
        executorService.shutdown();
        executorService.awaitTermination(5000, TimeUnit.MILLISECONDS);

//        System.out.println(matrix);
//        System.out.println(vector);
//        System.out.println(seqResult);
//        System.out.println(parResult);
//        System.out.println("Is it the same result? " + Arrays.deepEquals(seqResult.data, parResult.data));
    }
}
