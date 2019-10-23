package application;

import application.scenes.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader  = new FXMLLoader(getClass().getResource("/resources/scenes/Menu.fxml"));
        Parent root = loader.load();
        MenuController menuController = loader.getController();
        menuController.setStage(primaryStage);

        primaryStage.setTitle("VARpedia");
        primaryStage.setScene(new Scene(root, 1000 , 750));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
