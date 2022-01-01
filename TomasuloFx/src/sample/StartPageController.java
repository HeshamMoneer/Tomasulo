package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StartPageController {
    public TextField addLatency;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private AppPageController appPageController;
    @FXML
    private void switchToScene2(ActionEvent event) throws IOException {
        String latency= addLatency.getText();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("appPage.fxml"));
        root= loader.load();
        appPageController= loader.getController();
        appPageController.displayLatency(latency);
        stage= (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene= new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.setX(10);
        stage.setY(10);
        stage.show();

    }


}
