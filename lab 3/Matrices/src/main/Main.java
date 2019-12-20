package Matrices.src.main;

import model.Matrix;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Matrix> matrices = createMatrices();

        Scanner input = new Scanner(System.in);

        while (true) {
            menu();
            String option = input.next();
            if (option.equals("1")) {
                operationsWithThreadPools(1, matrices);
                operationsWithThreadPools(4, matrices);
                operationsWithThreadPools(100, matrices);
                operationsWithThreadPools(200, matrices);
            } else if (option.equals("2")) {
                operationsWithFuture(1, matrices);
                operationsWithFuture(4, matrices);
                operationsWithFuture(100, matrices);
                operationsWithFuture(200, matrices);
            } else if (option.equals("0")) {
                System.exit(0);
                System.out.println("Application ended!");
            } else {
                System.out.println("Wrong input! Try again!");
            }
        }
    }

    private static void menu() {
        System.out.println("Choose from: ");
        System.out.println("1 - Thread Pool");
        System.out.println("2 - Future");
        System.out.println("0 - Exit the application");
    }

    private static void operationsWithThreadPools(int nrOfThreads, List<Matrix> matrices) throws Exception {
        Date beforeDate = new Date();
        sumThreadPool(nrOfThreads, matrices.get(0), matrices.get(1));
        Date afterDate = new Date();

        Date startDate = new Date();
        multiplyThreadPool(nrOfThreads, matrices.get(0), matrices.get(1));
        Date endDate = new Date();

        long sum = getDifferenceBetween(beforeDate, afterDate);
        long multiply = getDifferenceBetween(startDate, endDate);
        System.out.println("for addition - number of threads: " + nrOfThreads + " time: " + sum);
        System.out.println("for multiplication - number of threads: " + nrOfThreads + " time: " + multiply);
        System.out.println("---------------------------------------------------------------------------------");
    }

    private static void operationsWithFuture(int nrOfThreads, List<Matrix> matrices) throws Exception {
        Date beforeDate = new Date();
        sumFuture(nrOfThreads, matrices.get(0), matrices.get(1));
        Date afterDate = new Date();

        Date startDate = new Date();
        multiplyFuture(nrOfThreads, matrices.get(0), matrices.get(1));
        Date endDate = new Date();

        long sum = getDifferenceBetween(beforeDate, afterDate);
        long multiply = getDifferenceBetween(startDate, endDate);
        System.out.println("for addition - number of futures: " + nrOfThreads + " time: " + sum);
        System.out.println("for multiplication - number of futures: " + nrOfThreads + " time: " + multiply);
        System.out.println("---------------------------------------------------------------------------------");
    }

    private static long getDifferenceBetween(Date startDate, Date endDate) {
        return endDate.getTime() - startDate.getTime();
    }

    private static List<Matrix> createMatrices() {
        int upperbound = 100;
        int nrOfLines = 1000;
        int nrOfColumns = 1000;
        int[][] ints = new int[nrOfLines][nrOfColumns];
        Matrix firstMatrix = new Matrix(nrOfLines, nrOfColumns, ints);
        Random random = new Random();
        for (int i = 0; i < nrOfLines; i++) {
            for (int j = 0; j < nrOfColumns; j++) {
                firstMatrix.getMatrix()[i][j] = random.nextInt(upperbound);
            }
        }

        int[][] ints1 = new int[nrOfLines][nrOfColumns];
        Matrix secondMatrix = new Matrix(nrOfLines, nrOfColumns, ints1);
        for (int i = 0; i < nrOfLines; i++) {
            for (int j = 0; j < nrOfColumns; j++) {
                secondMatrix.getMatrix()[i][j] = random.nextInt(upperbound);
            }
        }

        List<Matrix> matrices = new ArrayList<>(Arrays.asList(firstMatrix, secondMatrix));

        return matrices;
    }

    private static void sumThreadPool(int nrOfThreads, Matrix firstMatrix, Matrix secondMatrix) throws Exception {
        if (firstMatrix.getLines() != secondMatrix.getLines() ||
                firstMatrix.getColumns() != secondMatrix.getColumns()) {
            throw new Exception("Cannot perform addition operation");
        }
        int lines = firstMatrix.getLines();
        Matrix result = new Matrix(firstMatrix.getLines(), firstMatrix.getColumns());
        ExecutorService executorService = Executors.newFixedThreadPool(nrOfThreads);
        for (int i = 0; i < lines; i++) {
            final int finalPosition = i;
            executorService.execute(() -> addLine(result, finalPosition,
                    firstMatrix.getMatrix()[finalPosition], secondMatrix.getMatrix()[finalPosition]));
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        System.out.println("Finished all threads for addition for " + nrOfThreads + " threads");
    }

    private static void sumFuture(int nrOfThreads, Matrix firstMatrix, Matrix secondMatrix) throws Exception {
        if (firstMatrix.getLines() != secondMatrix.getLines() ||
                firstMatrix.getColumns() != secondMatrix.getColumns()) {
            throw new Exception("Cannot perform addition operation");
        }
        int lines = firstMatrix.getLines();
        Matrix result = new Matrix(firstMatrix.getLines(), firstMatrix.getColumns());

        ExecutorService executorService = Executors.newFixedThreadPool(nrOfThreads);
        List<Future<Void>> futures = new ArrayList<>();
        for (int i = 0; i < lines; i++) {
            final int finalPosition = i;
            futures.add(executorService.submit(
                    () -> {
                        addLine(result, finalPosition,
                                firstMatrix.getMatrix()[finalPosition], secondMatrix.getMatrix()[finalPosition]);
                        return null;
                    }));
        }

        for (Future<Void> future : futures) {
            future.get();
        }
        System.out.println("Finished all " + nrOfThreads + " futures for addition");
    }

    private static Matrix addLine(Matrix resultedMatrix, int position, int[] line1, int[] line2) {
        int[] rez = new int[line1.length];

        for (int i = 0; i < line1.length; i++) {
            rez[i] = line1[i] + line2[i];
        }

        resultedMatrix.getMatrix()[position] = rez;

        return resultedMatrix;
    }

    private static void multiplyThreadPool(int nrOfThreads, Matrix firstMatrix, Matrix secondMatrix) throws Exception {
        if (firstMatrix.getColumns() != secondMatrix.getLines()) {
            throw new Exception("Cannot perform multiplication");
        }
        int lines = firstMatrix.getLines();
        int columns = secondMatrix.getColumns();
        Matrix result = new Matrix(lines, columns);

        ExecutorService executorService = Executors.newFixedThreadPool(nrOfThreads);

        for (int i = 0; i < lines; i++) {
            final int finalLine = i;
            for (int j = 0; j < columns; j++) {
                final int finalColumn = j;
                executorService.execute(new Thread(() ->
                        multiplyLine(result, finalLine, finalColumn,
                                firstMatrix.getMatrix()[finalLine], secondMatrix.getColumn(finalColumn))));
            }
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        System.out.println("Finished all " + nrOfThreads + " futures for multiplication");
    }

    private static void multiplyFuture(int nrOfThreads, Matrix firstMatrix, Matrix secondMatrix) throws Exception {
        if (firstMatrix.getColumns() != secondMatrix.getLines()) {
            throw new Exception("Cannot perform multiplication");
        }
        int lines = firstMatrix.getLines();
        int columns = secondMatrix.getColumns();
        Matrix result = new Matrix(lines, columns);

        ExecutorService executorService = Executors.newFixedThreadPool(nrOfThreads);
        List<Future<Void>> futures = new ArrayList<>();
        for (int i = 0; i < lines; i += nrOfThreads) {
            final int finalLine = i;
            for (int j = 0; j < columns; j++) {
                final int finalColumn = j;
                futures.add(executorService.submit(() -> {
                    multiplyLine(result, finalLine, finalColumn,
                            firstMatrix.getMatrix()[finalLine], secondMatrix.getColumn(finalColumn));
                    return null;
                }));
            }
        }

        for (Future<Void> future : futures) {
            future.get();
        }
    }

    private static Matrix multiplyLine(Matrix resultedMatrix, int line, int column, int[] matrix1, int[] matrix2) {
        int rez = 0;
        for (int i = 0; i < matrix1.length; i++) {
            rez += matrix1[i] * matrix2[i];
        }

        resultedMatrix.getMatrix()[line][column] = rez;

        return resultedMatrix;
    }
}
