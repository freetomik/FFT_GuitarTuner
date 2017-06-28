/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guitartuner;

/**
 *
 * @author Matchos
 */
public class NearestToneInfo {
    private String name;
    private double freq;
    private double deviation;
    private double deviationInCents;
    private boolean isDeviationPositive;
    private int idx1; // octave
    private int idx2; // halftoneDiff
    
    public NearestToneInfo(String name, double f, double d, double dc, boolean isPositive,int i1, int i2){
        this.name = name;
        this.freq = f;
        this.deviation = d;
        this.deviationInCents = dc;
        this.isDeviationPositive = isPositive;
        this.idx1 = i1;
        this.idx2 = i2;
    }
    
    public String getName(){
        return this.name;
    }
    
    public double getFreq(){
        return this.freq;
    }
    
    public double getDeviation(){
        return this.deviation;
    }
    
    public double getDeviationInCents(){
        return this.deviationInCents;
    }
    
    public boolean isDeviationPositive(){
        return this.isDeviationPositive;
    }
    
    public int getIndex1(){
        return this.idx1;
    }
    
    public int getIndex2(){
        return this.idx2;
    }
}
