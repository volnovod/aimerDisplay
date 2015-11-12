package com.displays.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * Created by victor on 09.11.15.
 */
public class CamController {

    private Stage stage;
    private Scene nextScene;
    private Player player;
    private Browser browser;
    private boolean isMap =false;

    @FXML
    private WebView mapWebView;

    @FXML
    private Pane mapPane;

    public BossController getController() {
        return bossController;
    }

    public void setController(BossController bossController) {
        this.bossController = bossController;
    }

    private BossController bossController;

    @FXML
    private Pane videoPane;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getNextScene() {
        return nextScene;
    }

    public void setNextScene(Scene nextScene) {
        this.nextScene = nextScene;
    }

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

    public void displayChanger(){
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
