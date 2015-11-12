package com.displays.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by victor on 10.11.15.
 */
public class StartController {

    private Stage stage;
    private Scene nextScene;
    private SysController controller;
    private Player player;

    @FXML
    private RadioButton webcamButton;

    @FXML
    private RadioButton othercamButton;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField ipaddressField;

    public SysController getController() {
        return controller;
    }

    public void setController(SysController controller) {
        this.controller = controller;
    }

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

    public String getCamIpAddress() {
        return camIpAddress;
    }

    public void setCamIpAddress(String camIpAddress) {
        this.camIpAddress = camIpAddress;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private String camIpAddress;

    public void closeApplication(){
        Platform.exit();
        System.exit(0);
    }

    public void gotoFirstScene(){
        if(this.webcamButton.isSelected()){
            setCamIpAddress("v4l2:///dev/video0");
            player.setUrl(getCamIpAddress());
            displayChanger();
            return;
        }
        if(this.othercamButton.isSelected()){
            String address = this.ipaddressField.getText();
            if (address.length()!=0){
                setCamIpAddress(address);
                player.setUrl(getCamIpAddress());
                displayChanger();
                return;
            } else{
                this.errorLabel.setText("Некоректна адреса");
                return;
            }
        }

    }

    public void displayChanger(){
        getController().setCamIpAddress(this.getCamIpAddress());
        getController().initialize();
        getStage().setScene(getNextScene());
    }
}
