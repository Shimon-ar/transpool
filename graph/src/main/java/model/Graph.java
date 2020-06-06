package model;

import components.Cell;
import components.Edge;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.transpool.engine.ds.StopManager;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {

    private Group canvas;
    private List<Cell> allCells;
    private Map<String,Cell> cellMap;
    private Map<String,Edge> edgeMap;
    private List<Edge> allEdges;
    private List<Edge> removedEdges;
    private int width;
    private int length;



    public Graph(int width,int length) {

        allCells = new ArrayList<>();
        allEdges = new ArrayList<>();
        removedEdges = new ArrayList<>();
        canvas = new Group();
        this.width = width;
        this.length = length;
        cellMap = new HashMap<>();
        edgeMap = new HashMap<>();
    }


    public void addCell(String id, int x, int y) {

        Cell cell = new Cell(id,(double)x/(double)width,(double)y/(double)length , x,y);
        addCell(cell);
    }

    public void addCell( Cell cell) {

        allCells.add(cell);
        cellMap.put( cell.getCellId(), cell);
        canvas.getChildren().add(cell);
        cell.setLayoutX(cell.getX()*1050);
        cell.setLayoutY(cell.getY()*450);

    }

    public void addEdge( String sourceId, String targetId) {

        Cell sourceCell = cellMap.get( sourceId);
        Cell targetCell = cellMap.get( targetId);
        Edge edge = new Edge( sourceCell, targetCell);
        allEdges.add( edge);
        canvas.getChildren().add(edge);
        edgeMap.put(sourceId + targetId , edge);
    }

    public void connectEdges() {
        for(Edge edge:allEdges)
            edge.setEdge();

    }

    public Group getCanvas() {
        return canvas;
    }

    public void playAnimation(List<String> route) {
        List<String> reverseRoute = new ArrayList<>(route);
        Collections.reverse(reverseRoute);
        List<String> edgesKeys = new ArrayList<>();
        List<String> edgeKeysRev = new ArrayList<>();
        for (int i = 0; i < route.size() - 1; i++) {
            edgesKeys.add(route.get(i) + route.get(i + 1));
            edgeKeysRev.add(reverseRoute.get(i)+reverseRoute.get(i+1));
        }
        Collections.reverse(edgeKeysRev);

        for (int i=0;i<edgesKeys.size();i++) {
            setEdgeAnimation(edgesKeys.get(i),edgeKeysRev.get(i));
        }

    }



    public void setEdgeAnimation(String from_to,String to_from){
        Edge edge = edgeMap.get(from_to);
        Edge edgeRev = edgeMap.get(to_from);

        Line line = edge.getArrowLine().getLine();
        setLineAnimation(line,edgeRev);

    }

    private void setLineAnimation(Line line,Edge revEdge){
        if(revEdge != null)
            canvas.getChildren().remove(revEdge);
        line.getStrokeDashArray().setAll(20d, 20d, 20d, 20d);
        final double maxOffset =
                line.getStrokeDashArray().stream()
                        .reduce(
                                0d,
                                (a, b) -> a + b
                        );

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(
                                line.strokeDashOffsetProperty(),
                                0,
                                Interpolator.LINEAR
                        )
                ),
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(
                                line.strokeDashOffsetProperty(),
                                maxOffset,
                                Interpolator.LINEAR
                        )
                )
        );
        timeline.setCycleCount(2);
        timeline.setOnFinished(event -> {
            line.getStrokeDashArray().clear();
            if(revEdge != null)
                canvas.getChildren().add(revEdge);
        });


        timeline.play();
    }

    public List<Cell> getAllCells() {
        return allCells;
    }

    public Map<String, Cell> getCellMap() {
        return cellMap;
    }

    public Map<String, Edge> getEdgeMap() {
        return edgeMap;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }



    public void updateDetails(Map<String, StopManager> map){
        for(String stopName:map.keySet()){
            Cell cell = cellMap.get(stopName);
            cell.setCars(map.get(stopName).getCapacity());
            cell.setDown(map.get(stopName).getDownCostumers());
            cell.setUp(map.get(stopName).getUpCostumers());
        }
    }
}


