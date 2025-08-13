public class Customer extends User  // tính kế thừa
{
    public Customer(String userId, String name, String gender, String birthDate, String phoneNumber, 
            String email, String address, String password) 
    {
        super(userId, name, gender, birthDate, phoneNumber, email, address, password, "Khách hàng");
    }

    public void buy(String productName)  // mua sản phẩm
    {
        try {
            if (productName == null || productName.isEmpty()) {
                throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
            }

            System.out.println(getName() + " đã mua sản phẩm: " + productName);

        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi khi mua sản phẩm: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi không xác định khi mua sản phẩm.");
            e.printStackTrace(); // In chi tiết lỗi nếu cần debug
        }
    }
}
