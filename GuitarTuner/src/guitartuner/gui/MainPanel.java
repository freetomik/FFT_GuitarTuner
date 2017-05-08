/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guitartuner.gui;

import guitartuner.NearestToneInfo;
import guitartuner.ToneGenerator;
import guitartuner.ToneRecognizer;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Matchos
 */
public class MainPanel extends StackPane{
    
    private BorderPane pane;
    private HBox topHB;
    private HBox leftHB;
    private VBox leftVB;
    private HBox bottomHB;
    private VBox bottomVB;
    private TunePane tunePane;
    
    private Slider sliderFreq;
    
    private Button btnStop;
    private Button btnPlay;
    private Button btnControl;
    private Button btnPlayE2;
    private Button btnPlayA2;
    private Button btnPlayD3;
    private Button btnPlayG3;
    private Button btnPlayB3;
    private Button btnPlayE4;
    
    private Label labelTitle;
    private Label labelTones;
    private Label labelFreq;
    private Label labelFreqVal;
    
    private ToneRecognizer toneRecg;
    private Text toneFreqText;
    private StringProperty toneFreqStr;
    private Label toneFreqLabel;
    
    private ToneGenerator toneGen;
    
    public MainPanel(){
        
        this.toneGen = new ToneGenerator();
        toneRecg = new ToneRecognizer(this);
        
        pane = new BorderPane();
        topHB = new HBox();
        
        leftVB = new VBox(10);
        leftVB.setPadding(new Insets(10,10,0,10));
        
        leftHB = new HBox(10);
        
        tunePane = new TunePane(toneGen);
        
        bottomHB = new HBox(20);
        bottomHB.setAlignment(Pos.CENTER);
        bottomVB = new VBox();
        bottomVB.setAlignment(Pos.CENTER);
        bottomVB.setPadding(new Insets(0,10,10,10));//TOP RIGHT BOTTOM LEFT
        
        labelTitle = new Label("Guitar tuner");
        labelTitle.setFont(Font.font(20));        
        labelTones = new Label("Referential\n    tones");       
        labelFreq = new Label("Referential\n frequency");
        labelFreqVal = new Label("");
        labelFreqVal.setPadding(new Insets(0,0,10,0));
        
        sliderFreq = new Slider();
        sliderFreq.setMin(400);
        sliderFreq.setMax(500);
        sliderFreq.setValue(440);
        sliderFreq.setShowTickLabels(true);
        sliderFreq.setShowTickMarks(true);
        sliderFreq.setBlockIncrement(5);
        sliderFreq.setMajorTickUnit(50);
        sliderFreq.setMinorTickCount(4);  
        sliderFreq.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                labelFreqVal.setText(String.format("%d Hz", Math.round((Double) newValue)));
                toneGen.setRefFrequency((double)Math.round((Double) newValue));
                //toneGen.play(toneGen.lastPlayedTone); // change playXY methods to play(XY)
            }
        });
        
        labelFreqVal.setText(String.format("%.0f Hz", sliderFreq.getValue()));
        
        btnStop = new Button();
        btnStop.setText("Stop");
        btnStop.setMaxWidth(Double.MAX_VALUE);
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                toneGen.stop();
            }
        });
        
        btnControl = new Button();
        btnControl.setText("Control panel");
        btnControl.setMaxWidth(Double.MAX_VALUE);
        btnControl.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                toneGen.openSettings();
            }
        });
        
        btnPlayE2 = new Button("E2");
        btnPlayE2.setMaxWidth(Double.MAX_VALUE);
        btnPlayE2.setOnAction(new EventHandler<ActionEvent>() {            
            @Override
            public void handle(ActionEvent event) {toneGen.playE2();}});
        btnPlayA2 = new Button("A2");
        btnPlayA2.setMaxWidth(Double.MAX_VALUE);
        btnPlayA2.setOnAction(new EventHandler<ActionEvent>() {            
            @Override
            public void handle(ActionEvent event) {toneGen.playA2();}});
        btnPlayD3 = new Button("D3");
        btnPlayD3.setMaxWidth(Double.MAX_VALUE);
        btnPlayD3.setOnAction(new EventHandler<ActionEvent>() {            
            @Override
            public void handle(ActionEvent event) {toneGen.playD3();}});
        btnPlayG3 = new Button("G3");
        btnPlayG3.setMaxWidth(Double.MAX_VALUE);
        btnPlayG3.setOnAction(new EventHandler<ActionEvent>() {            
            @Override
            public void handle(ActionEvent event) {toneGen.playG3();}});
        btnPlayB3 = new Button("B3");
        btnPlayB3.setMaxWidth(Double.MAX_VALUE);
        btnPlayB3.setOnAction(new EventHandler<ActionEvent>() {            
            @Override
            public void handle(ActionEvent event) {toneGen.playB3();}});
        btnPlayE4 = new Button("E4");
        btnPlayE4.setMaxWidth(Double.MAX_VALUE);
        btnPlayE4.setOnAction(new EventHandler<ActionEvent>() {            
            @Override
            public void handle(ActionEvent event) {toneGen.playE4();}});   
        
        //////////////////////////////////////////////////////////
        
        VBox v2 = new VBox(10);
        v2.setAlignment(Pos.CENTER);
        v2.setPadding(new Insets(10,10,10,10));
        
        Label labelTuning = new Label("  Tune\nOptions");
        
        Button btnTune = new Button();
        btnTune.setText("Start tuning");
        btnTune.setMaxWidth(Double.MAX_VALUE);
        btnTune.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toneRecg.startAsio("ASIO4ALL v2", 440);
            }
        });

        Button btnTuneStop = new Button();
        btnTuneStop.setText("Stop tuning");
        btnTuneStop.setMaxWidth(Double.MAX_VALUE);
        btnTuneStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toneRecg.shutdownDriver();
            }
        });

        v2.getChildren().addAll(labelTuning, btnTune, btnTuneStop);
	v2.setLayoutX(50);
	v2.setLayoutY(50);
        
        //////////////////////////////////////////////////////////
        
        topHB.getChildren().addAll(labelTitle);
        topHB.setAlignment(Pos.CENTER);
        
        leftVB.getChildren().addAll(labelTones,btnStop,btnPlayE2, btnPlayA2, btnPlayD3, btnPlayG3, btnPlayB3, btnPlayE4 , btnControl);
        leftVB.setAlignment(Pos.CENTER);        
        leftHB.getChildren().addAll(leftVB);
        leftHB.setAlignment(Pos.CENTER);
        
        bottomHB.getChildren().addAll(labelFreq,sliderFreq, labelFreqVal);
        bottomVB.getChildren().addAll(bottomHB);
        
        pane.setTop(topHB);
        pane.setLeft(leftVB);
        pane.setRight(v2);
        pane.setCenter(tunePane);
        pane.setBottom(bottomVB);
        
        this.getChildren().add(pane);
    }
    
    public void updateText(double freq){
        Platform.runLater(() -> {
                String f = String.format("%.2f", freq)+" Hz";
                tunePane.propRecognizedFreq.set(String.format("%-10s", f));

                NearestToneInfo info = tunePane.findNearestTone(freq);
                String deviation;
                if (info.isDeviationPositive()){
                    deviation = new String("+"+String.format("%.2f", info.getDeviation())+ " Hz");
                }
                else{
                    deviation = new String("-"+String.format("%.2f", info.getDeviation())+ " Hz");
                }
                
                if (info.getDeviation() > 1.1){
                    tunePane.labelDeviation.setStyle("-fx-text-fill: red;-fx-font-size: 20pt;");
                    tunePane.labelNearestToneFreq.setStyle("-fx-text-fill: red;-fx-font-size: 20pt;");
                    tunePane.labelNearestToneName.setStyle("-fx-text-fill: red;-fx-font-size: 20pt;");
                    tunePane.labelRecognizedToneFreq.setStyle("-fx-text-fill: red;-fx-font-size: 20pt;");
                }
                else{
                    tunePane.labelDeviation.setStyle("-fx-text-fill: green;-fx-font-size: 20pt;");
                    tunePane.labelNearestToneFreq.setStyle("-fx-text-fill: green;-fx-font-size: 20pt;");
                    tunePane.labelNearestToneName.setStyle("-fx-text-fill: green;-fx-font-size: 20pt;");
                    tunePane.labelRecognizedToneFreq.setStyle("-fx-text-fill: green;-fx-font-size: 20pt;");
                }

                tunePane.propDeviation.set(deviation);
                tunePane.propNearestToneName.set(info.getName());
                String nearestFreq = String.format("%.2f", info.getFreq())+ " Hz";
                tunePane.propNearestToneFreq.set(nearestFreq);
            }
        );
    }
}
