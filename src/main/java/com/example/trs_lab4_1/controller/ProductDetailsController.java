package com.example.trs_lab4_1.controller;

import com.example.trs_lab4_1.dto.ParameterWithValue;
import com.example.trs_lab4_1.dto.ParametersByProductDto;
import com.example.trs_lab4_1.dto.ProductWithParametersDto;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDetailsController {

    @FXML private Label productIdLabel;
    @FXML private Label productNameLabel;
    @FXML private Label productDescriptionLabel;
    @FXML private Label productGroupIdLabel;
    @FXML private Label productGroupNameLabel;

    @FXML private TableView<ParameterWithValue> paramsTable;
    @FXML private TableColumn<ParameterWithValue, Long> paramIdColumn;
    @FXML private TableColumn<ParameterWithValue, String> paramNameColumn;
    @FXML private TableColumn<ParameterWithValue, String> paramUnitColumn;
    @FXML private TableColumn<ParameterWithValue, String> paramValueColumn;

    @FXML
    private void initialize() {
        // настройка колонок (можно и в setDtoTable, но initialize логичнее)
        paramIdColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().parameterId()).asObject()
        );

        paramNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().parameterName())
        );

        paramUnitColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().parameterUnit())
        );

        paramValueColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().parameterValue())
        );
    }

    public void setDtoTable(List<ParametersByProductDto> dtoTable) {
        if (dtoTable == null || dtoTable.isEmpty()) {
            return;
        }

        ParametersByProductDto dto = dtoTable.get(0);

        // заполняем лейблы по продукту
        productIdLabel.setText(String.valueOf(dto.getProduct().productId()));
        productNameLabel.setText(dto.getProduct().productName());
        productDescriptionLabel.setText(dto.getProduct().productDescription());
        productGroupIdLabel.setText(String.valueOf(dto.getProduct().productGroupId()));
        productGroupNameLabel.setText(dto.getProduct().productGroupName());

        // параметры
        paramsTable.setItems(FXCollections.observableArrayList(dto.getParameters()));
    }
}
