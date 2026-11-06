package com.shop.bean;

import java.io.Serializable;

public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Product product;   
    private int quantity;      

    public CartItem() {
    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity <= 0 ? 1 : quantity;
    }

    public void addQuantity(int n) {
        this.quantity += n;
    }

    public double getSubtotal() {
        return product == null ? 0 : product.getPrice() * quantity;
    }
}