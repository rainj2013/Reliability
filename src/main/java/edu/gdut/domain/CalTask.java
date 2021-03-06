package edu.gdut.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-15 下午10:03
 */
public class CalTask {
    private String id;
    private String resultFile;
    private String dataFile;
    private String remark;
    private String algoName;
    private Timestamp subTime;
    private Timestamp finTime;
    private Integer status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResultFile() {
        return resultFile;
    }

    public void setResultFile(String resultFile) {
        this.resultFile = resultFile;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAlgoName() {
        return algoName;
    }

    public void setAlgoName(String algoName) {
        this.algoName = algoName;
    }

    public Date getSubTime() {
        return subTime;
    }

    public void setSubTime(Timestamp subTime) {
        this.subTime = subTime;
    }

    public Date getFinTime() {
        return finTime;
    }

    public void setFinTime(Timestamp finTime) {
        this.finTime = finTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public CalTask(String id, String resultFile, String dataFile, String remark, String algoName, Timestamp subTime,
                   Timestamp finTime, Integer status) {
        this.id = id;
        this.resultFile = resultFile;
        this.dataFile = dataFile;
        this.remark = remark;
        this.algoName = algoName;
        this.subTime = subTime;
        this.finTime = finTime;
        this.status = status;
    }

    public CalTask(String id, String dataFile, String remark, String algoName, Timestamp subTime, Integer status) {
        this.id = id;
        this.dataFile = dataFile;
        this.remark = remark;
        this.algoName = algoName;
        this.subTime = subTime;
        this.status = status;
    }

    @Override
    public String toString() {
        return "CalTask{" +
                "id='" + id + '\'' +
                ", resultFile='" + resultFile + '\'' +
                ", dataFile='" + dataFile + '\'' +
                ", remark='" + remark + '\'' +
                ", algoName='" + algoName + '\'' +
                ", subTime=" + subTime +
                ", finTime=" + finTime +
                ", status='" + status + '\'' +
                '}';
    }
}
