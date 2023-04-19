package practicumopdracht.comparators;
import practicumopdracht.models.Flight;
import java.util.Comparator;

public class FlightdurationComparator implements Comparator<Flight> {
    private boolean sortDescending;

    public FlightdurationComparator(Boolean sortDescending) {
        this.sortDescending = sortDescending;
    }

    @Override
    public int compare(Flight o1, Flight o2) {
        return sortDescending ? Double.compare(o1.getFlightDuration(), o2.getFlightDuration()) : Double.compare(o2.getFlightDuration(), o1.getFlightDuration());
    }
}
