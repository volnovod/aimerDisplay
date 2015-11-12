package com.displays.view;

import com.displays.cams.Cams;
import com.displays.hickvisonrequests.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by victor on 09.10.15.
 */
public class Controller {

    private Scene nextScene;

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

    private Stage stage;

    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    @FXML
    private Path path;

    public Pane getVideoPane() {
        return videoPane;
    }

    public void setVideoPane(Pane videoPane) {
        this.videoPane = videoPane;
    }

    @FXML
    private Pane videoPane;

    private DriverController driverController;

    public DriverController getDriverController() {
        return driverController;
    }

    public void setDriverController(DriverController driverController) {
        this.driverController = driverController;
    }



    private double rotateAngle;

    private Request status;
    private MoveRequest moveRequest;
    private ZoomRequest zoomRequest;
    private ContinuousMove continuousMove;
    private HomePositionRequest homePositionRequest;
    private Timer timer;
    private double azimuth;
    /*
    if true = to right
    false = to left
     */
    private boolean rotationSide;

    public void init(){
        player.setPlayerHolder(videoPane);
        player.initialize();
        this.status = new Request();
        this.moveRequest = new MoveRequest();
        this.zoomRequest = new ZoomRequest();
        this.continuousMove = new ContinuousMove();
        this.homePositionRequest = new HomePositionRequest();
        if (ifHickvision()){
            homePositionRequest.start();
        }
        timer = new Timer(30, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.start();
                rotator((azimuth-Double.parseDouble(status.getAzimuth())));
                azimuth = Double.parseDouble(status.getAzimuth());
            }
        });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (ifHickvision()){
            status.start();
        }


    }

    @FXML
    public void displayChanger(){

        stage.setScene(getNextScene());


        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        player.stop();
                        getDriverController().init();
                    }
                });
            }
        }).start();


    }

    @FXML
    public void camsChanger(){
        this.player.camsChanger();
    }

    /**
     *
     * @param old - first element - x, second - y
     * @return new x,y
     */
    public double[] rotationCoordinates(double[] old, double angle){
            return new double[]{(old[0]*Math.cos(Math.toRadians(angle))-old[1]*Math.sin(Math.toRadians(angle))),(old[0]*Math.sin(Math.toRadians(angle))+old[1]*Math.cos(Math.toRadians(angle)))};
    }

    public ObservableList<PathElement> pathChanger(ObservableList<PathElement> elements, double angle){
        MoveTo moveTo = (MoveTo)elements.get(0);
        LineTo lineTofirst = (LineTo)elements.get(1);
        ArcTo arcToBig = (ArcTo)elements.get(2);
        LineTo lineToSecond = (LineTo)elements.get(3);
        ArcTo arcToSmall = (ArcTo)elements.get(4);

        double x = moveTo.getX();
        double y = moveTo.getY();
        double[] res = rotationCoordinates(new double[]{x,y}, angle);

        this.rotateAngle = getRotationAngle(res);



        moveTo.setX(res[0]);
        moveTo.setY(res[1]);

        x = lineTofirst.getX();
        y = lineTofirst.getY();

        res = rotationCoordinates(new double[]{x, y},angle);

        lineTofirst.setX(res[0]);
        lineTofirst.setY(res[1]);

        x = arcToBig.getX();
        y = arcToBig.getY();
        res = rotationCoordinates(new double[]{x, y},angle);

        arcToBig.setX(res[0]);
        arcToBig.setY(res[1]);

        x = lineToSecond.getX();
        y = lineToSecond.getY();
        res = rotationCoordinates(new double[]{x, y}, angle);

        lineToSecond.setX(res[0]);
        lineToSecond.setY(res[1]);

        x = arcToSmall.getX();
        y = arcToSmall.getY();
        res = rotationCoordinates(new double[]{x, y}, angle);

        arcToSmall.setX(res[0]);
        arcToSmall.setY(res[1]);

        elements.remove(0, 5);
        elements.add(0, moveTo);
        elements.add(1, lineTofirst);
        elements.add(2, arcToBig);
        elements.add(3, lineToSecond);
        elements.add(4, arcToSmall);
        return elements;
    }

    public boolean ifHickvision(){
        return player.getCams() == Cams.HICKVISION;
    }

    @FXML
    private void changeFirstDisplay(){
        stage.setScene(getNextScene());
        stage.show();
    }

    @FXML
    protected void stepToRight(){
//        if(ifHickvision()) {
//            new Thread() {
//                public void run() {
//                    Platform.runLater(() -> {
//                        status.start();
//                        moveRequest.setRequest(Double.parseDouble(status.getElevation()) * 10,
//                                ((Double.parseDouble(status.getAzimuth()) + 1) * 10), ((Double.parseDouble(status.getZoom())) * 10));
//                        moveRequest.start();
//                        status.start();
//                        rotator((azimuth - Double.parseDouble(status.getAzimuth())));
//                        azimuth = Double.parseDouble(status.getAzimuth());
//                    });
//                }
//            }.start();
//        }
    }

    public void rotator(double angle){
        ObservableList<PathElement> elements = path.getElements();
        elements = pathChanger(elements, angle);
        path = new Path(elements);
    }

    @FXML
    protected void moveToRight(){
        if(ifHickvision()) {
            new Thread() {
                public void run() {
                    Platform.runLater(() -> {
                        continuousMove.setRequest(40, 0, 0);
                        continuousMove.start();

                    });
                }
            }.start();
        }
    }

    @FXML
    protected void stopMove(){
        if (ifHickvision()) {
            new Thread() {
                public void run() {
                    Platform.runLater(() -> {
                        continuousMove.setRequest(0, 0, 0);
                        continuousMove.start();
                    });

                }
            }.start();

        }
    }

    public double getRotationAngle(double[] xy){
        double x = xy[0];
        double y = xy[1];

        if ( x<0 && y<0){
            return Math.toDegrees(Math.acos((Math.abs(y)/Math.sqrt(Math.pow(y,2) + Math.pow(x,2)))))-20;

        }
        if ( x<0 && y>0){
            return (70 + Math.toDegrees(Math.acos((Math.abs(x)/Math.sqrt(Math.pow(y,2) + Math.pow(x,2))))));
        }
        if (x > 0 && y > 0){
            return (160 + Math.toDegrees(Math.acos((y/Math.sqrt(Math.pow(y,2) + Math.pow(x,2))))));
        }
        if (x > 0 && y < 0){
            return (250 + Math.toDegrees(Math.acos((x/Math.sqrt(Math.pow(y,2) + Math.pow(x,2))))));
        }
        if(x < 0 && y==0){
            return 70;
        }
        if (x > 0 && y==0){
            return 250;
        }
        if(x==0 && y > 0){
            return 160;
        }
        if (x==0 && y<0){
            return 340;
        }
        return 0;
    }


    @FXML
    protected void stepToLeft(){
//        if (ifHickvision()) {
//            new Thread() {
//                public void run() {
//                    Platform.runLater(() -> {
//                        status.start();
//                        moveRequest.setRequest(Double.parseDouble(status.getElevation()) * 10, ((Double.parseDouble(status.getAzimuth()) - 1) * 10), ((Double.parseDouble(status.getZoom())) * 10));
//                        moveRequest.start();
//                        status.start();
//                        rotator((azimuth - Double.parseDouble(status.getAzimuth())));
//                        azimuth = Double.parseDouble(status.getAzimuth());
//                    });
//                }
//            }.start();
//        }
    }

    @FXML
    protected void moveToLeft(){
        if (ifHickvision()) {
            new Thread() {
                public void run() {
                    Platform.runLater(() -> {
                        continuousMove.setRequest(-40, 0, 0);
                        continuousMove.start();
                    });
                }
            }.start();
        }
    }

    @FXML
    protected void stepToDown(){
        if (ifHickvision()) {
            new Thread() {
                public void run() {
                    Platform.runLater(() -> {
                        status.start();
                        moveRequest.setRequest(((Double.parseDouble(status.getElevation())) + 1) * 10, (Double.parseDouble(status.getAzimuth())) * 10, ((Double.parseDouble(status.getZoom()))*10));
                        moveRequest.start();
                    });
                }
            }.start();
        }
    }

    @FXML
    protected void moveToDown(){
        if (ifHickvision()) {
            new Thread() {
                public void run() {
                    Platform.runLater(() -> {
                        continuousMove.setRequest(0, -25, 0);
                        continuousMove.start();
                    });
                }
            }.start();
        }
    }

    @FXML
    protected void stepToUp(){
        if (ifHickvision()) {
            new Thread() {
                public void run() {
                    Platform.runLater(() -> {
                        status.start();
                        moveRequest.setRequest(((Double.parseDouble(status.getElevation())) - 1) * 10, (Double.parseDouble(status.getAzimuth())) * 10, (Double.parseDouble(status.getZoom()))*10);
                        moveRequest.start();
                    });
                }
            }.start();
        }
    }

    @FXML
    protected void moveToUp(){
        if (ifHickvision()) {
            new Thread() {
                public void run() {
                    Platform.runLater(() -> {
                        continuousMove.setRequest(0, 25, 0);
                        continuousMove.start();
                    });
                }
            }.start();
        }
    }

    @FXML
    protected void startzoomadd(){
        if (ifHickvision()){
            new Thread(){
                public void run(){
                    Platform.runLater(() -> {
                        zoomRequest.setRequest(20);
                        zoomRequest.start();
                    });
                }
            }.start();
        }
    }

    @FXML
    protected void stopzoomadd(){
        if (ifHickvision()){
            new Thread(){
                public void run(){
                    Platform.runLater(() -> {
                        zoomRequest.setRequest(0);
                        zoomRequest.start();
                    });
                }
            }.start();
        }
    }

    @FXML
    protected void startzoomsub(){
        if (ifHickvision()){
            new Thread(){
                public void run(){
                    Platform.runLater(() -> {
                        zoomRequest.setRequest(-20);
                        zoomRequest.start();
                    });
                }
            }.start();
        }
    }

    @FXML
    protected void stopzoomsub(){
        if (ifHickvision()){
            new Thread(){
                public void run(){
                    Platform.runLater(() -> {
                        zoomRequest.setRequest(0);
                        zoomRequest.start();
                    });
                }
            }.start();
        }
    }



}
