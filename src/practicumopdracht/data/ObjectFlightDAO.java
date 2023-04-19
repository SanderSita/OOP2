package practicumopdracht.data;
import practicumopdracht.models.Flight;
import java.io.*;
import static practicumopdracht.MainApplication.getAirlineDAO;
import static practicumopdracht.MainApplication.getFlightDAO;

public class ObjectFlightDAO extends FlightDAO{
    private static final String FILENAME = "objectflight.txt";

    File file = new File(FILENAME);
    @Override
    public boolean save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            for(Flight flight : getFlightDAO().flightList){
                out.writeInt(getAirlineDAO().getIdFor(flight.getBelongsTo()));
                out.writeObject(flight);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("End of file reached");
        }

        return true;
    }

    @Override
    public boolean load() {
        getFlightDAO().flightList.clear();
        try (ObjectInputStream inp = new ObjectInputStream(new FileInputStream(file))) {
            while (inp.available() > 0) {
                int airlineIndex = inp.readInt();
                Flight flight = (Flight) inp.readObject();
                flight.setBelongsTo(getAirlineDAO().getById(airlineIndex));
                getFlightDAO().addOrUpdate(flight);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("End of file reached");
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found");
        }

        return true;
    }

}
