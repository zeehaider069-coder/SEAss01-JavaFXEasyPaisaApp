package com.example.calculator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point for the JavaFX Calculator application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        CalculatorUI calculatorUI = new CalculatorUI();
        Scene scene = calculatorUI.createScene();

        primaryStage.setTitle("Calculator");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(340);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
