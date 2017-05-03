/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guitartuner;

import com.synthbot.jasiohost.AsioChannel;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tomáš
 */
public class ToneRecognizerTest {
	
	public ToneRecognizerTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of tune method, of class ToneRecognizer.
	 */
	@Test
	public void testTune() {
		System.out.println("tune");
		ToneRecognizer instance = new ToneRecognizer();
		double sampleRates[] = {8000.0, 16000.0, 32000.0, 44100.0, 48000.0};

		for(int s = 0; s < sampleRates.length; s++) {
			System.out.println("sample rate "+sampleRates[s]);
			instance.setSampleRate(sampleRates[s]);

			for (int b = 8; b <= 14; b++) {		// from 256 to 16K
				int fftBufSize = (int)Math.pow(2, b);
				instance.setFFTBufSize(fftBufSize);
				System.out.println("  fft buf size "+fftBufSize);

				double f = 82.41/4;		// low limit of hearing
				while(f < 25000) {		// to high limit of hearing
					instance.setSinFreq(f);
					double result = instance.tune();
					result /= 2;	// I don't know why result is 2x bigger... (or maybe I have 2x bigger sample rate)
					double absDiff = Math.abs(f-result);
					double rel = absDiff / f * 100;
					System.out.println("    ref: "+f+", result: "+result+", abs: "+absDiff+", rel: "+rel+"%");
					f *= 2;
				}

			}

		}

	}

}
