public class CakeCase {
    public static void Cc(){
        for(int i=0;i<9;i++){
        char c = (char) (Math.random() * 26 +'a');
        switch(c){
            case 'u':
            case 'e':
            case 'o':
            case 'a':
            case 'i':
            System.out.println("Nguyen am");break;
            case 'w':
            case 'y':
            System.out.println("Ban nguyen");break;
            default:
            System.out.println("Khong phai nguyen am");break;
        }
        }
    }
}

