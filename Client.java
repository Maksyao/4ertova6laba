import java.io.*;
import java.net.*;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        HashSet<String> is_console = new HashSet<>();
        System.out.println("              Добро пожаловать в приложение! \n" +
                           "Соединение будет установлено буквально с секунды на секунду.\n" +
                "*Для просмотра доступных команд введите help\n" );
        boolean first_time = true;
        boolean sended = true;
        try {

            Scanner in = new Scanner(System.in);
            String command="";
            while (true) {
                if (!first_time & sended)
                    command = in.nextLine();
                sended = false;
                SocketChannel outcoming = SocketChannel.open();

                InetSocketAddress  isa = new InetSocketAddress(args[0] , Integer.parseInt(args[1]));
                outcoming.connect(isa);
                if (first_time) {
                    System.out.println("Server reached");
                    System.out.println(">Start command reading");
                    first_time = false;
                    command = in.nextLine();
                }
                outcoming.socket().setSoTimeout(10000);
                try (ObjectOutputStream SendtoServer = new ObjectOutputStream(outcoming.socket().getOutputStream());
                     ObjectInputStream GetfromServer = new ObjectInputStream(outcoming.socket().getInputStream())
                ) {
                    try {
                        try {
                            if (!command.equals("exit")) {
                                if(!command.contains("execute_script ")) {
                                    ClientCommandReader.start_reading(GetfromServer, SendtoServer, command);
                                    outcoming.finishConnect();
                                }else {
                                    try {
                                        ClientCommandReader.start(is_console, GetfromServer, SendtoServer, command);
                                    }
                                    catch (Exception e){}
                                    is_console.clear();
                                }
                                outcoming.finishConnect();
                            } else {
                                System.out.println("I'll be back...");
                                outcoming.finishConnect();
                                break;
                            }
                        } catch (IOException e)
                        {
                            System.out.println("Come oooon. Let's wait zzzzzzzzzzzzzzz");
                            continue;
                        }
                        sended = true;

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }catch (Exception e)
                {
                    continue;
                }
            }
        }  catch (IOException e) {
            System.out.println("ЭРОР КОНЕКШН ИЗ ЛОСТ АНДЖЕЛЕС");
            System.out.println(e.getMessage());
        }
    }
}