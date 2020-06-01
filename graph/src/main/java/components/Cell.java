package components;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Cell extends Pane {

    String cellId;
    StopController stopController;

    List<Cell> children = new ArrayList<>();
    List<Cell> parents = new ArrayList<>();
    private int x,y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    Node view;

    public Cell(String cellId,int x,int y) throws IOException {
        this.cellId = cellId;
        this.x = x;
        this.y = y;
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/Station.fxml");
        loader.setLocation(mainFXML);
        view = loader.load();
        setView(view);
        stopController = loader.getController();
        setNameStation(cellId);

    }

    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    public List<Cell> getCellChildren() {
        return children;
    }

    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    public List<Cell> getCellParents() {
        return parents;
    }

    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }

    public void setView(Node view) {

        this.view = view;
        getChildren().add(view);

    }

    public Node getView() {
        return this.view;
    }

    public String getCellId() {
        return cellId;
    }

    public void setNameStation(String string){
        stopController.getStopName().setText(string);
    }
}