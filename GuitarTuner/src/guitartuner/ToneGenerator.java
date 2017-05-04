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
        private double frequency;
	private int sampleIndex;
	private int bufferSize;
	private double sampleRate;
	private float[] output;

        public enum guitarTones{
            E2, A2, D3, G3, B3, E4
        }
        
	public ToneGenerator() {
		System.out.println("ToneGeneratorController constructor");
		this.activeChannels = new HashSet<AsioChannel>();
                frequency = 440.0; // Hz
//		ObservableList<String> driverList =
//			FXCollections.observableList(AsioDriver.getDriverNames());
	}

	public void play() {
            System.out.println("play()");
		
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
            System.out.println("stop()");
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
            switch (tone){
                case E2:
                    frequency = 82.41*4;
                    break;
                case A2:
                    frequency = 110*4;
                    break;
                case D3:
                    frequency = 146.8*4;
                    break;
                case G3:
                    frequency = 196*4;
                    break;
                case B3:
                    frequency = 246.9*4;
                    break;
                case E4:
                    frequency = 329.6*4;
                    break;
                default:
                    frequency = 110*4;    
            }
        }
}
