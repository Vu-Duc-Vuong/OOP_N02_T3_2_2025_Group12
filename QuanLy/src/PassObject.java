public class PassObject {
    static void f(Number m) {
m.i = 15;
}
public static void PO(){

Number n = new Number();
n.i =14;
PassObject.f(n);
System.out.println(n.i);

}

}