package com.example.demo;

import com.example.demo.model.HardwareData;
import com.example.demo.model.InstalledSoftware;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Objects;

/**
 * Created by Kamil on 13.05.2018.
 */

@Controller
@EnableAutoConfiguration
public class ApiController {

    String ipAddress;
    StringBuilder macBuilder;

    @RequestMapping("/installedSoftware")
    @ResponseBody
    HardwareData getInstalledSoftware(HttpServletRequest request) throws IOException, InterruptedException {

        HardwareData hardwareData = new HardwareData();

        ipAddress = getClientIp(request);
        byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getByName(ipAddress)).getHardwareAddress();

        macBuilder = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            macBuilder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }

        List<InstalledSoftware> installedSoftwareList = SoftwareRecognizer.getDisplayNameDword();

        Long recognizedSoftwareCount = installedSoftwareList.stream().filter(Objects::nonNull).count();
        Long unknownSoftwareCount = installedSoftwareList.stream().filter(Objects::isNull).count();

        hardwareData.setIpAddress(ipAddress);
        hardwareData.setMac(macBuilder.toString());
        hardwareData.setFoundSoftware((long) installedSoftwareList.size());
        hardwareData.setRecognizedSoftware(recognizedSoftwareCount);
        hardwareData.setUnknownSoftware(unknownSoftwareCount);
        hardwareData.setSoftwareList(installedSoftwareList);
        hardwareData.setSystem(System.getProperty("os.name"));
        hardwareData.setManufacturer(getManufacturer());

        return hardwareData;
    }

    @RequestMapping("/available")
    @ResponseBody
    HardwareData isAvailable(HttpServletRequest request) throws IOException, URISyntaxException, InterruptedException {

        ipAddress = getClientIp(request);
        byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getByName(ipAddress)).getHardwareAddress();


        macBuilder = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            macBuilder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }

        HardwareData hardwareData = new HardwareData();
        hardwareData.setIpAddress(ipAddress);
        hardwareData.setMac(macBuilder.toString());
        hardwareData.setManufacturer(getManufacturer());
        hardwareData.setSystem(System.getProperty("os.name"));

        return hardwareData;
    }

    private static String getClientIp(HttpServletRequest request) {
        return request.getServerName();
    }

    private static String getManufacturer() throws IOException, InterruptedException {
        Process nameProcess = null;
        nameProcess = Runtime.getRuntime().exec("reg query " +
                '"' + "HKEY_LOCAL_MACHINE\\HARDWARE\\DESCRIPTION\\System\\BIOS" + "\" /v SystemProductName");

        SoftwareRecognizer.StreamReader nameReader = new SoftwareRecognizer.StreamReader(nameProcess.getInputStream());
        nameReader.start();
        nameProcess.waitFor();
        nameReader.join();

        String[] name = nameReader.getResult().split("REG_SZ");

        return name[name.length -1].trim();
    }
}
