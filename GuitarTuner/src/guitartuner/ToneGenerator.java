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
import java.util.ResourceBundle;
import java.util.Set;
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
    
    private final int octaves = 8;
    private final int halftonesPerScale = 12;

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
                break;
            case A2:
                diffOctaves = 2;
                diffSemitones = 0;
                frequency = (referentialFrequency-diffSemitones*Math.pow(2, 1.0 / 12))/(Math.pow(Math.pow(2, 1.0 / 12),12*diffOctaves+diffSemitones));//110;
                break;
            case D3:
                diffOctaves = 1;
                diffSemitones = 7;
                frequency = (referentialFrequency-diffSemitones*Math.pow(2, 1.0 / 12))/(Math.pow(Math.pow(2, 1.0 / 12),12*diffOctaves+diffSemitones));//146.8;
                break;
            case G3:
                diffOctaves = 1;
                diffSemitones = 2;
                frequency = (referentialFrequency-diffSemitones*Math.pow(2, 1.0 / 12))/(Math.pow(Math.pow(2, 1.0 / 12),12*diffOctaves+diffSemitones));//196;
                break;
            case B3:
                diffOctaves = 0;
                diffSemitones = 10;
                frequency = (referentialFrequency-diffSemitones*Math.pow(2, 1.0 / 12))/(Math.pow(Math.pow(2, 1.0 / 12),12*diffOctaves+diffSemitones));//246.9;
                break;
            case E4:
                diffOctaves = 0;
                diffSemitones = 5;
                frequency = (referentialFrequency-diffSemitones*Math.pow(2, 1.0 / 12))/(Math.pow(Math.pow(2, 1.0 / 12),12*diffOctaves+diffSemitones));//329.6;
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
    
    public String[][] getAllTones(){
        String[][] names = new String[octaves][halftonesPerScale];
              
        names[0]= new String[]{"C0","C#0","D0","D#0","E0","F0","F#0","G0","G#0","A0","A#0","B0"};
        names[1]= new String[]{"C1","C#1","D1","D#1","E1","F1","F#1","G1","G#1","A1","A#1","B1"};
        names[2]= new String[]{"C2","C#2","D2","D#2","E2","F2","F#2","G2","G#2","A2","A#2","B2"};
        names[3]= new String[]{"C3","C#3","D3","D#3","E3","F3","F#3","G3","G#3","A3","A#3","B3"};
        names[4]= new String[]{"C4","C#4","D4","D#4","E4","F4","F#4","G4","G#4","A4","A#4","B4"};
        names[5]= new String[]{"C5","C#5","D5","D#5","E5","F5","F#5","G5","G#5","A5","A#5","B5"};
        names[6]= new String[]{"C6","C#6","D6","D#6","E6","F6","F#6","G6","G#6","A6","A#6","B6"};
        names[7]= new String[]{"C7","C#7","D7","D#7","E7","F7","F#7","G7","G#7","A7","A#7","B7"};
        
        return names;
    }
    
    public int[][] getHalftonesDiffsToA4(){
        int[][] count = new int[octaves][halftonesPerScale];
        
        count[0] = new int[]{-57, -56, -55, -54, -53, -52, -51, -50, -49, -48, -47, -46};
        count[1] = new int[]{-45, -44, -43, -42, -41, -40, -39, -38, -37, -36, -35, -34};
        count[2] = new int[]{-33, -32, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22};
        count[3] = new int[]{-21, -20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10};        
        count[4] = new int[]{- 9, - 8, - 7, - 6, - 5, - 4, - 3, - 2, - 1,   0,   1,   2};
        count[5] = new int[]{  3,   4,   5,   6,   7,   8,   9,  10,  11,  12,  13,  14};
        count[6] = new int[]{ 15,  16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26};
        count[7] = new int[]{ 27,  28,  29,  30,  31,  32,  33,  34,  35,  36,  37,  38};
        
        return count;
    }
    
    public double[][] getAllTonesFreqsRelativeToRefFreq(){
        double[][] freqs = new double[octaves][halftonesPerScale];
        int[][] n = getHalftonesDiffsToA4();
        
        for (int octave = 0; octave < octaves; octave++){
            for (int i = 0; i < halftonesPerScale; i++){
                freqs[octave][i] = referentialFrequency*(Math.pow(2, (n[octave][i]/12.0)));
            }
        }        
        return freqs;
    }
    
}
