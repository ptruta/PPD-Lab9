import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int[][] matrix1= new int[100][100];
        int[][] matrix2= new int[100][100];
        int n=100;
        int m=100;
        for (int i=0;i<100;i++){
            for (int j=0;j<100;j++){
                matrix1[i][j] = 440000;
                matrix2[i][j] = 440000;
            }
        }

        int[][] resultSum = new int[100][100];

        //1st CASE: One thread that computes the sum

        //the time it starts thread
        long start = System.currentTimeMillis();

        //sum of the 2 matrixes
        Sum sum=new Sum(matrix1,matrix2,resultSum,100,100,0,100,0,100);

        //start the thread
        sum.start();

        //join the first to run them in parallel
        sum.join();

        //the time it finishes the thread
        long finish = System.currentTimeMillis();

        //the time used to run this thread and finish it
        long timeElapsed = finish - start;
        System.out.println("One thread computed the sum in : "+timeElapsed +" miliseconds");


        //CASE2: 2 threads that compute the sum: each getting 1/2 of the lines
        resultSum = new int[100][100];
        double d = n/(double)3;  //33.33
        int step = (int)Math.ceil(d); //this rounds to the upper bound the d variable -> 34.0
        int startN = 0;
        int endN = startN + step;

        start = System.currentTimeMillis();
        List<Thread> threads=new ArrayList<>();
        while (endN < n){
            Sum sum1 = new Sum(matrix1,matrix2,resultSum,n,m,startN, endN,0,100);
            threads.add(sum1);
            startN = endN;
            endN = startN + step;
        }

        sum = new Sum(matrix1,matrix2,resultSum,n,m,startN, n,0,100);
        threads.add(sum);

        for (int i=0;i<2;i++) {
            threads.get(i).start();
        }

        for (int i=0;i<2;i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        finish = System.currentTimeMillis();
        timeElapsed = finish - start;
        System.out.println("Two threads computed the sum in : "+timeElapsed +" miliseconds");


        //CASE3: 100 threads: each gets one of the lines

        resultSum = new int[100][100];

        step = n/100;
        startN = 0;
        endN = startN+ step;

        start = System.currentTimeMillis();
        threads=new ArrayList<>();
        while (endN< n){
            Sum sum1 = new Sum(matrix1,matrix2,resultSum,n,m,startN, endN,0,99);
            threads.add(sum1);
            startN = endN;
            endN = startN + step;

        }

        sum = new Sum(matrix1,matrix2,resultSum,n,m,startN, n,0,99);
        threads.add(sum);

        for (int i=0;i<threads.size();i++) {
            threads.get(i).start();

        }

        for (int i=0;i<threads.size();i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        finish = System.currentTimeMillis();
        timeElapsed = finish - start;
        System.out.println("100 THREADS computed the sum in : "+timeElapsed +" miliseconds");


        int startM = 0;
        int endM =0;
        //CASE4: 10000 threads: each gets one element

        resultSum = new int[100][100];
        step = n/100;
        startN = 0;
        endN = startN+ step;

        startM = 0;
        endM = startM + step;

        start = System.currentTimeMillis();
        threads=new ArrayList<>();
        while (endN< n){
            startM = 0;
            endM = startM + step;
            while (endM < m) {
                Sum sum1 = new Sum(matrix1, matrix2, resultSum, n, m, startN, endN, startM, endM);
                threads.add(sum1);
                startM = endM ;
                endM = startM + step;
            }

            sum = new Sum(matrix1,matrix2,resultSum,n,m,startN, endN,startM,100);
            threads.add(sum);

            startN = endN;
            endN = startN + step;
        }

        startM = 0;
        endM = startM + step;
        while (endM < m) {
            Sum sum1 = new Sum(matrix1, matrix2, resultSum, n, m, startN, 100, startM, endM);
            threads.add(sum1);
            startM = endM ;
            endM = startM + step;
        }

        sum = new Sum(matrix1,matrix2,resultSum,n,m,startN, 100,startM,100);
        threads.add(sum);

        for (int i=0;i<threads.size();i++) {
            threads.get(i).start();

        }

        for (int i=0;i<threads.size();i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        finish = System.currentTimeMillis();
        timeElapsed = finish - start;
        System.out.println("1000 THREADS computed the sum in : "+timeElapsed +" miliseconds");




        // Product
        int[][] matrix3 = new int[100][100];
        int[][] matrix4 = new int[100][100];

        for (int i=0;i<100;i++){
            for (int j=0;j<100;j++){
                matrix3[i][j] = 1;
                matrix4[i][j] = 2;
            }
        }
        int[][] productResult = new int[100][100];

        //Case1: one thread computes everything
        start = System.currentTimeMillis();

        Product product=new Product(matrix3,matrix4, productResult,100,100,0,100,0,100);
        product.start();
        product.join();

        finish = System.currentTimeMillis();
        timeElapsed = finish - start;
        System.out.println("One thread computes the product in : "+timeElapsed +" miliseconds");


        //Case 2: two threads: each computes 1/2 of elements of lines
        start = System.currentTimeMillis();
        productResult = new int[100][100];
         d = n/(double)3;
         step = (int)Math.ceil(d);
         startN = 0;
         endN = startN+ step;
        threads=new ArrayList<>();
        while (endN< n){
            Product product1=new Product(matrix3,matrix4, productResult,100,100,startN,endN,0,100);
            threads.add(product1);
            startN = endN;
            endN = startN + step;

        }

        product=new Product(matrix3,matrix4, productResult,100,100,startN,100,0,100);
        threads.add(product);

        for (int i=0;i<2;i++) {
            threads.get(i).start();

        }

        for (int i=0;i<2;i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        finish = System.currentTimeMillis();
        timeElapsed = finish - start;
        System.out.println("Two threads computed the product in : "+timeElapsed +" miliseconds");


        //Case 3: 100 threads: each computes an element
        start = System.currentTimeMillis();
        productResult = new int[100][100];
        d = n/(double)100;
        step = (int)Math.ceil(d);
        startN = 0;
        endN = startN+ step;
        threads=new ArrayList<>();
        while (endN< n){
            Product product1=new Product(matrix3,matrix4, productResult,100,100,startN,endN,0,100);
            threads.add(product1);
            startN = endN;
            endN = startN + step;

        }

        product=new Product(matrix3,matrix4, productResult,100,100,startN,100,0,100);
        threads.add(product);

        for (int i=0;i<2;i++) {
            threads.get(i).start();

        }

        for (int i=0;i<2;i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        finish = System.currentTimeMillis();
        timeElapsed = finish - start;
        System.out.println("100 threads computed the product in : "+timeElapsed +" miliseconds");


        // Case4: 1000 threads each computes an element
        productResult = new int[100][100];
        step = n/100;
        startN = 0;
        endN = startN+ step;

        startM = 0;
        endM = startM + step;

        start = System.currentTimeMillis();
        threads=new ArrayList<>();
        while (endN< n){
            startM = 0;
            endM = startM + step;
            while (endM < m) {
                Product product1=new Product(matrix3,matrix4, productResult,100,100,startN,endN,startM,endM);
                threads.add(product1);
                startM = endM ;
                endM = startM + step;
            }

            product = new Product(matrix3,matrix4, productResult,100,100,startN,endN,startM,100);
            threads.add(product);

            startN = endN ;
            endN = startN + step;
        }

        startM = 0;
        endM = startM + step;
        while (endM < m) {
            Product product1=new Product(matrix3,matrix4, productResult,100,100,startN,100,startM,endM);
            threads.add(product1);
            startM = endM ;
            endM = startM + step;
        }

        product = new Product(matrix3,matrix4,productResult,n,m,startN, 100,startM,100);
        threads.add(product);

        for (int i=0;i<threads.size();i++) {
            threads.get(i).start();

        }

        for (int i=0;i<threads.size();i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        finish = System.currentTimeMillis();
        timeElapsed = finish - start;
        System.out.println("1000 THREADS computed the product in : "+timeElapsed +" miliseconds");
    }
}
