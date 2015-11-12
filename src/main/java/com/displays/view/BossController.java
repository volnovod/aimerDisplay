package com.displays.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * Created by victor on 03.11.15.
 */
public class BossController {

    @FXML
    private Region region;

    @FXML
    private Pane  regionPane;

    @FXML
    private Pane resizePane;

    @FXML
    private Pane gunpoint;

    @FXML
    private AnchorPane rootPane;

    private Stage stage;

    private SysController sysController;

    private double startX;
    private double startY;
    private double mouseStartX;
    private double mouseStartY;
    private Player player;
    private Scene nextScene;

    @FXML
    private Pane videoPane;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Scene getNextScene() {
        return nextScene;
    }

    public void setNextScene(Scene nextScene) {
        this.nextScene = nextScene;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public SysController getSysController() {
        return sysController;
    }

    public void setSysController(SysController sysController) {
        this.sysController = sysController;
    }

    public void init(){
        player.setPlayerHolder(videoPane);
        player.initialize();
    }

    public void released(MouseEvent event){
        this.startX = this.regionPane.getLayoutX();
        this.startY = this.regionPane.getLayoutY();
    }

    public void pressed(MouseEvent event){
        this.startX = this.regionPane.getLayoutX();
        this.startY = this.regionPane.getLayoutY();
        this.mouseStartX = event.getX();
        this.mouseStartY = event.getY();

    }

    public void dragged(MouseEvent event){
        this.startX += event.getX() - this.mouseStartX;
        this.startY += event.getY() - this.mouseStartY;
        this.mouseStartX = event.getX();
        this.mouseStartY = event.getY();

        this.regionPane.setLayoutX(this.startX);
        this.regionPane.setLayoutY(this.startY);
    }

    public void resizePressed(MouseEvent event){
        this.mouseStartX = event.getX();
        this.mouseStartY = event.getY();

    }

    public void resizeReleased(MouseEvent event){

    }

    public void resizeDragged(MouseEvent event){
        double width = this.regionPane.getWidth();
        double height = this.regionPane.getHeight();

        resizePane(width, height,(width+(event.getX() - this.mouseStartX)), (height+(event.getY() - this.mouseStartY)), event);

    }

    public void resizePane(double oldWidth, double oldHeight, double width, double height ,MouseEvent event){
        if (width <= regionPane.getMaxWidth() && width >= regionPane.getMinWidth()){
            this.regionPane.setPrefWidth(width);
            this.region.setPrefWidth(width);
            this.gunpoint.setLayoutX(width/2-100);
            this.resizePane.setLayoutX(((oldWidth-10)+(event.getX() - this.mouseStartX)));
        }
        if (height <= regionPane.getMaxHeight() && height >= regionPane.getMinHeight()){
            this.regionPane.setPrefHeight(height);
            this.region.setPrefHeight(height);
            this.gunpoint.setLayoutY(height/2-100);
            this.resizePane.setLayoutY(((oldHeight-10)+(event.getY() - this.mouseStartY)));
        }
    }

    public void gunpointPressed(MouseEvent event){
        this.startX = this.gunpoint.getLayoutX();
        this.startY = this.gunpoint.getLayoutY();
        this.mouseStartX = event.getX();
        this.mouseStartY = event.getY();
    }

    public void gunpointReleased(MouseEvent event){

    }

    public void gunpointDragged(MouseEvent event){
        this.startX += event.getX() - this.mouseStartX;
        this.startY += event.getY() - this.mouseStartY;
        this.mouseStartX = event.getX();
        this.mouseStartY = event.getY();

        this.gunpoint.setLayoutX(this.startX);
        this.gunpoint.setLayoutY(this.startY);
    }

    public void moved(MouseEvent event){

    }

    public void upclick(){

    }

    public void downclick(){

    }

    public void rightclick(){

    }

    public void leftclick(){

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
                        getSysController().initialize();
                    }
                });
            }
        }).start();
    }
}
