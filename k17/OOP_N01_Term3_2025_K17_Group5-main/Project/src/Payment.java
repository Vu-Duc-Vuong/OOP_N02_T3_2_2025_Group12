

import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private int orderId;
    private double amount;
    private String method;
    private String status;
    private LocalDate paymentDate;

    public Payment(int paymentId, int orderId, double amount, String method, String status, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    public void processPayment() {
        this.status = "Đã thanh toán";
        this.paymentDate = LocalDate.now();
        System.out.println("Thanh toán thành công.");
    }

    public void displayPaymentInfo() {
        System.out.println("Mã thanh toán: " + paymentId);
        System.out.println("Mã đơn hàng: " + orderId);
        System.out.println("Số tiền: " + amount);
        System.out.println("Phương thức: " + method);
        System.out.println("Trạng thái: " + status);
        System.out.println("Ngày thanh toán: " + paymentDate);
    }

    public void updateMethod(String newMethod) {
        this.method = newMethod;
        System.out.println("Cập nhật phương thức thanh toán thành công.");
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
        System.out.println("Cập nhật trạng thái thanh toán thành công.");
    }
}