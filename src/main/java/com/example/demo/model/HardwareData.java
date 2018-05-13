package com.example.demo.model;

import java.util.List;

/**
 * Created by Kamil on 13.05.2018.
 */
public class HardwareData {

    private String ipAddress;
    private String mac;
    private Long foundSoftware;
    private Long recognizedSoftware;
    private Long unknownSoftware;
    private String manufacturer;
    private String system;
    private List<InstalledSoftware> softwareList;

    public HardwareData() {
    }

    public HardwareData(String ipAddress, String mac, String manufacturer, String system) {
        this.ipAddress = ipAddress;
        this.mac = mac;
        this.manufacturer = manufacturer;
        this.system = system;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Long getFoundSoftware() {
        return foundSoftware;
    }

    public void setFoundSoftware(Long foundSoftware) {
        this.foundSoftware = foundSoftware;
    }

    public Long getRecognizedSoftware() {
        return recognizedSoftware;
    }

    public void setRecognizedSoftware(Long recognizedSoftware) {
        this.recognizedSoftware = recognizedSoftware;
    }

    public Long getUnknownSoftware() {
        return unknownSoftware;
    }

    public void setUnknownSoftware(Long unknownSoftware) {
        this.unknownSoftware = unknownSoftware;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public List<InstalledSoftware> getSoftwareList() {
        return softwareList;
    }

    public void setSoftwareList(List<InstalledSoftware> softwareList) {
        this.softwareList = softwareList;
    }
}
