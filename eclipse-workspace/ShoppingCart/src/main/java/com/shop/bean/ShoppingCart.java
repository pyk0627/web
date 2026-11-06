package com.shop.bean;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Integer, CartItem> items = new LinkedHashMap<>();

    public ShoppingCart() {
    }


    public void addProduct(Product p) {
        addProduct(p, 1);
    }

    public void addProduct(Product p, int qty) {
        if (p == null || qty <= 0) return;
        CartItem item = items.get(p.getId());
        if (item == null) {
            items.put(p.getId(), new CartItem(p, qty));   
        } else {
            item.addQuantity(qty);                        
        }
    }

    
    public void removeProduct(int productId) {
        items.remove(productId);
    }

    public void updateQuantity(int productId, int qty) {
        CartItem item = items.get(productId);
        if (item == null) return;
        if (qty <= 0) items.remove(productId);
        else item.setQuantity(qty);
    }

    public void clear() {
        items.clear();
    }

    public Collection<CartItem> getItems() { return items.values(); }

    public int getSize() { return items.size(); }

    public boolean isEmpty() { return items.isEmpty(); }

    public int getTotalCount() {
        int n = 0;
        for (CartItem i : items.values()) n += i.getQuantity();
        return n;
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem i : items.values()) total += i.getSubtotal();
        return total;
    }
}