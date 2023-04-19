package practicumopdracht.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MultipleSelectionModel;
import practicumopdracht.comparators.NameComparator;
import practicumopdracht.models.Airline;
import practicumopdracht.models.Flight;
import practicumopdracht.views.AirlineView;
import practicumopdracht.views.View;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import static practicumopdracht.MainApplication.*;

public class AirlineController extends Controller{
    private AirlineView view = new AirlineView();

    public AirlineController(){
        List<Airline> airlineList = getAirlineDAO().getAll();
        for(Airline airline : airlineList){
            view.getListView().getItems().add(airline);
        }

        // Listen for changes to the selected item
        view.getListView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Get the selection model for the list
                MultipleSelectionModel<Airline> selectionModel = view.getListView().getSelectionModel();

                // Get the currently selected item (if any)
                Airline selectedAirline = selectionModel.getSelectedItem();

                view.getNameField().setText(selectedAirline.getName());
                view.getLocationField().setText(selectedAirline.getLocation());
                view.getRatingField().setText(Double.toString(selectedAirline.getRating()));
                view.getEmployeeCountField().setText(Integer.toString(selectedAirline.getEmployeeCount()));
            }
        });

        view.getAddButton().setOnAction(event -> {
            addAirline();
        });

        view.getDeleteButton().setOnAction(event -> {
            deleteAirline();
        });

        view.getOpenFlightButton().setOnAction(event -> {
            if(!view.getListView().getSelectionModel().isEmpty()){
                switchController(new FlightController(view.getListView().getSelectionModel().getSelectedItem()));
            }
        });

        view.getSaveButton().setOnAction(event -> {
            validateFields();
        });

        // Save button in menu
        view.getSaveItem().setOnAction(event -> {
            Optional<ButtonType> result = showAlert("Save all airlines and flights?", "Save");
            if(result.isPresent()){
                ButtonType buttonType = result.get();
                if (buttonType.getText().equals("Yes")){
                    try {
                        getAirlineDAO().save();
                        getFlightDAO().save();

                        taskMessage("Airlines saved successfully");
                    } catch (FileNotFoundException e) {
                        System.err.println("File not found");
                        taskMessage("Airlines failed to save");
                    }
                }
            }
        });

        // Load button in menu
        view.getLoadItem().setOnAction(event -> {
            Optional<ButtonType> result = showAlert("Load the Airlines and Flights?", "Load");
            if(result.isPresent()){
                ButtonType buttonType = result.get();
                if (buttonType.getText().equals("Yes")){
                    try {
                        getAirlineDAO().load();
                        getFlightDAO().load();

                        List<Airline> airlines = getAirlineDAO().getAll();
                        ObservableList<Airline> observableAirlines = FXCollections.observableList(airlines);
                        view.getListView().setItems(observableAirlines);

                        taskMessage("Airlines loaded successfully");
                    } catch (FileNotFoundException e) {
                        System.err.println("File not found");
                        taskMessage("Airlines failed to load");
                    }
                }
            }
        });

        // Exit button in menu
        view.getExitItem().setOnAction(event -> {
            Optional<ButtonType> result = showAlert("Are you sure you want to close the application? Any unsaved changes will be lost. " +
                    "\nDo you want to save changes before closing?", "Exit");
            if (result.isPresent()) {
                ButtonType buttonType = result.get();
                if (buttonType.getText().equals("Yes")) {
                    try {
                        getAirlineDAO().save();
                        getFlightDAO().save();
                    } catch (FileNotFoundException e) {
                        System.err.println("File not found");
                    }
                }
                System.exit(0);
            }
        });

        // Sort button in menu
        view.getSortAscendingItem().setOnAction(event -> {
            sortAirlines(false);
        });

        // Sort button in menu
        view.getSortDescendingItem().setOnAction(event -> {
            sortAirlines(true);
        });
    }

    public void validateFields(){
        String name = view.getNameField().getText().trim();
        String location = view.getLocationField().getText().trim();
        String rating = view.getRatingField().getText().trim();
        String employeeCount = view.getEmployeeCountField().getText().trim();

        // String for all validation messages
        StringBuilder validateStringBuilder = new StringBuilder();

        if (name.isEmpty() || location.isEmpty() || rating.isEmpty() || employeeCount.isEmpty()) {
            validateStringBuilder.append("- All fields are required.\n");
        }

        int employeeCountValue = 0;
        double ratingValue = 0;

        if(!rating.equals("")){
            if(rating.matches("-?\\d+(\\.\\d+)?")){ // check if the rating is a valid number format
                ratingValue = Double.parseDouble(rating.replace(',', '.'));
                if(Double.isNaN(ratingValue) || Double.isInfinite(ratingValue)){
                    validateStringBuilder.append("- Rating is not filled in correctly.\n");
                }else if (ratingValue > 5 || ratingValue < 0){
                    validateStringBuilder.append("- Rating cannot be higher than 5 or lower than 0!\n");
                }
            } else {
                validateStringBuilder.append("- Rating is not in a valid number format.\n");
            }
        } else {
            validateStringBuilder.append("- Rating is required\n");
        }

        try {
            employeeCountValue = Integer.parseInt(employeeCount);
        } catch (NumberFormatException e) {
            validateStringBuilder.append("- Employee count must be an integer.\n");
        }

        if(validateStringBuilder.length() > 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(String.valueOf(validateStringBuilder));
            alert.showAndWait();
            return;
        }

        Airline airline;
        String prefix;

        // Update or Create Airline
        if(!view.getListView().getSelectionModel().isEmpty()){
            prefix = "UPDATED";
            airline = view.getListView().getSelectionModel().getSelectedItem();

            airline.setName(view.getNameField().getText());
            airline.setLocation(view.getLocationField().getText());
            airline.setRating(Double.parseDouble(view.getRatingField().getText()));
            airline.setEmployeeCount(Integer.parseInt(view.getEmployeeCountField().getText()));
        }else{
            prefix = "CREATED";
            airline = new Airline(name, location, ratingValue, employeeCountValue);
        }

        getAirlineDAO().addOrUpdate(airline);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(String.format("%s: %s", prefix, airline));
        alert.showAndWait();

        List<Airline> airlines = getAirlineDAO().getAll();
        ObservableList<Airline> observableAirlines = FXCollections.observableList(airlines);
        view.getListView().setItems(observableAirlines);

        view.getListView().getSelectionModel().clearSelection();
        clearFields();
    }

    @Override
    public View getView() {
        return view;
    }

    public void addAirline(){
        if(!view.getListView().getSelectionModel().isEmpty()) {
            view.getListView().getSelectionModel().clearSelection();

            clearFields();
            return;
        }

        validateFields();
    }

    public void clearFields(){
        view.getNameField().clear();
        view.getLocationField().clear();
        view.getRatingField().clear();
        view.getEmployeeCountField().clear();
    }

    public void deleteAirline(){
        if(!view.getListView().getSelectionModel().isEmpty()){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Delete this Airline?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                deleteFlights(view.getListView().getSelectionModel().getSelectedItem());

                getAirlineDAO().remove(view.getListView().getSelectionModel().getSelectedItem());

                List<Airline> airlines = getAirlineDAO().getAll();
                ObservableList<Airline> observableAirlines = FXCollections.observableList(airlines);
                view.getListView().setItems(observableAirlines);
                clearFields();
            }
        }
    }

    // Delete all Flights from the Airline
    public void deleteFlights(Airline airline) {
        List<Flight> flightList =  getFlightDAO().getAllFor(airline);
        for(Flight flight : flightList){
            getFlightDAO().remove(flight);
        }
    }

    public void sortAirlines(boolean sortDescending) {
        FXCollections.sort(view.getListView().getItems(), new NameComparator(sortDescending));
    }

    // Yes or No alert
    public Optional<ButtonType> showAlert(String text, String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(text);
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            ButtonType buttonType = result.get();
            if (buttonType == yesButton || buttonType == noButton) {
                return result;
            }
        }

        return Optional.empty();
    }

    public void taskMessage(String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }
}
