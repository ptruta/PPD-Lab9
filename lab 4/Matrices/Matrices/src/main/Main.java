package main;

import model.Matrix;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
        List<Matrix> matrices = createMatrices();
        int nrOfThreads = 5;

        ProducerConsumer producerConsumer = new ProducerConsumer();
        producerConsumer.finalMatrix = new Matrix(matrices.get(0).getNumberOfLines(), matrices.get(2).getNumberOfColumns());
        // Create producer threads
        Thread thread1 = new Thread(() -> {
            try {
                producerConsumer.produce(nrOfThreads, matrices.get(0), matrices.get(1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Create consumer threads
        Thread thread2 = new Thread(() -> {
            try {
                producerConsumer.consume(nrOfThreads, matrices.get(2));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread1.join();

        thread2.start();
        thread2.join();

        System.out.println(producerConsumer.finalMatrix);
    }
//
//    private static List<Matrix> createMatrices() {
//        int upperbound = 10;
//        int nrOfLines = 2;
//        int nrOfColumns = 2;
//        int[][] ints1 = new int[nrOfLines][nrOfColumns];
//        Matrix firstMatrix = new Matrix(nrOfLines, nrOfColumns, ints1);
//        Random random = new Random();
//        for (int i = 0; i < nrOfLines; i++) {
//            for (int j = 0; j < nrOfColumns; j++) {
//                firstMatrix.getMatrix()[i][j] = 1;
//            }
//        }
//        System.out.println(firstMatrix);
//
//        int[][] ints2 = new int[nrOfLines][nrOfColumns];
//        Matrix secondMatrix = new Matrix(nrOfLines, nrOfColumns, ints2);
//        for (int i = 0; i < nrOfLines; i++) {
//            for (int j = 0; j < nrOfColumns; j++) {
//                secondMatrix.getMatrix()[i][j] = 1;
//            }
//        }
//        System.out.println(secondMatrix);
//
//        int[][] ints3 = new int[nrOfLines][nrOfColumns];
//        Matrix thirdMatrix = new Matrix(nrOfLines, nrOfColumns, ints3);
//        for (int i = 0; i < nrOfLines; i++) {
//            for (int j = 0; j < nrOfColumns; j++) {
//                thirdMatrix.getMatrix()[i][j] = 1;
//            }
//        }
//        System.out.println(thirdMatrix);
//
//        List<Matrix> matrices = Arrays.asList(firstMatrix, secondMatrix, thirdMatrix);
//
//        return matrices;
//    }
//}
    private static List<Matrix> createMatrices() {
        int upperbound = 10;
        int nrOfLines = 100;
        int nrOfColumns = 100;
        int[][] ints1 = new int[nrOfLines][nrOfColumns];
        Matrix firstMatrix = new Matrix(nrOfLines, nrOfColumns, ints1);
        Random random = new Random();
        for (int i = 0; i < nrOfLines; i++) {
            for (int j = 0; j < nrOfColumns; j++) {
                firstMatrix.getMatrix()[i][j] = random.nextInt(upperbound);
            }
        }
        System.out.println(firstMatrix);

        int[][] ints2 = new int[nrOfLines][nrOfColumns];
        Matrix secondMatrix = new Matrix(nrOfLines, nrOfColumns, ints2);
        for (int i = 0; i < nrOfLines; i++) {
            for (int j = 0; j < nrOfColumns; j++) {
                secondMatrix.getMatrix()[i][j] = random.nextInt(upperbound);
            }
        }
        System.out.println(secondMatrix);

        int[][] ints3 = new int[nrOfLines][nrOfColumns];
        Matrix thirdMatrix = new Matrix(nrOfLines, nrOfColumns, ints3);
        for (int i = 0; i < nrOfLines; i++) {
            for (int j = 0; j < nrOfColumns; j++) {
                thirdMatrix.getMatrix()[i][j] = random.nextInt(upperbound);
            }
        }
        System.out.println(thirdMatrix);

        return Arrays.asList(firstMatrix, secondMatrix, thirdMatrix);
    }
}
