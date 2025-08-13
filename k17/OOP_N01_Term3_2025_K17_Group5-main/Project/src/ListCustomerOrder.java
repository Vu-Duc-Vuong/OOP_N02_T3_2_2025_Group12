import java.util.*;

public class ListCustomerOrder {
    ArrayList<CustomerOrder> co = new ArrayList<CustomerOrder>();

    public ArrayList<CustomerOrder> addObject(CustomerOrder c) {
        try {
            if (c == null) {
                throw new IllegalArgumentException("Đối tượng CustomerOrder không được null.");
            }
            co.add(c);
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm CustomerOrder: " + e.getMessage());
        }
        return co;
    }

    public void displayList() {
        try {
            if (co.isEmpty()) {
                System.out.println("Danh sách đơn hàng trống.");
                return;
            }

            for (int i = 0; i < co.size(); i++) {
                CustomerOrder order = co.get(i);
                System.out.println("User ID: " + order.userId);
                System.out.println("Order ID: " + order.orderId);
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi hiển thị danh sách đơn hàng: " + e.getMessage());
        }
    }

    public static void displayList(List<CustomerOrder> co) {
        try {
            if (co == null || co.isEmpty()) {
                System.out.println("Danh sách đầu vào trống hoặc null.");
                return;
            }

            for (int i = 0; i < co.size(); i++) {
                CustomerOrder order = co.get(i);
                System.out.println("User ID: " + order.userId);
                System.out.println("Order ID: " + order.orderId);
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi hiển thị danh sách tĩnh: " + e.getMessage());
        }
    }

    public void shortList() {
        try {
            if (co.size() < 2) {
                System.out.println("Không đủ đơn hàng để tạo danh sách ngắn (cần ít nhất 2).");
                return;
            }

            List<CustomerOrder> list = co.subList(0, 2);
            ListCustomerOrder.displayList(list);

        } catch (Exception e) {
            System.err.println("Lỗi khi tạo shortList: " + e.getMessage());
        }
    }

    public ArrayList<CustomerOrder> filterCustomerOrderByUserId(String userId) {
        ArrayList<CustomerOrder> newList = new ArrayList<>();
        try {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("User ID không hợp lệ.");
            }

            for (CustomerOrder c : co) {
                if (c.userId != null && c.userId.equals(userId)) {
                    newList.add(c);
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi lọc đơn hàng theo userId: " + e.getMessage());
        }

        return newList;
    }

    public ArrayList<CustomerOrder> getPaidOrdersByUser(String userId) {
        ArrayList<CustomerOrder> result = new ArrayList<>();
        try {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("User ID không hợp lệ.");
            }

            for (CustomerOrder c : co) {
                if (c.userId != null && c.userId.equals(userId) && c.isPaid) {
                    result.add(c);
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi lọc đơn hàng đã thanh toán: " + e.getMessage());
        }

        return result;
    }
}
