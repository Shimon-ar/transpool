package org.components;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import org.ds.RequestTripProperty;
import org.fxUtilities.FxUtilities;

public class RequestsTable {
    private JFXTreeTableView<RequestTripProperty> treeTableView;
    private JFXTreeTableColumn<RequestTripProperty,Integer> idColumn;
    private JFXTreeTableColumn sourceColumn;
    private JFXTreeTableColumn destinationColumn;
    private JFXTreeTableColumn nameColumn;
    private JFXTreeTableColumn checkArrival;
    private JFXTreeTableColumn<RequestTripProperty,Boolean> matchedColumn;
    private JFXTreeTableColumn dayColumn;
    private JFXTreeTableColumn timeColumn;

    public RequestsTable() {
        treeTableView = new JFXTreeTableView();
        idColumn = new JFXTreeTableColumn("Id");
        sourceColumn = new JFXTreeTableColumn("Source station");
        destinationColumn = new JFXTreeTableColumn("Destination station");
        timeColumn = new JFXTreeTableColumn("Time");
        checkArrival = new JFXTreeTableColumn("Check-out/Arrival");
        matchedColumn = new JFXTreeTableColumn("Matched");
        nameColumn = new JFXTreeTableColumn("Name");
        dayColumn = new JFXTreeTableColumn("Day");



        treeTableView.getColumns().addAll(idColumn,nameColumn,sourceColumn,destinationColumn,dayColumn,checkArrival,timeColumn,matchedColumn);
        treeTableView.setPrefSize(920,250);
        idColumn.setPrefWidth(70);
        dayColumn.setPrefWidth(70);
        sourceColumn.setPrefWidth(150);
        destinationColumn.setPrefWidth(150);
        checkArrival.setPrefWidth(150);
        timeColumn.setPrefWidth(120);
        matchedColumn.setPrefWidth(90);
        nameColumn.setPrefWidth(130);

    }

    public void show(){
        FxUtilities.openNewStage(treeTableView,"");
    }

    public JFXTreeTableView getTreeTableView() {
        return treeTableView;
    }

    public JFXTreeTableColumn<RequestTripProperty,Integer> getIdColumn() {
        return idColumn;
    }

    public JFXTreeTableColumn getSourceColumn() {
        return sourceColumn;
    }

    public JFXTreeTableColumn getDestinationColumn() {
        return destinationColumn;
    }

    public JFXTreeTableColumn getNameColumn() {
        return nameColumn;
    }

    public JFXTreeTableColumn getCheckArrival() {
        return checkArrival;
    }

    public JFXTreeTableColumn getMatchedColumn() {
        return matchedColumn;
    }

    public JFXTreeTableColumn getDayColumn() {
        return dayColumn;
    }

    public JFXTreeTableColumn getTimeColumn() {
        return timeColumn;
    }
}
