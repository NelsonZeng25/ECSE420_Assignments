package ca.mcgill.ecse420.a3;

import java.util.concurrent.*;

public class MatrixMultiplicationTest {

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
        int matrixSize = 10;

        double[][] randomMatrix = generateRandomMatrix(matrixSize,matrixSize);
        double[][] randomVec = generateRandomMatrix(matrixSize,1);

        Matrix matrix = new Matrix(matrixSize, matrixSize, randomMatrix);
        Matrix vector = new Matrix(matrixSize, 1, randomVec);
        Matrix result = new Matrix(matrixSize, 1);

        System.out.println(matrix);
        System.out.println(vector);

        ExecutorService executorService = Executors.newCachedThreadPool();

        long startTime = System.currentTimeMillis();
        result.multiply(matrix, vector);
        long endTime = System.currentTimeMillis();
        System.out.println("Sequential Time: " + (endTime - startTime) + "ms");

        result = new Matrix(matrixSize, 1);
        startTime = System.currentTimeMillis();
        Future<?> done = executorService.submit(new MatrixMultiplicationParallel.MatrixMulTask(matrix, vector, result, executorService));
        done.get();
        endTime = System.currentTimeMillis();
        System.out.println("Parallel Time: " + (endTime - startTime) + "ms");

        executorService.shutdown();
        executorService.awaitTermination(5000, TimeUnit.MILLISECONDS);

//        System.out.println(matrix);
//        System.out.println(vector);
        System.out.println(result);
    }
}
