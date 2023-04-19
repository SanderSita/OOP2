package practicumopdracht.comparators;
import practicumopdracht.models.Airline;
import java.util.Comparator;

public class NameComparator implements Comparator<Airline> {
    private boolean sortDescending;
    public NameComparator(Boolean sortDescending) {
        this.sortDescending = sortDescending;
    }

    @Override
    public int compare(Airline o1, Airline o2) {
        int result = o1.getName().compareTo(o2.getName());
        return sortDescending ? -result : result;
    }
}