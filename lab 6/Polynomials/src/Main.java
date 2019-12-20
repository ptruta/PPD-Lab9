import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        int nrOfThreads = 3;
        List<Integer> p1 = new ArrayList<>(Arrays.asList(2000, 345344, 4700, 9900));
        Polynomial polynomial1 = new Polynomial(p1);

        List<Integer> p2 = new ArrayList<>(Arrays.asList(1, 2, 4, 5));
        Polynomial polynomial2 = new Polynomial(p2);

        polynomial1.print();
        polynomial2.print();

        System.out.println("multiplyRegularSequential: ");
        Date beforeDate = new Date();
        multiplyRegularSequential(polynomial1, polynomial2).print();
        Date afterDate = new Date();
        long time = getDifferenceBetween(beforeDate, afterDate);
        System.out.println("time: " + time);

        System.out.println("multiplyRegularParallel");
        beforeDate = new Date();
        multiplyRegularParallel(nrOfThreads, polynomial1, polynomial2).print();
        afterDate = new Date();
        time = getDifferenceBetween(beforeDate, afterDate);
        System.out.println("time: " + time);

        System.out.println("karatsubaMultiply");
        beforeDate = new Date();
        karatsubaMultiply(polynomial1, polynomial2).print();
        afterDate = new Date();
        time = getDifferenceBetween(beforeDate, afterDate);
        System.out.println("time: " + time);

        System.out.println("karatsubaMultiplyParallel");
        beforeDate = new Date();
        karatsubaMultiplyParallel(nrOfThreads, polynomial1, polynomial2).print();
        afterDate = new Date();
        time = getDifferenceBetween(beforeDate, afterDate);
        System.out.println("time: " + time);
    }

    private static long getDifferenceBetween(Date startDate, Date endDate) {
        return endDate.getTime() - startDate.getTime();
    }

    private static Polynomial multiplyRegularSequential(Polynomial polynomial1, Polynomial polynomial2) {
        Polynomial resultPolynomial = new Polynomial(polynomial1.getLength() + polynomial2.getLength() - 1);
        for (int i = 0; i < polynomial1.getLength(); i++) {
            for (int j = 0; j < polynomial2.getLength(); j++)
                multiplication(polynomial1, polynomial2, resultPolynomial, i, j);
        }

        return resultPolynomial;
    }

    private static Polynomial multiplyRegularParallel(int nrOfThreads, Polynomial polynomial1, Polynomial polynomial2) {
        ExecutorService executorService = Executors.newFixedThreadPool(nrOfThreads);
        Polynomial resultPolynomial = new Polynomial(polynomial1.getLength() + polynomial2.getLength() - 1);
        for (int i = 0; i < polynomial1.getLength(); i++) {
            for (int j = 0; j < polynomial2.getLength(); j++) {
                final int finalI = i;
                final int finalJ = j;
                executorService.execute(() ->
                        multiplication(polynomial1, polynomial2, resultPolynomial, finalI, finalJ));
            }
        }

        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) break;
        }
        return resultPolynomial;
    }

    private static void multiplication(Polynomial polynomial1,
                                       Polynomial polynomial2,
                                       Polynomial resultPolynomial,
                                       int i,
                                       int j) {
        System.out.println("--" + Thread.currentThread().getId());
        resultPolynomial.getCoefficients().set(i + j,
                resultPolynomial.getCoefficients().get(i + j) +
                        polynomial1.getCoefficients().get(i) * polynomial2.getCoefficients().get(j));
    }

    private static Polynomial karatsubaMultiply(Polynomial polynomial1, Polynomial polynomial2) {

        if (polynomial1.getLength() < polynomial2.getLength()) {
            polynomial1.addZeroesToCoefficients(polynomial2.getLength());
        } else if (polynomial1.getLength() > polynomial2.getLength()) {
            polynomial2.addZeroesToCoefficients(polynomial1.getLength());
        }

        return karatsubaMultiplyRecursive(polynomial1, polynomial2);

    }

    private static Polynomial karatsubaMultiplyParallel(int nrOfThreads, Polynomial polynomial1, Polynomial polynomial2) {

        if (polynomial1.getLength() < polynomial2.getLength()) {
            polynomial1.addZeroesToCoefficients(polynomial2.getLength());
        } else if (polynomial1.getLength() > polynomial2.getLength()) {
            polynomial2.addZeroesToCoefficients(polynomial1.getLength());
        }

        return karatsubaMultiplyRecursiveParallel(nrOfThreads, polynomial1, polynomial2);

    }

    private static Polynomial karatsubaMultiplyRecursive(Polynomial polynomial1, Polynomial polynomial2) {

        Polynomial resultPolynomial = new Polynomial(2 * polynomial1.getLength());

        //Handle the base case where the polynomial2 has only one coefficient
        if (polynomial1.getLength() == 1) {
            resultPolynomial.getCoefficients()
                    .set(0, polynomial1.getCoefficients().get(0) * polynomial2.getCoefficients().get(0));
            return resultPolynomial;
        }

        int halfArraySize = polynomial1.getLength() / 2;

        //Declare arrays to hold halved factors
        Polynomial polynomial1Low = new Polynomial(halfArraySize);
        Polynomial polynomial1High = new Polynomial(halfArraySize);
        Polynomial polynomial2Low = new Polynomial(halfArraySize);
        Polynomial polynomial2High = new Polynomial(halfArraySize);

        Polynomial polynomial1LowHigh = new Polynomial(halfArraySize);
        Polynomial polynomial2LowHigh = new Polynomial(halfArraySize);

        //Fill in the low and high arrays
        for (int halfSizeIndex = 0; halfSizeIndex < halfArraySize; ++halfSizeIndex) {

            fillLowAndHigh(polynomial1,
                    polynomial2,
                    halfArraySize,
                    polynomial1Low,
                    polynomial1High,
                    polynomial2Low,
                    polynomial2High,
                    polynomial1LowHigh,
                    polynomial2LowHigh,
                    halfSizeIndex);
        }

        //Recursively call method on smaller arrays and construct the low and high parts of the resultPolynomial
        Polynomial productLow = karatsubaMultiplyRecursive(polynomial1Low, polynomial2Low);
        Polynomial productHigh = karatsubaMultiplyRecursive(polynomial1High, polynomial2High);

        Polynomial productLowHigh = karatsubaMultiplyRecursive(polynomial1LowHigh, polynomial2LowHigh);

        //Construct the middle portion of the resultPolynomial
        Polynomial productMiddle = new Polynomial(polynomial1.getLength());
        for (int halfSizeIndex = 0; halfSizeIndex < polynomial1.getLength(); ++halfSizeIndex) {
            fillMiddle(productLow, productHigh, productLowHigh, productMiddle, halfSizeIndex);
        }

        //Assemble the resultPolynomial from the low, middle and high parts. Start with the low and high parts of the resultPolynomial.
        for (int halfSizeIndex = 0, middleOffset = polynomial1.getLength() / 2;
             halfSizeIndex < polynomial1.getLength(); ++halfSizeIndex) {
            fillResult(polynomial1,
                    resultPolynomial,
                    productLow,
                    productHigh,
                    productMiddle,
                    halfSizeIndex,
                    middleOffset);
        }

        return resultPolynomial;
    }

    private static Polynomial karatsubaMultiplyRecursiveParallel(int nrOfThreads, Polynomial polynomial1, Polynomial polynomial2) {

        Polynomial resultPolynomial = new Polynomial(2 * polynomial1.getLength());

        //Handle the base case where the polynomial2 has only one coefficient
        if (polynomial1.getLength() == 1) {
            resultPolynomial.getCoefficients()
                    .set(0, polynomial1.getCoefficients().get(0) * polynomial2.getCoefficients().get(0));
            return resultPolynomial;
        }

        int halfArraySize = polynomial1.getLength() / 2;

        //Declare arrays to hold halved factors
        Polynomial polynomial1Low = new Polynomial(halfArraySize);
        Polynomial polynomial1High = new Polynomial(halfArraySize);
        Polynomial polynomial2Low = new Polynomial(halfArraySize);
        Polynomial polynomial2High = new Polynomial(halfArraySize);

        Polynomial polynomial1LowHigh = new Polynomial(halfArraySize);
        Polynomial polynomial2LowHigh = new Polynomial(halfArraySize);

        ExecutorService executorService = Executors.newFixedThreadPool(nrOfThreads);
        //Fill in the low and high arrays
        for (int halfSizeIndex = 0; halfSizeIndex < halfArraySize; ++halfSizeIndex) {
            final int finalHalfSizeIndex = halfSizeIndex;

            executorService.execute(() ->
                    fillLowAndHigh(polynomial1,
                            polynomial2,
                            halfArraySize,
                            polynomial1Low,
                            polynomial1High,
                            polynomial2Low,
                            polynomial2High,
                            polynomial1LowHigh,
                            polynomial2LowHigh,
                            finalHalfSizeIndex));

        }

        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) break;
        }

        //Recursively call method on smaller arrays and construct the low and high parts of the resultPolynomial
        Polynomial productLow = karatsubaMultiplyRecursive(polynomial1Low, polynomial2Low);
        Polynomial productHigh = karatsubaMultiplyRecursive(polynomial1High, polynomial2High);

        Polynomial productLowHigh = karatsubaMultiplyRecursive(polynomial1LowHigh, polynomial2LowHigh);

        ExecutorService executorService1 = Executors.newFixedThreadPool(nrOfThreads);

        //Construct the middle portion of the resultPolynomial
        Polynomial productMiddle = new Polynomial(polynomial1.getLength());
        for (int halfSizeIndex = 0; halfSizeIndex < polynomial1.getLength(); ++halfSizeIndex) {

            final int finalHalfSizeIndex = halfSizeIndex;
            executorService1.execute(() -> fillMiddle(productLow,
                    productHigh,
                    productLowHigh,
                    productMiddle,
                    finalHalfSizeIndex));

        }

        executorService1.shutdown();
        while (true) {
            if (executorService1.isTerminated()) break;
        }

        ExecutorService executorService2 = Executors.newFixedThreadPool(nrOfThreads);

        //Assemble the resultPolynomial from the low, middle and high parts. Start with the low and high parts of the resultPolynomial.
        for (int halfSizeIndex = 0, middleOffset = polynomial1.getLength() / 2;
             halfSizeIndex < polynomial1.getLength(); ++halfSizeIndex) {

            final int finalHalfSizeIndex = halfSizeIndex;
            executorService2.execute(() ->
                    fillResult(polynomial1,
                    resultPolynomial,
                    productLow,
                    productHigh,
                    productMiddle,
                    finalHalfSizeIndex,
                    middleOffset));

        }

        executorService2.shutdown();
        while (true) {
            if (executorService2.isTerminated()) break;
        }

        return resultPolynomial;
    }

    private static void fillResult(Polynomial polynomial1, Polynomial resultPolynomial, Polynomial productLow, Polynomial productHigh, Polynomial productMiddle, int halfSizeIndex, int middleOffset) {
        resultPolynomial.getCoefficients().set(halfSizeIndex,
                resultPolynomial.getCoefficients().get(halfSizeIndex) +
                        productLow.getCoefficients().get(halfSizeIndex));
        resultPolynomial.getCoefficients()
                .set(halfSizeIndex + polynomial1.getLength(),
                        resultPolynomial.getCoefficients()
                                .get(halfSizeIndex + polynomial1.getLength())
                                + productHigh.getCoefficients().get(halfSizeIndex));
        resultPolynomial.getCoefficients().set(halfSizeIndex + middleOffset,
                resultPolynomial.getCoefficients().get(halfSizeIndex + middleOffset) +
                        productMiddle.getCoefficients().get(halfSizeIndex));
    }

    private static void fillMiddle(Polynomial productLow, Polynomial productHigh, Polynomial productLowHigh, Polynomial productMiddle, int halfSizeIndex) {
        productMiddle.getCoefficients()
                .set(halfSizeIndex,
                        productLowHigh.getCoefficients().get(halfSizeIndex) -
                                productLow.getCoefficients().get(halfSizeIndex) -
                                productHigh.getCoefficients().get(halfSizeIndex));
    }

    private static void fillLowAndHigh(Polynomial polynomial1, Polynomial polynomial2, int halfArraySize, Polynomial polynomial1Low, Polynomial polynomial1High, Polynomial polynomial2Low, Polynomial polynomial2High, Polynomial polynomial1LowHigh, Polynomial polynomial2LowHigh, int halfSizeIndex) {
        polynomial1Low.getCoefficients().set(halfSizeIndex, polynomial1.getCoefficients().get(halfSizeIndex));
        polynomial1High.getCoefficients()
                .set(halfSizeIndex, polynomial1.getCoefficients().get(halfSizeIndex + halfArraySize));
        polynomial1LowHigh.getCoefficients()
                .set(halfSizeIndex,
                        polynomial1Low.getCoefficients().get(halfSizeIndex) +
                                polynomial1High.getCoefficients().get(halfSizeIndex));

        polynomial2Low.getCoefficients()
                .set(halfSizeIndex, polynomial2.getCoefficients().get(halfSizeIndex));
        polynomial2High.getCoefficients()
                .set(halfSizeIndex, polynomial2.getCoefficients().get(halfSizeIndex + halfArraySize));
        polynomial2LowHigh.getCoefficients()
                .set(halfSizeIndex, polynomial2Low.getCoefficients()
                        .get(halfSizeIndex) + polynomial2High.getCoefficients().get(halfSizeIndex));
    }
}

