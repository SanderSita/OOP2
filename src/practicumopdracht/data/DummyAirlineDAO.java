package practicumopdracht.data;
import practicumopdracht.models.Airline;
import static practicumopdracht.MainApplication.getAirlineDAO;

public class DummyAirlineDAO extends AirlineDAO{
    public boolean save(){
        return false;
    }

    public boolean load(){
        getAirlineDAO().addOrUpdate(new Airline("KLM", "Amsterdam", 4.2, 32247));
        getAirlineDAO().addOrUpdate(new Airline("Transavia", "Rotterdam", 3.9, 1643));
        getAirlineDAO().addOrUpdate(new Airline("TUI", "Utrecht", 3.6, 765));
        getAirlineDAO().addOrUpdate(new Airline("Corendon", "Amsterdam", 3.5, 319));

        return true;
    }
}
