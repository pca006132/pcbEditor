package syl.gui.controller;
/**
 * Created by QYF on 2016/5/6.
 */

import PcbFormat.SingleOOC;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import syl.gui.model.resultModel;

public class Main extends Application {
    @FXML
    private TextArea mainArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("Worst PCB GUI ever");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @FXML
    private void mainClear(ActionEvent e){
        mainArea.clear();
    }

    @FXML
    private void generate(ActionEvent e) throws Exception{
        String[] command = {};
        String[] output = "";
        SingleOOC OOC = new SingleOOC();

        command = mainArea.getText().split("\\n");
        for (String str:command) {
            OOC.addCommand(str);
        }


        Result result = new Result();
        resultModel.setResult(output);
        result.showResult();
    }

}
