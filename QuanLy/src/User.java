public class User {
    private String username;
    private String password;
    private String fullname;
    private String email;

    // gán giá trị cho đối tượng :
    public User(String username, String password, String fullname, String email) {
        this.username = username; // trỏ tới tên
        this.password = password;
        this.fullname = fullname;
        this.email = email;
    }

    // get: lấy giá trị, set :đặt cho biến lấy.(do private)
    public String getUser() {
        return username;
    }

    public void setuser(String username) {
        this.username = username;
    }

    public String getPass() {
        return password;
    }

    public void setPass(String username) {
        this.password = password;
    }

    public String getFullN() {
        return fullname;
    }

    public void setFullN(String username) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = email;
    }
}
