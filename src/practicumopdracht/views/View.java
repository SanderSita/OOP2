package practicumopdracht.views;
import javafx.scene.Parent;
public abstract class View {
    private Parent root;

    public View() {
        this.root = initializeView();
    }

    abstract protected Parent initializeView();

    public Parent getRoot(){
        return root;
    }
}
