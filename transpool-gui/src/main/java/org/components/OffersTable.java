package org.components;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import org.ds.OfferTripProperty;
import org.fxUtilities.FxUtilities;

public class OffersTable {

    private JFXTreeTableView<OfferTripProperty> treeTableView;
    private JFXTreeTableColumn<OfferTripProperty,Integer> idColumn;
    private JFXTreeTableColumn nameColumn;
    private JFXTreeTableColumn checkOutColumn;
    private JFXTreeTableColumn arrivalColumn;
    private JFXTreeTableColumn dayColumn;
    private JFXTreeTableColumn recurrencesColumn;




    public OffersTable() {
        createTable();
    }

    public void createTable(){
        treeTableView = new JFXTreeTableView();
        idColumn = new JFXTreeTableColumn("Id");
        checkOutColumn = new JFXTreeTableColumn("Check-out");
        nameColumn = new JFXTreeTableColumn("Name");
        arrivalColumn = new JFXTreeTableColumn("Arrival");
        dayColumn = new JFXTreeTableColumn("Day-start");
        recurrencesColumn = new JFXTreeTableColumn("Recurrences");


        treeTableView.getColumns().addAll(idColumn,nameColumn,dayColumn,checkOutColumn,arrivalColumn,recurrencesColumn);
        treeTableView.setPrefSize(700,150);
        idColumn.setPrefWidth(90);
        dayColumn.setPrefWidth(90);
        checkOutColumn.setPrefWidth(120);
        nameColumn.setPrefWidth(130);
        arrivalColumn.setPrefWidth(120);
        recurrencesColumn.setPrefWidth(130);

        treeTableView.getStylesheets().add("/org/css/tableLignment.css");





    }

    public void show(){
        FxUtilities.openNewStage(treeTableView,"table");
    }

    public JFXTreeTableView<OfferTripProperty> getTreeTableView() {
        return treeTableView;
    }

    public JFXTreeTableColumn<OfferTripProperty,Integer> getIdColumn() {
        return idColumn;
    }


    public JFXTreeTableColumn getNameColumn() {
        return nameColumn;
    }

    public JFXTreeTableColumn getCheckOutColumn() {
        return checkOutColumn;
    }

    public JFXTreeTableColumn getArrivalColumn() {
        return arrivalColumn;
    }

    public JFXTreeTableColumn getDayColumn() {
        return dayColumn;
    }

    public JFXTreeTableColumn getRecurrencesColumn() {
        return recurrencesColumn;
    }
}
