package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import java.io.IOException;

public class StartPageController {
    public TextField addLatency;
    public TextField subLatency;
    public TextField mulLatency;
    public TextField divLatency;
    public TextField ldLatency;
    public TextField stLatency;
    public TextArea program1;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private AppPageController app;
    @FXML
    private void switchToScene2(ActionEvent event) throws IOException {
        int addL= Integer.parseInt(addLatency.getText());
        int subL= Integer.parseInt(subLatency.getText());
        int mulL= Integer.parseInt(mulLatency.getText());
        int divL= Integer.parseInt(divLatency.getText());
        int ldL= Integer.parseInt(ldLatency.getText());
        int stL= Integer.parseInt(stLatency.getText());
        String prog= program1.getText();

        FXMLLoader loader= new FXMLLoader(getClass().getResource("appPage.fxml"));
        root= loader.load();
        root.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        app= loader.getController();
        app.setTomasulo(addL, subL, mulL, divL, ldL, stL, prog);
        stage= (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene= new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.setX(10);
        stage.setY(10);
        stage.show();

    }


}
