package syl.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import syl.gui.model.resultModel;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by QYF on 2016/5/6.
 */
public class Result implements Initializable {
    @FXML
    private TextArea resultArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resultArea.textProperty().bind(resultModel.resultProperty());
    }

    public void showResult() throws  Exception{
        Stage stage = new Stage();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/result.fxml")));
        stage.setScene(scene);
        stage.setTitle("Generated OOC");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    public void resultCopy(){
        final Clipboard cpb = Clipboard.getSystemClipboard();
        final ClipboardContent cp = new ClipboardContent();
        cp.putString(resultArea.getText());
        cpb.setContent(cp);
    }

    @FXML
    public void resultClose(ActionEvent e){
        Button but = (Button) e.getSource();
        Stage stage =(Stage) but.getScene().getWindow();
        stage.close();
    }

}
