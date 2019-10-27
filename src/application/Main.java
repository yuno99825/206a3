package application;

import application.scenes.menu.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader  = new FXMLLoader(getClass().getResource("/resources/scenes/menu/Menu.fxml"));
        Parent root = loader.load();
        MenuController menuController = loader.getController();
        menuController.setStage(primaryStage);

        primaryStage.setTitle("VARpedia");
        Scene scene = new Scene(root, PrimaryScene.APP_WIDTH , PrimaryScene.APP_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
