import java.util.ArrayList;
import java.util.List;

public class UserList {

    ArrayList<User> st = new ArrayList<User>();

    public ArrayList<User> addUser(User stu) {
        try {
            if (stu != null) {
                st.add(stu);
            } else {
                System.out.println("❌ Người dùng rỗng, không thể thêm.");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi thêm người dùng: " + e.getMessage());
        }
        return st;
    }

    public ArrayList<User> getEditUser(String name, String userId) {
        try {
            for (int i = 0; i < st.size(); i++) {
                if (st.get(i).getUserId().equals(userId)) {
                    st.get(i).setName(name);
                    System.out.println("✅ Đã cập nhật tên cho người dùng có ID: " + userId);
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi chỉnh sửa người dùng: " + e.getMessage());
        }
        return st;
    }

    public ArrayList<User> getDeleteUser(String userId) {
        try {
            for (int i = 0; i < st.size(); i++) {
                if (st.get(i).getUserId().equals(userId)) {
                    st.remove(i);
                    System.out.println("✅ Đã xóa người dùng có ID: " + userId);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi xóa người dùng: " + e.getMessage());
        }
        return st;
    }

    public void printUserList() {
        try {
            int len = st.size();
            if (len == 0) {
                System.out.println("📭 Danh sách người dùng trống.");
                return;
            }

            for (int i = 0; i < len; i++) {
                System.out.println("User ID: " + st.get(i).getUserId());
                System.out.println("Fullname: " + st.get(i).getName());
                System.out.println("Gender: " + st.get(i).getGender());
                System.out.println("Birth Date: " + st.get(i).getBirthDate());
                System.out.println("Phone Number: " + st.get(i).getPhoneNumber());
                System.out.println("Email: " + st.get(i).getEmail());
                System.out.println("Address: " + st.get(i).getAddress());
                System.out.println("Password: " + st.get(i).getPassword());
                System.out.println("User Type: " + st.get(i).getUserType());
                System.out.println("-----------------------------");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi hiển thị danh sách người dùng: " + e.getMessage());
        }
    }

    public static void displayList(List<User> co) {
        try {
            for (User user : co) {
                System.out.println("userName: " + user.getName());
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi hiển thị danh sách tên người dùng: " + e.getMessage());
        }
    }

    // Truyền thống: tìm theo userId chứa chuỗi con
    public ArrayList<User> searchUserNameTradition(String userId) {
        ArrayList<User> newList = new ArrayList<>();
        try {
            for (User stu : st) {
                if (stu.getUserId().contains(userId)) {
                    newList.add(stu);
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi tìm kiếm người dùng: " + e.getMessage());
        }
        return newList;
    }
}
