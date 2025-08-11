import java.util.Arrays;
import java.util.Scanner;

public class TestOrder {
    private static OrderList manager = new OrderList();

    private static void initSampleOrders() {
        manager = new OrderList(); // reset lại
        Order o1 = new Order("ORD001", "USER001", Arrays.asList(101, 102));
        o1.setTotalAmount(5000000);
        manager.addOrder(o1);

        Order o2 = new Order("ORD002", "USER002", Arrays.asList(103));
        o2.setTotalAmount(2500000);
        manager.addOrder(o2);
    }

    public static void test() {
        System.out.println("\n== TEST CREATE ORDER ==");

        try {
            initSampleOrders(); // đảm bảo luôn có dữ liệu
            manager.printAllOrders();
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo đơn hàng: " + e.getMessage());
        }
    }

    public static void testEdit(Scanner sc) {
        System.out.println("\n== TEST EDIT ORDER ==");
        try {
            initSampleOrders();

            System.out.print("Nhập mã đơn hàng cần sửa: ");
            String orderId = sc.nextLine().trim();

            System.out.print("Nhập trạng thái mới: ");
            String newStatus = sc.nextLine().trim();

            if (manager.editOrderStatus(orderId, newStatus)) {
                System.out.println(" Đã cập nhật trạng thái đơn hàng thành công.");
            } else {
                System.out.println("Không tìm thấy đơn hàng với ID: " + orderId);
            }

            manager.printAllOrders();
        } catch (Exception e) {
            System.out.println("Lỗi khi chỉnh sửa đơn hàng: " + e.getMessage());
        }
    }

    public static void testDelete(Scanner sc) {
        System.out.println("\n== TEST DELETE ORDER ==");

        try {
            initSampleOrders(); // đảm bảo có dữ liệu trước khi xóa

            System.out.print("Nhập mã đơn hàng cần xóa: ");
            String orderId = sc.nextLine().trim();

            if (manager.deleteOrder(orderId)) {
                System.out.println(" Đã xóa đơn hàng thành công.");
            } else {
                System.out.println(" Không tìm thấy đơn hàng với ID: " + orderId);
            }

            manager.printAllOrders();
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa đơn hàng: " + e.getMessage());
        }
    }


    public static void testOrderFromInput(Scanner sc) {
        ProductList plist = new ProductList();
        plist.addProduct(new Product("101", 1, "Laptop", 10000000, 10, "Máy tính xách tay"));
        plist.addProduct(new Product("102", 2, "Chuột", 200000, 5, "Chuột không dây"));

        manager.createOrderFromInput(plist);
    }

}
