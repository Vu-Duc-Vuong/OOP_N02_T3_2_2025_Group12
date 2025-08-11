import java.util.Scanner;

public class Product {
    private String productId;         // Mã sản phẩm (chuỗi)
    private int sellerId;             // Mã người bán (giữ int nếu muốn)
    private String productName;       // Tên sản phẩm
    private double price;             // Giá sản phẩm
    private int stock;                // Số lượng sản phẩm
    private String description;       // Mô tả sản phẩm

    // Constructor
    public Product(){}

    public Product(String productId, int sellerId, String productName, double price, int stock, String description) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    // Getter và Setter
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Hiển thị thông tin sản phẩm
    public void displayProductInfo() {
        System.out.println("Ma san pham: " + productId);
        System.out.println("Ma nguoi ban: " + sellerId);
        System.out.println("Ten san pham: " + productName);
        System.out.println("Gia: " + price);
        System.out.println("So luong con lai: " + stock);
        System.out.println("Mo ta: " + description);
    }

    // Cập nhật số lượng sau bán
    public void updateStock(int quantitySold) {
        if (quantitySold <= stock) {
            stock -= quantitySold;
            System.out.println("Cap nhap so luong thanh cong. So luong con lai: " + stock);
        } else {
            System.out.println("So luong ban khong hop le. Khong du hang!");
        }
    }

    public void productInput() {
    Scanner sc = new Scanner(System.in);

    System.out.print("Nhap ma san pham: ");
    this.productId = sc.nextLine();

    System.out.print("Nhap seller ID: ");
    this.sellerId = Integer.parseInt(sc.nextLine());

    System.out.print("Nhap ten san pham: ");
    this.productName = sc.nextLine();

    System.out.print("Nhap gia san pham: ");
    this.price = Double.parseDouble(sc.nextLine());

    System.out.print("Nhap so luong ton kho: ");
    this.stock = Integer.parseInt(sc.nextLine());

    System.out.print("Nhap mo ta san pham: ");
    this.description = sc.nextLine();  
    }

}
