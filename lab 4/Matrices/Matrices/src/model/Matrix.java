package model;

public class Matrix {
    private int numberOfLines;
    private int numberOfColumns;
    private int[][] matrix;
    private int currentLine;

    public Matrix() {
        this.numberOfLines = 0;
        this.numberOfColumns = 0;
        this.matrix = new int[][]{};
    }

    public Matrix(int numberOfLines, int numberOfColumns, int[][] matrix) {
        this.numberOfLines = numberOfLines;
        this.numberOfColumns = numberOfColumns;
        this.matrix = matrix;
    }

    public Matrix(int numberOfLines, int numberOfColumns) {
        this.numberOfLines = numberOfLines;
        this.numberOfColumns = numberOfColumns;
        this.matrix = new int[this.numberOfLines][this.numberOfColumns];
        this.currentLine = 0;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(int currentLine) {
        this.currentLine = currentLine;
    }

    public int[] getColumn(int column) {
        int[] rez = new int[this.numberOfLines];
        for (int i = 0; i < this.numberOfLines; i++) {
            rez[i] = this.matrix[i][column];
        }
        return rez;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.numberOfLines; i++) {
            for (int j = 0; j < this.numberOfColumns; j++) {
                s.append(this.matrix[i][j]).append("\t");
            }
            s.append("\n");
        }
        return s.toString();
    }
}
