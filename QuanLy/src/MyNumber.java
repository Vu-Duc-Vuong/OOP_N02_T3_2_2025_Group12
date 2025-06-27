public class MyNumber {
    public int i;

public static void Num(){
    MyNumber n1 = new MyNumber();
    MyNumber n2 = new MyNumber();
    n1.i=2;
    n2.i=4;
    n1=n2;
    n2.i=5;
    System.out.println(n2.i);
    n1.i=10;
    System.out.println(n1.i);

}
}
