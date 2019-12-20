import controller.Checker;
import controller.SalesController;
import domain.Product;
import domain.Store;
import repository.BillRepository;
import repository.ProductRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        Store store = new Store();
        //create products
        Product p = new Product("orez", 4, 100, 1);
        Product p2 = new Product("ceapa", 3, 400, 2);
        Product p3 = new Product("varza", 2, 150, 3);
        Product p4 = new Product("ulei", 5, 100, 4);
        Product p5 = new Product("avocado", 7, 200, 5);
        Product p6 = new Product("ciocolata", 4, 300, 6);
        Product p7 = new Product("hummus", 8, 220, 7);
        Product p8 = new Product("rosii", 5, 400, 8);
        Product p9 = new Product("ardei", 5, 400, 9);
        Product p10 = new Product("paste", 9, 200, 10);
        Product p11 = new Product("piept de pui", 20, 240, 11);
        Product p12 = new Product("seminte chia", 30, 100, 12);
        Product p13 = new Product("papanasi", 13, 100, 13);
        Product p14 = new Product("cofrag oua", 6, 600, 14);
        Product p15 = new Product("paine", 4, 800, 15);
        Product p16 = new Product("unt", 7, 400, 16);
        Product p17 = new Product("sare", 2, 800, 17);
        Product p18 = new Product("cafea", 12, 200, 18);
        Product p19 = new Product("sampon", 15, 200, 19);
        Product p20 = new Product("masline", 10, 1000, 20);
        Product p21 = new Product("branza", 17, 700, 21);
        Product p22 = new Product("salam", 20, 600, 22);
        Product p23 = new Product("cif", 12, 600, 23);
        Product p24 = new Product("peste", 25, 600, 24);

        ProductRepository productRepository = new ProductRepository();
        addProductsToRepository(Arrays.asList(p, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16,
                p17, p18, p19, p20, p21, p22, p23, p24), productRepository);

        BillRepository billRepository = new BillRepository();

        Semaphore semaphore = new Semaphore(1);

        //Case1: 8 clients
        startThreadOperations(store, productRepository, billRepository, semaphore, 8, "8 clients finished shopping in:");

        //CASE2: 40 clients
        startThreadOperations(store, productRepository, billRepository, semaphore, 40, "40 clients finished shopping in:");

        //Case3: 70 clients
        startThreadOperations(store, productRepository, billRepository, semaphore, 70, "70 clients finished shopping in:");
    }

    private static void addProductsToRepository(List<Product> products, ProductRepository productRepository) {
        products.forEach(productRepository::addProduct);
    }

    private static void startThreadOperations(Store store, ProductRepository productRepository,
                                              BillRepository billRepository, Semaphore semaphore,
                                              int numberOfThreads, String outputMessage) {
        List<Thread> threads = new ArrayList<>();
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        long start;
        long finish;
        long timeElapsed;

        exec.scheduleAtFixedRate(new Checker(billRepository, store, semaphore), 0, 5, TimeUnit.SECONDS);

        // the time it starts a thread
        start = System.currentTimeMillis();

        // add threads
        for (int i = 0; i < numberOfThreads; i++) {
            SalesController salesController = new SalesController(store, billRepository, productRepository, i, semaphore);
            threads.add(salesController);
        }

        // start them
        for (int i = 0; i < numberOfThreads; i++) {
            threads.get(i).start();
        }

        // working in parallel
        for (int i = 0; i < numberOfThreads; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        exec.shutdown();

        // last inventory of the bills and money earned
        System.out.println("Last check:");
        Checker lastCheck = new Checker(billRepository, store, semaphore);
        lastCheck.run();

        // the time a thread is finished
        finish = System.currentTimeMillis();

        // the time used to run the thread and finish it
        timeElapsed = finish - start;
        System.out.println(outputMessage + timeElapsed + " miliseconds");
    }

}
