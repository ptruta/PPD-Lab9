package domain;

import java.util.HashMap;

public class Bill {
    private int id;
    private HashMap<Product, Integer> items;
    private float totalPrice;

    public Bill(HashMap<Product, Integer> details, float price) {
        this.items = details;
        this.totalPrice = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Product, Integer> getItems() {
        return items;
    }

    public void setItems(HashMap<Product, Integer> items) {
        this.items = items;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
