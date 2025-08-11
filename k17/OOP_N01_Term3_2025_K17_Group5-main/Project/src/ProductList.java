import java.util.ArrayList;

public class ProductList {
    ArrayList<Product> st = new ArrayList<Product>();

    // Thêm sản phẩm
    public ArrayList<Product> addProduct(Product p) {
        st.add(p);
        return st;
    }

    // Sửa tên sản phẩm dựa theo productId
    public ArrayList<Product> getEditProduct(String newName, String productId) {
        boolean found = false;
        for (Product p : st) {
            if (p.getProductId().equals(productId)) {
                System.out.println("Sản phẩm tìm thấy, cập nhật tên.");
                p.setProductName(newName);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Khong tim thay san pham voi ID: " + productId);
        }
        return st;
    }

    // Xóa sản phẩm theo productId
    public ArrayList<Product> getDeleteProduct(String productId) {
        boolean found = false;
        for (int i = 0; i < st.size(); i++) {
            if (st.get(i).getProductId().equals(productId)) {
                st.remove(i);
                System.out.println("Đa xoa san pham co ID: " + productId);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Khong tim thay san pham đe xoa voi ID: " + productId);
        }
        return st;
    }

    // In danh sách sản phẩm
    public void printProductList() {
        System.out.println("Danh sach san pham:");
        int len = st.size();
        for (int i = 0; i < len; i++) {
            System.out.println("Product ID: " + st.get(i).getProductId() + 
                               " | Name: " + st.get(i).getProductName() +
                               " | Price: " + st.get(i).getPrice() +
                               " | Stock: " + st.get(i).getStock() +
                               " | Description: " + st.get(i).getDescription());
        }
    }
}
