package model;

import java.util.Observable;

/**
 * Parameter contains the relevant data that we get from the parsed response from the
 * server.
 */
public class Parameter extends Observable {

    private String validTime;
    private float t;
    private int tcc_mean;
    private int Wsymb2;

    public Parameter(String validTime_, float t_, int tcc_mean_, int Wsymb2_) {
        this.validTime = validTime_;
        this.t = t_;
        this.tcc_mean = tcc_mean_;
        this.Wsymb2 = Wsymb2_;
    }

    public String getValidTime() {
        return validTime;
    }

    public float getT() {
        return t;
    }

    public int getTcc_mean() {
        return tcc_mean;
    }

    public int getWsymb2() {
        return Wsymb2;
    }
}
