package com.example.servingwebcontent.model;
import java.time.LocalDate;

public class Order {
    private String orderId;
    private LocalDate orderDate;
    private String status;

    public Order(String orderId) {
        try {
            this.orderId = orderId;
            this.orderDate = LocalDate.now();
            this.status = "Pending";
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi khi khởi tạo đơn hàng: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi không xác định khi tạo đơn hàng: " + e.getMessage());
        }
    }

    public Order() {}

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public LocalDate getOrderDate() {
        return orderDate;
    }


    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    // Getter cho status
    public String getStatus() {
        return status;
    }

    // Setter cho status
    public void setStatus(String status) {
        this.status = status;
    }

    public void updateStatus(String newStatus) {
        try {
            this.status = newStatus;
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật trạng thái: " + e.getMessage());
        }
    }

    public void displayOrderInfo() {
        try {
            System.out.println("Mã đơn hàng: " + orderId);
            System.out.println("Ngày đặt hàng: " + orderDate);
            System.out.println("Trạng thái: " + status);
        } catch (Exception e) {
            System.out.println("Lỗi khi hiển thị đơn hàng: " + e.getMessage());
        }
    }



}
