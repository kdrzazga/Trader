package org.kd.main.client.view;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kd.main.client.presenter.PresenterHandler;
import org.kd.main.client.view.lib.PropertiesReader;
import org.kd.main.common.TraderConfig;

public class Trader extends Application {

    private PresenterHandler handler;

    @Override
    public void start(Stage primaryStage) throws Exception {
        var context = new AnnotationConfigApplicationContext(TraderConfig.class);

        this.handler = context.getBean(PresenterHandler.class);

        initialize();

        var loader = new FXMLLoader(getClass().getResource("main_form.fxml"));
        Parent root = loader.load();
        loader.getClass().getResource("customer_details_panel.fxml");

        setupPrimaryStage(primaryStage, root);
        //setupSecondaryStage()

        TraderViewController.setHandler(this.handler);
        CustomerDetailsPanel.setHandler(this.handler);

        ((TraderViewController)loader.getController()).loadBanks();
        ((TraderViewController)loader.getController()).loadCustomers();
        ((TraderViewController)loader.getController()).loadTrades();
    }

    private void setupPrimaryStage(Stage primaryStage, Parent root) {
        primaryStage.setTitle(new PropertiesReader().readKey("app.title"));
        primaryStage.setIconified(false);
        primaryStage.setScene(new Scene(root, 450, 565));
        primaryStage.setOnCloseRequest(event -> exit());

        primaryStage.show();
    }
    private void setupSecondaryStage(Stage primaryStage, Parent root) {
        primaryStage.setTitle(new PropertiesReader().readKey("app.title"));
        primaryStage.setIconified(false);
        primaryStage.setScene(new Scene(root, 450, 565));
        primaryStage.setOnCloseRequest(event -> exit());

        primaryStage.show();
    }

    private void initialize() {
        this.handler.initApplication();
    }

    private void exit() {
        this.handler.saveDb();
    }

    public static void start(String[] args) {
        launch(args);
    }

}