package practicumopdracht.views;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import practicumopdracht.models.Airline;

public class AirlineView extends View{
    private Button addButton;
    private Button deleteButton;
    private Button openFlightButton;
    private TextField nameField;
    private TextField locationField;
    private TextField ratingField;
    private TextField employeeCountField;
    private Button saveButton;
    private ListView<Airline> listView;
    private MenuItem saveItem;
    private MenuItem loadItem;
    private MenuItem exitItem;
    private MenuItem sortAscendingItem;
    private MenuItem sortDescendingItem;
    @Override
    protected Parent initializeView() {
        // File buttons on top
        BorderPane saveAndLoadButton = new BorderPane();

        // File
        Menu fileMenu = new Menu("File");
        saveItem = new MenuItem("Save");
        loadItem = new MenuItem("Load");
        exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(saveItem, loadItem, exitItem);

        // Sorting
        Menu sortMenu = new Menu("Sort");
        sortAscendingItem = new MenuItem("Name (A-Z)");
        sortDescendingItem = new MenuItem("Name (Z-A)");
        sortMenu.getItems().addAll(sortAscendingItem, sortDescendingItem);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, sortMenu);

        saveAndLoadButton.setTop(menuBar);

        // Title
        Label title = new Label("Airlines");
        title.setStyle("-fx-font-size: 24pt;");

        // Grid
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        Label nameLabel = new Label("Name:");
        nameField = new TextField();

        Label locationLabel = new Label("Location:");
        locationField = new TextField();

        Label ratingLabel = new Label("Rating:");
        ratingField = new TextField();

        Label employeeCountLabel = new Label("Employee\nCount:");
        employeeCountLabel.setMinHeight(40);
        employeeCountLabel.setPrefHeight(40);

        employeeCountField = new TextField();

        // Set the preferred height of the TextField to 50% of the Label height
        employeeCountField.prefHeightProperty().bind(employeeCountLabel.heightProperty().multiply(0.5));

        saveButton = new Button("SAVE");
        saveButton.setMaxWidth(Double.MAX_VALUE);

        // Set the width of the first column to a fixed width
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPrefWidth(65);
        gridPane.getColumnConstraints().add(column1);

        // Set the width of the second column to take up the remaining available space
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(column2);

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);

        gridPane.add(locationLabel, 0, 1);
        gridPane.add(locationField, 1, 1);

        gridPane.add(ratingLabel, 0, 2);
        gridPane.add(ratingField, 1, 2);

        gridPane.add(employeeCountLabel, 0, 3);
        gridPane.add(employeeCountField, 1, 3);

        // ListView
        listView = new ListView<>();
        listView.setMaxWidth(Double.MAX_VALUE);
        listView.setPrefWidth(Control.USE_COMPUTED_SIZE);

        //buttons
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        openFlightButton = new Button("OPEN AIRLINE");
        openFlightButton.setMaxWidth(Double.MAX_VALUE);

        addButton = new Button("ADD");
        addButton.setMaxWidth(Double.MAX_VALUE);

        deleteButton = new Button("DELETE");
        deleteButton.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(openFlightButton, Priority.ALWAYS);
        HBox.setHgrow(addButton, Priority.ALWAYS);
        HBox.setHgrow(deleteButton, Priority.ALWAYS);

        buttonBox.getChildren().addAll(openFlightButton, addButton, deleteButton);

        //ROOT
        VBox root = new VBox();
        root.setSpacing(10);
        root.setFillWidth(true);
        VBox.setMargin(saveButton, new Insets(10));
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(saveAndLoadButton, title, gridPane, saveButton, listView, buttonBox);

        return root;
    }

    public Button getAddButton() {
        return addButton;
    }
    public Button getDeleteButton() {
        return deleteButton;
    }
    public Button getOpenFlightButton() {
        return openFlightButton;
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getLocationField() {
        return locationField;
    }

    public TextField getRatingField() {
        return ratingField;
    }

    public TextField getEmployeeCountField() {
        return employeeCountField;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public ListView<Airline> getListView() {
        return listView;
    }

    public MenuItem getSaveItem() {
        return saveItem;
    }

    public MenuItem getLoadItem() {
        return loadItem;
    }

    public MenuItem getExitItem() {
        return exitItem;
    }

    public MenuItem getSortAscendingItem() {
        return sortAscendingItem;
    }

    public MenuItem getSortDescendingItem() {
        return sortDescendingItem;
    }
}
