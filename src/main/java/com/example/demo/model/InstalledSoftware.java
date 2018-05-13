package com.example.demo.model;

/**
 * Created by Kamil on 13.05.2018.
 */
public class InstalledSoftware {

    private String name;
    private String version;
    private String locationPath;
    private String publisher;
    private String installDate;
    private String description;

    public InstalledSoftware() {
    }

    public InstalledSoftware(String name, String version, String locationPath, String publisher, String installDate, String description) {
        this.name = name;
        this.version = version;
        this.locationPath = locationPath;
        this.publisher = publisher;
        this.installDate = installDate;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstallDate() {
        return installDate;
    }

    public void setInstallDate(String installDate) {
        this.installDate = installDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLocationPath() {
        return locationPath;
    }

    public void setLocationPath(String locationPath) {
        this.locationPath = locationPath;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
