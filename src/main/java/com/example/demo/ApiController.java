package com.example.demo;

import com.example.demo.model.HardwareData;
import com.example.demo.model.InstalledSoftware;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.*;
import java.util.List;
import java.util.Map;
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
    HardwareData getInstalledSoftware(HttpServletRequest request) throws UnknownHostException, SocketException {

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

        return hardwareData;
    }

    @RequestMapping("/available")
    @ResponseBody
    HardwareData isAvailable(HttpServletRequest request) throws UnknownHostException, SocketException, URISyntaxException {

        ipAddress = getClientIp(request);
        byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getByName(ipAddress)).getHardwareAddress();


        macBuilder = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            macBuilder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }

        HardwareData hardwareData = new HardwareData();
        hardwareData.setIpAddress(ipAddress);
        hardwareData.setMac(macBuilder.toString());
        hardwareData.setManufacturer("");
        hardwareData.setSystem(System.getProperty("os.name"));

        return hardwareData;
    }

    private static String getClientIp(HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }
}
