package practicumopdracht.data;
import practicumopdracht.models.Airline;
import practicumopdracht.models.Flight;
import java.time.LocalDate;
import java.util.List;
import static practicumopdracht.MainApplication.getAirlineDAO;
import static practicumopdracht.MainApplication.getFlightDAO;

public class DummyFlightDAO extends FlightDAO{

    public boolean save(){
        return false;
    }

    public boolean load(){
        List<Airline> allAirlines = getAirlineDAO().getAll();

        LocalDate date1 = LocalDate.of(2022, 10, 10);
        LocalDate date2 = LocalDate.of(1922, 1, 2);
        LocalDate date3 = LocalDate.of(2021, 8, 10);
        LocalDate date4 = LocalDate.of(2022, 10, 7);
        LocalDate date5 = LocalDate.of(1999, 10, 10);
        LocalDate date6 = LocalDate.of(2022, 12, 2);

        getFlightDAO().addOrUpdate(new Flight(allAirlines.get(0), date1, "New York", false, 10));
        getFlightDAO().addOrUpdate(new Flight(allAirlines.get(1), date2, "Moscow", false, 10));
        getFlightDAO().addOrUpdate(new Flight(allAirlines.get(2), date3, "London", false, 10));
        getFlightDAO().addOrUpdate(new Flight(allAirlines.get(3), date4, "Miami", false, 10));
        getFlightDAO().addOrUpdate(new Flight(allAirlines.get(2), date5, "Paris", false, 10));
        getFlightDAO().addOrUpdate(new Flight(allAirlines.get(1), date6, "Rome", false, 10));

        return true;
    }
}
