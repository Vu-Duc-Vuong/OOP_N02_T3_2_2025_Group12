package com.example.servingwebcontent.model;
import java.util.Scanner;
import java.util.regex.Pattern;

public class User 
{
    private String userId;
    private String name;
    private Gender gender;
    private String birthDate;
    private String phoneNumber;
    private String email;
    private String address;
    private String password;
    private UserType userType;

    public enum Gender {
        Male, Female, Other
    }

    public enum UserType {
        Customer, Seller
    }

    public User(){}

    public User(String userId, String name, Gender gender, String birthDate, String phoneNumber, 
            String email, String address, String password, UserType userType)
    {
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.password = password;
        this.userType = userType;
    }


    public String getUserId() { return userId; }
    public String getName() { return name; }
    public Gender getGender() { return gender; }
    public String getBirthDate() { return birthDate; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getPassword() { return password; }
    public UserType getUserType() { return userType; }
 
    public void setUser(String userId, String name, Gender gender, String birthDate, String phoneNumber, 
                            String email, String address, UserType userType) 
    { 
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.userType = userType; 
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name){
        this.name = name;

    } 

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthDate(String birthDate){
        this.birthDate = birthDate;
    } 

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void printUserName(User u){
        System.out.println("Submited Name:");
        System.out.println(u.name);
    }

    public boolean login(String loginId, String password)        //đăng nhập
    {
        return (loginId.equals(this.phoneNumber) || loginId.equals(this.email)) && password.equals(this.password);
    }

    public void showUser()          //hiển thị thông tin user
    {
        System.out.println("Mã người dùng: " + userId);
        System.out.println("Tên: " + name);
        System.out.println("Giới tính: " + gender);
        System.out.println("Ngày sinh: " + birthDate);
        System.out.println("SĐT: " + phoneNumber);
        System.out.println("Email: " + email);
        System.out.println("Địa chỉ: " + address);
        System.out.println("Loại người dùng: " + userType);
    }

    public void registerUser() 
    {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Nhập mã người dùng: ");
            userId = sc.nextLine();

            System.out.print("Nhập tên: ");
            name = sc.nextLine();

            // --- Giới tính ---
            while (true) {
                System.out.print("Nhập giới tính (Male/Female/Other): ");
                try {
                    gender = Gender.valueOf(sc.nextLine().trim());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("❌ Giới tính không hợp lệ! Vui lòng nhập lại.");
                }
            }

            System.out.print("Nhập ngày sinh (dd/mm/yyyy): ");
            birthDate = sc.nextLine();

            System.out.print("Nhập số điện thoại: ");
            phoneNumber = sc.nextLine();

            // --- Email ---
            while (true) {
                System.out.print("Nhập email: ");
                email = sc.nextLine().trim();
                if (isValidEmail(email)) {
                    break;
                } else {
                    System.out.println("❌ Email không hợp lệ! Vui lòng nhập đúng định dạng.");
                }
            }

            System.out.print("Nhập địa chỉ: ");
            address = sc.nextLine();

            System.out.print("Nhập mật khẩu: ");
            password = sc.nextLine();

            // --- Loại người dùng ---
            while (true) {
                System.out.print("Nhập loại người dùng (Customer/Seller): ");
                try {
                    userType = UserType.valueOf(sc.nextLine().trim());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("❌ Loại người dùng không hợp lệ! Vui lòng nhập lại.");
                }
            }

            System.out.println("\n✅ Đăng ký người dùng thành công!");

        } catch (Exception e) {
            System.out.println("❌ Đã xảy ra lỗi: " + e.getMessage());
        }

        sc.close();
    }

    private boolean isValidEmail(String email) {
        // Regex đơn giản kiểm tra định dạng email
        return Pattern.matches("^[\\w.-]+@gmail\\.com$", email);
    }
}
