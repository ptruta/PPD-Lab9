package model;

public class Matrix {
    private int lines;
    private int columns;
    private int[][] matrix;

    public Matrix() {
        this.lines = 0;
        this.columns = 0;
        this.matrix = new int[][]{};
    }

    public Matrix(int lines, int columns, int[][] matrix) {
        this.lines = lines;
        this.columns = columns;
        this.matrix = matrix;
    }

    public Matrix(int lines, int columns) {
        this.lines = lines;
        this.columns = columns;
        this.matrix = new int[this.lines][this.columns];
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int[] getColumn(int column) {
        int[] rez = new int[this.lines];
        for (int i = 0; i < this.lines; i++) {
            rez[i] = this.matrix[i][column];
        }
        return rez;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                s += this.matrix[i][j] + "\t";
            }
            s += "\n";
        }
        return s;
    }
}
