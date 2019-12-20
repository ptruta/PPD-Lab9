package repository;

import domain.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private List<Product> products;

    public ProductRepository(List<Product> products) {
        this.products = products;
    }

    public ProductRepository() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product){
        this.products.add(product);
    }
    public List<Product> getAll(){
        return this.products;
    }

    public void updateProduct(Product product,int q){
        for (Product p:products){
            if (p.equals(product)){
                p.setQuantity(q);
            }
        }
    }

    public int getSize(){
        return this.products.size();
    }

}
