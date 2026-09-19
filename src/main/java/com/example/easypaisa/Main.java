package com.example.easypaisa;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setWidth(400);
        stage.setHeight(640);
        stage.setResizable(true);
        stage.setTitle("EasyPaisa");
        new Signup().show(stage);
    }
    public static void main(String[] args) { launch(args); }
}
