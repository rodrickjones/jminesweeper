package com.rodrickjones.jminesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Boot extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        JMinesweeper jMinesweeper = new JMinesweeper();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/jminesweeper.fxml"));
        loader.setController(jMinesweeper);
        loader.setRoot(jMinesweeper);
        loader.load();
        Scene scene = new Scene(jMinesweeper);

        primaryStage.setTitle("JMinesweeper");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
