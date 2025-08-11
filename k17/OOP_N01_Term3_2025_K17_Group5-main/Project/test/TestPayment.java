public class TestPayment {
    public static void main() {
        Payment payment = new Payment(2001, 3001, 500000, "Tiền mặt", "Chưa thanh toán", null);
        
        payment.displayPaymentInfo();
        payment.updateMethod("Ví điện tử");
        payment.processPayment();
        payment.displayPaymentInfo();
    }
}