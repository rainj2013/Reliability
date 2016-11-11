package edu.gdut.domain;

public class AUCBean {
    private double fraud;
    private boolean label;
    private double unFraud;
    private double unC;

    public AUCBean(){}
    public AUCBean(double fraud, double unFraud, boolean label) {
        super();
        this.fraud = fraud;
        this.label = label;
        this.unFraud = unFraud;
    }

    public double getFraud() {
        return fraud;
    }

    public double getUnFraud() {
        return unFraud;
    }

    public double getUnC() {
        return unC;
    }

    public void setFraud(double fraud) {
        this.fraud = fraud;
    }

    public boolean isLabel() {
        return label;
    }

    public void setLabel(boolean label) {
        this.label = label;
    }
}
