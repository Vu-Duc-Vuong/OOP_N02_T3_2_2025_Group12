public class TestUser {
    public static void test() {
    User u = new User("vuong", "123456", "Vu Duc Vuong", "vuong.vuduc@gmail.com");

        System.out.println("Username: " + u.getUser());
        System.out.println("Password: " + u.getPass());
        System.out.println("Full name: " + u.getFullN());
        System.out.println("Email: " + u.getEmail());

    }
}

