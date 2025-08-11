import java.time.LocalDate;
import java.util.List;

public class Order {
    private String orderId;
    private String userId;
    private List<Integer> productIds;
    private double totalAmount;
    private LocalDate orderDate;
    private String status;

    public Order(String orderId, String userId, List<Integer> productIds) {
        try {
            this.orderId = orderId;
            this.userId = userId;
            if (productIds == null || productIds.isEmpty()) {
                throw new IllegalArgumentException("Danh sách sản phẩm không được rỗng.");
            }
            this.productIds = productIds;
            this.orderDate = LocalDate.now();
            this.status = "Chờ xác nhận";
            this.totalAmount = 0;
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi khi khởi tạo đơn hàng: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi không xác định khi tạo đơn hàng: " + e.getMessage());
        }
    }

    public void setTotalAmount(double amount) {
        try {
            if (amount < 0) {
                throw new IllegalArgumentException("Tổng tiền không được âm.");
            }
            this.totalAmount = amount;
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi không xác định khi cập nhật tổng tiền: " + e.getMessage());
        }
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
            System.out.println("Mã người dùng: " + userId);
            System.out.println("Danh sách mã sản phẩm: " + productIds);
            System.out.println("Tổng tiền: " + totalAmount + " VND");
            System.out.println("Ngày đặt hàng: " + orderDate);
            System.out.println("Trạng thái: " + status);
        } catch (Exception e) {
            System.out.println("Lỗi khi hiển thị đơn hàng: " + e.getMessage());
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }
}
