package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatrixMultiplication {

    private static int NUMBER_THREADS = 7;
    private static final int MATRIX_SIZE = 2000;

    public static void main(String[] args) {
    	// To run question 1.4 vs question 1.5, uncomment the respective piece of code in this main method
    	
        /*
        // Question 1.4
        double[][] a = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
        double[][] b = generateRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);

		// Setting a max of 10 threads
        for (int i = 1 ; i <= 10 ; i++) {
          NUMBER_THREADS = i;
          measureExecutionTime(a, b, "parallel");
        }
        */

        // Question 1.5:
        int[] matrixSizes = new int[] {100, 200, 500, 1000, 2000, 3000, 4000};
        for (int size : matrixSizes) {
            double[][] matrixA = generateRandomMatrix(size, size);
            double[][] matrixB = generateRandomMatrix(size, size);
            measureExecutionTime(matrixA, matrixB, "parallel");
        }
    }

    /**
     * Returns the result of a sequential matrix multiplication
     * The two matrices are randomly generated
     *
     * @param a is the first matrix
     * @param b is the second matrix
     * @return the result of the multiplication
     */
    public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {

        double tempResult = 0;
        int rows = b.length;
        int cols = a[0].length;

        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < cols; k++) {
                    tempResult += a[i][k] * b[k][j];
                }

                result[i][j] = tempResult;
                tempResult = 0;
            }
        }

        return result;
    }

    /**
     * Returns the result of a concurrent matrix multiplication
     * The two matrices are randomly generated
     *
     * @param a is the first matrix
     * @param b is the second matrix
     * @return the result of the multiplication
     */
    public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) {

        double[][] result = new double[b.length][a[0].length];
        
        // Starts a new thread pool 
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_THREADS);
        
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                executor.execute((new MatrixMultiplicationParallel(a, b, result, i, j, a.length)));
            }
        }

        executor.shutdown();

        // Ensure executor terminates
        while (!executor.isTerminated()) {
        }

        return result;
    }

    /**
     * Runnable class used for parallel matrix multiplication.
     *
     * @author arneetkalra
     */
    public static class MatrixMultiplicationParallel implements Runnable {

        private int row;
        private int column;
        private int size;
        private double result;
        private double[][] matrixA;
        private double[][] matrixB;
        private double[][] resultMatrix;

        public MatrixMultiplicationParallel(double[][] matrixA, double[][] matrixB,
            double[][] resultMatrix, int row, int column, int size) {
            this.row = row;
            this.column = column;
            this.size = size;
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.resultMatrix = resultMatrix;
        }

        public void run() {

            result = 0;
            for (int k = 0; k < this.size; k++) { //iteration
                result += matrixA[row][k] * matrixB[k][column];
            }
            resultMatrix[row][column] = result;
        }
    }

    /**
     * Populates a matrix of given size with randomly generated integers between 0-10.
     *
     * @param numRows number of rows
     * @param numCols number of cols
     * @return matrix
     */
    private static double[][] generateRandomMatrix(int numRows, int numCols) {
        double matrix[][] = new double[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                matrix[row][col] = (double) ((int) (Math.random() * 10.0));
            }
        }
        return matrix;
    }

    /**
     * Measures the execution time of multiplying matricies.
     *
     * @param matrixA first matrix to multiply
     * @param matrixB other matrix to multiply
     * @param type    can take in 3 values:
     *                "parallel" for only parallel execution,
     *                "sequential" for only sequential execution
     *                "both" for both types of execution
     */
    public static void measureExecutionTime(double[][] matrixA, double[][] matrixB, String type) {
        if (type.equals("both") || type.equals("sequential")) {
            long sequentialStart = System.currentTimeMillis();
            sequentialMultiplyMatrix(matrixA, matrixB);
            long sequentialStop = System.currentTimeMillis();
            System.out.println(sequentialStop - sequentialStart);
        }

        if (type.equals("both") || type.equals("parallel")) {
            long parallelStart = System.currentTimeMillis();
            parallelMultiplyMatrix(matrixA, matrixB);
            long parallelStop = System.currentTimeMillis();
            System.out.println(parallelStop - parallelStart);
        }
    }
}


