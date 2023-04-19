package practicumopdracht.data;
import practicumopdracht.models.Airline;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import static practicumopdracht.MainApplication.getAirlineDAO;

public class TextAirlineDAO extends AirlineDAO{
    private static final String FILENAME = "airline.txt";
    File file = new File(FILENAME);

    @Override
    public boolean save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("[");
            for(Airline airline : getAirlineDAO().airlinesList){
                writer.write(String.format("{\n" +
                        "      \"name\": \"%s\",\n" +
                        "      \"location\": \"%s\",\n" +
                        "      \"rating\": %.1f,\n" +
                        "      \"employeeCount\": %d\n" +
                        "    },", airline.getName(), airline.getLocation(), airline.getRating(), airline.getEmployeeCount()));
            }
            writer.write("]");
        } catch (IOException leesfout) {
            System.err.println("Bestand aanmaken mislukt.");
        }

        return true;
    }


    @Override
    public boolean load() {
        getAirlineDAO().airlinesList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            StringBuilder json = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                json.append(line.trim());
            }

            JSONArray jsonArray = new JSONArray(json.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonAirline = jsonArray.getJSONObject(i);
                Airline airline = new Airline(
                        jsonAirline.getString("name"),
                        jsonAirline.getString("location"),
                        jsonAirline.getInt("rating"),
                        jsonAirline.getInt("employeeCount")
                );
                getAirlineDAO().addOrUpdate(airline);
            }
        } catch (IOException | JSONException e) {
            System.err.println("An error occurred while reading the airline data file");
        }

        return true;
    }
}
