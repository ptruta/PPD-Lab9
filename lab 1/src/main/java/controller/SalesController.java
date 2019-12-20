package controller;

import domain.Bill;
import domain.Product;
import domain.Store;
import repository.BillRepository;
import repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class SalesController extends Thread {
    private Store store;
    private BillRepository billRepository;
    private ProductRepository productRepository;

    private Semaphore semBills;
    private int index;

    public SalesController(Store store, BillRepository billRepository, ProductRepository productRepository, int idx, Semaphore semaphore) {
        this.store = store;
        this.billRepository = billRepository;
        this.productRepository = productRepository;
        this.index = idx;

        this.semBills = semaphore;
    }

    public void run() {
        //hash map that holds the bought products
        HashMap<Product, Integer> items = new HashMap<>();

        //each client should buy some products
        /*
        Number of products: random between 1-15
        The chosen: random index not chosen until now
        The quantity: random between 1 and 10
         */
        try {
            List<Product> products = productRepository.getAll();
            Random r = new Random();
            int nrProducts = r.nextInt(products.size() - 1);
            nrProducts += 1;
            System.out.println("Thread " + index + " plans to buy " + nrProducts);
            int pricePerBill = 0;
            for (int i = 0; i < nrProducts; i++) {

                Thread.sleep(2000);
                boolean okNotBought = false;
                while (!okNotBought) {

                    int indexOfProduct = r.nextInt(products.size() - 1);
                    Product p = products.get(indexOfProduct);
                    p.getLock().lock();

                    boolean ok = true;
                    for (Product p1 : items.keySet()) {
                        if (p1.equals(p)) {
                            ok = false;
                        }
                    }
                    if (ok) {
                        Random r2 = new Random();
                        int quantity = r2.nextInt((10 - 1) + 1) + 1;
                        int newQuantity = p.getQuantity() - quantity;

                        if (newQuantity < 0) {
                            System.out.println("Item " + p.getName() + " not available in this quantity");
                            p.getLock().unlock();
                        } else {
                            System.out.println("Thread " + index + " is putting to chart " + p.getName());
                            items.put(p, quantity);
                            productRepository.updateProduct(p, newQuantity);
                            pricePerBill += quantity * p.getPrice();
                            p.getLock().unlock();
                        }
                        okNotBought = true;
                    } else {
                        p.getLock().unlock();
                    }
                }
            }

            // Creating and adding the bill to the list only if there is something in it
            if (items.size() > 0) {
                Bill b1 = new Bill(items, pricePerBill);
                Thread.sleep(2000);
                semBills.acquire();
                System.out.println("------------------------------------------------------------------" + "Thread " + index + " adds a bill");
                billRepository.addBill(b1);
                store.setMoney(store.getMoney() + pricePerBill);
                System.out.println("Money made until now:" + store.getMoney());
                semBills.release();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
