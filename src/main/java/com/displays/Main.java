package com.displays;

import com.displays.view.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Created by victor on 09.10.15.
 */
public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
        FXMLLoader startLoader = new FXMLLoader(getClass().getResource("/fxml/startwindow.fxml"));
        FXMLLoader firstLoader = new  FXMLLoader(getClass().getResource("/fxml/sysMonitor.fxml"));
        FXMLLoader secondLoader = new FXMLLoader(getClass().getResource("/fxml/aimer.fxml"));
        FXMLLoader thirdLoader = new FXMLLoader(getClass().getResource("/fxml/driverMonitor.fxml"));
        FXMLLoader camLoader = new FXMLLoader((getClass().getResource("/fxml/camsDisplay.fxml")));
        FXMLLoader bosLoader = new FXMLLoader(getClass().getResource("/fxml/bossDisplay.fxml"));

        Player player = new Player();

        Scene startScene = new Scene(startLoader.load());
        Scene firstScene = new Scene(firstLoader.load());
        Scene secondScene = new Scene(secondLoader.load());
        Scene thirdScene = new Scene(thirdLoader.load());
        Scene camScene = new Scene(camLoader.load());
        Scene bossScene = new Scene(bosLoader.load());

        StartController startController = startLoader.getController();
        startController.setStage(primaryStage);
        startController.setNextScene(firstScene);

        SysController sysController = firstLoader.getController();
        sysController.setStage(primaryStage);
        sysController.setNextScene(secondScene);

        Controller controller = secondLoader.getController();
        controller.setNextScene(thirdScene);
        controller.setStage(primaryStage);

        DriverController driverController = thirdLoader.getController();
        driverController.setNextScene(camScene);
        driverController.setStage(primaryStage);

        CamController camController = camLoader.getController();
        camController.setStage(primaryStage);
        camController.setNextScene(bossScene);

        BossController bossController = bosLoader.getController();
        bossController.setStage(primaryStage);
        bossController.setNextScene(firstScene);

        startController.setController(sysController);
        sysController.setController(controller);
        controller.setDriverController(driverController);
        driverController.setController(camController);
        camController.setController(bossController);
        bossController.setSysController(sysController);

        startController.setPlayer(player);
        controller.setPlayer(player);
        driverController.setPlayer(player);
        camController.setPlayer(player);
        bossController.setPlayer(player);


        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
