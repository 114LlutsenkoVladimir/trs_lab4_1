package com.example.trs_lab4_1;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class HelloApplication extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        // поднимаем Spring-контекст ОДИН РАЗ
        springContext = new SpringApplicationBuilder(SpringBootConfig.class)
                .web(WebApplicationType.NONE) // без веб-сервера
                .run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("/com/example/trs_lab4_1/products.fxml")
                // или другой твой главный fxml
        );

        // важный момент: контроллеры берём из Spring
        loader.setControllerFactory(springContext::getBean);

        Scene scene = new Scene(loader.load(), 900, 600);
        stage.setTitle("Products");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

