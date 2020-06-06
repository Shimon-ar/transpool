package org.guiMain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Graph;
import org.controllers.LiveMapController;
import org.transpool.engine.Engine;
import org.transpool.engine.ds.MapDb;
import org.transpool.engine.ds.Match;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {


    public static void main(String[] args) throws JAXBException, IOException {launch(args);
/*        //launch(args);
        Engine engine = new Engine();
        engine.loadMap(new File("/Users/shimon/Desktop/Transpool/transpool/transpool-engine/src/main/resources/ex1-real.xml"));
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

        graph.connectEdges();*/


       /* List<Match> matches = engine.getMatches(engine.getRequestTrips().get(0),5);
        System.out.println(matches.size());
        for(Match match:matches){
            int count = 0;
            System.out.println(match.getRoutes().get(0).get(0) + "-" + match.getLastStop());
            for(List<String> route:match.getRoutes()) {
                System.out.println(route + " offerid:" + match.getOfferIDs().get(count) + "  time:" + match.getTimeForEachRoute().get(count).getCheckoutTime()
                + " - " + match.getTimeForEachRoute().get(count).getArrivalTime());
                count++;
            }*/







    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Engine engine = new Engine();
        engine.loadMap(new File("/Users/shimon/Desktop/projects/transpool/transpool-engine/src/main/resources/ex1-real.xml"));
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

        engine.getTransPoolTrips().stream().forEach(transpoolTrip -> System.out.println(transpoolTrip.getCheckoutTime()));

     /*   List<String> list = new ArrayList<>();
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
        graph.getCellMap().get("B").setDetailMessage();*/

      /*  FXMLLoader loader = new FXMLLoader();

        URL mainFXML = getClass().getResource("/org/fxml/LiveMap.fxml");
        loader.setLocation(mainFXML);
        VBox root = loader.load();

        primaryStage.setTitle("transpool");
        LiveMapController liveMapController = loader.getController();
        liveMapController.setEngine(engine);
        liveMapController.setMap(graph);

     Scene scene = new Scene(root);

        primaryStage.setScene(scene);
         //graph.playAnimation(list);
        primaryStage.show();*/



    }
}
