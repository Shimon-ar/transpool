package org.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.utils.JFXUtilities;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.components.OffersTable;
import org.ds.OfferTripProperty;
import org.fxUtilities.FxUtilities;
import org.tasks.LoadMapTask;
import org.transpool.engine.ds.TranspoolTrip;

import java.util.List;
import java.util.SimpleTimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.controllers.StarRankController.getStarList;

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
        //rankChanged = new SimpleBooleanProperty(false);

        offersTable.getTreeTableView().setOnMouseClicked(event -> {
            OfferTripProperty offerTripProperty = offersTable.getTreeTableView().getSelectionModel().getSelectedItem().getValue();
            if (event.getClickCount() == 1)
                if (mainController.isAnimationPlay())
                    mainController.makeAnimation(offerTripProperty.getOfferTrip().getRoute());
            if (event.getClickCount() == 2) {
                showMassage(mainController.getStage(),createMassage(offerTripProperty),
                        "Offer trip: " + offerTripProperty.getName(),false,offerTripProperty.getOfferTrip());

            }
        });

    }

    private void actionOnRank(TranspoolTrip transpoolTrip,SimpleBooleanProperty rankChanged){
        JFXAlert alert = new JFXAlert(mainController.getStage());
         List<Pane> list = StarRankController.getStarList(5);
        HBox hBox = new HBox();
        for(int i=0;i<list.size();i++){
            JFXRippler rippler = new JFXRippler(list.get(i));
            rippler.setMaskType(JFXRippler.RipplerMask.CIRCLE);
            rippler.setRipplerFill(Paint.valueOf("#FFD700"));
            hBox.getChildren().add(rippler);
            int finalI = i + 1;
            rippler.setOnMouseClicked(myEvent->{
                transpoolTrip.addRank(finalI);
                rankChanged.set(true);
                alert.hideWithAnimation();
                showMassageAtFinishRank(transpoolTrip,finalI);
            });
        }


        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        alert.setSize(400,150);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("Choose how many stars you want to give:"));
        layout.setBody(hBox);
        alert.setContent(layout);
        alert.show();


   }

   private void showMassageAtFinishRank(TranspoolTrip transpoolTrip,int rank){
       JFXAlert alert = new JFXAlert(mainController.getStage());
       alert.initModality(Modality.APPLICATION_MODAL);
       alert.setOverlayClose(false);
       alert.setSize(400,250);
       JFXDialogLayout layout = new JFXDialogLayout();
       layout.setHeading(new Label("You gave: " + rank + " stars"));
       JFXTextArea textArea = new JFXTextArea();
       textArea.setPromptText("would you like to write something about: " + transpoolTrip.getName() + "?");
       layout.setBody(textArea);
       JFXButton closeButton = new JFXButton("Done");
           closeButton.setOnAction(myEvent -> {
               alert.hideWithAnimation();
               if (textArea.getText() != null && !textArea.getText().isEmpty()) {
                   transpoolTrip.addFeedback(textArea.getText().replaceAll(",", System.getProperty("line.separator")));
               }
           });

       layout.setActions(closeButton);
       alert.setContent(layout);
       alert.show();

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
                "capacity: " + offerTripProperty.getCapacityProperty().get() + "\n" +
                "route: " + offerTripProperty.getOfferTrip().getRoute().stream().collect(Collectors.joining(",")) +"\n" +
                "cost: " + offerTripProperty.getOfferTrip().getCost() + "\n" +
                "time: " +offerTripProperty.getCheckout() + "-" + offerTripProperty.getArrival() + "\n" +
                "average fuel utilization: " + offerTripProperty.getOfferTrip().getFuelCon();
/*
         String capacity = offerTripProperty.getTimeList().stream()
                .map(String::valueOf).
                        collect(Collectors.joining("\n"));
         String upDown = offerTripProperty.getUpDownPassengers().stream()
                 .map(String::valueOf).collect(Collectors.joining("\n"));


 */
        /* if(!upDown.isEmpty())
             return details + "\n" + offerTripProperty.getRequestsId() + "\n" + capacity + "\n" + upDown;
         else
             return details + "\n" + offerTripProperty.getRequestsId() +
                     "\n" + capacity;*/
        return details;
    }

    public void showMassage(Stage stage, String body, String head, boolean closeCurrentStage, TranspoolTrip transpoolTrip){
        SimpleBooleanProperty rankChanged = new SimpleBooleanProperty(false);
        JFXAlert alert = new JFXAlert(stage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        alert.setSize(270,400);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label(head));
        HBox hBox = new HBox();
        hBox.prefHeight(30);
        hBox.prefWidth(150);
        hBox.getChildren().addAll(getStarList(transpoolTrip.getRank()));
        layout.setBody(hBox,new Label(body));
        rankChanged.addListener((o,n,m)->{
            if(m){
                layout.getBody().removeAll();
                hBox.getChildren().clear();
                hBox.getChildren().addAll(getStarList(transpoolTrip.getRank()));
                rankChanged.set(false);
                layout.setBody(hBox,new Label(body));

            }
        });
        JFXButton closeButton = new JFXButton("Done");
        if(closeCurrentStage) {
            closeButton.setOnAction(myEvent -> {
                alert.hideWithAnimation();
                stage.close();
            });
        }
        else{
            closeButton.setOnAction(myEvent -> {
                alert.hideWithAnimation();
            });
        }

        JFXButton rankButton = new JFXButton("Rank me");
        JFXButton feedbackButton = new JFXButton("Feedbacks");
        feedbackButton.setOnAction(myEvent->{
            actionOnFeedback(transpoolTrip);
        });
        rankButton.setOnAction(myEvent->{
            actionOnRank(transpoolTrip,rankChanged);
        });

        layout.setActions(rankButton,feedbackButton,closeButton);

        alert.setContent(layout);
        alert.show();


    }

    public void actionOnFeedback(TranspoolTrip transpoolTrip){
        List<String> feedbacks = transpoolTrip.getFeedbackList();
        if(feedbacks.isEmpty()){
            FxUtilities.showAlert(mainController.getStage(),"",false,"no feedbacks",20,160);
            return;

        }
        String feedbackString = "";
        for(int i=1;i<=feedbacks.size();i++){
            feedbackString = feedbackString.concat(i + ". " + feedbacks.get(i-1) + "\n");
        }
        FxUtilities.showAlert(mainController.getStage(),feedbackString,false,"Feedbacks",300,400);
    }
}
