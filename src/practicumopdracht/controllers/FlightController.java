package practicumopdracht.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import practicumopdracht.comparators.FlightdurationComparator;
import practicumopdracht.comparators.FlightdurationDateComparator;
import practicumopdracht.models.Airline;
import practicumopdracht.models.Flight;
import practicumopdracht.views.FlightView;
import practicumopdracht.views.View;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static practicumopdracht.MainApplication.*;

public class FlightController extends Controller{
    private FlightView view = new FlightView();

    public FlightController(Airline airlineParent){
        view.getAirlineComboBox().getItems().addAll(getAirlineDAO().getAll());
        view.getAirlineComboBox().getSelectionModel().select(airlineParent);

        ObservableList<Flight> allFlightsByAirline = FXCollections.observableArrayList(getFlightDAO().getAllFor(airlineParent));
        view.getListView().setItems(allFlightsByAirline);

        // Add a listener to the ToggleGroup to detect when the selected checkbox changes
        view.getToggleGroup().selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            sortFlights((RadioButton) newToggle);
        });

        // Listen for changes in combobox
        view.getAirlineComboBox().valueProperty().addListener((observable, oldValue, newValue) -> {
            clearFields();
            ObservableList<Flight> filteredFlights = FXCollections.observableArrayList(getFlightDAO().getAllFor(newValue));
            view.getListView().setItems(filteredFlights);
            sortFlights(null);
        });

        // Listen for changes to the selected item
        view.getListView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Get the selection model for the list
                MultipleSelectionModel<Flight> selectionModel = view.getListView().getSelectionModel();

                // Get the currently selected item (if any)
                Flight selectedFlight = selectionModel.getSelectedItem();

                view.getDateField().setValue(selectedFlight.getDate());
                view.getDestinationField().setText(selectedFlight.getDestination());
                view.getCancelledCheckbox().setSelected(selectedFlight.isCancelled());
                view.getFlightDurationField().setText(Double.toString(selectedFlight.getFlightDuration()));
            }
        });

        view.getAddButton().setOnAction(event -> {
            addFlight();
        });

        view.getDeleteButton().setOnAction(event -> {
            deleteFlight();
        });

        view.getBackButton().setOnAction(event -> {
            switchController(new AirlineController());
        });

        view.getSaveButton().setOnAction(actionEvent -> {
            validateFields();
        });
    }

    public void validateFields(){
        StringBuilder validateStringBuilder = new StringBuilder();
        Airline airline = view.getAirlineComboBox().getValue();

        String destination = view.getDestinationField().getText().trim();
        String flightDuration = view.getFlightDurationField().getText().trim();
        boolean isCancelled = view.getCancelledCheckbox().isSelected();

        if(airline == null || destination.isEmpty() || flightDuration.isEmpty()){
            validateStringBuilder.append("- All fields are required.\n");
        }

        String inputDate = "";
        try {
            LocalDate date = view.getDateField().getValue();
            if (date == null) {
                validateStringBuilder.append("- Please select a date.\n");
            }

            assert date != null;
            inputDate = date.toString();
        } catch (Exception e) {
            System.err.println("Wrong date");
        }

        if (!inputDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            validateStringBuilder.append("- Invalid date format. Please use YYYY-MM-DD format.\n");
        }

        double doubleFlightDuration = 0;
        if(!flightDuration.equals("")){
            if(flightDuration.matches("-?\\d+(\\.\\d+)?") || flightDuration.matches("-?\\d+(,\\d+)?")){
                doubleFlightDuration = Double.parseDouble(flightDuration.replace(',', '.'));
                if(Double.isNaN(doubleFlightDuration) || Double.isInfinite(doubleFlightDuration)){
                    validateStringBuilder.append("- Flight duration is not filled in correctly.\n");
                }
            }else{
                validateStringBuilder.append("- Flight duration is not a valid number.\n");
            }
        }else{
            validateStringBuilder.append("- Flight duration is required\n");
        }

        if(validateStringBuilder.length() > 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(String.valueOf(validateStringBuilder));
            alert.showAndWait();
            return;
        }

        Airline belongsTo = view.getAirlineComboBox().getSelectionModel().getSelectedItem();

        Flight newFlight;
        String prefix;
        if(!view.getListView().getSelectionModel().isEmpty()){
            prefix = "UPDATED";

            newFlight = view.getListView().getSelectionModel().getSelectedItem();

            newFlight.setBelongsTo(view.getAirlineComboBox().getSelectionModel().getSelectedItem());
            newFlight.setDate(view.getDateField().getValue());
            newFlight.setDestination(view.getDestinationField().getText());
            newFlight.setCancelled(view.getCancelledCheckbox().isSelected());
            newFlight.setFlightDuration(Double.parseDouble(view.getFlightDurationField().getText().replace(',', '.')));
        }else{
            prefix = "CREATED";

            // Convert the input date to LocalDate
            LocalDate date = LocalDate.parse(inputDate);
            newFlight = new Flight(belongsTo, date, destination, isCancelled, Double.parseDouble(flightDuration.replace(',', '.')));
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(String.format("%s: %s", prefix, newFlight));
        alert.showAndWait();

        view.getListView().getItems().add(newFlight);
        getFlightDAO().addOrUpdate(newFlight);

        List<Flight> flights = getFlightDAO().getAllFor(airline);
        ObservableList<Flight> observableFlights = FXCollections.observableList(flights);
        view.getListView().setItems(observableFlights);

        view.getListView().getSelectionModel().clearSelection();
        clearFields();
        sortFlights(null);
    }

    public void clearFields(){
        view.getDestinationField().clear();
        view.getFlightDurationField().clear();
        view.getCancelledCheckbox().setSelected(false);
        view.getDateField().setValue(null);
    }

    @Override
    public View getView() {
        return view;
    }

    public void addFlight(){
        if(!view.getListView().getSelectionModel().isEmpty()) {
            view.getListView().getSelectionModel().clearSelection();

            clearFields();
            return;
        }
        validateFields();
    }

    public void deleteFlight(){
        if(!view.getListView().getSelectionModel().isEmpty()){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Delete this Flight?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                getFlightDAO().remove(view.getListView().getSelectionModel().getSelectedItem());
                List<Flight> flights = getFlightDAO().getAllFor(view.getAirlineComboBox().getValue());
                ObservableList<Flight> observableFlights = FXCollections.observableList(flights);
                view.getListView().setItems(observableFlights);
                clearFields();
                sortFlights(null);
            }
        }
    }

    public void sortFlights(RadioButton newValue) {
        // Get the newly selected RadioButton
        RadioButton selectedButton;
        if(newValue != null){
            selectedButton = newValue;
        }else{
            selectedButton = (RadioButton) view.getToggleGroup().getSelectedToggle();
        }

        // Determine which RadioButton was selected and handle it accordingly
        if (selectedButton == view.getType1AZ()) {
            FXCollections.sort(view.getListView().getItems(), new FlightdurationComparator(true));
        } else if (selectedButton == view.getType1ZA()) {
            FXCollections.sort(view.getListView().getItems(), new FlightdurationComparator(false));
        } else if (selectedButton == view.getType2AZ()) {
            FXCollections.sort(view.getListView().getItems(), new FlightdurationDateComparator(false));
        } else if (selectedButton == view.getType2ZA()) {
            FXCollections.sort(view.getListView().getItems(), new FlightdurationDateComparator(true));
        }
    }
}
