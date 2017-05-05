/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guitartuner;

import com.synthbot.jasiohost.AsioDriverListener;
import com.synthbot.jasiohost.AsioChannel;
import com.synthbot.jasiohost.AsioDriver;
import com.synthbot.jasiohost.AsioDriverState;

import java.net.URL;
import java.util.HashSet;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author Tomáš
 */
public class ToneGenerator implements Initializable, AsioDriverListener {
	
    private static final long serialVersionUID = 1L;

    private AsioDriver asioDriver;
    private Set<AsioChannel> activeChannels;
    private double referentialFrequency;
    private double frequency;
    private guitarTones lastPlayedTone;
    private int sampleIndex;
    private int bufferSize;
    private double sampleRate;
    private float[] output;

    public enum guitarTones{
        E2, A2, D3, G3, B3, E4
    }

    public ToneGenerator() {
            this.activeChannels = new HashSet<AsioChannel>();
            referentialFrequency = 440.0; // Hz
            frequency = 110.0;
            lastPlayedTone = guitarTones.A2;
    }

    public void play() {		
        System.out.println(AsioDriver.getDriverNames());
        if (asioDriver == null) {
            //System.out.println(driverComboBox.getValue().toString());
            //asioDriver = AsioDriver.getDriver(driverComboBox.getValue().toString());
            asioDriver = AsioDriver.getDriver("ASIO4ALL v2");
            asioDriver.addAsioDriverListener(this);
            activeChannels.add(asioDriver.getChannelOutput(0));
            activeChannels.add(asioDriver.getChannelOutput(1));
            sampleIndex = 0;
            bufferSize = asioDriver.getBufferPreferredSize();
            sampleRate = asioDriver.getSampleRate();
            output = new float[bufferSize];
            asioDriver.createBuffers(activeChannels);
            asioDriver.start();
        }		
    }

    public void playE2(){
        setToneTo(guitarTones.E2);
        this.play();
    }        
    public void playA2(){
        setToneTo(guitarTones.A2);
        this.play();
    }
    public void playD3(){
        setToneTo(guitarTones.D3);
        this.play();
    }
    public void playG3(){
        setToneTo(guitarTones.G3);
        this.play();
    }
    public void playB3(){
        setToneTo(guitarTones.B3);
        this.play();
    }
    public void playE4(){
        setToneTo(guitarTones.E4);
        this.play();
    }

    public void stop() {
        if (asioDriver != null) {
            asioDriver.shutdownAndUnloadDriver();
            activeChannels.clear();
            asioDriver = null;
        }
    }

    public void openSettings() {
        System.out.println("open settings()");
        if (asioDriver != null && asioDriver.getCurrentState().ordinal() >= AsioDriverState.INITIALIZED.ordinal()) {
            asioDriver.openControlPanel();          
        }
    }

    /*public void shutdown() {
            System.out.println("shutdown()");
    if (asioDriver != null) {
                    asioDriver.shutdownAndUnloadDriver();
                    activeChannels.clear();
                    asioDriver = null;
    }
    }*/

    @Override
    public void initialize(URL url, ResourceBundle rb) {
            // TODO
    }	

    @Override
    public void bufferSwitch(long systemTime, long samplePosition, Set<AsioChannel> channels) {
            for (int i = 0; i < bufferSize; i++, sampleIndex++) {
              output[i] = (float) Math.sin(2 * Math.PI * sampleIndex * frequency / sampleRate);
            }
            channels.forEach((channelInfo) -> {
                    channelInfo.write(output);
            });
            // or equivalently:
//		for (AsioChannel channelInfo : channels) {
//			channelInfo.write(output);
//		}
    }

    @Override
    public void bufferSizeChanged(int bufferSize) {
            System.out.println("bufferSizeChanged() callback received.");
    }

    @Override
    public void latenciesChanged(int inputLatency, int outputLatency) {
            System.out.println("latenciesChanged() callback received.");
    }

    @Override
    public void resetRequest() {
    /*
     * This thread will attempt to shut down the ASIO driver. However, it will
     * block on the AsioDriver object at least until the current method has returned.
     */
            new Thread() {
              @Override
              public void run() {
                    System.out.println("resetRequest() callback received. Returning driver to INITIALIZED state.");
                    asioDriver.returnToState(AsioDriverState.INITIALIZED);
              }
            }.start();
    }

    @Override
    public void resyncRequest() {
            System.out.println("resyncRequest() callback received.");
    }

    @Override
    public void sampleRateDidChange(double sampleRate) {
            System.out.println("sampleRateDidChange() callback received.");
    }

    public void setToneTo(guitarTones tone){
        lastPlayedTone = tone;
        int diffOctaves;
        int diffSemitones;
        switch (tone){
            case E2:                    
                diffOctaves = 2;
                diffSemitones = 5;
                frequency = referentialFrequency/(Math.pow(Math.pow(2, 1.0 / 12),12*diffOctaves+diffSemitones));//82.41;
                //System.out.println("82.41 vs "+frequency);
                break;
            case A2:
                diffOctaves = 2;
                diffSemitones = 0;
                frequency = (referentialFrequency-diffSemitones*Math.pow(2, 1.0 / 12))/(Math.pow(Math.pow(2, 1.0 / 12),12*diffOctaves+diffSemitones));//110;
                //System.out.println("110 vs "+frequency);
                break;
            case D3:
                diffOctaves = 1;
                diffSemitones = 7;
                frequency = (referentialFrequency-diffSemitones*Math.pow(2, 1.0 / 12))/(Math.pow(Math.pow(2, 1.0 / 12),12*diffOctaves+diffSemitones));//146.8;
                //System.out.println("146.8 vs "+frequency);
                break;
            case G3:
                diffOctaves = 1;
                diffSemitones = 2;
                frequency = (referentialFrequency-diffSemitones*Math.pow(2, 1.0 / 12))/(Math.pow(Math.pow(2, 1.0 / 12),12*diffOctaves+diffSemitones));//196;
                //System.out.println("196 vs "+frequency);
                break;
            case B3:
                diffOctaves = 0;
                diffSemitones = 10;
                frequency = (referentialFrequency-diffSemitones*Math.pow(2, 1.0 / 12))/(Math.pow(Math.pow(2, 1.0 / 12),12*diffOctaves+diffSemitones));//246.9;
                //System.out.println("246.9 vs "+frequency);
                break;
            case E4:
                diffOctaves = 0;
                diffSemitones = 5;
                frequency = (referentialFrequency-diffSemitones*Math.pow(2, 1.0 / 12))/(Math.pow(Math.pow(2, 1.0 / 12),12*diffOctaves+diffSemitones));//329.6;
                //System.out.println("329.6 vs "+frequency);
                break;
            default:
                frequency = 110;    
        }
    }

    public void setFrequency(double newValue){
        this.frequency = newValue;
    }

    public double getFrequency(){
        return this.frequency;
    }

    public void setRefFrequency(double newValue){
        this.referentialFrequency = newValue;
    }
    public double getRefFrequency(){
        return this.referentialFrequency;
    }
}
