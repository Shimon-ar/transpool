package org.controllers;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableColumn;
import org.components.RequestsTable;
import org.ds.OfferTripProperty;
import org.ds.RequestTripProperty;
import org.fxUtilities.FxUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RequestTableController {
    private RequestsTable requestsTable;
    private ObservableList<RequestTripProperty> requestTripPropertyObservableList;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public RequestTableController(ObservableList<RequestTripProperty> requestTripPropertyObservableList ) {
        requestsTable = new RequestsTable();
        this.requestTripPropertyObservableList = requestTripPropertyObservableList;
        setTable();

        requestsTable.getTreeTableView().setOnMouseClicked(event -> {
            RequestTripProperty requestTripProperty = requestsTable.getTreeTableView().getSelectionModel().getSelectedItem().getValue();
            if (event.getClickCount() == 1)
                if (mainController.isAnimationPlay() && requestTripProperty.isIsMatched())
                {//what to do
                    }
            if (event.getClickCount() == 2) {
                FxUtilities.showMassage(mainController.getStage(),createMassage(requestTripProperty),
                        "Request trip: " + requestTripProperty.getName(),false);

            }
        });
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

    public RequestsTable getRequestsTable() {
        return requestsTable;
    }

    public void addRequest(RequestTripProperty requestTripProperty){
        requestTripPropertyObservableList.add(requestTripProperty);
    }

    private String createMassage(RequestTripProperty requestTripProperty){
        String details = "id: " + requestTripProperty.getRequestTrip().getId() + "\n" +
                "name: " + requestTripProperty.getRequestTrip().getName() + "\n" +
                requestTripProperty.getRequestTrip().getFrom() + " - " + requestTripProperty.getRequestTrip().getTo() + "\n" +
                requestTripProperty.getRequestTrip().getRequestTime().getWhichTime().name() + " " + requestTripProperty.getRequestTrip().getRequestTime().getTime() + " day: " + requestTripProperty.getRequestTrip().getRequestTime().getTime().getDay();

        if(requestTripProperty.isIsMatched())
            return details + "\n\n" +
                    "match details" + "\n" +
                    "offer/s name: " + requestTripProperty.getRequestTrip().getMatch().getOffersNames().stream().collect(Collectors.joining(",")) + "\n" +
                    "cost: " + requestTripProperty.getRequestTrip().getMatch().getCost() + "\n" +
                    "average fuel utilization: " + requestTripProperty.getRequestTrip().getMatch().getAvgFoul() + "\n" +
                    "time: " + requestTripProperty.getRequestTrip().getMatch().getStartTime() + " - " + requestTripProperty.getRequestTrip().getMatch().getFinishTime();
        return details;
    }

    public List<RequestTripProperty> getRequestTripPropertyObservableList() {
        return new ArrayList<>(requestTripPropertyObservableList);
    }
}
