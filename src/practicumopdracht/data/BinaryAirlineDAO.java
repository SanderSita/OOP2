package practicumopdracht.data;
import practicumopdracht.models.Airline;
import java.io.*;
import static practicumopdracht.MainApplication.getAirlineDAO;

public class BinaryAirlineDAO extends AirlineDAO{
    private static final String FILENAME = "binaryairline.txt";
    File file = new File(FILENAME);
    @Override
    public boolean save() throws FileNotFoundException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
            int size = getAirlineDAO().airlinesList.size();
            out.writeInt(size);

            for (Airline airline : getAirlineDAO().airlinesList) {
                out.writeUTF(airline.getName());
                out.writeUTF(airline.getLocation());
                out.writeDouble(airline.getRating());
                out.writeInt(airline.getEmployeeCount());
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Bestand aanmaken mislukt.");
        }

        return true;
    }

    @Override
    public boolean load() throws FileNotFoundException {
        getAirlineDAO().airlinesList.clear();
        try (DataInputStream inp = new DataInputStream(new FileInputStream(file))) {
            int size = inp.readInt();
            for (int i = 0; i < size; i++) {
                String name = inp.readUTF();
                String location = inp.readUTF();
                double rating = inp.readDouble();
                int count = inp.readInt();
                Airline newAirline = new Airline(name, location, rating, count);
                getAirlineDAO().addOrUpdate(newAirline);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("File is empty");
        }

        return true;
    }
}
