package main;

import model.Matrix;
import model.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer {

    public Matrix finalMatrix;
    private Lock lock = new ReentrantLock();
    private Condition consumerCondition;
    private List<Pair> sharedData;

    public ProducerConsumer() {
        this.consumerCondition = lock.newCondition();
        this.sharedData = new ArrayList<>();
        this.finalMatrix = new Matrix();
    }

    public void produce(int nrOfThreads, Matrix firstMatrix, Matrix secondMatrix) throws InterruptedException {
        //used for the multiplication of the first 2 matrices, result should be used in consumer side
        int currentLine = 0;
        int numberOfLines = firstMatrix.getNumberOfLines();
        ExecutorService executorService = Executors.newFixedThreadPool(nrOfThreads);

        while (currentLine < numberOfLines) {
            final int finalCurrentLine = currentLine;
            executorService.execute(new Thread(() -> {
                lock.lock();
                System.out.println("producer Thread: " + Thread.currentThread().getName() + " lock acquired");
                sharedData.add(multiplyLine(finalCurrentLine, firstMatrix.getMatrix()[finalCurrentLine],
                        secondMatrix));
                // notifies the consumer thread that
                // now it can start consuming
                consumerCondition.signalAll();
                lock.unlock();
                System.out.println("producer Thread: " + Thread.currentThread().getName() + " lock released");
            }));
            currentLine += 1;
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
    }


    public void consume(int nrOfThreads, Matrix thirdMatrix) throws InterruptedException {

        int numberOfLines = thirdMatrix.getNumberOfColumns();
        ExecutorService executorService = Executors.newFixedThreadPool(nrOfThreads);

        for (int i = 0; i < numberOfLines; i++) {
            executorService.execute(new Thread(() -> {
                try {
                    lock.lock();
                    System.out.println("consumer Thread: " + Thread.currentThread().getName() + " lock acquired");
                    // consumer thread waits while the producer
                    // emits data (first line is computed)
                    while (sharedData.size() == 0)
                        consumerCondition.await();

                    Pair sharedPair = sharedData.remove(0);

                    // to retrieve the first job in the list
                    Pair finalPair = multiplyLine(sharedPair.getCurrentLine(), sharedPair.getLine(),
                            thirdMatrix);

                    int currentLine = finalPair.getCurrentLine();
                    finalMatrix.getMatrix()[currentLine] = finalPair.getLine();

                    lock.unlock();
                    System.out.println("consumer Thread: " + Thread.currentThread().getName() + " lock released");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
    }

    private Pair multiplyLine(int currentLine, int[] matrix1, Matrix matrix2) {
        Pair pair = new Pair();

        pair.setCurrentLine(currentLine);

        int[] resultLine = new int[matrix1.length];
        //i for lines
        for (int i = 0; i < matrix1.length; i++) {
            int rez = 0;
            //j for columns
            int[] columns = matrix2.getColumn(i);
            for (int j = 0; j < columns.length; j++) {
                rez += matrix1[j] * columns[j];
            }
            resultLine[i] = rez;
        }

        pair.setLine(resultLine);

        return pair;
    }
}
