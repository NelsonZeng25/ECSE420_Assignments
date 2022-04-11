package ca.mcgill.ecse420.a3;

/**
 * Matrix Class taken from the textbook with some changes
 */
public class Matrix {
    double[][] data;
    int rowDisplace, colDisplace;
    int n, m;

    /**
     * This constructor takes n x m to help initialize the vector
     * @param n - rows
     * @param m - cols
     */
    public Matrix(int n, int m) {
        this.n = n;
        this.m = m;
        rowDisplace = colDisplace = 0;
        data = new double[n][m];
    }

    /**
     * Same constructor as above but you can set the data directly
     * @param n - rows
     * @param m - cols
     * @param data - data of matrix
     */
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

    /**
     * Split function used to select which quadrant of matrix you want to return when splitting in 4
     * This function has been changed to handle the vector case
     */
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

    /**
     * Sequentially add 2 different matrices and store result in this class
     */
    public void add(Matrix matrixA, Matrix matrixB) {
        for (int i = 0; i < matrixA.n; i++) {
            for (int j = 0; j < matrixA.m; j++) {
                this.set(i, j, matrixA.get(i, j) + matrixB.get(i, j));
            }
        }
    }

    /**
     * Sequentially multiply 2 different matrices and store result in this class
     */
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

    /**
     * String representation of the matrix data
     */
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
