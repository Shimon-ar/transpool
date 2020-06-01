package org.controllers;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableColumn;
import org.components.RequestsTable;
import org.ds.RequestTripProperty;

import java.util.function.Function;

public class RequestTableController {
    private RequestsTable requestsTable;
    private ObservableList<RequestTripProperty> requestTripPropertyObservableList;

    public RequestTableController(ObservableList<RequestTripProperty> requestTripPropertyObservableList ) {
        requestsTable = new RequestsTable();
        this.requestTripPropertyObservableList = requestTripPropertyObservableList;
        setTable();
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<RequestTripProperty, T> column, Function<RequestTripProperty, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<RequestTripProperty, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    public void setTable(){

        setupCellValueFactory(requestsTable.getDayColumn(),RequestTripProperty::dayProperty );
        setupCellValueFactory(requestsTable.getDestinationColumn(), RequestTripProperty::toProperty);
        setupCellValueFactory(requestsTable.getIdColumn(), requestTripProperty -> requestTripProperty.idProperty().asObject());
        setupCellValueFactory(requestsTable.getMatchedColumn(), RequestTripProperty::matchedProperty);
        setupCellValueFactory(requestsTable.getNameColumn(), RequestTripProperty::nameProperty);
        setupCellValueFactory(requestsTable.getSourceColumn(), RequestTripProperty::fromProperty);

        setupCellValueFactory(requestsTable.getTimeColumn(),RequestTripProperty::timeProperty);
        setupCellValueFactory(requestsTable.getCheckArrival(), RequestTripProperty::checkArrivalProperty);

        requestsTable.getTreeTableView().setRoot(new RecursiveTreeItem<>(requestTripPropertyObservableList, RecursiveTreeObject::getChildren));
        requestsTable.getTreeTableView().setShowRoot(false);

    }
}
