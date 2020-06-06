package org.guiMain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import model.Graph;
import org.controllers.TripOfferController;
import org.fxUtilities.FxUtilities;
import org.transpool.engine.Engine;
import org.transpool.engine.ds.MapDb;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GuiMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
 /*       Engine engine = new Engine();
        engine.loadMap(new File("/Users/shimon/Desktop/projects/transpool/transpool-engine/src/main/resources/ex1-small.xml"));
        System.out.println(engine.getErrorDes());

        //engine.inRequest("shim","D","H",1,9,5,"checkout",true,10);
        MapDb mapDb = engine.getMap();
        Graph graph = new Graph(mapDb.getWidth(),mapDb.getLength());
        mapDb.getMap().values().stream().forEach(node -> {
            graph.addCell(node.getStop().getName(),node.getStop().getX(),node.getStop().getY());
        });

        mapDb.getMap().values().stream().forEach(node -> {
            node.getPaths().stream().forEach(path -> {
                graph.addEdge(node.getStop().getName(),path.getTo().getName());
            });
        });

        graph.connectEdges();
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("F");

        List<String> names = new ArrayList<>();
        names.add("Shimon");
        names.add("sapir");
        names.add("reut");

        List<String> namesy = new ArrayList<>();
        namesy.add("Shi");
        namesy.add("sapi");
        namesy.add("ret");
        graph.getCellMap().get("A").setCars(2);
        graph.getCellMap().get("A").setDown(namesy);
        graph.getCellMap().get("A").setUp(names);
        graph.getCellMap().get("B").setDown(names);
        graph.getCellMap().get("A").setDetailMessage();




*/






          FXMLLoader loader = new FXMLLoader();

        URL mainFXML = getClass().getResource("/org/fxml/Main.fxml");
        loader.setLocation(mainFXML);
        ScrollPane root = loader.load();

        primaryStage.setTitle("transpool");

       Scene scene = new Scene(root);

        primaryStage.setScene(scene);
       // graph.playAnimation(list);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
