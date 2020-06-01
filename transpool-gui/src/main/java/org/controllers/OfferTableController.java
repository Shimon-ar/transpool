package org.controllers;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableColumn;
import org.components.OffersTable;
import org.ds.OfferTripProperty;

import java.util.function.Function;

public class OfferTableController {

    private ObservableList<OfferTripProperty> offerTripPropertyObservableList;
    private OffersTable offersTable;

    public OfferTableController(ObservableList<OfferTripProperty> offerTripPropertyObservableList) {
        this.offerTripPropertyObservableList = offerTripPropertyObservableList;
        offersTable = new OffersTable();
        setTable();

    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<OfferTripProperty, T> column, Function<OfferTripProperty, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<OfferTripProperty, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });

    }


    public void setTable(){

        setupCellValueFactory(offersTable.getArrivalColumn(),OfferTripProperty::arrivalProperty);

        setupCellValueFactory(offersTable.getDayColumn(), OfferTripProperty::dayProperty);

        setupCellValueFactory(offersTable.getIdColumn(), offerTripProperty -> offerTripProperty.idProperty().asObject());

        setupCellValueFactory(offersTable.getNameColumn(), OfferTripProperty::nameProperty);

        setupCellValueFactory(offersTable.getRecurrencesColumn(),OfferTripProperty::recurrencesProperty);

        setupCellValueFactory(offersTable.getCheckOutColumn(), OfferTripProperty::checkoutProperty);

        offersTable.getTreeTableView().setRoot(new RecursiveTreeItem<>(offerTripPropertyObservableList, RecursiveTreeObject::getChildren));
        offersTable.getTreeTableView().setShowRoot(false);

    }

    public OffersTable getOffersTable() {

        return offersTable;

    }
}
