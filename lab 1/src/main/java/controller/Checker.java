package controller;

import domain.Bill;
import domain.Store;
import repository.BillRepository;

import java.util.List;
import java.util.concurrent.Semaphore;

public class Checker implements Runnable {
    private BillRepository billRepository;
    private Store store;

    private Semaphore semaphore;

    public Checker(BillRepository billRepository, Store store, Semaphore semaphore) {
        this.billRepository = billRepository;
        this.store = store;
        this.semaphore = semaphore;
    }

    public void run() {
        try {
            semaphore.acquire();

            List<Bill> bills = billRepository.getAll();
            int moneyRegistered = store.getMoney();

            semaphore.release();

            int notGood = 0;
            int moneyCalculated = 0;

            for (Bill bill : bills) {
                moneyCalculated += bill.getTotalPrice();
            }

            if (moneyCalculated != moneyRegistered) {
                notGood += 1;
            }

            if (notGood > 0) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! FOUND " + notGood + " INCONSISTENCIES");
            } else {
                System.out.println("******************************************************EVERYTHING GOOD");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
