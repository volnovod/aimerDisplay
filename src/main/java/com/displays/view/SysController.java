package com.displays.view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by victor on 22.10.15.
 */
public class SysController {

    private String camIpAddress;

    private Stage stage;

    @FXML
    private Path firstGunAzimuth;

    private double angle=0;

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @FXML
    private Path secondGunAzimuth;

    @FXML
    private Pane pitchPane;

    @FXML
    private Pane rolePane;

    @FXML
    private Label pitchLabel;

    @FXML
    private Label roleLabel;

    private Controller controller;

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    boolean side = true;

    public String getCamIpAddress() {
        return camIpAddress;
    }

    public void setCamIpAddress(String camIpAddress) {
        this.camIpAddress = camIpAddress;
    }



    Timer timer = new Timer(100, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(pitchPane.getRotate()<10 && pitchPane.getRotate()>-10 && side){
                pitchPane.setRotate(pitchPane.getRotate()+1);
                rolePane.setRotate(rolePane.getRotate()-1);
            } if (pitchPane.getRotate()==10){
                pitchPane.setRotate(pitchPane.getRotate()-1);
                rolePane.setRotate(rolePane.getRotate()-1);
                side=false;
            } if (pitchPane.getRotate()<10 && pitchPane.getRotate()>-10 && !side){
                pitchPane.setRotate(pitchPane.getRotate()-1);
                rolePane.setRotate(rolePane.getRotate()+1);
            } if (pitchPane.getRotate()==-10){
                side=true;
                pitchPane.setRotate(pitchPane.getRotate()+1);
                rolePane.setRotate(rolePane.getRotate()+1);
            }
        }
    });

    @FXML
    private Label timeLabel;

    @FXML
    private Label dateLabel;


    public void rotator(double angle, Path path){
        ObservableList<PathElement> elements = path.getElements();
        elements = pathChanger(elements, 1);
        path = new javafx.scene.shape.Path(elements);
    }

    public Scene getNextScene() {
        return nextScene;
    }

    public void setTime(){
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        timeLabel.setText(timeFormat.format(date));
        dateLabel.setText(dateFormat.format(date));
    }

    public void updateAngle(){
        pitchLabel.setText(String.valueOf(pitchPane.getRotate()));
        roleLabel.setText(String.valueOf(rolePane.getRotate()));
    }

    public void initialize(){
        timer.start();
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> setTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Timeline angleLine = new Timeline(new KeyFrame(Duration.millis(50),
                ae -> updateAngle()));

        angleLine.setCycleCount(Animation.INDEFINITE);
        angleLine.play();

    }

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
                        getController().init();

                    }
                });
            }
        }).start();

    }



    public double[] rotationCoordinates(double[] old, double angle){
        return new double[]{(old[0]*Math.cos(Math.toRadians(angle))-old[1]*Math.sin(Math.toRadians(angle))),(old[0]*Math.sin(Math.toRadians(angle))+old[1]*Math.cos(Math.toRadians(angle)))};
    }

    public ObservableList<PathElement> pathChanger(ObservableList<PathElement> elements, double angle){
        MoveTo moveTo = (MoveTo)elements.get(0);
        LineTo lineToSecond = (LineTo)elements.get(1);
        LineTo lineToThird = (LineTo)elements.get(2);
        LineTo lineToFourth = (LineTo)elements.get(3);
        LineTo lineToFifth = (LineTo)elements.get(4);
        LineTo lineToSixth = (LineTo)elements.get(5);
        LineTo lineToSeventh = (LineTo)elements.get(6);
        LineTo lineToEighth = (LineTo)elements.get(7);

        double x = moveTo.getX();
        double y = moveTo.getY();
        double[] res = rotationCoordinates(new double[]{x,y}, angle);


        moveTo.setX(res[0]);
        moveTo.setY(res[1]);

        x = lineToSecond.getX();
        y = lineToSecond.getY();

        res = rotationCoordinates(new double[]{x, y}, angle);

        lineToSecond.setX(res[0]);
        lineToSecond.setY(res[1]);

        x = lineToThird.getX();
        y = lineToThird.getY();
        res = rotationCoordinates(new double[]{x, y}, angle);

        lineToThird.setX(res[0]);
        lineToThird.setY(res[1]);

        x = lineToFourth.getX();
        y = lineToFourth.getY();
        res = rotationCoordinates(new double[]{x, y}, angle);

        lineToFourth.setX(res[0]);
        lineToFourth.setY(res[1]);

        x = lineToFifth.getX();
        y = lineToFifth.getY();
        res = rotationCoordinates(new double[]{x, y}, angle);

        lineToFifth.setX(res[0]);
        lineToFifth.setY(res[1]);

        x = lineToSixth.getX();
        y = lineToSixth.getY();
        res = rotationCoordinates(new double[]{x, y}, angle);

        lineToSixth.setX(res[0]);
        lineToSixth.setY(res[1]);

        x = lineToSeventh.getX();
        y = lineToSeventh.getY();
        res = rotationCoordinates(new double[]{x, y}, angle);

        lineToSeventh.setX(res[0]);
        lineToSeventh.setY(res[1]);

        x = lineToEighth.getX();
        y = lineToEighth.getY();
        res = rotationCoordinates(new double[]{x, y}, angle);

        lineToEighth.setX(res[0]);
        lineToEighth.setY(res[1]);

        elements.remove(0, 8);
        elements.add(0, moveTo);
        elements.add(1, lineToSecond);
        elements.add(2, lineToThird);
        elements.add(3, lineToFourth);
        elements.add(4, lineToFifth);
        elements.add(5, lineToSixth);
        elements.add(6, lineToSeventh);
        elements.add(7, lineToEighth);
        return elements;
    }




}
