import java.util.Scanner;

public class Port {
    private static int port;
    public static void setport(){
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        port = Integer.parseInt(s);
        System.out.println(getport());
    }
    public static int getport(){
        return port;
    }
}
