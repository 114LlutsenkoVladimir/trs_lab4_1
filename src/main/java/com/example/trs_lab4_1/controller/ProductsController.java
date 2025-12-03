package com.example.trs_lab4_1.controller;

import com.example.trs_lab4_1.entity.Parameter;
import com.example.trs_lab4_1.entity.Product;
import com.example.trs_lab4_1.entity.ProductGroup;
import com.example.trs_lab4_1.service.ParameterService;
import com.example.trs_lab4_1.service.ProductGroupService;
import com.example.trs_lab4_1.service.ProductParameterValueService;
import com.example.trs_lab4_1.service.ProductService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;
    private final ProductParameterValueService productParameterValueService;
    private final ParameterService parameterService;
    private final ProductGroupService productGroupService;
    private final ApplicationContext applicationContext;

    // UI из FXML
    @FXML private VBox parametersBox;

    @FXML private ComboBox<ProductGroup> productGroupCombo;
    @FXML private ComboBox<Parameter> parameterCombo;

    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, Long> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> descriptionColumn;
    @FXML private TableColumn<Product, String> releaseDateColumn;
    @FXML private TableColumn<Product, String> groupColumn;
    @FXML private TableColumn<Product, Void> actionsColumn;

    @FXML private Label emptyLabel;

    // Чтобы легко собрать выбранные parameterIds
    private final List<CheckBox> parameterCheckBoxes = new ArrayList<>();

    @FXML
    public void initialize() {
        // === аналог @ModelAttribute("allParameters") ===
        List<Parameter> allParameters = parameterService.findAll();
        buildParameterCheckboxes(allParameters);

        // === аналог @ModelAttribute("allProductGroups") ===
        List<ProductGroup> allProductGroups = productGroupService.findAll();
        productGroupCombo.setItems(FXCollections.observableArrayList(allProductGroups));
        productGroupCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProductGroup pg) {
                return pg == null ? "" : pg.getName();
            }

            @Override
            public ProductGroup fromString(String string) {
                return productGroupCombo.getItems().stream()
                        .filter(pg -> pg.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // тот же список параметров можно использовать для combo "productWithoutParameter"
        parameterCombo.setItems(FXCollections.observableArrayList(allParameters));
        parameterCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Parameter p) {
                return p == null ? "" : p.getName();
            }

            @Override
            public Parameter fromString(String string) {
                return parameterCombo.getItems().stream()
                        .filter(p -> p.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // === аналог @ModelAttribute("allProducts") при загрузке страницы ===
        List<Product> allProducts = productService.findAll();
        setProducts(allProducts);

        // настройка колонок таблицы
        idColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getId()));

        nameColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));

        descriptionColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getDescription()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        releaseDateColumn.setCellValueFactory(cell -> {
            var date = cell.getValue().getReleaseDate();
            String text = (date != null) ? date.format(formatter) : "";
            return new javafx.beans.property.SimpleStringProperty(text);
        });

        groupColumn.setCellValueFactory(cell -> {
            ProductGroup pg = cell.getValue().getProductGroup();
            return new javafx.beans.property.SimpleStringProperty(
                    pg != null ? pg.getName() : ""
            );
        });

        // колонка actions с кнопкой "глаз"
        initActionsColumn();

        productsTable.setPlaceholder(new Label("Немає продуктів, що задовольняють умову."));
    }

    private void buildParameterCheckboxes(List<Parameter> allParameters) {
        parametersBox.getChildren().clear();
        parameterCheckBoxes.clear();

        for (Parameter p : allParameters) {
            CheckBox cb = new CheckBox(p.getName());
            cb.setUserData(p.getId()); // чтобы потом забрать id

            parametersBox.getChildren().add(cb);
            parameterCheckBoxes.add(cb);
        }
    }

    private void setProducts(List<Product> products) {
        productsTable.setItems(FXCollections.observableArrayList(products));
        emptyLabel.setVisible(products == null || products.isEmpty());
    }

    private void initActionsColumn() {
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("👁");

            {
                btn.getStyleClass().add("btn-info");
                btn.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    if (product != null) {
                        showProductDetails(product);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    // === аналог /products/productsByProductGroupId ===
    @FXML
    private void onFilterByProductGroup() {
        ProductGroup pg = productGroupCombo.getValue();
        if (pg == null) {
            showError("Будь ласка, оберіть групу.");
            return;
        }
        List<Product> products = productService.findProductsByProductGroup(pg.getId());
        setProducts(products);
    }

    // === аналог /products/productWithoutParameter ===
    @FXML
    private void onFilterWithoutParameter() {
        Parameter parameter = parameterCombo.getValue();
        if (parameter == null) {
            showError("Будь ласка, оберіть параметр.");
            return;
        }
        List<Product> products = productService.findProductsWithoutParameter(parameter.getId());
        setProducts(products);
    }

    // === аналог /products/deleteProductsWithParameters ===
    @FXML
    private void onDeleteByParameters() {
        List<Long> parameterIds = parameterCheckBoxes.stream()
                .filter(CheckBox::isSelected)
                .map(cb -> (Long) cb.getUserData())
                .collect(Collectors.toList());

        if (parameterIds.isEmpty()) {
            showError("Оберіть хоча б один параметр.");
            return;
        }

        // как в Spring-контроллере:
        List<Product> products = productService.deleteProductsByParameterIds(parameterIds);
        setProducts(products);
    }

    // === модальное окно с деталями продукта (аналог Bootstrap modal + product-details.html) ===
    private void showProductDetails(Product product) {
        try {
            var dtoTable = productParameterValueService.getParametersByProduct(product.getId());

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/trs_lab4_1/product-details.fxml")
            );
            loader.setControllerFactory(applicationContext::getBean);

            Parent root = loader.load();

            ProductDetailsController controller = loader.getController();
            controller.setDtoTable(dtoTable); // <-- тут labels должны уже быть проинъектированы

            Stage dialog = new Stage();
            dialog.setTitle("Інформація про продукт");
            dialog.initOwner(productsTable.getScene().getWindow());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
