package com.kafka.core;
import java.math.BigDecimal;



public class OrderCreatedEvent {

        private String orderId;
        private String title;
        private BigDecimal price;
        private Integer quantity;

        public OrderCreatedEvent(String orderId, String title, BigDecimal price, Integer quantity) {
                this.orderId = orderId;
                this.title = title;
                this.price = price;
                this.quantity = quantity;
        }

        public OrderCreatedEvent() {
        }

        public String getOrderId() {
                return orderId;
        }

        public void setOrderId(String orderId) {
                this.orderId = orderId;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public BigDecimal getPrice() {
                return price;
        }

        public void setPrice(BigDecimal price) {
                this.price = price;
        }

        public Integer getQuantity() {
                return quantity;
        }

        public void setQuantity(Integer quantity) {
                this.quantity = quantity;
        }

}
