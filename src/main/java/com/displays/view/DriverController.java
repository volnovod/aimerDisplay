package com.displays.view;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * Created by victor on 30.10.15.
 */
public class DriverController {
    private Stage stage;
    private Browser browser;
    private boolean isMap=false;

    @FXML
    private Button changeDisplay;

    @FXML
    private WebView mapWebView;

    @FXML
    private Pane mapPane;

    public CamController getController() {
        return controller;
    }

    public void setController(CamController controller) {
        this.controller = controller;
    }

    private CamController controller;

    public void setNextScene(Scene nextScene) {
        this.nextScene = nextScene;
    }

    private Scene nextScene;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void displayChanger(){

        stage.setScene(getNextScene());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        player.stop();
                        getController().init();
                    }
                });
            }
        }).start();
    }

    public Scene getNextScene() {
        return nextScene;
    }



    @FXML
    private Pane videoPane;
    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void init(){
        player.setPlayerHolder(videoPane);
        player.initialize();
        browser = new Browser(mapWebView);

    }

    public void mapCamChanger(){
        if(isMap){
            mapPane.setVisible(false);
            this.isMap = false;
            return;
        }
        if (!isMap){
            mapPane.setVisible(true);
            this.isMap = true;
            return;
        }

    }
}
