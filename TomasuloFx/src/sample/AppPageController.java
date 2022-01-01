package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AppPageController {
    @FXML
    Label latencyLabel;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void displayLatency(String latency)
    {
        latencyLabel.setText("Latency: "+latency);
    }
    @FXML
    private void switchToScene1(ActionEvent event) throws IOException {
        root= FXMLLoader.load(getClass().getResource("startPage.fxml"));
        stage= (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene= new Scene(root);
        stage.setScene(scene);
        stage.show();

    }



}
