package repository;

import domain.Bill;
import domain.Product;

import java.util.ArrayList;
import java.util.List;

public class BillRepository {
    List<Bill> bills;

    public BillRepository() {
        this.bills=new ArrayList<>();
    }

    public void addBill(Bill bill){
        this.bills.add(bill);

    }
    public List<Bill> getAll(){
        return this.bills;
    }
}
