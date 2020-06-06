package components;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class StationController {

    @FXML
    private StackPane stackPane;

    @FXML
    private ImageView imageArea;

    @FXML
    private HBox carArea;

    @FXML
    private Label label;

    private Consumer<Stage> onClick;

    @FXML
    void stackPaneclicked(MouseEvent event) {
        Node source = (Node)event.getSource();
        Stage tripStage  = (Stage) source.getScene().getWindow();
        onClick.accept(tripStage);
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public ImageView getImageArea() {
        return imageArea;
    }

    public HBox getCarArea() {
        return carArea;
    }

    public Label getLabel() {
        return label;
    }

    public void setOnClick(Consumer<Stage> onClick) {
        this.onClick = onClick;
    }
}
