package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("startPage.fxml"));
        root.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        primaryStage.setTitle("Hello World");
        Scene scene=new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setX(10);
        primaryStage.setY(10);
        primaryStage.show();
//        panel.getStyleClass().add("panel-primary");                            //(2)
//        content.setPadding(new Insets(20));
//        Button button = new Button("Hello BootstrapFX");
//        button.getStyleClass().setAll("btn","btn-danger");                     //(2)
//        content.setCenter(button);
//        panel.setBody(content);
//        Text text= new Text("Hi champion");
//        text.setX(50);
//        text.setY(50);
//        root.getChildren().add(text);
//        Scene scene = new Scene(root, 600,600, Color.LIGHTSKYBLUE);
//        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());       //(3)
//        primaryStage.setTitle("Tomasulo");
//        primaryStage.setScene(scene);
//        primaryStage.sizeToScene();
//        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
