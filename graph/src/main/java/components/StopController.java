
package components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class StopController {

        @FXML
        private Label image;

        @FXML
        private Label stopName;

        @FXML
        void imageClicked(MouseEvent event) {

        }

        @FXML
        void labelClicked(MouseEvent event) {

        }

        public Label getImage() {
                return image;
        }

        public Label getStopName() {
                return stopName;
        }
}

