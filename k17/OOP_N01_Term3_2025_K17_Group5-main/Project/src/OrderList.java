import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class OrderList {

    private final ArrayList<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        try {
            if (order == null) {
                throw new IllegalArgumentException("Đơn hàng không được null.");
            }
            orders.add(order);
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm đơn hàng: " + e.getMessage());
        }
    }

    public void printAllOrders() {
        try {
            if (orders.isEmpty()) {
                System.out.println("Danh sách đơn hàng trống.");
                return;
            }

            for (Order order : orders) {
                order.displayOrderInfo();
                System.out.println("---------------");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi hiển thị danh sách đơn hàng: " + e.getMessage());
        }
    }

    public boolean editOrderStatus(String orderId, String newStatus) {
        try {
            for (Order order : orders) {
                if (order.getOrderId().equals(orderId)) {
                    order.updateStatus(newStatus);
                    return true;
                }
            }
            System.out.println("Không tìm thấy đơn hàng với mã: " + orderId);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật trạng thái đơn hàng: " + e.getMessage());
        }
        return false;
    }


    public boolean deleteOrder(String orderId) {
        try {
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getOrderId().equals(orderId)) {
                    orders.remove(i);
                    return true;
                }
            }
            System.out.println("Không tìm thấy đơn hàng để xóa: " + orderId);
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa đơn hàng: " + e.getMessage());
        }
        return false;
    }

    public ArrayList<Order> getAllOrders() {
        return orders;
    }
    
    public void createOrderFromInput(ProductList productList) {
    Scanner sc = new Scanner(System.in);
    try {
        System.out.println("\n== Nhập thông tin đơn hàng mới ==");

        System.out.print("Nhập mã đơn hàng: ");
        String orderId = sc.nextLine().trim();

        System.out.print("Nhập mã người dùng: ");
        String userId = sc.nextLine().trim();

        List<Integer> productCodes = new ArrayList<>();
        double total = 0;

        ArrayList<Product> products = productList.st; // hoặc productList.getAll()
        if (products.isEmpty()) {
            System.out.println(" Danh sách sản phẩm trống.");
            return;
        }

        while (true) {
            System.out.println("\nDanh sách sản phẩm:");
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                System.out.println((i + 1) + ". " + p.getProductName() + " - " + p.getPrice() + " VND (Còn: " + p.getStock() + ")");
            }

            System.out.print("Chọn số thứ tự sản phẩm muốn mua (0 để kết thúc): ");
            int index = Integer.parseInt(sc.nextLine()) - 1;
            if (index == -1) break;

            if (index < 0 || index >= products.size()) {
                System.out.println(" Lựa chọn không hợp lệ.");
                continue;
            }

            Product selected = products.get(index);

            System.out.print("Nhập số lượng: ");
            int qty = Integer.parseInt(sc.nextLine());

            if (qty <= 0 || qty > selected.getStock()) {
                System.out.println(" Số lượng không hợp lệ hoặc không đủ hàng.");
                continue;
            }

            selected.updateStock(qty);
            for (int i = 0; i < qty; i++) {
                productCodes.add(Integer.parseInt(selected.getProductId()));
            }
            total += qty * selected.getPrice();
        }

        if (productCodes.isEmpty()) {
            System.out.println(" Bạn chưa chọn sản phẩm nào.");
            return;
        }

        Order newOrder = new Order(orderId, userId, productCodes);
        newOrder.setTotalAmount(total);
        this.addOrder(newOrder);

        System.out.println(" Đơn hàng đã được tạo thành công:");
        newOrder.displayOrderInfo();

    } catch (Exception e) {
        System.out.println(" Lỗi khi tạo đơn hàng: " + e.getMessage());
    }
}



}

