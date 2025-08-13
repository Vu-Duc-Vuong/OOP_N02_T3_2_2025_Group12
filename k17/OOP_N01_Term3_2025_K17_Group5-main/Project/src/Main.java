public class Main {
   public static void main(String[] args) {

    try {
        TestPayment.main();
    } catch (Exception e) {
        System.err.println("Lỗi ở TestPayment.main(): " + e.getMessage());
    }

    try {
        TestTime.test();
    } catch (Exception e) {
        System.err.println("Lỗi ở TestTime.test(): " + e.getMessage());
    }

    try {
        TestUser.test();
    } catch (Exception e) {
        System.err.println("Lỗi ở TestUser.test(): " + e.getMessage());
    }

    try {
        TestUser us = new TestUser();
        us.testEdit();
    } catch (Exception e) {
        System.err.println("Lỗi ở us.testEdit(): " + e.getMessage());
    }

    try {
        TestUser.testRegisterUser();
    } catch (Exception e) {
        System.err.println("Lỗi ở TestUser.testRegisterUser(): " + e.getMessage());
    }

    try {
        TestProduct.testProductInput();
    } catch (Exception e) {
        System.err.println("Lỗi ở TestProduct.testProductInput(): " + e.getMessage());
    }

    try {
        TestOrder.testOrderFromInput(null);
    } catch (Exception e) {
        System.err.println("Lỗi ở TestOrder.testOrderFromInput(): " + e.getMessage());
    }

    try {
        Menu.showMenu();
    } catch (Exception e) {
        System.err.println("Lỗi ở Menu.showMenu(): " + e.getMessage());
    }

    try {
        TestSequence.test();
    } catch (Exception e) {
        System.err.println("Lỗi ở TestSequence.test(): " + e.getMessage());
    }

    try {
        TestCustomerOrder.test();
    } catch (Exception e) {
        System.err.println("Lỗi ở TestCustomerOrder.test(): " + e.getMessage());
    }
}

}