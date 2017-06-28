/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guitartuner.gui;

import guitartuner.NearestToneInfo;
import guitartuner.ToneGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Matchos
 */
public class TunePane extends StackPane{
    
    private HBox hbox;
    private VBox vboxNearest;
    private VBox vboxRecognized;
    
    private Label labelNearestTone;
    protected Label labelNearestToneName;
    protected Label labelNearestToneFreq;
    
    private Label labelRecognizedTone;
    protected Label labelRecognizedToneFreq;
    protected Label labelDeviation;
    
    public StringProperty propNearestToneName;
    public StringProperty propNearestToneFreq;
    public StringProperty propRecognizedFreq;
    public StringProperty propDeviation;    
    
    private ToneGenerator generator;
    private String[][] allTonesNames;
    private int[][] halftoneDiffs;
    private double[][] allTonesFreqs;
    private double recognizedFreq;
    
    public TunePane(ToneGenerator g){
        generator = g;
        hbox = new HBox(25);
        hbox.setAlignment(Pos.CENTER);
        
        vboxNearest = new VBox(10);
        vboxNearest.setAlignment(Pos.CENTER);
        vboxRecognized = new VBox(10);
        vboxRecognized.setAlignment(Pos.CENTER);
        
        allTonesNames = generator.getAllTones();
        halftoneDiffs = generator.getHalftonesDiffsToA4();
        allTonesFreqs = generator.getAllTonesFreqsRelativeToRefFreq();
        
        propRecognizedFreq = new SimpleStringProperty();
        propRecognizedFreq.set("None");
        propDeviation = new SimpleStringProperty();
        propDeviation.set("None");
        propNearestToneName = new SimpleStringProperty();
        propNearestToneName.set("None");
        propNearestToneFreq = new SimpleStringProperty();
        propNearestToneFreq.set("None");
        
        labelNearestTone = new Label("Nearest tone");
        labelNearestToneName = new Label("NearestToneName");
        labelNearestToneName.textProperty().bind(propNearestToneName);
        labelNearestToneFreq = new Label("NearestToneFreq");
        labelNearestToneFreq.textProperty().bind(propNearestToneFreq);
        
        labelRecognizedTone = new Label("Recognized tone");  
        labelRecognizedToneFreq = new Label("RecognizedToneFreq");
        labelRecognizedToneFreq.textProperty().bind(propRecognizedFreq);
        labelDeviation = new Label("Deviation");
        labelDeviation.textProperty().bind(propDeviation);
        
        vboxNearest.getChildren().addAll(labelNearestTone,labelNearestToneName,labelNearestToneFreq);
        vboxRecognized.getChildren().addAll(labelRecognizedTone,labelRecognizedToneFreq,labelDeviation);
        
        hbox.getChildren().add(vboxNearest);
        hbox.getChildren().add(vboxRecognized);
        getChildren().add(hbox);
    }
    
    public NearestToneInfo findNearestTone(double freq){
        allTonesFreqs = generator.getAllTonesFreqsRelativeToRefFreq();
        double distance = Math.abs(allTonesFreqs[0][0]-freq);
		double distanceInCents = 0;
        int idx1 = 0;
        int idx2 = 0;
        for (int i = 0; i < allTonesFreqs.length; i++){
            for (int j = 0; j < allTonesFreqs[0].length; j++){
                double cdistance = Math.abs(allTonesFreqs[i][j]-freq);
                if (cdistance < distance){
                    idx1 = i;
                    idx2 = j;
                    distance = cdistance;
                }
            }
        }
        boolean positive;
		// positive
        if ((freq - allTonesFreqs[idx1][idx2]) >= 0){
            positive = true;
			// cent is one hundredth of semitone
			// distanceInCents = 100 * distanceInHertzs / semitoneSizeInHertzs;
			distanceInCents = 100 *
				(distance)/(allTonesFreqs[idx1][idx2]*(Math.pow(2, (1/12.0))) - allTonesFreqs[idx1][idx2]);
        }
		// negative
		else {
            positive = false;
			distanceInCents = 100 *
				(distance)/(allTonesFreqs[idx1][idx2] - allTonesFreqs[idx1][idx2]/(Math.pow(2, (1/12.0))));
		}
        NearestToneInfo nearest = new NearestToneInfo(allTonesNames[idx1][idx2], allTonesFreqs[idx1][idx2], distance, distanceInCents, positive, idx1, idx2);
        return nearest;
    }
    
}
