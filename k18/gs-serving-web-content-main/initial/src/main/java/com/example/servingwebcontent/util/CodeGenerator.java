package com.example.servingwebcontent.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sinh mã dạng PREFIX + số tăng dần: HH1, HH2; N1, N2; B1, B2...
 * Dùng trong bộ nhớ (không đảm bảo an toàn đa tiến trình / cluster).
 * Có thể thay bằng sequence DB sau này.
 */
public class CodeGenerator {
    private static final AtomicInteger HH_SEQ = new AtomicInteger(0);
    private static final AtomicInteger N_SEQ = new AtomicInteger(0);
    private static final AtomicInteger B_SEQ = new AtomicInteger(0);

    public static String nextHangHoa(){ return "HH" + HH_SEQ.incrementAndGet(); }
    public static String nextNhap(){ return "N" + N_SEQ.incrementAndGet(); }
    public static String nextBan(){ return "B" + B_SEQ.incrementAndGet(); }

    // Cho phép set seed nếu muốn đồng bộ với dữ liệu có sẵn
    public static void initHangHoa(int currentMax){ HH_SEQ.set(currentMax); }
    public static void initNhap(int currentMax){ N_SEQ.set(currentMax); }
    public static void initBan(int currentMax){ B_SEQ.set(currentMax); }
}
