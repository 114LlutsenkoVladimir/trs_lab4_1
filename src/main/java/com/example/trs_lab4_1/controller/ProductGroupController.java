package com.example.trs_lab4_1.controller;

import com.example.trs_lab4_1.entity.ParameterGroup;
import com.example.trs_lab4_1.entity.ProductGroup;
import com.example.trs_lab4_1.entity.ProductGroupParameterGroup; // имя класса связи – подставь своё
import com.example.trs_lab4_1.service.ParameterGroupService;
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
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductGroupController {

    private final ProductGroupService productGroupService;
    private final ParameterGroupService parameterGroupService;

    @FXML
    private ComboBox<ProductGroup> fromGroupCombo;

    @FXML
    private ComboBox<ProductGroup> toGroupCombo;

    @FXML
    private ComboBox<ParameterGroup> parameterGroupCombo;

    @FXML
    private TableView<ProductGroup> productGroupTable;

    @FXML
    private TableColumn<ProductGroup, Long> idColumn;

    @FXML
    private TableColumn<ProductGroup, String> nameColumn;

    @FXML
    private TableColumn<ProductGroup, String> paramGroupsColumn;

    @FXML
    private Label emptyLabel;


    @FXML
    public void initialize() {
        // === аналог @ModelAttribute("allProductGroups") ===
        List<ProductGroup> allProductGroups = productGroupService.findAll();
        var productGroupsObs = FXCollections.observableArrayList(allProductGroups);
        fromGroupCombo.setItems(productGroupsObs);
        toGroupCombo.setItems(productGroupsObs);

        // === аналог @ModelAttribute("allParameterGroups") ===
        List<ParameterGroup> allParameterGroups = parameterGroupService.findAll();
        parameterGroupCombo.setItems(FXCollections.observableArrayList(allParameterGroups));

        // как показывать ProductGroup в ComboBox
        StringConverter<ProductGroup> productGroupConverter = new StringConverter<>() {
            @Override
            public String toString(ProductGroup pg) {
                return pg == null ? "" : pg.getName();
            }

            @Override
            public ProductGroup fromString(String string) {
                return fromGroupCombo.getItems().stream()
                        .filter(pg -> pg.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        };
        fromGroupCombo.setConverter(productGroupConverter);
        toGroupCombo.setConverter(productGroupConverter);

        // как показывать ParameterGroup в ComboBox
        parameterGroupCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(ParameterGroup pg) {
                return pg == null ? "" : pg.getName();
            }

            @Override
            public ParameterGroup fromString(String string) {
                return parameterGroupCombo.getItems().stream()
                        .filter(pg -> pg.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // === заполнение таблицы (аналог startPage) ===
        refreshTable();

        // настройки колонок
        idColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getId()));

        nameColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));

        // здесь предполагаю, что у ProductGroup есть getProductGroupParameterGroups()
        // и у связи – getParameterGroup().getName()
        paramGroupsColumn.setCellValueFactory(cell -> {
            ProductGroup group = cell.getValue();
            String text = "";
            if (group.getProductGroupParameterGroups() != null) {
                text = group.getProductGroupParameterGroups().stream()
                        .map(pgRel -> {
                            // подставь реальные имена методов
                            // например: pgRel.getParameterGroup().getName()
                            ParameterGroup pg = pgRel.getParameterGroup();
                            return pg != null ? pg.getName() : "";
                        })
                        .filter(s -> !s.isEmpty())
                        .distinct()
                        .collect(Collectors.joining(", "));
            }
            return new javafx.beans.property.SimpleStringProperty(text);
        });

        // сообщение, если таблица пуста
        productGroupTable.setPlaceholder(
                new Label("Немає продуктів, що задовольняють умову."));
    }

    private void refreshTable() {
        List<ProductGroup> productGroups = productGroupService.findAll();
        productGroupTable.setItems(FXCollections.observableArrayList(productGroups));
        emptyLabel.setVisible(productGroups.isEmpty());
    }

    // === аналог @GetMapping("/moveParametersToAnotherGroup") ===
    @FXML
    private void onMove() {
        ProductGroup from = fromGroupCombo.getValue();
        ProductGroup to = toGroupCombo.getValue();
        ParameterGroup parameterGroup = parameterGroupCombo.getValue();

        if (from == null || to == null || parameterGroup == null) {
            showError("Будь ласка, оберіть всі значення перед переміщенням.");
            return;
        }

        try {
            // как в контроллере: moveParameterGroup(from, to, parameterGroupId)
            productGroupService.moveParameterGroup(
                    from.getId(),
                    to.getId(),
                    parameterGroup.getId()
            );
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }

        // после операции – обновить таблицу
        refreshTable();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText("Помилка");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
