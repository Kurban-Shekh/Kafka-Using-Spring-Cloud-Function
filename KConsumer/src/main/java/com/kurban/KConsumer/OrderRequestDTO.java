package com.kurban.KConsumer;

import java.time.Instant;

public class OrderRequestDTO {
    private int orderId;
    private int productId;
    private int price;
    private Instant timestamp;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "OrderRequestDTO{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", price=" + price +
                ", timestamp=" + timestamp +
                '}';
    }
}
