package org.controllers;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.components.OffersTable;
import org.ds.OfferTripProperty;
import org.fxUtilities.FxUtilities;

import java.util.function.Function;
import java.util.stream.Collectors;

public class OfferTableController {

    private ObservableList<OfferTripProperty> offerTripPropertyObservableList;
    private OffersTable offersTable;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public OfferTableController(ObservableList<OfferTripProperty> offerTripPropertyObservableList) {
        this.offerTripPropertyObservableList = offerTripPropertyObservableList;
        offersTable = new OffersTable();
        setTable();

        offersTable.getTreeTableView().setOnMouseClicked(event -> {
            OfferTripProperty offerTripProperty = offersTable.getTreeTableView().getSelectionModel().getSelectedItem().getValue();
            if (event.getClickCount() == 1)
                if (mainController.isAnimationPlay())
                    mainController.makeAnimation(offerTripProperty.getOfferTrip().getRoute());
            if (event.getClickCount() == 2) {
                offerTripProperty.updateDynamicData(mainController.getEngine());
                FxUtilities.showMassage(mainController.getStage(),createMassage(offerTripProperty),
                        "Offer trip: " + offerTripProperty.getName(),false);

            }
        });

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

    public void addOffer(OfferTripProperty offerTripProperty){
        offerTripPropertyObservableList.add(offerTripProperty);
    }

    private String createMassage(OfferTripProperty offerTripProperty){
        String details = "id: " + offerTripProperty.getOfferTrip().getId() + "\n" +
                "name: " + offerTripProperty.getOfferTrip().getName() + "\n" +
                "route: " + offerTripProperty.getOfferTrip().getRoute().stream().collect(Collectors.joining(",")) +"\n" +
                "cost: " + offerTripProperty.getOfferTrip().getCost() + "\n" +
                "time: " +offerTripProperty.getCheckout() + "-" + offerTripProperty.getArrival() + "\n" +
                "average fuel utilization: " + offerTripProperty.getOfferTrip().getFuelCon();

         String capacity = offerTripProperty.getTimeList().stream()
                .map(String::valueOf).
                        collect(Collectors.joining("\n"));
         String upDown = offerTripProperty.getUpDownPassengers().stream()
                 .map(String::valueOf).collect(Collectors.joining("\n"));
         if(!upDown.isEmpty())
             return details + "\n" + offerTripProperty.getRequestsId() + "\n" + capacity + "\n" + upDown;
         else
             return details + "\n" + offerTripProperty.getRequestsId() +
                     "\n" + capacity;
    }
}
