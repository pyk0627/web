package com.shop.dao;

import com.shop.bean.Product;

import java.util.ArrayList;
import java.util.List;


public class ProductDao {

    private static final List<Product> PRODUCTS = new ArrayList<>();

    static {
        PRODUCTS.add(new Product(1, "机械键盘",   299.00, "87 键 青轴 白光背光"));
        PRODUCTS.add(new Product(2, "无线鼠标",    89.00, "2.4G 静音 可充电"));
        PRODUCTS.add(new Product(3, "27寸显示器", 1099.00, "2K 165Hz IPS 屏"));
        PRODUCTS.add(new Product(4, "笔记本电脑", 5499.00, "i7 / 16G / 512G SSD"));
        PRODUCTS.add(new Product(5, "USB-C 扩展坞", 159.00, "HDMI+USB3.0+读卡器"));
    }

    
    public List<Product> findAll() {
        return new ArrayList<>(PRODUCTS);
    }

    
    public Product findById(int id) {
        for (Product p : PRODUCTS) {
            if (p.getId() == id) return p;
        }
        return null;
    }
}