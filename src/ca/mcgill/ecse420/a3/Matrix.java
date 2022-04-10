package ca.mcgill.ecse420.a3;

public class Matrix {
    double[][] data;
    int rowDisplace, colDisplace;
    int n, m;

    public Matrix(int n, int m) {
        this.n = n;
        this.m = m;
        rowDisplace = colDisplace = 0;
        data = new double[n][m];
    }

    public Matrix(int n, int m, double[][] data) {
        this.n = n;
        this.m = m;
        rowDisplace = colDisplace = 0;
        this.data = data;
    }

    private Matrix(double[][] matrix, int x, int y, int n, int m){
        data = matrix;
        rowDisplace = x;
        colDisplace = y;
        this.n = n;
        this.m = m;
    }

    public double get(int row, int col){
        return data[row + rowDisplace][col + colDisplace];
    }

    public void set(int row, int col, double value){
        data[row + rowDisplace][col + colDisplace] = value;
    }

    public Matrix split(int i, int j) {
        int newDim = n / 2;
        if (n == m) {
            // Handle the square matrix case
            return new Matrix(data, rowDisplace + (i * newDim), colDisplace + (j * newDim), newDim, newDim);
        } else {
            // Handle the vector case
            return new Matrix(data, rowDisplace + (i * newDim), 0, newDim, m);
        }

    }

    public void add(Matrix matrixA, Matrix matrixB) {
        for (int i = 0; i < matrixA.n; i++) {
            for (int j = 0; j < matrixA.m; j++) {
                this.set(i, j, matrixA.get(i, j) + matrixB.get(i, j));
            }
        }
    }

    public void multiply(Matrix matrixA, Matrix matrixB) {
        for (int i = 0; i < matrixA.n; i++) {
            for (int j = 0; j < matrixB.m; j++) {
                double result = 0;
                for (int u = 0; u < matrixA.m; u++) {
                    result += matrixA.get(i, u) * matrixB.get(u, j);
                }
                this.set(i, j, result);
            }
        }
    }

    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < n; i++) {
            output.append("[ ");
            for (int j = 0; j < m; j++) {
                output.append(this.get(i, j)).append(" ");
            }
            output.append("]\n");
        }

        return output.toString();
    }

}
