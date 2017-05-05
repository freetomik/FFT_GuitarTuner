/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guitartuner;

//import java.net.URL;
import guitartuner.gui.MainPanel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Tomáš
 */
public class GUIController extends Application {

    private Stage stage;
    private ToneGenerator toneGen;
	private ToneRecognizer toneRecg;

	private Text toneFreqText;

    private StringProperty toneFreqStr;
    private Label toneFreqLabel;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        toneGen = new ToneGenerator();
		toneRecg = new ToneRecognizer(this);
        System.out.println("start()");
        ////////////////////////////////////////////////////////////
        /*ObservableList driverList = FXCollections.observableArrayList("ASIO4ALL v2");
        //System.out.println(driverList);
//        this.driverComboBox.getItems().setAll(driverList);
        ComboBox driversCombo = new ComboBox<>(driverList);*/
        

//        HBox h = new HBox();
//        
//        Button btnPlay = new Button();
//        btnPlay.setText("Play");
//        btnPlay.setOnAction(new EventHandler<ActionEvent>() {
//            
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("handleStartButtonAction...");
//                toneGen.play();
//                System.out.println("handleStartButtonAction ended...");
//            }
//        });
//        
//        Button btnStop = new Button();
//        btnStop.setText("Stop");
//        btnStop.setOnAction(new EventHandler<ActionEvent>() {
//            
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("handleStopButtonAction...");
//                toneGen.stop();
//                System.out.println("handleStopButtonAction ended...");
//            }
//        });
//        
//        Button btnControl = new Button();
//        btnControl.setText("Control panel");
//        btnControl.setOnAction(new EventHandler<ActionEvent>() {
//            
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("handleControlButtonAction...");
//                toneGen.openSettings();
//                System.out.println("handleControlButtonAction ended...");
//            }
//        });
//        
//        Button btnPlayE2 = new Button("E2");
//        btnPlayE2.setOnAction(new EventHandler<ActionEvent>() {            
//            @Override
//            public void handle(ActionEvent event) {toneGen.playE2();}});
//        Button btnPlayA2 = new Button("A2");
//        btnPlayA2.setOnAction(new EventHandler<ActionEvent>() {            
//            @Override
//            public void handle(ActionEvent event) {toneGen.playA2();}});
//        Button btnPlayD3 = new Button("D3");
//        btnPlayD3.setOnAction(new EventHandler<ActionEvent>() {            
//            @Override
//            public void handle(ActionEvent event) {toneGen.playD3();}});
//        Button btnPlayG3 = new Button("G3");
//        btnPlayG3.setOnAction(new EventHandler<ActionEvent>() {            
//            @Override
//            public void handle(ActionEvent event) {toneGen.playG3();}});
//        Button btnPlayB3 = new Button("B3");
//        btnPlayB3.setOnAction(new EventHandler<ActionEvent>() {            
//            @Override
//            public void handle(ActionEvent event) {toneGen.playB3();}});
//        Button btnPlayE4 = new Button("E4");
//        btnPlayE4.setOnAction(new EventHandler<ActionEvent>() {            
//            @Override
//            public void handle(ActionEvent event) {toneGen.playE4();}});        
//        
//        StackPane root = new StackPane();
//        //root.getChildren().add(driversCombo);
//        h.getChildren().addAll(btnPlay, btnStop,btnPlayE2, btnPlayA2, btnPlayD3, btnPlayG3, btnPlayB3, btnPlayE4 , btnControl);
//        root.getChildren().add(h);

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
        
        Button btnPlayE2 = new Button("E2");
        btnPlayE2.setOnAction(new EventHandler<ActionEvent>() {            
            @Override
            public void handle(ActionEvent event) {toneGen.playE2();}});
        Button btnPlayA2 = new Button("A2");
        btnPlayA2.setOnAction(new EventHandler<ActionEvent>() {            
            @Override
            public void handle(ActionEvent event) {toneGen.playA2();}});
        Button btnPlayD3 = new Button("D3");
        btnPlayD3.setOnAction(new EventHandler<ActionEvent>() {            
            @Override
            public void handle(ActionEvent event) {toneGen.playD3();}});
        Button btnPlayG3 = new Button("G3");
        btnPlayG3.setOnAction(new EventHandler<ActionEvent>() {            
            @Override
            public void handle(ActionEvent event) {toneGen.playG3();}});
        Button btnPlayB3 = new Button("B3");
        btnPlayB3.setOnAction(new EventHandler<ActionEvent>() {            
            @Override
            public void handle(ActionEvent event) {toneGen.playB3();}});
        Button btnPlayE4 = new Button("E4");
        btnPlayE4.setOnAction(new EventHandler<ActionEvent>() {            
            @Override
            public void handle(ActionEvent event) {toneGen.playE4();}});        
        
        StackPane root = new StackPane();
        //root.getChildren().add(driversCombo);
        h.getChildren().addAll(btnPlay, btnStop,btnPlayE2, btnPlayA2, btnPlayD3, btnPlayG3, btnPlayB3, btnPlayE4 , btnControl);

	HBox h2 = new HBox();
        
        Button btnTune = new Button();
        btnTune.setText("Tune");
        btnTune.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toneRecg.startAsio("ASIO4ALL v2", 440);
            }
        });

        Button btnTuneStop = new Button();
        btnTuneStop.setText("Stop tuning");
        btnTuneStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toneRecg.shutdownDriver();
            }
        });

        h2.getChildren().addAll(btnTune, btnTuneStop);
	h2.setLayoutX(50);
	h2.setLayoutY(50);

//		toneFreqText = new Text();
//		toneFreqText.setText("Hello World!");
//		toneFreqText.setFont(Font.font("Verdana", 20));
//		toneFreqText.setFill(Color.RED);
//		toneFreqText.setLayoutX(50);
//		toneFreqText.setLayoutY(50);

	toneFreqLabel = new Label();
        toneFreqStr = new SimpleStringProperty();
//		toneFreqText.setFill(Color.BLUE);
        toneFreqStr.set("Label");
        toneFreqLabel.textProperty().bind(toneFreqStr);

        root.getChildren().add(h);
        root.getChildren().add(h2);
//        root.getChildren().add(toneFreqText);
        root.getChildren().add(toneFreqLabel);

        ////////////////////////////////////////////////////////////
        MainPanel root = new MainPanel();
        Scene scene = new Scene(root, 550,400);

        this.stage.setScene(scene);
        this.stage.setTitle("Tone generator");
        this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        });
        this.stage.show();
    }

    @Override
    public void stop() {
        System.out.println("stop(): Stage is closing");
    }

    public void updateText(double freq){
        Platform.runLater(() -> {
//			if(freq > 20 && freq < 2000){
//				toneFreqText.setText(String.format("%,.2f", freq)+"Hz");
				toneFreqStr.set(String.format("%,.2f", freq)+"Hz");
//            }
//            else{
//                toneFreqText.setText("out");
//				toneFreqStr.set("none");
//            }    
        }
        );
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
