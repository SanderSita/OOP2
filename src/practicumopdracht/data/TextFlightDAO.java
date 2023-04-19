package practicumopdracht.data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import practicumopdracht.models.Flight;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static practicumopdracht.MainApplication.getAirlineDAO;
import static practicumopdracht.MainApplication.getFlightDAO;

public class TextFlightDAO extends FlightDAO{
    private static final String FILENAME = "flight.txt";
    File file = new File(FILENAME);
    @Override
    public boolean save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("[");
            for(Flight flight : getFlightDAO().flightList){
                writer.write(String.format("{\n" +
                        "      \"belongsTo\": %d,\n" +
                        "      \"date\": \"%s\",\n" +
                        "      \"destination\": \"%s\",\n" +
                        "      \"cancelled\": %s,\n" +
                        "      \"flightDuration\": %f\n" +
                        "    },", getAirlineDAO().getIdFor(flight.getBelongsTo()), flight.getDate(), flight.getDestination(), flight.isCancelled(), flight.getFlightDuration()));
            }
            writer.write("]");
        } catch (IOException leesfout) {
            System.err.println("End of file.");
        }

        return true;
    }

    @Override
    public boolean load() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            StringBuilder json = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                json.append(line.trim());
            }

            JSONArray jsonArray = new JSONArray(json.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonFlight = jsonArray.getJSONObject(i);

                LocalDate localDate = LocalDate.parse(jsonFlight.getString("date"), formatter);
                Flight flight = new Flight(
                        getAirlineDAO().airlinesList.get(jsonFlight.getInt("belongsTo")),
                        localDate,
                        jsonFlight.getString("destination"),
                        jsonFlight.getBoolean("cancelled"),
                        jsonFlight.getDouble("flightDuration")
                );
                getFlightDAO().addOrUpdate(flight);
            }
        } catch (IOException | JSONException e) {
            System.err.println("An error occurred while reading the airline data file");
        }

        return true;
    }
}
