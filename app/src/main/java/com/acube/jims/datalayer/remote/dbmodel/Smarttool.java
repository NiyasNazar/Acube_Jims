package com.acube.jims.datalayer.remote.dbmodel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.rscja.team.qcom.deviceapi.D;

@Entity(tableName = "SmartTool", indices = @Index(value = {"serialNumber", "ID"}, unique = true))

public class Smarttool implements Comparable<Smarttool> {
    private int DBID;
    private String ID;
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String serialNumber;
    private String imagepath;

    public Smarttool(String ID, String serialNumber, String imagepath) {
        this.ID = ID;
        this.serialNumber = serialNumber;
        this.imagepath = imagepath;
    }

    public Smarttool() {

    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getDBID() {
        return DBID;
    }

    public void setDBID(int DBID) {
        this.DBID = DBID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    @Override
    public int compareTo(Smarttool f) {

        return serialNumber.compareToIgnoreCase(f.serialNumber);

    }
}
