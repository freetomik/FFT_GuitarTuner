/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guitartuner;

//import java.net.URL;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
//import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Tomáš
 */
public class GUIController extends Application {

    private Stage stage;
    //private FXMLLoader fxmlLoader;
    private ToneGenerator toneGen;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        toneGen = new ToneGenerator();
        System.out.println("start()");
        ////////////////////////////////////////////////////////////
        /*ObservableList driverList = FXCollections.observableArrayList("ASIO4ALL v2");
        //System.out.println(driverList);
//        this.driverComboBox.getItems().setAll(driverList);
        ComboBox driversCombo = new ComboBox<>(driverList);*/
        
        HBox h = new HBox();
        
        Button btnPlay = new Button();
        btnPlay.setText("Play");
        btnPlay.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("handleStartButtonAction...");
                toneGen.play();
                System.out.println("handleStartButtonAction ended...");
            }
        });
        
        Button btnStop = new Button();
        btnStop.setText("Stop");
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("handleStopButtonAction...");
                toneGen.stop();
                System.out.println("handleStopButtonAction ended...");
            }
        });
        
        Button btnControl = new Button();
        btnControl.setText("Control panel");
        btnControl.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("handleControlButtonAction...");
                toneGen.openSettings();
                System.out.println("handleControlButtonAction ended...");
            }
        });
        
        StackPane root = new StackPane();
        //root.getChildren().add(driversCombo);
        h.getChildren().addAll(btnPlay, btnStop, btnControl);
        root.getChildren().add(h);
        ////////////////////////////////////////////////////////////

        Scene scene = new Scene(root, 300,250);

        this.stage.setScene(scene);
        this.stage.setTitle("Tone generator");
        this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("handle(): Stage is closing");
                System.exit(0);
            }
        });
        this.stage.show();
    }

    @Override
    public void stop() {
        System.out.println("stop(): Stage is closing");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}