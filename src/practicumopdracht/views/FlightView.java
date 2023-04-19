package practicumopdracht.views;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.converter.LocalDateStringConverter;
import practicumopdracht.models.Airline;
import practicumopdracht.models.Flight;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static practicumopdracht.MainApplication.getAirlineDAO;
import static practicumopdracht.MainApplication.getFlightDAO;

public class FlightView extends View{
    class CustomLocalDateConverter extends LocalDateStringConverter {
        private final List<DateTimeFormatter> dateFormatters = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
        );
        @Override
        public LocalDate fromString(String s) {
            for (DateTimeFormatter formatter : dateFormatters) {
                try {
                    return LocalDate.parse(s, formatter);
                } catch (Exception ex) {
                    // Ignore and try the next formatter
                }
            }
            return null;
        }
    }
    private Button deleteButton;
    private Button addButton;
    private Button backButton;
    private ComboBox<Airline> airlineComboBox;
    private DatePicker dateField;
    private TextField destinationField;
    private TextField flightDurationField;
    private Button saveButton;
    private CheckBox cancelledCheckbox;
    private ListView<Flight> listView;
    private RadioButton type1AZ;
    private RadioButton type1ZA;
    private RadioButton type2AZ;
    private RadioButton type2ZA;
    private ToggleGroup toggleGroup;

    @Override
    protected Parent initializeView() {

        // Title
        Label title = new Label("Flights");
        title.setStyle("-fx-font-size: 24pt;");

        // Grid
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        Label airlineLabel = new Label("Airline:");

        // Create the ComboBox and set the options
        airlineComboBox = new ComboBox<>();
        HBox.setHgrow(airlineComboBox, Priority.ALWAYS);
        airlineComboBox.setMaxWidth(Double.MAX_VALUE);

        Label dateLabel = new Label("Date:");
        dateField = new DatePicker();
        dateField.setConverter(new CustomLocalDateConverter());

        HBox.setHgrow(dateField, Priority.ALWAYS);
        dateField.setMaxWidth(Double.MAX_VALUE);

        Label destinationLabel = new Label("Destination:");
        destinationField = new TextField();

        Label flightDurationLabel = new Label("flight\nDuration:");
        flightDurationLabel.setMinHeight(40);
        flightDurationLabel.setPrefHeight(40);
        flightDurationField = new TextField();

        Label cancelledLabel = new Label("Cancelled:");
        cancelledCheckbox = new CheckBox();

        // Set the width of the first column to a fixed width
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPrefWidth(65);
        gridPane.getColumnConstraints().add(column1);

        // Set the width of the second column to take up the remaining available space
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(column2);

        gridPane.add(airlineLabel, 0, 0);
        gridPane.add(airlineComboBox, 1, 0);

        gridPane.add(dateLabel, 0, 1);
        gridPane.add(dateField, 1, 1);

        gridPane.add(destinationLabel, 0, 2);
        gridPane.add(destinationField, 1, 2);

        gridPane.add(flightDurationLabel, 0, 3);
        gridPane.add(flightDurationField, 1, 3);

        gridPane.add(cancelledLabel, 0, 4);
        gridPane.add(cancelledCheckbox, 1, 4);

        saveButton = new Button("SAVE");
        saveButton.setMaxWidth(Double.MAX_VALUE);

        // ListView
        listView = new ListView<>();
        listView.setMaxWidth(Double.MAX_VALUE);
        listView.setPrefWidth(Control.USE_COMPUTED_SIZE);

        //buttons
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        backButton = new Button("BACK");
        backButton.setMaxWidth(Double.MAX_VALUE);

        addButton = new Button("ADD");
        addButton.setMaxWidth(Double.MAX_VALUE);

        deleteButton = new Button("DELETE");
        deleteButton.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(backButton, Priority.ALWAYS);
        HBox.setHgrow(addButton, Priority.ALWAYS);
        HBox.setHgrow(deleteButton, Priority.ALWAYS);

        buttonBox.getChildren().addAll(backButton, addButton, deleteButton);

        //radio buttons
        HBox radiobuttonsBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.BASELINE_RIGHT);

        Label sortingLabel = new Label("Sorting:");

        type1AZ = new RadioButton("Type #1 (A-Z)");
        type1ZA = new RadioButton("Type #1 (Z-A)");
        type2AZ = new RadioButton("Type #2 (A-Z)");
        type2ZA = new RadioButton("Type #2 (Z-A)");

        toggleGroup = new ToggleGroup();
        type1AZ.setToggleGroup(toggleGroup);
        type1ZA.setToggleGroup(toggleGroup);
        type2AZ.setToggleGroup(toggleGroup);
        type2ZA.setToggleGroup(toggleGroup);

        sortingLabel.prefWidthProperty().bind(radiobuttonsBox.widthProperty().multiply(0.2));
        type1AZ.prefWidthProperty().bind(radiobuttonsBox.widthProperty().multiply(0.20));
        type1ZA.prefWidthProperty().bind(radiobuttonsBox.widthProperty().multiply(0.20));
        type2AZ.prefWidthProperty().bind(radiobuttonsBox.widthProperty().multiply(0.20));
        type2ZA.prefWidthProperty().bind(radiobuttonsBox.widthProperty().multiply(0.20));

        HBox.setHgrow(backButton, Priority.ALWAYS);
        HBox.setHgrow(addButton, Priority.ALWAYS);
        HBox.setHgrow(deleteButton, Priority.ALWAYS);

        radiobuttonsBox.getChildren().addAll(sortingLabel, type1AZ, type1ZA, type2AZ, type2ZA);

        //ROOT
        VBox root = new VBox();
        root.setSpacing(10);
        root.setFillWidth(true);
        VBox.setMargin(saveButton, new Insets(10));
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, gridPane, saveButton, listView, radiobuttonsBox, buttonBox);

        return root;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public ComboBox<Airline> getAirlineComboBox() {
        return airlineComboBox;
    }

    public DatePicker getDateField() {
        return dateField;
    }

    public TextField getDestinationField() {
        return destinationField;
    }

    public TextField getFlightDurationField() {
        return flightDurationField;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public CheckBox getCancelledCheckbox() {
        return cancelledCheckbox;
    }

    public ListView<Flight> getListView() {
        return listView;
    }

    public RadioButton getType1AZ() {
        return type1AZ;
    }

    public RadioButton getType1ZA() {
        return type1ZA;
    }

    public RadioButton getType2AZ() {
        return type2AZ;
    }

    public RadioButton getType2ZA() {
        return type2ZA;
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }
}
