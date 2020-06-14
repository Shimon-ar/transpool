package org.controllers;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.transpool.engine.ds.TranspoolTrip;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StarRankController {

    public static  List<Pane> getStarList(double rank){

        InputStream star = null;
        InputStream halfStar = null;
        ClassLoader classLoader = StarRankController.class.getClassLoader();
        try {
                star = StarRankController.class.getResourceAsStream("/org/css/star.jpg");
                halfStar = StarRankController.class.getResourceAsStream("/org/css/halfStar.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    

        List<Pane> list = new ArrayList<>();

        Image imageStar = new Image(star);
        int i;
        for(i = 0;i<(int)rank;i++){
            ImageView imageView = new ImageView(imageStar);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            Pane pane = new Pane();
            pane.prefHeight(25);
            pane.prefWidth(14);
            pane.getChildren().add(imageView);
            list.add(pane);
        }
        if(rank - i > 0 ){
            Image imageHalfStar = new Image(halfStar);
            ImageView imageView = new ImageView(imageHalfStar);
            imageView.setFitHeight(25);
            imageView.setFitWidth(14);
            Pane pane = new Pane();
            pane.prefHeight(25);
            pane.prefWidth(14);
            pane.getChildren().add(imageView);
            list.add(pane);
        }
        return list;
    }
}
