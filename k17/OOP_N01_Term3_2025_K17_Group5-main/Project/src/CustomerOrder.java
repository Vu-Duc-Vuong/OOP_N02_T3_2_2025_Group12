public class CustomerOrder {
    public String userId;
    public String orderId;
    public boolean isPaid;

    public CustomerOrder(String userId, String orderId, boolean isPaid) {
        this.userId = userId;
        this.orderId = orderId;
        this.isPaid = isPaid;
    }

    public void displayCustomerOrder() {
        try {
            if (userId == null || orderId == null) {
                throw new NullPointerException("User ID hoặc Order ID bị null.");
            }

            System.out.println("User ID: " + userId);
            System.out.println("Order ID: " + orderId);
            System.out.println("Trạng thái thanh toán: " + (isPaid ? "Đã thanh toán" : "Chưa thanh toán"));

        } catch (NullPointerException e) {
            System.err.println("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định khi hiển thị đơn hàng.");
            e.printStackTrace(); // In stack trace nếu cần debug
        }
    }
}
