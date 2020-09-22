import java.io.*;
import java.util.HashSet;

public class ClientCommandReader {

    public static void start_reading(ObjectInputStream fromServer, ObjectOutputStream toServer, String command) throws IOException, ClassNotFoundException {

        ClientCommandHub hub = new ClientCommandHub(fromServer, toServer);

        command = command.toLowerCase();
        String[] commandParts;
        commandParts = command.split(" ", 2);

        switch (commandParts[0]) {
            case "":
                break;
            case "help":
                hub.help(command);
                break;
            case "info":
                hub.info();
                break;
            case "show":
                hub.show();
                break;
            case "add":
                hub.add();
                break;
            case "update_by_id":
                hub.update_by_id(Long.parseLong(commandParts[1]));
                break;
            case "remove_by_id":
                hub.remove_by_id(Long.parseLong(commandParts[1]));
                break;
            case "clear":
                hub.clear();
                break;
            case "add_if_min":
                hub.add_if_min();
                break;
            case "remove_greater":
                try {
                    hub.remove_greater(commandParts[1]);
                } catch (Exception e) {
                    System.out.println("Попробуйте еще раз");
                }
                break;
            case "remove_lower":
                try {
                    hub.remove_lower(commandParts[1]);
                } catch (Exception e) {
                    System.out.println("Попробуйте еще раз");
                }
                break;
            case "count_greater_than_type":
                try {
                    hub.count_greater_than_type(commandParts[1]);
                } catch (Exception e) {
                    System.out.println("Попробуйте еще раз");
                }
                break;
            case "print_descending":
                hub.print_descending();
                break;
            case "print_field_ascending_official_address":
                hub.print_field_ascending_official_address();
                break;
            default:
                System.out.println('"' + command + "\" не является командой. Используйте help, чтобы узнать список доступных команд.");
                break;
        }
        System.out.print('>');
    }

    public static void start(HashSet<String> is_console, ObjectInputStream fromServer, ObjectOutputStream toServer, String com) throws IOException, ClassNotFoundException {

        ClientCommandHub hub = new ClientCommandHub(fromServer, toServer);
        Boolean is_ok = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] Path = com.split(" ");
        try {
            FileReader fr = new FileReader(Path[1]);
            reader = new BufferedReader(fr);
        } catch (Exception e) {
            System.out.println("Проблема с файлом");
            is_ok = false;
        }
        if (is_console.contains(Path[1])) {
            is_ok = false;
            System.out.println("Вы попытались вызвать из скрипта тот же самый скрипт");
        }
        is_console.add(Path[1]);
        if (is_ok) {
            String command = reader.readLine().toLowerCase().trim();
            String[] commandParts;

            while (!command.equals("exit") && (reader.ready())) {
                commandParts = command.split(" ", 3);
                switch (commandParts[0]) {
                    case "":
                        break;
                    case"execute_script":
                        start(is_console,fromServer, toServer, command);
                        break;
                    case "help":
                        hub.help(command);
                        break;
                    case "info":
                        hub.info();
                        break;
                    case "show":
                        hub.show();
                        break;
                    case "add":
                        hub.add(reader);
                        break;
                    case "update_by_id":
                        hub.update_by_id(reader,Long.parseLong(commandParts[1]));
                        break;
                    case "remove_by_id":
                        hub.remove_by_id(Long.parseLong(commandParts[1]));
                        break;
                    case "clear":
                        hub.clear();
                        break;
                    case "add_if_min":
                        hub.add_if_min(reader);
                        break;
                    case "remove_greater":
                        try {
                            hub.remove_greater(commandParts[1]);
                        } catch (Exception e) {
                            System.out.println("Попробуйте еще раз");
                        }
                        break;
                    case "remove_lower":
                        try {
                            hub.remove_lower(commandParts[1]);
                        } catch (Exception e) {
                            System.out.println("Попробуйте еще раз");
                        }
                        break;
                    case "count_greater_than_type":
                        try {
                            hub.count_greater_than_type(commandParts[1]);
                        } catch (Exception e) {
                            System.out.println("Попробуйте еще раз");
                        }
                        break;
                    case "print_descending":
                        hub.print_descending();
                        break;
                    case "print_field_ascending_official_address":
                        hub.print_field_ascending_official_address();
                        break;
                    default:
                        System.out.println('"' + command + "\" не является командой. Используйте help, чтобы узнать список доступных команд.");
                        break;
                }
                System.out.print('>');
            }
        }
    }
}