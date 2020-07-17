package com.gpc.salestaxes.model;

import java.math.BigDecimal;

public class LineItem {

    private int count;
    private String itemName;
    private BigDecimal itemPrice;
    private BigDecimal itemTaxRate;
    private Goods goodsType;

    public LineItem(int count, String itemName, BigDecimal itemPrice, BigDecimal itemTaxRate, Goods goodsType) {
        this.count = count;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemTaxRate = itemTaxRate;
        this.goodsType = goodsType;
    }

    public LineItem() {
        super();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getItemTaxRate() {
        return itemTaxRate;
    }

    public void setItemTaxRate(BigDecimal itemTaxRate) {
        this.itemTaxRate = itemTaxRate;
    }

    public Goods getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Goods goodsType) {
        this.goodsType = goodsType;
    }


    @Override
    public String toString() {
        return "LineItem{" +
                "count=" + count +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemTaxRate=" + itemTaxRate +
                ", goodsType=" + goodsType +
                '}';
    }
}
