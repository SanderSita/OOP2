package practicumopdracht.models;
import java.time.LocalDate;

public class Flight implements java.io.Serializable{
    private transient Airline belongsTo;
    private LocalDate date;
    private String destination;
    private boolean cancelled;
    private double flightDuration;

    public Flight(Airline belongsTo, LocalDate date, String destination, boolean cancelled, double flightDuration) {
        this.belongsTo = belongsTo;
        this.date = date;
        this.destination = destination;
        this.cancelled = cancelled;
        this.flightDuration = flightDuration;
    }

    public Airline getBelongsTo() {
        return belongsTo;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDestination() {
        return destination;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public double getFlightDuration() {
        return flightDuration;
    }

    public void setBelongsTo(Airline belongsTo) {
        this.belongsTo = belongsTo;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setFlightDuration(double flightDuration) {
        this.flightDuration = flightDuration;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s - %.2f", date, destination, cancelled, flightDuration);
    }
}
