import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Graph;


public class Main extends Application {

    Graph graph;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        graph = new Graph(20,20);

        root.setCenter(graph.getCanvas());

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.show();

        addGraphComponents();

      /*  Layout layout = new RandomLayout(graph,20,20);
        layout.execute();
*/
    }

    private void addGraphComponents() {

    /*    Model model = graph.getModel();

        graph.beginUpdate();*/

        graph.addCell("Cell A",7,9);
        graph.addCell("Cell B", 8,1);
        graph.addCell("Cell C", 5,13);
        graph.addCell("Cell D", 20,17);
        graph.addCell("Cell E", 15,2);
        graph.addCell("Cell F", 2,13);
        graph.addCell("Cell G", 4,1);

      /*  graph.addEdge("Cell A", "Cell B");
        graph.addEdge("Cell B", "Cell A");
        graph.addEdge("Cell A", "Cell C");
        graph.addEdge("Cell B", "Cell C");
        graph.addEdge("Cell C", "Cell D");*/
        graph.addEdge("Cell B", "Cell E");
        graph.addEdge("Cell E", "Cell B");
        graph.addEdge("Cell D", "Cell G");
        graph.connectEdges();

        /*graph.endUpdate();*/

    }

    public static void main(String[] args) {
        launch(args);
    }
}