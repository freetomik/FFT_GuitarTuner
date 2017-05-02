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
import com.synthbot.jasiohost.AsioException;
import java.util.Set;
import org.jtransforms.fft.DoubleFFT_1D;

/**
 *
 * @author Tomáš
 */
public class ToneRecognizer implements AsioDriverListener {


    private AsioDriver asioDriver;
    private Set<AsioChannel> activeChannels;
    private int[] index;
    private int bufferSize;
    private double sampleRate;
    private float[] output;
    private DoubleFFT_1D fft;
    private double[][] fftBuffer;
    private static int fftBufferSize;
    private int bufferCount;
    private AsioDriverListener host;
    private boolean runRecognizer;
    public static double thresholdValue;
    GUIController controller;
    
   
	// TODO: add constructor


	@Override
	public void sampleRateDidChange(double sampleRate) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void resetRequest() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void resyncRequest() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void bufferSizeChanged(int bufferSize) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void latenciesChanged(int inputLatency, int outputLatency) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void bufferSwitch(long sampleTime, long samplePosition, Set<AsioChannel> activeChannels) {
        for (AsioChannel channelInfo : activeChannels) {
            if(runRecognizer){
                if (!channelInfo.isInput()){
//                    if(controller.canPlay()){
                        channelInfo.write(output);
//                    }
                }
                else{
                    if(channelInfo.getChannelIndex()==0){
                        channelInfo.read(output);
                        for(int i=0;i<bufferSize; i++){
                            for(int j=0;j<bufferCount;j++){
                                if (index[j]==fftBufferSize)
                                    break;
                            }
                            for(int j=0;j<bufferCount;j++){
                                fftBuffer[j][index[j]] = output[i];
                            }
                            for(int j=0;j<bufferCount;j++){
                                index[j]++;
                            }
                        }
                        for(int i=0;i<bufferCount;i++){
                            if (index[i]== fftBufferSize){
                                fftBuffer[i] = applyHannWindow(fftBuffer[i]);
                                fft.realForward(fftBuffer[i]);
                                double[] fftData = fftAbs(fftBuffer[i]);                         
								// TODO: 
								// double freq = hps(fftData); 
								// ^^^ harmonic product spectrum - get base freq from spectrum by examining peaks
								// controller.updateFreqLabel(freq);
                                index[i]=0;
                            }
                        }
                    }
                }
            }
            else{
                if (!channelInfo.isInput()){
                    for(int i=0;i<output.length;i++)
                        output[i] = 0;
                    channelInfo.write(output);
                }
            }
        }
	}
	

    private static double[] fftAbs(double[] buffer){
        double[] fftAbs = new double[fftBufferSize/2]; 
        for(int i=0;i<fftBufferSize/2;i++){
            double re = buffer[2*i];
            double im = buffer[2*i+1];
            fftAbs[i] = Math.sqrt(re*re+im*im);
        }
        return fftAbs;
    }
    
    public double[] applyHannWindow(double[] input){
        double[] out = new double[fftBufferSize];
        for (int i = 0; i < fftBufferSize; i++) {
            double mul = 0.5 * (1 - Math.cos(2*Math.PI*i/fftBufferSize-1));
            out[i] = mul * input[i];
        }
        return out;
    }
    
    public boolean startAsio(String driver, int refFreq){
        if (asioDriver == null) {
            try{
                asioDriver = AsioDriver.getDriver(driver);
                if (asioDriver.canSampleRate(48000))
                    asioDriver.setSampleRate(48000);
                asioDriver.addAsioDriverListener(host);
                activeChannels.add(asioDriver.getChannelOutput(0));
                activeChannels.add(asioDriver.getChannelOutput(1));

                activeChannels.add(asioDriver.getChannelInput(0));
                activeChannels.add(asioDriver.getChannelInput(1));
                bufferSize = asioDriver.getBufferPreferredSize();
                sampleRate = asioDriver.getSampleRate();
                output = new float[bufferSize];
//                reInitTonesAndChords(refFreq);
                asioDriver.createBuffers(activeChannels);
                asioDriver.start();
                return true;
            }
            catch(AsioException e) {
                System.out.println("No driver available.");
                return false;
            }
        }
        return false;
      }
    
}
