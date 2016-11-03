package edu.gdut.domain;

public class AUCBean {
    private double fraud;
    private boolean lable;

    public AUCBean(double fraud, boolean lable) {
        super();
        this.fraud = fraud;
        this.lable = lable;
    }

    public double getFraud() {
        return fraud;
    }

    public void setFraud(double fraud) {
        this.fraud = fraud;
    }

    public boolean isLable() {
        return lable;
    }

    public void setLable(boolean lable) {
        this.lable = lable;
    }
}
