
public class Matrix {
    private int[][] matrix;

    Matrix(int[][] matrix) {
        this.matrix = matrix;
    }

    int getValue(int r, int c) {
        return matrix[r][c];
    }

    int getRowsSize() {
        return matrix.length;
    }

    int getColsSize() {
        return matrix[0].length;
    }

    @Override
    public String toString() {
        StringBuilder toPrint = new StringBuilder();

        for (int[] row : matrix) {
            for (int cell : row) {
                toPrint.append(cell).append("\t\t ");
            }
            toPrint.append("\n");
        }

        return toPrint.toString();
    }
}
