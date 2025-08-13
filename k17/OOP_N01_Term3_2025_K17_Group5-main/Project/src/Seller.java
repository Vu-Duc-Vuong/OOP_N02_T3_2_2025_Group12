public class Seller extends User 
{
    public Seller(String userId, String name, String gender, String birthDate, String phoneNumber, 
                  String email, String address, String password) 
    {
        super(userId, name, gender, birthDate, phoneNumber, email, address, password, "Nhà cung cấp");
    }

    public void sell(String productName)  // bán sản phẩm
    {
        try {
            if (productName == null || productName.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
            }
            System.out.println(getName() + " đã đăng bán sản phẩm: " + productName);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Đã xảy ra lỗi không xác định khi bán sản phẩm: " + e.getMessage());
        }
    }
}
