package org.components;

import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.scene.control.Tab;

public class Tabs {

    private JFXTabPane tabPane;

    public Tabs(JFXTreeTableView offersTable,JFXTreeTableView requestsTable) {
        this.tabPane = new JFXTabPane();
        Tab offersTab = new Tab();
        offersTab.setText("Trip offers");
        offersTab.setContent(offersTable);

        Tab requestTab = new Tab();
        requestTab.setContent(requestsTable);
        requestTab.setText("Trip requests");

        tabPane.getTabs().addAll(offersTab,requestTab);
    }

    public JFXTabPane getTabPane() {
        return tabPane;
    }
}
