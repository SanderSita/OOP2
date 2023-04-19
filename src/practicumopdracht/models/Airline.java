package practicumopdracht.models;

public class Airline {
    private String name;
    private String location;
    private double rating;
    private int employeeCount;
    public Airline(String name, String location, double rating, int employeeCount) {
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.employeeCount = employeeCount;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public double getRating() {
        return rating;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %.1f - %d", name, location, rating, employeeCount);
    }
}
