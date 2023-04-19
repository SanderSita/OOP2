package practicumopdracht.comparators;
import practicumopdracht.models.Flight;
import java.time.LocalDate;
import java.util.Comparator;

public class FlightdurationDateComparator implements Comparator<Flight> {
    private boolean sortDescending;

    public FlightdurationDateComparator(Boolean sortDescending) {
        this.sortDescending = sortDescending;
    }

    @Override
    public int compare(Flight o1, Flight o2) {
        LocalDate date1 = o1.getDate();
        LocalDate date2 = o2.getDate();

        int result = sortDescending ? date2.compareTo(date1) : date1.compareTo(date2);
        if (result == 0) {
            return sortDescending ? Double.compare(o1.getFlightDuration(), o2.getFlightDuration()) : Double.compare(o2.getFlightDuration(), o1.getFlightDuration());
        }

        return result;
    }
}
