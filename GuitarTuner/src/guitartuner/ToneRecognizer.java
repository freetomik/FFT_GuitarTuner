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
import guitartuner.gui.MainPanel;
import java.util.HashSet;
import java.util.Set;
import org.jtransforms.fft.DoubleFFT_1D;

/**
 *
 * @author Tomáš
 */
public class ToneRecognizer implements AsioDriverListener {


	// taken from Bachelor thesis at https://www.vutbr.cz/studium/zaverecne-prace?zp_id=88462
    private AsioDriver asioDriver;
    private Set<AsioChannel> activeChannels;
    private int[] index;
    private int bufferSize;
    private double sampleRate;
    private float[] output;
    private double[] outputTest;
    private DoubleFFT_1D fft;
    private double[][] fftBuffer;
    private static int fftBufferSize;
    private int bufferCount;
    private AsioDriverListener host;
    private boolean runRecognizer;
    public static double thresholdValue;
    MainPanel controller;
    
	private int sampleIndex;
	private double sinusFreq;
   
	public ToneRecognizer(MainPanel mainPanel) {
        host = this;
        controller = mainPanel;
        runRecognizer = true;
        bufferCount = 1;
        activeChannels = new HashSet<AsioChannel>();
        fftBufferSize = 16384;
        fft = new DoubleFFT_1D(fftBufferSize);
        fftBuffer = new double[bufferCount] [fftBufferSize];
        index = new int[bufferCount];
        for(int i=0;i<bufferCount;i++){
            index[i]=i*fftBufferSize/bufferCount;
        }
	}


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

	// taken from Bachelor thesis at https://www.vutbr.cz/studium/zaverecne-prace?zp_id=88462
	@Override
	public void bufferSwitch(long sampleTime, long samplePosition, Set<AsioChannel> activeChannels) {
        for (AsioChannel channelInfo : activeChannels) {
			if(channelInfo.isInput()){
				channelInfo.read(output);
				// circular buffer implementation
				for(int i=0;i<bufferSize; i++){
					for(int j=0;j<bufferCount;j++){ //this for loop is unneccesary IMO
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

						int baseFrequencyIndex = getBaseFrequencyIndex(fftData);
//							int baseFrequencyIndex = getBaseFrequencyIndexHPS(fftData);

						double baseFrequency = getFrequencyForIndex(baseFrequencyIndex, fftData.length, (int)sampleRate);
						baseFrequency /= 2;		// only one half of spectrum is examined 
						controller.updateText(baseFrequency);

						index[i]=0;
					}
				}
			}
        }
	}

	// taken from Bachelor thesis at https://www.vutbr.cz/studium/zaverecne-prace?zp_id=88462
    private static double[] fftAbs(double[] buffer){
        double[] fftAbs = new double[fftBufferSize/2]; 	// due to symmetry, fft.realForward() computes only 1.half
        for(int i=0;i<fftAbs.length;i++){
            double re = buffer[2*i];
            double im = buffer[2*i+1];
            fftAbs[i] = Math.sqrt(re*re+im*im);
        }
        return fftAbs;
    }
    
	// taken from Bachelor thesis at https://www.vutbr.cz/studium/zaverecne-prace?zp_id=88462
    public double[] applyHannWindow(double[] input){
        double[] out = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            double mul = 0.5 * (1 - Math.cos(2*Math.PI*i/input.length-1));
            out[i] = mul * input[i];
        }
        return out;
    }

	// Harmonic Product Spectrum method of finding fundamental frequency of tone
	// from thesis FFT Guitar Tuner by Jeff Wang and Kay-Won Chang
	// and http://musicweb.ucsd.edu/~trsmyth/analysis/Harmonic_Product_Spectrum.html
	private int getBaseFrequencyIndexHPS(double[] spectrum) {
		double maxVal = Double.NEGATIVE_INFINITY;
		int maxInd = 0;
		int limit = spectrum.length / 3;
		for(int i = 0; i < limit; i++) {
			// product does not work correctly, gives integer multiplies of base frequency (harmonics)
			// product of base freq and 2 harmonics
//			double product = spectrum[i] * spectrum[2*i] * spectrum[3*i];
			// sum works better than product
			double sum = spectrum[i] + spectrum[2*i] + spectrum[3*i];
			if(maxVal < sum) {
				maxVal = spectrum[i];
				maxInd = i;
			}
		}
		return maxInd;
	}

	private int getBaseFrequencyIndex(double[] spectrum) {
		double maxVal = Double.NEGATIVE_INFINITY;
		int maxInd = 0;
		for(int i = 0; i < spectrum.length; i++) {
			if(maxVal < spectrum[i]) {
				maxVal = spectrum[i];
				maxInd = i;
			}
		}
		// Interpolate (https://gist.github.com/akuehntopf/4da9bced2cb88cfa2d19#file-hps-java-L144)
		// not necessary, does not help, gives the same results
//		double mid = spectrum[maxInd];
//		double left  = spectrum[maxInd- 1];
//		double right = spectrum[maxInd + 1];
//		double shift = 0.5f*(right-left) / ( 2.0f*mid - left - right );
//		maxInd = (int) Math.round(maxInd + shift);
		// maybe useful can be quadratic interpolation:
		// http://musicweb.ucsd.edu/~trsmyth/analysis/Quadratic_interpolation.html
		return maxInd;
	}

	// taken from https://gist.github.com/akuehntopf/4da9bced2cb88cfa2d19, author Andreas Kühntopf
	private float getFrequencyForIndex(int index, int size, int rate) {
		return (float)index * (float)rate / (float)size;
}
    
	// taken from Bachelor thesis at https://www.vutbr.cz/studium/zaverecne-prace?zp_id=88462
    public boolean startAsio(String driver, int refFreq){
        if (asioDriver == null) {
            try{
                asioDriver = AsioDriver.getDriver(driver);
                // the lower sample rate, the better tuning accuracy, but longer time to fill the buffer
                // trying to set the lowest possible, 44100 is usually default
                if (asioDriver.canSampleRate(32000)) asioDriver.setSampleRate(32000);
                if (asioDriver.canSampleRate(22050)) asioDriver.setSampleRate(22050);
                if (asioDriver.canSampleRate(16000)) asioDriver.setSampleRate(16000);
                if (asioDriver.canSampleRate(11025)) asioDriver.setSampleRate(11025);
                if (asioDriver.canSampleRate(8000)) asioDriver.setSampleRate(8000);
                asioDriver.addAsioDriverListener(host);
                activeChannels.add(asioDriver.getChannelOutput(0));
                activeChannels.add(asioDriver.getChannelOutput(1));

                activeChannels.add(asioDriver.getChannelInput(0));
                activeChannels.add(asioDriver.getChannelInput(1));
                bufferSize = asioDriver.getBufferPreferredSize();  // usually 512 by default
                sampleRate = asioDriver.getSampleRate();           // usually 44100.0 by default
                output = new float[bufferSize];
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
    
    public void shutdownDriver(){
        if (asioDriver != null) {
                asioDriver.shutdownAndUnloadDriver();
                asioDriver = null;
            }
    }
    
    public void openAsioSettings(){
        if (asioDriver != null && 
            asioDriver.getCurrentState().ordinal() >= AsioDriverState.INITIALIZED.ordinal()) {
            asioDriver.openControlPanel();          
        }
    }
    
}
