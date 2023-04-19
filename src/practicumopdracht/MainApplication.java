package practicumopdracht;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import practicumopdracht.controllers.AirlineController;
import practicumopdracht.controllers.Controller;
import practicumopdracht.data.*;
import java.io.FileNotFoundException;

public class MainApplication extends Application {
    private final String TITLE = "Practicumopdracht OOP2";
    private static final int WIDTH = 640;
    private static final int HEIGHT = 580;
    private static Stage stage;
    private static AirlineDAO airlineDAO;
    private static FlightDAO flightDAO;

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        if(!Main.launchedFromMain) {
            System.err.println("Je moet deze applicatie opstarten vanuit de Main-class, niet de MainApplication-class!");
            System.exit(1337);

            return;
        }

        airlineDAO = new BinaryAirlineDAO();
        airlineDAO.load();

        flightDAO = new ObjectFlightDAO();
        flightDAO.load();

        MainApplication.stage = stage;

        stage.setTitle(String.format("%s - %s", TITLE, Main.studentNaam));
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);

        MainApplication.stage.setScene(new Scene(new Label()));
        MainApplication.stage.show();

        switchController(new AirlineController());
    }

    public static void switchController(Controller controller){
        MainApplication.stage.getScene().setRoot(controller.getView().getRoot());
    }

    public static AirlineDAO getAirlineDAO(){
        return airlineDAO;
    }

    public static FlightDAO getFlightDAO() {
        return flightDAO;
    }
}
