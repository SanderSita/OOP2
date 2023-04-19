package practicumopdracht.data;
import practicumopdracht.models.Airline;
import practicumopdracht.models.Flight;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class FlightDAO implements DAO<Flight>{
    protected List<Flight> flightList = new ArrayList<>();
    @Override
    public List<Flight> getAll() {
        return flightList;
    }

    public List<Flight> getAllFor(Airline object){
        ArrayList<Flight> flightsFound = new ArrayList<>();
        for(Flight flight : flightList){
            if(Objects.equals(flight.getBelongsTo().toString(), object.toString())){
                flightsFound.add(flight);
            }

        }

        return flightsFound;
    }

    @Override
    public void addOrUpdate(Flight object) {
        if(!flightList.contains(object)){
            flightList.add(object);
        }
    }

    @Override
    public void remove(Flight object) {
        flightList.remove(object);
    }

    @Override
    public abstract boolean save() throws FileNotFoundException;

    @Override
    public abstract boolean load() throws FileNotFoundException;
}
