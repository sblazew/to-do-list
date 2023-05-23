//Simple FXML to-do list app in progress, still lot of bugs to fix and needs lots of features to add

package com.example.todolist1;

import datamodel.ToDoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainwindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 550);
        stage.setTitle("To-do list");
        stage.setScene(scene);
        stage.show();
    }
//saves to a file
    @Override
    public void stop() throws Exception {
        try {
            ToDoData.getInstance().saveFile();
        } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
//for autoloading file at start
    @Override
    public void init() throws Exception {
        try {
            ToDoData.getInstance().loadFile();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}