public class TestTime {
    public static void test() {
        try {
            Time t1 = new Time();
            Time t2 = new Time(20, 3, 45); // Có thể ném lỗi nếu constructor có kiểm tra

            t1.setHour(7).setMinute(32).setSecond(23);

            System.out.println("t1 is " + t1);
            System.out.println("t2 is " + t2);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Lỗi dữ liệu thời gian không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Đã xảy ra lỗi không xác định: " + e.getMessage());
        }
    }
}
