package com.shop.bean;

import java.io.Serializable;
import java.sql.Date;

/** 商品实体 JavaBean：与数据库 product 表一一对应 */
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;            // 商品编号
    private String name;       // 商品名称
    private int stock;         // 商品存货
    private double price;      // 商品价格
    private Date produceDate;  // 生产日期（java.sql.Date，对应 DATE 类型）

    public Product() { }

    public Product(String name, int stock, double price, Date produceDate) {
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.produceDate = produceDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Date getProduceDate() { return produceDate; }
    public void setProduceDate(Date produceDate) { this.produceDate = produceDate; }
}