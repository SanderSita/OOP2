package practicumopdracht.data;
import practicumopdracht.models.Airline;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public abstract class AirlineDAO implements DAO<Airline>{
    protected List<Airline> airlinesList = new ArrayList<>();
    @Override
    public List<Airline> getAll() {
        return airlinesList;
    }

    public Airline getById(int id){
        return id >= 0 && id < airlinesList.size() ? airlinesList.get(id) : null;
    }

    public int getIdFor(Airline airline){
        return airlinesList.indexOf(airline);
    }

    @Override
    public void addOrUpdate(Airline object) {
        if(!airlinesList.contains(object)){
            airlinesList.add(object);
        }
    }

    @Override
    public void remove(Airline object) {
        airlinesList.remove(object);
    }

    @Override
    public abstract boolean save() throws FileNotFoundException;

    @Override
    public abstract boolean load() throws FileNotFoundException;
}
