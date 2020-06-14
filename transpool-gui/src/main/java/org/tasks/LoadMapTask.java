package org.tasks;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.layout.BorderPane;
import model.Graph;
import org.components.Tabs;
import org.controllers.MainController;
import org.controllers.OfferTableController;
import org.controllers.RequestTableController;
import org.ds.OfferTripProperty;
import org.fxUtilities.FxUtilities;
import org.transpool.engine.Engine;
import org.transpool.engine.ds.MapDb;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class LoadMapTask extends Task<Boolean> {

    private Engine engine;
    private String pathFxml;
    private Graph graph;
    private Consumer<Boolean> onFinish;
    private MainController mainController;

    public LoadMapTask(Engine engine, String pathFxml, Consumer<Boolean> onFinish, MainController mainController) {
        this.engine = engine;
        this.pathFxml = pathFxml;
        this.onFinish = onFinish;
        this.mainController = mainController;
    }

    @Override
    protected Boolean call() {
        updateMessage("checking file validation..");
        updateProgress(0,1);
        sleepForAWhile(300);
        try {
            if (!engine.loadMap(new File(pathFxml)))
                throw new TaskException();

            updateProgress(0.3,1);
            updateMessage("start build map..");
            sleepForAWhile(300);
            updateProgress(0.6,1);
            Platform.runLater(() -> {
                MapDb mapDb = engine.getMap();
                graph = new Graph(mapDb.getWidth(),mapDb.getLength());
                updateMessage("build station's..");
                mapDb.getMap().values().forEach(node -> {
                    graph.addCell(node.getStop().getName(),node.getStop().getX(),node.getStop().getY());
                });
                updateProgress(0.8,1);
                sleepForAWhile(300);
                updateMessage("build paths..");
                mapDb.getMap().values().forEach(node -> {
                    node.getPaths().stream().forEach(path -> {
                        graph.addEdge(node.getStop().getName(),path.getTo().getName());
                    });
                });
                sleepForAWhile(300);
                updateProgress(0.9,1);
                graph.connectEdges();
                updateMessage("build table..");
                sleepForAWhile(300);

                mainController.build(graph);

            });
            updateMessage("done..");
            sleepForAWhile(400);

            updateProgress(1, 1);
            Platform.runLater(() -> {
                onFinish.accept(true);
            });
        }  catch (JAXBException | IOException e) {
        e.printStackTrace();
    } catch (TaskException e){
            Platform.runLater(()->onFinish.accept(false));


    }

        return Boolean.TRUE;


    }

    public static void sleepForAWhile(long sleepTime) {
        if (sleepTime != 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {

            }
        }
    }


}


