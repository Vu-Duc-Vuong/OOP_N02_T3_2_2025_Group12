import java.util.Scanner;
import java.util.ArrayList;

public class TestCustomerOrder {
    public static void test() {
        ListCustomerOrder orderList = new ListCustomerOrder();

        // Thêm vài đơn hàng mẫu
        orderList.addObject(new CustomerOrder("u01", "od001", true));
        orderList.addObject(new CustomerOrder("u01", "od002", false));
        orderList.addObject(new CustomerOrder("u02", "od003", true));

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Nhập User ID: ");
            String inputUserId = scanner.nextLine().trim();

            ArrayList<CustomerOrder> result = orderList.filterCustomerOrderByUserId(inputUserId);

            if (result.isEmpty()) {
                System.out.println("❌ Không tìm thấy đơn hàng cho user: " + inputUserId);
            } else {
                System.out.println("✅ Đơn hàng của user " + inputUserId + ":");
                for (CustomerOrder c : result) {
                    System.out.println("- Order ID: " + c.orderId);
                }
            }

            // Nếu muốn lọc thêm đơn hàng đã thanh toán
            /*
            ArrayList<CustomerOrder> paidOrders = orderList.getPaidOrdersByUser(inputUserId);
            if (paidOrders.isEmpty()) {
                System.out.println("❌ Không có đơn hàng đã thanh toán.");
            } else {
                System.out.println("✅ Đơn hàng đã thanh toán:");
                for (CustomerOrder c : paidOrders) {
                    System.out.println("- Order ID: " + c.orderId);
                }
            }
            */

        } catch (Exception e) {
            System.out.println("❌ Đã xảy ra lỗi khi xử lý: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
