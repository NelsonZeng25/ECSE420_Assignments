package ca.mcgill.ecse420.a3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MatrixMultiplicationParallel {

    public static final int THRESHOLD = 500;

    public static class MatrixAddTask implements Runnable {
        Matrix lhs, rhs, sum;

        ExecutorService executorService;

        public MatrixAddTask(Matrix lhs, Matrix rhs, Matrix sum, ExecutorService executorService) {
            this.lhs = lhs;
            this.rhs = rhs;
            this.sum = sum;
            this.executorService = executorService;
        }

        @Override public void run() {
            int n = lhs.n;
            if (n <= THRESHOLD) {
                sum.add(lhs, rhs);
            } else {
                List<Future<?>> tasks = new ArrayList<>(2);
                for (int i = 0; i < 2; i++) {
                    tasks.add(executorService.submit(new MatrixAddTask(lhs.split(i, 0), rhs.split(i, 0), sum.split(i, 0), executorService)));
                }

                tasks.forEach(task -> {
                    try {
                        task.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public static class MatrixMulTask implements Runnable {
        Matrix lhs, rhs, product;

        ExecutorService executorService;

        public MatrixMulTask(Matrix lhs, Matrix rhs, Matrix product, ExecutorService executorService) {
            this.lhs = lhs;
            this.rhs = rhs;
            this.product = product;
            this.executorService = executorService;
        }

        @Override public void run() {
            int n = lhs.n;
            if (n <= THRESHOLD) {
                product.multiply(lhs, rhs);
            } else {
                List<Future<?>> tasks = new ArrayList<>(4);
                Matrix[] term = new Matrix[] { new Matrix(n, rhs.m), new Matrix(n, rhs.m) };
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        tasks.add(executorService.submit(new MatrixMulTask(lhs.split(j, i), rhs.split(i, 0), term[i].split(j, 0), executorService)));
                    }
                }

                tasks.forEach(task -> {
                    try {
                        task.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });

                try {
                    (executorService.submit(new MatrixAddTask(term[0], term[1], product, executorService))).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
