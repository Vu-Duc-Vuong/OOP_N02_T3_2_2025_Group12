public class Time {
    private int H;
    private int P;
    private int S;

    // constructor
    public Time() {
        setTime(0, 0, 0);
    }

    public Time(int h) {
        setTime(h, 0, 0);
    }

    public Time(int h, int p) {
        setTime(h, p, 0);
    }

    public Time(int h, int p, int s) {
        setTime(h, p, s);
    }

    // gắn time
    public Time setTime(int h, int p, int s) {
        setH(h);
        setP(p);
        setS(s);
        return this;
    }

    public Time setH(int h) {
        if (h >= 0 && h < 24) {
            H = h;
        } else {
            H = 0;
        }
        return this;
    }

    public Time setP(int p) {
        if (p >= 0 && p < 60) {
            P = p;
        } else {
            P = 0;
        }
        return this;
    }

    public Time setS(int s) {
        if (s >= 0 && s < 60) {
            S = s;
        } else {
            P = 0;
        }
        return this;
    }

    public int  getH(){
        return H;
    }
    public int  getP(){
        return P;
    }
    public int  getS(){
        return S;
    }



    
   public String hienThi() {
    String h;
    if (this.H < 10) {
        h = "0" + this.H;
    } else {
        h = "" + this.H;
    }

    String p;
    if (this.P < 10) {
        p = "0" + this.P;
    } else {
        p = "" + this.P;
    }

    String s;
    if (this.S < 10) {
        s = "0" + this.S;
    } else {
        s = "" + this.S;
    }

    return h + ":" + p + ":" + s;
}


}