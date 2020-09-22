import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;



public class Server {

    public static void main(String[] args) throws IOException {



        String env_name = "LAB_PROG_ENV_NAME";
        if (System.getenv(env_name) == null)
            System.out.println("Переменная окружения не задана. Для считывания файла добавьте переменную среды " + env_name);
        else {

            String file_name = System.getenv(env_name);

            System.out.println("\nСчитано: " + env_name + "   " + file_name);

            File file = new File(file_name);

            TreeSet<Organization> organizations = readFromFile(file);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String collection_creation_date = now.format(formatter);




            try {
              while(true) {
                  ServerSocket d = new ServerSocket(Integer.parseInt(args[0]));
                  try (Socket socket = d.accept()) {
                      try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                           ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                          ServerCommandReader reader = new ServerCommandReader(objectOutputStream, objectInputStream);


                          try {
                              reader.start_listening(organizations, collection_creation_date, file);
                          }
                          catch (Exception e)
                          {
                              System.out.println(e.getMessage());
                          }
                          d.close();
                      } catch (Exception e)
                      {
                          System.out.println("Unexpected termination with exception " + e.getMessage());
                      }
                  }
              }
            }
            catch (Exception e)
            {
                SaveToFile.saveToFile(organizations, file);
            }
        }
    }
    public static TreeSet<Organization> readFromFile(File file)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String text = new String();

            while((line=reader.readLine())!=null)  text+=line+'\n';

            TreeSet<Organization> organizations = new TreeSet<Organization>();

            JSONArray orgs = new JSONArray(text);


            for (int i=0; i<orgs.length(); i++)
            {
                JSONObject a = orgs.getJSONObject(i);

                String name = a.getString("name");

                long id = a.getLong("id");

                JSONObject c = a.getJSONObject("coordinates");
                Coordinates coords = new Coordinates(c.getLong("x"), c.getInt("y"));

                String creationDate = a.getString("creationDate");

                float annualTurnover = a.getFloat("annualTurnover");

                Integer employeesCount = a.getInt("employeesCount");

                OrganizationType type;
                if (a.isNull("type"))  type = null;
                else type = OrganizationType.valueOf(a.getString("type"));

                Address officialAddress;
                if (a.isNull("officialAddress")) officialAddress = null;
                else officialAddress = new Address(a.getString("officialAddress"));

                Organization organization = new Organization(name, coords, creationDate, annualTurnover, employeesCount, type, officialAddress);

                organizations.add(organization);
            }

            return organizations;
        }
        catch (Exception e)
        {
            System.out.println("Беда с файлом. Создана пустая коллекция");
            System.out.println(e.getMessage());
            return new TreeSet<Organization>();
        }
    }
}
