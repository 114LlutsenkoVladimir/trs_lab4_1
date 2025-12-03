package com.example.trs_lab4_1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class HeaderController {

    private final ApplicationContext applicationContext;

    public HeaderController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @FXML
    private void openProducts(ActionEvent event) {
        openView("/com/example/trs_lab4_1/products.fxml", event);
    }

    @FXML
    private void openProductGroups(ActionEvent event) {
        openView("/com/example/trs_lab4_1/productGroups.fxml", event);
    }

    @FXML
    private void openParameters(ActionEvent event) {
        openView("/com/example/trs_lab4_1/parameters.fxml", event);
    }

    private void openView(String fxmlPath, ActionEvent event) {
        try {
            var url = HeaderController.class.getResource(fxmlPath);
            if (url == null) {
                System.err.println("FXML not found: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            loader.setControllerFactory(applicationContext::getBean);

            Parent rootNode = loader.load();

            // Берём Stage из кнопки, по которой кликнули
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(rootNode));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
