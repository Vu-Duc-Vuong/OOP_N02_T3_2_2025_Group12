package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BanDAO {
    public static ArrayList<Ban> getAll() throws SQLException {
        ArrayList<Ban> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Ban");
            while (rs.next()) {
                Ban b = new Ban(
                    rs.getString("maPhieu"),
                    new HangHoa(
                        rs.getString("maHang"),
                        rs.getString("tenHang"),
                        0, // soLuong không lấy từ phiếu bán
                        "", // nhaSanXuat không có trong bảng Ban
                        rs.getDouble("donGia")
                    ),
                    rs.getInt("soLuong")
                );
                b.tenKhach = rs.getString("tenKhach");
                b.ngayBan = rs.getDate("ngayBan").toLocalDate();
                b.thoiGianBan = rs.getTimestamp("thoiGianBan").toLocalDateTime();
                list.add(b);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        return list;
    }
    private static final String URL = "jdbc:mysql://mysql-1ebf9202-st-4624.g.aivencloud.com:22804/defaultdb?ssl-mode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_74m7tZ-DYDSYFsG0TZy";

    public static void save(Ban ban) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "INSERT INTO Ban (maPhieu, maHang, tenHang, tenKhach, soLuong, donGia, ngayBan, thoiGianBan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, ban.getMaPhieu());
            stmt.setString(2, ban.getMaHang());
            stmt.setString(3, ban.getTenHang());
            stmt.setString(4, ban.getTenKhach());
            stmt.setInt(5, ban.getSoLuong());
            stmt.setDouble(6, ban.getDonGia());
            stmt.setDate(7, java.sql.Date.valueOf(ban.getNgayBan()));
            stmt.setTimestamp(8, java.sql.Timestamp.valueOf(ban.getThoiGianBan()));
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
}
