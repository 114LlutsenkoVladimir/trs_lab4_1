package com.example.trs_lab4_1.controller;

import com.example.trs_lab4_1.entity.Parameter;
import com.example.trs_lab4_1.entity.ProductGroup;
import com.example.trs_lab4_1.service.ParameterService;
import com.example.trs_lab4_1.service.ProductGroupService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ParameterController {

    private final ParameterService parameterService;
    private final ProductGroupService productGroupService;

    @FXML
    private ComboBox<ProductGroup> groupComboBox;

    @FXML
    private TableView<Parameter> parameterTable;

    @FXML
    private TableColumn<Parameter, Long> idColumn;

    @FXML
    private TableColumn<Parameter, String> nameColumn;

    @FXML
    private TableColumn<Parameter, String> unitColumn;

    @FXML
    private TableColumn<Parameter, String> groupColumn;

    @FXML
    private Label emptyLabel;

    @FXML
    public void initialize() {
        // === аналог @ModelAttribute("allProductGroups") ===
        List<ProductGroup> groups = productGroupService.findAll();
        groupComboBox.setItems(FXCollections.observableArrayList(groups));

        // как отображать группы в ComboBox
        groupComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProductGroup group) {
                return group == null ? "" : group.getName();
            }

            @Override
            public ProductGroup fromString(String string) {
                return groupComboBox.getItems().stream()
                        .filter(g -> g.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // === настройка колонок таблицы (аналог th:text) ===
        idColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getId()));

        nameColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));

        unitColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getUnit()));

        groupColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getParameterGroup() != null
                                ? cell.getValue().getParameterGroup().getName()
                                : ""
                ));

        // сообщение по умолчанию
        parameterTable.setPlaceholder(new Label("Немає продуктів, що задовольняють умову."));
    }

    // === аналог @GetMapping("/parametersByGroup") ===
    @FXML
    private void onSearch() {
        ProductGroup selectedGroup = groupComboBox.getValue();

        if (selectedGroup == null) {
            parameterTable.getItems().clear();
            emptyLabel.setVisible(true);
            return;
        }

        // в Spring было: parameterService.getParametersByProductGroup(productGroupId)
        List<Parameter> parameters =
                parameterService.getParametersByProductGroup(selectedGroup.getId());

        parameterTable.setItems(FXCollections.observableArrayList(parameters));
        emptyLabel.setVisible(parameters.isEmpty());
    }
}
