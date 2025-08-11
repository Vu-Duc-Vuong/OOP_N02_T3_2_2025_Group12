public class NeverCaught {
    static void g() {
        throw new RuntimeException("From g()");  // Line 5
    }

    static void f() {
        g();  // Line 8
    }

    public static void main(String[] args) {
        f();  // Line 11
    }
}
