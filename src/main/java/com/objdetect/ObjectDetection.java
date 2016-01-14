package com.objdetect;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;

/**
 * Created by victor on 13.11.15.
 */
public class ObjectDetection extends Application {
    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            // load the FXML resource
            BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/fxml/ObjRecognition.fxml"));
            // set a whitesmoke background
            root.setStyle("-fx-background-color: whitesmoke;");
            // create and style a scene
            Scene scene = new Scene(root, 800, 600);
//            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            // create the stage with the given title and the previously created
            // scene
            primaryStage.setTitle("Object Detection");
            primaryStage.setScene(scene);
            // show the GUI
            primaryStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        // load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        launch(args);
    }
}
